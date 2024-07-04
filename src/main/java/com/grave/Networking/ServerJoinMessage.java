package com.grave.Networking;

import com.jme3.network.serializing.Serializable;
import com.jme3.network.AbstractMessage;

@Serializable
public class ServerJoinMessage extends AbstractMessage {
    private final String serverName;

    public ServerJoinMessage() {
        serverName = null;
    }

    public ServerJoinMessage(String serverName_)
    {
        serverName = serverName_;
    }

    public String getServerName() {
        return serverName;
    }
}
