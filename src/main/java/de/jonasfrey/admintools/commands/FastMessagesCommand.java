package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Jonas Frey
 * @version 1.0, 17.03.18
 */

public class FastMessagesCommand extends JFCommand {
    public FastMessagesCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        if (args.length == 0) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                return fmList(sender, args, cmd);
            case "show":
                return fmShow(sender, args, cmd);
            default:
                return fmBroadcast(sender, args, cmd);
        }
    }

    private boolean fmList(CommandSender sender, String[] args, Command cmd) {
        if (args.length != 1) {
            return false;
        }
        
        List<String> messages = plugin.getConfig().getStringList("fastmessages");
        if (messages.isEmpty()) {
            sender.sendMessage(JFLiterals.kNoMessages);
        } else {
            sender.sendMessage(JFLiterals.kMessagesHeader);
            String names = "§7";
            for (String line : messages) {
                String[] ln = line.split(":", 2);
                names += ", " + ln[0];
            }
            names = names.replaceFirst(", ", "");
            sender.sendMessage(names);
        }
        
        return true;
    }

    private boolean fmShow(CommandSender sender, String[] args, Command cmd) {
        if (args.length != 2) {
            return false;
        }
        
        String message = getMessage(args[1]);
        if (message != null) {
            sender.sendMessage("§6[§aTest§6] §f" + message);
        }
        
        sender.sendMessage(JFLiterals.kNoMessageFound);
        
        return true;
    }

    private boolean fmBroadcast(CommandSender sender, String[] args, Command cmd) {
        if (args.length != 2) {
            return false;
        }

        String message = getMessage(args[1]);
        if (message != null) {
            plugin.getServer().broadcastMessage("§6[§4FM§6] §f" + message);
        }
        
        sender.sendMessage(JFLiterals.kNoMessageFound);
        
        return true;
    }
    
    private String getMessage(String name) {
        List<String> messages = plugin.getConfig().getStringList("fastmessages");
        for (String line : messages) {
            String[] ln = line.split(":", 2);
            if (ln[0].equalsIgnoreCase(name)) {
                // Message found
                return line;
            }
        }
        return null;
    }
}
