package com.grave.Game.Entities;

import com.grave.Game.Gun;
import com.grave.Uuid;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.Action;
import com.grave.Object.Actions.DeleteAction;
import com.grave.Object.Actions.FireAction;
import com.grave.Object.Actions.MoveAction;
import com.grave.Object.Actions.VelocityAction;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.system.NanoTimer;

import java.util.Random;

public class Bullet extends RigEntity {
    private NanoTimer flyTimer = new NanoTimer();

    private float time = 0.0f;

    Uuid shooterID;

    public Bullet(Uuid id_, Type type_, ObjectManager objectManager_, String name_, Mesh mesh_) {
        super(id_, type_, objectManager_, name_, mesh_);
        
        rig.setMass(1);

        rig.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);

        rig.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_02);
        rig.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
    }

    @Override
    public void onInit() {
        // ...
    }

    @Override
    public void onUpdate(float tpf) {
        if (flyTimer.getTimeInSeconds() >= time) {
            if (objectManager.knownIs(id)) {
                //objectManager.deleteEntity(id);
                objectManager.submitEntityAction(id, new DeleteAction(), false);
            }
        }
    }

    @Override
    public void processAction(Action action) {
        if (action instanceof MoveAction moveAction) {
            rig.setPhysicsLocation(moveAction.getPosition());
        } else if (action instanceof VelocityAction) {
            VelocityAction velocityAction = (VelocityAction) action;

            if (rig.getMass() > 0) {
                rig.setLinearVelocity(velocityAction.getVelocity());
            }
        } else if (action instanceof FireAction) {
            FireAction fireAction = (FireAction) action;

            rig.setMass(fireAction.gun.getMass());
            time = fireAction.gun.getTime();

            shooterID = fireAction.shooterID;

            flyTimer.reset();

            Vector3f playerLoc = objectManager.getEntity(fireAction.shooterID).getPosition();
            Vector3f targetPos = fireAction.targetPosition;
            targetPos.setZ(0);

            Vector3f direction = targetPos.subtract(playerLoc).normalize();

            float spreadAngle = randomSpread(fireAction.gun.getSpread());

            Vector3f spreadVector = turnVector(direction, spreadAngle);

            objectManager.submitEntityAction(id, new MoveAction(playerLoc), false);
            objectManager.submitEntityAction(id, new VelocityAction(spreadVector.mult(fireAction.gun.getSpeed())), false);
        }
    }

    private float randomSpread(float spread){
        System.out.println(spread);
        Random random = new Random();
        float min = -spread;
        float max = spread;
        float randomFloatInRange = min + random.nextFloat() * (max - min);
        System.out.println(randomFloatInRange);
        return randomFloatInRange;
    }

    private Vector3f turnVector(Vector3f vector, float angle){
        float radianAngle = (float) Math.toRadians(angle);
        Vector3f rotationAxis = new Vector3f(0, 0, 1).normalize();
        Quaternion quaternion = new Quaternion();
        quaternion.fromAngleAxis(radianAngle, rotationAxis);
        Vector3f rotatedVector = quaternion.mult(vector);

        return rotatedVector;
    }

    
    @Override
    public void onCollision(Uuid otherID)
    {
        if (!objectManager.knownIs(otherID)) {
            return;
        }

        assert (!otherID.equals(shooterID));

        Entity entity = objectManager.getEntity(otherID);

        if ((entity instanceof Human || entity instanceof Zombie || entity instanceof Bullet)) {

            if (objectManager.knownIs(otherID)) {
                //objectManager.deleteEntity(otherID);
                objectManager.submitEntityAction(otherID, new DeleteAction(), false);

                if(objectManager.knownIs(shooterID))
                {
                    Human h = (Human)objectManager.getEntity(shooterID);
                    h.countKill();
                }
            }
        }

        if (objectManager.knownIs(id)) {
            //objectManager.deleteEntity(id);
            objectManager.submitEntityAction(id, new DeleteAction(), false);
        }
    }

    @Override
    public void onShutdown() {
        // ...
    }
}
