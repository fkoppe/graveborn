package com.grave.Game;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import org.lwjgl.opencl.pocl_content_size;

import com.grave.Graveborn;
import com.grave.Uuid;
import com.grave.Game.Entities.Entity;
import com.grave.Game.Entities.RigEntity;
import com.grave.Game.Entities.Type;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.MoveAction;
import com.jme3.math.Vector3f;
import com.jme3.system.NanoTimer;

public class World {
    private int MAX_ZOMBIE = 10;
    private float SPAWN_COOLDOWN = 2.0f;

    private final float WAVE_COOLDOWN = 10.0f;

    private ObjectManager objectManager;

    private Uuid backgroundId;
    private Uuid obstacleId;

    private ArrayList<Uuid> zombies;

    private NanoTimer spawnTimer;

    private NanoTimer waveTimer;

    public World(Graveborn app_, ObjectManager objectManager_)
    {
        objectManager = objectManager_;
        zombies = new ArrayList<Uuid>();
        spawnTimer = new NanoTimer();
        waveTimer = new NanoTimer();

        app_.setDisplayStatView(false);
        app_.setDisplayFps(false);
    }

    public void init()
    {
        backgroundId = objectManager.createEntity(Type.BACKGROUND, "background");
        objectManager.submitEntityAction(backgroundId, new MoveAction(new Vector3f(0, 0, -5f)), true);

        obstacleId = objectManager.createEntity(Type.OBSTACKLE, "obstacle");
        objectManager.submitEntityAction(obstacleId, new MoveAction(new Vector3f(3, 3, 0)), true);
    }

    public void update(float tpf) {
        for (int i = 0; i < zombies.size(); i++) {
            if (!objectManager.knownIs(zombies.get(i))) {
                zombies.remove(i);
            }
        }
        
        if(spawnTimer.getTimeInSeconds() > SPAWN_COOLDOWN && zombies.size() < MAX_ZOMBIE)
        {
            spawnTimer.reset();

            ArrayList<Entity> array = objectManager.queryEntityByClass(RigEntity.class);

            boolean legit = false;
            Vector3f pos = new Vector3f(0, 0, 0);

            while(!legit)
            {
                final int x_spawn = (int) ((Math.random() * (20 - -20)) + -20);
                final int y_spawn = (int) ((Math.random() * (20 - -20)) + -20);
                pos = new Vector3f(x_spawn, y_spawn, 0);

                legit = true;

                for(Entity e : array)
                {
                    if(e.getPosition().subtract(pos).length() < 10.0f)
                    {
                        legit = false;
                    }
                }
            }

            Uuid zombieId = objectManager.createEntity(Type.ZOMBIE, "zombie");
            objectManager.submitEntityAction(zombieId, new MoveAction(pos), true);

            zombies.add(zombieId);
        }

        if(waveTimer.getTimeInSeconds() > WAVE_COOLDOWN)
        {
            waveTimer.reset();

            if(MAX_ZOMBIE < 300)
            {
                MAX_ZOMBIE *= 1.2f;

                System.out.println("max zombies: " + MAX_ZOMBIE);
            }

            if(SPAWN_COOLDOWN > 0.4f)
            {
                SPAWN_COOLDOWN -= 0.1f;
            }

            System.out.println("current zombies: " + zombies.size());
        }
    }

    public void shutdown() {

    }
}
