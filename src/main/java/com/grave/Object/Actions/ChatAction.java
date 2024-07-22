package com.grave.Object.Actions;

import com.jme3.network.serializing.Serializable;

@Serializable
public class ChatAction extends Action {
    final private String name;
    final private String data;

    // necessaray
    public ChatAction() {
        name = null;
        data = null;
    }

    public ChatAction(String name_, String data_)
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
