package com.grave.Game;

import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

public abstract class Entity extends Geometry {
    public Entity(String name_, Mesh mesh_)
    {
        super(name_, mesh_);
    }

    abstract public void onInit();
    abstract public void onShutdown();
    abstract public void onUpdate(float tpf);
}
