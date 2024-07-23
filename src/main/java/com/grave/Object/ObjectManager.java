package com.grave.Object;

import com.google.common.collect.ArrayListMultimap;
import com.grave.Graveborn;
import com.grave.Uuid;
import com.grave.Game.Entities.Entity;
import com.grave.Game.Entities.RigEntity;
import com.grave.Game.Entities.Type;
import com.grave.Object.Actions.Action;
import com.grave.Object.Actions.CreateAction;
import com.grave.Object.Actions.DeleteAction;
import com.grave.Object.Actions.MoveAction;
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

    private ArrayListMultimap<Uuid, Action> localActionBuffer;
    private ArrayListMultimap<Uuid, Action> netActionBuffer;

    private PhysicsSpace physicsSpace;

    public ObjectManager(Graveborn app) {
        entityMap = new HashMap<Uuid, Entity>();

        localEntitiesNew = new HashMap<Uuid, Entity>();
        localEntitiesDeleted = new HashMap<Uuid, Entity>();

        localActionBuffer = ArrayListMultimap.create();
        netActionBuffer = ArrayListMultimap.create();

        BulletAppState bulletAppState = new BulletAppState();
        app.getStateManager().attach(bulletAppState);
        physicsSpace = bulletAppState.getPhysicsSpace();
        physicsSpace.setGravity(Vector3f.ZERO);
    }

    public void init() {

    }

    public void update(float tpf) {
        //process net creations and deletions
        netActionBuffer.asMap().forEach((uuid, collection) -> {
            collection.forEach((action) -> {
                if (action instanceof CreateAction) {
                    CreateAction createAction = (CreateAction) action;
                    System.out.println("gffdfdgfdg");
                    entityMap.put(uuid, createAction.getType().build(uuid, this, createAction.getName()));

                    localEntitiesNew.put(uuid, getEntity(uuid));
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
        localActionBuffer.get(uuid).add(action);

        if (entityMap.containsKey(uuid)) {
            Entity entity = entityMap.get(uuid);

            entity.processAction(action);
        }
    }

    public Uuid createEntity(Type type, String name) {
        Uuid id = new Uuid();
        Entity entity = type.build(id, this, name);

        entityMap.put(id, entity);
        localEntitiesNew.put(id, entity);

        localActionBuffer.get(id).add(new CreateAction(entity.getType(), entity.getName()));

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
        //forceUpdate(update);
    }

    public void forceUpdate(Update update) {
        System.out.println("forcing-" + update.getActions().size());
        netActionBuffer = update.getActions();
    }

    public Update getUpdate() {
        Update update = new Update();

        update.addActions(localActionBuffer);

        return update;
    }

    public Update getAll() {
        Update update = new Update();

        entityMap.forEach((uuid, entity) -> {
            update.addAction(uuid, new CreateAction(entity.getType(), entity.getName()));
            update.addAction(uuid, new MoveAction(entity.getPosition()));
        });

        System.out.println("sending-" + update.getActions().size());

        return update;
    }
}