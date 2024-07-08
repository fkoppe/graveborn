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
        if (message instanceof ClientJoinMessage) {
            ClientJoinMessage joinMessage = (ClientJoinMessage) message;
            LOGGER.log(Level.INFO, "client #" + source.getId() + " established connection as '" + joinMessage.getClientName() + "'");

            Message responce = new ServerJoinMessage(server.name);
            server.instance.getConnection(source.getId()).send(responce);
        }
    }
}
