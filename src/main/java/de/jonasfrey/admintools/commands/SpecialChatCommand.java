package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.SpecialChatType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Jonas Frey
 * @version 1.0, 08.08.17
 */

public abstract class SpecialChatCommand extends JFCommand {
    
    private SpecialChatType type;

    public SpecialChatCommand(AdminTools plugin, SpecialChatType type) {
        super(plugin);
        this.type = type;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        if (!(sender instanceof Player)) {
            plugin.getLogger().warning("This command has to be executed as a player");
            return true;
        }

        Player playerSender = (Player) sender;
        
        if (args.length == 0) {
            plugin.getUtils().enableSpecialChat(type, playerSender);
        } else {
            plugin.getUtils().writeInSpecialChat(type, playerSender, String.join(" ", args));
        }
        
        return true;
    }
    
}
