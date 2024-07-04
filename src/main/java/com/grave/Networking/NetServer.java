package com.grave.Networking;

import java.io.IOException;
import java.net.InetAddress;

import com.grave.Graveborn;

import com.jme3.network.serializing.Serializer;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.NanoTimer;

public class NetServer {
    static public final int NETUPDATE_FREQUENCY = 60;

    private Graveborn application;

    private Server myServer = null;
    private String name;
    private String ip;
    private int port;

    private NanoTimer netUpdateTimer;

    public NetServer(Graveborn application_, String name_, int port_)
    {
        application = application_;
        name = name_;
        port = port_;
        
        InetAddress localHost;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to get localhost");
        }
        ip = localHost.getHostAddress();

        netUpdateTimer = new NanoTimer();
    }

    public void init() {
        try {
            myServer = Network.createServer(port);
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Failed to create server");
        }

        Serializer.registerClass(ClientJoinMessage.class);
        Serializer.registerClass(RealtimeClientMessage.class);

        myServer.addMessageListener(new NetServerListener(this), ClientJoinMessage.class);
        myServer.addMessageListener(new NetServerListener(this), RealtimeClientMessage.class);

        myServer.start();

        System.out.println("Server listening on " + ip + ":" + port);
    }

    public void shutdown() {

    }

    public void update() {
        if(netUpdateTimer.getTimeInSeconds() * NETUPDATE_FREQUENCY >= 1)
        {
            netUpdate();
        }
    }

    private void netUpdate() {
        //application.getObjectManager()
        //fetch gameworld updates

        RealtimeServerMessage updateMessage = new RealtimeServerMessage();

        myServer.broadcast(updateMessage);
    }

    public String getIp()
    {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
