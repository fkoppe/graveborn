package com.grave.Networking;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.grave.Networking.Message.ServerHandshakeMessage;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.Server;

public class NetServerConnectionListener implements ConnectionListener {
    private static final Logger LOGGER = Logger.getLogger(NetServerConnectionListener.class.getName());

    private NetServer server;

    protected NetServerConnectionListener(NetServer server_)
    {
        server = server_;
    }

    @Override
    public void connectionAdded(Server instance, HostedConnection hostConnection) {
        LOGGER.log(Level.INFO, "SERVER: connection #" + hostConnection.getId() + " added");

        Message responce = new ServerHandshakeMessage(server.getName());
        hostConnection.send(responce);
    }

    @Override
    public void connectionRemoved(Server instance, HostedConnection hostConnection) {
        LOGGER.log(Level.INFO, "SERVER: connection #" + hostConnection.getId() + " removed");
    }
}
