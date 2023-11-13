/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package com.jbrod.joblink_api.app.exceptions;

/**
 *
 * @author Jorge
 */
public class NotFoundException extends Exception {

    /**
     * Creates a new instance of <code>NotFoundException</code> without detail message.
     */
    public NotFoundException() {
    }

    /**
     * Constructs an instance of <code>NotFoundException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NotFoundException(String msg) {
        super(msg);
    }
}
