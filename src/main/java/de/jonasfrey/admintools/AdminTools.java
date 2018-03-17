package de.jonasfrey.admintools;

import com.earth2me.essentials.Essentials;
import de.jonasfrey.admintools.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * @author Jonas Frey
 * @version 1.0, 10.07.17
 */

public class AdminTools extends JavaPlugin implements Listener {
    
    private boolean debugMode;
    private JFUtils utils;

    private Essentials essentialsPlugin;
    private GroupManagerHandler gmHandler;
    
    public AdminTools() {
        debugMode = getConfig().getBoolean("debug");
        this.utils = new JFUtils(this);
        PluginManager manager = getServer().getPluginManager();
        this.essentialsPlugin = (Essentials) manager.getPlugin("Essentials");
        this.gmHandler = new GroupManagerHandler(this);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommands();
        registerTimers();
    }
    
    @Override
    public void onDisable() {
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
        /*getCommand("spy").setExecutor(new JFCommand(this));
        getCommand("leaveskypvp").setExecutor(new JFCommand(this));
        getCommand("muteall").setExecutor(new JFCommand(this));
        getCommand("insultfilter").setExecutor(new JFCommand(this));
        getCommand("fastmessages").setExecutor(new JFCommand(this));
        getCommand("showmessage").setExecutor(new JFCommand(this));
        getCommand("helpopreply").setExecutor(new JFCommand(this));
        getCommand("friends").setExecutor(new JFCommand(this));
        getCommand("checkenchantment").setExecutor(new JFCommand(this));
        getCommand("uuidforname").setExecutor(new JFCommand(this));
        getCommand("clearchat").setExecutor(new JFCommand(this));*/
    }
    
    private void registerTimers() {
        // Minute timer
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                minuteTimer();
            }
        }, 20L, 60 * 20L);
        // 10-second timer
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                secondTimer();
            }
        }, 20L, 10 * 20L);
    }
    
    private void minuteTimer() {
        // Add playtime to all online players
        utils.addPlaytimeToOnlinePlayers();
        utils.updateVotefly();
        utils.reloadPlaytimeRanks();
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
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        for (SpecialChatType key : utils.specialChatPlayers.keySet()) {
            ArrayList<Player> players = utils.specialChatPlayers.get(key);
            if (players.contains(e.getPlayer())) {
                utils.writeInSpecialChat(key, e.getPlayer(), e.getMessage());
                e.setCancelled(true);
            }
        }
    }
}
