package de.jonasfrey.admintools;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
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

    private static File getUserDataFile(UUID uuid) {
        return new File("plugins/AdminTools/userdata/" + uuid.toString() + ".yml");
    }
    
}
