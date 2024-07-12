package com.grave.Networking;

import com.jme3.network.serializing.Serializable;
import com.jme3.network.AbstractMessage;

@Serializable
public class ServerHandshakeMessage extends AbstractMessage {
    private final String serverName;

    public ServerHandshakeMessage() {
        serverName = null;
    }

    public ServerHandshakeMessage(String serverName_)
    {
        serverName = serverName_;
    }

    public String getServerName() {
        return serverName;
    }
}
