/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PropertyServerException provides a checked exception for reporting errors when connecting to a
 * metadata repository to retrieve properties about the connected asset.
 * It may be a configuration error or temporary outage.  The parameters captured by the constructors
 * pinpoint the type an cause of the error.
 */
public class PropertyServerException extends OCFCheckedExceptionBase
{
    private static final Logger log = LoggerFactory.getLogger(PropertyServerException.class);

    /**
     * This is the typical constructor for creating a PropertyServerException.  It captures the essential details
     * about the error, where it occurred and how to fix it.
     *
     * @param httpCode   code to use on a REST interface
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     */
    public PropertyServerException(int httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        log.debug(httpCode + ", " + className + ", " + actionDescription);
    }


    /**
     * This constructor is used when an unexpected exception has been caught that needs to be wrapped in a
     * PropertyServerException in order to add the essential details about the error, where it occurred and
     * how to fix it.
     *
     * @param httpCode code to use on a REST interface
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the exception/error that caused this exception to be raised
     */
    public PropertyServerException(int httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        log.debug(httpCode + ", " + className + ", " + actionDescription + ", " + caughtError.toString());
    }
}