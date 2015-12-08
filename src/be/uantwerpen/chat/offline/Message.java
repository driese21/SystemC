package be.uantwerpen.chat.offline;

import be.uantwerpen.rmiInterfaces.IMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dries on 21/11/2015.
 *
 * This class creates the message itself
 */
public class Message extends UnicastRemoteObject implements IMessage {
    private String message, username;
    private Date datePosted;

    public Message() throws RemoteException { }

    public Message(String message, String username) throws RemoteException {
        this.message = message;
        this.username = username;
        this.datePosted = Calendar.getInstance().getTime();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Date getDate() throws RemoteException { return datePosted; }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
