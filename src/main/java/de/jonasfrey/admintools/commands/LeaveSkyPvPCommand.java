package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Jonas Frey
 * @version 1.0, 17.03.18
 */

public class LeaveSkyPvPCommand extends JFCommand {
    
    public LeaveSkyPvPCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        if (!(sender instanceof Player)) {
            plugin.getLogger().warning("This command has to be executed as a player");
            return true;
        }

        Player p = (Player) sender;

        if (args.length != 0) {
            return false;
        }

        if (plugin.getUtils().playersWaitingForTeleport.containsKey(p)) {
            p.sendMessage(JFLiterals.kAlreadyWaitingForTeleport);
            return true;
        }
        int scheduler = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (plugin.getUtils().playersWaitingForTeleport.containsKey(p) && p.isOnline()) {
                    plugin.getUtils().execute("mv spawn " + p.getName());
                    p.sendMessage(JFLiterals.kTeleporting);
                    plugin.getUtils().playersWaitingForTeleport.remove(p);
                }
            }
        }, 20 * 5);
        p.sendMessage(JFLiterals.kTeleportingIn5Seconds);
        plugin.getUtils().playersWaitingForTeleport.put(p, scheduler);

        return false;
    }
}
