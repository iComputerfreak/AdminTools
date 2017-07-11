package de.jonasfrey.admintools;

/*
 * Copyright jonasfrey.
 * Created on 10.07.17.
 */

import de.jonasfrey.admintools.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminTools extends JavaPlugin {
    
    private boolean debugMode;
    
    public AdminTools() {
        debugMode = getConfig().getBoolean("debug");
    }

    @Override
    public void onEnable() {
        registerCommands();
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
        /*getCommand("rainbow").setExecutor(new JFCommand(this));
        getCommand("votefly").setExecutor(new JFCommand(this));
        getCommand("playtime").setExecutor(new JFCommand(this));
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
    
}
