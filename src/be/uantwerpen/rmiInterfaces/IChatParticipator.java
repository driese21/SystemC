package be.uantwerpen.rmiInterfaces;

import be.uantwerpen.chat.ChatParticipatorKey;
import be.uantwerpen.enums.ChatNotificationType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * A chat participator is a participator in a chat session
 *
 * Created by Dries on 23/10/2015.
 */
public interface IChatParticipator extends Remote {
    void notifyListener(IMessage msg) throws RemoteException;
    void notifyListener(IChatParticipator chatParticipator, boolean host) throws RemoteException;
    void notifyListener(String userName) throws RemoteException;
    void addChatSession(IChatSession chatSession) throws RemoteException;
    IChatSession getChatSession() throws RemoteException;
    String getUserName() throws RemoteException;
    int getId() throws RemoteException;
    String getChatName() throws RemoteException;
    boolean isServer() throws RemoteException;
    boolean hostChat(IChatParticipator newHost) throws RemoteException;
    boolean alive() throws RemoteException;
    void cloneSession(ChatNotificationType cnt) throws RemoteException;
    void hostChanged(IChatParticipator newHost, IChatSession newSession) throws RemoteException;
}
