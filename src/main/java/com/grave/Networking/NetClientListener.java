package com.grave.Networking;

import com.jme3.network.Client;
import com.jme3.network.MessageListener;
import com.jme3.network.Message;

public class NetClientListener implements MessageListener<Client> {
    NetClient client;

    protected NetClientListener(NetClient client_)
    {
        client = client_;
    }

    public void messageReceived(Client source, Message message) {
        if (message instanceof ClientJoinMessage) {
            //ClientJoinMessage helloMessage = (ClientJoinMessage) message;
            //System.out.println("client #" + source.getId() + " established connection as '" + helloMessage.getClientName() + "'");
        }
    }
}
