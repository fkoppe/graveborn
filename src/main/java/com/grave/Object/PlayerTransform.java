package com.grave.Object;

import com.jme3.math.Vector3f;

public class PlayerTransform {
    Vector3f position;

    public PlayerTransform(Vector3f position_) {
        position = position_;
    }

    public Vector3f getPosition() {
        return position;
    }
}
