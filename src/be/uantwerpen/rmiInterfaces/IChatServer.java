package be.uantwerpen.rmiInterfaces;

import be.uantwerpen.client.Client;
import be.uantwerpen.exceptions.ClientNotOnlineException;
import be.uantwerpen.exceptions.InvalidCredentialsException;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 16/10/2015.
 */
public interface IChatServer extends Remote {
    ArrayList<String> showHome(String username) throws RemoteException;
    IClientSession register(String username, String password, String fullName) throws RemoteException, AlreadyBoundException, InvalidCredentialsException;
    IClientSession login(String username, String password) throws RemoteException, AlreadyBoundException, InvalidCredentialsException;
    Client search(String username, boolean online) throws RemoteException, ClientNotOnlineException;
    ArrayList<Client> search(boolean online) throws RemoteException;
}
