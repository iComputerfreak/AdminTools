package de.jonasfrey.admintools.commands;

/*
 * Copyright jonasfrey.
 * Created on 10.07.17.
 */

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.JFLiterals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BallotCommand extends JFCommand {

    public BallotCommand(AdminTools plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        super.onCommand(sender, cmd, label, args);

        if (!(sender instanceof Player)) {
            plugin.getLogger().warning("This command has to be executed as a player");
            return true;
        }

        Player playerSender = (Player) sender;
        String topic = plugin.getConfig().getString("ballot.topic");

        if (args.length == 1) {
            // User commands
            if (args[0].equalsIgnoreCase("yes") || args[0].equalsIgnoreCase("no") || args[0].equalsIgnoreCase("topic")) {
                if (topic == null || topic.isEmpty()) {
                    sender.sendMessage(JFLiterals.kNoBallotRunning);
                    return true;
                }
                // Ballot running
                if (args[0].equalsIgnoreCase("topic")) {
                    sender.sendMessage(JFLiterals.kPrefix + "§2Topic: " + topic);
                } else {
                    String vote = args[0].toLowerCase();
                    List<String> players = plugin.getConfig().getStringList("ballot." + vote);
                    if (players.contains(playerSender.getUniqueId().toString())) {
                        sender.sendMessage(JFLiterals.kAlreadyVoted);
                        return true;
                    }
                    players.add(playerSender.getUniqueId().toString());
                    plugin.getConfig().set("ballot." + vote, players);
                    plugin.saveConfig();
                    sender.sendMessage(JFLiterals.kVoteSuccessful);
                }
                return true;
            }
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            if (!sender.hasPermission(cmd.getPermission() + ".mod")) {
                sender.sendMessage(JFLiterals.kNoPermissionMessage);
                return true;
            }

            // Reset topic
            plugin.getConfig().set("ballot.topic", "");
            plugin.getConfig().set("ballot.yes", new ArrayList<String>());
            plugin.getConfig().set("ballot.no", new ArrayList<String>());
            plugin.saveConfig();
            sender.sendMessage(JFLiterals.kBallotReset);
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            // Set topic
            if (!sender.hasPermission(cmd.getPermission() + ".mod")) {
                sender.sendMessage(JFLiterals.kNoPermissionMessage);
                return true;
            }

            plugin.getConfig().set("ballot.topic", args[1]);
            plugin.saveConfig();
            sender.sendMessage(JFLiterals.kNewTopicSet);
        }

        return false;
    }

}
