package be.uantwerpen.server;

import be.uantwerpen.chat.offline.ChatSession;
import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatSession;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Dries on 16/10/2015.
 */
public class ChatServer {
    private static ChatServer instance = new ChatServer();
    private HashMap<String, Client> clients;
    private HashMap<String, ClientSession> onlineClients;
    private HashMap<IChatSession, IChatParticipator> chatSessions; //chatsessions that server has joined
    private HashMap<String, HashSet<ChatSession>> offlineChatMessages;
    private int sessionId;

    public static ChatServer getInstance() {
        return instance;
    }

    private ChatServer() {
        super();
        this.clients = new HashMap<>();
        this.onlineClients = new HashMap<>();
        this.chatSessions = new HashMap<>();
        this.offlineChatMessages = new HashMap<>();
        this.sessionId = 0;
    }

    public HashSet<Client> getFriends(String username) {
        return clients.get(username).getFriends();
    }

    public synchronized void addChatSession(IChatSession chatSession, IChatParticipator chatParticipator) throws RemoteException {
        chatParticipator.addChatSession(chatSession);
        chatSessions.put(chatSession, chatParticipator);
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

    public void updateUserFriends(Client user, Client friend, boolean add) {
        user.updateFriends(friend, add);
    }

    public void addOfflineSession(String username, ChatSession offlineSession) {
        HashSet<ChatSession> sessions = offlineChatMessages.get(username);
        if (sessions == null) sessions = new HashSet<>();
        sessions.add(offlineSession);
        offlineChatMessages.put(username, sessions);
    }

    public ArrayList<ChatSession> getOfflineChatMessages(String username) {
        System.out.println(username);
        HashSet<ChatSession> offlineSessions = offlineChatMessages.get(username);
        if (offlineSessions == null) return null;
        else return new ArrayList<>(offlineChatMessages.get(username));
    }

    public void offlineMessagesRead(String username) {
        System.out.println("Removing offline messages for " + username);
        if (offlineChatMessages.remove(username) != null) System.out.println("User had offline ChatSessions, now removed");
        else System.out.println("User didn't have offline sessions, nothing happened");
    }

    public void offlineMessagesRead(String username, IChatSession iChatSession) {
        ChatSession chatSession = (ChatSession) iChatSession;
        HashSet<ChatSession> chatSessions = offlineChatMessages.get(username);
        chatSessions.stream().filter(cs -> cs.equals(chatSession)).forEach(chatSessions::remove);
        offlineChatMessages.put(username, chatSessions);
    }

    public int getSessionId() {
        return sessionId++;
    }
}
