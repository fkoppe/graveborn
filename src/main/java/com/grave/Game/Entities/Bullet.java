package com.grave.Game.Entities;

import com.grave.Game.Gun;
import com.grave.Uuid;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.DeleteAction;
import com.grave.Object.Actions.MoveAction;
import com.grave.Object.Actions.VelocityAction;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.system.NanoTimer;

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

    public void fire(Uuid entityID, Vector3f targetPosition, Gun gun)
    {
        rig.setMass(gun.getMass());
        time = gun.getTime();
        
        shooterID = entityID;

        flyTimer.reset();

        Vector3f playerLoc = objectManager.getEntity(entityID).getPosition();
        Vector3f direction = targetPosition.subtract(playerLoc).normalize();

        objectManager.submitEntityAction(id, new MoveAction(playerLoc), true);
        objectManager.submitEntityAction(id, new VelocityAction(direction.normalize().mult(gun.getSpeed())), true);
    }

    @Override
    public void onShutdown() {
        // ...
    }
}
