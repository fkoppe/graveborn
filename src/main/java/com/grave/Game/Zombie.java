package com.grave.Game;

import com.grave.Object.ObjectManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Box;

public class Zombie extends Entity {
    private final float SPEED = 3f;

    private ObjectManager objectManager;

    //belong of Objectmanager
    private AssetManager assetManager;

    private RigidBody2DControl zombieRig;

    public Zombie(ObjectManager objectManager_, String name_, Vector3f pos_){
        super(name_, new Box(1, 1, 1));

        objectManager = objectManager_;

        assetManager = objectManager.getAssetManager();
        
        this.setLocalTranslation(pos_);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        this.setMaterial(mat);

        CollisionShape zombieShape = CollisionShapeFactory.createBoxShape(this);
        zombieRig = new RigidBody2DControl(zombieShape, 1);
        zombieRig.setRotation(false);
        this.addControl(zombieRig);
        objectManager.getPhysicsSpace().add(zombieRig);
    }

    @Override
    public void onInit() {
        //...
    }

    @Override
    public void onUpdate(float tpf) {
        Vector3f playerPos = objectManager.getPlayerPos();
        Vector3f moveVector = playerPos.subtract(this.getLocalTranslation()).normalize();
        zombieRig.setLinearVelocity(moveVector.mult(SPEED));
    }

    @Override
    public void onShutdown() {
        // ...
    }
}
