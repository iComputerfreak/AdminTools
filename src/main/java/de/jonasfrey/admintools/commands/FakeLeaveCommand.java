package de.jonasfrey.admintools.commands;

/*
 * Copyright jonasfrey.
 * Created on 10.07.17.
 */

import de.jonasfrey.admintools.AdminTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FakeLeaveCommand extends JFCommand {

    public FakeLeaveCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);
        if (args.length > 1) {
            return false;
        }

        String target = sender.getName();
        if (args.length == 1) {
            target = args[0];
        }
        String msg = plugin.getConfig().getString("messages.leave").replaceAll("%player%", target);
        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));

        return true;
    }
}
