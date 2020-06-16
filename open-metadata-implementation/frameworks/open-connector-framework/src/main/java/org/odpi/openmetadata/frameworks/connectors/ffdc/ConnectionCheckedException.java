/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

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
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public ConnectionCheckedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public ConnectionCheckedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError the error that resulted in this exception.
     */
    public ConnectionCheckedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      Throwable                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError the error that resulted in this exception.
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public ConnectionCheckedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      Throwable                  caughtError,
                                      Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);
    }


    /**
     * This is the constructor used when receiving an exception from a remote server.  The values are
     * stored directly in the response object and are passed explicitly to the new exception.
     * Notice that the technical aspects of the exception - such as class name creating the exception
     * are local values so that the implementation of the server is not exposed.
     *
     * @param httpCode   http response code to use if this exception flows over a REST call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param errorMessageId unique identifier for the message
     * @param errorMessageParameters parameters that were inserted in the message
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtErrorClassName   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public ConnectionCheckedException(int                 httpCode,
                                      String              className,
                                      String              actionDescription,
                                      String              errorMessage,
                                      String              errorMessageId,
                                      String[]            errorMessageParameters,
                                      String              systemAction,
                                      String              userAction,
                                      String              caughtErrorClassName,
                                      Map<String, Object> relatedProperties)
    {
        super(httpCode,
              className,
              actionDescription,
              errorMessage,
              errorMessageId,
              errorMessageParameters,
              systemAction,
              userAction,
              caughtErrorClassName,
              relatedProperties);
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

        log.debug("{}, {}, {}", httpCode, className, actionDescription, caughtError);
    }
}