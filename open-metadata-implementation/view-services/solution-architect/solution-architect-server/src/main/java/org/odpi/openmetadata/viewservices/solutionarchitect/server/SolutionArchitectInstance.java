/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;


/**
 * SolutionArchitectInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SolutionArchitectInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.SOLUTION_ARCHITECT;

    private final DesignPatternHandler          designPatternHandler;
    private final InformationSupplyChainHandler informationSupplyChainHandler;
    private final CollectionHandler             solutionBlueprintHandler;
    private final SolutionComponentHandler      solutionComponentHandler;


    /**
     * Set up the Solution Architect OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public SolutionArchitectInstance(String   serverName,
                                     AuditLog auditLog,
                                     String   localServerUserId,
                                     String   localServerSecretsStoreProvider,
                                     String   localServerSecretsStoreLocation,
                                     String   localServerSecretsStoreCollection,
                                     int      maxPageSize,
                                     String   remoteServerName,
                                     String   remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        OpenMetadataClient openMetadataClient = new EgeriaOpenMetadataStoreClient(remoteServerName,
                                                                                  remoteServerURL,
                                                                                  localServerSecretsStoreProvider,
                                                                                  localServerSecretsStoreLocation,
                                                                                  localServerSecretsStoreCollection,
                                                                                  maxPageSize,
                                                                                  auditLog);

        solutionComponentHandler = new SolutionComponentHandler(serverName,
                                                                auditLog,
                                                                myDescription.getViewServiceFullName(),
                                                                openMetadataClient);

        solutionBlueprintHandler = new CollectionHandler(serverName,
                                                         auditLog,
                                                         myDescription.getViewServiceFullName(),
                                                         openMetadataClient,
                                                         OpenMetadataType.SOLUTION_BLUEPRINT.typeName);

        informationSupplyChainHandler = new InformationSupplyChainHandler(serverName,
                                                                auditLog,
                                                                myDescription.getViewServiceFullName(),
                                                                openMetadataClient);

        designPatternHandler = new DesignPatternHandler(serverName,
                                                       auditLog,
                                                       myDescription.getViewServiceFullName(),
                                                       openMetadataClient);
    }


    /**
     * Return the solution component handler.
     *
     * @return client
     */
    public SolutionComponentHandler getSolutionComponentHandler()
    {
        return solutionComponentHandler;
    }



    /**
     * Return the solution blueprint handler.
     *
     * @return client
     */
    public CollectionHandler getSolutionBlueprintHandler()
    {
        return solutionBlueprintHandler;
    }


    /**
     * Return the information supply chain handler.
     *
     * @return client
     */
    public InformationSupplyChainHandler getInformationSupplyChainHandler()
    {
        return informationSupplyChainHandler;
    }


    /**
     * Return the design pattern handler.
     *
     * @return client
     */
    public DesignPatternHandler getDesignPatternHandler()
    {
        return designPatternHandler;
    }
}
