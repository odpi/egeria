/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;


public class MalformedInputException extends RuntimeException {

    public MalformedInputException(String message, Exception e) {
        super(message, e);
    }
}
