package com.grave.Object.Actions;

import com.grave.Uuid;
import com.grave.Game.Gun;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

@Serializable
public class FireAction extends Action {
    public Uuid shooterID;
    public Vector3f targetPosition;
    public Gun gun;
    
    public FireAction() {}
    
    public FireAction(Uuid shooterID_, Vector3f targetPosition_, Gun gun_) {
        shooterID = shooterID_;
        targetPosition = targetPosition_;
        gun = gun_;
    }
}
