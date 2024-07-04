package com.grave.Networking;

import com.jme3.network.serializing.Serializable;
import com.jme3.network.AbstractMessage;

@Serializable
public class SyncMessage extends AbstractMessage {
    // timestamp

    public SyncMessage()
    {
        
    }

// public String getTimestamp() {
// return data;
// }

}
