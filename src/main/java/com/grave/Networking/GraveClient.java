package com.grave.Networking;

import java.io.IOException;

import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.Message;

public class GraveClient {
    private Client myClient = null;
    private String ip;
    private int port;

    public GraveClient(String i, int p)
    {
        ip = i;
        port = p;
    }

    public void init()
    {
        boolean connected = false;
        while (!connected) {
            try {
                myClient = Network.connectToServer(ip, port);
                connected = true;
            } catch (IOException exception) {
                System.out.println("Failed to connect to Server (trying again in 5s)");
                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException e) {
                    System.err.println("Thread was interrupted while sleeping");
                    e.printStackTrace();
                }
            }
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
