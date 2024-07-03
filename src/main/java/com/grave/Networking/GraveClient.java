package com.grave.Networking;

import java.io.IOException;

import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.Message;

public class GraveClient {
    private Client myClient = null;

    public void init()
    {
        try {
            myClient = Network.connectToServer("localhost", 6143);
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Failed to connect to Server");
        }

        myClient.start();
    }

    public void shutdown()
    {

    }

    public void update()
    {
        Message message = new GraveMessage("Hello World!");

        myClient.send(message);
    }
}
