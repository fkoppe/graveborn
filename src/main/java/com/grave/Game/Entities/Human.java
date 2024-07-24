package com.grave.Game.Entities;

import com.grave.Uuid;
import com.grave.Object.ObjectManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Mesh;

public class Human extends RigEntity {
    public static final int MASS = 10;

    public Human(Uuid id_, Type type_, ObjectManager objectManager_, String name_, Mesh mesh_) {
        super(id_, type_, objectManager_, name_, mesh_);

        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);

        rig.setMass(MASS);
        rig.setFriction(0.5f);
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onUpdate(float tpf) {
        
    }

    @Override
    public void onShutdown() {

    }
}
