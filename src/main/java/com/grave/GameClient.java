package com.grave;

import com.jme3.bullet.BulletAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class GameClient implements UpdateHandler {
    private final float ZOOM = 8f;

    private Graveborn application;
    private PlayerHandler playerHandler;

    public GameClient(Graveborn application_){
        application = application_;

        BulletAppState bulletAppState = new BulletAppState();
        application.getStateManager().attach(bulletAppState);
        application.setPhysicsSpace(bulletAppState.getPhysicsSpace());
        application.getPhysicsSpace().setGravity(Vector3f.ZERO);

        playerHandler = new PlayerHandler(application);
        application.getObjectManager().add(playerHandler.getPlayer());

        initCam();
        initBG();

        //On Space_Key spawn Zombie
        application.getInputManager().addMapping("spawn", new KeyTrigger(KeyInput.KEY_SPACE));
        application.getInputManager().addListener((ActionListener) (name, isPressed, tpf) -> {
            if(isPressed) application.getObjectManager().spawnZombie();
        }, "spawn");
    }

    private void initBG(){
        application.getViewPort().setBackgroundColor(new ColorRGBA(1.0f, 0.8f, 1f, 1f));
        Box b = new Box(10, 10, 0);
        Geometry bg = new Geometry("Plane", b);
        bg.setLocalTranslation(0f, 0f, -0.1f);
        Material mat = new Material(application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        bg.setMaterial(mat);

        application.getRootNode().attachChild(bg);
    }

    private void initCam(){
        application.getFlyByCamera().setEnabled(false);
        application.getCamera().setLocation(new Vector3f(0,0,20));
        application.getCamera().lookAt(Vector3f.ZERO,Vector3f.UNIT_Z);
        application.getCamera().setParallelProjection(true);
        float aspect = (float) application.getCamera().getWidth() / application.getCamera().getHeight();
        float size = ZOOM;
        application.getCamera().setFrustum(-1000,1000, -aspect*size, aspect*size, size, -size);
    }

    @Override
    public void update(float tpf) {
        playerHandler.update(tpf);
    }
}
