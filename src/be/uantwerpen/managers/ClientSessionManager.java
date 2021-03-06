package be.uantwerpen.managers;

import be.uantwerpen.chat.ChatParticipatorKey;
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
 *
 * This class helps clients to invite other clients to a chat session.
 *
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
    public String getFullName() {
        return chatServer.getClient(clientSession.getUsername()).getFullName();
    }

    /**
     * Tries to send an invite to an existing user. If the user is offline, send an offline message.
     *
     * @param otherUsername the invitee
     * @param ics the chat session
     * @return true if an offlineSession is started
     * @throws RemoteException
     * @throws UnknownClientException
     */
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
            if (serverJoinSession(serverParticipator,offlineSession, true)) chatServer.addOfflineSession(otherUsername,offlineSession);
            return offlineSession;
        }
        if (friend.getActiveSession().invite(ics)) {
            return null;
        } else throw new RemoteException("Something went wrong while inviting the other user");
    }

    /**
     *Actually invites another user and the server to the chat session.
     *
     * @param ics the chat session
     * @return true if the other user is successfully invited
     * @throws RemoteException
     */
    @Override
    public boolean invite(IChatSession ics) throws RemoteException {
        if (clientSession.getClientListener().initialHandshake(ics)) {
            ChatParticipator chatParticipator = getServerParticipator(ics);
            serverJoinSession(chatParticipator, ics, false);
        } else {
            System.out.println("Something with wrong while handshaking my client...");
            return false;
        }
        return true;
    }

    /*
    Bruce Wayne by day,...Batman by night.
     */
    private ChatParticipator getServerParticipator(IChatSession ics) throws RemoteException {
        ChatParticipator chatParticipator;
        if (ics == null) chatParticipator = new ChatParticipator((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 6 || Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 20 ? "BATMAN" : "BRUCE WAYNE"));
        else chatParticipator = new ChatParticipator((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 6 || Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 20 ? "BATMAN" : "BRUCE WAYNE"), ics);
        return chatParticipator;
    }

    /**
     * The server sets the participator as host, and joins the chat session as a participator.
     *
     * @param participator the participator that started the chat session
     * @param ics the chat session
     * @return true if the server has joined the chat as participator
     * @throws RemoteException
     */
    @Override
    public boolean serverJoinSession(ChatParticipator participator, IChatSession ics, boolean offlineSession) throws RemoteException {
        if (ics.joinSession(participator, offlineSession)) {
            participator.setHost(new ChatParticipatorKey(ics.getHost().getUserName(), ics.getHost(), true));
            chatServer.addChatSession(ics, participator);
            ics.chooseChatName();
            return true;
        } else {
            System.out.println("Something went wrong while adding SERVER participator to session...");
            return false;
        }
    }

    /**
     * If the user received messages while he was offline, show them.
     * @return the offline messages
     */
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
}
