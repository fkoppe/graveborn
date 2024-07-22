package com.grave.Game;

import java.util.UUID;

import com.grave.Graveborn;
import com.grave.Game.Entities.Entity;
import com.grave.Game.Entities.Human;
import com.grave.Game.Entities.RigEntity;
import com.grave.Object.ObjectManager;
import com.grave.Object.Actions.MoveAction;
import com.grave.Object.Actions.VelocityAction;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
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

    private UUID playerID;
    private UUID backgroundID;

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
        initBackground();
        
        initKeys();
        initPlayer();

        //TODO
        // On Space_Key spawn Zombie
        //inputManager.addMapping("spawn", new KeyTrigger(KeyInput.KEY_SPACE));
        //inputManager.addListener((ActionListener) (name, isPressed, tpf) -> {
        //    if (isPressed) objectManager.spawnZombie();
        //}, "spawn");
    }

    public void update(float tpf) {
        proccessNew();
        proccessDeleted();

        VelocityAction action = new VelocityAction(new Vector3f(moveHorizontal, moveVertical, 0).normalize().mult(PLAYER_SPEED));
        objectManager.submitEntityAction(playerID, action);
    }

    public void shutdown() {
    }

    private void proccessNew() {
        objectManager.getLocalEntitiesNew().forEach((uuid, entity) -> {
            entity.attachToNode(rootNode);
        });
    }

    private void proccessDeleted() {
        objectManager.getLocalEntitiesDeleted().forEach((uuid, entity) -> {
            entity.detachFromNode(rootNode);
        });
    }

    private void initBackground() {
        viewPort.setBackgroundColor(new ColorRGBA(1.0f, 0.8f, 1f, 1f));

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Red);

        Entity background = new Entity(objectManager, "background", new Box(10, 10, 0), material);

        backgroundID = objectManager.createEntity(background);

        objectManager.submitEntityAction(backgroundID, new MoveAction( new Vector3f(0f, 0f, -0.01f)));
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

    private void initPlayer(){
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = assetManager.loadTexture("Textures/character.png");

        texture.setMagFilter(Texture.MagFilter.Nearest);

        material.setTexture("ColorMap", texture);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        Entity player = new Human(objectManager, "player", material);

        playerID = objectManager.createEntity(player);

        objectManager.submitEntityAction(playerID, new MoveAction(new Vector3f(-3, -3, 0)));
    }
}
