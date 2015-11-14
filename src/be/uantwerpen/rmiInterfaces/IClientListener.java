package be.uantwerpen.rmiInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Dries on 16/10/2015.
 */
public interface IClientListener extends Remote {
    boolean initialHandshake(IChatSession otherChatSession) throws RemoteException;
    boolean alive() throws RemoteException;
    void friendListUpdated() throws RemoteException;
}
