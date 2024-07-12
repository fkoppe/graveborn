package com.grave.Networking;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.Position;

import com.grave.Graveborn;
import com.grave.Game.GameClient;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.system.NanoTimer;
import com.jme3.network.Message;

public class NetClient {
    private static final Logger LOGGER = Logger.getLogger(NetClient.class.getName());

    private static final int RETRY_DELAY = 3;

    Graveborn application;
    
    private GameClient gameClient = null;
    
    private Client instance = null;
    private boolean connected = false;
    private NanoTimer lastTry = new NanoTimer();

    String clientName;
    private String ip;
    private int port;
    String serverName;

    private boolean initialised = false;

    public NetClient(Graveborn application_, String clientName_, String ip_, int port_)
    {
        application = application_;
        clientName = clientName_;
        ip = ip_;
        port = port_;

        gameClient = new GameClient(application_);
    }

    public void init()
    {
        assert (!initialised);

        initialised = true;

        gameClient.init();
    }

    public void shutdown()
    {
        assert (initialised);

        initialised = false;

        gameClient.shutdown();

        if(null != instance)
        {
            LOGGER.log(Level.FINE, "CLIENT: closing connection...");
            instance.close();
        }
    }

    public void update(float tpf) {
        if (!initialised) {
            return;
        }

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

                instance.addMessageListener(new NetClientListener(this), ServerHandshakeMessage.class);

                instance.addMessageListener(new NetClientListener(this), ChatMessage.class);
                instance.addMessageListener(new NetClientListener(this), PlayerPositionMessage.class);
                instance.addMessageListener(new NetClientListener(this), ClientJoinMessage.class);

                instance.start();

                Message message = new ClientHandshakeMessage(clientName);
                instance.send(message);
            }
        }

        gameClient.update(tpf);
    }
    
    public void chat(String data) {
        Message message = new ChatMessage(clientName, data);

        if(connected)
        {
            instance.send(message);
        }
    }

    public void setPlayerPosition(Vector3f position) {
        Message message = new PlayerPositionMessage(position);
        
        if (connected) {
            instance.send(message);
        }
    }
}
