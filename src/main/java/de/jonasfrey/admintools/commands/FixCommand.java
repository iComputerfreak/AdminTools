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
            plugin.getLogger().warning(JFLiterals.kHasToBeExecutedAsPlayer);
            return true;
        }

        Player playerSender = (Player) sender;

        if (args.length != 1) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "hand":
                return fixHand(playerSender, args, cmd);
            case "all":
                return fixAll(playerSender, args, cmd);
        }
        
        return false;
    }

    private boolean fixHand(Player sender, String[] args, Command cmd) {
        /* Reference to item in hand */
        ItemStack item = sender.getInventory().getItemInMainHand();
        if (item != null) {
            if (!repairItem(item)) {
                sender.sendMessage(JFLiterals.kNotRepairable);
                return true;
            }
            sender.sendMessage(JFLiterals.kHandItemFixed);
        } else {
            sender.sendMessage(JFLiterals.kNoItemInHand);
        }
        
        return true;
    }

    private boolean fixAll(Player sender, String[] args, Command cmd) {
        // Repair all
        for (ItemStack item : sender.getInventory().getContents()) {
            repairItem(item);
        }
        for (ItemStack item : sender.getInventory().getArmorContents()) {
            repairItem(item);
        }

        sender.sendMessage(JFLiterals.kAllItemsFixed);
        
        return true;
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
