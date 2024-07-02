package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Graveborn extends SimpleApplication {

    private Geometry cube1;
    private Geometry cube2;
    
    public static void main(String[] args) {
        Graveborn app = new Graveborn();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(new ColorRGBA(1.0f, 0.8f, 1f, 1f));
        
        Box b = new Box(1, 1, 1);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/zombie.png"));
        
        cube1 = new Geometry("Cube1", b);
        cube1.setMaterial(mat);
        cube1.setLocalTranslation(0f, 0f, 0f);
        rootNode.attachChild(cube1);
        
        cube2 = new Geometry("Cube2", b);
        cube2.setMaterial(mat);
        cube2.setLocalTranslation(-4f, -4f, -4f);
        rootNode.attachChild(cube2);
    }

    @Override
    public void simpleUpdate(float tpf) {
        cube1.rotate(0.01f, 0.01f, 0.01f);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}

