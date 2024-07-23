package com.grave.Networking.Message;

import com.jme3.network.serializing.Serializable;
import com.grave.Object.Update;
import com.jme3.network.AbstractMessage;

@Serializable
public class ClientHandshakeMessage extends AbstractMessage {
    private final String clientName;
    private final Update all;

    public ClientHandshakeMessage() {
        clientName = null;
        all = null;
    }

    public ClientHandshakeMessage(String clientName_, Update all_)
    {
        clientName = clientName_;
        all = all_;
    }

    public String getClientName() {
        return clientName;
    }

    public Update getAll() {
        return all;
    }
}
