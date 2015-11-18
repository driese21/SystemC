package be.uantwerpen.rmiInterfaces;

import be.uantwerpen.exceptions.ClientNotOnlineException;
import be.uantwerpen.enums.ClientStatusType;
import be.uantwerpen.exceptions.UnknownClientException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 16/10/2015.
 */
public interface IClientSession extends Remote {
    boolean updateStatus() throws RemoteException;
    boolean addFriend(String friendName) throws RemoteException, UnknownClientException;
    ArrayList<String> getFriends() throws RemoteException;
    boolean deleteFriend(String friendName) throws RemoteException, UnknownClientException;
    void forwardStatus(ClientStatusType cnt) throws RemoteException;
    boolean sendInvite(String otherUsername, IChatSession ics) throws RemoteException, ClientNotOnlineException;
    boolean invite(IChatSession ics) throws RemoteException;
    void setClientListener(IClientListener ici) throws RemoteException;
    String getUsername() throws RemoteException;
    String getFullname() throws RemoteException;
}
