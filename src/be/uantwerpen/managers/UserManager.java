package be.uantwerpen.managers;

import be.uantwerpen.exceptions.UnknownClientException;
import be.uantwerpen.interfaces.IUserManager;
import be.uantwerpen.server.ChatServer;
import be.uantwerpen.server.Client;
import be.uantwerpen.server.ClientSession;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Created by Dries on 26/10/2015.
 */
public class UserManager implements IUserManager {
    protected UserManager() {}
    @Override
    public Client addFriend(String username, String friendName) throws RemoteException, UnknownClientException {
        Client user = ChatServer.getInstance().getClient(username), friend = ChatServer.getInstance().getClient(friendName);
        if (friend == null) throw new UnknownClientException("That user does not exist!");
        if (friend.getActiveSession() == null) System.out.println("[USERMANAGER]Friend's session is null");
        //mutually add each other
        ChatServer.getInstance().updateUserFriends(user, friend, true);
        ChatServer.getInstance().updateUserFriends(friend, user, true);
        return friend;
    }

    @Override
    public boolean removeFriend(String username, String friendName) throws RemoteException {
        Client user = ChatServer.getInstance().getClient(username), exFriend = ChatServer.getInstance().getClient(friendName); //Fetch the friend's profile
        if (exFriend == null) return false;
        ChatServer.getInstance().updateUserFriends(user, exFriend, false); //Update the list on the server instance
        ChatServer.getInstance().updateUserFriends(exFriend, user, false); //Update the list on the server instance
        return true;
    }

    @Override
    public ArrayList<String> getFriends(String username, boolean online) throws RemoteException {
        HashSet<Client> friends = ChatServer.getInstance().getFriends(username);
        ArrayList<String> userFriends = new ArrayList<>();
        if (online) friends.forEach(fr -> { if (fr.getActiveSession()!=null) userFriends.add(fr.getUsername()); });
        else friends.forEach(fr -> userFriends.add(fr.getUsername()));
        return userFriends;
    }
}
