package com.grave.Game;

import com.grave.Graveborn;
import com.grave.UpdateHandler;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

import java.util.HashSet;

public class ObjectManager implements UpdateHandler{
    private Graveborn application;
    private HashSet<Geometry> objectSet;
    private int idCounter;
    private Geometry clientPlayer;

    public ObjectManager(Graveborn application_){
        application = application_;
        this.objectSet = new HashSet<>();
        idCounter = 0;
        clientPlayer = null;
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
        for(Geometry obj: objectSet){
            if(obj instanceof UpdateHandler){
                ((UpdateHandler) obj).update(tpf);
            }
        }
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

    public void addClientPlayer(){
        clientPlayer = new Geometry("ClientPlayer", new Box(1,1,1));
        clientPlayer.setLocalTranslation(0,0,0);
        Material clientPlayerMat = new Material(application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        Texture characterTex = application.getAssetManager().loadTexture("Textures/character.png");
        characterTex.setMagFilter(Texture.MagFilter.Nearest);
        clientPlayerMat.setTexture("ColorMap", characterTex);
        clientPlayerMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        clientPlayer.setQueueBucket(RenderQueue.Bucket.Transparent);
        clientPlayer.setMaterial(clientPlayerMat);
        application.getRootNode().attachChild(clientPlayer);

        objectSet.add(clientPlayer);
    }

    public void moveClientPlayer(Vector3f pos) {
        clientPlayer.setLocalTranslation(pos);
    }
}