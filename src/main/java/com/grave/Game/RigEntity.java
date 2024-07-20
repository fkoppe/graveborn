package com.grave.Game;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

public abstract class RigEntity extends Entity {
    protected RigidBody2DControl rig;

    public RigEntity(String name_, Mesh mesh_, Material material_, float mass_)
    {
        super(name_, mesh_, material_);

        CollisionShape shape = CollisionShapeFactory.createBoxShape(geometry);
        rig = new RigidBody2DControl(shape, mass_);
        rig.setRotation(false);
        geometry.addControl(rig);
    }

    //TODO: idk if that works
    public RigEntity(String name_, Mesh mesh_, Material material_, float mass_, Geometry collider) {
        super(name_, mesh_, material_);

        CollisionShape shape = CollisionShapeFactory.createBoxShape(collider);
        rig = new RigidBody2DControl(shape, mass_);
        rig.setRotation(false);
        
        geometry.addControl(rig);
        collider.addControl(rig);
    }

    abstract public void onInit();

    abstract public void onUpdate(float tpf);

    abstract public void onShutdown();
    
    @Override
    public void setPosition(float x, float y, float z) {
        rig.setPhysicsLocation(new Vector3f(x, y, z));
    }
    
    public RigidBodyControl getRig() {
        return rig;
    }
}
