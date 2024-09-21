package com.grave.Game.Entities;

import java.util.ArrayList;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.grave.Uuid;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.VelocityAction;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.system.NanoTimer;

public class Zombie extends RigEntity {
    private static final float SPEED = 1.7f;
    private static final float MASS = 5;
    private static final float FIND_DISTANCE = 15.0f;
    private static final float FOLLOW_DISTANCE = 10.0f;
    private static final float FIND_COOLDOWN = 2.0f;

    Uuid targetID = null;

    private NanoTimer findTimer = new NanoTimer();

    Vector3f moveVector = new Vector3f(0, 0, 0);

    public Zombie(Uuid id_, Type type_, ObjectManager objectManager_, String name_, Mesh mesh_) {
        super(id_, type_, objectManager_, name_, mesh_);

        rig.setMass(MASS);
        rig.setFriction(0.5f);
        
        rig.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);

        rig.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);
        rig.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
    }

    @Override
    public void onInit() {
        //...
    }

    @Override
    public void onUpdate(float tpf) {
        if (!objectManager.knownIs(targetID)) {
            targetID = null;
        }

        if(null == targetID && findTimer.getTimeInSeconds() > FIND_COOLDOWN)
        {
            findTimer.reset();

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

            if(targetID != null)
            {
                Entity target = objectManager.getEntity(targetID);
                Vector3f moveVector = target.getPosition().subtract(getPosition());

                if(moveVector.length() >= FIND_DISTANCE)
                {
                    targetID = null;
                }
            }
        }
        
        if(null != targetID)
        {
            Entity target = objectManager.getEntity(targetID);
            Vector3f vector = target.getPosition().subtract(getPosition());

            if (moveVector.length() >= FOLLOW_DISTANCE) {
                targetID = null;

                final int x = (int) ((Math.random() * (2 - 0)) + -0);
                final int y = (int) ((Math.random() * (2 - 0)) + -0);

                Vector3f vec = new Vector3f(x, y, 0);

                vec = moveVector.mult(vec);

                moveVector = vec;
            } else {
                moveVector = vector;
            }
        }
        
        if(moveVector.length() > 0)
        {
            objectManager.submitEntityAction(id, new VelocityAction(moveVector.normalize().mult(SPEED)), false);
        }
    }

    @Override
    public void onShutdown() {
        // ...
    }

    @Override
    public void onColision(Uuid otherID) {
        if (!objectManager.knownIs(otherID)) {
            return;
        }

        if(null == targetID)
        {
            return;
        }

        if(!otherID.equals(targetID))
        {
            return;
        }

        Entity entity = objectManager.getEntity(otherID);

        assert(entity instanceof Human);

        if (objectManager.knownIs(otherID)) {
            objectManager.deleteEntity(otherID);
        }
    }
}
