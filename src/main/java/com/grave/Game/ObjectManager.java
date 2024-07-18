package com.grave.Game;

import com.grave.Graveborn;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class ObjectManager {
    private HashMap<String, Entity> entityMap;
    private HashMap<String, Vector3f> entiyPosBufferMap;

    private PhysicsSpace physicsSpace;

    private AssetManager assetManager;
    private Node rootNode;

    private String humanId;

    public ObjectManager(Graveborn app) {
        entityMap = new HashMap<>();
        entiyPosBufferMap = new HashMap<>();
        humanId = null;

        BulletAppState bulletAppState = new BulletAppState();
        app.getStateManager().attach(bulletAppState);
        physicsSpace = bulletAppState.getPhysicsSpace();
        physicsSpace.setGravity(Vector3f.ZERO);

        assetManager = app.getAssetManager();
        rootNode = app.getRootNode();
    }

    public void spawnZombie() {
        String id = getId();
        Zombie zombie = new Zombie(this, id, new Vector3f(-2, -5, 0));
        entityMap.put(id, zombie);
        System.out.println("zombie: " + id + " spawned");
    }


    public String getId() {
        return UUID.randomUUID().toString();
    }

    public void init() {
    }

    public void update(float tpf) {
        for (Map.Entry<String, Entity> entry : entityMap.entrySet()) {
            entry.getValue().onUpdate(tpf);

            if(entiyPosBufferMap.containsKey(entry.getKey())){
                String clientName = entry.getKey();
                Vector3f bufferPos = entiyPosBufferMap.get(clientName);
                entityMap.get(clientName).setLocalTranslation(bufferPos);
            }
        }
    }

    public void shutdown() {

    }

    private Geometry getPlayer() {
        if(entityMap.containsKey(humanId)) return entityMap.get(humanId);
        throw new RuntimeException("Player is not in objectSet");
    }

    public Vector3f getPlayerPos() {
        return getPlayer().getLocalTranslation();
    }

    public String addEntity(Entity entity){
        String id = getId();
        entityMap.put(id, entity);
        return id;
    }

    public void addClientPlayer(String clientName) {
        if (entityMap.containsKey(clientName)) throw new RuntimeException("clientName is already in Map");

        ClientPlayer cp = new ClientPlayer(clientName, this);
        entityMap.put(clientName, cp);
        entiyPosBufferMap.put(clientName, new Vector3f(0, 0, 0));
    }

    public void moveEntity(String entityId, Vector3f pos) {
        entiyPosBufferMap.put(entityId, pos);
    }

    public void removeEntity(String entityId) {
        rootNode.detachChild(entityMap.get(entityId));
        entityMap.remove(entityId);
        entiyPosBufferMap.remove(entityId);
    }

    public void setHuman(Entity human){
        String id = getId();
        humanId = id;
        entityMap.put(id, human);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public PhysicsSpace getPhysicsSpace() {
        return physicsSpace;
    }
}
