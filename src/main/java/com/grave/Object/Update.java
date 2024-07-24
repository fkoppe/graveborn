package com.grave.Object;

import java.util.ArrayList;
import java.util.HashMap;

import com.grave.Uuid;
import com.grave.Object.Actions.Action;
import com.grave.Object.Actions.MoveAction;
import com.grave.Object.Actions.VelocityAction;
import com.jme3.network.serializing.Serializable;

@Serializable
public class Update {
    private HashMap<Uuid, ArrayList<Action>> actions;
    private HashMap<Uuid, ArrayList<Action>> transforms;

    public Update() {
        actions = new HashMap<Uuid, ArrayList<Action>>();
        transforms = new HashMap<Uuid, ArrayList<Action>>();
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

            additional_.get(uuid).forEach((action) -> {
                actions.get(uuid).add(action);
            });
        });
    }

    public void addTransform(Uuid uuid, MoveAction move, VelocityAction velocity) {
        if (null == transforms.get(uuid)) {
            transforms.put(uuid, new ArrayList<Action>(2));
        }

        transforms.get(uuid).add(move);
        transforms.get(uuid).add(velocity);
    }

    public void addTransforms(HashMap<Uuid, ArrayList<Action>> additional_) {
        additional_.forEach((uuid, array) -> {
            assert (array.size() == 2);
        });

        transforms.putAll(additional_);
    }

    public HashMap<Uuid, ArrayList<Action>> getActions() {
        return actions;
    }

    public HashMap<Uuid, ArrayList<Action>> getTransforms() {
        return transforms;
    }
}
