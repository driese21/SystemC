package be.uantwerpen.managers;

import java.util.HashMap;

/**
 * Created by Dries on 1/11/2015.
 */
public class CommandManager extends Thread {
    private static HashMap<String, String> commandSet = new HashMap<>();

    public static String parse(String command) {
        if (command.startsWith("add")) {
            String[] newCommand = command.split(" ");
            if (newCommand.length == 3)
                if (addCommand(newCommand[1], newCommand[2])) return "New command added";
            else return "Wrong notation, try: add commandName reply";
        }
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
