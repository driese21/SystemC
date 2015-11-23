package be.uantwerpen.chat.offline;

import be.uantwerpen.chat.ChatParticipator;
import be.uantwerpen.enums.ChatNotificationType;
import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatSession;
import be.uantwerpen.rmiInterfaces.IMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Dries on 21/11/2015.
 */
public class ChatSession extends UnicastRemoteObject implements IChatSession {
    private int id;
    private ArrayList<IChatParticipator> participators;
    private ArrayList<Message> messages;
    private IChatParticipator host;
    private String offlineUser;

    public ChatSession() throws RemoteException {
        this.participators = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public ChatSession(int id, ChatParticipator chatParticipator, String offlineUser) throws RemoteException {
        this();
        this.id = id;
        this.host = chatParticipator;
        this.offlineUser = offlineUser;
    }

    public int getId() {
        return id;
    }

    @Override
    public synchronized boolean newMessage(String msg, String username) throws RemoteException {
        if (isAllowedToSend(username)) {
            Message message = new Message(msg, username);
            messages.add(message);
            notifyParticipators(ChatNotificationType.NEWMESSAGE, message);
            return true;
        } else throw new RemoteException("This is a read-only ChatSession");
    }

    @Override
    public void notifyParticipators(ChatNotificationType cnt, Message msg) throws RemoteException {
        new Thread(new DeliveryAgent(participators, msg, cnt)).start();
    }

    @Override
    public void notifyParticipators(ChatNotificationType cnt, IChatParticipator newParticipator) throws RemoteException {
        new Thread(new DeliveryAgent(participators, newParticipator, cnt)).start();
    }

    /**
     * User can join the ChatSession, a maximum of 2 users is allowed (one user, the server user)
     * @param participator A reference to the ChatParticipator
     * @return false if the ChatSession is full, true if it successfully joined
     * @throws RemoteException
     */
    @Override
    public boolean joinSession(IChatParticipator participator) throws RemoteException {
        if (participators.size() == 2) throw new RemoteException("Not more than 1 user is allowed in the offline ChatSession");
        participators.add(participator);
        return true;
    }

    @Override
    public IChatParticipator getHost() throws RemoteException {
        return host;
    }

    @Override
    public String getChatName() throws RemoteException {
        return "Offline " + offlineUser;
    }

    @Override
    public void setChatName(String chatName) throws RemoteException { }

    @Override
    public void chooseChatName() throws RemoteException { }

    @Override
    public ArrayList<IChatParticipator> getOtherParticipators() throws RemoteException {
        return participators;
    }

    @Override
    public boolean hostQuit(IChatParticipator newHost) throws RemoteException {
        throw new RemoteException("Can't change host of offline ChatSession");
    }

    private boolean isAllowedToSend(String userName) {
        if (offlineUser.equalsIgnoreCase(userName)) return false;
        return true;
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
