package de.jonasfrey.admintools;

/**
 * @author Jonas Frey
 * @version 1.0, 08.08.17
 */

public enum SpecialChatType {
    ADMIN_CHAT("admintools.adminchat", "AC", "§4"),
    TEAM_CHAT("admintools.teamchat", "TC", "§2"),
    PRIVATE_CHAT("admintools.privatechat", "PC", "§8");

    private String permission;
    private String abbreviation;
    private String color;
    
    SpecialChatType(String permission, String abbr, String color) {
        this.permission = permission;
        this.abbreviation = abbr;
        this.color = color;
    }

    public String getPermission() {
        return permission;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
    
    public String getPrefix(String playerName) {
        return color + "§l" + "[" + abbreviation + "] §3" + playerName + "§7: " + this.color;
    }
    
}
