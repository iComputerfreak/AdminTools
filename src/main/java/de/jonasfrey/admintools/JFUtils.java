package de.jonasfrey.admintools;

import com.earth2me.essentials.Essentials;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.Group;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author Jonas Frey
 * @version 1.0, 11.07.17
 */

public class JFUtils {

    private AdminTools plugin;
    private Essentials essentialsPlugin;
    private GroupManager groupManagerPlugin;
    

    public JFUtils(AdminTools plugin) {
        this.plugin = plugin;
        PluginManager manager = plugin.getServer().getPluginManager();
        this.essentialsPlugin = (Essentials) manager.getPlugin("Essentials");
        this.groupManagerPlugin = (GroupManager) manager.getPlugin("GroupManager");
    }

    public YamlConfiguration getUserData(UUID uuid) {
        return YamlConfiguration.loadConfiguration(getUserDataFile(uuid));
    }

    public void saveUserData(YamlConfiguration data, UUID uuid) {
        try {
            data.save(getUserDataFile(uuid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getUserDataFile(UUID uuid) {
        return new File(plugin.getDataFolder() + "/userdata/" + uuid.toString() + ".yml");
    }

    public void addPlaytimeToOnlinePlayers() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            com.earth2me.essentials.User u = essentialsPlugin.getUser(p.getUniqueId());
            if (!u.isAfk()) {
                YamlConfiguration data = getUserData(p.getUniqueId());
                int playtime = data.getInt("playtime") + 1;
                data.set("playtime", playtime);
                saveUserData(data, p.getUniqueId());
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
                char colorChar = color.getChar();
                if (player.hasPermission("admintools.tabcolors." + colorChar)) {
                    player.setPlayerListName("§" + colorChar + p);
                }
            }
        }
    }

    public void updateScoreboards() {
        // TODO: Implement
    }

    public void reloadPlaytimeRanks() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            String group = essentialsPlugin.getUser(p.getUniqueId()).getGroup();
            YamlConfiguration data = getUserData(p.getUniqueId());
            int playtimeMin = data.getInt("playtime");
            if (playtimeMin >= 3000 && group.equalsIgnoreCase("SkyBeginner")) {
                execute("manuadd " + p.getName() + " SkyProfi world");
                plugin.getLogger().info(p.getName() + " promoted to SkyProfi.");
            } else if (playtimeMin >= 9000 && group.equalsIgnoreCase("SkyProfi")) {
                execute("manuadd " + p.getName() + " SkyExperte world");
                plugin.getLogger().info(p.getName() + " promoted to SkyExperte.");
            } else if (playtimeMin >= 18000 && group.equalsIgnoreCase("SkyExperte")) {
                execute("manuadd " + p.getName() + " SkySuchtie world");
                plugin.getLogger().info(p.getName() + " promoted to SkyExperte.");
            }
        }
    }
    
    public void updateVotefly() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            YamlConfiguration userData = getUserData(p.getUniqueId());
            int minutes = userData.getInt("votefly");
            if (minutes == 0) {
                // No votefly active
                continue;
            }
            if (minutes == 1) {
                // Remove votefly
                removeUserSubgroup(p.getName(), "VoteFly");
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
            saveUserData(userData, p.getUniqueId());
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
    
    /* *************************** */
    /*        GROUP MANAGER        */
    /* *************************** */
    
    public void addUserSubgroup(String playerName, String groupName) {
        addUserSubgroup(playerName, groupName, "world");
    }
    
    public void addUserSubgroup(String playerName, String groupName, String worldName) {
        OverloadedWorldHolder worldHolder = groupManagerPlugin.getWorldsHolder().getWorldData(worldName);
        if (worldHolder == null) {
            plugin.getLogger().warning("{addUserSubgroup} Unknown world: '" + worldName + "'");
            return;
        }
        Group g = worldHolder.getGroup(groupName);
        if (g == null) {
            plugin.getLogger().warning("{addUserSubgroup} Unknown group: '" + groupName + "'");
            return;
        }
        User u = worldHolder.getUser(playerName);
        if (u == null) {
            plugin.getLogger().warning("{addUserSubgroup} Could not find player: '" + playerName + "'");
            return;
        }
        u.addSubGroup(g);
    }

    public void removeUserSubgroup(String playerName, String groupName) {
        removeUserSubgroup(playerName, groupName, "world");
    }
    
    public void removeUserSubgroup(String playerName, String groupName, String worldName) {
        OverloadedWorldHolder worldHolder = groupManagerPlugin.getWorldsHolder().getWorldData(worldName);
        if (worldHolder == null) {
            plugin.getLogger().warning("{addUserSubgroup} Unknown world: '" + worldName + "'");
            return;
        }
        Group g = worldHolder.getGroup(groupName);
        if (g == null) {
            plugin.getLogger().warning("{addUserSubgroup} Unknown group: '" + groupName + "'");
            return;
        }
        User u = worldHolder.getUser(playerName);
        if (u == null) {
            plugin.getLogger().warning("{addUserSubgroup} Could not find player: '" + playerName + "'");
            return;
        }
        u.removeSubGroup(g);
    }
}
