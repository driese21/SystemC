package be.uantwerpen.chat;

import be.uantwerpen.enums.ChatNotificationType;
import be.uantwerpen.interfaces.IChatManager;
import be.uantwerpen.managers.CommandManager;
import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.rmiInterfaces.IMessage;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.ServerException;

/**
 * Created by Dries on 23/10/2015.
 */
public class ChatParticipator extends UnicastRemoteObject implements IChatParticipator {
    private String username;
    private IChatSession chatSession;
    private IChatParticipator host;
    private boolean changingHost=false;

    public ChatParticipator() throws RemoteException { }

    public ChatParticipator(String username, IChatSession chatSession) throws RemoteException {
        this.username = username;
        this.chatSession = chatSession;
    }

    public IChatParticipator getHost() {
        return host;
    }

    public void setHost(IChatParticipator host) {
        this.host = host;
    }

    @Override
    public void addChatSession(IChatSession chatSession) throws RemoteException {
        this.chatSession = chatSession;
    }

    @Override
    public void notifyListener(ChatNotificationType cnt, IMessage msg) throws RemoteException, InterruptedException {
        if (msg.getMessage().startsWith("/") && !msg.getUsername().equalsIgnoreCase(username)) {
            String reply = CommandManager.parse(msg.getMessage());
            System.out.println(reply);
            chatSession.newMessage(reply, username);
        }
    }

    @Override
    public void notifyListener(ChatNotificationType cnt, IChatParticipator newParticipator) throws RemoteException {

    }

    @Override
    public String getName() throws RemoteException {
        return username;
    }

    @Override
    public String getChatName() throws RemoteException {
        return chatSession.getChatName();
    }

    /**
     * Server should not clone the ChatSession
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
     * Notification to server that newHost is hosting the chatsession
     * @param newHost reference to the new host
     * @return true if the actual host is no longer alive, false if host is still reachable
     * @throws RemoteException
     */
    @Override
    public synchronized boolean hostChat(IChatParticipator newHost) throws RemoteException {
        System.out.println("ChatParticipator notifying that host has left");
        try {
            if (host.alive()) return false;
        } catch (RemoteException re) {
            if (changingHost) throw new RemoteException("Someone is already taking over...");
            host = newHost;
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

    @Override
    public void hostChanged(IChatParticipator newHost, IChatSession newSession) throws RemoteException {
        this.host = newHost;
        this.chatSession = newSession;
        setChangingHost(false);
        chatSession.joinSession(this);
    }
}
