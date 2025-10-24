/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.engineservices.governanceaction.handlers;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.EgeriaOpenGovernanceClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
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
    public GovernanceEngineHandler createGovernanceEngineHandler(EngineConfig                  engineConfig,
                                                                 String                        localServerName,
                                                                 String                        localServerUserId,
                                                                 String                        localServerPassword,
                                                                 String                        partnerServerName,
                                                                 String                        partnerURLRoot,
                                                                 GovernanceConfigurationClient configurationClient,
                                                                 GovernanceContextClient       serverClient,
                                                                 AuditLog                      auditLog,
                                                                 int                           maxPageSize) throws InvalidParameterException
    {
        if (engineConfig != null)
        {
            GovernanceContextClient       governanceContextClient;
            OpenGovernanceClient          openGovernanceClient;
            EgeriaOpenMetadataStoreClient openMetadataStoreClient;
            EgeriaConnectedAssetClient    connectedAssetClient;
            GAFRESTClient                 restClient;

            if ((localServerName != null) && (localServerPassword != null))
            {
                openMetadataStoreClient = new EgeriaOpenMetadataStoreClient(partnerServerName,
                                                                            partnerURLRoot,
                                                                            maxPageSize);
                connectedAssetClient = new EgeriaConnectedAssetClient(partnerServerName,
                                                                      partnerURLRoot,
                                                                      maxPageSize,
                                                                      auditLog);
                openGovernanceClient = new EgeriaOpenGovernanceClient(partnerServerName,
                                                                      partnerURLRoot,
                                                                      maxPageSize);
                restClient = new GAFRESTClient(partnerServerName,
                                               partnerURLRoot,
                                               localServerUserId,
                                               localServerPassword);
            }
            else
            {
                openMetadataStoreClient = new EgeriaOpenMetadataStoreClient(partnerServerName,
                                                                            partnerURLRoot,
                                                                            localServerUserId,
                                                                            localServerPassword,
                                                                            maxPageSize);
                connectedAssetClient = new EgeriaConnectedAssetClient(partnerServerName,
                                                                      partnerURLRoot,
                                                                      localServerUserId,
                                                                      localServerPassword,
                                                                      maxPageSize,
                                                                      auditLog);
                openMetadataStoreClient = new EgeriaOpenMetadataStoreClient(partnerServerName,
                                                                            partnerURLRoot,
                                                                            localServerUserId,
                                                                            localServerPassword,
                                                                            maxPageSize);
                openGovernanceClient = new EgeriaOpenGovernanceClient(partnerServerName,
                                                                      partnerURLRoot,
                                                                      localServerUserId,
                                                                      localServerPassword,
                                                                      maxPageSize);
                restClient = new GAFRESTClient(partnerServerName, partnerURLRoot);
            }

            governanceContextClient = new GovernanceContextClient(partnerServerName,
                                                                  partnerURLRoot,
                                                                  restClient,
                                                                  maxPageSize);



            return new GovernanceActionEngineHandler(engineConfig,
                                                     localServerName,
                                                     partnerServerName,
                                                     partnerURLRoot,
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
