package be.uantwerpen.server;

import be.uantwerpen.chat.ChatParticipator;
import be.uantwerpen.server.Client;
import be.uantwerpen.managers.MainManager;
import be.uantwerpen.rmiInterfaces.IChatInitiator;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.rmiInterfaces.IClientSession;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dries on 16/10/2015.
 */
public class ClientSession extends UnicastRemoteObject implements IClientSession {
    private String username;
    private IChatInitiator chatInitiator;
    private Date lastUpdate;
    private MainManager mainManager;

    public ClientSession() throws RemoteException {
        mainManager = new MainManager();
    }

    public ClientSession(String username) throws RemoteException {
        this();
        this.username = username;
        this.lastUpdate = Calendar.getInstance().getTime();
    }

    @Override
    public boolean updateStatus() throws RemoteException {
        this.lastUpdate = Calendar.getInstance().getTime();
        return true;
    }

    @Override
    public boolean addFriend(String friendName) throws RemoteException {
        return mainManager.addFriend(username, friendName);
    }

    @Override
    public ArrayList<String> getFriends() throws RemoteException {
        return ChatServer.getInstance().getFriends(username);
    }

    @Override
    public boolean deleteFriend(String friendName) throws RemoteException {
        return ChatServer.getInstance().deleteFriend(username, friendName);
    }

    /**
     * This gets called by a client who wants to set up a chat with another client
     * @param otherUsername the username of the other user
     * @param ics the chatsession created by the initiating client
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    @Override
    public boolean invite(String otherUsername, IChatSession ics) throws RemoteException {
        System.out.println(username + " is inviting " + otherUsername + " voor een leuk gesprek");
        ClientSession otherClientSession = ChatServer.getInstance().getOnlineClients().get(otherUsername);
        ics.setChatName(getFullname());
        return otherClientSession.invite(ics);
    }

    /**
     * This gets called by clientSessionA, thus this clientsession gets an invitiation
     * @param ics the set up chatsession
     * @throws AlreadyBoundException
     * @throws RemoteException
     */
    @Override
    public boolean invite(IChatSession ics) throws RemoteException {
        ChatParticipator chatParticipator = new ChatParticipator("BRUCE WAYNE");
        if (chatInitiator.initialHandshake(ics)) {
            if (ics.joinSession(chatParticipator, true)) {
                chatParticipator.setHost(ics.getHost());
                ChatServer.getInstance().addChatSession(ics, chatParticipator);
            } else {
                System.out.println("Something went wrong while adding SERVER participator to session...");
                return false;
            }
        } else {
            System.out.println("Something with wrong while handshaking my client...");
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<Client> search(boolean online) throws RemoteException {
        return null;
    }

    @Override
    public void setChatInitiator(IChatInitiator ici) throws RemoteException {
        chatInitiator = ici;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getFullname() throws RemoteException {
        return ChatServer.getInstance().getClient(username).getFullName();
    }
}
