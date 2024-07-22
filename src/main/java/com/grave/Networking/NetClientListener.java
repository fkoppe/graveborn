package com.grave.Networking;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Networking.Message.UpdateMessage;
import com.grave.Networking.Message.ServerHandshakeMessage;
import com.grave.Networking.Message.ServerShutdownMessage;
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

    @Override
    public void messageReceived(Client source, Message message) {
        if (message instanceof ServerHandshakeMessage) {
            ServerHandshakeMessage joinMessage = (ServerHandshakeMessage) message;
            LOGGER.log(Level.INFO, "CLIENT: connected to server '" + joinMessage.getServerName() + "'");

            client.serverName = joinMessage.getServerName();
        }
        else if(message instanceof ServerShutdownMessage)
        {
            client.disconnect();
            
            LOGGER.log(Level.INFO, "CLIENT: disconnected from server");
        }
        else if (message instanceof UpdateMessage) {
            UpdateMessage updateMessage = (UpdateMessage) message;

            client.objectmanager.forceUpdate(updateMessage.getUpdate());
        }
        else {
            LOGGER.log(Level.WARNING, "CLIENT: received unknown message");
        }
    }
}
