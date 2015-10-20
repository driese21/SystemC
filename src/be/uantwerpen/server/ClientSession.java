package be.uantwerpen.server;

import be.uantwerpen.client.Client;
import be.uantwerpen.exceptions.ClientNotOnlineException;
import be.uantwerpen.rmiInterfaces.IClientSession;

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

    @Override
    public ArrayList<String> getOtherUsers() throws RemoteException {
        return ChatServer.getInstance().getOtherUsers();
    }

    @Override
    public Client search(String username, boolean online) throws RemoteException, ClientNotOnlineException {
        return ChatServer.getInstance().search(username,online);
    }

    @Override
    public ArrayList<Client> search(boolean online) throws RemoteException {
        return ChatServer.getInstance().search(online);
    }

    public String getUsername() {
        return username;
    }
}
