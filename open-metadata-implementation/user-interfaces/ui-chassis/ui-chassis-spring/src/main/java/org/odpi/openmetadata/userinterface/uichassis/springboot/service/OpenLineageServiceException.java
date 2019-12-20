/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;

public class OpenLineageServiceException extends RuntimeException{

    public OpenLineageServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
