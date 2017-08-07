package de.jonasfrey.admintools.exceptions;

/**
 * @author Jonas Frey
 * @version 1.0, 07.08.17
 */

public class JFUnknownPlayerException extends JFException {
    
    private String playerName;
    
    public JFUnknownPlayerException(String playerName) {
        super("Unknown Player: '" + playerName + "'");
        this.playerName = playerName;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
}
