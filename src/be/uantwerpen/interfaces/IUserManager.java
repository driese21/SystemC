package be.uantwerpen.interfaces;

import java.util.ArrayList;

/**
 * Created by Dries on 26/10/2015.
 */
public interface IUserManager {
    boolean addFriend(String username);
    boolean removeFriend(String username);
    ArrayList<String> getOnlineFriends();
}
