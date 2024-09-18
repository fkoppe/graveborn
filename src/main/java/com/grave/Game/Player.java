package com.grave.Game;

import java.security.KeyPair;
import java.util.ArrayList;

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
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;

public class Player {
    private final float CAMERA_ZOOM = 8f;
    private final float CAMERA_MOVE_BORDER = 4f;
    private final float PLAYER_SPEED = 5f;
    private final float SPRINT_MULTIPLIER = 3f;

    private final float CROSSHAIR_LAYER = 10.0f;
    private final int CROSSHAIR_COUNT = 10;

    private ObjectManager objectManager;
    private String playerName;

    private InputManager inputManager;
    private Camera camera;
    private ViewPort viewPort;
    private AssetManager assetManager;
    private Node rootNode;

    private ArrayList<Geometry> crosshair;

    private Uuid humanID;

    private boolean shooting = false;
    private boolean sprinting = false;
    private boolean aiming = false;
    private int moveVertical = 0;
    private int moveHorizontal = 0;

    private boolean crosshairAttached = false;

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
                case "Sprint":
                    sprinting = isPressed ? true : false;
                    break;
                case "Shoot":
                    shooting = isPressed ? true : false;
                    break;
                case "Aim":
                    aiming = isPressed ? true : false;
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

        crosshair = new ArrayList<Geometry>(CROSSHAIR_COUNT);

        app_.getFlyByCamera().setEnabled(false);
    }

    public void init() {
        initCamera();
        initCrosshair();
        initKeys();

        humanID = objectManager.createEntity(Type.HUMAN, playerName);

        final int x_spawn = (int) ((Math.random() * (0 - -5)) + -5);
        final int y_spawn = (int) ((Math.random() * (0 - -10)) + -10);

        objectManager.submitEntityAction(humanID, new MoveAction(new Vector3f(x_spawn, y_spawn, 0)), true);

        proccessNew();
        proccessDeleted();
    }

    public void update(float tpf) {
        proccessNew();
        proccessDeleted();

        if (moveHorizontal != 0 || moveVertical != 0) {
            Vector3f moveVector = new Vector3f(moveHorizontal, moveVertical, 0).normalize().mult(PLAYER_SPEED);

            if(sprinting)
            {
                moveVector = moveVector.mult(SPRINT_MULTIPLIER);
            }

            //if (shooting) {
            //    ShootAction = new ShootAction();
            //}

            VelocityAction action = new VelocityAction(moveVector);
            objectManager.submitEntityAction(humanID, action, true);
        } else {
            Human human = (Human)objectManager.getEntity(humanID);
            if (human.getVelocity().length() > 0) {
                VelocityAction action = new VelocityAction(new Vector3f(0, 0, 0));
                objectManager.submitEntityAction(humanID, action, true);
            }
        }

        handleCamera();

        if(aiming)
        {
            if(!crosshairAttached)
            {
                for (Geometry dot : crosshair) {
                    rootNode.attachChild(dot);
                }

                crosshairAttached = true;
            }

            handleCrosshair();
        }
        else
        {
            if (crosshairAttached) {
                for (Geometry dot : crosshair) {
                    rootNode.detachChild(dot);
                }

                crosshairAttached = false;
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

    private void initCrosshair()
    {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Brown);

        for(int i = 0; i < CROSSHAIR_COUNT; i++)
        {
            Geometry dot = new Geometry("crosshair", new Box(0.1f, 0.1f, 1));
            dot.setMaterial(material);
            dot.setLocalTranslation(0, 0, CROSSHAIR_LAYER);

            crosshair.add(dot);
        }
    }

    private void initKeys() {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Sprint", new KeyTrigger(KeyInput.KEY_LCONTROL));
        inputManager.addMapping("Shoot", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Aim", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        //inputManager.addMapping("CurserMove", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        //inputManager.addMapping("CurserMove", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        //inputManager.addMapping("CurserMove", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        //inputManager.addMapping("CurserMove", new MouseAxisTrigger(MouseInput.AXIS_Y, true));

        inputManager.addListener(actionListener, "Up", "Down", "Left", "Right", "Sprint", "Shoot", "Aim");
    }

    private void handleCamera() {
        Vector3f camLoc = camera.getLocation();
        Vector3f playerLoc = objectManager.getEntity(humanID).getPosition();

        Vector3f bottomLeft = camera.getWorldCoordinates(new Vector2f(0, 0), 0);
        Vector3f topRight = camera.getWorldCoordinates(new Vector2f(camera.getWidth(), camera.getHeight()), 0);

        float top = topRight.y;
        float bottom = bottomLeft.y;
        float left = bottomLeft.x;
        float right = topRight.x;

        if (playerLoc.y > top - CAMERA_MOVE_BORDER) {
            camera.setLocation(new Vector3f(camLoc.x, camLoc.y + (playerLoc.y - (top - CAMERA_MOVE_BORDER)), camLoc.z));
        }
        if (playerLoc.y < bottom + CAMERA_MOVE_BORDER) {
            camera.setLocation(new Vector3f(camLoc.x, camLoc.y - ((bottom + CAMERA_MOVE_BORDER) - playerLoc.y), camLoc.z));
        }
        if (playerLoc.x < left + CAMERA_MOVE_BORDER) {
            camera.setLocation(new Vector3f(camLoc.x - ((left + CAMERA_MOVE_BORDER) - playerLoc.x), camLoc.y, camLoc.z));
        }
        if (playerLoc.x > right - CAMERA_MOVE_BORDER) {
            camera.setLocation(new Vector3f(camLoc.x + (playerLoc.x - (right - CAMERA_MOVE_BORDER)), camLoc.y, camLoc.z));
        }
    }

    private void handleCrosshair()
    {
        Human human = (Human)objectManager.getEntity(humanID);

        Vector2f mousePositionScreen = inputManager.getCursorPosition();

        Vector3f playerLoc = human.getPosition();
        Vector3f mouseLoc = camera.getWorldCoordinates(mousePositionScreen, 0);
        
        Vector3f direction = mouseLoc.subtract(playerLoc);

        float lenght = direction.length();
        
        for (int i = 0; i < crosshair.size(); i++) {
            Vector3f pos = playerLoc.add(direction.normalize().mult(lenght / CROSSHAIR_COUNT * (1 + i)));
            crosshair.get(i).setLocalTranslation(pos.x, pos.y, CROSSHAIR_LAYER);
        }

        //crosshair.setLocalTranslation(playerLoc.x, playerLoc.y, CROSSHAIR_LAYER);

        //crosshair.lookAt(new Vector3f(direction.getX(), direction.getY(), CROSSHAIR_LAYER), new Vector3f(0, 1, 0));
    }
}
