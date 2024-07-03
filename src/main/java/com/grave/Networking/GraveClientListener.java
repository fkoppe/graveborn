package com.grave.Networking;

import com.jme3.network.Client;
import com.jme3.network.MessageListener;
import com.jme3.network.Message;

public class GraveClientListener implements MessageListener<Client> {
    public void messageReceived(Client source, Message message) {
        if (message instanceof GraveMessage) {
            GraveMessage helloMessage = (GraveMessage) message;
            System.out.println("Client #" + source.getId() + " received: '" + helloMessage.getData() + "'");
        }
    }
}
