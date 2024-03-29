package de.jonasfrey.admintools;

import com.earth2me.essentials.Essentials;
import de.jonasfrey.admintools.commands.*;
import de.jonasfrey.admintools.exceptions.JFUnknownGroupException;
import de.jonasfrey.admintools.exceptions.JFUnknownPlayerException;
import de.jonasfrey.admintools.exceptions.JFUnknownWorldException;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Jonas Frey
 * @version 1.0, 10.07.17
 *
 * The main class.
 */
public class AdminTools extends JavaPlugin implements Listener {
    
    /** Whether the server is in debug mode. **/
    private boolean debugMode;
    /** The utility class instance. **/
    private JFUtils utils;

    /** The instance of the EssentialsX Plugin **/
    private Essentials essentialsPlugin;
    /** The instance of the GroupManagerHandler **/
    private GroupManagerHandler gmHandler;
    
    /** The ID of the bukkit scheduler that runs every 10 seconds **/
    private int secondSchedulerID;
    /** The ID of the bukkit scheduler that runs every minute **/
    private int minuteSchedulerID;
    
    public AdminTools() {
        debugMode = getConfig().getBoolean("debug");
        this.utils = new JFUtils(this);
        this.gmHandler = new GroupManagerHandler(this);
        PluginManager manager = getServer().getPluginManager();
        this.essentialsPlugin = (Essentials) manager.getPlugin("Essentials");
        getLogger().info("v" + this.getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommands();
        registerTimers();
        
        this.getServer().getPluginManager().registerEvents(this, this);
        // TODO: REMOVE THIS WHEN REMOVING /retrieve
        //this.getServer().getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("BetterEnderChest"));
    }
    
    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(minuteSchedulerID);
        Bukkit.getScheduler().cancelTask(secondSchedulerID);
        getLogger().info("v" + this.getDescription().getVersion() + " disabled.");
    }
    
    
    private void registerCommands() {
        getCommand("admintools").setExecutor(new AdminToolsCommand(this));
        getCommand("fakejoin").setExecutor(new FakeJoinCommand(this));
        getCommand("fakeleave").setExecutor(new FakeLeaveCommand(this));
        getCommand("ballot").setExecutor(new BallotCommand(this));
        getCommand("fix").setExecutor(new FixCommand(this));
        getCommand("rainbow").setExecutor(new RainbowCommand(this));
        getCommand("votefly").setExecutor(new VoteFlyCommand(this));
        getCommand("playtime").setExecutor(new PlaytimeCommand(this));
        getCommand("colors").setExecutor(new ColorsCommand(this));
        getCommand("adminchat").setExecutor(new AdminChatCommand(this));
        getCommand("teamchat").setExecutor(new TeamChatCommand(this));
        getCommand("privatechat").setExecutor(new PrivateChatCommand(this));
        getCommand("kills").setExecutor(new KillsCommand(this));
        getCommand("spy").setExecutor(new SpyCommand(this));
        getCommand("leaveskypvp").setExecutor(new LeaveSkyPvPCommand(this));
        getCommand("muteall").setExecutor(new MuteAllCommand(this));
        getCommand("fastmessages").setExecutor(new FastMessagesCommand(this));
        getCommand("showmessage").setExecutor(new ShowMessageCommand(this));
        getCommand("showme").setExecutor(new ShowMeCommand(this));
        getCommand("helpopreply").setExecutor(new HelpOpReply(this));
        getCommand("friends").setExecutor(new FriendsCommand(this));
        getCommand("uuidforname").setExecutor(new UUIDForNameCommand(this));
        getCommand("checkenchantment").setExecutor(new CheckEnchantmentCommand(this));
    }
    
    private void registerTimers() {
        // Minute timer
        minuteSchedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                minuteTimer();
            }
        }, 20L, 60 * 20L);
        // 10-second timer
        secondSchedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                secondTimer();
            }
        }, 20L, 10 * 20L);
    }
    
    private void minuteTimer() {
        // Add playtime to all online players
        try {
            utils.updateVotefly();
            utils.reloadPlaytimeRanks();
            utils.addPlaytimeToOnlinePlayers();
        } catch (JFUnknownWorldException | JFUnknownPlayerException | JFUnknownGroupException e) {
            e.printStackTrace();
        }
    }
    
    private void secondTimer() {
        // Refresh the Scoreboard
        utils.updateTabColors();
        utils.updateScoreboards();
    }
    
    /* ******************* */
    /* Getters and Setters */
    /* ******************* */

    public JFUtils getUtils() {
        return utils;
    }

    public Essentials getEssentialsPlugin() {
        return essentialsPlugin;
    }

    public GroupManagerHandler getGMHandler() {
        return gmHandler;
    }

    /* ******************* */
    /* Events              */
    /* ******************* */
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        
        // Server muted?
        if (utils.chatDisabled && !p.hasPermission(JFLiterals.kPermissionMuteallExempt)) {
            e.setCancelled(true);
            p.sendMessage(JFLiterals.kChatDisabled);
            return;
        }
        
        // Write in special chat, if active
        for (SpecialChatType key : utils.specialChatPlayers.keySet()) {
            ArrayList<Player> players = utils.specialChatPlayers.get(key);
            if (players.contains(e.getPlayer())) {
                utils.writeInSpecialChat(key, e.getPlayer(), e.getMessage());
                e.setCancelled(true);
            }
        }
        
        // Add world prefix to chat message
        String world = p.getWorld().getName();
        String worldTag = world.substring(0, 1).toUpperCase() + world.substring(1);
        
        switch (world.toLowerCase()) {
            case "world":
            case "skyblockworld":
                worldTag = "SB";
                break;
            case "skypvp": 
            case "skypvp-2":
                worldTag = "PvP";
                break;
            case "send":
                worldTag = "End";
                break;
            case "snether":
                worldTag = "Nether";
                break;
        }
        
        worldTag = "§7{" + worldTag + "}";
        
        // Append the world tag to the message format
        e.setFormat(worldTag + " §r" + e.getFormat());

        // Caps filter
        if (e.getMessage().equals(e.getMessage().toUpperCase()) && !p.hasPermission(JFLiterals.kPermissionCapsExempt)) {
            if (e.getMessage().length() > 5) {
                e.setMessage(e.getMessage().toLowerCase());
            }
        }

        // Filter insults
        if (!p.hasPermission(JFLiterals.kPermissionFilterExempt)) {
            List<String> insults = getConfig().getStringList("filter");
            String message = e.getMessage();
            for (String insult : insults) {
                String censor = "";
                for (int i = 0; i < insult.length(); i++) {
                    censor += "*";
                }
                message = message.replaceAll(insult, censor);
            }
            e.setMessage(message);
        }
    }

    @EventHandler
    public void onPlayerJoined(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        
        // Set join message
        String msg = getConfig().getString("messages.join");
        msg = msg.replaceAll("%player%", p.getName());
        e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', msg));
        
        // Update the scoreboard
        utils.updateScoreboards();
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                // Update tab colors
                utils.updateTabColors();
                
                // Show online friends
                List<UUID> friends = JFFileController.getFriends(p.getUniqueId());
                String friendList = "";
                for (UUID friend : friends) {
                    OfflinePlayer op = getServer().getOfflinePlayer(friend);
                    if (op.isOnline()) {
                        friendList += ", " + op.getName();
                    }
                }
                friendList = friendList.replaceFirst(", ", "");
                if (friendList.equals("")) {
                    p.sendMessage(JFLiterals.kNoFriendsOnline);
                    return;
                }
                p.sendMessage(JFLiterals.onlineFriends(friendList));
            }
        }, 20L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        // Set custom leave message
        Player p = e.getPlayer();
        String msg = getConfig().getString("messages.leave");
        msg = msg.replace("%player%", p.getName());
        e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', msg));
        // Remove from teleport queue and spying list
        utils.spyPlayers.remove(p);
        utils.playersWaitingForTeleport.remove(p);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPerformedCommand(PlayerCommandPreprocessEvent e) {
        String messageParts[] = e.getMessage().split(" ");
        String command = messageParts[0];

        // Block commands that list plugins
        if (command.startsWith("/?") || command.startsWith("/bukkit:") || command.startsWith("/minecraft:")) {
            e.getPlayer().sendMessage(JFLiterals.kCommandBlocked);
            e.setCancelled(true);
        }

        // Show command to spying players
        if (utils.spyPlayers.containsValue(e.getPlayer())) {
            for (Player key : utils.spyPlayers.keySet()) {
                Player target = key;
                if (target != null && utils.spyPlayers.get(key).equals(e.getPlayer())) {
                    target.sendMessage("§6[§b" + e.getPlayer().getName() + "§6] §8§o" + e.getMessage());
                }
            }
        }
        
        // If the command is /enchant, tag the item with an NBT tag
        if (command.equalsIgnoreCase("/enchant") || command.equalsIgnoreCase("/essentials:enchant") || command.equalsIgnoreCase("/essentials:enchantment") || command.equalsIgnoreCase("/enchantment")) {
            // Enchanting item...
            if (!e.getPlayer().hasPermission("essentials.enchant")) {
                return;
            }
            if (messageParts.length == 1) {
                return;
            }

            ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();
            if (itemInHand == null || itemInHand.getType() == Material.AIR) {
                return;
            }
    
    
            NamespacedKey keyEnchanted = new NamespacedKey(this, "command-enchanted");
            NamespacedKey keyEnchanter = new NamespacedKey(this, "enchanter");
            NamespacedKey keyDateTime = new NamespacedKey(this, "date-time");
            ItemMeta itemMeta = itemInHand.getItemMeta();
            itemMeta.getCustomTagContainer().setCustomTag(keyEnchanted, ItemTagType.SHORT, (short) 1);
            itemMeta.getCustomTagContainer().setCustomTag(keyEnchanter, ItemTagType.STRING, e.getPlayer().getName());
            itemMeta.getCustomTagContainer().setCustomTag(keyDateTime, ItemTagType.STRING, new Date().toString());
            itemInHand.setItemMeta(itemMeta);
            
            e.getPlayer().getInventory().setItemInMainHand(itemInHand);
        }

    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        // Give a dragon egg to the killer of the dragon
        if (e.getEntityType().equals(EntityType.ENDER_DRAGON)) {
            ItemStack egg = new ItemStack(Material.DRAGON_EGG, 1);
            // Add egg to inventory
            if (e.getEntity().getKiller().getInventory().firstEmpty() != -1) {
                e.getEntity().getKiller().getInventory().addItem(egg);
            } else {
                // drop the item, if inv full
                Location l = e.getEntity().getKiller().getLocation();
                l.getWorld().dropItem(l, egg);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) throws JFUnknownWorldException, JFUnknownPlayerException, JFUnknownGroupException {
        // Add a kill to the kill stats and promote the player, if appropriate
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            // add kill
            YamlConfiguration userData = JFFileController.getUserData(killer.getUniqueId());
            int kills = userData.getInt("kills");
            kills += 1;
            userData.set("kills", kills);
            JFFileController.saveUserData(userData, killer.getUniqueId());

            String group = gmHandler.getUserGroup(killer.getName(), killer.getWorld().getName()).getName();
            
            if (kills >= 750 && group.equalsIgnoreCase("SkyKrieger")) {
                gmHandler.setUserGroup(killer.getName(), "SkyElite", "skypvp");
                getLogger().info(killer.getName() + " promoted to SkyElite.");
            } else if (kills >= 3000 && group.equalsIgnoreCase("SkyElite")) {
                gmHandler.setUserGroup(killer.getName(), "SkyGeneral", "skypvp");
                getLogger().info(killer.getName() + " promoted to SkyGeneral.");
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        // Skip movement inside the same block
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) {
            return;
        }
        
        // cancel teleport
        Integer id = utils.playersWaitingForTeleport.get(e.getPlayer());
        if (id != null) {
            utils.playersWaitingForTeleport.remove(e.getPlayer());
            Bukkit.getScheduler().cancelTask(id);
            e.getPlayer().sendMessage(JFLiterals.kTeleportAborted);
        }
    }
}
