package com.grave.Game;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

public abstract class Entity extends Geometry {
    private RigidBody2DControl rig;

    public Entity(String name_, Mesh mesh_, float mass_)
    {
        super(name_, mesh_);

        CollisionShape shape = CollisionShapeFactory.createBoxShape(this);
        rig = new RigidBody2DControl(shape, mass_);
        rig.setRotation(false);
        addControl(rig);
    }

    abstract public void onInit();
    abstract public void onUpdate(float tpf);

    abstract public void onShutdown();

    public void setPosition(float x, float y, float z) {
        rig.setPhysicsLocation(new Vector3f(x, y, z));
    }
    
    public RigidBodyControl getRig() {
        return rig;
    }
}
