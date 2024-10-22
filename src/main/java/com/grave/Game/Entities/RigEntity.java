package com.grave.Game.Entities;

import com.grave.Uuid;
import com.grave.Game.RigidBody2DControl;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.Action;
import com.grave.Object.Actions.MoveAction;
import com.grave.Object.Actions.VelocityAction;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

public class RigEntity extends Entity {
    protected RigidBody2DControl rig;

    public RigEntity(Uuid id_, Type type_, ObjectManager objectManager_, String name_, Mesh mesh_)
    {
        super(id_, type_, objectManager_, name_, mesh_);

        CollisionShape shape = CollisionShapeFactory.createBoxShape(geometry);
        rig = new RigidBody2DControl(shape, 0);
        
        rig.setRotation(false);

        geometry.addControl(rig);

        rig.setUserObject(new Uuid(id_));

        rig.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_04);

        rig.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);
        rig.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        rig.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
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
        }
    }
    
    public void onCollision(Uuid other) {
        
    }
    
    public void setMass(float mass) {
        rig.setMass(mass);
    }
    
    //TODO remove: analog to Entity geometry rootNode attach
    public RigidBodyControl getRig() {
        return rig;
    }

    public Vector3f getVelocity() {
        return rig.getLinearVelocity();
    }

    public void setFriction(float firction) {
        rig.setFriction(firction);
    }

    public float getFriction() {
        return rig.getFriction();
    }
}
