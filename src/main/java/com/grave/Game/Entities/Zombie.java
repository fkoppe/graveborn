package com.grave.Game.Entities;

import java.util.ArrayList;
import java.util.UUID;

import com.grave.Game.Player;
import com.grave.Object.ObjectManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Box;

public class Zombie extends RigEntity {
    private static final float SPEED = 2f;
    private static final float MASS = 5;

    UUID targetID = null;

    public Zombie(ObjectManager objectManager_, String name_, Material material_) {
        super(objectManager_, name_, new Box(1, 1, 1), material_, MASS);
    }

    @Override
    public void onInit() {
        //...
    }

    @Override
    public void onUpdate(float tpf) {
        if(null == targetID)
        {
            Entity nearestPlayer = null;
            float nearestPlayerDistance = 0;

            ArrayList<Entity> players = objectManager.queryEntityByClass(Human.class);

            for (Entity player : players) {
                if (null == nearestPlayer) {
                    nearestPlayer = player;
                } else {
                    float distance = geometry.getLocalTranslation().distance(player.getPosition());

                    if (distance < nearestPlayerDistance) {
                        nearestPlayer = player;
                        nearestPlayerDistance = distance;
                    }
                }
            }

            if (null != nearestPlayer) {
                targetID = nearestPlayer.getID();
            }
        }
        
        if(null != targetID)
        {
            Entity target = objectManager.getEntity(targetID);
            Vector3f moveVector = target.getPosition().subtract(getPosition());

            rig.setLinearVelocity(moveVector.normalize().mult(SPEED));
        }
    }

    @Override
    public void onShutdown() {
        // ...
    }
}
