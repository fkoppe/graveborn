package com.grave.Game;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

public abstract class Entity extends Geometry {
    private RigidBody2DControl rig;

    public Entity(String name_, Mesh mesh_)
    {
        super(name_, mesh_);

        CollisionShape player_shape = CollisionShapeFactory.createBoxShape(this);
        rig = new RigidBody2DControl(player_shape, 10);
        rig.setRotation(false);
        this.addControl(rig);
    }

    abstract public void onInit();
    abstract public void onUpdate(float tpf);

    abstract public void onShutdown();
    
    RigidBodyControl getRig() {
        return rig;
    }
}
