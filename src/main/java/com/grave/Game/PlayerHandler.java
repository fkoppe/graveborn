package com.grave.Game;

import com.grave.Graveborn;
import com.grave.UpdateHandler;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class PlayerHandler implements UpdateHandler{
    private final float SPEED = 5f;

    private Geometry player;
    private RigidBody2DControl player_rig;
    private Graveborn application;

    private int moveVert;
    private int moveHori;

    public PlayerHandler(Graveborn application_){
        application = application_;

        initKeys();
        initPlayer();
        initTestObstacle();

        moveVert = 0;
        moveHori = 0;
    }

    private void initKeys() {
        application.getInputManager().addMapping("Up",  new KeyTrigger(KeyInput.KEY_W));
        application.getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        application.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        application.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        application.getInputManager().addListener(actionListener, "Up", "Down", "Left", "Right");
    }

    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            switch(name){
                case "Up":
                    moveVert = isPressed ? 1 : 0;
                    break;
                case "Down":
                    moveVert = isPressed ? -1 : 0;
                    break;
                case "Left":
                    moveHori = isPressed ? -1 : 0;
                    break;
                case "Right":
                    moveHori = isPressed ? 1 : 0;
                    break;
                default:
                    break;
            }
        }
    };
    private void initPlayer(){
        Box p = new Box(1,1,1);
        player = new Geometry("Player", p);
        player.setLocalTranslation(0,0,0);
        Material p_mat = new Material(application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        Texture characterTex = application.getAssetManager().loadTexture("Textures/character.png");
        characterTex.setMagFilter(Texture.MagFilter.Nearest);
        p_mat.setTexture("ColorMap", characterTex);
        p_mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        player.setQueueBucket(RenderQueue.Bucket.Transparent);
        player.setMaterial(p_mat);


        CollisionShape player_shape = CollisionShapeFactory.createBoxShape(player);
        player_rig = new RigidBody2DControl(player_shape, 10);
        player_rig.setRotation(false);
        player.addControl(player_rig);
        application.getPhysicsSpace().add(player_rig);

        application.getRootNode().attachChild(player);
    }

    private void initTestObstacle(){
        //Test Obstacle
        Box o_box = new Box(1, 1, 0.1f);
        Geometry o = new Geometry("Plane", o_box);
        o.setLocalTranslation(3f, 3f, 0);
        Material mat = new Material(application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        o.setMaterial(mat);
        CollisionShape o_shape = CollisionShapeFactory.createBoxShape(o);
        RigidBody2DControl o_rig = new RigidBody2DControl(o_shape, 0);
        o.addControl(o_rig);
        application.getPhysicsSpace().add(o_rig);
        application.getRootNode().attachChild(o);
    }

    @Override
    public void init() {
    }

    @Override
    public void shutdown() {
    }

    public void update(float tpf){
        player_rig.setLinearVelocity(new Vector3f(moveHori,moveVert,0).normalize().mult(SPEED));
    }

    public Geometry getPlayer() {
        return player;
    }
}
