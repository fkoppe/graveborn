package com.grave.Networking;

import com.grave.Networking.Message.*;
import com.jme3.network.serializing.Serializer;

public class NetSerializer {
    public static void serializeAll()
    {
        Serializer.setReadOnly(false);

        Serializer.registerClass(ServerHandshakeMessage.class);
        Serializer.registerClass(ServerShutdownMessage.class);

        Serializer.registerClass(ClientHandshakeMessage.class);

        Serializer.registerClass(SyncMessage.class);
        Serializer.registerClass(NoticeMessage.class);
    }
}
