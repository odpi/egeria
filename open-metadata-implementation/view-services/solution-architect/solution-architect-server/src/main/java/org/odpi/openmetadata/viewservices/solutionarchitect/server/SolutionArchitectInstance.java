/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ActorRoleHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.SolutionHandler;

/**
 * SolutionArchitectInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SolutionArchitectInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.SOLUTION_ARCHITECT;

    private final SolutionHandler solutionHandler;
    private final ActorRoleHandler actorRoleHandler;


    /**
     * Set up the Solution Architect OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public SolutionArchitectInstance(String       serverName,
                                     AuditLog     auditLog,
                                     String       localServerUserId,
                                     String       localServerUserPassword,
                                     int          maxPageSize,
                                     String       remoteServerName,
                                     String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        if (localServerUserPassword == null)
        {
            solutionHandler = new SolutionHandler(serverName,
                                                  remoteServerName,
                                                  remoteServerURL,
                                                  auditLog,
                                                  AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceURLMarker(),
                                                  ViewServiceDescription.SOLUTION_ARCHITECT.getViewServiceFullName(),
                                                  maxPageSize);

            actorRoleHandler = new ActorRoleHandler(serverName,
                                                    remoteServerName,
                                                    remoteServerURL,
                                                    auditLog,
                                                    AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceURLMarker(),
                                                    ViewServiceDescription.SOLUTION_ARCHITECT.getViewServiceFullName(),
                                                    maxPageSize,
                                                    true);
        }
        else
        {
            solutionHandler = new SolutionHandler(serverName,
                                                  remoteServerName,
                                                  remoteServerURL,
                                                  localServerUserId,
                                                  localServerUserPassword,
                                                  auditLog,
                                                  AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceURLMarker(),
                                                  ViewServiceDescription.SOLUTION_ARCHITECT.getViewServiceFullName(),
                                                  maxPageSize);

            actorRoleHandler = new ActorRoleHandler(serverName,
                                                    remoteServerName,
                                                    remoteServerURL,
                                                    localServerUserId,
                                                    localServerUserPassword,
                                                    auditLog,
                                                    AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceURLMarker(),
                                                    ViewServiceDescription.SOLUTION_ARCHITECT.getViewServiceFullName(),
                                                    maxPageSize,
                                                    true);
        }
    }


    /**
     * Return the solution manager client.  This client is from the Digital Architecture OMAS and is for maintaining
     * information supply chains and solutions.
     *
     * @return client
     */
    public SolutionHandler getSolutionManagerClient()
    {
        return solutionHandler;
    }


    /**
     * Return the solution manager client.  This client is from the Digital Architecture OMAS and is for maintaining
     * information supply chains and solutions.
     *
     * @return client
     */
    public ActorRoleHandler getSolutionRoleClient()
    {
        return actorRoleHandler;
    }
}
