package be.uantwerpen.managers;

import be.uantwerpen.interfaces.IMainManager;

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
    public boolean addFriend(String username) {
        return userManager.addFriend(username);
    }

    @Override
    public boolean removeFriend(String username) {
        return userManager.removeFriend(username);
    }

    @Override
    public ArrayList<String> getOnlineFriends() {
        return userManager.getOnlineFriends();
    }
}
