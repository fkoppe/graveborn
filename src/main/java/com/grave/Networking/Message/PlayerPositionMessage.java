package com.grave.Networking.Message;

import com.jme3.network.serializing.Serializable;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;

@Serializable
public class PlayerPositionMessage extends AbstractMessage {
    private Vector3f playerPosition;
    private String senderName;

    public PlayerPositionMessage(){
        playerPosition = null;
        senderName = null;
    }

    public PlayerPositionMessage(String senderName_, Vector3f playerPosition_) {
        senderName = senderName_;
        playerPosition = playerPosition_;
    }

    public String getSenderName() {
        return senderName;
    }

    public Vector3f getPlayerPosition() {
        return playerPosition;
     }
}
