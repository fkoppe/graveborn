package com.grave.Object;

import com.grave.Uuid;
import com.grave.Game.Entities.RigEntity;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;

public class PhysicsColisionListener implements PhysicsCollisionListener {
    private ObjectManager objectmanager;

    protected PhysicsColisionListener(ObjectManager objectmanager_)
    {
        objectmanager = objectmanager_;
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        Uuid a = (Uuid)event.getObjectA().getUserObject();
        Uuid b = (Uuid)event.getObjectB().getUserObject();

        if(!(objectmanager.knownIs(a) && objectmanager.knownIs(b)))
        {
            return;
        }

        RigEntity riggedA = (RigEntity) objectmanager.getEntity(a);
        RigEntity riggedB = (RigEntity) objectmanager.getEntity(b);

        riggedA.onColision(b);
        riggedB.onColision(a);
    }
}
