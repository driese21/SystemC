package be.uantwerpen.rmiInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Dries on 23/10/2015.
 */
public interface IChatParticipator extends Remote {
    void notifyListener(String msg) throws RemoteException;
    void addChatSession(IChatSession chatSession) throws RemoteException;
    String getName() throws RemoteException;
    void pushMessage(String msg) throws RemoteException, InterruptedException;
}
