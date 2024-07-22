package com.grave.Object;

import com.grave.Graveborn;
import com.grave.uuid;
import com.grave.Game.Entities.Entity;
import com.grave.Game.Entities.RigEntity;
import com.grave.Object.Actions.Action;
import com.grave.Object.Actions.CreateAction;
import com.grave.Object.Actions.DeleteAction;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;

import com.jme3.bullet.BulletAppState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectManager {
    private static final Logger LOGGER = Logger.getLogger(ObjectManager.class.getName());

    private HashMap<uuid, Entity> entityMap;

    private HashMap<uuid, Action> localActionBuffer;
    private HashMap<uuid, Entity> localEntitiesNew;
    private HashMap<uuid, Entity> localEntitiesDeleted;

    private HashMap<uuid, Action> netActionBuffer;

    private PhysicsSpace physicsSpace;

    public ObjectManager(Graveborn app) {
        entityMap = new HashMap<uuid, Entity>();
        localActionBuffer = new HashMap<uuid, Action>();

        localEntitiesNew = new HashMap<uuid, Entity>();
        localEntitiesDeleted = new HashMap<uuid, Entity>();

        netActionBuffer = new HashMap<uuid, Action>();

        BulletAppState bulletAppState = new BulletAppState();
        app.getStateManager().attach(bulletAppState);
        physicsSpace = bulletAppState.getPhysicsSpace();
        physicsSpace.setGravity(Vector3f.ZERO);
    }

    public void init() {

    }

    public void update(float tpf) {
        //send updates


        //fetch updates

        netActionBuffer.entrySet().forEach((entry) -> {
            if (entry.getValue() instanceof CreateAction) {
                CreateAction createAction = (CreateAction) entry.getValue();

                entityMap.put(entry.getKey(), createAction.getEntity());
                localEntitiesNew.put(entry.getKey(), createAction.getEntity());
            }
            else if (entry.getValue() instanceof DeleteAction) {
                DeleteAction deleteAction = (DeleteAction) entry.getValue();

                localEntitiesDeleted.put(entry.getKey(), getEntity(deleteAction.getId()));
            }
        });

        //process updates
        entityMap.forEach((uuid, entity) -> {
            if (netActionBuffer.containsKey(uuid)) {
                netActionBuffer.entrySet().forEach((entry) -> {
                    entity.processAction(entry.getValue());
                });
            }

            entity.onUpdate(tpf);
        });

        netActionBuffer.clear();
    }

    public void shutdown() {

    }

    public void submitEntityAction(uuid uuid, Action action) {
        localActionBuffer.put(uuid, action);

        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            entity.processAction(action);
        }
    }

    public uuid createEntity(Entity entity) {
        uuid id = new uuid();

        entityMap.put(id, entity);
        localEntitiesNew.put(id, entity);

        localActionBuffer.put(id, new CreateAction());

        entity.setId(id);

        if (entity instanceof RigEntity) {
            RigEntity rigEntity = (RigEntity) entity;

            physicsSpace.add(rigEntity.getRig());
        }

        entity.onInit();

        return id;
    }

    public void deleteEntity(uuid uuid) {
        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            entity.onShutdown();

            localActionBuffer.put(uuid, new DeleteAction());

            localEntitiesDeleted.put(uuid, entity);
            entityMap.remove(uuid);
        } else {
            LOGGER.log(Level.WARNING, "OM: unknown entity");
        }
    }

    public Entity getEntity(uuid uuid) {
        if (entityMap.containsKey(uuid)) {
            return entityMap.get(uuid);
        } else {
            throw new RuntimeException("unknown entity");
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

    public HashMap<uuid, Entity> getLocalEntitiesNew() {
        HashMap<uuid, Entity> copy = new HashMap<>(localEntitiesNew);

        localEntitiesNew.clear();

        return copy;
    }

    public HashMap<uuid, Entity> getLocalEntitiesDeleted() {
        HashMap<uuid, Entity> copy = new HashMap<>(localEntitiesDeleted);

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
        HashMap<uuid, Action> copy = new HashMap<>(localActionBuffer);

        localActionBuffer.clear();

        Update update = new Update();

        update.addActions(copy);

        return update;
    }

    public Update getAll() {
        Update update = new Update();

        entityMap.forEach((uuid, entity) -> {
            update.addAction(uuid, new CreateAction(entity));
        });

        return update;
    }
}