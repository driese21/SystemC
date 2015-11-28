package be.uantwerpen;

import be.uantwerpen.managers.ServerManager;
import be.uantwerpen.server.ServerListener;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Dries on 16/10/2015.
 */
public class Server {
    public static void main(String[] args) throws AlreadyBoundException, RemoteException {
        new ServerManager();

    }
}
