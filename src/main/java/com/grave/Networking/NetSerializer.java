package com.grave.Networking;

import com.jme3.network.serializing.Serializer;

public class NetSerializer {
    public static void serializeAll()
    {
        Serializer.setReadOnly(false);
        Serializer.registerClass(ServerJoinMessage.class);
        Serializer.registerClass(ServerSyncMessage.class);
        Serializer.registerClass(ClientJoinMessage.class);
        Serializer.registerClass(ClientSyncMessage.class);
        Serializer.setReadOnly(true);
    }
}
