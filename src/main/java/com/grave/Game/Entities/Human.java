package com.grave.Game.Entities;

import com.grave.Object.ObjectManager;
import com.jme3.material.Material;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.shape.Box;

@Serializable
public class Human extends RigEntity {
    public static final int MASS = 10;

    // necessary
    public Human() {
        super(null, null, null, null, 0);
    }

    public Human(ObjectManager objectManager_, String name_, Material material_) {
        super(objectManager_, name_, new Box(1, 1, 1), material_, MASS);

        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
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
