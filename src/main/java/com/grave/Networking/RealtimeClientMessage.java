package com.grave.Networking;

import com.jme3.network.serializing.Serializable;

@Serializable
public class RealtimeClientMessage extends RealtimeMessage {
    //timestamp
    //ArrayList<Object updates that occured>

    public RealtimeClientMessage()
    {
        
    }

    //public String getTimestamp() {
    //    return data;
    //}
}