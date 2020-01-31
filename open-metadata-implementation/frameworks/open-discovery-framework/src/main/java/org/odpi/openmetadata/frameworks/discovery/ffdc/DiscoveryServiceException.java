/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.ffdc;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

/**
 * DiscoveryServiceException indicates there has been a problem running a discovery service.  The
 * error codes and messages indicate the cause of the problem and guidance on finding a remedy.
 */
public class DiscoveryServiceException extends ConnectorCheckedException
{
    private static final long serialVersionUID = 1L;

    /**
     * This is the typical constructor for creating the exception.  It captures the essential details
     * about the error, where it occurred and how to fix it.
     *
     * @param httpCode code to use across a REST interface
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     */
    public DiscoveryServiceException( int    httpCode,
                                     String className,
                                     String actionDescription,
                                     String errorMessage,
                                     String systemAction,
                                     String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This constructor is used when an unexpected exception has been caught that needs to be wrapped in a
     * ConnectorCheckedException in order to add the essential details about the error, where it occurred and
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
    public DiscoveryServiceException(int       httpCode,
                                     String    className,
                                     String    actionDescription,
                                     String    errorMessage,
                                     String    systemAction,
                                     String    userAction,
                                     Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
