package be.uantwerpen.server;

import be.uantwerpen.client.Client;
import be.uantwerpen.exceptions.ClientNotOnlineException;
import be.uantwerpen.exceptions.InvalidCredentialsException;
import be.uantwerpen.rmiInterfaces.IChatServer;
import be.uantwerpen.rmiInterfaces.IClientSession;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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

    public static ChatServer getInstance() throws RemoteException {
        if (instance == null) instance = new ChatServer();
        return instance;
    }

    private ChatServer() throws RemoteException {
        super();
        this.clients = new HashMap<>();
        this.userFriends = new HashMap<>();
        this.onlineClients = new HashMap<>();
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
        /*for (Client cl : clients) {
            //if (cl.getUsername().equalsIgnoreCase(username))
        }*/
        return null;
    }

    @Override
    public IClientSession register(String username, String password) throws RemoteException, AlreadyBoundException, InvalidCredentialsException {
        Client c = clients.get(username);
        if (c != null) {
            System.out.println("Client already exists, logging in instead.");
            return login(username, password);
        }
        c = new Client(username, password);
        /*for(Client c : clients) if (c.getUsername().equalsIgnoreCase(username)) {
            //client already exists
            System.out.println("Client already exists....");
            return login(username, password);
        }*/
        //if client does not exist, add him
        clients.put(username, c);
        System.out.println(username + " registered, logging in automatically.");
        return login(username, password);
    }

    @Override
    public IClientSession login(String username, String password) throws RemoteException, AlreadyBoundException, InvalidCredentialsException {
        Client c = clients.get(username);
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
                Registry registry = LocateRegistry.createRegistry(11338);
                registry.bind("ClientSession-"+username.hashCode(), cs);
                System.out.println( username + " logged in and created session on port " + 11338);
                return cs;
            } else {
                //client already logged on, let's return the session
                System.out.println("Just returning previous session");
                return cs;
            }
        } else throw new InvalidCredentialsException("User provided invalid credentials.");
        /*for (Client cl : clients) {
            if (cl.getUsername().equalsIgnoreCase(username) && cl.getPassword().equalsIgnoreCase(password)) {
                c = cl;
                break;
            }
        }*/
        /*if (c == null) return null; //client does not exist, can't login, most likely wrong credentials
        for (ClientSession cs : onlineClients) {
            if (cs.getUsername().equalsIgnoreCase(username)) return cs;
        }
        //int nextPort = getNextPort();
        ClientSession newSession = new ClientSession(username);
        onlineClients.add(newSession); //should still check if no active session
        Registry registry = LocateRegistry.createRegistry(11338);
        registry.bind("ClientSession-"+username.hashCode(), newSession);
        System.out.println( username + " logged in and created session on port " + 11338);
        System.out.println(onlineClients.size());
        return newSession; //return the session to the client*/
    }

    @Override
    public Client search(String username, boolean online) throws ClientNotOnlineException {
        Client other = clients.get(username);
        /*for (Client c : clients) {
            if (c.getUsername().equalsIgnoreCase(username)) {
                other = c;
                break;
            }
        }*/
        if (other == null) return null;
        if (online) {
            ClientSession cs = onlineClients.get(username);
            if (cs == null) throw new ClientNotOnlineException("You're looking for online clients, but " + username + " is not online.");
        }
        return other;
        /*if (online) { //look only for online users
            for (ClientSession cs : onlineClients) if (cs.getUsername().equalsIgnoreCase(username)) return other;
        }
        //return other user
        return other;*/
    }

    @Override
    public ArrayList<Client> search(boolean online) {
        ArrayList<Client> otherUsers = new ArrayList<>();
        for (ClientSession cs : onlineClients.values()) {
            try {
                otherUsers.add(search(cs.getUsername(), online));
            } catch (ClientNotOnlineException cnoe) {
                //doesn't matter
            }
        }
        /*if (online) {
            for (ClientSession cs : onlineClients) otherUsers.add(search(cs.getUsername(), true));
        }*/
        return otherUsers;
    }

    public ArrayList<String> getOtherUsers() {
        ArrayList<String> otherUsers = new ArrayList<>();
        for (Client c : search(true)) otherUsers.add(c.getUsername());
        return otherUsers;
    }

    public boolean addFriend(String username, String friendUserName) {
        Client friend = clients.get(friendUserName);
        /*for (Client c : clients) {
            if (c.getUsername().equalsIgnoreCase(friendUserName)) {
                friend = c;
                break;
            }
        }*/
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
}
