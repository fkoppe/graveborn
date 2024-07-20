package com.grave.Game;

import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.shape.Box;

public class Human extends RigEntity {
    public static final int MASS = 10;

    public Human(String name_, Material material_) {
        super(name_, new Box(1, 1, 1), material_, MASS);

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
