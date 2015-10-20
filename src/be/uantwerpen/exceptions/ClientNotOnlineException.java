package be.uantwerpen.exceptions;

/**
 * Created by Dries on 20/10/2015.
 */
public class ClientNotOnlineException extends Exception {
    public ClientNotOnlineException(String message) {
        super(message);
    }
}
