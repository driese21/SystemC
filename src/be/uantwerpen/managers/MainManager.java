package be.uantwerpen.managers;

import be.uantwerpen.exceptions.UnknownClientException;
import be.uantwerpen.interfaces.IMainManager;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 26/10/2015.
 */
public class MainManager implements IMainManager {
    private ClientSessionManager clientSessionManager;
    private UserManager userManager;

    public MainManager() {
        this.clientSessionManager = new ClientSessionManager();
        this.userManager = new UserManager();
    }

    //region UserManager
    @Override
    public boolean addFriend(String username, String friendName) throws RemoteException, UnknownClientException {
        return userManager.addFriend(username, friendName);
    }

    @Override
    public boolean removeFriend(String username, String friendName) throws RemoteException, UnknownClientException {
        return userManager.removeFriend(username, friendName);
    }

    @Override
    public ArrayList<String> getFriends(String username, boolean online) throws RemoteException {
        return userManager.getFriends(username, online);
    }

    //endregion

    @Override
    public void run() { }
}
