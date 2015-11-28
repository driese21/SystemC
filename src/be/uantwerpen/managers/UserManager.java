package be.uantwerpen.managers;

import be.uantwerpen.exceptions.UnknownClientException;
import be.uantwerpen.interfaces.IUserManager;
import be.uantwerpen.server.ChatServer;
import be.uantwerpen.server.client.Client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Created by Dries on 26/10/2015.
 */
public class UserManager implements IUserManager {
    protected UserManager() {}

    /**
     * Adds a friend for a given user, also mutually adds the user to the friend's list
     * @param username username initiating the friendship
     * @param friendName the friend's name we want to add
     * @return Will return true if the friend has been added, false if the users are already friends, otherwise throws error
     * @throws RemoteException
     * @throws UnknownClientException
     */
    @Override
    public boolean addFriend(String username, String friendName) throws RemoteException, UnknownClientException {
        if (username.equalsIgnoreCase(friendName)) throw new UnknownClientException("You can't add yourself silly...");
        Client user = ChatServer.getInstance().getClient(username), friend = ChatServer.getInstance().getClient(friendName);
        if (user == null) throw new UnknownClientException("User's username didn't yield a Client, this is very wrong!");
        if (friend == null) throw new UnknownClientException("The user you want too add does not exist!");
        if (user.isFriend(friendName) && friend.isFriend(username)) return false; //already friends
        //mutually add each other
        updateFriends(user,friend,true);
        return true;
    }

    @Override
    public boolean removeFriend(String username, String friendName) throws RemoteException, UnknownClientException {
        if (username.equalsIgnoreCase(friendName)) throw new UnknownClientException("You can't remove yourself silly...");
        Client user = ChatServer.getInstance().getClient(username), exFriend = ChatServer.getInstance().getClient(friendName); //Fetch the friend's profile
        if (user == null) throw new UnknownClientException("User'ss username didn't yield a Client, this is very wrong!");
        if (exFriend == null) throw new UnknownClientException("The user you want too remove does not exist!");
        if (!user.isFriend(friendName) && !exFriend.isFriend(username)) return true; //not even friends
        //mutually remove each other
        updateFriends(user,exFriend,false);
        return true;
    }

    /**
     * Mutually adds or removes a friend
     * @param user The user initiating add or remove
     * @param friend The friend we want to add or remove
     * @param addFriend true if we want to add him/her, false if we want to remove him/her
     */
    private void updateFriends(Client user, Client friend, boolean addFriend) throws RemoteException {
        user.updateFriends(friend, addFriend);
        friend.updateFriends(user, addFriend);
        //notify user's ClientListener that their friend's list has been updated
        user.getActiveSession().getClientListener().friendListUpdated();
        friend.getActiveSession().getClientListener().friendListUpdated();
    }

    @Override
    public ArrayList<String> getFriends(String username) throws RemoteException {
        HashSet<Client> friends = ChatServer.getInstance().getClient(username).getFriends().stream().map(ck -> ChatServer.getInstance().getClient(ck)).collect(Collectors.toCollection(HashSet::new));
        ArrayList<String> userFriends = new ArrayList<>();
        friends.forEach(fr -> userFriends.add(fr.getUsername()));
        return userFriends;
    }
}
