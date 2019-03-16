package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

/**
 * @author Jonas Frey
 * @version 1.0, 20.03.18
 */

public class CheckEnchantmentCommand extends JFCommand {
    
    public CheckEnchantmentCommand(AdminTools plugin) {
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

        if (args.length != 0) {
            return false;
        }

        ItemStack itemInHand = playerSender.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            playerSender.sendMessage(JFLiterals.kNoItemInHand);
            return true;
        }
    
        NamespacedKey keyEnchanted = new NamespacedKey(plugin, "command-enchanted");
        NamespacedKey keyEnchanter = new NamespacedKey(plugin, "enchanter");
        NamespacedKey keyDateTime = new NamespacedKey(plugin, "date-time");
        ItemMeta itemMeta = itemInHand.getItemMeta();
        Short enchanted = itemMeta.getCustomTagContainer().getCustomTag(keyEnchanted, ItemTagType.SHORT);
        String enchanter = itemMeta.getCustomTagContainer().getCustomTag(keyEnchanter, ItemTagType.STRING);
        String dateTime = itemMeta.getCustomTagContainer().getCustomTag(keyDateTime, ItemTagType.STRING);
        
            if (enchanted != null && enchanted == 1) {
                playerSender.sendMessage(JFLiterals.itemIsEnchanted(enchanter, dateTime));
                return true;
            }
        
        playerSender.sendMessage(JFLiterals.kNotEnchanted);
        
        return true;
    }
}
