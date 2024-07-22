package com.grave.Networking.Message;

import com.jme3.network.serializing.Serializable;
import com.grave.Object.Update;
import com.jme3.network.AbstractMessage;

@Serializable
public class ServerHandshakeMessage extends AbstractMessage {
    private final String serverName;
    private final Update all;

    public ServerHandshakeMessage() {
        serverName = null;
        all = null;
    }

    public ServerHandshakeMessage(String serverName_, Update all_)
    {
        serverName = serverName_;
        all = all_;
    }

    public String getServerName() {
        return serverName;
    }

    public Update getAll() {
        return all;
    }
}
