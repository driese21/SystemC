package be.uantwerpen.rmiInterfaces;

import be.uantwerpen.client.Client;

import be.uantwerpen.exceptions.ClientNotOnlineException;
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
    ArrayList<String> getOtherUsers() throws RemoteException;
    Client search(String username, boolean online) throws RemoteException, ClientNotOnlineException;
    ArrayList<Client> search(boolean online) throws RemoteException;
}
