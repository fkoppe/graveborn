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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ObjectManager {
    private HashSet<Geometry> objectSet;
    private int idCounter;

    private HashMap<String, Geometry> clientPlayerMap;
    private HashMap<String, Vector3f> clientPosBufferMap;

    //TODO make private
    protected PhysicsSpace physicsSpace = null;

    private AssetManager assetManager;
    private Node rootNode;

    public ObjectManager(Graveborn app){
        objectSet = new HashSet<>();
        idCounter = 0;
        clientPlayerMap = new HashMap<>();
        clientPosBufferMap = new HashMap<>();

        //physicspace???

        assetManager = app.getAssetManager();
        rootNode = app.getRootNode();
    }

    public void spawnZombie(){
        String zombieName = "zombie"+getId();
        Zombie zombie = new Zombie(this, zombieName, new Vector3f(-2,-5,0));
        objectSet.add(zombie);
        rootNode.attachChild(zombie);
        System.out.println(zombieName + " spawned");
    }

    public int getId(){
        return idCounter++;
    }

    public void init() {

    }

    public void update(float tpf) {
        for (Geometry obj : objectSet) {
            if (obj instanceof Entity) {
                ((Entity) obj).onUpdate(tpf);
            }
        }

        for (Map.Entry<String, Geometry> entry : clientPlayerMap.entrySet()) {
            String clientName = entry.getKey();
            Vector3f bufferPos = clientPosBufferMap.get(clientName);
            clientPlayerMap.get(clientName).setLocalTranslation(bufferPos);
        }
    }

    public void shutdown() {

    }

    private Geometry createClientPlayer(String name){
        Geometry o = new Geometry(name, new Box(1, 1, 1));
        o.setLocalTranslation(0, 0, 0);
        Material oMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture oTex = assetManager.loadTexture("Textures/character.png");
        oTex.setMagFilter(Texture.MagFilter.Nearest);
        oMat.setTexture("ColorMap", oTex);
        oMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        o.setQueueBucket(RenderQueue.Bucket.Transparent);
        o.setMaterial(oMat);
        rootNode.attachChild(o);
        return o;
    }

    private Geometry getPlayer(){
        for(Geometry g: objectSet){
            if(g.getName().equals("Player")){
                return g;
            }
        }
        throw new RuntimeException("Player is not in objectSet");
    }

    public Vector3f getPlayerPos(){
        return getPlayer().getLocalTranslation();
    }

    public void add(Geometry g){
        objectSet.add(g);
    }

    public void addClientPlayer(String clientName) {
        if(clientPlayerMap.containsKey(clientName)) throw new RuntimeException("clientName is already in Map");
        clientPlayerMap.put(clientName, createClientPlayer(clientName));
        clientPosBufferMap.put(clientName, new Vector3f(0,0,0));
    }

    public void moveClientPlayer(String clientName, Vector3f pos) {
        clientPosBufferMap.put(clientName, pos);
    }

    public void removeClientPlayer(String clientName) {
        rootNode.detachChild(clientPlayerMap.get(clientName));
        clientPlayerMap.remove(clientName);
        clientPosBufferMap.remove(clientName);
    }
    
    public AssetManager getAssetManager() {
        return assetManager;
    }

    public RootNode getRootNode() {
        return rootNode;
    }
}
