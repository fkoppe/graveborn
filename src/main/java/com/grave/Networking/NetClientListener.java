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
        if (message instanceof ChatMessage) {
            ChatMessage helloMessage = (ChatMessage) message;
            System.out.println("Client #" + source.getId() + " received: '" + helloMessage.getChatMessage() + "'");
        }
    }
}
