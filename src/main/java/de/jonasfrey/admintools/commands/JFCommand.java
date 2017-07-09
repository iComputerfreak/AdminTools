package de.jonasfrey.admintools.commands;

/*
 * Copyright jonasfrey.
 * Created on 10.07.17.
 */

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class JFCommand implements CommandExecutor {

    AdminTools plugin;

    public JFCommand(AdminTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(cmd.getPermission())) {
            sender.sendMessage(JFLiterals.kNoPermissionMessage);
            return true;
        }
        
        return true;
    }
}
