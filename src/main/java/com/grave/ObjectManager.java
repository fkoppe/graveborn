package com.grave;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ObjectManager implements UpdateHandler{
    private final AssetManager assetManager;
    private final Node rootNode;
    private final PhysicsSpace physicsSpace;
    private HashSet<Geometry> objectSet;
    private List<Integer> idList;
    private final PlayerHandler playerHandler;
    public ObjectManager(AssetManager assetManager, Node rootNode, PhysicsSpace physicsSpace, PlayerHandler playerHandler){
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.objectSet = new HashSet<>();
        this.physicsSpace = physicsSpace;
        this.playerHandler = playerHandler;
        idList = new ArrayList<>();
    }

    public void spawnZombie(){
        String zombieName = "zombie"+getId();
        Zombie zombie = new Zombie(zombieName,new Vector3f(-2,-5,0), this, assetManager, physicsSpace);
        objectSet.add(zombie);
        rootNode.attachChild(zombie);
        System.out.println(zombieName + " spawned");
    }

    public int getId(){
        int o = idList.size();
        idList.add(o);
        return o;
    }


    @Override
    public void update(float tpf) {
        for(Geometry obj: objectSet){
            if(obj instanceof UpdateHandler){
                ((UpdateHandler) obj).update(tpf);
            }
        }
    }

    public Vector3f getPlayerPos(){
        return playerHandler.getPosition();
    }
}
