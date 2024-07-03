package com.grave.Networking;

import com.jme3.network.serializing.Serializable;
import com.jme3.network.AbstractMessage;

@Serializable
public class GraveMessage extends AbstractMessage {
    private final String data;

    public GraveMessage()
    {
        data = "";
    }

    public GraveMessage(String string)
    {
        data = string;
    }

    public String getData() {
        return data;
    }
}
