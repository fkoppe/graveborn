package com.grave.Object;

import java.util.ArrayList;

import com.grave.Object.Actions.Action;

public class Notice {
    private ArrayList<Action> actions;

    public Notice() {
        actions = new ArrayList<Action>();
    }

    public void addAction(Action action) {   
        actions.add(action);
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
}
