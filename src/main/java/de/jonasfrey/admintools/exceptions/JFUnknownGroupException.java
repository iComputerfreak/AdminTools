package de.jonasfrey.admintools.exceptions;

/**
 * @author Jonas Frey
 * @version 1.0, 07.08.17
 */

public class JFUnknownGroupException extends JFException {
    
    public JFUnknownGroupException(String groupName) {
        super("Unknown group: '" + groupName + "'");
    }
    
}
