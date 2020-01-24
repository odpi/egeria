/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.exceptions;

import java.util.Map;

/**
 * The InvalidParameterException is thrown by the OMAG Service when a parameter is null or an invalid value.
 */
public class InvalidParameterException extends org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException
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
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, parameterName);
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
     * @param parameterName name of the invalid parameter if known
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public InvalidParameterException(int                  httpCode,
                                     String               className,
                                     String               actionDescription,
                                     String               errorMessage,
                                     String               systemAction,
                                     String               userAction,
                                     String               parameterName,
                                     Map<String, Object>  relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, parameterName, relatedProperties);
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
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError, parameterName);
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
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public InvalidParameterException(int                  httpCode,
                                     String               className,
                                     String               actionDescription,
                                     String               errorMessage,
                                     String               systemAction,
                                     String               userAction,
                                     Throwable            caughtError,
                                     String               parameterName,
                                     Map<String, Object>  relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError, parameterName, relatedProperties);
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param errorMessage associated error message
     * @param template   object to copy
     */
    public InvalidParameterException(String                                                                     errorMessage,
                                     org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException template)
    {
        super(errorMessage, template);
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param errorMessage associated error message
     * @param template   object to copy
     * @param parameterName name of parameter in error
     */
    public InvalidParameterException(String                                                                      errorMessage,
                                     org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException template,
                                     String                                                                      parameterName)
    {
        super(errorMessage, template, parameterName);
    }

}
