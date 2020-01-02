/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc;


public class JanusConnectorException extends Exception {
    private String reportingClassName;
    private String reportingActionDescription;
    private String reportedErrorMessage;
    private String reportedSystemAction;
    private String reportedUserAction;
    private Throwable reportedCaughtException = null;


    public JanusConnectorException(String className,
                                   String actionDescription,
                                   String errorMessage,
                                   String systemAction,
                                   String userAction)
    {
        super(errorMessage);
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
    }

    public JanusConnectorException(int    httpCode,
                                   String className,
                                   String actionDescription,
                                   String errorMessage,
                                   String systemAction,
                                   String userAction,
                                   Throwable throwable)
    {
        super(errorMessage);
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = throwable;
    }

    public String getReportingClassName() {
        return reportingClassName;
    }

    public void setReportingClassName(String reportingClassName) {
        this.reportingClassName = reportingClassName;
    }

    public String getReportingActionDescription() {
        return reportingActionDescription;
    }

    public void setReportingActionDescription(String reportingActionDescription) {
        this.reportingActionDescription = reportingActionDescription;
    }

    public String getReportedErrorMessage() {
        return reportedErrorMessage;
    }

    public void setReportedErrorMessage(String reportedErrorMessage) {
        this.reportedErrorMessage = reportedErrorMessage;
    }

    public String getReportedSystemAction() {
        return reportedSystemAction;
    }

    public void setReportedSystemAction(String reportedSystemAction) {
        this.reportedSystemAction = reportedSystemAction;
    }

    public String getReportedUserAction() {
        return reportedUserAction;
    }

    public void setReportedUserAction(String reportedUserAction) {
        this.reportedUserAction = reportedUserAction;
    }

    public Throwable getReportedCaughtException() {
        return reportedCaughtException;
    }

    public void setReportedCaughtException(Throwable reportedCaughtException) {
        this.reportedCaughtException = reportedCaughtException;
    }
}