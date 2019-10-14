package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Jonas Frey
 * @version 1.0, 06.07.2018
 */

public class ShowMeCommand extends JFCommand {
    public ShowMeCommand(AdminTools plugin) {
        super(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);
        
        if (args.length < 1) {
            return false;
        }
        
        Player target = (Player) sender;
        
        String message = String.join(" ", args).trim();
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        sender.sendMessage(JFLiterals.kMessageShown);
        
        return true;
    }
}
