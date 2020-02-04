/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.exceptions;

import java.util.Map;

/**
 * The PropertyServerException is thrown by the OMAG Service when it is not able to communicate with the
 * property server.
 */
public class PropertyServerException extends org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException
{
    private static final long    serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     */
    public PropertyServerException(int    httpCode,
                                   String className,
                                   String actionDescription,
                                   String errorMessage,
                                   String systemAction,
                                   String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param relatedProperties properties providing more information about the error
     */
    public PropertyServerException(int                 httpCode,
                                   String              className,
                                   String              actionDescription,
                                   String              errorMessage,
                                   String              systemAction,
                                   String              userAction,
                                   Map<String, Object> relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, relatedProperties);
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the error that resulted in this exception.
     */
    public PropertyServerException(int       httpCode,
                                   String    className,
                                   String    actionDescription,
                                   String    errorMessage,
                                   String    systemAction,
                                   String    userAction,
                                   Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the error that resulted in this exception.
     * @param relatedProperties properties providing more information about the error
     */
    public PropertyServerException(int                  httpCode,
                                   String               className,
                                   String               actionDescription,
                                   String               errorMessage,
                                   String               systemAction,
                                   String               userAction,
                                   Throwable            caughtError,
                                   Map<String, Object>  relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError, relatedProperties);
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param template   object to copy
     */
    public PropertyServerException(org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "PropertyServerException{" +
                "reportedHTTPCode=" + getReportedHTTPCode() +
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
