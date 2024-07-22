package com.grave.Object.Actions;

import com.grave.Game.Entities.Entity;
import com.jme3.network.serializing.Serializable;

@Serializable
public class CreateAction extends Action {
    Entity entity;

    // necessaray
    public CreateAction() {}

    public CreateAction(Entity entity_)
    {
        entity = entity_;
    }


    public Entity getEntity() {
        return entity;
    }
}
