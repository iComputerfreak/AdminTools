package de.jonasfrey.admintools;

import java.util.UUID; /**
 * @author Jonas Frey
 * @version 1.0, 10.07.17
 */

public abstract class JFLiterals {
    
    public static final String kPrefix = "§6[AdminTools] ";
    public static final String kGood = "§2";
    public static final String kBad = "§c";
    public static final String kCmd = "§d";
    public static final String kSpecial = "§a";
    public static final String kPrefixGood = kPrefix + kGood;
    public static final String kPrefixBad = kPrefix + kBad;
    public static final String kPrefixSpecial = kPrefix + kSpecial;
    
    public static final String kHasToBeExecutedAsPlayer = "This command has to be executed as a player";

    public static final String kPermissionMuteallExempt = "admintools.muteall.exempt";
    public static final String kPermissionCapsExempt = "admintools.capsexempt";
    public static final String kPermissionFilterExempt = "admintools.filterexempt";

    public static final String kNoBallotRunning = kPrefixBad + "There is no ballot running at the moment.";
    public static final String kVoteSuccessful = kPrefixGood + "Your vote was successful.";
    public static final String kAlreadyVoted = kPrefixGood + "You have already voted for this option.";
    public static final String kBallotReset = kPrefixGood + "The ballot has been successfully reset.";
    public static final String kNewTopicSet = kPrefixGood + "The new topic has been successfully set.";
    public static final String kAllItemsFixed = kPrefixGood + "All items in your inventory have been fixed.";
    public static final String kHandItemFixed = kPrefixGood + "The item in your hand has been fixed.";
    public static final String kNotRepairable = kPrefixBad + "The item in your hand can not be repaired.";
    public static final String kVoteflyActivated = kPrefixGood + "You can now use " + kCmd + "/fly" + kGood + " for " + 
            JFLiterals.kVoteFlyDurationMinutes + " minutes";
    public static final String kVoteFlyDeactivated = kPrefixBad + "Your Vote Fly has been disabled. Your fly mode "
            + "will be disabled in " + kSpecial + "10 seconds.";
    public static final String kDataTopHeader = "§a----- §6Top Ten §a-----";

    public static final int kVoteFlyDurationMinutes = 10;
    public static final String kWrongTimeFormat = kPrefixBad + "Wrong time format. Please use <hours>:<minutes>";
    public static final String kNotSpying = kPrefixBad + "You are not spying at anyone right now.";
    public static final String kAlreadySpying = kPrefixBad + "You are already spying on someone.";
    public static final String kAlreadyWaitingForTeleport = kPrefixBad + "You are already waiting for a teleport.";
    public static final String kTeleportingIn5Seconds = kPrefixGood + "You are being teleported in 5 seconds. Don't move!";
    public static final String kTeleporting = kPrefixGood + "Teleporting...";
    public static final String kNoMessages = kPrefixGood + "There are no messages.";
    public static final String kMessagesHeader = "§6------------- §aMessages §6-------------";
    public static final String kNoMessageFound = kPrefixBad + "There is no message with this name.";
    public static final String kMessageShown = kPrefixGood + "The message has been shown.";
    public static final String kNoFriends = kPrefixGood + "You currently have no friends.";
    public static final String kFriendsListPrefix = kPrefixGood + "Friends: " + kSpecial;
    public static final String kNoItemInHand = kPrefixBad + "You don't have any item in your hand.";
    public static final String kChatDisabled = kPrefixBad + "The chat is currently disabled. You cannot talk now.";
    public static final String kNoFriendsOnline = kPrefixGood + "None of your friends are online right now.";
    public static final String kCommandBlocked = kPrefixBad + "This command has been blocked.";
    public static final String kTeleportAborted = kPrefixBad + "Teleport aborted!";
    public static final String kNotEnchanted = kPrefixGood + "The item was not enchanted with " + kCmd + "/enchant" + kGood + ".";

    public static String voteResult(int yesVotes, int noVotes) {
        return kPrefixSpecial + yesVotes + kGood + " Players voted yes and " + kSpecial + noVotes + kGood + " Players voted no.";
    }
    
    public static String dataMe(String type, String value) {
        return kPrefixGood + "Your " + type + ": " + kSpecial + value + kGood + ".";
    }

    public static String dataGet(String type, String playerName, String value) {
        return kPrefixSpecial + playerName + kGood + "'s " + type + ": " + kSpecial + value + kGood + ".";
    }

    public static String dataSet(String type, String playerName, String value) {
        return kPrefixSpecial + playerName + kGood + "'s " + type + " has been set to " + kSpecial + value + kGood + ".";
    }

    public static String dataReset(String type, String playerName, String oldValue) {
        return kPrefixSpecial + playerName + kGood + "'s " + type + " has been reset. The previous " + type + " was " + kSpecial + oldValue + kGood + ".";
    }

    public static String dataTopLine(int place, String playerName, String value) {
        return "§e" + place + ". §a" + playerName + ": §2" + value;
    }

    public static String playerDoesNotExist(String playerName) {
        return kPrefixBad + "The player " + kSpecial + playerName + kBad + " does not exist.";
    }

    public static String spyingAtPlayer(String name) {
        return kPrefixGood + "You are currently spying at " + kSpecial + name + kGood + ".";
    }

    public static String removedSpyingAtPlayer(String name) {
        return kPrefixGood + "You are not spying at " + kSpecial + name + kGood + " anymore.";
    }

    public static String nowSpying(String name) {
        return kPrefixGood + "You now see all commands performed by " + kSpecial + name + kGood + ".";
    }

    public static String chatUnMuted(boolean wasDisabled) {
        return kPrefix + (wasDisabled ? JFLiterals.kGood : JFLiterals.kBad) + "The server has been " + (wasDisabled ? "un" : "") + "muted!";
    }

    public static String kHelpOpReplyExternMessage(String sender, String target, String message) {
        return "§6[§4HelpOp Reply§6] [§c" + sender + "§6 -> §c" + target + "§6] §b" + message;
    }

    public static String kHelpOpReplyInternMessage(String sender, String message) {
        return "§6[§4HelpOp Reply§6] [§c" + sender + "§6] §b" + message;
    }

    public static String friendAdded(String name) {
        return kPrefixSpecial + name + kGood + " has been added to your friends.";
    }

    public static String friendRemoved(String name) {
        return kPrefixSpecial + name + kGood + " has been removed from your friends.";
    }

    public static String playerIsAlreadyFriend(String name) {
        return kPrefixSpecial + name + kGood + " is already your friend.";
    }

    public static String notAFriend(String name) {
        return kPrefixSpecial + name + kBad + " is not your friend.";
    }

    public static String uuidForName(String name, UUID uuid) {
        return kPrefixGood + "The UUID of " + kSpecial + name + kGood + " is " + kSpecial + uuid + kGood + ".";
    }

    public static String onlineFriends(String friendList) {
        return kPrefixGood + "Online friends: " + kSpecial + friendList;
    }

    public static String scoreboardToggled(boolean newState) {
        return kPrefixGood + "Your scoreboard is now " + (newState ? "§aenabled" : "§4disabled") + kGood + ".";
    }

    public static String voteFlyActivatedForTarget(String target) {
        return kPrefixSpecial + target + kGood + " can now use " + kCmd + "/fly" + kGood + " for " + 
        JFLiterals.kVoteFlyDurationMinutes + " minutes";
    }

    public static String itemIsEnchanted(String enchanter, String datetime) {
        return kPrefixGood + "The item was enchanted with " + kCmd + "/enchant" + kGood + " by " + kSpecial + enchanter + kGood + ". (Timestamp: " + kSpecial + datetime + kGood + ")";
    }
}
