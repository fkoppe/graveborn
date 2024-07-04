package com.grave.Networking;

import com.jme3.network.MessageListener;

import java.util.logging.Logger;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;

public class NetServerListener implements MessageListener<HostedConnection> {
    NetServer server;

    protected NetServerListener(NetServer server_)
    {
        server = server_;
    }

    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof ClientJoinMessage) {
            ClientJoinMessage joinMessage = (ClientJoinMessage) message;
            System.out.println("Client #" + source.getId() + "joined the server '" + joinMessage.getClientName() + "'");
        }
    }
}
