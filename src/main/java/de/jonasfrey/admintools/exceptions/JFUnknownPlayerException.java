package de.jonasfrey.admintools.exceptions;

/**
 * @author Jonas Frey
 * @version 1.0, 07.08.17
 */

public class JFUnknownPlayerException extends JFException {
    
    public JFUnknownPlayerException(String playerName) {
        super("Unknown Player: '" + playerName + "'");
    }
    
}
