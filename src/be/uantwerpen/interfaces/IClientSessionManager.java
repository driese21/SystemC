package be.uantwerpen.interfaces;

import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.exceptions.ClientNotOnlineException;
import be.uantwerpen.rmiInterfaces.IChatSession;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

/**
 * Created by Dries on 26/10/2015.
 */
public interface IClientSessionManager {
    boolean sendInvite(String otherUsername, IChatSession ics) throws RemoteException, ClientNotOnlineException;
    boolean invite(IChatSession ics) throws RemoteException;
    boolean serverJoinSession(IChatSession ics) throws RemoteException;
    void joinOfflineSession(String otherUsername, IChatSession ics) throws RemoteException;
}
