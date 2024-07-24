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
    private HashMap<Uuid, ArrayList<Action>> localTransforms;

    private HashMap<Uuid, ArrayList<Action>> netActions;
    private HashMap<Uuid, ArrayList<Action>> netTransforms;
    private HashMap<Uuid, ArrayList<Action>> netActionBuffer;
    private HashMap<Uuid, ArrayList<Action>> netTransformBuffer;

    private ReentrantLock lock;

    private PhysicsSpace physicsSpace;

    public ObjectManager(Graveborn app) {
        entityMap = new HashMap<Uuid, Entity>();

        localEntitiesNew = new HashMap<Uuid, Entity>();
        localEntitiesDeleted = new HashMap<Uuid, Entity>();

        localActions = new HashMap<Uuid, ArrayList<Action>>();
        localTransforms = new HashMap<Uuid, ArrayList<Action>>();

        netActions = new HashMap<Uuid, ArrayList<Action>>();
        netTransforms = new HashMap<Uuid, ArrayList<Action>>();
        netActionBuffer = new HashMap<Uuid, ArrayList<Action>>();
        netTransformBuffer = new HashMap<Uuid, ArrayList<Action>>();

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

        netTransforms.putAll(netTransformBuffer);
        netTransformBuffer.clear();
        lock.unlock();

        //process netCreate and netDelete actions
        netActions.forEach((uuid, array) -> {
            array.forEach((action) -> {
                if (action instanceof CreateAction) {
                    CreateAction createAction = (CreateAction) action;

                    if (!entityMap.containsKey(uuid)) {
                        entityMap.put(uuid, createAction.getType().build(uuid, this, createAction.getName()));

                        localEntitiesNew.put(uuid, getEntity(uuid));

                        if (getEntity(uuid) instanceof RigEntity) {
                            RigEntity rigEntity = (RigEntity) getEntity(uuid);

                            physicsSpace.add(rigEntity.getRig());
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
        
        entityMap.forEach((uuid, entity) -> {
            if (netTransforms.size() > 0) {
                // process net position
                if (netTransforms.containsKey(uuid)) {
                    ArrayList<Action> array = netTransforms.get(uuid);

                    if(null != array.toArray()[0]) {
                        entity.processAction((Action)array.toArray()[0]);
                    }

                    if (null != array.toArray()[1]) {
                        entity.processAction((Action)array.toArray()[1]);
                    }
                }
                else {
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
        netTransforms.clear();
    }

    public void shutdown() {

    }

    public void submitEntityAction(Uuid uuid, Action action) {
        
        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            entity.processAction(action);

            if (action instanceof MoveAction) {
                if (localTransforms.containsKey(uuid)) {
                    localTransforms.get(uuid).toArray()[0] = action;
                } else {
                    ArrayList<Action> array = new ArrayList<Action>();
                    array.add(0, action);
                    array.add(1, null);

                    localTransforms.put(uuid, array);
                }
            } else if (action instanceof VelocityAction) {
                ArrayList<Action> array = new ArrayList<Action>(2);
                array.add(0, new MoveAction(getEntity(uuid).getPosition()));
                array.add(1, action);

                localTransforms.put(uuid, array);
            } else {
                if (null == localActions.get(uuid)) {
                    localActions.put(uuid, new ArrayList<Action>());
                }
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
        netTransformBuffer.putAll(update.getTransforms());

        lock.unlock();
    }

    public Update getUpdate() {
        Update update = new Update();

        update.addActions(localActions);
        localActions.clear();

        update.addTransforms(localTransforms);
        localTransforms.clear();

        return update;
    }

    public Update getAll() {
        Update update = new Update();

        entityMap.forEach((uuid, entity) -> {
            update.addAction(uuid, new CreateAction(entity.getType(), entity.getName()));

            if (entity instanceof RigEntity) {
                RigEntity rigEntity = (RigEntity) entity;
                update.addTransform(uuid, new MoveAction(rigEntity.getPosition()), new VelocityAction(rigEntity.getVelocity()));
            } else {
                update.addTransform(uuid, new MoveAction(entity.getPosition()), null);
            }
        });

        return update;
    }
}