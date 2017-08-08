package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Jonas Frey
 * @version 1.0, 08.08.17
 */
public class ColorsCommand extends JFCommand {

    public ColorsCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);
        
        if (args.length != 0) return false;
        
        for (ChatColor c : ChatColor.values()) {
            if (c.isColor()) {
                sender.sendMessage(c + c.name() + ": &" + c.getChar());
            }
        }
        
        return true;
    }
    
}
