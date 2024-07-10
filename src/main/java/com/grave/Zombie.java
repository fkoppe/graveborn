package com.grave;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Zombie extends Geometry implements UpdateHandler{
    private final float SPEED = 3f;
    private final RigidBody2DControl zombieRig;
    private final ObjectManager objectManager;
    public Zombie(String name, Vector3f pos, ObjectManager objectManager, AssetManager assetManager, PhysicsSpace physicsSpace){
        super(name, new Box(1,1,1));

        this.objectManager = objectManager;
        this.setLocalTranslation(pos);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        this.setMaterial(mat);

        CollisionShape zombieShape = CollisionShapeFactory.createBoxShape(this);
        zombieRig = new RigidBody2DControl(zombieShape, 1);
        zombieRig.setRotation(false);
        this.addControl(zombieRig);
        physicsSpace.add(zombieRig);
    }


    @Override
    public void update(float tpf) {
        Vector3f playerPos = objectManager.getPlayerPos();
        Vector3f moveVector = playerPos.subtract(this.getLocalTranslation()).normalize();
        zombieRig.setLinearVelocity(moveVector.mult(SPEED));
    }
}
