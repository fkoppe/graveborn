package com.grave.Networking.Message;

import com.jme3.network.serializing.Serializable;
import com.grave.Object.Sync;
import com.jme3.network.AbstractMessage;

@Serializable
public class SyncMessage extends AbstractMessage {
    Sync sync;

    public SyncMessage() {

    }

    public SyncMessage(Sync sync_) {
        sync = sync_;
    }

    public Sync getSync() {
        return sync;
    }
}
