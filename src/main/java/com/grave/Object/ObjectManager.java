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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectManager {
    private static final Logger LOGGER = Logger.getLogger(ObjectManager.class.getName());

    private HashMap<Uuid, Entity> entityMap;

    private HashMap<Uuid, Entity> localEntitiesNew;
    private HashMap<Uuid, Entity> localEntitiesDeleted;

    private HashMap<Uuid, ArrayList<Action>> localActionBuffer;
    private HashMap<Uuid, ArrayList<Action>> netActionBuffer;

    HashMap<Uuid, Action> positionBuffer;

    private PhysicsSpace physicsSpace;

    public ObjectManager(Graveborn app) {
        entityMap = new HashMap<Uuid, Entity>();

        localEntitiesNew = new HashMap<Uuid, Entity>();
        localEntitiesDeleted = new HashMap<Uuid, Entity>();

        localActionBuffer = new HashMap<Uuid, ArrayList<Action>>();
        netActionBuffer = new HashMap<Uuid, ArrayList<Action>>();

        positionBuffer = new HashMap<Uuid, Action>();

        BulletAppState bulletAppState = new BulletAppState();
        app.getStateManager().attach(bulletAppState);
        physicsSpace = bulletAppState.getPhysicsSpace();
        physicsSpace.setGravity(Vector3f.ZERO);
    }

    public void init() {

    }

    public void update(float tpf) {
        //process net creations and deletions
        netActionBuffer.forEach((uuid, array) -> {
            array.forEach((action) -> {
                if (action instanceof CreateAction) {
                    CreateAction createAction = (CreateAction) action;

                    if(!entityMap.containsKey(uuid)) {
                        entityMap.put(uuid, createAction.getType().build(uuid, this, createAction.getName()));

                        localEntitiesNew.put(uuid, getEntity(uuid));

                        if (getEntity(uuid) instanceof RigEntity) {
                            RigEntity rigEntity = (RigEntity) getEntity(uuid);

                            // physicsSpace.add(rigEntity.getRig());
                        }
                    }
                } else if (action instanceof DeleteAction) {
                    DeleteAction deleteAction = (DeleteAction) action;

                    localEntitiesDeleted.put(uuid, getEntity(uuid));
                }
            }); 
        });
        
        //process net actions
        entityMap.forEach((uuid, entity) -> {
            if (netActionBuffer.containsKey(uuid)) {
                netActionBuffer.get(uuid).forEach((action) -> {
                    entity.processAction(action);
                });
            }

            entity.onUpdate(tpf);
        });

        netActionBuffer.clear();
    }

    public void shutdown() {

    }

    public void submitEntityAction(Uuid uuid, Action action) {
        
        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            entity.processAction(action);

            if (action instanceof MoveAction) {
                positionBuffer.put(uuid, action);
            } else if (action instanceof VelocityAction) {
                positionBuffer.put(uuid, new MoveAction(entity.getPosition()));
            } else {
                localActionBuffer.get(uuid).add(action);
            }
        }
    }

    public Uuid createEntity(Type type, String name) {
        Uuid id = new Uuid();
        Entity entity = type.build(id, this, name);

        entityMap.put(id, entity);
        localEntitiesNew.put(id, entity);

        if (null == localActionBuffer.get(id)) {
            localActionBuffer.put(id, new ArrayList<Action>());
        }
        //localActionBuffer.get(id).add(new CreateAction(entity.getType(), entity.getName()));

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

            if (null == localActionBuffer.get(uuid)) {
                localActionBuffer.put(uuid, new ArrayList<Action>());
            }
            localActionBuffer.get(uuid).add(new DeleteAction());

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
        // ...
        //TODO
        forceUpdate(update);
    }

    public void forceUpdate(Update update) {
        netActionBuffer = update.getActions();
    }

    public Update getUpdate() {
        Update update = new Update();

        update.addActions(localActionBuffer);

        //localActionBuffer.clear();

        positionBuffer.forEach((uuid, action) -> {
            update.addAction(uuid, action);
        });

        //positionBuffer.clear();

        return update;
    }

    public Update getAll() {
        Update update = new Update();

        entityMap.forEach((uuid, entity) -> {
            update.addAction(uuid, new CreateAction(entity.getType(), entity.getName()));
            //update.addAction(uuid, new MoveAction(entity.getPosition()));
        });

        return update;
    }
}