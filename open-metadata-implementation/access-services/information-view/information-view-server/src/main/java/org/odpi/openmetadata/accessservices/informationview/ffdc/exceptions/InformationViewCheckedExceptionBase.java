/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions;



public class InformationViewCheckedExceptionBase extends Exception {

    private String reportingClassName;
    private String reportingActionDescription;
    private String reportedErrorMessage;
    private String reportedSystemAction;
    private String reportedUserAction;
    private Throwable reportedCaughtException = null;

    /**
     * @param reportingClassName         - name of class reporting error
     * @param reportingActionDescription - description of function it was performing when error detected
     * @param reportedErrorMessage       - description of error
     * @param reportedSystemAction       - actions of the system as a result of the error
     * @param reportedUserAction         - instructions for correcting the error
     */
    public InformationViewCheckedExceptionBase(String reportingClassName, String reportingActionDescription, String reportedErrorMessage, String reportedSystemAction, String reportedUserAction) {
        super(reportedErrorMessage);
        this.reportingClassName = reportingClassName;
        this.reportingActionDescription = reportingActionDescription;
        this.reportedErrorMessage = reportedErrorMessage;
        this.reportedSystemAction = reportedSystemAction;
        this.reportedUserAction = reportedUserAction;
    }

    /**
     * This is the  constructor used for creating a InformationViewCheckedException that resulted from a previous error.
     *
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     * @param caughtError       - the error that resulted in this exception.
     */
    public InformationViewCheckedExceptionBase(
            String className,
            String actionDescription,
            String errorMessage,
            String systemAction,
            String userAction,
            Throwable caughtError) {
        super(errorMessage, caughtError);
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = caughtError;
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
