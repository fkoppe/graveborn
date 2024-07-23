package com.grave.Object;

import java.util.HashMap;

import com.grave.Uuid;
import com.grave.Object.Actions.Action;
import com.jme3.network.serializing.Serializable;

@Serializable
public class Update {
    private HashMap<Uuid, Action> actions;

    public Update() {
        actions = new HashMap<Uuid, Action>();
    }

    public void addAction(Uuid uuid, Action action) {
        actions.put(uuid, action);
    }
    
    public void addActions(HashMap<Uuid, Action> actions_) {
        actions_.putAll(actions);
    }

    public HashMap<Uuid, Action> getActions() {
        return actions;
    }
}
