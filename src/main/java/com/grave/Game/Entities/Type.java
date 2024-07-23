package com.grave.Game.Entities;

import com.grave.Uuid;
import com.grave.Object.ObjectManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public enum Type {
    NONE,
    BACKGROUND {
        @Override
        public Entity build(Uuid id, ObjectManager objectManager_, String name_) {
            Entity entity = new Entity(id, Type.BACKGROUND, objectManager_, name_, new Box(10, 10, 1));
            return entity;
        }
        
        @Override
        public Material buildMaterial(AssetManager assetManager) {
            Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            material.setColor("Color", ColorRGBA.Red);
            return material;
        }
    },
    OBSTACKLE {
        @Override
        public Entity build(Uuid id, ObjectManager objectManager_, String name_) {
            RigEntity entity = new RigEntity(id, Type.OBSTACKLE, objectManager_, name_, new Box(1, 1, 1));
            return entity;
        }
        @Override
        public Material buildMaterial(AssetManager assetManager) {
            Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            material.setColor("Color", ColorRGBA.Black);
            return material;
        }
    },
    HUMAN {
        @Override
        public Entity build(Uuid id, ObjectManager objectManager_, String name_) {
            Human entity = new Human(id, Type.HUMAN, objectManager_, name_, new Box(1, 1, 1));
            return entity;
        }
        @Override
        public Material buildMaterial(AssetManager assetManager) {
            Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            Texture texture = assetManager.loadTexture("Textures/character.png");

            texture.setMagFilter(Texture.MagFilter.Nearest);

            material.setTexture("ColorMap", texture);
            material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

            return material;
        }
    },
    ZOMBIE {
        @Override
        public Entity build(Uuid id, ObjectManager objectManager_, String name_) {
            Zombie entity = new Zombie(id, Type.ZOMBIE, objectManager_, name_, new Box(1, 1, 1));
            return entity;
        }
        
        @Override
        public Material buildMaterial(AssetManager assetManager) {
            Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            material.setColor("Color", ColorRGBA.Green);
            return material;
        }
    };

    public Entity build(Uuid id, ObjectManager objectManager_, String name_) {
        return new Entity(id, NONE, objectManager_, name_, new Box(0, 0, 0));
    }

    public Material buildMaterial(AssetManager assetManager) {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        return material;
    }
}