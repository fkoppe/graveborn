package com.grave;

import java.util.UUID;

import com.jme3.network.serializing.Serializable;

@Serializable
public final class uuid {
    String idString;

    public uuid() {
        idString = UUID.randomUUID().toString();
    }
   
    @Override
    public boolean equals(Object id_) {
        uuid id = (uuid) id_;

        return idString.equals(id.idString);
    }

   public String toString() {
       return idString;
   }
}
