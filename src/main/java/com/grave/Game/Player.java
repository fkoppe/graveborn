package com.grave.Game;

import java.security.KeyPair;
import java.util.ArrayList;

import com.grave.Graveborn;
import com.grave.Uuid;
import com.grave.Game.Entities.Bullet;
import com.grave.Game.Entities.Entity;
import com.grave.Game.Entities.Human;
import com.grave.Game.Entities.RigEntity;
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
import com.jme3.system.NanoTimer;

public class Player {
    private final float CAMERA_ZOOM = 8f;
    private final float CAMERA_MOVE_BORDER = 4f;
    private final float PLAYER_SPEED = 5f;
    private final float SPRINT_MULTIPLIER = 2.5f;

    private final float CROSSHAIR_LAYER = 10.0f;
    private final int CROSSHAIR_COUNT = 10;

    private Gun gun;

    private ObjectManager objectManager;
    private String playerName;

    private InputManager inputManager;
    private Camera camera;
    private ViewPort viewPort;
    private AssetManager assetManager;
    private Node rootNode;
    private GUI gui;

    private ArrayList<Geometry> crosshair;

    private Uuid humanID;

    private boolean shooting = false;
    private boolean sprinting = false;
    private boolean aiming = false;
    private int moveVertical = 0;
    private int moveHorizontal = 0;

    private boolean crosshairAttached = false;

    private NanoTimer shootTimer;
    private NanoTimer salvoTimer;
    private NanoTimer respawnTimer;

    private int salvo = 0;
    private int magazin = 0;
    private int kills = 0;

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
                case "Reload":
                    magazin = 0;
                    shootTimer.reset();
                    gui.setAmmoText(magazin);
                    break;
                case "AssaultRiffle":
                    gun = Gun.ASSAULT;
                    gui.setGun(gun);
                    magazin = 0;
                    shootTimer.reset();
                    gui.setAmmoText(magazin);
                    break;
                case "AssaultRiffle2":
                    gun = Gun.ASSAULT2;
                    gui.setGun(gun);
                    magazin = 0;
                    shootTimer.reset();
                    gui.setAmmoText(magazin);
                    break;
                case "MaschineGun":
                    gun = Gun.MACHINE;
                    gui.setGun(gun);
                    magazin = 0;
                    shootTimer.reset();
                    gui.setAmmoText(magazin);
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
        shootTimer = new NanoTimer();
        salvoTimer = new NanoTimer();
        respawnTimer = new NanoTimer();

        gui = new GUI(assetManager, app_.getGuiNode());

        app_.getFlyByCamera().setEnabled(false);

        gun = Gun.ASSAULT;
        gui.setGun(gun);
    }

    public void init() {
        initCamera();
        initCrosshair();
        initKeys();
        initGUI();

        spawnHuman();

        //TODO remove process calls
        proccessNew();
        proccessDeleted();
    }

    public void update(float tpf) {
        proccessNew();
        proccessDeleted();

        if(!objectManager.knownIs(humanID) && humanID != null)
        {
            respawnTimer.reset();
            humanID = null;
            System.out.println("player died with " + kills + " kills\n While being dead change weapon using 1,2,3");

            kills = 0;
            magazin = gun.getMagazine();

            gui.setAmmoText(magazin);
            gui.setKillText(kills);

            hideCrosshair();
        }
        
        if (objectManager.knownIs(humanID)) {
            Human h = (Human) objectManager.getEntity(humanID);

            kills = h.getKills();
        }

        if (!objectManager.knownIs(humanID) && respawnTimer.getTimeInSeconds() > 10) {
            spawnHuman();
        }

        if(null == humanID)
        {
            return;
        }

        if(shooting || aiming && gun == Gun.MACHINE)
        {
            moveHorizontal = 0;
            moveVertical = 0;
        }

        if(gun == Gun.MACHINE)
        {
            sprinting = false;
        }

        if (moveHorizontal != 0 || moveVertical != 0) {
            Vector3f moveVector = new Vector3f(moveHorizontal, moveVertical, 0).normalize().mult(PLAYER_SPEED);

            if(sprinting)
            {
                moveVector = moveVector.mult(SPRINT_MULTIPLIER);
            }

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
        handleCrosshair();

        if (shooting) {
            if (shootTimer.getTimeInSeconds() > gun.getGap() && magazin > 0) {
                if(salvoTimer.getTimeInSeconds() > gun.getSgap())
                {
                    salvoTimer.reset();
                    salvo++;
                    magazin--;
                    gui.setAmmoText(magazin);

                    Vector2f mousePositionScreen = inputManager.getCursorPosition();
                    Vector3f mouseLocation = camera.getWorldCoordinates(mousePositionScreen, 0);

                    Uuid bulletID = objectManager.createEntity(Type.BULLET, "bullet");

                    Bullet bullet = (Bullet) objectManager.getEntity(bulletID);
                    bullet.fire(humanID, mouseLocation, gun);
                }

                if(salvo >= gun.getSalvo())
                {
                    shootTimer.reset();
                    salvo = 0;
                }
            }
        }

        if (shootTimer.getTimeInSeconds() > gun.getReload() && magazin == 0) {
            magazin = gun.getMagazine();
            gui.setAmmoText(magazin);
        }
    }

    public void kill(){
        kills++;
        gui.setKillText(kills);
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

    private void initGUI(){
        magazin = gun.getMagazine();
        gui.setAmmoText(magazin);

        kills = 0;
        gui.setKillText(kills);
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
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Aim", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("Reload", new KeyTrigger(KeyInput.KEY_R));

        inputManager.addMapping("AssaultRiffle", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("AssaultRiffle2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("MaschineGun", new KeyTrigger(KeyInput.KEY_3));

        //inputManager.addMapping("CurserMove", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        //inputManager.addMapping("CurserMove", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        //inputManager.addMapping("CurserMove", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        //inputManager.addMapping("CurserMove", new MouseAxisTrigger(MouseInput.AXIS_Y, true));

        inputManager.addListener(actionListener, "Up", "Down", "Left", "Right", "Sprint", "Shoot", "Aim", "Reload", "AssaultRiffle", "AssaultRiffle2", "MaschineGun");
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
        if (aiming) {
            showCrosshair();

            Human human = (Human) objectManager.getEntity(humanID);

            Vector2f mousePositionScreen = inputManager.getCursorPosition();

            Vector3f playerLoc = human.getPosition();
            Vector3f mouseLoc = camera.getWorldCoordinates(mousePositionScreen, 0);

            Vector3f direction = mouseLoc.subtract(playerLoc);

            float lenght = direction.length();

            for (int i = 0; i < crosshair.size(); i++) {
                Vector3f pos = playerLoc.add(direction.normalize().mult(lenght / CROSSHAIR_COUNT * (1 + i)));
                crosshair.get(i).setLocalTranslation(pos.x, pos.y, CROSSHAIR_LAYER);
            }
        } else {
            hideCrosshair();
        }
    }

    private void hideCrosshair()
    {
        if (crosshairAttached) {
            for (Geometry dot : crosshair) {
                rootNode.detachChild(dot);
            }

            crosshairAttached = false;
        }
    }

    private void showCrosshair()
    {
        if (!crosshairAttached) {
            for (Geometry dot : crosshair) {
                rootNode.attachChild(dot);
            }

            crosshairAttached = true;
        }
    }
    
    private void spawnHuman()
    {
        respawnTimer.reset();

        shootTimer.reset();
        salvoTimer.reset();
        magazin = gun.getMagazine();

        ArrayList<Entity> array = objectManager.queryEntityByClass(RigEntity.class);

            boolean legit = false;
            Vector3f pos = new Vector3f(0, 0, 0);;

            while(!legit)
            {
                final int x_spawn = (int) ((Math.random() * (20 - -20)) + -20);
                final int y_spawn = (int) ((Math.random() * (20 - -20)) + -20);
                pos = new Vector3f(x_spawn, y_spawn, 0);

                legit = true;

                for (Entity e : array) {
                    if (e.getPosition().subtract(pos).length() < 5.0f) {
                        legit = false;
                    }
                }
            }
            
        humanID = objectManager.createEntity(Type.HUMAN, playerName);

        objectManager.submitEntityAction(humanID, new MoveAction(pos), true);
    }
}
