package com.grave.Networking.Message;

import com.jme3.network.serializing.Serializable;
import com.grave.Object.Update;
import com.jme3.network.AbstractMessage;

@Serializable
public class UpdateMessage extends AbstractMessage {
    Update update;

    public UpdateMessage() {

    }
    
    public UpdateMessage(Update update_) {
        update = update_;
    }

    public Update getUpdate() {
        return update;
    }
}
