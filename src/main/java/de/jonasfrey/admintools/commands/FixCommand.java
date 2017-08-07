package de.jonasfrey.admintools.commands;

/*
 * Copyright jonasfrey.
 * Created on 10.07.17.
 */

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import de.jonasfrey.admintools.JFUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FixCommand extends JFCommand {
    
    public FixCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        if (!(sender instanceof Player)) {
            plugin.getLogger().warning("This command has to be executed as a player");
            return true;
        }

        Player playerSender = (Player) sender;
        
        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("all"))) {
            if (args.length == 0) {
                // Repair hand
                /* Reference to item in hand */
                ItemStack item = playerSender.getInventory().getItemInHand();
                if (item != null) {
                    if (!repairItem(item)) {
                        sender.sendMessage(JFLiterals.kNotRepairable);
                        return true;
                    }
                    sender.sendMessage(JFLiterals.kHandItemFixed);
                }
                return true;
            } else {
                // Repair all
                for (ItemStack item : playerSender.getInventory().getContents()) {
                    repairItem(item);
                }
                for (ItemStack item : playerSender.getInventory().getArmorContents()) {
                    repairItem(item);
                }

                sender.sendMessage(JFLiterals.kAllItemsFixed);
                return true;
            }
        }
        
        return false;
    }

    /**
     * Repairs the ItemStack on the given pointer if possible
     * @param item the pointer to the ItemStack
     */
    private boolean repairItem(ItemStack item) {
        if (item == null) return false;
        if (JFUtils.isRepairable(item.getType())) {
            item.setDurability((short) 0);
            return true;
        }
        return false;
    }

}
