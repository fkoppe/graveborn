package com.grave.Game;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class Human extends Entity{
    public Human(String name_, ObjectManager objectmanager_) {
        super(name_, new Box(1, 1, 1));

        //this.setLocalTranslation(0, 0, 0);
        //Material oMat = new Material(objectmanager_.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        //Texture oTex = om.getAssetManager().loadTexture("Textures/character.png");
        //oTex.setMagFilter(Texture.MagFilter.Nearest);
        //oMat.setTexture("ColorMap", oTex);
        //oMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        //this.setQueueBucket(RenderQueue.Bucket.Transparent);
        //this.setMaterial(oMat);
        //om.getRootNode().attachChild(this);
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
