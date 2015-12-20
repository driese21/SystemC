package be.uantwerpen.server;

import be.uantwerpen.enums.ClientStatusType;
import be.uantwerpen.exceptions.UnknownClientException;
import be.uantwerpen.interfaces.IClientSessionManager;
import be.uantwerpen.interfaces.IUserManager;
import be.uantwerpen.rmiInterfaces.IClientListener;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.rmiInterfaces.IClientSession;
import be.uantwerpen.exceptions.ClientNotOnlineException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class contains methods to adjust a user's friend list
 *
 * Created by Dries on 16/10/2015.
 */
public class ClientSession extends UnicastRemoteObject implements IClientSession {
    private String username;
    private IClientListener clientListener;
    private IUserManager userManager;
    private IClientSessionManager clientSessionManager;

    public ClientSession() throws RemoteException {
    }

    public ClientSession(String username, IUserManager userManager) throws RemoteException {
        this();
        this.username = username;
        this.userManager = userManager;
    }

    /**
     * Adds a friend
     *
     * @param friendName the friend
     * @return true if the friend is added
     * @throws RemoteException
     * @throws UnknownClientException
     */
    @Override
    public boolean addFriend(String friendName) throws RemoteException, UnknownClientException {
        return userManager.addFriend(username, friendName);
    }

    @Override
    public ArrayList<String> getFriends() throws RemoteException {
        return userManager.getFriends(username);
    }

    /**
     * Deletes a friend
     *
     * @param friendName the friend
     * @return true if the friend is deleted
     * @throws RemoteException
     * @throws UnknownClientException
     */
    @Override
    public boolean deleteFriend(String friendName) throws RemoteException, UnknownClientException {
        return userManager.removeFriend(username, friendName);
    }

    /**
     * This gets invoked by a client who wants to set up a chat with another client
     * @param otherUsername the username of the other user
     * @param ics the ChatSession created by the initiating client
     * @throws RemoteException
     * @throws ClientNotOnlineException
     */
    @Override
    public IChatSession sendInvite(String otherUsername, IChatSession ics) throws RemoteException, UnknownClientException {
        return clientSessionManager.sendInvite(otherUsername, ics);
    }

    /**
     * This gets invoked by another ClientSession
     * @param ics the set up ChatSession
     * @throws RemoteException
     */
    @Override
    public boolean invite(IChatSession ics) throws RemoteException {
        return clientSessionManager.invite(ics);
    }

    public void setClientSessionManager(IClientSessionManager clientSessionManager) {
        this.clientSessionManager = clientSessionManager;
    }

    public IClientListener getClientListener() {
        return this.clientListener;
    }

    public void setClientListener(IClientListener ici) throws RemoteException {
        this.clientListener = ici;
        clientSessionManager.notifyFriends();
    }

    public boolean userAlive() {
        try {
            if (clientListener.alive()) return true;
        } catch (RemoteException e) {
            return false;
        }
        return false;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getFullName() throws RemoteException {
        return clientSessionManager.getFullName();
    }

    @Override
    public ArrayList<IChatSession> getOfflineMessage() throws RemoteException {
        return clientSessionManager.getOfflineMessages();
    }

    @Override
    public void offlineMessagesRead() throws RemoteException {
        clientSessionManager.offlineMessagesRead();
    }
}
