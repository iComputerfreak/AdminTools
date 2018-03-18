package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFFileController;
import de.jonasfrey.admintools.JFLiterals;
import de.jonasfrey.admintools.PlaytimeComparator;
import de.jonasfrey.admintools.exceptions.JFUnknownPlayerException;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author Jonas Frey
 * @version 1.0, 17.03.18
 */

enum DataType {
    PLAYTIME("playtime"),
    KILLS("kills");
    
    String name;
    
    DataType(String type) {
        this.name = type;
    }
    
    public String configKey() {
        return name.toLowerCase();
    }
}

/**
 * Represents a Command to fetch integer data from the userData file (e.g. Playtime, Kills)
 */
public class DataCommand extends JFCommand {
    
    DataType type;
    
    public DataCommand(AdminTools plugin, DataType type) {
        super(plugin);
        this.type = type;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        // /data < me | top | get | set | reset > [player] [amount]

        if (!(sender instanceof Player)) {
            if (args[0].equalsIgnoreCase("me")) {
                plugin.getLogger().warning(JFLiterals.kHasToBeExecutedAsPlayer);
                return true;
            }
        }

        if (args.length == 0) {
            return false;
        }

        try {

            switch (args[0].toLowerCase()) {
                case "me":
                    return dataMe((Player) sender, args, cmd);
                case "top":
                    return dataTop(sender, args, cmd);
                case "get":
                    return dataGet(sender, args, cmd);
                case "set":
                    return dataSet(sender, args, cmd);
                case "reset":
                    return dataReset(sender, args, cmd);
            }

        } catch (JFUnknownPlayerException e) {
            sender.sendMessage(JFLiterals.playerDoesNotExist(e.getPlayerName()));
            return true;
        }

        return false;
    }

    private boolean dataMe(Player sender, String[] args, Command cmd) {
        if (args.length != 1) return false;

        sender.sendMessage(JFLiterals.dataMe(type.name, getData(sender.getUniqueId())));
        return true;
    }

    private boolean dataTop(CommandSender sender, String[] args, Command cmd) {
        // Initialize structures
        OfflinePlayer[] offlinePlayers = plugin.getServer().getOfflinePlayers();
        HashMap<String, Integer> data = new HashMap<>();

        // Get and sort data
        for (OfflinePlayer op : offlinePlayers) {
            data.put(op.getName(), JFFileController.getUserData(op.getUniqueId()).getInt(type.configKey()));
        }
        
        PlaytimeComparator dataComparator = new PlaytimeComparator(data);
        TreeMap<String, Integer> dataSorted = new TreeMap<>(dataComparator);
        dataSorted.putAll(data);
        sender.sendMessage(JFLiterals.kDataTopHeader);

        // Construct top ten
        Object[] topDataKeys = dataSorted.keySet().toArray();
        Object[] topData = dataSorted.values().toArray();

        int max = topDataKeys.length;
        if (max > 10) {
            max = 10;
        }
        for (int i = 0; i < max; i++) {
            int d = Integer.valueOf(topData[i].toString());
            String value = String.valueOf(d);
            if (type == DataType.PLAYTIME) {
                value = plugin.getUtils().getTimeString(d);
            }
            sender.sendMessage(JFLiterals.dataTopLine(i + 1, topDataKeys[i].toString(), value));
        }
        return true;
    }

    private boolean dataGet(CommandSender sender, String[] args, Command cmd) throws JFUnknownPlayerException {

        if (args.length != 2) return false;

        OfflinePlayer target = plugin.getUtils().getOfflinePlayer(args[1]);
        sender.sendMessage(JFLiterals.dataGet(type.name, target.getName(), getData(target.getUniqueId())));
        return true;
    }

    private boolean dataSet(CommandSender sender, String[] args, Command cmd) throws JFUnknownPlayerException {

        if (args.length != 3) return false;

        if (!sender.hasPermission(cmd.getPermission() + ".mod")) {
            sender.sendMessage("§cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
            return true;
        }

        OfflinePlayer target = plugin.getUtils().getOfflinePlayer(args[1]);
        
        int data = parseData(sender, args[2]);
        if (data < 0) {
            // If an error occures while parsing, exit the command
            return true;
        }
        
        // Sava new value and send message
        YamlConfiguration userData = JFFileController.getUserData(target.getUniqueId());
        userData.set(type.configKey(), data);
        JFFileController.saveUserData(userData, target.getUniqueId());
        sender.sendMessage(JFLiterals.dataSet(type.name, target.getName(), getData(target.getUniqueId())));
        return true;
    }

    private boolean dataReset(CommandSender sender, String[] args, Command cmd) throws JFUnknownPlayerException {

        if (args.length != 2) return false;

        if (!sender.hasPermission(cmd.getPermission() + ".mod")) {
            sender.sendMessage("§cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
            return true;
        }

        OfflinePlayer target = plugin.getUtils().getOfflinePlayer(args[1]);
        YamlConfiguration userData = JFFileController.getUserData(target.getUniqueId());
        String data = plugin.getUtils().getTimeString(userData.getInt(type.configKey()));
        JFFileController.saveUserData(userData, target.getUniqueId());
        sender.sendMessage(JFLiterals.dataReset(type.name, target.getName(), data));
        return true;
    }

    private String getData(UUID playerUUID) {
        YamlConfiguration userData = JFFileController.getUserData(playerUUID);
        int value = userData.getInt(type.configKey());
        if (type == DataType.PLAYTIME) {
             return plugin.getUtils().getTimeString(value);
        }
        return String.valueOf(value);
    }
    
    private int parseData(CommandSender sender, String input) {
        // Parse data
        if (type == DataType.PLAYTIME) {
            if (!input.contains(":") || input.split(":").length != 2) {
                sender.sendMessage(JFLiterals.kWrongTimeFormat);
                return -1;
            }

            String[] time = input.split(":");
            int hours = Integer.parseInt(time[0]);
            int minutes = Integer.parseInt(time[1]);
            return minutes + (hours * 60);
        }
        return Integer.parseInt(input);
    }
    
}
