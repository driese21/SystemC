package be.uantwerpen.rmiInterfaces;

import be.uantwerpen.exceptions.ClientNotOnlineException;
import be.uantwerpen.server.Client;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 16/10/2015.
 */
public interface IClientSession extends Remote {
    boolean updateStatus() throws RemoteException;
    boolean addFriend(String friendName) throws RemoteException;
    ArrayList<String> getFriends() throws RemoteException;
    boolean deleteFriend(String friendName) throws RemoteException;
    boolean sendInvite(String otherUsername, IChatSession ics) throws RemoteException, ClientNotOnlineException;
    boolean invite(IChatSession ics) throws AlreadyBoundException, RemoteException;
    void setChatInitiator(IChatInitiator ici) throws RemoteException;
    ArrayList<Client> search(boolean online) throws RemoteException;
    String getUsername() throws RemoteException;
    String getFullname() throws RemoteException;
}
