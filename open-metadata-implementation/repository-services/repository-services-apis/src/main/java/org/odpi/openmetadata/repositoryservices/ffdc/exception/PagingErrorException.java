/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.ffdc.exception;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.io.Serial;
import java.util.Map;

/**
 * The PagingErrorException is thrown by an OMRS Connector when the caller has passed invalid paging attributes
 * on a search call.
 */
public class PagingErrorException extends InvalidParameterException
{
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public PagingErrorException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription, "paging");
    }


    /**
     * This is the typical constructor used for creating an exception.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public PagingErrorException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, "paging", relatedProperties);
    }


    /**
     * This is the constructor used for creating an exception when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     */
    public PagingErrorException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Exception                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError, "paging");
    }


    /**
     * This is the constructor used for creating an exception when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public PagingErrorException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Exception                  caughtError,
                                Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, "paging", relatedProperties);
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
    public PagingErrorException(int                 httpCode,
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
              "paging",
              relatedProperties);
    }
}
