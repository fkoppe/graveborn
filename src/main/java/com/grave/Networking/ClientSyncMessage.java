package com.grave.Networking;

import com.jme3.network.serializing.Serializable;

@Serializable
public class ClientSyncMessage extends SyncMessage {
    //timestamp
    //ArrayList<Object updates that occured>

    public ClientSyncMessage()
    {
        
    }

    //public String getTimestamp() {
    //    return data;
    //}
}