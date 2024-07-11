package com.grave.Networking;

import com.jme3.network.serializing.Serializable;
import com.jme3.network.AbstractMessage;

@Serializable
public class ChatMessage extends AbstractMessage {
    private String name;
    private String data;

    public ChatMessage() {
        name = null;
        data = null;
    }

    public ChatMessage(String name_, String data_)
    {
        name = name_;
        data = data_;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }
}
