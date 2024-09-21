package com.grave.Game;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public class GUI {
    BitmapText ammoText;
    String AMMO_STRING = "Ammunition: ";

    BitmapText killText;
    String KILL_STRING = "Kills: ";

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
    }

    public void setAmmoText(int ammo){
        ammoText.setText(AMMO_STRING + ammo);
    }

    public void setKillText(int kills) {
        killText.setText(KILL_STRING + kills);
    }
}
