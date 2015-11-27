package be.uantwerpen.server;

import be.uantwerpen.chat.offline.ChatSession;
import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.server.client.ClientKey;

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
    private HashMap<ClientKey, Client> clients;
    private HashMap<IChatSession, IChatParticipator> chatSessions; //chatsessions that server has joined
    private HashMap<ClientKey, HashSet<ChatSession>> offlineChatMessages;
    private int sessionId;

    public static ChatServer getInstance() {
        return instance;
    }

    private ChatServer() {
        super();
        this.clients = new HashMap<>();
        this.chatSessions = new HashMap<>();
        this.offlineChatMessages = new HashMap<>();
        this.sessionId = 0;
    }

    public void addClient(Client client) { clients.put(new ClientKey(client.getUsername()), client); }

    public Client getClient(String username) { return clients.get(new ClientKey(username)); }

    public void updateUserFriends(Client user, Client friend, boolean add) {
        user.updateFriends(friend, add);
    }

    public synchronized void addChatSession(IChatSession chatSession, IChatParticipator chatParticipator) throws RemoteException {
        chatParticipator.addChatSession(chatSession);
        chatSessions.put(chatSession, chatParticipator);
    }

    public void addOfflineSession(String username, ChatSession offlineSession) {
        HashSet<ChatSession> sessions = offlineChatMessages.get(username);
        if (sessions == null) sessions = new HashSet<>();
        sessions.add(offlineSession);
        offlineChatMessages.put(new ClientKey(username), sessions);
    }

    public ArrayList<ChatSession> getOfflineChatMessages(String username) {
        ClientKey ck = new ClientKey(username);
        HashSet<ChatSession> offlineSessions = offlineChatMessages.get(ck);
        if (offlineSessions == null) return null;
        else return new ArrayList<>(offlineChatMessages.get(ck));
    }

    public void offlineMessagesRead(String username) {
        System.out.println("Removing offline messages for " + username);
        if (offlineChatMessages.remove(new ClientKey(username)) != null) System.out.println("User had offline ChatSessions, now removed");
        else System.out.println("User didn't have offline sessions, nothing happened");
    }

    public void offlineMessagesRead(String username, IChatSession iChatSession) {
        ChatSession chatSession = (ChatSession) iChatSession;
        ClientKey ck = new ClientKey(username);
        HashSet<ChatSession> chatSessions = offlineChatMessages.get(ck);
        chatSessions.stream().filter(cs -> cs.equals(chatSession)).forEach(chatSessions::remove);
        offlineChatMessages.put(ck, chatSessions);
    }

    public int getSessionId() {
        return sessionId++;
    }
}
