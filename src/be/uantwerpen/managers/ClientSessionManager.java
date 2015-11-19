package be.uantwerpen.managers;

import be.uantwerpen.chat.ChatParticipator;
import be.uantwerpen.chat.OfflineChat;
import be.uantwerpen.exceptions.ClientNotOnlineException;
import be.uantwerpen.interfaces.IClientSessionManager;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.server.ChatServer;
import be.uantwerpen.server.ClientSession;

import java.rmi.RemoteException;
import java.util.Calendar;

/**
 * Created by Dries on 26/10/2015.
 */
public class ClientSessionManager extends Thread implements IClientSessionManager {
    private ClientSession clientSession;

    protected ClientSessionManager() { }

    protected ClientSessionManager(ClientSession clientSession) {
        this.clientSession = clientSession;
    }

    @Override
    public boolean sendInvite(String otherUsername, IChatSession ics) throws RemoteException, ClientNotOnlineException {
        System.out.println(clientSession.getUsername() + " is inviting " + otherUsername + " voor een leuk gesprek");
        ClientSession otherClientSession = ChatServer.getInstance().getOnlineClients().get(otherUsername);
        if (otherClientSession == null) throw new ClientNotOnlineException("Could not find user " + otherUsername);
        ics.setChatName(clientSession.getFullname());
        return otherClientSession.invite(ics);
    }

    @Override
    public boolean invite(IChatSession ics) throws RemoteException {
        if (clientSession.getClientListener().initialHandshake(ics)) {
            serverJoinSession(ics);
        } else {
            System.out.println("Something with wrong while handshaking my client...");
            return false;
        }
        return true;
    }

    @Override
    public boolean serverJoinSession(IChatSession ics) throws RemoteException {
        ChatParticipator chatParticipator = new ChatParticipator((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 6 || Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 20 ? "BATMAN" : "BRUCE WAYNE"), ics);
        if (ics.joinSession(chatParticipator)) {
            chatParticipator.setHost(ics.getHost());
            ChatServer.getInstance().addChatSession(ics, chatParticipator);
            ics.chooseChatName();
            return true;
        } else {
            System.out.println("Something went wrong while adding SERVER participator to session...");
            return false;
        }
    }

    @Override
    public void joinOfflineSession(String otherUsername, IChatSession ics) throws RemoteException {
        serverJoinSession(ics);
    }
}
