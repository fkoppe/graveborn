package com.grave.Game.Entities;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

public abstract class Entity {
    protected Geometry geometry;

    public Entity(String name_, Mesh mesh_, Material material_)
    {
        geometry = new Geometry(name_, mesh_);

        geometry.setMaterial(material_);
    }

    abstract public void onInit();
    
    abstract public void onUpdate(float tpf);

    abstract public void onShutdown();

    public void setPosition(float x, float y, float z) {
        geometry.setLocalTranslation(x, y, z);
    }

    public Geometry getGeometry() {
        return geometry;
    }
}
