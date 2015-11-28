package be.uantwerpen.Utilities;

import be.uantwerpen.server.Clients;
import be.uantwerpen.server.client.Client;
import be.uantwerpen.server.client.ClientKey;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Creator: Seb
 * Date: 28/11/2015
 */
public class XMLHandler {
    @Deprecated
    public static HashMap<ClientKey, Client> readClientsXml(String filename) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Clients.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        //We had written this file in marshalling example
        Clients xmlClients = (Clients) jaxbUnmarshaller.unmarshal( new File(filename) );

        System.out.println(xmlClients);

        return xmlClients.getClients();
    }

    @Deprecated
    public static void writeClientsToXML(String filename,  HashMap<ClientKey, Client> clients) throws JAXBException {
        //Marshall to XML
        JAXBContext context = JAXBContext.newInstance(Clients.class);

        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        Clients xmlClients = new Clients(clients);

        m.marshal(xmlClients, new File(filename));
    }

    public static HashMap<ClientKey, Client> readClientsXml() throws JAXBException, IOException {
        HashMap<ClientKey, Client> clients = new HashMap<>();

        Files.walk(Paths.get("./clients")).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                Client client = null;
                try {
                    client = readClientXml(filePath.toString());
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
                clients.put(new ClientKey(client.getUsername()), client);
            }
        });

        /*for (final File fileEntry : new File("./clients").listFiles()) {
            Client client = readClientXml(fileEntry.getName());
            clients.put(new ClientKey(client.getUsername()), client);
        }*/
        return clients;
    }

    public static Client readClientXml(String filename) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Client.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        //We had written this file in marshalling example
        Client client = (Client) jaxbUnmarshaller.unmarshal(new File( filename));

        return client;
    }

    public static void writeClientToXML(String filename,  Client client) throws JAXBException {
        //Marshall to XML
        JAXBContext context = JAXBContext.newInstance(Client.class);

        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        m.marshal(client, new File("./clients/" + filename + ".xml"));
    }

}
