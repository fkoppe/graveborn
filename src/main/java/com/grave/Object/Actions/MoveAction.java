package com.grave.Object.Actions;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

@Serializable
public class MoveAction extends Action {
    private Vector3f position;

    // necessary
    public MoveAction() {
        position = new Vector3f(0, 0, 0);
    }

    public MoveAction(Vector3f position_) {
        position = position_;
    }

    public Vector3f getPosition() {
        return position;
    }
}
