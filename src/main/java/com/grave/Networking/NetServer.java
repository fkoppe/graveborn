package com.grave.Networking;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Graveborn;

import com.jme3.network.Network;
import com.jme3.network.Server;

public class NetServer {
    private static final Logger LOGGER = Logger.getLogger(NetServer.class.getName());

    Graveborn application;

    Server instance = null;

    String name;
    String ip;
    int port;

    private boolean initialised = false;

    public NetServer(Graveborn application_, String name_, int port_)
    {
        application = application_;
        name = name_;
        port = port_;
        
        InetAddress localHost;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (IOException exception) {
            throw new RuntimeException("failed to get localhost ip adress");
        }
        ip = localHost.getHostAddress();

        try {
            instance = Network.createServer(port);
        } catch (IOException exception) {
            throw new RuntimeException("failed to create server");
        }

        NetSerializer.serializeAll();

        instance.addMessageListener(new NetServerListener(this), ClientJoinMessage.class);
        instance.addMessageListener(new NetServerListener(this), ClientSyncMessage.class);
    }

    
    public void init()
    {
        assert (!initialised);

        instance.start();

        LOGGER.log(Level.INFO, "server listening on " + ip + ":" + port);

        initialised = true;
    }

    public void shutdown()
    {
        assert (initialised);

        LOGGER.log(Level.INFO, "server stopped");

        initialised = false;
    }

    public void update() {
        if (!initialised) {
            return;
        }

        //...
    }

    public String getIp()
    {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
