package com.grave.Game.Entities;

import java.util.ArrayList;

import com.grave.Uuid;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.VelocityAction;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

public class Zombie extends RigEntity {
    private static final float SPEED = 1.7f;
    private static final float MASS = 5;

    Uuid targetID = null;

    public Zombie(Uuid id_, Type type_, ObjectManager objectManager_, String name_, Mesh mesh_) {
        super(id_, type_, objectManager_, name_, mesh_);

        rig.setMass(MASS);
    }

    @Override
    public void onInit() {
        //...
    }

    @Override
    public void onUpdate(float tpf) {
        if(null == targetID)
        {
            Entity nearestHuman = null;
            float nearestHumanDistance = 0;

            ArrayList<Entity> players = objectManager.queryEntityByClass(Human.class);

            for (Entity player : players) {
                if (null == nearestHuman) {
                    nearestHuman = player;
                } else {
                    float distance = geometry.getLocalTranslation().distance(player.getPosition());

                    if (distance < nearestHumanDistance) {
                        nearestHuman = player;
                        nearestHumanDistance = distance;
                    }
                }
            }

            if (null != nearestHuman) {
                targetID = nearestHuman.getId();
            }
        }
        
        if(null != targetID)
        {
            Entity target = objectManager.getEntity(targetID);
            Vector3f moveVector = target.getPosition().subtract(getPosition());

            //TODO
            //rig.setLinearVelocity(moveVector.normalize().mult(SPEED));
            objectManager.submitEntityAction(id, new VelocityAction(moveVector.normalize().mult(SPEED)));

        }
    }

    @Override
    public void onShutdown() {
        // ...
    }
}
