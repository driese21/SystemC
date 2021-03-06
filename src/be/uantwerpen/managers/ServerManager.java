package be.uantwerpen.managers;

import be.uantwerpen.server.ChatServer;
import be.uantwerpen.server.ServerListener;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Dries on 28/11/2015.
 */
public class ServerManager {
    private ServerListener serverListener;
    private ChatServer chatServer;
    private AuthenticationManager authenticationManager;
    private DiscoveryManager discoveryManager;

    public ServerManager() throws RemoteException, AlreadyBoundException {
        this.serverListener = new ServerListener();
        this.chatServer = new ChatServer();
        this.authenticationManager = new AuthenticationManager(chatServer);
        this.serverListener.setAuthenticationManager(authenticationManager);
        this.discoveryManager = new DiscoveryManager();
        registerServer();
    }

    /**
     * Setup a RMI connection
     *
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    private void registerServer() throws RemoteException, AlreadyBoundException {
        System.out.println("Registering server ...");
        Registry registry = LocateRegistry.createRegistry(11337);
        registry.bind("ChatServer", serverListener);
        System.out.println("Server registered");
    }
}
