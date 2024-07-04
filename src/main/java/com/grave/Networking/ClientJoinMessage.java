package com.grave.Networking;

import com.jme3.network.serializing.Serializable;
import com.jme3.network.AbstractMessage;

@Serializable
public class ClientJoinMessage extends AbstractMessage {
    private final String clientName;

    public ClientJoinMessage() {
        clientName = null;
    }

    public ClientJoinMessage(String clientName_)
    {
        clientName = clientName_;
    }

    public String getClientName() {
        return clientName;
    }
}
