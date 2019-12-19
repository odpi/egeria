/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.common.ffdc;

/**
 * The DependantServerNotAvailableException is thrown by the UI when a dependant server is not available. The depenant server
 * is either sa metadata server or a governance server.
 */
public class DependantServerNotAvailableException extends UICheckedExceptionBase
{
    public DependantServerNotAvailableException(int httpCode, String className, String actionDescription, String errorMessage, String systemAction, String userAction) {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }
}
