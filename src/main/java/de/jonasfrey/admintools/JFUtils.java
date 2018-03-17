package de.jonasfrey.admintools;

import com.avaje.ebean.validation.NotNull;
import de.jonasfrey.admintools.exceptions.JFUnknownPlayerException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Jonas Frey
 * @version 1.0, 11.07.17
 */

public class JFUtils {

    private AdminTools plugin;
    HashMap<SpecialChatType, ArrayList<Player>> specialChatPlayers;
    
    public JFUtils(AdminTools plugin) {
        this.plugin = plugin;
        this.specialChatPlayers = new HashMap<>();
    }

    public void addPlaytimeToOnlinePlayers() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            com.earth2me.essentials.User u = plugin.getEssentialsPlugin().getUser(p.getUniqueId());
            if (!u.isAfk()) {
                YamlConfiguration data = JFFileController.getUserData(p.getUniqueId());
                int playtime = data.getInt("playtime") + 1;
                data.set("playtime", playtime);
                JFFileController.saveUserData(data, p.getUniqueId());
            }
        }
    }

    public void updateTabColors() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            String p;
            if (player.getName().length() < 15) {
                p = player.getName();
            } else {
                p = player.getName().substring(0, 13);
            }

            for (ChatColor color : ChatColor.values()) {
                if (!color.isColor()) {
                    continue;
                }
                char colorChar = color.getChar();
                if (player.hasPermission("admintools.tabcolors." + colorChar)) {
                    player.setPlayerListName("§" + colorChar + p);
                }
            }
        }
    }

    public void updateScoreboards() {
        throw new NotImplementedException();
    }

    public void reloadPlaytimeRanks() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            YamlConfiguration data = JFFileController.getUserData(p.getUniqueId());
            int playtimeMin = data.getInt("playtime");
            
            if (playtimeMin >= 3000 && plugin.getGMHandler().getUserGroup(p.getName()).getName().equalsIgnoreCase("SkyBeginner")) {
                plugin.getGMHandler().setUserGroup(p.getName(), "SkyProfi");
            }
            if (playtimeMin >= 9000 && plugin.getGMHandler().getUserGroup(p.getName()).getName().equalsIgnoreCase("SkyProfi")) {
                plugin.getGMHandler().setUserGroup(p.getName(), "SkyExperte");
            }
            if (playtimeMin >= 18000 && plugin.getGMHandler().getUserGroup(p.getName()).getName().equalsIgnoreCase("SkyExperte")) {
                plugin.getGMHandler().setUserGroup(p.getName(), "SkySuchtie");
            }
        }
    }
    
    public void updateVotefly() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            YamlConfiguration userData = JFFileController.getUserData(p.getUniqueId());
            int minutes = userData.getInt("votefly");
            if (minutes == 0) {
                // No votefly active
                continue;
            }
            if (minutes == 1) {
                // Remove votefly
                plugin.getGMHandler().removeUserSubgroup(p.getName(), "VoteFly");
                p.sendMessage(JFLiterals.kVoteFlyDeactivated);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        p.setAllowFlight(false);
                        p.setFlying(false);
                    }
                }, 20 * 10L);
            }
            
            minutes -= 1;
            userData.set("votefly", minutes);
            JFFileController.saveUserData(userData, p.getUniqueId());
        }
    }

    public static boolean isRepairable(Material m) {
        ArrayList<Material> repairables = new ArrayList<>(Arrays.asList(
                Material.WOOD_SWORD,
                Material.WOOD_PICKAXE,
                Material.WOOD_AXE,
                Material.WOOD_SPADE,
                Material.WOOD_HOE,
                Material.STONE_SWORD,
                Material.STONE_PICKAXE,
                Material.STONE_AXE,
                Material.STONE_SPADE,
                Material.STONE_HOE,
                Material.IRON_SWORD,
                Material.IRON_PICKAXE,
                Material.IRON_AXE,
                Material.IRON_SPADE,
                Material.IRON_HOE,
                Material.GOLD_SWORD,
                Material.GOLD_PICKAXE,
                Material.GOLD_AXE,
                Material.GOLD_SPADE,
                Material.GOLD_HOE,
                Material.DIAMOND_SWORD,
                Material.DIAMOND_PICKAXE,
                Material.DIAMOND_AXE,
                Material.DIAMOND_SPADE,
                Material.DIAMOND_HOE,
                Material.FISHING_ROD,
                Material.ELYTRA,
                Material.ANVIL,
                Material.SHEARS,
                Material.BOW,
                Material.LEATHER_HELMET,
                Material.LEATHER_CHESTPLATE,
                Material.LEATHER_LEGGINGS,
                Material.LEATHER_BOOTS,
                Material.IRON_HELMET,
                Material.IRON_CHESTPLATE,
                Material.IRON_LEGGINGS,
                Material.IRON_BOOTS,
                Material.GOLD_HELMET,
                Material.GOLD_CHESTPLATE,
                Material.GOLD_LEGGINGS,
                Material.GOLD_BOOTS,
                Material.DIAMOND_HELMET,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_BOOTS,
                Material.FLINT_AND_STEEL
        ));
        return repairables.contains(m);
    }
    
    public void execute(String cmd) {
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);
    }

    public String getTimeString(int minutes) {
        int hours = minutes / 60;
        int newMinutes = minutes % 60;
        return String.format("%02d:%02d hours", hours, newMinutes);
    }
    
    public @NotNull OfflinePlayer getOfflinePlayer(String name) throws JFUnknownPlayerException {
        OfflinePlayer p = plugin.getServer().getOfflinePlayer(name);
        if (p == null) {
            throw new JFUnknownPlayerException(name);
        }
        return p;
    }
    
    public void writeInSpecialChat(SpecialChatType type, Player sender, String message) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.hasPermission(type.getPermission())) {
                p.sendMessage(type.getPrefix(sender.getName()) + message);
            }
        }
    }
    
    public void enableSpecialChat(SpecialChatType type, Player sender) {
        ArrayList<Player> players = specialChatPlayers.get(type);
        if (players == null) {
            players = new ArrayList<>();
        }
        players.add(sender);
        specialChatPlayers.put(type, players);
    }
}
