package be.uantwerpen.managers;

import be.uantwerpen.interfaces.IMainManager;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 26/10/2015.
 */
public class MainManager implements IMainManager {
    private static MainManager instance;
    private ClientSessionManager clientSessionManager;
    private UserManager userManager;

    public static MainManager getInstance() {
        if (instance == null) instance = new MainManager();
        return instance;
    }

    private MainManager() {
        this.clientSessionManager = new ClientSessionManager();
        this.userManager = new UserManager();
    }

    @Override
    public boolean addFriend(String username, String friendName) throws RemoteException {
        return userManager.addFriend(username, friendName);
    }

    @Override
    public boolean removeFriend(String username) {
        return false;
    }

    @Override
    public ArrayList<String> getOnlineFriends(String username) throws RemoteException {
        return userManager.getOnlineFriends(username);
    }
}
