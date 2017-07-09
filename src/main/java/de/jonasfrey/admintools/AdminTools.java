package de.jonasfrey.admintools;

/*
 * Copyright jonasfrey.
 * Created on 10.07.17.
 */

import de.jonasfrey.admintools.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminTools extends JavaPlugin {
    
    public boolean debugMode;
    
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
        getCommand("at").setExecutor(new AdminToolsCommand(this));
        getCommand("fakejoin").setExecutor(new FakeJoinCommand(this));
        getCommand("fakeleave").setExecutor(new FakeLeaveCommand(this));
    }
    
}
