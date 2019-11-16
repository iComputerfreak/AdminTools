package de.jonasfrey.admintools;

import com.avaje.ebean.validation.NotNull;
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
 *
 * Utility class to handle interactions with the GroupManager plugin.
 */
public class GroupManagerHandler {
    
    /** The instance of the AdminTools plugin **/
    private AdminTools plugin;
    /** The instance of the group manager plugin **/
    private GroupManager groupManagerPlugin;
    
    public GroupManagerHandler(AdminTools plugin) {
        this.plugin = plugin;
        this.groupManagerPlugin = (GroupManager) plugin.getServer().getPluginManager().getPlugin("GroupManager");
        if (groupManagerPlugin == null) {
            throw new RuntimeException("Plugin dependency not loaded: GroupManager");
        }
    }
    
    /**
     * Returns a GroupManager User instance of the player with the given username in the given world.
     * @param playerName The name of the player/user
     * @param worldName The name of the world of the user
     * @return The User instance of the given player
     * @throws JFUnknownWorldException If the given world name does not exist
     * @throws JFUnknownPlayerException If the given username does not exist
     */
    public @NotNull User getGMUser(String playerName, String worldName) throws JFUnknownWorldException, JFUnknownPlayerException {
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
    
    /**
     * Returns a GroupManager Group instance with the given name
     * @param groupName The name of the GroupManager group
     * @param worldName The name of the world
     * @return The Group instance with the given name in the given world
     * @throws JFUnknownWorldException If the given world name does not exist
     * @throws JFUnknownGroupException If the given group name does not exist
     */
    public @NotNull Group getGMGroup(String groupName, String worldName) throws JFUnknownWorldException, JFUnknownGroupException {
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
    
    /**
     * Returns the Group of the given player in the world "world".
     * @param playerName The username of the player
     * @return The Group instance of the given player in "world"
     * @throws JFUnknownWorldException If the given world name does not exist
     * @throws JFUnknownPlayerException If the given username does not exist
     */
    public Group getUserGroup(String playerName) throws JFUnknownWorldException, JFUnknownPlayerException {
        return getUserGroup(playerName, "world");
    }
    
    /**
     * Returns the Group of the given player in the given world.
     * @param playerName The username of the player
     * @param worldName THe name of the world
     * @return The Group of the given player in the given world
     * @throws JFUnknownWorldException If the given world name does not exist
     * @throws JFUnknownPlayerException If the given username does not exist
     */
    public Group getUserGroup(String playerName, String worldName) throws JFUnknownWorldException, JFUnknownPlayerException {
        return getGMUser(playerName, worldName).getGroup();
    }
    
    /**
     * Sets the user Group of the given player in the world "world".
     * @param playerName The username of the player
     * @param groupName The name of the group
     * @throws JFUnknownWorldException If the given world name does not exist
     * @throws JFUnknownPlayerException If the given username does not exist
     * @throws JFUnknownGroupException If the given group name does not exist
     */
    public void setUserGroup(String playerName, String groupName) throws JFUnknownWorldException, JFUnknownPlayerException, JFUnknownGroupException {
        setUserGroup(playerName, groupName, "world");
    }
    
    /**
     * Sets the user Group of the given player in the given world.
     * @param playerName The username of the player
     * @param groupName The name of the group
     * @param worldName The name of the world
     * @throws JFUnknownWorldException If the given world name does not exist
     * @throws JFUnknownGroupException If the given group name does not exist
     * @throws JFUnknownPlayerException If the given username does not exist
     */
    public void setUserGroup(String playerName, String groupName, String worldName) throws JFUnknownWorldException, JFUnknownGroupException, JFUnknownPlayerException {
        Group g = getGMGroup(groupName, worldName);
        User u = getGMUser(playerName, worldName);
        u.setGroup(g);
    }
    
    /**
     * Adds a subgroup to a given user in the world "world".
     * @param playerName The username of the player
     * @param groupName The name of the group
     * @throws JFUnknownWorldException If the given world name does not exist
     * @throws JFUnknownPlayerException If the given username does not exist
     * @throws JFUnknownGroupException If the given group name does not exist
     */
    public void addUserSubgroup(String playerName, String groupName) throws JFUnknownWorldException, JFUnknownPlayerException, JFUnknownGroupException {
        addUserSubgroup(playerName, groupName, "world");
    }
    
    /**
     * Adds a subgroup to a given user in the given world.
     * @param playerName The username of the player
     * @param groupName The name of the group
     * @param worldName The name of the world
     * @throws JFUnknownWorldException If the given world name does not exist
     * @throws JFUnknownPlayerException If the given username does not exist
     * @throws JFUnknownGroupException If the given group name does not exist
     */
    public void addUserSubgroup(String playerName, String groupName, String worldName) throws JFUnknownWorldException, JFUnknownGroupException, JFUnknownPlayerException {
        Group g = getGMGroup(groupName, worldName);
        User u = getGMUser(playerName, worldName);
        u.addSubGroup(g);
    }
    
    /**
     * Removes a given subgroup of a given User.
     * @param playerName The username of the player
     * @param groupName The name of the group
     * @throws JFUnknownWorldException If the given world name does not exist
     * @throws JFUnknownPlayerException If the given username does not exist
     * @throws JFUnknownGroupException If the given group name does not exist
     */
    public void removeUserSubgroup(String playerName, String groupName) throws JFUnknownWorldException, JFUnknownPlayerException, JFUnknownGroupException {
        removeUserSubgroup(playerName, groupName, "world");
    }
    
    /**
     * Removes a given subgroup of a given User.
     * @param playerName The username of the player
     * @param groupName The name of the group
     * @param worldName The name of the world
     * @throws JFUnknownWorldException If the given world name does not exist
     * @throws JFUnknownPlayerException If the given username does not exist
     * @throws JFUnknownGroupException If the given group name does not exist
     */
    public void removeUserSubgroup(String playerName, String groupName, String worldName) throws JFUnknownWorldException, JFUnknownGroupException, JFUnknownPlayerException {
        Group g = getGMGroup(groupName, worldName);
        User u = getGMUser(playerName, worldName);
        u.removeSubGroup(g);
    }
    
}
