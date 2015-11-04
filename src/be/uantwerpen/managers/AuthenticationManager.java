package be.uantwerpen.managers;

import be.uantwerpen.exceptions.InvalidCredentialsException;
import be.uantwerpen.rmiInterfaces.IClientSession;
import be.uantwerpen.server.ChatServer;
import be.uantwerpen.server.Client;
import be.uantwerpen.server.ClientSession;

import java.rmi.RemoteException;

/**
 * Created by Dries on 4/11/2015.
 */
public class AuthenticationManager {
    public static IClientSession register(String username, String password, String fullName) throws RemoteException, InvalidCredentialsException {
        Client c = new Client(username, password, fullName);
        Client test = ChatServer.getInstance().getClients().get(c.getUsername());
        if (test != null) {
            System.out.println("Client already exists, trying to log in instead...");
            return login(c.getUsername(), c.getPassword());
        }
        //if client does not exist, add him
        ChatServer.getInstance().addClient(c.getUsername(), c);
        System.out.println(c.toString() + " registered, logging in automatically.");
        return login(c.getUsername(), password);
    }

    public static IClientSession login(String username, String password) throws RemoteException, InvalidCredentialsException {
        Client c = ChatServer.getInstance().getClients().get(username);
        if (c == null) {
            System.out.println("Client is null, try registering instead.");
            return null;
        } else if (c.getUsername().equalsIgnoreCase(username) && c.getPassword().equalsIgnoreCase(password)) {
            //client exists, let's execute some checks
            ClientSession cs = ChatServer.getInstance().getOnlineClients().get(username);
            if (cs == null) {
                //client not online, let's log him in
                cs = new ClientSession(username);
                ChatServer.getInstance().addClientSession(username, cs);
                return cs;
            } else {
                //client already logged on, let's return the session
                System.out.println("Just returning previous session");
                return cs;
            }
        } else throw new InvalidCredentialsException("User provided invalid credentials.");
    }

}