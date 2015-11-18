package be.uantwerpen.server;

import java.util.Comparator;
import java.util.HashSet;

/**
 * Created by Dries on 16/10/2015.
 */
public class Client implements Comparator<Client> {
    private String username, fullName;
    private String password;
    private ClientSession activeSession;
    private HashSet<Client> friends;

    public Client(String username, String password, String fullName) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.friends = new HashSet<>();
    }

    public ClientSession getActiveSession() {
        return activeSession;
    }

    public void setActiveSession(ClientSession activeSession) {
        this.activeSession = activeSession;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() { return fullName; }

    @Override
    public String toString() {
        return "Client{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void updateFriends(Client friend, boolean add) {
        if (add) {
            friends.add(friend);
        } else friends.remove(friend);
    }

    public boolean isFriend(String username) {
        boolean isFriend = false;
        for (Client cl : friends) {
            if (cl.getUsername().equalsIgnoreCase(username)) {
                isFriend = true;
                break;
            }
        }
        return isFriend;
    }

    public HashSet<Client> getFriends() {
        return friends;
    }

    @Override
    public int compare(Client o1, Client o2) {
        return o1.getUsername().compareTo(o2.getUsername());
    }
}
