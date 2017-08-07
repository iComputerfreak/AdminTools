package de.jonasfrey.admintools;

/*
 * Copyright jonasfrey.
 * Created on 10.07.17.
 */

public abstract class JFLiterals {
    
    public static final String kPrefix = "§6[AdminTools] ";
    public static final String kGood = "§2";
    public static final String kBad = "§c";
    public static final String kCmd = "§d";
    public static final String kPrefixGood = kPrefix + kGood;
    public static final String kPrefixBad = kPrefix + kBad;
    
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
            + "will be disabled in 10 seconds.";

    public static final int kVoteFlyDurationMinutes = 2;
}
