package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * @author Jonas Frey
 * @version 1.0, 11.07.17
 */

public class VoteFlyCommand extends JFCommand {
    
    public VoteFlyCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        String targetName = args.length == 1 ? args[0] : sender.getName();
        Player target = plugin.getServer().getPlayer(targetName);
        
        if (args.length <= 1) {
            YamlConfiguration userData = plugin.utils.getUserData(target.getUniqueId());
            int minutes = userData.getInt("votefly");
            userData.set("votefly", minutes + JFLiterals.kVoteFlyDurationMinutes);
            plugin.utils.saveUserData(userData, target.getUniqueId());
            plugin.utils.addUserSubgroup(target.getName(), "VoteFly");
            target.sendMessage(JFLiterals.kVoteflyActivated);
            return true;
        }
        
        return false;
    }
    
}
