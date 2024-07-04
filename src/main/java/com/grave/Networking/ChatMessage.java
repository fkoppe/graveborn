package com.grave.Networking;

import com.jme3.network.serializing.Serializable;
import com.jme3.network.AbstractMessage;

@Serializable
public class ChatMessage extends AbstractMessage {
    private final String chatMessage;

    public ChatMessage(String chatMessage_)
    {
        chatMessage = chatMessage_;
    }

    public String getChatMessage() {
        return chatMessage;
    }
}
