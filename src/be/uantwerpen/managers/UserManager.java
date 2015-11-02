package be.uantwerpen.managers;

import be.uantwerpen.interfaces.IUserManager;
import be.uantwerpen.server.ChatServer;
import be.uantwerpen.server.Client;
import be.uantwerpen.server.ClientSession;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by Dries on 26/10/2015.
 */
public class UserManager implements IUserManager {
    protected UserManager() {}
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
    public boolean removeFriend(String username, String friendName) throws RemoteException {
        ArrayList<Client> friends = ChatServer.getInstance().getUserFriends(username); //Fetch the user's friends
        if (friends == null) return false; //User has no friends, thus can't remove any.
        Client friend = ChatServer.getInstance().getClient(friendName); //Fetch the friend's profile
        boolean removed = friends.remove(friend); // Returns false if there is no friend to remove
        ChatServer.getInstance().updateUserFriends(username, friends); //Update the list on the server instance
        return removed;
    }

    @Override
    public ArrayList<String> getOnlineFriends(String username) throws RemoteException {
        ArrayList<Client> friends = ChatServer.getInstance().getUserFriends(username);
        if (friends == null) return new ArrayList<>();
        ArrayList<String> userFriends = new ArrayList<>();
        friends.forEach(fr -> { if (fr.getActiveSession()!=null) userFriends.add(fr.getUsername()); });
        return userFriends;
    }

}
