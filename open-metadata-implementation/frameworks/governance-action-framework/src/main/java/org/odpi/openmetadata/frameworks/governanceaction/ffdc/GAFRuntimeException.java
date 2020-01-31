/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.governanceaction.ffdc;

/**
 * The GAFRuntimeException is used for all runtime exceptions from GAF components.  The GAFErrorCode is used to
 * provide the first failure data capture for this exception.  Typically runtime exceptions are the result of
 * programming errors and so a developer is looking at their resolution.
 */
public class GAFRuntimeException extends RuntimeException
{
    private static final long    serialVersionUID = 1L;

    /*
     * These default values are only seen if this exception is initialized using one of its superclass constructors.
     */
    private int       reportedHTTPCode = 500;
    private String    reportingClassName = "<Unknown>";
    private String    reportingActionDescription = "<Unknown>";
    private String    reportedErrorMessage = "<Unknown>";
    private String    reportedSystemAction = "<Unknown>";
    private String    reportedUserAction = "<Unknown>";
    private Throwable reportedCaughtException = null;


    /**
     * This is the typical constructor used for creating a GAFRuntimeException.
     *
     * @param httpCode - http response code to use if this exception flows over a rest call
     * @param className - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage - description of error
     * @param systemAction - actions of the system as a result of the error
     * @param userAction - instructions for correcting the error
     */
    public GAFRuntimeException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(errorMessage);
        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
    }


    /**
     * This is the constructor used for creating a GAFRuntimeException that results from a previous error.
     *
     * @param httpCode - http response code to use if this exception flows over a rest call
     * @param className - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage - description of error
     * @param systemAction - actions of the system as a result of the error
     * @param userAction - instructions for correcting the error
     * @param caughtError - previous error
     */
    public GAFRuntimeException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(errorMessage, caughtError);
        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = caughtError;
    }


    /**
     * Return the HTTP response code to use with this exception.
     *
     * @return reportedHTTPCode
     */
    public int getReportedHTTPCode()
    {
        return reportedHTTPCode;
    }

    /**
     * The class that created the GAFRuntimeException.
     *
     * @return reportingClassName
     */
    public String getReportingClassName()
    {
        return reportingClassName;
    }


    /**
     * The type of request that the class was performing when the condition occurred that resulted in this
     * GAFRuntimeException.
     *
     * @return reportingActionDescription
     */
    public String getReportingActionDescription()
    {
        return reportingActionDescription;
    }


    /**
     * A formatted short description of the cause of the condition that resulted in this GAFRuntimeException.
     *
     * @return reportedErrorMessage
     */
    public String getErrorMessage()
    {
        return reportedErrorMessage;
    }


    /**
     * A description of the action that the system took as a result of the error condition.
     *
     * @return reportedSystemAction
     */
    public String getReportedSystemAction()
    {
        return reportedSystemAction;
    }


    /**
     * A description of the action necessary to correct the error.
     *
     * @return reportedUserAction
     */
    public String getReportedUserAction()
    {
        return reportedUserAction;
    }


    /**
     * An exception that was caught and wrapped by this exception.  If a null is returned, then this exception is
     * newly created and not the result of a previous exception.
     *
     * @return reportedCaughtException
     */
    public Throwable getReportedCaughtException() { return reportedCaughtException; }
}
