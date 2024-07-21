package com.grave.Game.Entities;

import com.grave.Object.Actions.Action;
import com.grave.Object.Actions.MoveAction;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

public class Entity {
    protected Geometry geometry;

    public Entity(String name_, Mesh mesh_, Material material_)
    {
        geometry = new Geometry(name_, mesh_);

        geometry.setMaterial(material_);
    }

    public void onInit() {}
    
    public void onUpdate(float tpf) {}

    public void onShutdown() {
    }
    
    public void processAction(Action action) {
        if(action instanceof MoveAction) {
            MoveAction moveAction = (MoveAction) action;
            geometry.setLocalTranslation(moveAction.getPosition());
        }
    }

    public Geometry getGeometry() {
        return geometry;
    }
}
