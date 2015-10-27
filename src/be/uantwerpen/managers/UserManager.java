package be.uantwerpen.managers;

import be.uantwerpen.interfaces.IUserManager;

import java.util.ArrayList;

/**
 * Created by Dries on 26/10/2015.
 */
public class UserManager implements IUserManager {
    @Override
    public boolean addFriend(String username) {
        return false;
    }

    @Override
    public boolean removeFriend(String username) {
        return false;
    }

    @Override
    public ArrayList<String> getOnlineFriends() {
        return null;
    }
}
