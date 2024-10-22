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

    private HashMap<Uuid, Action> positions;
    private HashMap<Uuid, Action> velocities;

    public Update() {
        actions = new HashMap<Uuid, ArrayList<Action>>();

        positions = new HashMap<Uuid, Action>();
        velocities = new HashMap<Uuid, Action>();
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

    public void addPosition(Uuid uuid, MoveAction move) {
        positions.put(uuid, move);
    }

    public void addPositions(HashMap<Uuid, Action> additional_) {
        positions.putAll(additional_);
    }

    public void addVelocity(Uuid uuid, VelocityAction velocity) {
        velocities.put(uuid, velocity);
    }

    public void addVelocities(HashMap<Uuid, Action> additional_) {
        velocities.putAll(additional_);
    }

    public HashMap<Uuid, ArrayList<Action>> getActions() {
        return actions;
    }

    public HashMap<Uuid, Action> getPositions() {
        return positions;
    }

    public HashMap<Uuid, Action> getVelocities() {
        return velocities;
    }
}
