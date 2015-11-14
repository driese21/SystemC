package be.uantwerpen.exceptions;

/**
 * Created by Dries on 14/11/2015.
 */
public class UnknownClientException extends Exception {
    public UnknownClientException() {
    }

    public UnknownClientException(String message) {
        super(message);
    }
}
