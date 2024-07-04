package com.grave.Networking;

import java.io.IOException;

import com.grave.Graveborn;

import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.NanoTimer;
import com.jme3.network.Message;

public class NetClient {
    static public final int NETUPDATE_FREQUENCY = 120;
    static public final int RETRY_DELAY = 3;

    private Graveborn application;
    
    private Client myClient = null;
    private String ip;
    private int port;

    private NanoTimer netUpdateTimer;

    public NetClient(Graveborn application_, String ip_, int port_)
    {
        application = application_;
        ip = ip_;
        port = port_;

        netUpdateTimer = new NanoTimer();
    }

    public void init()
    {
        boolean connected = false;
        while (!connected) {
            try {
                System.out.println("Try to connect to " + ip + ":" + Integer.toString(port));
                myClient = Network.connectToServer(ip, port);
                connected = true;
            } catch (IOException exception) {
                System.out.println("Failed to connect to Server (trying again in " + RETRY_DELAY + "s)");
                try {
                    Thread.sleep(RETRY_DELAY * 1000);
                } catch (InterruptedException e) {
                    System.err.println("Thread was interrupted while sleeping");
                    e.printStackTrace();
                }
            }
        }
        
        Serializer.registerClass(ChatMessage.class);
        Serializer.registerClass(RealtimeServerMessage.class);

        myClient.addMessageListener(new NetClientListener(this), ChatMessage.class);
        myClient.addMessageListener(new NetClientListener(this), RealtimeServerMessage.class);

        myClient.start();
    }

    public void shutdown()
    {

    }

    public void update() {
        if (netUpdateTimer.getTimeInSeconds() * NETUPDATE_FREQUENCY >= 1) {
            netUpdate();
        }
    }

    private void netUpdate() {
        Message message = new ChatMessage("Hello World!");

        myClient.send(message);
    }
}
