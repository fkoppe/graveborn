package com.grave.Networking;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Graveborn;

import com.jme3.network.serializing.Serializer;
import com.jme3.network.Network;
import com.jme3.network.Server;
//import com.jme3.system.NanoTimer;

public class NetServer {
    private static final Logger LOGGER = Logger.getLogger(NetServer.class.getName());

    private Graveborn application;

    private Server myServer = null;
    private String name;
    private String ip;
    private int port;

    boolean initialised = false;
    //private NanoTimer netUpdateTimer;

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

        //netUpdateTimer = new NanoTimer();

        try {
            myServer = Network.createServer(port);
        } catch (IOException exception) {
            throw new RuntimeException("failed to create server");
        }

        Serializer.registerClass(RealtimeServerMessage.class);
        Serializer.registerClass(ClientJoinMessage.class);
        Serializer.registerClass(RealtimeClientMessage.class);

        myServer.addMessageListener(new NetServerListener(this), ClientJoinMessage.class);
        myServer.addMessageListener(new NetServerListener(this), RealtimeClientMessage.class);
    }

    public void update() {
        if (!initialised)
        {
            return;
        }

        //
    }
    
    public void init()
    {
        assert (!initialised);

        myServer.start();

        LOGGER.log(Level.INFO, "server listening on " + ip + ":" + port);

        initialised = true;
    }

    public void shutdown()
    {
        assert (initialised);

        LOGGER.log(Level.INFO, "server stopped");

        initialised = false;
    }

    public String getIp()
    {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
