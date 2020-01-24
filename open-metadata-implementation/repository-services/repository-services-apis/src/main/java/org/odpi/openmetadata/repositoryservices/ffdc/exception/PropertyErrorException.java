/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.ffdc.exception;

/**
 * The PropertyErrorException is thrown by an OMRS Connector when the properties defined for a specific entity
 * or relationship instance do not match the TypeDefs for the metadata collection.
 */
public class PropertyErrorException extends OMRSCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating a PropertyErrorException.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     */
    public PropertyErrorException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a PropertyErrorException that resulted from a previous error.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param caughtError  the error that resulted in this exception.
     * */
    public PropertyErrorException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
