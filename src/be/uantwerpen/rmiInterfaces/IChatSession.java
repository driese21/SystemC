package be.uantwerpen.rmiInterfaces;

import be.uantwerpen.chat.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Dries on 16/10/2015.
 */
public interface IChatSession extends Remote {
    boolean newMessage(Message msg) throws RemoteException, InterruptedException;
    void addListener(IChatListener listener) throws RemoteException;
}
