package be.uantwerpen.chat.offline;

import be.uantwerpen.chat.ChatParticipatorKey;
import be.uantwerpen.chat.ChatParticipator;
import be.uantwerpen.chat.Message;
import be.uantwerpen.enums.ChatNotificationType;
import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.rmiInterfaces.IMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by Dries on 21/11/2015.
 *
 * This class creates a new chat session. A chat session brings 'chat participators' together and is responsible for
 * pushing messages and notifications. This chat session is meant for offline messages, so it sends messages to the
 * server. The server sends these messages to the intended client when he is online.
 *
 */
public class ChatSession extends UnicastRemoteObject implements IChatSession {
    private int id;
    private HashSet<ChatParticipatorKey> participators;
    private ArrayList<Message> messages;
    private ChatParticipatorKey host;
    private String offlineUser;

    public ChatSession() throws RemoteException {
        this.participators = new HashSet<>();
        this.messages = new ArrayList<>();
    }

    public ChatSession(int id, ChatParticipator chatParticipator, String offlineUser) throws RemoteException {
        this();
        this.id = id;
        this.host = new ChatParticipatorKey(chatParticipator.getUserName(), chatParticipator, true);
        this.offlineUser = offlineUser;
    }

    public int getId() {
        return id;
    }

    /**
     * Sends a new message to the server, and tells for which user the message is intended
      *@param msg the message
     * @param username the intended user
     * @return true if the user is allowed to send a message
     * @throws RemoteException
     */
    @Override
    public synchronized boolean newMessage(String msg, String username) throws RemoteException {
        if (isAllowedToSend(username)) {
            Message message = new Message(msg, username);
            messages.add(message);
            notifyParticipators(ChatNotificationType.NEWMESSAGE, message);
            return true;
        } else throw new RemoteException("This is a read-only ChatSession");
    }

    /**
     * Sends a new message to chat participators when there is a new offline message
     * @param cnt the notification type
     * @param msg the offline message
     * @throws RemoteException
     */
    @Override
    public void notifyParticipators(ChatNotificationType cnt, Message msg) throws RemoteException {
        new Thread(new DeliveryAgent(participators, msg, cnt)).start();
    }

    /**
     * Sends a new message to chat participators when a new chat participator joins the session
     * @param cnt the notification type
     * @param newParticipator the new chat participator
     * @throws RemoteException
     */
    @Override
    public void notifyParticipators(ChatNotificationType cnt, ChatParticipatorKey cpk) throws RemoteException {
        new Thread(new DeliveryAgent(participators, cpk, cnt)).start();
    }

    /**
     * User can join the ChatSession, a maximum of 2 users is allowed (one user, the server user)
     * @param participator A reference to the ChatParticipator
     * @return false if the ChatSession is full, true if it successfully joined
     * @throws RemoteException
     */
    @Override
    public boolean joinSession(IChatParticipator participator, boolean host) throws RemoteException {
        if (participators.size() == 2) throw new RemoteException("Not more than 1 user is allowed in the offline ChatSession");
        participators.add( new ChatParticipatorKey(participator.getUserName(), participator, host));
        return true;
    }

    @Override
    public boolean joinSession(ChatParticipatorKey cpk) throws RemoteException {
        return false;
    }

    @Override
    public boolean leaveSession(String username) throws RemoteException {
        Iterator it = participators.iterator();
        while (it.hasNext()) {
            ChatParticipatorKey cpk = (ChatParticipatorKey) it.next();
            if (cpk.getUserName().equalsIgnoreCase(username)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public IChatParticipator getHost() throws RemoteException {
        return host.getParticipator();
    }

    @Override
    public String getChatName() throws RemoteException {
        return "Offline " + offlineUser;
    }

    @Override
    public void setChatName(String chatName) throws RemoteException { }

    /**
     * A user can change the name of a chat session he has joined
     * @throws RemoteException
     */
    @Override
    public void chooseChatName() throws RemoteException { }

    @Override
    public HashSet<IChatParticipator> getOtherParticipators() throws RemoteException {
        HashSet<IChatParticipator> parts = new HashSet<>(participators.size());
        participators.forEach(cpk -> parts.add(cpk.getParticipator()));
        return parts;
    }

    /**
     * Normally used for online chatsessions when the chat host leaves the session.
     * This is irrelevant in an offline chat.
     * @param newHost a client in the chatsession that takes over as host
     * @return 
     * @throws RemoteException
     */
    @Override
    public boolean hostQuit(String oldHost, ChatParticipatorKey newHost) throws RemoteException {
        throw new RemoteException("Can't change host of offline ChatSession");
    }

    private boolean isAllowedToSend(String userName) {
        return !offlineUser.equalsIgnoreCase(userName);
    }

    @Override
    public ArrayList<IMessage> getMessages() throws RemoteException {
        return new ArrayList<>(messages);
    }

    @Override
    public boolean equals(Object obj) {
        return getId() == ((ChatSession)obj).getId();
    }
}
