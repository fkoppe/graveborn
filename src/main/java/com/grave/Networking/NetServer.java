package com.grave.Networking;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Game.ObjectManager;
import com.grave.Networking.Message.ChatMessage;
import com.grave.Networking.Message.ClientHandshakeMessage;
import com.grave.Networking.Message.ClientJoinMessage;
import com.grave.Networking.Message.PlayerPositionMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.Server;

public class NetServer extends Net {
    private static final Logger LOGGER = Logger.getLogger(NetServer.class.getName());

    private int port;
    private String ip;

    private Server instance = null;
    private HashMap<String, Integer> clientList = null;

    public NetServer(ObjectManager objectmanager_, String name_, int port_)
    {
        super(objectmanager_, name_);
        port = port_;

        clientList = new HashMap<String, Integer>();

        InetAddress localHost;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (IOException exception) {
            throw new RuntimeException("SERVER: failed to get localhost ip adress");
        }
        ip = localHost.getHostAddress();

        try {
            instance = Network.createServer(port);
        } catch (IOException exception) {
            throw new RuntimeException("SERVER: failed to create server");
        }

        NetSerializer.serializeAll();

        instance.addMessageListener(new NetServerListener(this), ClientHandshakeMessage.class);
        //instance.addMessageListener(new NetServerListener(this), ClientSyncMessage.class);

        instance.addMessageListener(new NetServerListener(this), ChatMessage.class);
        instance.addMessageListener(new NetServerListener(this), PlayerPositionMessage.class);
        instance.addMessageListener(new NetServerListener(this), ClientJoinMessage.class);

        instance.addConnectionListener(new NetServerConnectionListener(this));
    }


    public void init()
    {
        instance.start();

        LOGGER.log(Level.INFO, "SERVER: server listening on " + ip + ":" + port);
    }

    public void update(float tpf) {
        // ...
    }

    public void shutdown()
    {
        LOGGER.log(Level.INFO, "SERVER: server stopped");
    }

    void relay(HostedConnection source, Message message) {
        clientList.forEach((name, cid) -> {
            if (cid != source.getId()) instance.getConnection(cid).send(message);
        });
    }

    void relayTo(HostedConnection source, String name, Message message) {
        instance.getConnection(clientList.get(name)).send(message);
    }

    public String getIp()
    {
        return ip;
    }

    public int getPort() {
        return port;
    }

    //TODO: on remove client

}
