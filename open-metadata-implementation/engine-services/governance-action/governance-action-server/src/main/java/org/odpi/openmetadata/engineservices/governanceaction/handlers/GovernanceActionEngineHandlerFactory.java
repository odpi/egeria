/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.engineservices.governanceaction.handlers;

import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceContextClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.GovernanceEngineHandlerFactory;

/**
 * GovernanceEngineHandler factory class for the Governance Action OMES.
 */
public class GovernanceActionEngineHandlerFactory extends GovernanceEngineHandlerFactory
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
            GovernanceContextClient governanceContextClient;
            GAFRESTClient           restClient;

            if ((localServerName != null) && (localServerPassword != null))
            {
                restClient = new GAFRESTClient(partnerServerName,
                                               partnerURLRoot,
                                               localServerUserId,
                                               localServerPassword);
            }
            else
            {
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
                                                     configurationClient,
                                                     serverClient,
                                                     governanceContextClient,
                                                     auditLog,
                                                     maxPageSize);
        }

        return null;
    }
}
