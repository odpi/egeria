/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.ffdc.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ConformanceSuiteRuntimeException is used for all logic errors detected by the Open Metadata Conformance Suite.
 * It is used in conjunction with the ConformanceSuiteErrorCode to provide first failure data capture for these errors.
 */
public class ConformanceSuiteRuntimeException extends RuntimeException
{
    private static final long    serialVersionUID = 1L;

    private int                 reportedHTTPCode;
    private String              reportingClassName;
    private String              reportingActionDescription;
    private String              reportedErrorMessage;
    private String              reportedSystemAction;
    private String              reportedUserAction;
    private Map<String, Object> relatedProperties;

    private Throwable           reportedCaughtException = null;


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode           http response code to use if this exception flows over a rest call
     * @param className          name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage       description of error
     * @param systemAction       actions of the system as a result of the error
     * @param userAction         instructions for correcting the error
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public ConformanceSuiteRuntimeException(int                 httpCode,
                                            String              className,
                                            String              actionDescription,
                                            String              errorMessage,
                                            String              systemAction,
                                            String              userAction,
                                            Map<String, Object> relatedProperties)
    {
        super(errorMessage);
        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.relatedProperties = relatedProperties;
    }


    /**
     * This is the constructor used for creating an exception when an unexpected error has been
     * caught.
     *
     * @param httpCode           http response code to use if this exception flows over a rest call
     * @param className          name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage       description of error
     * @param systemAction       actions of the system as a result of the error
     * @param userAction         instructions for correcting the error
     * @param caughtError        previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public ConformanceSuiteRuntimeException(int                 httpCode,
                                            String              className,
                                            String              actionDescription,
                                            String              errorMessage,
                                            String              systemAction,
                                            String              userAction,
                                            Throwable           caughtError,
                                            Map<String, Object> relatedProperties)
    {
        super(errorMessage, caughtError);
        this.reportedHTTPCode = httpCode;
        this.reportingClassName = className;
        this.reportingActionDescription = actionDescription;
        this.reportedErrorMessage = errorMessage;
        this.reportedSystemAction = systemAction;
        this.reportedUserAction = userAction;
        this.reportedCaughtException = caughtError;
        this.relatedProperties = relatedProperties;
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
    public Throwable getReportedCaughtException()
    {
        return reportedCaughtException;
    }


    /**
     * Return any additional properties that were added to the exception to aid diagnosis.
     *
     * @return property map
     */
    public Map<String, Object> getRelatedProperties()
    {
        if (relatedProperties == null)
        {
            return null;
        }
        else if (relatedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(relatedProperties);
        }
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "ConformanceSuiteRuntimeException{" +
                "reportedHTTPCode=" + reportedHTTPCode +
                ", reportingClassName='" + reportingClassName + '\'' +
                ", reportingActionDescription='" + reportingActionDescription + '\'' +
                ", reportedErrorMessage='" + reportedErrorMessage + '\'' +
                ", reportedSystemAction='" + reportedSystemAction + '\'' +
                ", reportedUserAction='" + reportedUserAction + '\'' +
                ", reportedCaughtException=" + reportedCaughtException +
                ", relatedProperties=" + relatedProperties +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof ConformanceSuiteRuntimeException))
        {
            return false;
        }
        ConformanceSuiteRuntimeException that = (ConformanceSuiteRuntimeException) objectToCompare;
        return getReportedHTTPCode() == that.getReportedHTTPCode() &&
                Objects.equals(getReportingClassName(), that.getReportingClassName()) &&
                Objects.equals(getReportingActionDescription(), that.getReportingActionDescription()) &&
                Objects.equals(getErrorMessage(), that.getErrorMessage()) &&
                Objects.equals(getReportedSystemAction(), that.getReportedSystemAction()) &&
                Objects.equals(getReportedUserAction(), that.getReportedUserAction()) &&
                Objects.equals(getRelatedProperties(), that.getRelatedProperties()) &&
                Objects.equals(getReportedCaughtException(), that.getReportedCaughtException());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getReportedHTTPCode(),
                            getReportingClassName(),
                            getReportingActionDescription(),
                            getErrorMessage(),
                            getReportedSystemAction(),
                            getReportedUserAction(),
                            getRelatedProperties(),
                            getReportedCaughtException());
    }


}
