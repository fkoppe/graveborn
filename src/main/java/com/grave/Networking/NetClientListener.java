package com.grave.Networking;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.network.Client;
import com.jme3.network.MessageListener;
import com.jme3.network.Message;

public class NetClientListener implements MessageListener<Client> {
    private static final Logger LOGGER = Logger.getLogger(NetClientListener.class.getName());
    private NetClient client;

    protected NetClientListener(NetClient client_)
    {
        client = client_;
    }

    public void messageReceived(Client source, Message message) {
        if (message instanceof ServerJoinMessage) {
            ServerJoinMessage joinMessage = (ServerJoinMessage) message;
            LOGGER.log(Level.INFO, "connected to server '" + joinMessage.getServerName() + "'");
        }
    }
}
