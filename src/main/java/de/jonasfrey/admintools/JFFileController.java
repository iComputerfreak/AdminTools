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
 */

public class JFFileController {

    public static YamlConfiguration getUserData(UUID uuid) {
        return YamlConfiguration.loadConfiguration(getUserDataFile(uuid));
    }

    public static void saveUserData(YamlConfiguration data, UUID uuid) {
        try {
            data.save(getUserDataFile(uuid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<UUID> getFriends(UUID uuid) {
        YamlConfiguration userData = getUserData(uuid);
        List<String> uuids = userData.getStringList("friends");
        ArrayList<UUID> friends = new ArrayList<>();
        for (String s : uuids) {
            friends.add(UUID.fromString(s));
        }
        
        return friends;
    }

    public static void addFriend(UUID sender, UUID newFriend) {
        YamlConfiguration userData = getUserData(sender);
        List<String> uuids = userData.getStringList("friends");
        uuids.add(newFriend.toString());
        userData.set("friends", uuids);
        saveUserData(userData, sender);
    }

    public static void removeFriend(UUID sender, UUID friend) {
        YamlConfiguration userData = getUserData(sender);
        List<String> uuids = userData.getStringList("friends");
        uuids.remove(friend.toString());
        userData.set("friends", uuids);
        saveUserData(userData, sender);
    }

    private static File getUserDataFile(UUID uuid) {
        return new File("plugins/AdminTools/userdata/" + uuid.toString() + ".yml");
    }
}
