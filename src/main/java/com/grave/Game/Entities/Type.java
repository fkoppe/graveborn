package com.grave.Game.Entities;

import com.grave.Uuid;
import com.grave.Object.ObjectManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

@Serializable
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
    },
    BULLET{
    @Override
    public Entity build(Uuid id, ObjectManager objectManager_, String name_) {
        Bullet entity = new Bullet(id, Type.BULLET, objectManager_, name_, new Box(0.2f, 0.2f, 0.2f));
        return entity;
    }

    @Override
    public Material buildMaterial(AssetManager assetManager) {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Pink);
        return material;
    }

    };

    public Entity build(Uuid id, ObjectManager objectManager_, String name_) {
        throw new RuntimeException("no entity type specified");
    }

    public Material buildMaterial(AssetManager assetManager) {
        throw new RuntimeException("no entity type specified");
    }
}