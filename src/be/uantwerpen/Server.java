package be.uantwerpen;

import be.uantwerpen.server.ChatServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Dries on 16/10/2015.
 */
public class Server {
    public static void main(String[] args) {
        try {
            ChatServer cs = ChatServer.getInstance();
            Registry registry = LocateRegistry.createRegistry(11337);
            registry.bind("ChatServer", cs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
