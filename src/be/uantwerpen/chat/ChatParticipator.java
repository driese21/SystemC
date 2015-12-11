package be.uantwerpen.chat;

import be.uantwerpen.enums.ChatNotificationType;
import be.uantwerpen.managers.CommandManager;
import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.rmiInterfaces.IMessage;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

/**
 * Created by Dries on 23/10/2015.
 *
 * A chat participator is a client who has joined a chat session
 */
public class ChatParticipator extends UnicastRemoteObject implements IChatParticipator {
    private String username;
    private IChatSession chatSession;
    private ChatParticipatorKey host;
    private boolean changingHost=false;

    public ChatParticipator() throws RemoteException { }

    public ChatParticipator(String username) throws RemoteException {
        this.username = username;
    }

    public ChatParticipator(String username, IChatSession chatSession) throws RemoteException {
        this.username = username;
        this.chatSession = chatSession;
    }

    public ChatParticipatorKey getHost() {
        return host;
    }

    public void setHost(ChatParticipatorKey host) {
        this.host = host;
    }

    @Override
    public void addChatSession(IChatSession chatSession) throws RemoteException {
        this.chatSession = chatSession;
    }

    /**
     * If a message starts with "/", the server will read it as a command
     * @param cnt the notification type
     * @param msg the message
     * @throws RemoteException
     */
    @Override
    public void notifyListener(ChatNotificationType cnt, IMessage msg) throws RemoteException {
        if (msg.getMessage().startsWith("/") && !msg.getUsername().equalsIgnoreCase(username)) {
            String reply = CommandManager.parse(msg.getMessage());
            System.out.println(reply);
            chatSession.newMessage(reply, username);
        }
    }

    @Override
    public void notifyListener(ChatNotificationType cnt, ChatParticipatorKey cpk) throws RemoteException { }

    @Override
    public String getUserName() throws RemoteException {
        return username;
    }

    @Override
    public int getId() throws RemoteException {
        return 0;
    }

    @Override
    public String getChatName() throws RemoteException {
        return chatSession.getChatName();
    }

    @Override
    public IChatSession getChatSession() throws RemoteException {
        return chatSession;
    }

    /**
     * Server should not clone the ChatSession, this is done by the participators
     * @param cnt
     * @throws RemoteException
     */
    @Override
    public void cloneSession(ChatNotificationType cnt) throws RemoteException {
        return;
    }

    /**
     * Mark this participator as the server
     * @return will always return true
     * @throws RemoteException
     */
    @Override
    public boolean isServer() throws RemoteException {
        return true;
    }

    public void setChangingHost(boolean changingHost) {
        this.changingHost = changingHost;
    }

    /**
     * Notification to server that newHost is hosting the ChatSession
     * @param newHost reference to the new host
     * @return true if the actual host is no longer alive, false if host is still reachable
     * @throws RemoteException
     */
    @Override
    public synchronized boolean hostChat(IChatParticipator newHost) throws RemoteException {
        System.out.println("ChatParticipator notifying that host has left");
        try {
            if (host.getParticipator().alive()) return false;
        } catch (RemoteException re) {
            if (changingHost) throw new RemoteException("Someone is already taking over...");
            this.host = new ChatParticipatorKey(newHost.getUserName(), newHost, true);
            setChangingHost(true);
            return true;
        }
        throw new RemoteException("Not planned...");
    }

    /**
     * Will throw exception when participator is no longer alive
     * @return will return true when participator is alive, otherwise throw error
     * @throws RemoteException
     */
    @Override
    public boolean alive() throws RemoteException {
        return true;
    }

    /**
     * When the host dies, a new host takes over and starts a 'new' chat session.
     * This is a cloned version of the original chat session.
     *
     * @param newHost   the new host
     * @param newSession    the new session
     * @throws RemoteException
     */
    @Override
    public void hostChanged(IChatParticipator newHost, IChatSession newSession) throws RemoteException {
        this.host = new ChatParticipatorKey(newHost.getUserName(), newHost, true);
        this.chatSession = newSession;
        setChangingHost(false);
        this.chatSession.joinSession(this, false);
    }
}
