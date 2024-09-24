package com.grave.Game;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public class GUI {
    BitmapText ammoText;
    final String AMMO_STRING = "Ammunition: ";

    BitmapText killText;
    final String KILL_STRING = "Kills: ";

    BitmapText gunText;
    final String GUN_STRING = "Gun: ";

    Gun gun = null;

    public GUI(AssetManager assetManager, Node guiNode){
        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Console.fnt");

        ammoText = new BitmapText(guiFont);
        ammoText.setSize(guiFont.getCharSet().getRenderedSize()*3f);      // font size
        ammoText.setColor(ColorRGBA.White);                             // font color
        ammoText.setText(AMMO_STRING);             // the text
        ammoText.setLocalTranslation(10, ammoText.getLineHeight(), 0); // position
        guiNode.attachChild(ammoText);

        killText = new BitmapText(guiFont);
        killText.setSize(guiFont.getCharSet().getRenderedSize()*3f);      // font size
        killText.setColor(ColorRGBA.White);                             // font color
        killText.setText(KILL_STRING);             // the text
        killText.setLocalTranslation(10, killText.getLineHeight()+5+ammoText.getLineHeight(), 0); // position
        guiNode.attachChild(killText);

        gunText = new BitmapText(guiFont);
        gunText.setSize(guiFont.getCharSet().getRenderedSize() * 3f); // font
                                                                       // size
        gunText.setColor(ColorRGBA.White); // font color
        gunText.setText(GUN_STRING); // the text
        gunText.setLocalTranslation(10, gunText.getLineHeight() + 5 + killText.getLineHeight() + 5 + ammoText.getLineHeight(), 0); // position
        guiNode.attachChild(gunText);
    }

    public void setAmmoText(int ammo) {
        if(gun == null)
        {
            ammoText.setText(AMMO_STRING + ammo);
        }
        else
        {
            ammoText.setText(AMMO_STRING + ammo + "/" + gun.getMagazine());
        }
    }

    public void setKillText(int kills) {
        killText.setText(KILL_STRING + kills);
    }

    public void setGun(Gun gun) {
        this.gun = gun;
        
        gunText.setText(GUN_STRING + gun.name());
    }
}
