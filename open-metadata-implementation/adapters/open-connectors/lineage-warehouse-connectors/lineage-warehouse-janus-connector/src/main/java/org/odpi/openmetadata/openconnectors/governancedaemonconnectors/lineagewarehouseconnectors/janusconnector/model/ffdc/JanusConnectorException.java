/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model.ffdc;

import lombok.Getter;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model.JanusConnectorErrorCode;

@Getter
public class JanusConnectorException extends RuntimeException {
    private final String reportingClassName;
    private final String reportingActionDescription;
    private final String reportedErrorMessage;
    private final String reportedSystemAction;
    private final String reportedUserAction;

    public JanusConnectorException(String className, String actionDescription, String errorMessage, String systemAction,
                                   String userAction) {
        super(errorMessage);
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
    }

    public JanusConnectorException(String className, String methodName, JanusConnectorErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.reportingClassName = className;
        this.reportingActionDescription = methodName;
        this.reportedErrorMessage = errorCode.getErrorMessage();
        this.reportedSystemAction = errorCode.getSystemAction();
        this.reportedUserAction = errorCode.getUserAction();

    }

}