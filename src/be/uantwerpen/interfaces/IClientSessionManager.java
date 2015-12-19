package be.uantwerpen.interfaces;

import be.uantwerpen.chat.ChatParticipator;
import be.uantwerpen.exceptions.UnknownClientException;
import be.uantwerpen.rmiInterfaces.IChatParticipator;
import be.uantwerpen.exceptions.ClientNotOnlineException;
import be.uantwerpen.rmiInterfaces.IChatSession;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 26/10/2015.
 */
public interface IClientSessionManager {
    IChatSession sendInvite(String otherUsername, IChatSession ics) throws RemoteException, UnknownClientException;
    boolean invite(IChatSession ics) throws RemoteException;
    boolean serverJoinSession(ChatParticipator participator, IChatSession ics, boolean offlineSession) throws RemoteException;
    ArrayList<IChatSession> getOfflineMessages();
    void offlineMessagesRead();
    String getFullName();
}
