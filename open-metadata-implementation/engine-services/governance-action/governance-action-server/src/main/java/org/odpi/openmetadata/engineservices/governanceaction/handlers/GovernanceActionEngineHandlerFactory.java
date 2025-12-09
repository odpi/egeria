/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.engineservices.governanceaction.handlers;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.EgeriaConnectedAssetClient;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.GovernanceEngineHandlerFactory;

/**
 * GovernanceEngineHandler factory class for the Governance Action OMES.
 */
public class GovernanceActionEngineHandlerFactory extends GovernanceEngineHandlerFactory
{

    /**
     * Create a client-side object for calling a governance action engine.
     *
     * @param engineConfig        information about the governance engine.
     * @param localServerName     the name of the engine host server where the survey action engine is running
     * @param localServerUserId   user id for the engine host server to use
     * @param configurationClient client to retrieve the configuration
     * @param serverClient        client used by the engine host services to control the execution of engine action requests
     * @param auditLog            logging destination
     * @param maxPageSize         maximum number of results that can be returned in a single request
     * @throws InvalidParameterException unable to connect to the clients
     */
    @Override
    public GovernanceEngineHandler createGovernanceEngineHandler(EngineConfig                  engineConfig,
                                                                 String                        localServerName,
                                                                 String                        localServerUserId,
                                                                 GovernanceConfigurationClient configurationClient,
                                                                 GovernanceContextClient       serverClient,
                                                                 AuditLog                      auditLog,
                                                                 int                           maxPageSize) throws InvalidParameterException
    {
        if (engineConfig != null)
        {
            ConnectedAssetClient connectedAssetClient = new EgeriaConnectedAssetClient(engineConfig.getOMAGServerName(),
                                                                                       engineConfig.getOMAGServerPlatformRootURL(),
                                                                                       engineConfig.getSecretsStoreProvider(),
                                                                                       engineConfig.getSecretsStoreLocation(),
                                                                                       engineConfig.getSecretsStoreCollection(),
                                                                                       maxPageSize,
                                                                                       auditLog);

            OpenMetadataClient openMetadataStoreClient = new EgeriaOpenMetadataStoreClient(engineConfig.getOMAGServerName(),
                                                                                           engineConfig.getOMAGServerPlatformRootURL(),
                                                                                           engineConfig.getSecretsStoreProvider(),
                                                                                           engineConfig.getSecretsStoreLocation(),
                                                                                           engineConfig.getSecretsStoreCollection(),
                                                                                           maxPageSize,
                                                                                           auditLog);

            GovernanceContextClient governanceContextClient = new GovernanceContextClient(engineConfig.getOMAGServerName(),
                                                                                          engineConfig.getOMAGServerPlatformRootURL(),
                                                                                          engineConfig.getSecretsStoreProvider(),
                                                                                          engineConfig.getSecretsStoreLocation(),
                                                                                          engineConfig.getSecretsStoreCollection(),
                                                                                          maxPageSize,
                                                                                          auditLog);

            return new GovernanceActionEngineHandler(engineConfig,
                                                     localServerName,
                                                     localServerUserId,
                                                     openMetadataStoreClient,
                                                     connectedAssetClient,
                                                     configurationClient,
                                                     serverClient,
                                                     governanceContextClient,
                                                     auditLog,
                                                     maxPageSize);
        }

        return null;
    }
}
