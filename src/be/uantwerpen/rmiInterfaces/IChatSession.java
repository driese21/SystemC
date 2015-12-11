package be.uantwerpen.rmiInterfaces;

import be.uantwerpen.chat.ChatParticipatorKey;
import be.uantwerpen.enums.ChatNotificationType;
import be.uantwerpen.chat.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Dries on 16/10/2015.
 */
public interface IChatSession extends Remote {
    boolean newMessage(String msg, String username) throws RemoteException;
    void notifyParticipators(ChatNotificationType cnt, Message msg) throws RemoteException;
    void notifyParticipators(ChatNotificationType cnt, ChatParticipatorKey cpk) throws RemoteException;
    boolean joinSession(IChatParticipator participator, boolean host) throws RemoteException;
    boolean joinSession(ChatParticipatorKey cpk) throws RemoteException;
    boolean leaveSession(String username) throws RemoteException;
    IChatParticipator getHost() throws RemoteException;
    String getChatName() throws RemoteException;
    ArrayList<IMessage> getMessages() throws RemoteException;
    void setChatName(String chatName) throws RemoteException;
    void chooseChatName() throws RemoteException;
    HashSet<IChatParticipator> getOtherParticipators() throws RemoteException;
    boolean hostQuit(String oldHost, ChatParticipatorKey newHost) throws RemoteException;
}
