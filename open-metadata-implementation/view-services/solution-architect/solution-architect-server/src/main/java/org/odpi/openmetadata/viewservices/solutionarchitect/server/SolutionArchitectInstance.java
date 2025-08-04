/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorRoleHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.InformationSupplyChainHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SolutionBlueprintHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SolutionComponentHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.EgeriaOpenMetadataStoreHandler;


/**
 * SolutionArchitectInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SolutionArchitectInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.SOLUTION_ARCHITECT;

    private final InformationSupplyChainHandler informationSupplyChainHandler;
    private final SolutionBlueprintHandler      solutionBlueprintHandler;
    private final SolutionComponentHandler      solutionComponentHandler;
    private final ActorRoleHandler         actorRoleHandler;


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

        OpenMetadataClient openMetadataClient;
        if (localServerUserPassword == null)
        {
            openMetadataClient = new EgeriaOpenMetadataStoreHandler(remoteServerName,
                                                                    remoteServerURL,
                                                                    maxPageSize);

        }
        else
        {
            openMetadataClient = new EgeriaOpenMetadataStoreHandler(remoteServerName,
                                                                    remoteServerURL,
                                                                    localServerUserId,
                                                                    localServerUserPassword,
                                                                    maxPageSize);
        }

        solutionComponentHandler = new SolutionComponentHandler(serverName,
                                                                auditLog,
                                                                myDescription.getViewServiceFullName(),
                                                                openMetadataClient);

        solutionBlueprintHandler = new SolutionBlueprintHandler(serverName,
                                                                auditLog,
                                                                myDescription.getViewServiceFullName(),
                                                                openMetadataClient);

        informationSupplyChainHandler = new InformationSupplyChainHandler(serverName,
                                                                auditLog,
                                                                myDescription.getViewServiceFullName(),
                                                                openMetadataClient);

        actorRoleHandler = new ActorRoleHandler(serverName,
                                                auditLog,
                                                myDescription.getViewServiceFullName(),
                                                openMetadataClient);
    }


    /**
     * Return the solution manager client.  This client is from the OMF services and is for maintaining
     * solution components.
     *
     * @return client
     */
    public SolutionComponentHandler getSolutionComponentHandler()
    {
        return solutionComponentHandler;
    }



    /**
     * Return the solution manager client.  This client is from the OMF services and is for maintaining
     * solution blueprints.
     *
     * @return client
     */
    public SolutionBlueprintHandler getSolutionBlueprintHandler()
    {
        return solutionBlueprintHandler;
    }




    /**
     * Return the solution manager client.  This client is from the OMF services and is for maintaining
     * information supply chains.
     *
     * @return client
     */
    public InformationSupplyChainHandler getInformationSupplyChainHandler()
    {
        return informationSupplyChainHandler;
    }


    /**
     * Return the solution manager client.  This client is from the Digital Architecture OMAS and is for maintaining
     * information supply chains and solutions.
     *
     * @return client
     */
    public ActorRoleHandler getSolutionRoleHandler()
    {
        return actorRoleHandler;
    }
}
