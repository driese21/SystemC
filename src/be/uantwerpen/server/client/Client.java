package be.uantwerpen.server.client;

import be.uantwerpen.Utilities.XMLHandler;
import be.uantwerpen.server.ClientSession;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Comparator;
import java.util.HashSet;

/**
 * This class looks up information about a client
 *
 * Created by Dries on 16/10/2015.
 */
@XmlRootElement(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
public class Client implements Comparator<Client> {
    private ClientKey clientKey;
    private String fullName;
    private String password;
    @XmlTransient
    private ClientSession activeSession;
    private HashSet<ClientKey> friends;

    public Client() {
        this.friends = new HashSet<>();
    }

    public Client(String username, String password, String fullName) {
        this();
        this.clientKey = new ClientKey(username);
        this.fullName = fullName;
        this.password = password;
    }

    public ClientSession getActiveSession() {
        if (activeSession == null) return null;
        if (activeSession.userAlive()) return activeSession;
        return null;
    }

    public void setActiveSession(ClientSession activeSession) {
        this.activeSession = activeSession;
    }

    public ClientKey getClientKey() {
        return clientKey;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() { return fullName; }

    @Override
    public String toString() {
        return "Client{" +
                "username='" + clientKey.getUsername() + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void updateFriends(Client friend, boolean add) {
        if (add) {
            friends.add(friend.clientKey);
        } else friends.remove(friend.clientKey);

        try {
            XMLHandler.writeClientToXML(this.getFullName(), this);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public boolean isFriend(String username) {
        boolean isFriend = false;
        for (ClientKey cl : friends) {
            if (cl.equals(new ClientKey(username))) {
                isFriend = true;
                break;
            }
        }
        return isFriend;
    }

    public HashSet<ClientKey> getFriends() {
        return friends;
    }

    /**
     * Compare two usernames
     *
     * @param o1 client 1
     * @param o2 client 2
     * @return true if the o1's username equals o2's username
     */
    @Override
    public int compare(Client o1, Client o2) {
        return o1.clientKey.getUsername().compareTo(o2.clientKey.getUsername());
    }
}
