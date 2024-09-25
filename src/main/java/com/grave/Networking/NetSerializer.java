package com.grave.Networking;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.grave.Uuid;
import com.grave.Game.Entities.Type;
import com.grave.Networking.Message.*;
import com.grave.Object.Update;
import com.grave.Object.Actions.Action;
import com.grave.Object.Actions.ChatAction;
import com.grave.Object.Actions.CreateAction;
import com.grave.Object.Actions.DeleteAction;
import com.grave.Object.Actions.FireAction;
import com.grave.Object.Actions.MoveAction;
import com.grave.Object.Actions.VelocityAction;
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

        Serializer.registerClass(Action.class);
        Serializer.registerClass(ChatAction.class);
        Serializer.registerClass(CreateAction.class);
        Serializer.registerClass(DeleteAction.class);
        Serializer.registerClass(MoveAction.class);
        Serializer.registerClass(VelocityAction.class);
        Serializer.registerClass(FireAction.class);

        Serializer.registerClass(Uuid.class);
    }
}
