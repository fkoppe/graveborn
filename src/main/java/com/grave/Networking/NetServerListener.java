package com.grave.Networking;

import com.grave.Networking.Message.ClientHandshakeMessage;
import com.grave.Networking.Message.NoticeMessage;
import com.grave.Networking.Message.ServerHandshakeMessage;
import com.grave.Networking.Message.SyncMessage;
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
            LOGGER.log(Level.INFO, "SERVER: client #" + source.getId() + " established connection as \"" + handshakeMessage.getClientName() + "\"");

            //Message joinMessage = new ChatMessage(handshakeMessage.getClientName(), "joined the game");
            //server.broadcast(joinMessage);
        }
        else if (message instanceof SyncMessage) {
            SyncMessage syncMessage = (SyncMessage) message;

            server.objectmanager.takeSync(syncMessage.getSync());
        }
        else if (message instanceof NoticeMessage) {
            NoticeMessage noticeMessage = (NoticeMessage) message;

            server.objectmanager.takeNotice(noticeMessage.getNotice());
        }
        else {
            LOGGER.log(Level.WARNING, "SERVER: received unknown message from client #" + source.getId());
        }
    }
}
