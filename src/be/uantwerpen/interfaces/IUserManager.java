package be.uantwerpen.interfaces;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 26/10/2015.
 */
public interface IUserManager {
    boolean addFriend(String username, String friendName) throws RemoteException;
    boolean removeFriend(String username);
    ArrayList<String> getOnlineFriends(String username) throws RemoteException;
}
