package com.grave.Game;

import com.grave.Graveborn;
import com.grave.UpdateHandler;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ObjectManager implements UpdateHandler{
    private Graveborn application;
    private HashSet<Geometry> objectSet;
    private int idCounter;
    private Geometry clientPlayer;

    private HashMap<String, Geometry> clientPlayerMap;
    private HashMap<String, Vector3f> clientPosBufferMap;
    private Vector3f clientPosBuffer = new Vector3f(0, 0, 0);

    boolean oldAddedIs = false;
    boolean addedIs = false;

    public ObjectManager(Graveborn application_){
        application = application_;
        this.objectSet = new HashSet<>();
        idCounter = 0;
        clientPlayer = null;
        clientPlayerMap = new HashMap<>();
        clientPosBufferMap = new HashMap<>();
    }

    public void spawnZombie(){
        String zombieName = "zombie"+getId();
        Zombie zombie = new Zombie(zombieName,new Vector3f(-2,-5,0), application);
        objectSet.add(zombie);
        application.getRootNode().attachChild(zombie);
        System.out.println(zombieName + " spawned");
    }

    public int getId(){
        return idCounter++;
    }

    @Override
    public void init() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void update(float tpf) {
        for (Geometry obj : objectSet) {
            if (obj instanceof UpdateHandler) {
                ((UpdateHandler) obj).update(tpf);
            }
        }

        System.out.println(clientPlayerMap);
        System.out.println(clientPosBufferMap);


        for(Map.Entry<String, Geometry> entry: clientPlayerMap.entrySet()){
            String clientName = entry.getKey();
            Vector3f bufferPos = clientPosBufferMap.get(clientName);
            clientPlayerMap.get(clientName).setLocalTranslation(bufferPos);
        }
    }

    private Geometry createClientPlayer(String name){
        Geometry o = new Geometry(name, new Box(1, 1, 1));
        o.setLocalTranslation(0, 0, 0);
        Material oMat = new Material(application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        Texture oTex = application.getAssetManager().loadTexture("Textures/character.png");
        oTex.setMagFilter(Texture.MagFilter.Nearest);
        oMat.setTexture("ColorMap", oTex);
        oMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        o.setQueueBucket(RenderQueue.Bucket.Transparent);
        o.setMaterial(oMat);
        application.getRootNode().attachChild(o);
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
        clientPlayerMap.put(clientName, createClientPlayer(clientName));
    }

    public void moveClientPlayer(String clientName, Vector3f pos) {
        clientPosBufferMap.put(clientName, pos);
    }
}
