package be.uantwerpen.managers;

import be.uantwerpen.interfaces.IUserManager;
import be.uantwerpen.client.Client;
import be.uantwerpen.server.ChatServer;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 26/10/2015.
 */
public class UserManager implements IUserManager {
    @Override
    public boolean addFriend(String username, String friendName) throws RemoteException {
        ArrayList<Client> friends = ChatServer.getInstance().getUserFriends(username);
        if (friends == null) friends = new ArrayList<>();
        Client friend = ChatServer.getInstance().getClient(username);
        if (friend == null) return false;
        friends.add(friend);
        ChatServer.getInstance().updateUserFriends(username, friends);
        return true;
    }

    @Override
    public boolean removeFriend(String username) {
        return false;
    }

    @Override
    public ArrayList<String> getOnlineFriends(String username) throws RemoteException {
        ArrayList<Client> friends = ChatServer.getInstance().getUserFriends(username);
        if (friends == null) return new ArrayList<>();
        ArrayList<String> userFriends = new ArrayList<>();
        for (Client friend : friends) {
            if (friend.getActiveSession() != null) userFriends.add(friend.getUsername());
        }
        return userFriends;
    }
}
