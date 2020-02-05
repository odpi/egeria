/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.ffdc.exception;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

import java.util.Map;

/**
 * OMRSCheckedExceptionBase provides a checked exception for reporting errors found when using the OMRS.
 * Typically these errors are either configuration or operational errors that can be fixed by an administrator
 * or power user.  However, there may be the odd bug that surfaces here. The OMRSErrorCode can be used with
 * this exception to populate it with standard messages.  Otherwise messages defined uniquely for a
 * ConnectorProvider/Connector implementation can be used.  The aim is to be able to uniquely identify the cause
 * and remedy for the error.
 */
public abstract class OMRSCheckedExceptionBase extends OCFCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating a OMRSCheckedExceptionBase.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     */
    public OMRSCheckedExceptionBase(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the typical constructor used for creating a OMRSCheckedExceptionBase.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public OMRSCheckedExceptionBase(int                 httpCode,
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
     * This is the constructor used for creating a OMRSCheckedExceptionBase when an unexpected error has been caught.
     *
     * @param httpCode  http response code to use if this exception flows over a rest call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param caughtError  previous error causing this exception
     */
    public OMRSCheckedExceptionBase(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }



    /**
     * This is the constructor used for creating a OMRSCheckedExceptionBase when an unexpected error has been caught.
     *
     * @param httpCode  http response code to use if this exception flows over a rest call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param caughtError  previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public OMRSCheckedExceptionBase(int  httpCode,
                                    String className,
                                    String  actionDescription,
                                    String errorMessage,
                                    String systemAction, String userAction, Throwable caughtError,
                                    Map<String, Object> relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError, relatedProperties);
    }
}
