package com.grave.Networking;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Networking.Message.*;
import com.grave.Object.ObjectManager;
import com.grave.Object.Update;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.system.NanoTimer;

public class NetClient extends Net {
    private static final Logger LOGGER = Logger.getLogger(NetClient.class.getName());

    private static final int NET_FREQUENCY = 120;
    private static final int RETRY_DELAY = 3;

    private String ip;
    private int port;

    private Client instance = null;
    private NanoTimer lastTryTimer = new NanoTimer();
    private NanoTimer netTimer = new NanoTimer();

    Update allLocal;

    String serverName;

    public NetClient(ObjectManager objectManager_, String name_, String ip_, int port_)
    {
        super(objectManager_, name_);

        ip = ip_;
        port = port_;
    }

    public void init()
    {
        allLocal = objectmanager.getAll();
    }

    public void shutdown()
    {
        disconnect();
    }

    public void update(float tpf) {
        if(null == instance) {
            disconnect();
        }

        if (!instance.isConnected() && lastTryTimer.getTimeInSeconds() >= RETRY_DELAY) {
            lastTryTimer.reset();
            LOGGER.log(Level.INFO, "CLIENT: trying to connect to " + ip + ":" + port);

            try {
                instance = Network.connectToServer(ip, port);
            } catch (IOException exception) {
                LOGGER.log(Level.WARNING, "CLIENT: failed to connect to server");
            }

            if (instance.isConnected()) {
                LOGGER.log(Level.FINE, "CLIENT: establishing connection...");

                NetSerializer.serializeAll();

                instance.addMessageListener(new NetClientListener(this), ServerHandshakeMessage.class);
                instance.addMessageListener(new NetClientListener(this), ServerShutdownMessage.class);

                instance.addMessageListener(new NetClientListener(this), UpdateMessage.class);

                instance.addClientStateListener(new NetClientStateListener(this));

                instance.start();
            }
        }

        if (instance.isConnected() && netTimer.getTimeInSeconds() * NET_FREQUENCY >= 1) {
            netTimer.reset();

            UpdateMessage updateMessage = new UpdateMessage(allLocal);

            instance.send(updateMessage);
        }
    }

    void disconnect() {
        if(null != instance) {
            if (instance.isConnected()) {
                LOGGER.log(Level.FINE, "CLIENT: closing connection...");

                instance.close();
            }
        }
    }
}
