package com.grave.Object.Actions;

import com.jme3.math.Vector3f;

public class VelocityAction extends Action {
    private Vector3f velocity;

    public VelocityAction(Vector3f velocity_) {
        velocity = velocity_;
    }

    public Vector3f getVelocity() {
        return velocity;
    }
}
