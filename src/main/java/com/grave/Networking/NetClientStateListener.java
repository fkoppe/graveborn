package com.grave.Networking;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Networking.Message.ClientHandshakeMessage;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;

public class NetClientStateListener implements ClientStateListener {
    private static final Logger LOGGER = Logger.getLogger(NetServerConnectionListener.class.getName());

    NetClient client;

    protected NetClientStateListener(NetClient client_) {
        client = client_;
    }

    public void clientConnected(Client instance) {
        Message message = new ClientHandshakeMessage(client.getName());
        instance.send(message);
    }

    public void clientDisconnected(Client instance, DisconnectInfo info) {
        if(null != info) {
            LOGGER.log(Level.INFO, "CLIENT: disconnected because \"" + info.reason + "\"");
        }
        else {
            LOGGER.log(Level.INFO, "CLIENT: disconnected");
        }

        client.disconnect();
    }
}
