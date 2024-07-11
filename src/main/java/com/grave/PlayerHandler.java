package com.grave;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class PlayerHandler implements UpdateHandler{
    private final float SPEED = 5f;

    private Geometry player;
    private final InputManager inputManager;
    private final AssetManager assetManager;
    private final Node rootNode;
    private final PhysicsSpace physicsSpace;
    public PlayerHandler(InputManager inputManager, AssetManager assetManager, Node rootNode, PhysicsSpace physicsSpace){
        this.inputManager = inputManager;
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.physicsSpace = physicsSpace;

        initKeys();
        initPlayer();

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
        physicsSpace.add(o_rig);
        rootNode.attachChild(o);
    }

    private void initKeys() {
        inputManager.addMapping("Up",  new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(actionListener, "Up", "Down", "Left", "Right");
    }

    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            Vector3f velocity = new Vector3f(0,0,0);
            switch(name){
                case "Up":
                    velocity.setY(isPressed ? 1 : 0);
                    break;
                case "Down":
                    velocity.setY(isPressed ? -1 : 0);
                    break;
                case "Left":
                    velocity.setX(isPressed ? -1 : 0);
                    break;
                case "Right":
                    velocity.setX(isPressed ? 1 : 0);
                    break;
                default:
                    break;
            }
            player_rig.setLinearVelocity(velocity.normalize().mult(SPEED));
        }
    };
    private RigidBody2DControl player_rig;
    private void initPlayer(){
        Box p = new Box(1,1,1);
        player = new Geometry("Player", p);
        player.setLocalTranslation(0,0,0);
        Material p_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture characterTex = assetManager.loadTexture("Textures/character.png");
        characterTex.setMagFilter(Texture.MagFilter.Nearest);
        p_mat.setTexture("ColorMap", characterTex);
        p_mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        player.setQueueBucket(RenderQueue.Bucket.Transparent);
        player.setMaterial(p_mat);

        CollisionShape player_shape = CollisionShapeFactory.createBoxShape(player);

        player_rig = new RigidBody2DControl(player_shape, 10);
        player_rig.setRotation(false);
        player.addControl(player_rig);
        physicsSpace.add(player_rig);
        rootNode.attachChild(player);
    }

    public void update(float tpf){
        //TODO: Implement Input in update

        System.out.println(player_rig.getLinearVelocity());

    }

    public Vector3f getPosition(){
        return player.getLocalTranslation();
    }
}
