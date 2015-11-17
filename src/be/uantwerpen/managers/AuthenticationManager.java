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
public class AuthenticationManager  {
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
        //System.out.println(username);
        if (c == null) throw new InvalidCredentialsException("User does not exist, try registering instead");
        else if (c.getUsername().equalsIgnoreCase(username) && c.getPassword().equals(password)) {
            //client exists, let's execute some checks
            ClientSession cs = ChatServer.getInstance().getOnlineClients().get(username);
            if (cs == null) {
                //client not online, let's log him in
                cs = new ClientSession(username);
                cs.setClientSessionManager(new ClientSessionManager(cs));
                ChatServer.getInstance().addClientSession(username, cs);
                c.setActiveSession(cs);
                return cs;
            } else return cs;//client already logged on, let's return the session
        } else throw new InvalidCredentialsException("User/password combination is incorrect.");
    }
}
