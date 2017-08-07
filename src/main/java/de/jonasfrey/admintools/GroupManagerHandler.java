package de.jonasfrey.admintools;

import com.avaje.ebean.validation.NotNull;
import de.jonasfrey.admintools.exceptions.JFException;
import de.jonasfrey.admintools.exceptions.JFUnknownGroupException;
import de.jonasfrey.admintools.exceptions.JFUnknownPlayerException;
import de.jonasfrey.admintools.exceptions.JFUnknownWorldException;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.Group;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;

/**
 * @author Jonas Frey
 * @version 1.0, 07.08.17
 */

public class GroupManagerHandler {
    
    private AdminTools plugin;
    private GroupManager groupManagerPlugin;
    
    public GroupManagerHandler(AdminTools plugin) {
        this.plugin = plugin;
        this.groupManagerPlugin = (GroupManager) plugin.getServer().getPluginManager().getPlugin("GroupManager");
        if (groupManagerPlugin == null) {
            throw new JFException("Plugin dependency not loaded: GroupManager");
        }
    }

    public @NotNull User getGMUser(String playerName, String worldName) {
        OverloadedWorldHolder worldHolder = groupManagerPlugin.getWorldsHolder().getWorldData(worldName);
        if (worldHolder == null) {
            throw new JFUnknownWorldException(worldName);
        }
        User u = worldHolder.getUser(playerName);
        if (u == null) {
            throw new JFUnknownPlayerException(playerName);
        }
        return u;
    }

    public @NotNull Group getGMGroup(String groupName, String worldName) {
        OverloadedWorldHolder worldHolder = groupManagerPlugin.getWorldsHolder().getWorldData(worldName);
        if (worldHolder == null) {
            throw new JFUnknownWorldException(worldName);
        }
        Group g = worldHolder.getGroup(groupName);
        if (g == null) {
            throw new JFUnknownGroupException(groupName);
        }
        return g;
    }
    
    public Group getUserGroup(String playerName) {
        return getUserGroup(playerName, "world");
    }

    public Group getUserGroup(String playerName, String worldName) {
        return getGMUser(playerName, worldName).getGroup();
    }
    
    public void setUserGroup(String playerName, String groupName) {
        setUserGroup(playerName, groupName, "world");
    }

    public void setUserGroup(String playerName, String groupName, String worldName) {
        Group g = getGMGroup(groupName, worldName);
        User u = getGMUser(playerName, worldName);
        u.setGroup(g);
    }

    public void addUserSubgroup(String playerName, String groupName) {
        addUserSubgroup(playerName, groupName, "world");
    }

    public void addUserSubgroup(String playerName, String groupName, String worldName) {
        Group g = getGMGroup(groupName, worldName);
        User u = getGMUser(playerName, worldName);
        u.addSubGroup(g);
    }

    public void removeUserSubgroup(String playerName, String groupName) {
        removeUserSubgroup(playerName, groupName, "world");
    }

    public void removeUserSubgroup(String playerName, String groupName, String worldName) {
        Group g = getGMGroup(groupName, worldName);
        User u = getGMUser(playerName, worldName);
        u.removeSubGroup(g);
    }
    
}
