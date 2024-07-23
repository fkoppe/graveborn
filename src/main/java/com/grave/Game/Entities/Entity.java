package com.grave.Game.Entities;

import com.grave.Uuid;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.Action;
import com.grave.Object.Actions.MoveAction;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

public class Entity {
    protected Uuid id;
    protected Type type;

    protected Geometry geometry;
    protected ObjectManager objectManager;

    public Entity(Uuid id_, Type type_, ObjectManager objectManager_, String name_, Mesh mesh_)
    {
        id = id_;
        type = type_;

        geometry = new Geometry(name_, mesh_);
        objectManager = objectManager_;
    }

    public void onInit() {}
    
    public void onUpdate(float tpf) {}

    public void onShutdown() {
    }
    
    public void processAction(Action action) {
        if (action instanceof MoveAction) {
            MoveAction moveAction = (MoveAction) action;
            geometry.setLocalTranslation(moveAction.getPosition());
        }
    }

    public void attachToNode(Node node) {
        node.attachChild(geometry);
    }

    public void detachFromNode(Node node) {
        node.detachChild(geometry);
    }

    public void setMaterial(Material material) {
        geometry.setMaterial(material);
    }

    public Vector3f getPosition() {
        return geometry.getLocalTranslation();
    }

    public String getName() {
        return geometry.getName();
    }

    public Uuid getId() {
        return id;
    }

    public Type getType() {
        return type;
    }
}
