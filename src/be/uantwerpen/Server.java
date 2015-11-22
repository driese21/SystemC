package be.uantwerpen;

import be.uantwerpen.server.ServerListener;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Dries on 16/10/2015.
 */
public class Server {
    public static void main(String[] args) {
        try {
            ServerListener ca = new ServerListener();
            Registry registry = LocateRegistry.createRegistry(11337);
            registry.bind("ChatServer", ca);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
