package be.uantwerpen;

import be.uantwerpen.managers.MainManager;
import be.uantwerpen.server.ChatServer;
import be.uantwerpen.server.ClientAcceptor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Dries on 16/10/2015.
 */
public class Server {
    public static void main(String[] args) {
        try {
            //ChatServer cs = ChatServer.getInstance();
            ClientAcceptor ca = new ClientAcceptor();
            Registry registry = LocateRegistry.createRegistry(11337);
            registry.bind("ChatServer", ca);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
