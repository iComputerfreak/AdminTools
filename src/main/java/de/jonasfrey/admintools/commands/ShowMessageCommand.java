package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import de.jonasfrey.admintools.exceptions.JFUnknownPlayerException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Jonas Frey
 * @version 1.0, 17.03.18
 */

public class ShowMessageCommand extends JFCommand {
    public ShowMessageCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        if (args.length < 2) {
            return false;
        }

        Player target;
        try {
            target = plugin.getUtils().getOnlinePlayer(args[0]);
        } catch (JFUnknownPlayerException e) {
            sender.sendMessage(JFLiterals.playerDoesNotExist(e.getPlayerName()));
            return true;
        }
        args[0] = "";
        String message = String.join(" ", args).trim();
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        sender.sendMessage(JFLiterals.kMessageShown);
        
        return true;
    }
}
