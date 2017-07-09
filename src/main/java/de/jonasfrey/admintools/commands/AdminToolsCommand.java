package de.jonasfrey.admintools.commands;

/*
 * Copyright jonasfrey.
 * Created on 10.07.17.
 */

import de.jonasfrey.admintools.AdminTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AdminToolsCommand extends JFCommand {

    public AdminToolsCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        super.onCommand(sender, cmd, s, args);
        
        return true;
    }
}
