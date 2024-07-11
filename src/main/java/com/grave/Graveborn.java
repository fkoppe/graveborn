package com.grave;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
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

    private ObjectManager objectManager;
    private GameClient gameClient;
    private  PhysicsSpace physicsSpace;

    @Override
    public void simpleInitApp() {
        //TODO:
        //Player in ObjectManager
        //PlayerHandler Input Fix
        physicsSpace = null;
        objectManager = new ObjectManager(this);
        gameClient = new GameClient(this);
    }

    @Override
    public void simpleUpdate(float tpf) {
        objectManager.update(tpf);
        gameClient.update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public PhysicsSpace getPhysicsSpace() {
        return physicsSpace;
    }

    public void setPhysicsSpace(PhysicsSpace physicsSpace_) {
        physicsSpace = physicsSpace_;
    }
}

