/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.engineservices.assetanalysis.handlers;

import org.odpi.openmetadata.accessservices.discoveryengine.client.DiscoveryEngineClient;
import org.odpi.openmetadata.accessservices.discoveryengine.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceContextClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.GovernanceEngineHandlerFactory;

/**
 * GovernanceEngineHandler factory class for the Asset Analysis OMES.
 */
public class DiscoveryEngineHandlerFactory extends GovernanceEngineHandlerFactory
{
    /**
     * Create a client-side object for calling a survey action engine.
     *
     * @param engineConfig        information about the governance engine.
     * @param localServerName     the name of the engine host server where the survey action engine is running
     * @param localServerUserId   user id for the engine host server to use
     * @param localServerPassword optional password for the engine host server to use
     * @param partnerServerName   name of partner server
     * @param partnerURLRoot      partner platform
     * @param configurationClient client to retrieve the configuration
     * @param serverClient        client used by the engine host services to control the execution of engine action requests
     * @param auditLog            logging destination
     * @param maxPageSize         maximum number of results that can be returned in a single request
     * @throws InvalidParameterException unable to connect to the clients
     */
    @Override
    public GovernanceEngineHandler createGovernanceEngineHandler(EngineConfig                        engineConfig,
                                                                 String                              localServerName,
                                                                 String                              localServerUserId,
                                                                 String                              localServerPassword,
                                                                 String                              partnerServerName,
                                                                 String                              partnerURLRoot,
                                                                 GovernanceEngineConfigurationClient configurationClient,
                                                                 GovernanceContextClient             serverClient,
                                                                 AuditLog                            auditLog,
                                                                 int                                 maxPageSize) throws InvalidParameterException
    {
        if (engineConfig != null)
        {
            DiscoveryEngineClient  discoveryEngineClient;
            OpenMetadataClient openMetadataClient;

            if (localServerPassword == null)
            {
                discoveryEngineClient = new DiscoveryEngineClient(partnerServerName,
                                                                  partnerURLRoot,
                                                                  auditLog);

                openMetadataClient = new OpenMetadataStoreClient(partnerServerName,
                                                                 partnerURLRoot);
            }
            else
            {
                discoveryEngineClient = new DiscoveryEngineClient(partnerServerName,
                                                                  partnerURLRoot,
                                                                  localServerUserId,
                                                                  localServerPassword,
                                                                  auditLog);

                openMetadataClient = new OpenMetadataStoreClient(partnerServerName,
                                                                 partnerURLRoot,
                                                                 localServerUserId,
                                                                 localServerPassword);
            }

            return new DiscoveryEngineHandler(engineConfig,
                                                 localServerName,
                                                 localServerUserId,
                                                 configurationClient,
                                                 serverClient,
                                                 discoveryEngineClient,
                                                 openMetadataClient,
                                                 auditLog,
                                                 maxPageSize);
        }

        return null;
    }
}
