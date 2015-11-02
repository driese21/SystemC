package be.uantwerpen.rmiInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Created by Dries on 1/11/2015.
 */
public interface IMessage extends Remote {
    String getUsername() throws RemoteException;
    String getMessage() throws RemoteException;
    Date getDate() throws RemoteException;
}
