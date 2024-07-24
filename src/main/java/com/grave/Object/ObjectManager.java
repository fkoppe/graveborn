package com.grave.Object;

import com.grave.Graveborn;
import com.grave.Uuid;
import com.grave.Game.Entities.Entity;
import com.grave.Game.Entities.RigEntity;
import com.grave.Game.Entities.Type;
import com.grave.Object.Actions.Action;
import com.grave.Object.Actions.CreateAction;
import com.grave.Object.Actions.DeleteAction;
import com.grave.Object.Actions.MoveAction;
import com.grave.Object.Actions.VelocityAction;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;
import com.jme3.bullet.BulletAppState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectManager {
    private static final Logger LOGGER = Logger.getLogger(ObjectManager.class.getName());

    private HashMap<Uuid, Entity> entityMap;

    private HashMap<Uuid, Entity> localEntitiesNew;
    private HashMap<Uuid, Entity> localEntitiesDeleted;

    private HashMap<Uuid, ArrayList<Action>> localActions;
    private HashMap<Uuid, Action> localPositions;

    private HashMap<Uuid, ArrayList<Action>> netActions;
    private HashMap<Uuid, Action> netPositions;
    private HashMap<Uuid, ArrayList<Action>> netActionBuffer;
    private HashMap<Uuid, Action> netPositionBuffer;

    private ReentrantLock lock;

    private PhysicsSpace physicsSpace;

    public ObjectManager(Graveborn app) {
        entityMap = new HashMap<Uuid, Entity>();

        localEntitiesNew = new HashMap<Uuid, Entity>();
        localEntitiesDeleted = new HashMap<Uuid, Entity>();

        localActions = new HashMap<Uuid, ArrayList<Action>>();
        localPositions = new HashMap<Uuid, Action>();

        netActions = new HashMap<Uuid, ArrayList<Action>>();
        netPositions = new HashMap<Uuid, Action>();
        netActionBuffer = new HashMap<Uuid, ArrayList<Action>>();
        netPositionBuffer = new HashMap<Uuid, Action>();

        lock = new ReentrantLock();

        BulletAppState bulletAppState = new BulletAppState();
        app.getStateManager().attach(bulletAppState);
        physicsSpace = bulletAppState.getPhysicsSpace();
        physicsSpace.setGravity(Vector3f.ZERO);
    }

    public void init() {

    }

    public void update(float tpf) {
        lock.lock();
        netActions.putAll(netActionBuffer);
        netActionBuffer.clear();

        netPositions.putAll(netPositionBuffer);
        netPositionBuffer.clear();
        lock.unlock();

        //TODO
        System.out.println(entityMap.size());

        netActions.forEach((uuid, array) -> {
            array.forEach((action) -> {
                if (action instanceof CreateAction) {
                    CreateAction createAction = (CreateAction) action;

                    if (!entityMap.containsKey(uuid)) {
                        entityMap.put(uuid, createAction.getType().build(uuid, this, createAction.getName()));

                        localEntitiesNew.put(uuid, getEntity(uuid));

                        if (getEntity(uuid) instanceof RigEntity) {
                            RigEntity rigEntity = (RigEntity) getEntity(uuid);

                            // physicsSpace.add(rigEntity.getRig());
                        }
                    } else {
                        LOGGER.log(Level.FINER, "OM: entity is already known");
                    }
                } else if (action instanceof DeleteAction) {
                    DeleteAction deleteAction = (DeleteAction) action;

                    localEntitiesDeleted.put(uuid, getEntity(uuid));
                }
            });
        });

        //TODO
        System.out.println(entityMap.size());
        
        entityMap.forEach((uuid, entity) -> {
            if (netPositions.size() > 0) {
                System.out.println(netPositions);
                netPositions.get(uuid);
                System.out.println("xxx " + uuid);
                
                // process net position
                if (netPositions.containsKey(uuid)) {
                    System.out.println(netPositions.get(uuid));
                    entity.processAction(netPositions.get(uuid));
                    System.out.println("yes " + uuid);
                }
                else {
                    System.out.println("nope " + uuid);
                }
            }
            
            if(netActions.size() > 0) {
                // process net actions
                if (netActions.containsKey(uuid)) {
                    netActions.get(uuid).forEach((action) -> {
                        entity.processAction(action);
                    });
                }
            }

            entity.onUpdate(tpf);
        });

        netActions.clear();
        netPositions.clear();
    }

    public void shutdown() {

    }

    public void submitEntityAction(Uuid uuid, Action action) {
        
        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            entity.processAction(action);

            if (action instanceof MoveAction) {
                localPositions.put(uuid, action);
            } else if (action instanceof VelocityAction) {
                localPositions.put(uuid, new MoveAction(entity.getPosition()));
            } else {
                localActions.get(uuid).add(action);
            }
        }
    }

    public Uuid createEntity(Type type, String name) {
        Uuid id = new Uuid();
        Entity entity = type.build(id, this, name);

        entityMap.put(id, entity);
        localEntitiesNew.put(id, entity);

        if (null == localActions.get(id)) {
            localActions.put(id, new ArrayList<Action>());
        }
        localActions.get(id).add(new CreateAction(entity.getType(), entity.getName()));

        if (entity instanceof RigEntity) {
            RigEntity rigEntity = (RigEntity) entity;

            physicsSpace.add(rigEntity.getRig());
        }

        entity.onInit();

        return id;
    }

    public void deleteEntity(Uuid uuid) {
        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            entity.onShutdown();

            if (null == localActions.get(uuid)) {
                localActions.put(uuid, new ArrayList<Action>());
            }
            localActions.get(uuid).add(new DeleteAction());

            localEntitiesDeleted.put(uuid, entity);
            entityMap.remove(uuid);
        } else {
            LOGGER.log(Level.WARNING, "OM: unknown entity");
        }
    }

    public Entity getEntity(Uuid uuid) {
        if (entityMap.containsKey(uuid)) {
            return entityMap.get(uuid);
        } else {
            throw new RuntimeException("OM: unknown entity");
        }
    }

    public ArrayList<Entity> queryEntityByName(String name) {
        ArrayList<Entity> found = new ArrayList<Entity>();

        entityMap.forEach((uuid, entity) -> {
            if (entity.getName().equals(name)) {
                found.add(entity);
            }
        });

        return found;
    }
    
    public ArrayList<Entity> queryEntityByClass(Class c) {
        ArrayList<Entity> found = new ArrayList<Entity>();

        entityMap.forEach((uuid, entity) -> {
            if (entity.getClass().equals(c)) {
                found.add(entity);
            }
        });

        return found;
    }

    public HashMap<Uuid, Entity> getLocalEntitiesNew() {
        HashMap<Uuid, Entity> copy = new HashMap<>(localEntitiesNew);

        localEntitiesNew.clear();

        return copy;
    }

    public HashMap<Uuid, Entity> getLocalEntitiesDeleted() {
        HashMap<Uuid, Entity> copy = new HashMap<>(localEntitiesDeleted);

        localEntitiesDeleted.clear();

        return copy;
    }

    public void takeUpdate(Update update) {
        //update.getPositions().forEach((uuid, action) -> {
        //    if(entityMap.containsKey(uuid)) {
        //        if (getEntity(uuid) instanceof Zombie) {
        //            update.getPositions().remove(uuid);
        //        }
        //    }
        //});

        forceUpdate(update);
    }

    public void forceUpdate(Update update) {
        lock.lock();
        netActionBuffer.putAll(update.getActions());

        netPositionBuffer.putAll(update.getPositions());
        lock.unlock();

        LOGGER.log(Level.INFO, "OM: forcing " + update.getActions() + " entities and " + update.getPositions() + " positions");
    }

    public Update getUpdate() {
        Update update = new Update();

        update.addActions(localActions);

        localActions.clear();

        update.addPositions(localPositions);

        localPositions.clear();

        return update;
    }

    public Update getAll() {
        Update update = new Update();

        entityMap.forEach((uuid, entity) -> {
            update.addAction(uuid, new CreateAction(entity.getType(), entity.getName()));
            update.addPosition(uuid, new MoveAction(entity.getPosition()));
        });

        LOGGER.log(Level.INFO, "OM: getAll with " + update.getActions() + "entities and " + update.getPositions() + " entities");

        return update;
    }
}