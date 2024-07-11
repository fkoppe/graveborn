package com.grave;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

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

