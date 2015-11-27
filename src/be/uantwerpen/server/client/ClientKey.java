package be.uantwerpen.server.client;

import be.uantwerpen.server.Client;

/**
 * Created by Dries on 27/11/2015.
 */
public class ClientKey {
    private final String username;
    private final int hashCode;

    public ClientKey(String username) {
        this.username = username;
        this.hashCode = username.toUpperCase().hashCode();
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof ClientKey && this.username.equalsIgnoreCase(((ClientKey) obj).username);
    }
}
