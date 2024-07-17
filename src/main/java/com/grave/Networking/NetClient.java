package com.grave.Networking;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Game.ObjectManager;
import com.grave.Networking.Message.*;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.system.NanoTimer;
import com.jme3.network.Message;

public class NetClient extends Net {
    private static final Logger LOGGER = Logger.getLogger(NetClient.class.getName());

    private static final int RETRY_DELAY = 3;

    private String ip;
    private int port;

    private Client instance = null;
    private boolean connected = false;
    private NanoTimer lastTry = new NanoTimer();

    String serverName;

    public NetClient(ObjectManager objectManager_, String name, String ip_, int port_)
    {
        super(objectManager_, name);

        ip = ip_;
        port = port_;
    }

    public void init()
    {
        
    }

    public void shutdown()
    {
        if(null != instance)
        {
            LOGGER.log(Level.FINE, "CLIENT: closing connection...");
            instance.close();
        }
    }

    public void update(float tpf) {
        if (!connected && lastTry.getTimeInSeconds() >= RETRY_DELAY) {
            lastTry.reset();
            LOGGER.log(Level.INFO, "CLIENT: trying to connect to " + ip + ":" + port);

            try {
                instance = Network.connectToServer(ip, port);
                connected = true;
            } catch (IOException exception) {
                LOGGER.log(Level.WARNING, "CLIENT: failed to connect to server");
            }

            if (connected) {
                LOGGER.log(Level.FINE, "CLIENT: establishing connection...");

                NetSerializer.serializeAll();

                //from server
                instance.addMessageListener(new NetClientListener(this), ServerHandshakeMessage.class);

                //from clients
                instance.addMessageListener(new NetClientListener(this), ChatMessage.class);
                instance.addMessageListener(new NetClientListener(this), PlayerPositionMessage.class);

                instance.addClientStateListener(new NetClientStateListener(this));

                instance.start();
            }
        }
    }

    //TODO revisit: shouldnt chat be objectmanager handeled?
    public void sendChat(String data) {
        Message message = new ChatMessage(name, data);

        if(connected)
        {
            instance.send(message);
        }
    }

    //TODO replace by general sendUpdate
    public void setPlayerPosition(Vector3f position) {
        Message message = new PlayerPositionMessage(name, position);

        if (connected) {
            instance.send(message);
        }
    }
}
