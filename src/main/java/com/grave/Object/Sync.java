package com.grave.Object;

import java.util.ArrayList;

public class Sync {
    private ArrayList<PlayerTransform> playerTransforms;

    public Sync() {
        playerTransforms = new ArrayList<PlayerTransform>();
    }

    public void addPlayerPosition(PlayerTransform transform) {
        playerTransforms.add(transform);
    }

    public ArrayList<PlayerTransform> getTransforms() {
        return playerTransforms;
    }
}
