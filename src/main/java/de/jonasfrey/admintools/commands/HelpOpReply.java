package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import de.jonasfrey.admintools.exceptions.JFUnknownPlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Jonas Frey
 * @version 1.0, 17.03.18
 */

public class HelpOpReply extends JFCommand {
    public HelpOpReply(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        if (!(sender instanceof Player)) {
            plugin.getLogger().warning(JFLiterals.kHasToBeExecutedAsPlayer);
            return true;
        }

        Player playerSender = (Player) sender;

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
        String message = String.join(" ", args);
        target.sendMessage(JFLiterals.kHelpOpReplyInternMessage(playerSender.getName(), message));
        // Show the message to all players with permissions to reply
        for (Player pl : plugin.getServer().getOnlinePlayers()) {
            if (pl.hasPermission(cmd.getPermission()) && !(pl.getName().equals(target.getName()))) {
                pl.sendMessage(JFLiterals.kHelpOpReplyExternMessage(playerSender.getName(), target.getName(), message));
            }
        }

        return true;
    }
}
