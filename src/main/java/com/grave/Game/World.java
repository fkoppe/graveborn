package com.grave.Game;

import java.util.UUID;

import com.grave.Graveborn;
import com.grave.Game.Entities.Entity;
import com.grave.Game.Entities.RigEntity;
import com.grave.Game.Entities.Zombie;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.MoveAction;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Box;

public class World {
    private ObjectManager objectManager;

    private AssetManager assetManager;

    private UUID obstacleID;
    private UUID zombieID;

    public World(Graveborn app_, ObjectManager objectManager_)
    {
        objectManager = objectManager_;

        assetManager = app_.getAssetManager();
    }

    public void init()
    {
        initTestObstacle();
        initTestZombie();
    }

    public void update(float tpf) {
        
    }

    public void shutdown()
    {

    }
    
    private void initTestObstacle() {

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Black);

        Entity obstacle = new RigEntity(objectManager, "Obstacle", new Box(1, 1, 0f), material, 0);

        obstacleID = objectManager.createEntity(obstacle);

        objectManager.submitEntityAction(obstacleID, new MoveAction(new Vector3f(3, 3, 0)));
    }
    
    private void initTestZombie() {

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Green);

        Entity zombie = new Zombie(objectManager, "Obstacle", material);

        zombieID = objectManager.createEntity(zombie);

        objectManager.submitEntityAction(zombieID, new MoveAction(new Vector3f(6, 6, 0)));
    }
}
