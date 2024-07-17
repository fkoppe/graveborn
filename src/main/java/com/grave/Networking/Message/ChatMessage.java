package com.grave.Networking.Message;

import com.jme3.network.serializing.Serializable;
import com.jme3.network.AbstractMessage;

@Serializable
public class ChatMessage extends AbstractMessage {
    final int targetId;
    final private String name;
    final private String data;

    public ChatMessage() {
        targetId = -1;
        name = null;
        data = null;
    }

    public ChatMessage(String name_, String data_)
    {
        targetId = -1;
        name = name_;
        data = data_;
    }

    public ChatMessage(String name_, String data_, int targetId_) {
        targetId = targetId_;
        name = name_;
        data = data_;
    }

    public boolean isPrivate() {
        return targetId != -1;
    }

    public int getTargetId() {
        return targetId;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }
}
