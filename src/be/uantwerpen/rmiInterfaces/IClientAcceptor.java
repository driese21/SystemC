package be.uantwerpen.rmiInterfaces;

import be.uantwerpen.exceptions.InvalidCredentialsException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Dries on 4/11/2015.
 */
public interface IClientAcceptor extends Remote {
    IClientSession register(String username, String password, String fullName) throws RemoteException, InvalidCredentialsException;
    IClientSession login(String username, String password) throws RemoteException, InvalidCredentialsException;
}
