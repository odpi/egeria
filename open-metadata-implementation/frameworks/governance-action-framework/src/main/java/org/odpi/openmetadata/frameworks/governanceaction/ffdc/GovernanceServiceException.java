/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

/**
 * GovernanceServiceException indicates there is a problem with a request to a specific governance engine.  The
 * error codes and messages indicate the cause of the problem and guidance on finding a remedy.
 */
public class GovernanceServiceException extends ConnectorCheckedException
{
    private static final long serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating a GovernanceServiceException.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     */
    public GovernanceServiceException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the constructor used for creating a GovernanceServiceException in response to a previous exception.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError   the error that resulted in this exception.
     */
    public GovernanceServiceException(ExceptionMessageDefinition messageDefinition,
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
    public GovernanceServiceException(String                  errorMessage,
                                      OCFCheckedExceptionBase template)
    {
        super(errorMessage, template);
    }
}
