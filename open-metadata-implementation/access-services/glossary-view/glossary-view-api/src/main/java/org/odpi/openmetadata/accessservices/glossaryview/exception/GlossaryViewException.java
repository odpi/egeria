/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.exception;


/**
 * Provides a checked exception for reporting errors found when using the Glossary View OMAS services.
 * Typically, these errors are either configuration or operational errors that can be fixed by an administrator.
 * The {@link GlossaryViewErrorCode} can be used with this exception to populate it with standard messages.
 * The aim is to be able to uniquely identify the cause and remedy for the error.
 */
public class GlossaryViewException extends Exception {

    /*
     * These default values are only seen if this exception is initialized using one of its superclass constructors.
     */
    private int reportedHTTPCode;
    private String reportingClassName;
    private String reportingActionDescription;
    private String reportedErrorMessage;
    private String reportedSystemAction;
    private String reportedUserAction;
    private Throwable reportedCaughtException;

    /**
     * This is the typical constructor used for creating a GlossaryViewException.
     *
     * @param httpCode          http response code to use if this exception flows over a rest call
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage      description of error
     * @param systemAction      actions of the system as a result of the error
     * @param userAction        instructions for correcting the error
     */
    GlossaryViewException(int httpCode, String className, String actionDescription, String errorMessage,
                          String systemAction, String userAction) {
        super(errorMessage);
        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
    }


    /**
     * This is the  constructor used for creating a GlossaryViewException that resulted from a previous error.
     *
     * @param httpCode          http response code to use if this exception flows over a rest call
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage      description of error
     * @param systemAction      actions of the system as a result of the error
     * @param userAction        instructions for correcting the error
     * @param caughtError       the error that resulted in this exception.
     */
    GlossaryViewException(int httpCode, String className, String actionDescription, String errorMessage,
                          String systemAction, String userAction, Throwable caughtError) {
        super(errorMessage, caughtError);
        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = caughtError;
    }

    public int getReportedHTTPCode() {
        return reportedHTTPCode;
    }

    public String getReportingClassName() {
        return reportingClassName;
    }

    public String getReportingActionDescription() {
        return reportingActionDescription;
    }

    public String getReportedErrorMessage() {
        return reportedErrorMessage;
    }

    public String getReportedSystemAction() {
        return reportedSystemAction;
    }

    public String getReportedUserAction() {
        return reportedUserAction;
    }

    public Throwable getReportedCaughtException() {
        return reportedCaughtException;
    }

}
