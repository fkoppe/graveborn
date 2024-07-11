package com.grave;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Zombie extends Geometry implements UpdateHandler{

    private final float SPEED = 3f;

    private RigidBody2DControl zombieRig;
    private Graveborn application;

    public Zombie(String name, Vector3f pos, Graveborn application_){
        super(name, new Box(1,1,1));
        application = application_;
        this.setLocalTranslation(pos);

        Material mat = new Material(application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        this.setMaterial(mat);

        CollisionShape zombieShape = CollisionShapeFactory.createBoxShape(this);
        zombieRig = new RigidBody2DControl(zombieShape, 1);
        zombieRig.setRotation(false);
        this.addControl(zombieRig);
        application.getPhysicsSpace().add(zombieRig);
    }


    @Override
    public void update(float tpf) {
        Vector3f playerPos = application.getObjectManager().getPlayerPos();
        Vector3f moveVector = playerPos.subtract(this.getLocalTranslation()).normalize();
        zombieRig.setLinearVelocity(moveVector.mult(SPEED));
    }
}
