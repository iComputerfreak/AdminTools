package de.jonasfrey.admintools.exceptions;

/**
 * @author Jonas Frey
 * @version 1.0, 07.08.17
 */

public class JFException extends Exception {
    
    public JFException(String message) {
        super(message);
        //super("{" + Thread.currentThread().getStackTrace()[2].getClassName() + " | " + Thread.currentThread().getStackTrace()[2].getMethodName() + "}" + message);
    }
    
}
