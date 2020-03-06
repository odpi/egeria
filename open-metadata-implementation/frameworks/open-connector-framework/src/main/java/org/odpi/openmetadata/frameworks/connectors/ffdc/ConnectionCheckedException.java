/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConnectionCheckedException provides a checked exception for reporting errors found in connection objects.
 * Typically these errors are configuration errors that can be fixed by an administrator or power user.
 * The connection object has a complex structure and the aim of this exception, in conjunction with
 * OCFErrorCode, is to identify exactly what is wrong with the contents of the connection object
 * and the consequences of this error.
 */
public class ConnectionCheckedException extends OCFCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(ConnectionCheckedException.class);

    /**
     * This is the typical constructor for creating a ConnectionCheckedException.  It captures the essential details
     * about the error, where it occurred and how to fix it.
     *
     * @param httpCode code to use across a REST interface
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     */
    @Deprecated
    public ConnectionCheckedException(int httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        log.debug("{}, {}, {}", httpCode, className, actionDescription);
    }


    /**
     * This constructor is used when an unexpected exception has been caught that needs to be wrapped in a
     * ConnectionCheckedException in order to add the essential details about the error, where it occurred and
     * how to fix it.
     *
     * @param httpCode code to use across a REST interface
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     * @param caughtError the exception/error that caused this exception to be raised
     */
    @Deprecated
    public ConnectionCheckedException(int httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        log.debug("{}, {}, {}, {}", httpCode, className, actionDescription, caughtError);
    }


    /**
     * This is the typical constructor for creating a ConnectionCheckedException.  It captures the essential details
     * about the error, where it occurred and how to fix it.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     */
    public ConnectionCheckedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);

        log.debug("{}, {}, {}", messageDefinition, className, actionDescription);
    }


    /**
     * This constructor is used when an unexpected exception has been caught that needs to be wrapped in a
     * ConnectionCheckedException in order to add the essential details about the error, where it occurred and
     * how to fix it.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError the exception/error that caused this exception to be raised
     */
    public ConnectionCheckedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      Throwable                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError);

        log.debug("{}, {}, {}, {}", messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param errorMessage associated message
     * @param template   object to copy
     */
    public ConnectionCheckedException(String                     errorMessage,
                                      ConnectionCheckedException template)
    {
        super(errorMessage, template);
    }
}