package com.grave;

import java.util.UUID;

import com.jme3.network.serializing.Serializable;

@Serializable
public final class Uuid {
    String idString;

    public Uuid() {
        idString = UUID.randomUUID().toString();
    }
   
    @Override
    public boolean equals(Object id_) {
        Uuid id = (Uuid) id_;

        return idString.equals(id.idString);
    }

    @Override
    public int hashCode() {
        return UUID.fromString(idString).hashCode();
    }

   public String toString() {
       return idString;
   }
}
