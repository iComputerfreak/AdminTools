package de.jonasfrey.admintools;

/**
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

    public static final int kVoteFlyDurationMinutes = 2;
    public static String kWrongTimeFormat = kPrefixBad + "Wrong time format. Please use <hours>:<minutes>";
    public static String kNotSpying = kPrefixBad + "You are not spying at anyone right now.";
    public static String kAlreadySpying = kPrefixBad + "You are already spying on someone.";
    public static String kAlreadyWaitingForTeleport = kPrefixBad + "You are already waiting for a teleport.";
    public static String kTeleportingIn5Seconds = kPrefixGood + "You are being teleported in 5 seconds. Don't move!";
    public static String kTeleporting = kPrefixGood + "Teleporting...";

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
}
