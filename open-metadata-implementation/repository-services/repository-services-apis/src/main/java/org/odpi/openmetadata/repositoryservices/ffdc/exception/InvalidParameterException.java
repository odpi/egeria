/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.ffdc.exception;

/**
 * The InvalidParameterException is thrown by an OMRS Connector when the parameters passed to a repository
 * connector, or its accompanying metadata collection, are not valid.
 */
public class InvalidParameterException extends OMRSCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    private   String  parameterName = null;

    /**
     * This is the typical constructor used for creating a InvalidParameterException.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     */
    public InvalidParameterException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }

    /**
     * This is the typical constructor used for creating a InvalidParameterException.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param parameterName name of the invalid parameter if known
     */
    public InvalidParameterException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, String parameterName)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.parameterName = parameterName;
    }


    /**
     * This is the constructor used for creating a InvalidParameterException that resulted from a previous error.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param caughtError  the error that resulted in this exception.
     * */
    public InvalidParameterException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }

    /**
     * This is the constructor used for creating a InvalidParameterException that resulted from a previous error.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param parameterName name of the invalid parameter if known
     * @param caughtError  the error that resulted in this exception.
     */
    public InvalidParameterException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, String parameterName, Throwable caughtError)
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
                       ", relatedProperties=" + getRelatedProperties() +
                       '}';
    }
}
