package com.grave.Networking;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;

public class NetServerConnectionListener implements ConnectionListener {
    private static final Logger LOGGER = Logger.getLogger(NetServerListener.class.getName());

    private NetServer server;

    protected NetServerConnectionListener(NetServer server_)
    {
        server = server_;
    }

    @Override
    public void connectionAdded(Server s, HostedConnection c) {
        LOGGER.log(Level.INFO, "SERVER: added connection to client #" + c.getId());
    }

    @Override
    public void connectionRemoved(Server s, HostedConnection c) {
        LOGGER.log(Level.INFO, "SERVER: lost connection to client #" + c.getId());
    }
}
