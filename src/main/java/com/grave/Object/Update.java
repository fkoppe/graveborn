package com.grave.Object;

import com.google.common.collect.ArrayListMultimap;
import com.grave.Uuid;
import com.grave.Object.Actions.Action;
import com.jme3.network.serializing.Serializable;

@Serializable
public class Update {
    private ArrayListMultimap<Uuid, Action> actions;

    public Update() {
        actions = ArrayListMultimap.create();
    }

    public void addAction(Uuid uuid, Action action) {
        actions.put(uuid, action);
    }
    
    public void addActions(ArrayListMultimap<Uuid, Action> actions_) {
        actions_.putAll(actions);
    }

    public ArrayListMultimap<Uuid, Action> getActions() {
        return actions;
    }
}
