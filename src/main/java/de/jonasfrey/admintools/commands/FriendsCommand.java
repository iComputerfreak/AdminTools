package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFFileController;
import de.jonasfrey.admintools.JFLiterals;
import de.jonasfrey.admintools.exceptions.JFUnknownPlayerException;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * @author Jonas Frey
 * @version 1.0, 17.03.18
 */

public class FriendsCommand extends JFCommand {
    public FriendsCommand(AdminTools plugin) {
        super(plugin);
    }
    
    // /friends < list | add | remove > [player]
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        if (!(sender instanceof Player)) {
            plugin.getLogger().warning(JFLiterals.kHasToBeExecutedAsPlayer);
            return true;
        }

        Player playerSender = (Player) sender;

        if (args.length < 1 || args.length > 2) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                return friendsList(playerSender, args, cmd);
            case "add":
                return friendsAdd(playerSender, args, cmd);
            case "remove":
                return friendsRemove(playerSender, args, cmd);
        }

        return false;
    }

    private boolean friendsList(Player playerSender, String[] args, Command cmd) {
        if (args.length != 1) {
            return false;
        }
        
        List<UUID> friends = JFFileController.getFriends(playerSender.getUniqueId());
        if (friends.isEmpty()) {
            playerSender.sendMessage(JFLiterals.kNoFriends);
            return true;
        }
        
        String friendList = "";
        for (UUID friend : friends) {
            OfflinePlayer op = plugin.getServer().getOfflinePlayer(friend);
            if (op == null) {
                friendList += ", " + friend.toString();
            } else {
                friendList += ", " + op.getName();
            }
        }
        
        friendList = friendList.replaceFirst(", ", "");
        playerSender.sendMessage(JFLiterals.kFriendsListPrefix + friendList);
        
        return true;
    }

    private boolean friendsAdd(Player playerSender, String[] args, Command cmd) {
        if (args.length != 2) {
            return false;
        }

        OfflinePlayer target;
        try {
            target = plugin.getUtils().getOfflinePlayer(args[1]);
        } catch (JFUnknownPlayerException e) {
            playerSender.sendMessage(JFLiterals.playerDoesNotExist(e.getPlayerName()));
            return true;
        }
        
        List<UUID> friends = JFFileController.getFriends(playerSender.getUniqueId());
        if (friends.contains(target.getUniqueId())) {
            playerSender.sendMessage(JFLiterals.playerIsAlreadyFriend(target.getName()));
            return true;
        }
        
        JFFileController.addFriend(playerSender.getUniqueId(), target.getUniqueId());
        playerSender.sendMessage(JFLiterals.friendAdded(target.getName()));
        
        return true;
    }

    private boolean friendsRemove(Player playerSender, String[] args, Command cmd) {
        if (args.length != 2) {
            return false;
        }
        
        OfflinePlayer target;
        try {
            target = plugin.getUtils().getOfflinePlayer(args[1]);
        } catch (JFUnknownPlayerException e) {
            playerSender.sendMessage(JFLiterals.playerDoesNotExist(e.getPlayerName()));
            return true;
        }
        
        List<UUID> friends = JFFileController.getFriends(playerSender.getUniqueId());
        
        if (friends.contains(target.getUniqueId())) {
            JFFileController.removeFriend(playerSender.getUniqueId(), target.getUniqueId());
            playerSender.sendMessage(JFLiterals.friendRemoved(target.getName()));
        } else {
            playerSender.sendMessage(JFLiterals.notAFriend(target.getName()));
        }
        
        return true;
    }
}
