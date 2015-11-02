package be.uantwerpen.server;

import java.util.Comparator;

/**
 * Created by Dries on 16/10/2015.
 */
public class Client implements Comparator<Client> {
    private String username, domain, fullName;
    private String password;
    private ClientSession activeSession;

    public Client(String username, String password, String fullName) {
        this.username = username.split("@")[0];
        this.domain = username.split("@")[1];
        this.fullName = fullName;
        this.password = password;
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

    @Override
    public String toString() {
        return "Client{" +
                "username='" + username + '\'' +
                ", domain='" + domain + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public int compare(Client o1, Client o2) {
        return o1.getUsername().compareTo(o2.getUsername());
    }
}
