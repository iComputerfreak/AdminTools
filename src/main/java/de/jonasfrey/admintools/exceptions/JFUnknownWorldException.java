package de.jonasfrey.admintools.exceptions;

/**
 * @author Jonas Frey
 * @version 1.0, 07.08.17
 */

public class JFUnknownWorldException extends JFException {
    
    public JFUnknownWorldException(String worldName) {
        super("Unknown world: '" + worldName + "'");
    }
    
}
