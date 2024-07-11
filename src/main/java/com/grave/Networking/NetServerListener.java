package com.grave.Networking;

import com.jme3.network.MessageListener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;

public class NetServerListener implements MessageListener<HostedConnection> {
    private static final Logger LOGGER = Logger.getLogger(NetServerListener.class.getName());
    private NetServer server;

    protected NetServerListener(NetServer server_)
    {
        server = server_;
    }

    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof ClientHandshakeMessage) {
            ClientHandshakeMessage handshakeMessage = (ClientHandshakeMessage) message;
            LOGGER.log(Level.INFO, "SERVER: client #" + source.getId() + " established connection as '" + handshakeMessage.getClientName() + "'");

            server.clientList.put(handshakeMessage.getClientName(), source.getId());

            Message responce = new ServerHandshakeMessage(server.name);
            server.instance.getConnection(source.getId()).send(responce);

            Message joinMessage = new ClientJoinMessage(handshakeMessage.getClientName());
            server.relay(source, joinMessage);
        }
        else {
            server.relay(source, message);
        }
    }
}
