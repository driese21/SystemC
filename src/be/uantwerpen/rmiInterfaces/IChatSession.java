package be.uantwerpen.rmiInterfaces;

import be.uantwerpen.enums.ChatNotificationType;
import sun.plugin2.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 16/10/2015.
 */
public interface IChatSession extends Remote {
    boolean newMessage(String msg, String username) throws RemoteException;
    void notifyParticipators(ChatNotificationType cnt, Message msg) throws RemoteException;
    void notifyParticipators(ChatNotificationType cnt, IChatParticipator newParticipator) throws RemoteException;
    boolean joinSession(IChatParticipator participator) throws RemoteException;
    IChatParticipator getHost() throws RemoteException;
    String getChatMessages() throws RemoteException;
    String getChatName() throws RemoteException;
    void setChatName(String chatName) throws RemoteException;
    void chooseChatName() throws RemoteException;
    ArrayList<IChatParticipator> getOtherParticipators() throws RemoteException;
    void setParticipators(ArrayList<IChatParticipator> participators) throws RemoteException;
    boolean hostQuit(IChatParticipator newHost) throws RemoteException;
}
