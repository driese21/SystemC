package be.uantwerpen.server;

import be.uantwerpen.server.client.Client;
import be.uantwerpen.server.client.ClientKey;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

/**
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
