package com.grave.Object.Actions;

import com.grave.uuid;
import com.jme3.network.serializing.Serializable;

@Serializable
public class DeleteAction extends Action {
    uuid id;

    // necessary
    public DeleteAction() {}

    public DeleteAction(uuid id_)
    {
        id = id_;
    }


    public uuid getId() {
        return id;
    }
}
