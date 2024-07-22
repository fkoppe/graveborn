package com.grave.Object;

import java.util.HashMap;
import java.util.UUID;

import com.grave.Object.Actions.Action;
import com.jme3.network.serializing.Serializable;

@Serializable
public class Update {
    private HashMap<UUID, Action> actions;

    public Update() {
        actions = new HashMap<UUID, Action>();
    }

    public void addAction(UUID uuid, Action action) {
        actions.put(uuid, action);
    }
    
    public void addActions(HashMap<UUID, Action> actions_) {
        actions.putAll(actions_);
    }

    public HashMap<UUID, Action> getActions() {
        return actions;
    }
}
