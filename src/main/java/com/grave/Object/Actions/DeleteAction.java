package com.grave.Object.Actions;

import java.util.UUID;

import com.grave.Game.Entities.Entity;
import com.jme3.network.serializing.Serializable;

@Serializable
public class DeleteAction extends Action {
    String idString;

    //necessaray
    public DeleteAction() {}

    public DeleteAction(UUID id_)
    {
        idString = id_.toString();
    }


    public UUID getId() {
        return UUID.fromString(idString);
    }
}
