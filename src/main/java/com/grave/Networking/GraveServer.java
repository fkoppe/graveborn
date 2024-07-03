package com.grave.Networking;

import java.io.IOException;

import com.jme3.network.serializing.Serializer;
import com.jme3.network.Network;
import com.jme3.network.Server;

public class GraveServer {
    private Server myServer = null;

    public void init() {
        try {
            myServer = Network.createServer(6143);
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Failed to create Server");
        }

        Serializer.registerClass(GraveMessage.class);

        myServer.addMessageListener(new GraveServerListener(), GraveMessage.class);

        myServer.start();
    }

    public void shutdown() {

    }

    public void update() {

    }
}
