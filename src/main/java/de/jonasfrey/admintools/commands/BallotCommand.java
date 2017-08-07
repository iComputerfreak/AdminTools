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
        
        if (args.length == 0) {
            return false;
        }
        
        switch (args[0].toLowerCase()) {
            case "topic":
                return ballotTopic(playerSender, args, cmd);
            case "yes":
            case "no":
                return ballotVote(playerSender, args, cmd);
            case "set":
                return ballotSet(playerSender, args, cmd);
            case "reset":
                return ballotReset(playerSender, args, cmd);
            case "result":
                return ballotResult(playerSender, args, cmd);
        }
        
        return false;
    }

    private boolean ballotTopic(Player sender, String[] args, Command cmd) {
        
        if (args.length != 1) {
            return false;
        }
        
        String topic = plugin.getConfig().getString("ballot.topic");
        if (topic == null || topic.isEmpty()) {
            sender.sendMessage(JFLiterals.kNoBallotRunning);
        } else {
            sender.sendMessage(JFLiterals.kPrefix + "§2Topic: §a" + topic);
        }
        
        return true;
    }

    private boolean ballotVote(Player sender, String[] args, Command cmd) {
        
        if (args.length != 1) {
            return false;
        }
        
        String topic = plugin.getConfig().getString("ballot.topic");
        if (topic == null || topic.isEmpty()) {
            sender.sendMessage(JFLiterals.kNoBallotRunning);
            return true;
        }

        String vote = args[0].toLowerCase();
        List<String> yesVotes = plugin.getConfig().getStringList("ballot.yes");
        List<String> noVotes = plugin.getConfig().getStringList("ballot.no");
        String currentVote = null;
        if (yesVotes.contains(sender.getUniqueId().toString())) {
            currentVote = "yes";
        } else if (noVotes.contains(sender.getUniqueId().toString())) {
            currentVote = "no";
        }
        
        if (currentVote != null && vote.equals(currentVote)) {
            // Already voted for that
            sender.sendMessage(JFLiterals.kAlreadyVoted);
        } else {
            // Change vote?
            if (currentVote != null) {
                if (currentVote.equals("yes")) {
                    yesVotes.remove(sender.getUniqueId().toString());
                } else {
                    noVotes.remove(sender.getUniqueId().toString());
                }
            }
            // Set vote
            if (vote.equals("yes")) {
                yesVotes.add(sender.getUniqueId().toString());
            } else {
                noVotes.add(sender.getUniqueId().toString());
            }

            plugin.getConfig().set("ballot.yes", yesVotes);
            plugin.getConfig().set("ballot.no", noVotes);
            plugin.saveConfig();
            sender.sendMessage(JFLiterals.kVoteSuccessful);
        }
        
        return true;
    }

    private boolean ballotSet(Player sender, String[] args, Command cmd) {
        
        if (args.length < 2) {
            return false;
        }
        
        if (!sender.hasPermission(cmd.getPermission() + ".mod")) {
            sender.sendMessage(JFLiterals.kNoPermissionMessage);
            return true;
        }

        String[] topicParts = args.clone();
        topicParts[0] = "";
        String newTopic = String.join(" ", topicParts).trim();
        plugin.getConfig().set("ballot.topic", newTopic);
        plugin.saveConfig();
        sender.sendMessage(JFLiterals.kNewTopicSet);
        return true;
    }

    private boolean ballotReset(Player sender, String[] args, Command cmd) {

        if (args.length != 1) {
            return false;
        }
        
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
        return true;
    }
    
    private boolean ballotResult(Player sender, String[] args, Command cmd) {
        
        if (args.length != 1) {
            return false;
        }
        
        if (!sender.hasPermission(cmd.getPermission() + ".mod")) {
            sender.sendMessage(JFLiterals.kNoPermissionMessage);
            return true;
        }

        String topic = plugin.getConfig().getString("ballot.topic");
        if (topic == null || topic.isEmpty()) {
            sender.sendMessage(JFLiterals.kNoBallotRunning);
            return true;
        }

        List<String> yesVotes = plugin.getConfig().getStringList("ballot.yes");
        List<String> noVotes = plugin.getConfig().getStringList("ballot.yes");
        int yesVoteCount = (yesVotes == null ? 0 : yesVotes.size());
        int noVoteCount = (noVotes == null ? 0 : noVotes.size());
        sender.sendMessage(JFLiterals.voteResult(yesVoteCount, noVoteCount));
        return true;
    }

}
