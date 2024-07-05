package com.grave;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Graveborn extends SimpleApplication {

    private Geometry bg;
    private Geometry player;

    public static void main(String[] args) {
        Graveborn app = new Graveborn();
        AppSettings settings = new AppSettings(true);
        settings.setCenterWindow(true);
        settings.setHeight(800);
        settings.setWidth(1000);
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }
    PlayerHandler playerHandler;
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(new ColorRGBA(1.0f, 0.8f, 1f, 1f));
        flyCam.setEnabled(false);

        cam.setLocation(new Vector3f(0,0,20));
        cam.lookAt(Vector3f.ZERO,Vector3f.UNIT_Z);

        Box b = new Box(10, 10, 0);
        bg = new Geometry("Plane", b);
        bg.setLocalTranslation(0f, 0f, -0.1f);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        bg.setMaterial(mat);

        rootNode.attachChild(bg);

        playerHandler = new PlayerHandler(inputManager,assetManager,rootNode, stateManager);

    }



    @Override
    public void simpleUpdate(float tpf) {
//        System.out.println(cam.getLocation());
        playerHandler.update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}

