package de.jonasfrey.admintools;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Jonas Frey
 * @version 1.0, 11.07.17
 *
 * Manages file interactions.
 */
public class JFFileController {
    
    /**
     * Returns a configuration of the user data file.
     * @param uuid The UUID of the player
     * @return The user data configuration
     */
    public static YamlConfiguration getUserData(UUID uuid) {
        return YamlConfiguration.loadConfiguration(getUserDataFile(uuid));
    }
    
    /**
     * Saves the user data configuration to file
     * @param data The configuration data
     * @param uuid The UUID of the player
     */
    public static void saveUserData(YamlConfiguration data, UUID uuid) {
        try {
            data.save(getUserDataFile(uuid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the list of UUIDs of the friends of a given player.
     * @param uuid The player
     * @return The list of UUIDs of the friends of the given player
     */
    public static List<UUID> getFriends(UUID uuid) {
        YamlConfiguration userData = getUserData(uuid);
        List<String> uuids = userData.getStringList("friends");
        ArrayList<UUID> friends = new ArrayList<>();
        for (String s : uuids) {
            friends.add(UUID.fromString(s));
        }
        
        return friends;
    }
    
    /**
     * Adds a given player to the list of friends of a given player.
     * @param sender The player, whose friends list is being modified
     * @param newFriend The new friend of the sender
     */
    public static void addFriend(UUID sender, UUID newFriend) {
        YamlConfiguration userData = getUserData(sender);
        List<String> uuids = userData.getStringList("friends");
        uuids.add(newFriend.toString());
        userData.set("friends", uuids);
        saveUserData(userData, sender);
    }
    
    /**
     * Removes a friend from a given player
     * @param sender The player to remove the friend from
     * @param friend The UUID of the friend to remove
     */
    public static void removeFriend(UUID sender, UUID friend) {
        YamlConfiguration userData = getUserData(sender);
        List<String> uuids = userData.getStringList("friends");
        uuids.remove(friend.toString());
        userData.set("friends", uuids);
        saveUserData(userData, sender);
    }
    
    /**
     * Returns the file location of the user data file of a given player.
     * @param uuid The UUID of the player
     * @return The File instance pointing to the user data yml file
     */
    private static File getUserDataFile(UUID uuid) {
        return new File("plugins/AdminTools/userdata/" + uuid.toString() + ".yml");
    }
}
