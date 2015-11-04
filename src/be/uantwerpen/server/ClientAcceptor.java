package be.uantwerpen.server;

import be.uantwerpen.exceptions.InvalidCredentialsException;
import be.uantwerpen.managers.AuthenticationManager;
import be.uantwerpen.rmiInterfaces.IClientAcceptor;
import be.uantwerpen.rmiInterfaces.IClientSession;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Dries on 4/11/2015.
 */
public class ClientAcceptor extends UnicastRemoteObject implements IClientAcceptor {
    public ClientAcceptor() throws RemoteException { }

    @Override
    public synchronized IClientSession register(String username, String password, String fullName) throws RemoteException, InvalidCredentialsException {
        return AuthenticationManager.register(username, password, fullName);
    }

    @Override
    public synchronized IClientSession login(String username, String password) throws RemoteException, InvalidCredentialsException {
        return AuthenticationManager.login(username, password);
    }
}
