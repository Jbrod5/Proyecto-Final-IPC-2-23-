
package com.jbrod.joblink_api.app.exceptions;

/**
 *
 * @author Jorge
 */
public class InvalidInformationException extends Exception {

    /**
     * Creates a new instance of <code>InvalidInformationException</code> without detail message.
     */
    public InvalidInformationException() {
    }

    /**
     * Constructs an instance of <code>InvalidInformationException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidInformationException(String msg) {
        super(msg);
    }
}
