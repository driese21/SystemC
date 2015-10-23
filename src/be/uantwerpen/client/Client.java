package be.uantwerpen.client;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dries on 16/10/2015.
 */
public class Client {
    private String username, domain, fullName;
    private String password;

    public Client(String username, String password, String fullName) {
        this.username = username.split("@")[0];
        this.domain = username.split("@")[1];
        this.fullName = fullName;
        this.password = password;
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
}
