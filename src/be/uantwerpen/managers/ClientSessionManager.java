package be.uantwerpen.managers;

import be.uantwerpen.chat.ChatParticipator;
import be.uantwerpen.chat.offline.ChatSession;
import be.uantwerpen.exceptions.UnknownClientException;
import be.uantwerpen.interfaces.IClientSessionManager;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.server.ChatServer;
import be.uantwerpen.server.client.Client;
import be.uantwerpen.server.ClientSession;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Dries on 26/10/2015.
 */
public class ClientSessionManager extends Thread implements IClientSessionManager {
    private ClientSession clientSession;
    private ChatServer chatServer;

    protected ClientSessionManager() { }

    /*protected ClientSessionManager(ClientSession clientSession) {
        this.clientSession = clientSession;
    }*/

    protected ClientSessionManager(ClientSession clientSession, ChatServer chatServer) {
        this.clientSession = clientSession;
        this.chatServer = chatServer;
    }

    @Override
    public IChatSession sendInvite(String otherUsername, IChatSession ics) throws RemoteException, UnknownClientException {
        System.out.println(clientSession.getUsername() + " is inviting " + otherUsername + " voor een leuk gesprek");
        Client friend = chatServer.getClient(otherUsername);
        if (friend == null) throw new UnknownClientException("That user does not exist!");
        //user is offline
        if (friend.getActiveSession() == null) {
            System.out.println("user is not online");
            ChatParticipator serverParticipator = getServerParticipator(null);
            int sessionId = chatServer.getSessionId();
            ChatSession offlineSession = new ChatSession(sessionId, serverParticipator, otherUsername);
            if (serverJoinSession(serverParticipator,offlineSession)) chatServer.addOfflineSession(otherUsername,offlineSession);
            return offlineSession;
        }
        if (friend.getActiveSession().invite(ics)) {
            return null;
        } else throw new RemoteException("Something went wrong while inviting the other user");
    }

    @Override
    public boolean invite(IChatSession ics) throws RemoteException {
        if (clientSession.getClientListener().initialHandshake(ics)) {
            ChatParticipator chatParticipator = getServerParticipator(ics);
            serverJoinSession(chatParticipator, ics);
        } else {
            System.out.println("Something with wrong while handshaking my client...");
            return false;
        }
        return true;
    }

    private ChatParticipator getServerParticipator(IChatSession ics) throws RemoteException {
        ChatParticipator chatParticipator;
        if (ics == null) chatParticipator = new ChatParticipator((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 6 || Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 20 ? "BATMAN" : "BRUCE WAYNE"));
        else chatParticipator = new ChatParticipator((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 6 || Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 20 ? "BATMAN" : "BRUCE WAYNE"), ics);
        return chatParticipator;
    }

    @Override
    public boolean serverJoinSession(ChatParticipator participator, IChatSession ics) throws RemoteException {
        if (ics.joinSession(participator)) {
            participator.setHost(ics.getHost());
            chatServer.addChatSession(ics, participator);
            ics.chooseChatName();
            return true;
        } else {
            System.out.println("Something went wrong while adding SERVER participator to session...");
            return false;
        }
    }

    @Override
    public ArrayList<IChatSession> getOfflineMessages() {
        System.out.println(clientSession.getUsername() + " wants to receive his offline messages");
        ArrayList<ChatSession> offlineMessages = chatServer.getOfflineChatMessages(clientSession.getUsername());
        if (offlineMessages == null) return null;
        System.out.println(clientSession.getUsername() + " has offline messages...");
        return new ArrayList<>(offlineMessages);
    }

    @Override
    public void offlineMessagesRead() {
        chatServer.offlineMessagesRead(clientSession.getUsername());
    }

    @Override
    public void offlineMessagesRead(IChatSession iChatSession) {
        chatServer.offlineMessagesRead(clientSession.getUsername(), iChatSession);
    }
}
