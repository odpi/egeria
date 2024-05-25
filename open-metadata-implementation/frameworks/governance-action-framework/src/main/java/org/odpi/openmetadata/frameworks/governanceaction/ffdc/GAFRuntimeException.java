/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;

import java.io.Serial;
import java.util.Map;

/**
 * The GAFRuntimeException is used for all runtime exceptions from GAF components.  The GAFErrorCode is used to
 * provide the first failure data capture for this exception.  Typically runtime exceptions are the result of
 * programming errors and so a developer is looking at their resolution.
 */
public class GAFRuntimeException extends OCFRuntimeException
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating an OCFRuntimeException.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public GAFRuntimeException(ExceptionMessageDefinition messageDefinition,
                               String                     className,
                               String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the typical constructor used for creating an OCFRuntimeException.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public GAFRuntimeException(ExceptionMessageDefinition messageDefinition,
                               String                     className,
                               String                     actionDescription,
                               Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);
    }


    /**
     * This is the constructor used for creating a OCFRuntimeException that results from a previous error/exception
     * being thrown.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     */
    public GAFRuntimeException(ExceptionMessageDefinition messageDefinition,
                               String                     className,
                               String                     actionDescription,
                               Throwable                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This is the constructor used for creating a OCFRuntimeException that results from a previous error/exception
     * being thrown.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public GAFRuntimeException(ExceptionMessageDefinition messageDefinition,
                               String                     className,
                               String                     actionDescription,
                               Throwable                  caughtError,
                               Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);
    }
}
