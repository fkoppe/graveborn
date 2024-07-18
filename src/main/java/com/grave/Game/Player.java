package com.grave.Game;

import com.grave.Graveborn;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.FlyByCamera;
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
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.scene.Node;

public class Player {
    private final float CAMERA_ZOOM = 8f;
    private final float PLAYER_SPEED = 5f;

    private ObjectManager objectManager;

    //belong of Graveborn
    private InputManager inputManager;
    private Camera camera;
    private ViewPort viewPort;

    //belong of Objectmanager
    private AssetManager assetManager;
    private Node rootNode;

    private Entity player;
    private RigidBody2DControl playerRig;
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

    public Player(Graveborn app, ObjectManager objectManager_) {
        inputManager = app.getInputManager();
        viewPort = app.getViewPort();
        camera = app.getCamera();

        objectManager = objectManager_;

        assetManager = objectManager.getAssetManager();
        rootNode = objectManager.getRootNode();

        app.getFlyByCamera().setEnabled(false);
    }

    public void init() {
        initCamera();
        initBackground();
        
        initKeys();
        initPlayer();
        initTestObstacle();

        //TODO
        // On Space_Key spawn Zombie
        inputManager.addMapping("spawn", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener((ActionListener) (name, isPressed, tpf) -> {
            if (isPressed) objectManager.spawnZombie();
        }, "spawn");
    }

    public void update(float tpf) {
        playerRig.setLinearVelocity(new Vector3f(moveHorizontal, moveVertical, 0).normalize().mult(PLAYER_SPEED));

        //application.getNetClient().setPlayerPosition(player_rig.getPhysicsLocation());
    }

    public void shutdown() {
    }

    private void initBackground() {
        viewPort.setBackgroundColor(new ColorRGBA(1.0f, 0.8f, 1f, 1f));

        Box b = new Box(10, 10, 0);

        Geometry bg = new Geometry("Plane", b);
        bg.setLocalTranslation(0f, 0f, -0.1f);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        bg.setMaterial(mat);

        rootNode.attachChild(bg);
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

        Material p_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture characterTex = assetManager.loadTexture("Textures/character.png");
        characterTex.setMagFilter(Texture.MagFilter.Nearest);
        p_mat.setTexture("ColorMap", characterTex);
        p_mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        player = new Entity("Player", new Box(1, 1, 1)) {
            @Override
            public void onInit() {

            }

            @Override
            public void onShutdown() {

            }

            @Override
            public void onUpdate(float tpf) {

            }
        };
        player.setLocalTranslation(0, 0, 0);
        player.setQueueBucket(RenderQueue.Bucket.Transparent);
        player.setMaterial(p_mat);

        CollisionShape player_shape = CollisionShapeFactory.createBoxShape(player);
        playerRig = new RigidBody2DControl(player_shape, 10);
        playerRig.setRotation(false);
        player.addControl(playerRig);
        rootNode.attachChild(player);

        objectManager.setHuman(player);
        objectManager.getPhysicsSpace().add(playerRig);
    }

    private void initTestObstacle(){
        //Test Obstacle
        Box o_box = new Box(1, 1, 0.1f);
        Geometry o = new Geometry("Plane", o_box);
        o.setLocalTranslation(3f, 3f, 0);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        o.setMaterial(mat);
        CollisionShape o_shape = CollisionShapeFactory.createBoxShape(o);
        RigidBody2DControl o_rig = new RigidBody2DControl(o_shape, 0);
        o.addControl(o_rig);
        rootNode.attachChild(o);

        //TODO
        objectManager.getPhysicsSpace().add(o_rig);
    }
}
