package com.grave.Networking;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Graveborn;

import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.system.NanoTimer;
import com.jme3.network.Message;

public class NetClient {
    private static final Logger LOGGER = Logger.getLogger(NetClient.class.getName());

    private static final int RETRY_DELAY = 3;

    Graveborn application;
    
    private Client instance = null;
    private boolean connected = false;
    private NanoTimer lastTry = new NanoTimer();

    private String clientName;
    private String ip;
    private int port;

    private boolean initialised = false;

    public NetClient(Graveborn application_, String clientName_, String ip_, int port_)
    {
        application = application_;
        clientName = clientName_;
        ip = ip_;
        port = port_;
    }

    public void init()
    {
        assert (!initialised);

        initialised = true;
    }

    public void shutdown()
    {
        assert (initialised);

        initialised = false;
    }

    public void update() {
        if (!initialised) {
            return;
        }

        if(!connected && lastTry.getTimeInSeconds() >= RETRY_DELAY)
        {
            lastTry.reset();
            LOGGER.log(Level.INFO, "trying to connect to " + ip + ":" + port);

            try {
                instance = Network.connectToServer(ip, port);
                connected = true;
            } catch (IOException exception) {
                LOGGER.log(Level.WARNING, "failed to connect to server");
            }

            if(connected)
            {
                LOGGER.log(Level.FINE, "establishing connection...");

                NetSerializer.serializeAll();
                
                instance.addMessageListener(new NetClientListener(this), ServerJoinMessage.class);
                instance.addMessageListener(new NetClientListener(this), ServerSyncMessage.class);

                instance.start();

                Message message = new ClientJoinMessage(clientName);
                instance.send(message);
            }
        }
    }
}
