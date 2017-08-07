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

import java.util.*;

/**
 * @author Jonas Frey
 * @version 1.0, 07.08.17
 */
public class PlaytimeCommand extends JFCommand {
    
    public PlaytimeCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);
        
        // /playtime < me | top | get | set | reset > [player] [time]"
        
        if (!(sender instanceof Player)) {
            if (args[0].equalsIgnoreCase("me")) {
                plugin.getLogger().warning("This command has to be executed as a player");
                return true;
            }
        }

        if (args.length == 0) {
            return false;
        }

        try {

            switch (args[0].toLowerCase()) {
                case "me":
                    return playtimeMe((Player) sender, args, cmd);
                case "top":
                    return playtimeTop(sender, args, cmd);
                case "get":
                    return playtimeGet(sender, args, cmd);
                case "set":
                    return playtimeSet(sender, args, cmd);
                case "reset":
                    return playtimeReset(sender, args, cmd);
            }

        } catch (JFUnknownPlayerException e) {
            sender.sendMessage(JFLiterals.playerDoesNotExist(e.getPlayerName()));
            return true;
        }

        return false;
    }

    private boolean playtimeMe(Player sender, String[] args, Command cmd) {
        
        if (args.length != 1) return false;
        
        sender.sendMessage(JFLiterals.playtimeMe(getPlaytime(sender.getUniqueId())));
        return true;
    }

    private boolean playtimeTop(CommandSender sender, String[] args, Command cmd) {
        OfflinePlayer[] offlinePlayers = plugin.getServer().getOfflinePlayers();
        HashMap<String, Integer> playtimes = new HashMap<>();
        PlaytimeComparator ptComparator = new PlaytimeComparator(playtimes);
        TreeMap<String, Integer> playtimesSorted = new TreeMap<>(ptComparator);

        for (OfflinePlayer op : offlinePlayers) {
            playtimes.put(op.getName(), JFFileController.getUserData(op.getUniqueId()).getInt("playtime"));
        }
        playtimesSorted.putAll(playtimes);
        sender.sendMessage(JFLiterals.kPlaytimeTopHeader);

        Object[] topPlaytimeKeys = playtimesSorted.keySet().toArray();
        Object[] topPlaytimes = playtimesSorted.values().toArray();
        
        int max = topPlaytimeKeys.length;
        if (max > 10) {
            max = 10;
        }
        for (int i = 0; i < max; i++) {
            int pt = Integer.valueOf(topPlaytimes[i].toString());
            sender.sendMessage(JFLiterals.playtimeTopLine(i + 1, topPlaytimeKeys[i].toString(), plugin.getUtils().getTimeString(pt)));
        }
        return true;
    }

    private boolean playtimeGet(CommandSender sender, String[] args, Command cmd) {
        
        if (args.length != 2) return false;
        
        OfflinePlayer target = plugin.getUtils().getOfflinePlayer(args[1]);
        sender.sendMessage(JFLiterals.playtimeGet(target.getName(), getPlaytime(target.getUniqueId())));
        return true;
    }

    private boolean playtimeSet(CommandSender sender, String[] args, Command cmd) {
        
        if (args.length != 3) return false;
        
        OfflinePlayer target = plugin.getUtils().getOfflinePlayer(args[1]);
        // Parse time
        if (!args[2].contains(":") || args[2].split(":").length != 2) {
            sender.sendMessage(JFLiterals.kWrongTimeFormat);
            return true;
        }
        
        String[] time = args[2].split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        int playtime = minutes + (hours * 60);
        YamlConfiguration userData = JFFileController.getUserData(target.getUniqueId());
        userData.set("playtime", playtime);
        JFFileController.saveUserData(userData, target.getUniqueId());
        sender.sendMessage(JFLiterals.playtimeSet(target.getName(), plugin.getUtils().getTimeString(playtime)));
        return true;
    }

    private boolean playtimeReset(CommandSender sender, String[] args, Command cmd) {
        
        if (args.length != 2) return false;
        
        OfflinePlayer target = plugin.getUtils().getOfflinePlayer(args[1]);
        YamlConfiguration userData = JFFileController.getUserData(target.getUniqueId());
        String playtime = plugin.getUtils().getTimeString(userData.getInt("playtime"));
        JFFileController.saveUserData(userData, target.getUniqueId());
        sender.sendMessage(JFLiterals.playtimeReset(target.getName(), playtime));
        return true;
    }
    
    private String getPlaytime(UUID playerUUID) {
        YamlConfiguration userData = JFFileController.getUserData(playerUUID);
        return plugin.getUtils().getTimeString(userData.getInt("playtime"));
    }
}
