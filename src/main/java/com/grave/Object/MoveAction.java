package com.grave.Object;

import com.jme3.math.Vector3f;

public class MoveAction extends Action {
    private Vector3f position;

    public MoveAction(Vector3f position_) {
        position = position_;
    }

    public Vector3f getPosition() {
        return position;
    }
}
