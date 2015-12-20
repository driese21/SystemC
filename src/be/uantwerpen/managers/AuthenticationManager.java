package be.uantwerpen.managers;

import be.uantwerpen.exceptions.InvalidCredentialsException;
import be.uantwerpen.rmiInterfaces.IClientSession;
import be.uantwerpen.server.ChatServer;
import be.uantwerpen.server.client.Client;
import be.uantwerpen.server.ClientSession;

import java.rmi.RemoteException;

/**
 * Created by Dries on 4/11/2015.
 *
 * This class makes sure that a client can register and log on to the system.
 */
public class AuthenticationManager  {
    private ChatServer chatServer;

    public AuthenticationManager(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    /**
     * A new client has to create an account to be able to log on to the system.
     * If the client already exists, and the credentials are correct: log on to the system.
     *
     * @param username username that the client has to choose
     * @param password new password
     * @param fullName client's full name
     * @return true if the automatic login succeeds
     * @throws RemoteException
     * @throws InvalidCredentialsException
     */
    public IClientSession register(String username, String password, String fullName) throws RemoteException, InvalidCredentialsException {
        Client c = chatServer.getClient(username);
        if (c != null) {
            System.out.println("User already exists.");
            return login(username, password);
        }
        c = new Client(username, password, fullName);
        //if client does not exist, add him
        chatServer.addClient(c);
        System.out.println(c + " registered, logging in automatically.");
        return login(c.getClientKey().getUsername(), password);
    }

    /**
     * An existing user can log on to the system. If the user doesn't yet exist, try registering instead.
     * The method also makes sure that the user/password combination is correct.
     *
     * @param username username of the user that want's to log in
     * @param password his password
     * @return true if the login succeeds
     * @throws RemoteException
     * @throws InvalidCredentialsException
     */
    public IClientSession login(String username, String password) throws RemoteException, InvalidCredentialsException {
        Client c = chatServer.getClient(username);
        if (c == null) throw new InvalidCredentialsException("User does not exist, try registering instead");
        else if (c.getClientKey().getUsername().equalsIgnoreCase(username) && c.getPassword().equals(password)) {
            //client exists, let's execute some checks
            ClientSession cs = c.getActiveSession();
            if (cs == null) {
                //client not online, let's log him in
                cs = new ClientSession(username, new UserManager(chatServer));
                cs.setClientSessionManager(new ClientSessionManager(cs, chatServer));
                c.setActiveSession(cs);
                return cs;
            } else return cs;//client already logged on, let's return the session
        } else throw new InvalidCredentialsException("User/password combination is incorrect.");
    }
}
