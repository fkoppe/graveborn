package com.grave.Object;

import com.grave.Graveborn;
import com.grave.Game.Entities.Entity;
import com.grave.Game.Entities.RigEntity;
import com.grave.Object.Actions.Action;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;

import com.jme3.bullet.BulletAppState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectManager {
    private static final Logger LOGGER = Logger.getLogger(ObjectManager.class.getName());

    private HashMap<UUID, Entity> entityMap;

    private HashMap<UUID, Action> localActionBuffer;
    private HashMap<UUID, Entity> localEntitiesNew;
    private HashMap<UUID, Entity> localEntitiesDeleted;

    private HashMap<UUID, Action> netActionBuffer;

    private PhysicsSpace physicsSpace;

    public ObjectManager(Graveborn app) {
        entityMap = new HashMap<UUID, Entity>();
        localActionBuffer = new HashMap<UUID, Action>();

        localEntitiesNew = new HashMap<UUID, Entity>();
        localEntitiesDeleted = new HashMap<UUID, Entity>();

        netActionBuffer = new HashMap<UUID, Action>();

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

    public void submitEntityAction(UUID uuid, Action action) {
        localActionBuffer.put(uuid, action);

        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            entity.processAction(action);
        }
    }

    public UUID createEntity(Entity entity) {
        UUID id = UUID.randomUUID();

        entityMap.put(id, entity);
        localEntitiesNew.put(id, entity);

        entity.setID(id);

        if (entity instanceof RigEntity) {
            RigEntity rigEntity = (RigEntity) entity;

            physicsSpace.add(rigEntity.getRig());
        }

        entity.onInit();

        return id;
    }

    public void deleteEntity(UUID uuid) {
        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            entity.onShutdown();

            localEntitiesDeleted.put(uuid, entity);
            entityMap.remove(uuid);
        } else {
            LOGGER.log(Level.WARNING, "OM: unknown entity");
        }
    }

    public Entity getEntity(UUID uuid) {
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

    public HashMap<UUID, Entity> getLocalEntitiesNew() {
        HashMap<UUID, Entity> copy = new HashMap<>(localEntitiesNew);

        localEntitiesNew.clear();

        return copy;
    }

    public HashMap<UUID, Entity> getLocalEntitiesDeleted() {
        HashMap<UUID, Entity> copy = new HashMap<>(localEntitiesDeleted);

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
        HashMap<UUID, Action> copy = new HashMap<>(localActionBuffer);

        localActionBuffer.clear();

        Update update = new Update();

        update.addActions(copy);

        return update;
    }
}