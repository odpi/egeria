/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime;

public class InformationViewUncheckedExceptionBase extends RuntimeException{

    private int httpErrorCode;
    private String reportingClassName;
    private String reportedErrorMessage;
    private String reportedSystemAction;
    private String reportedUserAction;
    private Throwable reportedCaughtException ;

    public InformationViewUncheckedExceptionBase(
            String className,
            String errorMessage,
            String systemAction,
            String userAction,
            Throwable caughtError) {
        super(errorMessage, caughtError);
        this.reportingClassName = className;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = caughtError;
    }

    public InformationViewUncheckedExceptionBase(int httpErrorCode,
                                                 String reportingClassName,
                                                 String reportedErrorMessage,
                                                 String reportedSystemAction,
                                                 String reportedUserAction,
                                                 Throwable reportedCaughtException) {
        this.httpErrorCode = httpErrorCode;
        this.reportingClassName = reportingClassName;
        this.reportedErrorMessage = reportedErrorMessage;
        this.reportedSystemAction = reportedSystemAction;
        this.reportedUserAction = reportedUserAction;
        this.reportedCaughtException = reportedCaughtException;
    }

    public String getReportingClassName() {
        return reportingClassName;
    }

    public void setReportingClassName(String reportingClassName) {
        this.reportingClassName = reportingClassName;
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

    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    public void setHttpErrorCode(int httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
    }

}
