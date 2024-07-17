package com.grave.Networking;

import com.grave.Networking.Message.ClientHandshakeMessage;
import com.grave.Networking.Message.ClientJoinMessage;
import com.grave.Networking.Message.ServerHandshakeMessage;
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

    @Override
    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof ClientHandshakeMessage) {
            ClientHandshakeMessage handshakeMessage = (ClientHandshakeMessage) message;
            LOGGER.log(Level.INFO, "client #" + source.getId() + " established connection as \"" + handshakeMessage.getClientName() + "\"");

            Message responce = new ServerHandshakeMessage(server.name);
            //server.instance.getConnection(source.getId()).send(responce);

            //server.clientList.forEach((name, cid) -> {
            //    Message standup = new ClientJoinMessage(name);
            //    server.instance.getConnection(source.getId()).send(standup);
            //});

            //Message joinMessage = new ClientJoinMessage(handshakeMessage.getClientName());
            //server.relay(source, joinMessage);

            //server.clientList.put(handshakeMessage.getClientName(), source.getId());
        }
        else {
            server.relay(source, message);
        }
    }
}
