package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Jonas Frey
 * @version 1.0, 17.03.18
 */

public class MuteAllCommand extends JFCommand {
    
    public MuteAllCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        if (args.length != 0) {
            return false;
        }
        
        boolean wasDisabled = plugin.getUtils().chatDisabled;
        // Toggle
        plugin.getUtils().chatDisabled = !wasDisabled;
        plugin.getServer().broadcastMessage(JFLiterals.chatUnMuted(wasDisabled));

        return true;
    }
}
