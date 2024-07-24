package com.grave.Game;

import com.grave.Graveborn;
import com.grave.Uuid;
import com.grave.Game.Entities.Entity;
import com.grave.Game.Entities.Human;
import com.grave.Game.Entities.Type;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.MoveAction;
import com.grave.Object.Actions.VelocityAction;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

public class Player {
    private final float CAMERA_ZOOM = 8f;
    private final float PLAYER_SPEED = 5f;

    private ObjectManager objectManager;
    private String playerName;

    private InputManager inputManager;
    private Camera camera;
    private ViewPort viewPort;
    private AssetManager assetManager;
    private Node rootNode;

    private Uuid humanID;

    private boolean moving = false;
    private int moveVertical = 0;
    private int moveHorizontal = 0;

    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            switch (name) {
                case "Up":
                    moveVertical = isPressed ? 1 : 0;
                    break;
                case "Down":
                    moveVertical = isPressed ? -1 : 0;
                    break;
                case "Left":
                    moveHorizontal = isPressed ? -1 : 0;
                    break;
                case "Right":
                    moveHorizontal = isPressed ? 1 : 0;
                    break;
                default:
                    break;
            }
        }
    };

    public Player(Graveborn app_, ObjectManager objectManager_, String playerName_) {
        objectManager = objectManager_;
        playerName = playerName_;

        inputManager = app_.getInputManager();
        viewPort = app_.getViewPort();
        camera = app_.getCamera();
        assetManager = app_.getAssetManager();
        rootNode = app_.getRootNode();

        app_.getFlyByCamera().setEnabled(false);
    }

    public void init() {
        initCamera();
        
        initKeys();

        humanID = objectManager.createEntity(Type.HUMAN, playerName);

        final int x_spawn = (int) ((Math.random() * (0 - -5)) + -5);
        final int y_spawn = (int) ((Math.random() * (0 - -10)) + -10);

        objectManager.submitEntityAction(humanID, new MoveAction(new Vector3f(x_spawn, y_spawn, 0)));

        proccessNew();
        proccessDeleted();
    }

    public void update(float tpf) {
        proccessNew();
        proccessDeleted();

        if (moveHorizontal != 0 || moveVertical != 0) {
            moving = true;
            VelocityAction action = new VelocityAction(new Vector3f(moveHorizontal, moveVertical, 0).normalize().mult(PLAYER_SPEED));
            objectManager.submitEntityAction(humanID, action);
        } else {
            if (moving) {
                moving = false;

                VelocityAction action = new VelocityAction(new Vector3f(0, 0, 0));
                objectManager.submitEntityAction(humanID, action);
            }
        }
    }

    public void shutdown() {
    }

    private void proccessNew() {
        objectManager.getLocalEntitiesNew().forEach((uuid, entity) -> {
            Material material = entity.getType().buildMaterial(assetManager);
            entity.setMaterial(material);

            entity.attachToNode(rootNode);
        });
    }

    private void proccessDeleted() {
        objectManager.getLocalEntitiesDeleted().forEach((uuid, entity) -> {
            entity.detachFromNode(rootNode);
        });
    }

    private void initCamera() {
        camera.setLocation(new Vector3f(0, 0, 20));
        camera.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z);
        camera.setParallelProjection(true);

        float aspect = (float) camera.getWidth() / camera.getHeight();
        float size = CAMERA_ZOOM;
        camera.setFrustum(-1000, 1000, -aspect * size, aspect * size, size, -size);
    }

    private void initKeys() {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));

        inputManager.addListener(actionListener, "Up", "Down", "Left", "Right");
    }
}
