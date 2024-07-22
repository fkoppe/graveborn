package com.grave.Networking;

import java.util.UUID;

import com.grave.Networking.Message.*;
import com.grave.Object.Update;
import com.jme3.network.serializing.Serializer;

public class NetSerializer {
    public static void serializeAll()
    {
        Serializer.setReadOnly(false);

        Serializer.registerClass(ServerHandshakeMessage.class);
        Serializer.registerClass(ServerShutdownMessage.class);

        Serializer.registerClass(ClientHandshakeMessage.class);

        Serializer.registerClass(UpdateMessage.class);
        Serializer.registerClass(Update.class);
        Serializer.registerClass(UUID.class);
    }
}
