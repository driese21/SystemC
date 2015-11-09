package be.uantwerpen.interfaces;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 26/10/2015.
 */
public interface IUserManager {
    boolean addFriend(String username, String friendName) throws RemoteException;
    boolean removeFriend(String username, String friendName) throws RemoteException;
    //ArrayList<String> getOnlineFriends(String username) throws RemoteException;
    ArrayList<String> getFriends(String username, boolean online) throws RemoteException;
}
