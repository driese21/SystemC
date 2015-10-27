package be.uantwerpen.rmiInterfaces;

import be.uantwerpen.chat.Message;
import be.uantwerpen.enums.ChatNotificationType;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Dries on 16/10/2015.
 */
public interface IChatSession extends Remote {
    boolean newMessage(Message msg) throws RemoteException, InterruptedException;
    void notifyParticipators(ChatNotificationType nt) throws RemoteException;
    boolean joinSession(IChatParticipator participator) throws RemoteException;
    String getChatMessages() throws RemoteException;
    String getChatName() throws RemoteException;
    void setChatName(String chatName) throws RemoteException;
}
