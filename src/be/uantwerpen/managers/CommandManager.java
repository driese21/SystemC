package be.uantwerpen.managers;

import java.util.HashMap;

/**
 * Created by Dries on 1/11/2015.
 *
 * Users can add and execute commands in a chat session.
 */
public class CommandManager extends Thread {
    private static HashMap<String, String> commandSet = new HashMap<>();

    /**
     * The server reads messages to see if there is command to be added or executed.
     *
     * @param command the messages
     * @return a reply
     */
    public static String parse(String command) {
        if (command.startsWith("/add")) {
            String[] newCommand = command.split("::");
            if (newCommand.length == 3)
                if (addCommand(newCommand[1], newCommand[2])) return "New command added";
            else return "Wrong notation, try: /add::commandName::reply";
        }
        String reply = commandSet.get(command.replace("/",""));
        return reply == null ? "Command not found." : reply;
    }

    /**
     * Add a command
     *
     * @param command the new command
     * @param reply the reply that should be the result of the command
     * @return true if the command is successfully added
     */
    public static boolean addCommand(String command, String reply) {
        commandSet.put(command, reply);
        return true;
    }

    public static boolean removeCommand(String command) {
        commandSet.remove(command);
        return true;
    }
}
