package be.uantwerpen.server;

import be.uantwerpen.Utilities.XMLHandler;
import be.uantwerpen.chat.offline.ChatSession;
import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.server.client.Client;
import be.uantwerpen.server.client.ClientKey;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class is the link between clients.
 *
 * Created by Dries on 16/10/2015.
 */
public class ChatServer {
    private HashMap<ClientKey, Client> clients;
    private HashMap<IChatSession, IChatParticipator> chatSessions; //chatsessions that server has joined
    private HashMap<ClientKey, HashSet<ChatSession>> offlineChatMessages;
    private int sessionId;

    public ChatServer() {
        this.clients = new HashMap<>();

        try {
            this.clients = XMLHandler.readClientsXml();
            if(clients == null || clients.isEmpty()){
                this.clients = new HashMap<>();
            }
        } catch (JAXBException e) {
            //Something went wrong, making new file later.
            this.clients = new HashMap<>();
        } catch (IOException e) {
            //Something went wrong, making new file later.
            this.clients = new HashMap<>();
        }

        this.chatSessions = new HashMap<>();
        this.offlineChatMessages = new HashMap<>();
        this.sessionId = 0;
    }

    /**
     * Executed when a new user registers to SystemC
     *
     * @param client the new user
     */
    public void addClient(Client client) {
        clients.put(getClientKey(client.getClientKey().getUsername()), client);
        try {
            XMLHandler.writeClientToXML(client.getFullName(), client);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public Client getClient(String username) { return clients.get(getClientKey(username)); }

    public Client getClient(ClientKey ck) { return clients.get(ck); }

    private ClientKey getClientKey(String username) { return new ClientKey(username); }

/*    public HashSet<Client> getFriends(String username) {
        return getClient(username).getFriends().stream().map(this::getClient).collect(Collectors.toCollection(HashSet::new));
    }*/

    /**
     * Adds a participator to a chat session
     *
     * @param chatSession the chat session
     * @param chatParticipator the participator
     * @throws RemoteException
     */
    public synchronized void addChatSession(IChatSession chatSession, IChatParticipator chatParticipator) throws RemoteException {
        chatParticipator.addChatSession(chatSession);
        chatSessions.put(chatSession, chatParticipator);
    }

    /**
     *Adds a participator to an offline chat session
     *
     * @param username the participator
     * @param offlineSession the offline chat session
     */
    public void addOfflineSession(String username, ChatSession offlineSession) {
        HashSet<ChatSession> sessions = offlineChatMessages.get(username);
        if (sessions == null) sessions = new HashSet<>();
        sessions.add(offlineSession);
        offlineChatMessages.put(new ClientKey(username), sessions);
    }

    /**
     *
     * @param username
     * @return
     */
    public ArrayList<ChatSession> getOfflineChatMessages(String username) {
        ClientKey ck = getClientKey(username);
        HashSet<ChatSession> offlineSessions = offlineChatMessages.get(ck);
        if (offlineSessions == null) return null;
        else return new ArrayList<>(offlineChatMessages.get(ck));
    }

    /**
     * Prints the messages a user received while he was offline
     *
     * @param username the user
     */
    public void offlineMessagesRead(String username) {
        System.out.println("Removing offline messages for " + username);
        if (offlineChatMessages.remove(getClientKey(username)) != null) System.out.println("User had offline ChatSessions, now removed");
        else System.out.println("User didn't have offline sessions, nothing happened");
    }

    /**
     * Deletes a user's offline messages if they are read.
     *
     * @param username the user
     * @param iChatSession the chat session that contains the offline messages
     */
    public void offlineMessagesRead(String username, IChatSession iChatSession) {
        ChatSession chatSession = (ChatSession) iChatSession;
        ClientKey ck = getClientKey(username);
        HashSet<ChatSession> chatSessions = offlineChatMessages.get(ck);
        chatSessions.stream().filter(cs -> cs.equals(chatSession)).forEach(chatSessions::remove);
        offlineChatMessages.put(ck, chatSessions);
    }

    public int getSessionId() {
        return sessionId++;
    }
}
