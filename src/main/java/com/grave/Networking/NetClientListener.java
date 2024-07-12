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
        if (message instanceof ServerHandshakeMessage) {
            ServerHandshakeMessage joinMessage = (ServerHandshakeMessage) message;
            LOGGER.log(Level.INFO, "CLIENT: connected to server '" + joinMessage.getServerName() + "'");

            client.serverName = joinMessage.getServerName();
        }
        else if (message instanceof ChatMessage) {
            ChatMessage chatMessage = (ChatMessage) message;

            LOGGER.log(Level.INFO, "CLIENT: " + chatMessage.getName() + ": " + chatMessage.getData());
        }
        else if(message instanceof PlayerPositionMessage)
        {
            PlayerPositionMessage playerPositionMessage = (PlayerPositionMessage) message;

            client.application.getObjectManager().moveClientPlayer(playerPositionMessage.getPlayerPosition());
        }
        else if (message instanceof ClientJoinMessage) {
            ClientJoinMessage clientJoinMessage = (ClientJoinMessage) message;

            client.application.getObjectManager().addClientPlayer();
        }
    }
}
