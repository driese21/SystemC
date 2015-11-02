package be.uantwerpen.managers;

import java.util.HashMap;

/**
 * Created by Dries on 1/11/2015.
 */
public class CommandManager extends Thread {
    private static HashMap<String, String> commandSet;

    private CommandManager() {
        commandSet = new HashMap<>();
    }

    public static String parse(String command) {
        String reply = commandSet.get(command);
        return reply == null ? "Command not found." : reply;
    }

    public static boolean addCommand(String command, String reply) {
        commandSet.put(command, reply);
        return true;
    }

    public static boolean removeCommand(String command) {
        commandSet.remove(command);
        return true;
    }
}
