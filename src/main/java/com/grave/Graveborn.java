package com.grave;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
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
    private final float ZOOM = 8f;
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
    private PlayerHandler playerHandler;
    private ObjectManager objectManager;

    @Override
    public void simpleInitApp() {
        //TODO:
        //InitKlasse (GameClient) -- ObjectManager außerhalb
        //Player in ObjectManager
        //PlayerHandler Input Fix

        initCam();
        initBG();

        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();
        physicsSpace.setGravity(Vector3f.ZERO);

        playerHandler = new PlayerHandler(inputManager,assetManager,rootNode, physicsSpace);
        objectManager = new ObjectManager(assetManager, rootNode, physicsSpace, playerHandler);

        //On Space_Key spawn Zombie
        inputManager.addMapping("spawn", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener((ActionListener) (name, isPressed, tpf) -> {
            if(isPressed) objectManager.spawnZombie();
        }, "spawn");
    }

    private void initBG(){
        viewPort.setBackgroundColor(new ColorRGBA(1.0f, 0.8f, 1f, 1f));
        Box b = new Box(10, 10, 0);
        bg = new Geometry("Plane", b);
        bg.setLocalTranslation(0f, 0f, -0.1f);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        bg.setMaterial(mat);

        rootNode.attachChild(bg);
    }

    private void initCam(){
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f(0,0,20));
        cam.lookAt(Vector3f.ZERO,Vector3f.UNIT_Z);
        cam.setParallelProjection(true);
        float aspect = (float) cam.getWidth() / cam.getHeight();
        float size = ZOOM;
        cam.setFrustum(-1000,1000, -aspect*size, aspect*size, size, -size);
    }

    @Override
    public void simpleUpdate(float tpf) {
        playerHandler.update(tpf);
        objectManager.update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}

