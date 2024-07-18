package com.grave.Networking.Message;

import com.jme3.network.serializing.Serializable;
import com.jme3.network.AbstractMessage;

@Serializable
public class ServerShutdownMessage extends AbstractMessage {
    public ServerShutdownMessage() {
    }
}