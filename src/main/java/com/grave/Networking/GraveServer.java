package com.grave.Networking;

import java.io.IOException;
import java.net.InetAddress;

import com.jme3.network.serializing.Serializer;
import com.jme3.network.Network;
import com.jme3.network.Server;

public class GraveServer {
    private Server myServer = null;
    private String ip;
    private int port;

    public GraveServer(int p)
    {
        InetAddress localHost;

        try {
            localHost = InetAddress.getLocalHost();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to get localhost");
        }

        ip = localHost.getHostAddress();
        port = p;
    }

    public void init() {
        try {
            myServer = Network.createServer(port);
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Failed to create server");
        }

        Serializer.registerClass(GraveMessage.class);

        myServer.addMessageListener(new GraveServerListener(), GraveMessage.class);

        myServer.start();
    }

    public void shutdown() {

    }

    public void update() {

    }

    public String getIp()
    {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
