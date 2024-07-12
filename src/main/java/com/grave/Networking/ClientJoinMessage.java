package com.grave.Networking;

import com.jme3.network.serializing.Serializable;
import com.jme3.network.AbstractMessage;

@Serializable
public class ClientJoinMessage extends AbstractMessage {
    private String name;

    public ClientJoinMessage() {
        name = null;
    }

    public ClientJoinMessage(String name_) {
        name = name_;
    }

    public String getName() {
        return name;
    }
}