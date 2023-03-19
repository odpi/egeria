/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

/**
 * DiscoveryEngineException indicates there is a problem with a request to a specific discovery engine.  The
 * error codes and messages indicate the cause of the problem and guidance on finding a remedy.
 */
public class DiscoveryEngineException extends OCFCheckedExceptionBase
{
    private static final long serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating a DiscoveryEngineException.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     */
    public DiscoveryEngineException(ExceptionMessageDefinition messageDefinition,
                                    String                     className,
                                    String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the constructor used for creating a DiscoveryEngineException in response to a previous exception.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError   the error that resulted in this exception.
     */
    public DiscoveryEngineException(ExceptionMessageDefinition messageDefinition,
                                    String                     className,
                                    String                     actionDescription,
                                    Throwable                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param errorMessage associated message
     * @param template   object to copy
     */
    public DiscoveryEngineException(String                  errorMessage,
                                    OCFCheckedExceptionBase template)
    {
        super(errorMessage, template);
    }
}
