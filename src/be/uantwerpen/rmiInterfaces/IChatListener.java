package be.uantwerpen.rmiInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Dries on 23/10/2015.
 */
public interface IChatListener extends Remote {
    void notifyListener(String msg) throws RemoteException;
}
