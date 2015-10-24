package be.uantwerpen.chat;

import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.rmiInterfaces.IChatSession;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

/**
 * Created by Dries on 23/10/2015.
 */
public class ChatParticipator extends UnicastRemoteObject implements IChatParticipator {
    private String username;
    private String newMessage;
    private IChatSession chatSession;

    public ChatParticipator() throws RemoteException { }

    public ChatParticipator(String username) throws RemoteException {
        this.username = username;
    }

    @Override
    public void addChatSession(IChatSession chatSession) throws RemoteException {
        this.chatSession = chatSession;
    }

    @Override
    public void notifyListener(String msg) {
        this.newMessage = msg;
        System.out.println("[SERVER] " + msg);
    }

    @Override
    public String getName() throws RemoteException {
        return username;
    }

    @Override
    public void pushMessage(String msg) throws RemoteException, InterruptedException {
        Message message = new Message(msg, getName());
        chatSession.newMessage(message);
    }
}
