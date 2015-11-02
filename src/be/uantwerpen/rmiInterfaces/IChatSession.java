package be.uantwerpen.rmiInterfaces;

import be.uantwerpen.enums.ChatNotificationType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 16/10/2015.
 */
public interface IChatSession extends Remote {
    boolean newMessage(String msg, String username) throws RemoteException, InterruptedException;
    void notifyParticipators(ChatNotificationType cnt, IMessage msg) throws RemoteException;
    void notifyParticipators(ChatNotificationType cnt, IChatParticipator newParticipator) throws RemoteException;
    boolean joinSession(IChatParticipator participator) throws RemoteException;
    boolean joinSession(IChatParticipator participator, boolean silent) throws RemoteException;
    IChatParticipator getHost() throws RemoteException;
    String getChatMessages() throws RemoteException;
    String getChatName() throws RemoteException;
    void setChatName(String chatName) throws RemoteException;
    boolean hostQuit(IChatParticipator newHost) throws RemoteException;
    ArrayList<IChatParticipator> getOtherParticipators() throws RemoteException;
    void setParticipators(ArrayList<IChatParticipator> participators) throws RemoteException;
}
