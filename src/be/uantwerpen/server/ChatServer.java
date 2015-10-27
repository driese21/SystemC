package be.uantwerpen.server;

import be.uantwerpen.client.Client;
import be.uantwerpen.exceptions.InvalidCredentialsException;
import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatServer;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.rmiInterfaces.IClientSession;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dries on 16/10/2015.
 */
public class ChatServer extends UnicastRemoteObject implements IChatServer {
    private static ChatServer instance;
    private HashMap<String, Client> clients;
    private HashMap<String, ArrayList<Client>> userFriends;
    private HashMap<String, ClientSession> onlineClients;
    private HashMap<IChatSession, IChatParticipator> chatSessions;

    public static ChatServer getInstance() throws RemoteException {
        if (instance == null) instance = new ChatServer();
        return instance;
    }

    private ChatServer() throws RemoteException {
        super();
        this.clients = new HashMap<>();
        this.userFriends = new HashMap<>();
        this.onlineClients = new HashMap<>();
        this.chatSessions = new HashMap<>();
    }

    @Override
    public ArrayList<String> showHome(String username) throws RemoteException {
        if (username == null || username.equalsIgnoreCase("")) { //non existing user, show default
            ArrayList<String> defaultHome = new ArrayList<>();
            defaultHome.add("Register");
            defaultHome.add("Login");
            defaultHome.add("Exit");
            return defaultHome;
        }
        return null;
    }

    @Override
    public IClientSession register(String username, String password, String fullName) throws RemoteException, AlreadyBoundException, InvalidCredentialsException {
        Client c = clients.get(username);
        if (c != null) {
            System.out.println("Client already exists, logging in instead.");
            return login(username, password);
        }
        c = new Client(username, password, fullName);
        //if client does not exist, add him
        clients.put(c.getUsername(), c);
        System.out.println(c.toString() + " registered, logging in automatically.");
        return login(c.getUsername(), password);
    }

    @Override
    public IClientSession login(String username, String password) throws RemoteException, AlreadyBoundException, InvalidCredentialsException {
        System.out.println(username);
        Client c = clients.get(username);
        for (Client cl : clients.values()) {
            System.out.println(cl.toString());
        }
        if (c == null) {
            System.out.println("Client is null, try registering instead.");
            return null;
        } else if (c.getUsername().equalsIgnoreCase(username) && c.getPassword().equalsIgnoreCase(password)) {
            //client exists, let's execute some checks
            ClientSession cs = onlineClients.get(username);
            if (cs == null) {
                //client not online, let's log him in
                cs = new ClientSession(username);
                onlineClients.put(username, cs);
                System.out.println( username + " logged in and created session on port " + 11338);
                return cs;
            } else {
                //client already logged on, let's return the session
                System.out.println("Just returning previous session");
                return cs;
            }
        } else throw new InvalidCredentialsException("User provided invalid credentials.");
    }

    public boolean addFriend(String username, String friendUserName) {
        Client friend = clients.get(friendUserName);
        if (friend == null) return false; //user trying to add does not exist
        //other user exists, we can continue
        ArrayList<Client> cfriends = userFriends.get(username);
        for (Client c : cfriends) {
            if (c.getUsername().equalsIgnoreCase(friendUserName)) return true; //friend already added
        }
        cfriends.add(friend); //friend does not exist, add him/her
        userFriends.put(username, cfriends); //update map
        return true;
    }

    public ArrayList<String> getFriends(String username) {
        ArrayList<String> friends = new ArrayList<>();
        ArrayList<Client> cfriends = userFriends.get(username);
        if (cfriends == null) return null;
        cfriends.forEach(cf -> friends.add(cf.getUsername()));
        return friends;
    }

    public boolean deleteFriend(String userName, String friendName) {
        ArrayList<Client> cfriends = userFriends.get(userName);
        if (cfriends == null) return true; //user has no friends, so it's okay
        Client friendToDelete = null;
        for (Client c : cfriends) {
            if (c.getUsername().equalsIgnoreCase(friendName)) {
                friendToDelete = c;
                break;
            }
        }
        if (friendToDelete == null) return true; //friend doesn't exist, so it's okay
        cfriends.remove(friendToDelete); //remove friend
        userFriends.put(userName, cfriends); //update with new list
        return true; //everything okay
    }

    public synchronized void addChatSession(IChatSession chatSession, IChatParticipator chatParticipator) throws RemoteException {
        if (chatSessions.get(chatSession) != null) System.out.println("chatsession already exists...");
        chatParticipator.addChatSession(chatSession);
        chatSessions.put(chatSession, chatParticipator);
    }

    public HashMap<IChatSession, IChatParticipator> getChatSessions() {
        return chatSessions;
    }

    public HashMap<String, Client> getClients() {
        return clients;
    }

    public HashMap<String, ClientSession> getOnlineClients() {
        return onlineClients;
    }
}
