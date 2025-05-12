/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.io.Serial;

/**
 * Exception thrown when there is a conflict in Apache Atlas names.
 */
public class NameConflictException extends InvalidParameterException
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param parameterName     name of the invalid parameter if known
     * @param error             original exception
     */
    public NameConflictException(ExceptionMessageDefinition messageDefinition,
                                 String                     className,
                                 String                     actionDescription,
                                 String                     parameterName,
                                 Exception                  error)
    {
        super(messageDefinition, className, actionDescription, error, parameterName);
    }
}
