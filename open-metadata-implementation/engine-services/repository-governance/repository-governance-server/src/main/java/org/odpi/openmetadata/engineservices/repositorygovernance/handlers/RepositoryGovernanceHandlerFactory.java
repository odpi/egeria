/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.engineservices.repositorygovernance.handlers;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.GovernanceEngineHandlerFactory;
import org.odpi.openmetadata.repositoryservices.clients.EnterpriseRepositoryServicesClient;

/**
 * GovernanceEngineHandler factory class for the Repository Governance OMES.
 */
public class RepositoryGovernanceHandlerFactory extends GovernanceEngineHandlerFactory
{
    /**
     * Create a client-side object for calling a survey action engine.
     *
     * @param engineConfig        information about the governance engine.
     * @param localServerName     the name of the engine host server where the survey action engine is running
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
                                                                 GovernanceConfigurationClient       configurationClient,
                                                                 GovernanceContextClient             serverClient,
                                                                 AuditLog                            auditLog,
                                                                 int                                 maxPageSize) throws InvalidParameterException
    {
        if (engineConfig != null)
        {
            EnterpriseRepositoryServicesClient repositoryServicesClient;

            try
            {
                repositoryServicesClient = new EnterpriseRepositoryServicesClient(engineConfig.getOMAGServerName(),
                                                                                  engineConfig.getOMAGServerPlatformRootURL(),
                                                                                  engineConfig.getSecretsStoreProvider(),
                                                                                  engineConfig.getSecretsStoreLocation(),
                                                                                  engineConfig.getSecretsStoreCollection(),
                                                                                  auditLog,
                                                                                  maxPageSize,
                                                                                  engineConfig.getEngineId());
            }
            catch (InvalidParameterException error)
            {
                throw new InvalidParameterException(error, "client parameters");
            }

            return new RepositoryGovernanceEngineHandler(engineConfig,
                                                         localServerName,
                                                         localServerUserId,
                                                         configurationClient,
                                                         serverClient,
                                                         repositoryServicesClient,
                                                         auditLog,
                                                         maxPageSize);
        }

        return null;
    }
}
