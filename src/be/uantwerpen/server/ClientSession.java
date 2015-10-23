package be.uantwerpen.server;

import be.uantwerpen.chat.ChatListener;
import be.uantwerpen.client.Client;
import be.uantwerpen.exceptions.ClientNotOnlineException;
import be.uantwerpen.rmiInterfaces.IChatInitiator;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.rmiInterfaces.IClientSession;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
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

    public ClientSession(String username) throws RemoteException {
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
        return ChatServer.getInstance().addFriend(this.username, friendName);
    }

    @Override
    public ArrayList<String> getFriends() throws RemoteException {
        return ChatServer.getInstance().getFriends(username);
    }

    @Override
    public boolean deleteFriend(String friendName) throws RemoteException {
        return ChatServer.getInstance().deleteFriend(username, friendName);
    }

    /*@Override
    public ArrayList<String> getOtherUsers() throws RemoteException {
        return ChatServer.getInstance().getOtherUsers();
    }*/

    /*@Override
    public IChatSession search(String username, boolean online, IChatSession ics) throws ClientNotOnlineException, RemoteException {
        Client other = ChatServer.getInstance().getClients().get(username);
        if (other == null) return null;
        if (online) {
            ClientSession cs = ChatServer.getInstance().getOnlineClients().get(username);
            if (cs == null) throw new ClientNotOnlineException("You're looking for online clients, but " + username + " is not online.");
        }
        return ics;
    }*/

    @Override
    public void invite(String otherUsername, IChatSession ics) throws RemoteException, AlreadyBoundException {
        System.out.println(username + " wants to chat with " + otherUsername);
        ClientSession otherClientSession = ChatServer.getInstance().getOnlineClients().get(otherUsername);
        otherClientSession.invite(ics);
        ics.addListener(new ChatListener());
    }

    @Override
    public void invite(IChatSession ics) throws AlreadyBoundException, RemoteException {
        System.out.println("want to chat with me?");
        chatInitiator.initialHandshake(ics);
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
}
