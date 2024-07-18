package com.grave.Object;

public class ChatAction extends Action {
    final private String name;
    final private String data;

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
