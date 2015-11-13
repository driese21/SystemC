package be.uantwerpen.interfaces;

import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.exceptions.ClientNotOnlineException;

import java.rmi.RemoteException;

/**
 * Created by Dries on 26/10/2015.
 */
public interface IClientSessionManager {
    public IChatParticipator sendInvite(String friendName) throws RemoteException, ClientNotOnlineException;
}
