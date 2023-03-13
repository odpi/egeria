/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

/**
 * DiscoveryServiceException indicates there has been a problem running a discovery service.  The
 * error codes and messages indicate the cause of the problem and guidance on finding a remedy.
 */
public class DiscoveryServiceException extends ConnectorCheckedException
{
    private static final long serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating a DiscoveryServiceException.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     */
    public DiscoveryServiceException(ExceptionMessageDefinition messageDefinition,
                                     String                     className,
                                     String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the constructor used for creating a DiscoveryServiceException in response to a previous exception.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError   the error that resulted in this exception.
     */
    public DiscoveryServiceException(ExceptionMessageDefinition messageDefinition,
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
    public DiscoveryServiceException(String                    errorMessage,
                                     ConnectorCheckedException template)
    {
        super(errorMessage, template);
    }
}
