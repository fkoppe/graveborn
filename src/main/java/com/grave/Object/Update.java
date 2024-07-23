package com.grave.Object;

import java.util.ArrayList;
import java.util.HashMap;

import com.grave.Uuid;
import com.grave.Object.Actions.Action;
import com.jme3.network.serializing.Serializable;

@Serializable
public class Update {
    private HashMap<Uuid, ArrayList<Action>> actions;

    public Update() {
        actions = new HashMap<Uuid, ArrayList<Action>>();
    }

    public void addAction(Uuid uuid, Action action) {
        if (null == actions.get(uuid)) {
                actions.put(uuid, new ArrayList<Action>());
        }
        actions.get(uuid).add(action);
    }
    
    public void addActions(HashMap<Uuid, ArrayList<Action>> additional_) {
        additional_.forEach((uuid, array) -> {
            if (null == actions.get(uuid)) {
                actions.put(uuid, new ArrayList<Action>());
            }
            actions.get(uuid).addAll(array);
        });
    }

    public HashMap<Uuid, ArrayList<Action>> getActions() {
        return actions;
    }
}
