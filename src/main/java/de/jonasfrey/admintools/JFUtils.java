package de.jonasfrey.admintools;

import com.avaje.ebean.validation.NotNull;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;
import de.jonasfrey.admintools.exceptions.JFUnknownGroupException;
import de.jonasfrey.admintools.exceptions.JFUnknownPlayerException;
import de.jonasfrey.admintools.exceptions.JFUnknownWorldException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Jonas Frey
 * @version 1.0, 11.07.17
 *
 * Representa a utility class
 */
public class JFUtils {

    private AdminTools plugin;
    /** The map of players that have the special chat active. E.g. AdminChat, TeamChat or PersonalChat **/
    HashMap<SpecialChatType, ArrayList<Player>> specialChatPlayers;
    /** The list of players **/
    public HashMap<Player, OfflinePlayer> spyPlayers;
    public HashMap<Player, Integer> playersWaitingForTeleport;
    public boolean chatDisabled;
    
    public JFUtils(AdminTools plugin) {
        this.plugin = plugin;
        this.specialChatPlayers = new HashMap<>();
        this.spyPlayers = new HashMap<>();
        this.playersWaitingForTeleport = new HashMap<>();
        this.chatDisabled = false;
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
            // if a player has all permissions, he should get the red (4) color
            if (player.hasPermission("admintools.tabcolors.4")) {
                player.setPlayerListName("§4" + p);
            }
            
        }
    }

    public void updateScoreboards() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            updateScoreboard(p);
        }
    }
    
    /**
     * Updates the custom scoreboard for a specific player.
     * @param p The player
     */
    public void updateScoreboard(Player p) {
        
        YamlConfiguration userData = JFFileController.getUserData(p.getUniqueId());
        if (userData.getBoolean("scoreboard-disabled")) {
            return;
        }
        
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("GSN Scoreboard", "None");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("§6§oGolden Sky Server");

        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("You need to have VAULT plugin installed.");
            return;
        }
        
        ArrayList<String> lines = new ArrayList<>();

        if (p.hasPermission("admintools.adminchat")) {
            boolean adminChat = false;
            ArrayList<Player> players = specialChatPlayers.get(SpecialChatType.ADMIN_CHAT);
            if (players != null && players.contains(p)) {
                adminChat = true;
            }
            lines.add((adminChat ? "§a" : "§c") + "Admin Chat " + (adminChat ? "on" : "off"));
        }
        if (p.hasPermission("admintools.teamchat")) {
            boolean teamChat = false;
            ArrayList<Player> players = specialChatPlayers.get(SpecialChatType.TEAM_CHAT);
            if (players != null && players.contains(p)) {
                teamChat = true;
            }
            lines.add((teamChat ? "§a" : "§c") + "Team Chat " + (teamChat ? "on" : "off"));
        }

        // get balance of player
        double money = 0;
        try {
            money = Economy.getMoneyExact(p.getName()).doubleValue();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        }
        // Playtime
        lines.add("§3Playtime");
        lines.add("§b" + getTimeString(userData.getInt("playtime")));
        // Money
        lines.add("§2Money");
        lines.add(String.format("§a%.2f $", money));
        // Kills
        lines.add("§5Kills");
        lines.add("§d" + userData.getInt("kills"));
        
        // parse array list to scoreboard
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Score s = obj.getScore(line.length() < 17 ? line : "ERROR");
            s.setScore(lines.size() - i);
        }

        p.setScoreboard(board);
    }
    
    /**
     * Checks the playtime of all online players and promotes them if they reached a certain threshold.
     * @throws JFUnknownWorldException If no world with the name "world" exists
     * @throws JFUnknownPlayerException If the GroupManagerHandler encountered a problem resolving a username
     * @throws JFUnknownGroupException If one of the groups below does not exist
     */
    public void reloadPlaytimeRanks() throws JFUnknownWorldException, JFUnknownPlayerException, JFUnknownGroupException {
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
            if (playtimeMin >= 60000 && plugin.getGMHandler().getUserGroup(p.getName()).getName().equalsIgnoreCase("SkySuchtie")) {
                plugin.getGMHandler().setUserGroup(p.getName(), "SkyLegende");
            }
        }
    }
    
    /**
     * Updates the votefly permissions for all players on the server. Should be called exactly once per minute
     * @throws JFUnknownWorldException If no world named "world" or "survival" exists
     * @throws JFUnknownPlayerException If the GroupManagerHandler encountered an error resolving a player username
     * @throws JFUnknownGroupException If the group "VoteFly" does not exist
     */
    public void updateVotefly() throws JFUnknownWorldException, JFUnknownPlayerException, JFUnknownGroupException {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            YamlConfiguration userData = JFFileController.getUserData(p.getUniqueId());
            // Retrieve the amount of votefly minutes left
            int minutes = userData.getInt("votefly");
            if (minutes == 0) {
                // No votefly active
                continue;
            } else if (minutes == 1) {
                // Remove votefly
                plugin.getGMHandler().removeUserSubgroup(p.getName(), "VoteFly", "world");
                plugin.getGMHandler().removeUserSubgroup(p.getName(), "VoteFly", "survival");
                p.sendMessage(JFLiterals.kVoteFlyDeactivated);
                // If the player still has the permission to fly (e.g. is SkyVIP+), don't remove the flight
                if (!p.hasPermission("essentials.fly")) {
                    // Deactivate the flight mode in 10 seconds
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            p.setAllowFlight(false);
                            p.setFlying(false);
                        }
                    }, 20 * 10L);
                }
            }
            // Update and save the new votefly minutes
            minutes -= 1;
            userData.set("votefly", minutes);
            JFFileController.saveUserData(userData, p.getUniqueId());
        }
    }
    
    /**
     * Checks, if a Material can be repaired
     * @param m The material
     * @return Whether the material is repairable
     */
    public static boolean isRepairable(Material m) {
        ArrayList<Material> repairables = new ArrayList<>(Arrays.asList(
                Material.WOODEN_SWORD,
                Material.WOODEN_PICKAXE,
                Material.WOODEN_AXE,
                Material.WOODEN_SHOVEL,
                Material.WOODEN_HOE,
                Material.STONE_SWORD,
                Material.STONE_PICKAXE,
                Material.STONE_AXE,
                Material.STONE_SHOVEL,
                Material.STONE_HOE,
                Material.IRON_SWORD,
                Material.IRON_PICKAXE,
                Material.IRON_AXE,
                Material.IRON_SHOVEL,
                Material.IRON_HOE,
                Material.GOLDEN_SWORD,
                Material.GOLDEN_PICKAXE,
                Material.GOLDEN_AXE,
                Material.GOLDEN_SHOVEL,
                Material.GOLDEN_HOE,
                Material.DIAMOND_SWORD,
                Material.DIAMOND_PICKAXE,
                Material.DIAMOND_AXE,
                Material.DIAMOND_SHOVEL,
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
                Material.GOLDEN_HELMET,
                Material.GOLDEN_CHESTPLATE,
                Material.GOLDEN_LEGGINGS,
                Material.GOLDEN_BOOTS,
                Material.DIAMOND_HELMET,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_BOOTS,
                Material.FLINT_AND_STEEL
        ));
        return repairables.contains(m);
    }
    
    /**
     * Executes a command on console level
     * @param cmd The command string to execute (without /)
     */
    public void execute(String cmd) {
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);
    }
    
    /**
     * Returns a formatted time string in the format hh:mm
     * @param minutes The sum of minutes to format
     * @return A formatted string in the format "hh:mm hours"
     */
    public String getTimeString(int minutes) {
        int hours = minutes / 60;
        int newMinutes = minutes % 60;
        return String.format("%02d:%02d hours", hours, newMinutes);
    }
    
    /**
     * Find an OfflinePlayer by its username.
     * @param name The username
     * @return The OfflinePlayer instance
     * @throws JFUnknownPlayerException When there exists no offline player with the given username
     */
    public @NotNull OfflinePlayer getOfflinePlayer(String name) throws JFUnknownPlayerException {
        OfflinePlayer p = plugin.getServer().getOfflinePlayer(name);
        if (p == null) {
            throw new JFUnknownPlayerException(name);
        }
        return p;
    }
    
    /**
     * Find an OnlinePlayer by its username.
     * @param name The username
     * @return The OnlinePlayer instance
     * @throws JFUnknownPlayerException When no player with the given is currently online on the server
     */
    public Player getOnlinePlayer(String name) throws JFUnknownPlayerException {
        Player p = plugin.getServer().getPlayer(name);
        if (p == null) {
            throw new JFUnknownPlayerException(name);
        }
        return p;
    }
    
    /**
     * Sends a message into a special chat.
     * @param type The type of special chat
     * @param sender The sender of the message
     * @param message The message string
     */
    public void writeInSpecialChat(SpecialChatType type, Player sender, String message) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.hasPermission(type.getPermission())) {
                // Send the message to every player that has permissions to read the chat
                p.sendMessage(type.getPrefix(sender.getName()) + message);
            }
        }
    }
    
    /**
     * Enables the special chat for a specific player.
     * @param type The type of special chat to activate
     * @param sender The player to activate the chat for
     */
    public void enableSpecialChat(SpecialChatType type, Player sender) {
        ArrayList<Player> players = specialChatPlayers.get(type);
        if (players == null) {
            players = new ArrayList<>();
        }
        players.add(sender);
        // Add the player to specialChatPlayers, so every message they send from now on will be sent into the special chat.
        specialChatPlayers.put(type, players);
    }
}
