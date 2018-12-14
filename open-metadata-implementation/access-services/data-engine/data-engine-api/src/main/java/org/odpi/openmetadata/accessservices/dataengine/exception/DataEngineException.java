/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.exception;

import lombok.Getter;

/**
 * DataEngineException provides a checked exception for reporting errors found when using
 * the Data Engine OMAS services.
 * Typically these errors are either configuration or operational errors that can be fixed by an administrator.
 * The DataEngineErrorCode can be used with this exception to populate it with standard messages.
 * The aim is to be able to uniquely identify the cause and remedy for the error.
 */
@Getter
public class DataEngineException extends Exception {
    /*
     * These default values are only seen if this exception is initialized using one of its superclass constructors.
     */
    private int reportedHTTPCode;
    private String reportingClassName;
    private String reportingActionDescription;
    private String reportedErrorMessage;
    private String reportedSystemAction;
    private String reportedUserAction;
    private Throwable reportedCaughtException = null;


    /**
     * This is the typical constructor used for creating a DataEngineException.
     *
     * @param httpCode          - http response code to use if this exception flows over a rest call
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     */
    DataEngineException(int httpCode, String className, String actionDescription, String errorMessage,
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
     * This is the  constructor used for creating a DataEngineException that resulted from a previous error.
     *
     * @param httpCode          - http response code to use if this exception flows over a rest call
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     * @param caughtError       - the error that resulted in this exception.
     */
    DataEngineException(int httpCode,
                        String className,
                        String actionDescription,
                        String errorMessage,
                        String systemAction,
                        String userAction,
                        Throwable caughtError) {
        super(errorMessage, caughtError);
        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = caughtError;
    }
}
