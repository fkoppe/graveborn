package com.grave.Game.Entities;

import com.grave.Game.RigidBody2DControl;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.Action;
import com.grave.Object.Actions.MoveAction;
import com.grave.Object.Actions.VelocityAction;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

public class RigEntity extends Entity {
    protected RigidBody2DControl rig;

    public RigEntity(ObjectManager objectManager_, String name_, Mesh mesh_, Material material_, float mass_)
    {
        super(objectManager_, name_, mesh_, material_);

        CollisionShape shape = CollisionShapeFactory.createBoxShape(geometry);
        rig = new RigidBody2DControl(shape, mass_);
        rig.setRotation(false);
        geometry.addControl(rig);
    }

    //TODO: idk if that works
    public RigEntity(ObjectManager objectManager_, String name_, Mesh mesh_, Material material_, float mass_, Geometry collider) {
        super(objectManager_, name_, mesh_, material_);

        CollisionShape shape = CollisionShapeFactory.createBoxShape(collider);
        rig = new RigidBody2DControl(shape, mass_);
        rig.setRotation(false);

        geometry.addControl(rig);
        collider.addControl(rig);
    }
    
    

    public void processAction(Action action) {
        if (action instanceof MoveAction) {
            MoveAction moveAction = (MoveAction) action;

            rig.setPhysicsLocation(moveAction.getPosition());
        }
        else if (action instanceof VelocityAction) {
            VelocityAction velocityAction = (VelocityAction) action;

            rig.setLinearVelocity(velocityAction.getVelocity());
        }
    }
    
    public RigidBodyControl getRig() {
        return rig;
    }
}
