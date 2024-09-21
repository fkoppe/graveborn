package com.grave.Game.Entities;

import javax.swing.Box;

import com.grave.Uuid;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.MoveAction;
import com.grave.Object.Actions.VelocityAction;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.system.NanoTimer;

public class Bullet extends RigEntity {
    private static final float SPEED = 3000;
    private static final float MASS = 1;
    private static final float TIME = 3;

    private NanoTimer flyTimer = new NanoTimer();

    Uuid shooterID;

    public Bullet(Uuid id_, Type type_, ObjectManager objectManager_, String name_, Mesh mesh_) {
        super(id_, type_, objectManager_, name_, mesh_);
        
        rig.setMass(MASS);

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
        if (flyTimer.getTimeInSeconds() >= TIME) {
            if (objectManager.knownIs(id)) {
                objectManager.deleteEntity(id);
            }
        }
    }
    
    @Override
    public void onColision(Uuid otherID)
    {
        if(!objectManager.knownIs(otherID))
        {
            return;
        }

        assert(!otherID.equals(shooterID));

        Entity entity = objectManager.getEntity(otherID);

        if ((entity instanceof Human || entity instanceof Zombie || entity instanceof Bullet)) {

            if (objectManager.knownIs(otherID)) {
                objectManager.deleteEntity(otherID);
            }
        }
        
        if (objectManager.knownIs(id)) {
            objectManager.deleteEntity(id);
        }
    }
    
    public void fire(Uuid entityID, Vector3f targetPosition)
    {
        shooterID = entityID;

        flyTimer.reset();

        Vector3f playerLoc = objectManager.getEntity(entityID).getPosition();
        Vector3f direction = targetPosition.subtract(playerLoc).normalize();

        objectManager.submitEntityAction(id, new MoveAction(playerLoc), true);
        objectManager.submitEntityAction(id, new VelocityAction(direction.normalize().mult(SPEED)), true);
    }

    @Override
    public void onShutdown() {
        // ...
    }
}
