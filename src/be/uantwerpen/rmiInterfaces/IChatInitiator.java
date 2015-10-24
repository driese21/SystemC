package be.uantwerpen.rmiInterfaces;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Dries on 16/10/2015.
 */
public interface IChatInitiator extends Remote {
    boolean initialHandshake(IChatSession otherChatSession) throws RemoteException, AlreadyBoundException;
}
