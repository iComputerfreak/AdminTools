package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


/**
 * @author Jonas Frey
 * @version 1.0, 11.07.17
 */

public class RainbowCommand extends JFCommand {
    
    public RainbowCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);
        
        if (args.length == 0) {
            return false;
        }

        String text = String.join(" ", args);
        for (ChatColor c : ChatColor.values()) {
            if (c.isColor()) {
                plugin.getServer().broadcastMessage("§6[§aRainbow§6] " + c + text);
            }
        }
        
        return true;
    }
    
}
