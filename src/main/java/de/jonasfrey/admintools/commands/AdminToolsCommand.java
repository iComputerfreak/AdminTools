package de.jonasfrey.admintools.commands;

/*
 * Copyright jonasfrey.
 * Created on 10.07.17.
 */

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFFileController;
import de.jonasfrey.admintools.JFLiterals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class AdminToolsCommand extends JFCommand {

    public AdminToolsCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        super.onCommand(sender, cmd, s, args);

        if (!(sender instanceof Player)) {
            plugin.getLogger().warning(JFLiterals.kHasToBeExecutedAsPlayer);
            return true;
        }

        Player playerSender = (Player) sender;
        
        if (args.length != 1 || !args[0].equalsIgnoreCase("scoreboard")) {
            return false;
        }
        
        // toggle scoreboard
        YamlConfiguration userData = JFFileController.getUserData(playerSender.getUniqueId());
        boolean scoreboard = userData.getBoolean("scoreboard-disabled");
        userData.set("scoreboard-disabled", !scoreboard);
        JFFileController.saveUserData(userData, playerSender.getUniqueId());
        // if scoreboard was enabled
        if (scoreboard) {
            plugin.getUtils().updateScoreboard(playerSender);
        } else {
            playerSender.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
        
        playerSender.sendMessage(JFLiterals.scoreboardToggled(scoreboard));
        
        return true;
    }
}
