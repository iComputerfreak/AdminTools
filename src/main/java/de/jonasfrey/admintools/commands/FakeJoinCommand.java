package de.jonasfrey.admintools.commands;

/*
 * Copyright jonasfrey.
 * Created on 10.07.17.
 */

import de.jonasfrey.admintools.AdminTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FakeJoinCommand extends JFCommand {


    public FakeJoinCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);
        if (args.length > 1) {
            return false;
        }
        
        String target = args.length == 1 ? args[0] : sender.getName();
        String msg = plugin.getConfig().getString("messages.join").replaceAll("%player%", target);
        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
        
        return true;
    }
}
