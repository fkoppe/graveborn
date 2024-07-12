package com.grave.Networking.Message;

import com.jme3.network.serializing.Serializable;
import com.jme3.network.AbstractMessage;

@Serializable
public class ClientHandshakeMessage extends AbstractMessage {
    private final String clientName;

    public ClientHandshakeMessage() {
        clientName = null;
    }

    public ClientHandshakeMessage(String clientName_)
    {
        clientName = clientName_;
    }

    public String getClientName() {
        return clientName;
    }
}
