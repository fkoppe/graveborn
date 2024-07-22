package com.grave.Object.Actions;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

@Serializable
public class VelocityAction extends Action {
    private Vector3f velocity;

    public VelocityAction() {
        velocity = new Vector3f(0, 0, 0);
    }

    public VelocityAction(Vector3f velocity_) {
        velocity = velocity_;
    }

    public Vector3f getVelocity() {
        return velocity;
    }
}
