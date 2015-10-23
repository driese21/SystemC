package be.uantwerpen.chat;

import be.uantwerpen.server.*;
import be.uantwerpen.rmiInterfaces.IChatListener;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

/**
 * Created by Dries on 23/10/2015.
 */
public class ChatListener extends UnicastRemoteObject implements IChatListener {
    private String newMessage;

    public ChatListener() throws RemoteException { }

    @Override
    public void notifyListener(String msg) {
        this.newMessage = msg;
        System.out.println("[CLIENT] " + msg);
    }
}
