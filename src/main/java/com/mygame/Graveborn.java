package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

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

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(new ColorRGBA(1.0f, 0.8f, 1f, 1f));
        flyCam.setEnabled(false);

        cam.setLocation(new Vector3f(0,0,18));
        cam.setRotation(new Quaternion(0,0,0,0));
        cam.lookAt(Vector3f.ZERO,Vector3f.UNIT_Z);

        Box b = new Box(10, 10, 0);
        bg = new Geometry("Plane", b);
        bg.setLocalTranslation(0f, 0f, 0f);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        bg.setMaterial(mat);

        Sphere p = new Sphere(100,100,1f);
        player = new Geometry("Player", p);
        player.setLocalTranslation(0,0,0);
        Material p_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        p_mat.setColor("Color", ColorRGBA.Blue);
        player.setMaterial(p_mat);

        rootNode.attachChild(player);
        rootNode.attachChild(bg);

        initKeys();
    }

    private void initKeys() {
        inputManager.addMapping("Up",  new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down",   new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(analogListener, "Up", "Down", "Left", "Right");
    }

    final private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            Vector3f v = player.getLocalTranslation();
            switch(name){
                case "Up":
                    player.setLocalTranslation(v.x,v.y+0.1f,v.z);
                    break;
                case "Down":
                    player.setLocalTranslation(v.x,v.y-0.1f,v.z);
                    break;
                case "Left":
                    player.setLocalTranslation(v.x-0.1f,v.y,v.z);
                    break;
                case "Right":
                    player.setLocalTranslation(v.x+0.1f,v.y,v.z);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}

