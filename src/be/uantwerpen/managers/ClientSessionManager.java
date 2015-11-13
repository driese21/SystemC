package be.uantwerpen.managers;

import be.uantwerpen.exceptions.ClientNotOnlineException;
import be.uantwerpen.interfaces.IClientSessionManager;
import be.uantwerpen.rmiInterfaces.IChatParticipator;

import java.rmi.RemoteException;

/**
 * Created by Dries on 26/10/2015.
 */
public class ClientSessionManager extends Thread implements IClientSessionManager {
    protected ClientSessionManager() {
    }

    @Override
    public IChatParticipator sendInvite(String friendName) throws RemoteException, ClientNotOnlineException {
        return null;
    }
}
