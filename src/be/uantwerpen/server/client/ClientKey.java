package be.uantwerpen.server.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by Dries on 27/11/2015.
 */
@XmlRootElement(name = "clientKey")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientKey {
    private final String username;
    private final int hashCode;

    public ClientKey() {
        this("");
    }

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
