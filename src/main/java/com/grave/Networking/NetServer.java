package com.grave.Networking;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Game.ObjectManager;
import com.grave.Networking.Message.ChatMessage;
import com.grave.Networking.Message.ClientHandshakeMessage;
import com.grave.Networking.Message.PlayerPositionMessage;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.Server;

public class NetServer extends Net {
    private static final Logger LOGGER = Logger.getLogger(NetServer.class.getName());

    private int port;
    private String ip;

    private Server instance;

    public NetServer(ObjectManager objectmanager_, String name_, int port_)
    {
        super(objectmanager_, name_);
        port = port_;

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

        instance.addMessageListener(new NetServerListener(this), ChatMessage.class);
        instance.addMessageListener(new NetServerListener(this), PlayerPositionMessage.class);

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
        LOGGER.log(Level.INFO, "server stopped");
    }

    void broadcast(Message message)
    {
        instance.broadcast(message);
    }

    void relay(int senderId, Message message) {
        instance.getConnections().forEach((hostedConnection) -> {
            if (hostedConnection.getId() != senderId) hostedConnection.send(message);
        });
    }

    void relayTo(int targetId, Message message) {
        instance.getConnection(targetId).send(message);
    }

    void kick(int targetId, String reason)
    {
        instance.getConnection(targetId).close(reason);
    }

    public String getIp()
    {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
