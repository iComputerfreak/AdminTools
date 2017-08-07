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
    
    public static final String kNoPermissionMessage = kPrefixBad + "You have no permissions to execute this command";
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
    public static final String kPlaytimeTopHeader = "§a----- §6Top Ten §a-----";

    public static final int kVoteFlyDurationMinutes = 2;
    public static String kWrongTimeFormat = kBad + "Wrong time format. Please use <hours>:<minutes>";

    public static String voteResult(int yesVotes, int noVotes) {
        return kPrefixSpecial + yesVotes + kGood + " Players voted yes and " + kSpecial + noVotes + kGood + " Players voted no.";
    }

    public static String playtimeMe(String timeString) {
        return kPrefixGood + "You have played here for " + kSpecial + timeString + kGood + " hours.";
    }

    public static String playtimeGet(String playerName, String timeString) {
        return kPrefixSpecial + playerName + kGood + " played here for " + kSpecial + timeString + kGood + " hours.";
    }

    public static String playtimeSet(String playerName, String timeString) {
        return kPrefixGood + "The playtime of " + kSpecial + playerName + kGood + " has been set to " + kSpecial + timeString + kGood + " hours.";
    }

    public static String playerDoesNotExist(String playerName) {
        return kPrefixBad + "The player " + kSpecial + playerName + kBad + " does not exist.";
    }

    public static String playtimeReset(String playerName, String previousPlaytime) {
        return kPrefixGood + "The playtime of " + kSpecial + playerName + kGood + " has been reset. The previous playtime was " + kSpecial + previousPlaytime + kGood + " hours.";
    }

    public static String playtimeTopLine(int place, String playerName, String timeString) {
        return "§e" + place + ". §a" + playerName + ": §2" + timeString;
    }
}
