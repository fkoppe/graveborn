package com.grave.Object;

import com.grave.Graveborn;
import com.grave.Game.Entities.Entity;
import com.grave.Game.Entities.RigEntity;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;

import com.jme3.bullet.BulletAppState;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectManager {
    private static final Logger LOGGER = Logger.getLogger(ObjectManager.class.getName());

    private HashMap<UUID, Entity> entityMap;
    private HashMap<UUID, Vector3f> entityPositionBuffer;

    private HashMap<UUID, Entity> localEntitiesNew;
    private HashMap<UUID, Entity> localEntitiesDeleted;

    private PhysicsSpace physicsSpace;

    public ObjectManager(Graveborn app) {
        entityMap = new HashMap<UUID, Entity>();
        entityPositionBuffer = new HashMap<UUID, Vector3f>();

        localEntitiesNew = new HashMap<UUID, Entity>();
        localEntitiesDeleted = new HashMap<UUID, Entity>();

        BulletAppState bulletAppState = new BulletAppState();
        app.getStateManager().attach(bulletAppState);
        physicsSpace = bulletAppState.getPhysicsSpace();
        physicsSpace.setGravity(Vector3f.ZERO);
    }

    public void init() {

    }

    public void update(float tpf) {
        // process updates

        entityMap.forEach((uuid, entity) -> {
            if (entityPositionBuffer.containsKey(uuid)) {
                Vector3f position = entityPositionBuffer.get(uuid);

                // entity.setLocalTranslation(position);
            }

            entity.onUpdate(tpf);
        });

        entityPositionBuffer.clear();

        // send updates
    }

    public void shutdown() {

    }

    public void moveEntity(UUID uuid, Vector3f position) {
        entityPositionBuffer.put(uuid, position);
    }

    public void setEntityVelocity(UUID uuid, Vector3f velocity) {
        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            if (entity instanceof RigEntity) {
                RigEntity rigEntity = (RigEntity) entity;

                rigEntity.getRig().setLinearVelocity(velocity);
            }

        } else {
            LOGGER.log(Level.WARNING, "OM: unknown entity");
        }
    }

    public UUID createEntity(Entity entity) {
        UUID id = UUID.randomUUID();

        entityMap.put(id, entity);
        localEntitiesNew.put(id, entity);

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

    public HashMap<UUID, Entity> getLocalEntitiesNew() {
        HashMap<UUID, Entity> copy = (HashMap<UUID, Entity>) localEntitiesNew.clone();
        assert (null != copy);

        localEntitiesNew.clear();

        return copy;
    }

    public HashMap<UUID, Entity> getLocalEntitiesDeleted() {
        HashMap<UUID, Entity> copy = (HashMap<UUID, Entity>) localEntitiesDeleted.clone();
        assert (null != copy);

        localEntitiesDeleted.clear();

        return copy;
    }

    public void takeNotice(Notice notice) {
        // ...
    }

    public void forceNotice(Notice notice) {
        // ...
    }

    public Notice giveNotice() {
        Notice notice = new Notice();

        return notice;
    }

    public void takeSync(Sync sync) {
        // ...
    }

    public void forceSync(Sync sync) {
        // ...
    }

    public Sync giveSync() {
        Sync sync = new Sync();

        return sync;
    }
}