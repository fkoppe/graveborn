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
    private HashMap<Uuid, Action> localVelocities;

    private HashMap<Uuid, ArrayList<Action>> netActions;
    private HashMap<Uuid, Action> netPositions;
    private HashMap<Uuid, Action> netVelocities;
    private HashMap<Uuid, ArrayList<Action>> netActionBuffer;
    private HashMap<Uuid, Action> netPositionBuffer;
    private HashMap<Uuid, Action> netVelocityBuffer;

    private ReentrantLock lock;

    private PhysicsColisionListener physicsColisionListener;
    private PhysicsSpace physicsSpace;

    private boolean isDominant;

    public ObjectManager(Graveborn app_, boolean isDominant_) {
        entityMap = new HashMap<Uuid, Entity>();

        localEntitiesNew = new HashMap<Uuid, Entity>();
        localEntitiesDeleted = new HashMap<Uuid, Entity>();

        localActions = new HashMap<Uuid, ArrayList<Action>>();
        localPositions = new HashMap<Uuid, Action>();
        localVelocities = new HashMap<Uuid, Action>();

        netActions = new HashMap<Uuid, ArrayList<Action>>();
        netPositions = new HashMap<Uuid, Action>();
        netVelocities = new HashMap<Uuid, Action>();
        netActionBuffer = new HashMap<Uuid, ArrayList<Action>>();
        netPositionBuffer = new HashMap<Uuid, Action>();
        netVelocityBuffer = new HashMap<Uuid, Action>();

        lock = new ReentrantLock();

        physicsColisionListener = new PhysicsColisionListener(this);

        BulletAppState bulletAppState = new BulletAppState();
        app_.getStateManager().attach(bulletAppState);
        physicsSpace = bulletAppState.getPhysicsSpace();
        physicsSpace.setGravity(Vector3f.ZERO);
        physicsSpace.addCollisionListener(physicsColisionListener);

        isDominant = isDominant_;
    }

    public void init() {

    }

    public void update(float tpf) {
        lock.lock();
        netActions.putAll(netActionBuffer);
        netActionBuffer.clear();

        netPositions.putAll(netPositionBuffer);
        netPositionBuffer.clear();

        netVelocities.putAll(netVelocityBuffer);
        netVelocityBuffer.clear();
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

                    //entityMap.remove(uuid);

                    //localEntitiesDeleted.put(uuid, getEntity(uuid));

                    deleteEntity(uuid);
                }
            });
        });
        
        entityMap.forEach((uuid, entity) -> {
            if (netVelocities.size() > 0) {
                // process net velocity
                if (netVelocities.containsKey(uuid)) {
                    entity.processAction(netVelocities.get(uuid));
                }
            }

            if (netPositions.size() > 0) {
                // process net position
                if (netPositions.containsKey(uuid)) {
                    entity.processAction(netPositions.get(uuid));
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
        netVelocities.clear();

        deleteStaged();
    }

    public void shutdown() {
        entityMap.clear();

        physicsSpace.destroy();
    }

    public void submitEntityAction(Uuid uuid, Action action, boolean isDominantAction) {

        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            boolean process = true;

            if (action instanceof MoveAction) {
                if (isDominant || isDominantAction) {
                    localPositions.put(uuid, action);
                }
            } else if (action instanceof VelocityAction) {
                if (isDominant || isDominantAction) {
                    localPositions.put(uuid, new MoveAction(getEntity(uuid).getPosition()));
                    localVelocities.put(uuid, action);
                }
            } else if (action instanceof DeleteAction) {
                if (isDominant || isDominantAction) {
                    if (null == localActions.get(uuid)) {
                        localActions.put(uuid, new ArrayList<Action>());
                    }
                    localActions.get(uuid).add(action);
                    deleteEntity(uuid);
                }
                else {
                    process = false;
                }
            } else {
                if (null == localActions.get(uuid)) {
                    localActions.put(uuid, new ArrayList<Action>());
                }
                localActions.get(uuid).add(action);
            }

            if(process)
            {
                entity.processAction(action);
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

    private void deleteEntity(Uuid uuid) {
        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            

            //if (null == localActions.get(uuid)) {
            //    localActions.put(uuid, new ArrayList<Action>());
            //}
            //localActions.get(uuid).add(new DeleteAction());

            localEntitiesDeleted.put(uuid, entity);
        } else {
            LOGGER.log(Level.WARNING, "OM: unknown entity");
        }
    }

    private void deleteStaged()
    {
        localEntitiesDeleted.forEach((uuid, entity) -> {
            entity.onShutdown();
            
            if(entityMap.containsKey(uuid))
            {
                entityMap.remove(uuid);
            }

            if (entity instanceof RigEntity) {
                RigEntity rigEntity = (RigEntity) entity;

                physicsSpace.remove(rigEntity.getRig());
            }
        });
    }

    public Entity getEntity(Uuid uuid) {
        if (entityMap.containsKey(uuid)) {
            return entityMap.get(uuid);
        } else {
            throw new RuntimeException("OM: unknown entity");
        }
    }

    public boolean knownIs(Uuid uuid) {
        if (entityMap.containsKey(uuid)) {
            return true;
        } else {
            return false;
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
        lock.lock();

        netActionBuffer.putAll(update.getActions());
        netPositionBuffer.putAll(update.getPositions());
        netVelocityBuffer.putAll(update.getVelocities());

        lock.unlock();
    }

    public Update getUpdate() {
        Update update = new Update();

        update.addActions(localActions);
        localActions.clear();

        update.addPositions(localPositions);
        localPositions.clear();

        update.addVelocities(localVelocities);
        localVelocities.clear();

        return update;
    }

    public Update getAll() {
        Update update = new Update();

        entityMap.forEach((uuid, entity) -> {
            update.addAction(uuid, new CreateAction(entity.getType(), entity.getName()));

            if (entity instanceof RigEntity) {
                RigEntity rigEntity = (RigEntity) entity;

                update.addPosition(uuid, new MoveAction(rigEntity.getPosition()));
                update.addVelocity(uuid, new VelocityAction(rigEntity.getVelocity()));

            } else {
                update.addPosition(uuid, new MoveAction(entity.getPosition()));
            }
        });

        return update;
    }
}