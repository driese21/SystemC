package be.uantwerpen.server;

import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatSession;

import java.io.*;
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
        //Try to read a file to import friendslist, should this fail, create a new one
        try {
            File file = new File("friendList.txt");
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(f);
            userFriends = (HashMap<String, ArrayList<Client>>) s.readObject();
            s.close();
        } catch (FileNotFoundException e) {
            //Make a new file later
            userFriends = new HashMap<>();
        } catch (ClassNotFoundException e) {
            //This definitely should never happen
            e.printStackTrace();
        } catch (IOException e) {
            //Something went wrong with IO, make a new file later
            userFriends = new HashMap<>();
        }

        this.userFriends = new HashMap<>();
        this.onlineClients = new HashMap<>();
        this.chatSessions = new HashMap<>();
    }

    public ArrayList<String> getFriends(String username) {
        ArrayList<String> friends = new ArrayList<>();
        ArrayList<Client> cfriends = userFriends.get(username);
        cfriends.forEach(cf -> friends.add(cf.getUsername()));
        return friends;
    }

    public synchronized void addChatSession(IChatSession chatSession, IChatParticipator chatParticipator) throws RemoteException {
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
        //Save the friendslist to a file
        try {
            File file = new File("friendList.txt");
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(userFriends);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
