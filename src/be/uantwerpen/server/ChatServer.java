package be.uantwerpen.server;

import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatSession;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.rmi.ssl.SslRMIServerSocketFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;

/**
 * Created by Dries on 16/10/2015.
 */
public class ChatServer {
    private static ChatServer instance = new ChatServer();
    private HashMap<String, Client> clients;
    private HashMap<String, ArrayList<Client>> userFriends;
    private HashMap<String, ClientSession> onlineClients;
    private HashMap<IChatSession, IChatParticipator> chatSessions; //chatsessions that server has joined

    public static ChatServer getInstance() {
        return instance;
    }

    private ChatServer() {
        super();
        this.clients = new HashMap<>();
        this.userFriends = new HashMap<>();
        this.onlineClients = new HashMap<>();
        this.chatSessions = new HashMap<>();
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

    public void addClient(String username, Client client) { clients.put(username, client); }

    public Client getClient(String username) { return clients.get(username); }

    public HashMap<String, ClientSession> getOnlineClients() {
        return onlineClients;
    }

    public void addClientSession(String username, ClientSession clientSession) {
        onlineClients.put(username, clientSession);
    }

    public ArrayList<Client> getUserFriends(String username) {
        return userFriends.get(username);
    }

    public void updateUserFriends(String username, ArrayList<Client> friends) {
        userFriends.put(username, friends);
    }
}
