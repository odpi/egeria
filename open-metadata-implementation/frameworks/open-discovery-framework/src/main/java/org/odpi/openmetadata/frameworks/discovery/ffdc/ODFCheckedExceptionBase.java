/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.ffdc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ODFCheckedExceptionBase provides a checked exception base for reporting errors found when using services built
 * around the Open Discovery Framework (ODF) interfaces.
 * 
 * Typically these errors are either configuration or operational errors that can be fixed by an administrator
 * or power user.  However, there may be the odd bug that surfaces here. The ODFErrorCode can be used with
 * this exception to populate it with standard messages.  Otherwise messages defined uniquely for a
 * discovery service implementation.  The aim is to be able to uniquely identify the cause
 * and remedy for the error.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class ODFCheckedExceptionBase extends Exception
{
    /*
     * These default values are only seen if this exception is initialized using one of its superclass constructors.
     */
    private int       reportedHTTPCode;
    private String    reportingClassName;
    private String    reportingActionDescription;
    private String    reportedErrorMessage;
    private String    reportedSystemAction;
    private String    reportedUserAction;
    private Throwable reportedCaughtException = null;



    /**
     * This is the typical constructor used for creating an ODFCheckedExceptionBase.
     *
     * @param httpCode   http response code to use if this exception flows over a REST call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     */
    public ODFCheckedExceptionBase(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
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
     * This is the constructor used for creating an ODFCheckedExceptionBase when an unexpected error has been caught.
     *
     * @param httpCode   http response code to use if this exception flows over a REST call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   previous error causing this exception
     */
    public ODFCheckedExceptionBase(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
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
     * The class that created this exception.
     *
     * @return reportingClassName
     */
    public String getReportingClassName()
    {
        return reportingClassName;
    }


    /**
     * The type of request that the class was performing when the condition occurred that resulted in this
     * exception.
     *
     * @return reportingActionDescription
     */
    public String getReportingActionDescription()
    {
        return reportingActionDescription;
    }


    /**
     * A formatted short description of the cause of the condition that resulted in this exception.
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


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof ODFCheckedExceptionBase))
        {
            return false;
        }
        ODFCheckedExceptionBase that = (ODFCheckedExceptionBase) objectToCompare;
        return getReportedHTTPCode() == that.getReportedHTTPCode() &&
                Objects.equals(getReportingClassName(), that.getReportingClassName()) &&
                Objects.equals(getReportingActionDescription(), that.getReportingActionDescription()) &&
                Objects.equals(reportedErrorMessage, that.reportedErrorMessage) &&
                Objects.equals(getReportedSystemAction(), that.getReportedSystemAction()) &&
                Objects.equals(getReportedUserAction(), that.getReportedUserAction()) &&
                Objects.equals(getReportedCaughtException(), that.getReportedCaughtException());
    }


    /**
     * Provide a common implementation of hashCode for all ODF Exception objects.
     *
     * @return integer hash code based on the values in the attributes
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getReportedHTTPCode(),
                            getReportingClassName(),
                            getReportingActionDescription(),
                            reportedErrorMessage,
                            getReportedSystemAction(),
                            getReportedUserAction(),
                            getReportedCaughtException());
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ODFCheckedExceptionBase{" +
                ", reportedHTTPCode=" + reportedHTTPCode +
                ", reportingClassName='" + reportingClassName + '\'' +
                ", reportingActionDescription='" + reportingActionDescription + '\'' +
                ", reportedErrorMessage='" + reportedErrorMessage + '\'' +
                ", reportedSystemAction='" + reportedSystemAction + '\'' +
                ", reportedUserAction='" + reportedUserAction + '\'' +
                ", reportedCaughtException=" + reportedCaughtException +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}