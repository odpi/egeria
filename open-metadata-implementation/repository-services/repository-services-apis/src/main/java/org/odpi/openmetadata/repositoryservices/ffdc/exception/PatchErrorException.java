/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.ffdc.exception;

/**
 * PatchErrorException provides a checked exception for reporting that a typedef can not be updated because there are
 * problems with the supplied TypeDefPatch.  The OMRSErrorCode adds specific details for the cause/effect of the error.
 */
public class PatchErrorException extends OMRSCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    /**
     * This is the typical constructor for creating an PatchErrorException.  It captures the essential details
     * about the error, where it occurred and how to fix it.
     *
     * @param httpCode code to use across a REST interface
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     */
    public PatchErrorException(int httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This constructor is used when an unexpected exception has been caught that needs to be wrapped in a
     * PatchErrorException in order to add the essential details about the error, where it occurred and
     * how to fix it.
     *
     * @param httpCode code to use across a REST interface
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     * @param caughtException the exception/error that caused this exception to be raised
     */
    public PatchErrorException(int httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtException)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtException);
    }


    /**
     * Construct a Patch error that is caused because it references an invalid type.
     *
     * @param error invalid type error.
     */
    public PatchErrorException(TypeErrorException   error)
    {
        super(error.getReportedHTTPCode(),
              error.getReportingClassName(),
              error.getReportingActionDescription(),
              error.getErrorMessage(),
              error.getReportedSystemAction(),
              error.getReportedUserAction(),
              error);
    }
}
