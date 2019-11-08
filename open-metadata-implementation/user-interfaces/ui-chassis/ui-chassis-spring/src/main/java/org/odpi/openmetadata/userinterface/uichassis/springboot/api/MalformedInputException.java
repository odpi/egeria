/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;


public class MalformedInputException extends RuntimeException {

    /**
     *
     * @param message text to display
     * @param e raised parent exception
     */
    public MalformedInputException(String message, Exception e) {
        super(message, e);
    }
}
