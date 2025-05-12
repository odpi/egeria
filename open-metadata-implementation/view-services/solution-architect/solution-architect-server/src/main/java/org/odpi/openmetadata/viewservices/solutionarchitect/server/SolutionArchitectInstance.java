/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server;

import org.odpi.openmetadata.accessservices.digitalarchitecture.client.SolutionManager;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

/**
 * SolutionArchitectInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SolutionArchitectInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.SOLUTION_ARCHITECT;

    private final SolutionManager solutionManager;


    /**
     * Set up the Solution Architect OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public SolutionArchitectInstance(String       serverName,
                                     AuditLog     auditLog,
                                     String       localServerUserId,
                                     int          maxPageSize,
                                     String       remoteServerName,
                                     String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        solutionManager = new SolutionManager(remoteServerName, remoteServerURL, maxPageSize, auditLog);
    }


    /**
     * Return the solution manager client.  This client is from the Digital Architecture OMAS and is for maintaining
     * information supply chains and solutions.
     *
     * @return client
     */
    public SolutionManager getSolutionManagerClient()
    {
        return solutionManager;
    }
}
