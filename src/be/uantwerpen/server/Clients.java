package be.uantwerpen.server;

import be.uantwerpen.server.client.Client;
import be.uantwerpen.server.client.ClientKey;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

/**
 * This class makes a list of clients to be saved for later use.
 * For example: if a user quits the application, he shouldn't have an empty friend list in his next session.
 *
 * Creator: Seb
 * Date: 17/11/2015
 */
@XmlRootElement(name = "clients")
@XmlAccessorType(XmlAccessType.FIELD)
public class Clients {
    private HashMap<ClientKey, Client> clients;

    public Clients() {
        clients = new HashMap<>();
    }

    public Clients(HashMap<ClientKey, Client> clients) {
        this.clients = clients;
    }

    public HashMap<ClientKey, Client> getClients() {
        return clients;
    }

    public void setClients(HashMap<ClientKey, Client> clients) {
        this.clients = clients;
    }
}
