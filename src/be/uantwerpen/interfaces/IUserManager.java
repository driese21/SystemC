package be.uantwerpen.interfaces;

import be.uantwerpen.exceptions.UnknownClientException;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dries on 26/10/2015.
 */
public interface IUserManager {
    boolean addFriend(String username, String friendName) throws RemoteException, UnknownClientException;
    boolean removeFriend(String username, String friendName) throws RemoteException, UnknownClientException;
    ArrayList<String> getFriends(String username);
}
