package com.grave.Networking;

import com.jme3.network.MessageListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;

public class GraveServerListener implements MessageListener<HostedConnection> {
    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof GraveMessage) {
            GraveMessage helloMessage = (GraveMessage) message;
            System.out.println("Server received '" + helloMessage.getData() + "' from client #" + source.getId());
        }
    }
}
