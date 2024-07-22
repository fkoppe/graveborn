package com.grave.Object;

import java.util.HashMap;

import com.grave.uuid;
import com.grave.Object.Actions.Action;
import com.jme3.network.serializing.Serializable;

@Serializable
public class Update {
    private HashMap<uuid, Action> actions;

    public Update() {
        actions = new HashMap<uuid, Action>();
    }

    public void addAction(uuid uuid, Action action) {
        actions.put(uuid, action);
    }
    
    public void addActions(HashMap<uuid, Action> actions_) {
        actions_.putAll(actions);
    }

    public HashMap<uuid, Action> getActions() {
        return actions;
    }
}
