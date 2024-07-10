package com.grave;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class RigidBody2DControl extends RigidBodyControl {
    public RigidBody2DControl(float f){
        super(f);
    }

    public RigidBody2DControl(CollisionShape s, float f){
        super(s,f);
    }

    @Override
    public void update(float tpf){
        super.update(tpf);
        Vector3f loc = this.getPhysicsLocation();
        loc.setZ(0f);
        this.setPhysicsLocation(loc);
    }
}
