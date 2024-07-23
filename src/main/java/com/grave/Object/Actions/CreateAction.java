package com.grave.Object.Actions;

import com.grave.Game.Entities.Type;
import com.jme3.network.serializing.Serializable;

@Serializable
public class CreateAction extends Action {
    Enum<Type> type;
    String name;

    // necessary
    public CreateAction() {}

    public CreateAction(Type type_, String name_)
    {
        type = type_;
        name = name_;
    }

    public Type getType() {
        return (Type)type;
    }

    public String getName() {
        return name;
    }
}
