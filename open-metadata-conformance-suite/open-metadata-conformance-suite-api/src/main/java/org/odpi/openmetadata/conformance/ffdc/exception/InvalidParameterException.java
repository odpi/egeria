/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.ffdc.exception;

import java.util.Objects;

/**
 * The InvalidParameterException is thrown by the conformance suite when a parameter is null or an invalid value.
 */
public class InvalidParameterException extends ConformanceSuiteCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    private   String  parameterName;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param parameterName name of the invalid parameter if known
     */
    public InvalidParameterException(int    httpCode,
                                     String className,
                                     String actionDescription,
                                     String errorMessage,
                                     String systemAction,
                                     String userAction,
                                     String parameterName)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.parameterName = parameterName;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode  http response code to use if this exception flows over a rest call
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     * @param caughtError the error that resulted in this exception.
     * @param parameterName name of the invalid parameter if known
     */
    public InvalidParameterException(int       httpCode,
                                     String    className,
                                     String    actionDescription,
                                     String    errorMessage,
                                     String    systemAction,
                                     String    userAction,
                                     Throwable caughtError,
                                     String    parameterName)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.parameterName = parameterName;
    }


    /**
     * Return the invalid parameter's name, if known.
     *
     * @return string name
     */
    public String getParameterName()
    {
        return parameterName;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "InvalidParameterException{" +
                "parameterName='" + parameterName + '\'' +
                ", reportedHTTPCode=" + getReportedHTTPCode() +
                ", reportingClassName='" + getReportingClassName() + '\'' +
                ", reportingActionDescription='" + getReportingActionDescription() + '\'' +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
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
        if (!(objectToCompare instanceof InvalidParameterException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        InvalidParameterException that = (InvalidParameterException) objectToCompare;
        return Objects.equals(getParameterName(), that.getParameterName());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getParameterName());
    }
}
