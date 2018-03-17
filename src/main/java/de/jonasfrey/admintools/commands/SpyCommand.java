package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import de.jonasfrey.admintools.exceptions.JFUnknownPlayerException;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Jonas Frey
 * @version 1.0, 17.03.18
 */

public class SpyCommand extends JFCommand {
    
    public SpyCommand(AdminTools plugin) {
        super(plugin);
    }
    
    // /spy < get | remove | set > [player]
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        if (!(sender instanceof Player)) {
            plugin.getLogger().warning(JFLiterals.kHasToBeExecutedAsPlayer);
            return true;
        }

        if (args.length == 0 || args.length > 2) {
            return false;
        }

        Player playerSender = (Player) sender;

        try {
            switch (args[0].toLowerCase()) {
                case "get":
                    return spyGet(playerSender, args, cmd);
                case "remove":
                    return spyRemove(playerSender, args, cmd);
                case "set":
                    return spySet(playerSender, args, cmd);
            }
        } catch (JFUnknownPlayerException e) {
            sender.sendMessage(JFLiterals.playerDoesNotExist(e.getPlayerName()));
            return true;
        }

        return false;
    }
    
    private boolean spyGet(Player sender, String[] args, Command cmd) {
        if (args.length != 1) {
            return false;
        }

        if (!plugin.getUtils().spyPlayers.containsKey(sender)) {
            sender.sendMessage(JFLiterals.kNotSpying);
            return true;
        }
        sender.sendMessage(JFLiterals.spyingAtPlayer(plugin.getUtils().spyPlayers.get(sender).getName()));
        return true;
    }

    private boolean spyRemove(Player sender, String[] args, Command cmd) {
        if (args.length != 1) {
            return false;
        }

        if (!plugin.getUtils().spyPlayers.containsKey(sender)) {
            sender.sendMessage(JFLiterals.kNotSpying);
            return true;
        }
        plugin.getUtils().spyPlayers.remove(sender);
        sender.sendMessage(JFLiterals.removedSpyingAtPlayer(plugin.getUtils().spyPlayers.get(sender).getName()));
        
        return true;
    }

    private boolean spySet(Player sender, String[] args, Command cmd) throws JFUnknownPlayerException {
        if (args.length != 2) {
            return false;
        }

        OfflinePlayer target = plugin.getUtils().getOfflinePlayer(args[1]);
        if (plugin.getUtils().spyPlayers.containsKey(sender)) {
            sender.sendMessage(JFLiterals.kAlreadySpying);
            return true;
        }
        plugin.getUtils().spyPlayers.put(sender, target);
        sender.sendMessage(JFLiterals.nowSpying(target.getName()));
        return true;
    }
}
