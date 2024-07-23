package com.grave.Game;

import com.grave.Graveborn;
import com.grave.Uuid;
import com.grave.Game.Entities.Type;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.MoveAction;
import com.jme3.math.Vector3f;

public class World {
    private ObjectManager objectManager;

    private Uuid backgroundId;
    private Uuid obstacleId;
    private Uuid zombieId;

    boolean spawned = false;

    public World(Graveborn app_, ObjectManager objectManager_)
    {
        objectManager = objectManager_;
    }

    public void init()
    {
        backgroundId = objectManager.createEntity(Type.BACKGROUND, "background");
        objectManager.submitEntityAction(backgroundId, new MoveAction(new Vector3f(0, 0, -0.1f)));

        obstacleId = objectManager.createEntity(Type.OBSTACKLE, "obstacle");
        objectManager.submitEntityAction(obstacleId, new MoveAction(new Vector3f(3, 3, 0)));

        zombieId = objectManager.createEntity(Type.ZOMBIE, "zombie");
        objectManager.submitEntityAction(zombieId, new MoveAction(new Vector3f(6, 6, 0)));
    }

    public void update(float tpf) {
        //TODO
        if(!spawned && false) {
            obstacleId = objectManager.createEntity(Type.OBSTACKLE, "obstackle");
            objectManager.submitEntityAction(obstacleId, new MoveAction(new Vector3f(3, 3, 0)));

            zombieId = objectManager.createEntity(Type.ZOMBIE, "zombie");
            objectManager.submitEntityAction(zombieId, new MoveAction(new Vector3f(6, 6, 0)));

            spawned = true;
        }
    }

    public void shutdown() {

    }
}
