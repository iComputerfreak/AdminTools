package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFFileController;
import de.jonasfrey.admintools.JFLiterals;
import de.jonasfrey.admintools.exceptions.JFUnknownGroupException;
import de.jonasfrey.admintools.exceptions.JFUnknownPlayerException;
import de.jonasfrey.admintools.exceptions.JFUnknownWorldException;
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
            YamlConfiguration userData = JFFileController.getUserData(target.getUniqueId());
            int minutes = userData.getInt("votefly");
            userData.set("votefly", minutes + JFLiterals.kVoteFlyDurationMinutes);
            JFFileController.saveUserData(userData, target.getUniqueId());
            try {
                plugin.getGMHandler().addUserSubgroup(target.getName(), "VoteFly", "world");
                plugin.getGMHandler().addUserSubgroup(target.getName(), "VoteFly", "survival");
            } catch (JFUnknownWorldException | JFUnknownPlayerException | JFUnknownGroupException e) {
                e.printStackTrace();
            }
            target.sendMessage(JFLiterals.kVoteflyActivated);
            sender.sendMessage(JFLiterals.voteFlyActivatedForTarget(target.getName()));
            return true;
        }
        
        return false;
    }
    
}
