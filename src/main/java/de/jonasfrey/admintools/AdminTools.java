package de.jonasfrey.admintools;

/*
 * Copyright jonasfrey.
 * Created on 10.07.17.
 */

import de.jonasfrey.admintools.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class AdminTools extends JavaPlugin {
    
    private boolean debugMode;
    public JFUtils utils;
    
    public AdminTools() {
        debugMode = getConfig().getBoolean("debug");
        this.utils = new JFUtils(this);
    }

    @Override
    public void onEnable() {
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
        /*getCommand("playtime").setExecutor(new JFCommand(this));
        getCommand("colors").setExecutor(new JFCommand(this));
        getCommand("adminchat").setExecutor(new JFCommand(this));
        getCommand("teamchat").setExecutor(new JFCommand(this));
        getCommand("privatechat").setExecutor(new JFCommand(this));
        getCommand("kills").setExecutor(new JFCommand(this));
        getCommand("spy").setExecutor(new JFCommand(this));
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
    }
    
    private void secondTimer() {
        // Refresh the Scoreboard
        utils.updateTabColors();
        utils.updateScoreboards();
    }
}
