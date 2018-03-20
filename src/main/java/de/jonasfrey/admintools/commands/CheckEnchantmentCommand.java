package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemInHand);
        NBTTagCompound compound = nmsStack.getTag();
        if (compound != null) {
            boolean enchanted = compound.getBoolean("command-enchanted");
            String enchanter = compound.getString("enchanter");
            String datetime = compound.getString("date-time");
            if (enchanted) {
                playerSender.sendMessage(JFLiterals.itemIsEnchanted(enchanter, datetime));
                return true;
            }
        }
        
        playerSender.sendMessage(JFLiterals.kNotEnchanted);
        
        return true;
    }
}
