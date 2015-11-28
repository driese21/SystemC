package be.uantwerpen.server;

import be.uantwerpen.chat.offline.ChatSession;
import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.server.client.Client;
import be.uantwerpen.server.client.ClientKey;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Dries on 16/10/2015.
 */
public class ChatServer {
    private String filename = "clients.xml";
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

        try {
            this.clients = readClientsXml(filename);
            if(clients == null || clients.isEmpty()){
                this.clients = new HashMap<>();
            }
        } catch (JAXBException e) {
            //Something went wrong, making new file later.
            this.clients = new HashMap<>();
        }

        this.chatSessions = new HashMap<>();
        this.offlineChatMessages = new HashMap<>();
        this.sessionId = 0;
    }

    public void addClient(Client client) {
        clients.put(getClientKey(client.getUsername()), client);
        try {
            writeToXML(filename);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public Client getClient(String username) { return clients.get(getClientKey(username)); }

    public Client getClient(ClientKey ck) { return clients.get(ck); }

    private ClientKey getClientKey(String username) { return new ClientKey(username); }

    //// TODO: 28/11/2015 Seb, check of ge ergens anders dit kunt opvangen
    public void updateUserFriends(Client user, Client friend, boolean add) {
        user.updateFriends(friend, add);
        try {
            writeToXML(filename);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private HashMap<ClientKey, Client> readClientsXml(String filename) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Clients.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        //We had written this file in marshalling example
        Clients xmlClients = (Clients) jaxbUnmarshaller.unmarshal( new File(filename) );

        System.out.println(xmlClients);

        return xmlClients.getClients();
    }

    public void writeToXML(String filename) throws JAXBException {
        //Marshall to XML
        JAXBContext context = JAXBContext.newInstance(Clients.class);

        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        Clients xmlClients = new Clients(clients);

        m.marshal(xmlClients, new File(filename));
    }

/*    public HashSet<Client> getFriends(String username) {
        return getClient(username).getFriends().stream().map(this::getClient).collect(Collectors.toCollection(HashSet::new));
    }*/

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
        ClientKey ck = getClientKey(username);
        HashSet<ChatSession> offlineSessions = offlineChatMessages.get(ck);
        if (offlineSessions == null) return null;
        else return new ArrayList<>(offlineChatMessages.get(ck));
    }

    public void offlineMessagesRead(String username) {
        System.out.println("Removing offline messages for " + username);
        if (offlineChatMessages.remove(getClientKey(username)) != null) System.out.println("User had offline ChatSessions, now removed");
        else System.out.println("User didn't have offline sessions, nothing happened");
    }

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
