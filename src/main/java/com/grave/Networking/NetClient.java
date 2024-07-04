package com.grave.Networking;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Graveborn;

import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
//import com.jme3.system.NanoTimer;
import com.jme3.network.Message;

public class NetClient {
    private static final Logger LOGGER = Logger.getLogger(NetClient.class.getName());
    private static final int RETRY_DELAY = 3;

    private Graveborn application;
    
    private Client myClient = null;
    String clientName;
    private String ip;
    private int port;

    //private NanoTimer netUpdateTimer;

    public NetClient(Graveborn application_, String clientName_, String ip_, int port_)
    {
        application = application_;
        clientName = clientName_;
        ip = ip_;
        port = port_;

        //netUpdateTimer = new NanoTimer();
        Serializer.registerClass(RealtimeServerMessage.class);
        Serializer.registerClass(ClientJoinMessage.class);
        Serializer.registerClass(RealtimeClientMessage.class);
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

        myClient.addMessageListener(new NetClientListener(this), RealtimeServerMessage.class);

        myClient.start();

        Message message = new ClientJoinMessage(clientName);
        myClient.send(message);
        LOGGER.log(Level.INFO, "connected to server '" + "" + "'");
    }

    public void shutdown()
    {

    }

    public void update() {
        
    }
}
