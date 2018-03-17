package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import de.jonasfrey.admintools.exceptions.JFUnknownPlayerException;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Jonas Frey
 * @version 1.0, 17.03.18
 */

public class UUIDForNameCommand extends JFCommand {
    public UUIDForNameCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        if (args.length != 1) {
            return false;
        }
        
        OfflinePlayer target;
        try {
            target = plugin.getUtils().getOfflinePlayer(args[0]);
        } catch (JFUnknownPlayerException e) {
            sender.sendMessage(JFLiterals.playerDoesNotExist(e.getPlayerName()));
            return true;
        }

        sender.sendMessage(JFLiterals.uuidForName(target.getName(), target.getUniqueId()));

        return true;
    }
}
