package com.grave.Networking;

import com.jme3.network.serializing.Serializable;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;

@Serializable
public class PlayerPositionMessage extends AbstractMessage {
    private Vector3f playerPosition;

    public PlayerPositionMessage()
    {

    }

    public PlayerPositionMessage(Vector3f playerPosition_) {
        playerPosition = playerPosition_;
    }
    
    
     public Vector3f getPlayerPosition() {
        return playerPosition;
     }
}
