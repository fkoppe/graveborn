package com.grave.Object;

import java.util.HashMap;
import java.util.UUID;

import com.grave.Object.Actions.Action;
import com.jme3.network.serializing.Serializable;

@Serializable
public class Update {
    private HashMap<String, Action> actions;

    public Update() {
        actions = new HashMap<String, Action>();
    }

    public void addAction(UUID uuid, Action action) {
        actions.put(uuid.toString(), action);
    }
    
    public void addActions(HashMap<UUID, Action> actions_) {
        actions_.forEach((uuid, action) -> {
            actions.put(uuid.toString(), action);
        });
    }

    public HashMap<UUID, Action> getActions() {
        HashMap<UUID, Action> map = new HashMap<UUID, Action>();

        actions.forEach((string, action) -> {
            map.put(UUID.fromString(string), action);
        });

        return map;
    }
}
