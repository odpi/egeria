/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.admin;


import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceContextClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.engineservices.repositorygovernance.ffdc.RepositoryGovernanceAuditCode;
import org.odpi.openmetadata.engineservices.repositorygovernance.ffdc.RepositoryGovernanceErrorCode;
import org.odpi.openmetadata.engineservices.repositorygovernance.handlers.RepositoryGovernanceEngineHandler;
import org.odpi.openmetadata.engineservices.repositorygovernance.server.RepositoryGovernanceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.repositoryservices.clients.EnterpriseRepositoryServicesClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RepositoryGovernanceAdmin is called during server start-up and initializes the Repository Governance OMES.
 */
public class RepositoryGovernanceAdmin extends EngineServiceAdmin
{
    private RepositoryGovernanceInstance archiveManagerInstance = null;

    /**
     * Initialize engine service.
     *
     * @param localServerId unique identifier of this server
     * @param localServerName name of this server
     * @param auditLog link to the repository responsible for servicing the REST calls
     * @param localServerUserId user id for this server to use if sending REST requests and processing inbound messages
     * @param localServerPassword password for this server to use if sending REST requests
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     * @param configurationClient client used by the engine host services to connect to the Governance Engine OMAS to retrieve the governance engine definitions
     * @param governanceActionClient client used by the engine host services to connect to the Governance Engine OMAS to manage governance actions
     * @param engineServiceConfig details of the options and the engines to run
     * @throws OMAGConfigurationErrorException an issue in the configuration prevented initialization
     */
    @Override
    public Map<String, GovernanceEngineHandler> initialize(String                              localServerId,
                                                           String                              localServerName,
                                                           AuditLog                            auditLog,
                                                           String                              localServerUserId,
                                                           String                              localServerPassword,
                                                           int                                 maxPageSize,
                                                           GovernanceEngineConfigurationClient configurationClient,
                                                           GovernanceContextClient              governanceActionClient,
                                                           EngineServiceConfig                 engineServiceConfig) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize engine service";
        final String methodName        = "initialize";

        EnterpriseRepositoryServicesClient restClient;

        this.auditLog = auditLog;
        this.localServerName = localServerName;

        auditLog.logMessage(actionDescription, RepositoryGovernanceAuditCode.ENGINE_SERVICE_INITIALIZING.getMessageDefinition(localServerName));

        try
        {
            this.validateConfigDocument(engineServiceConfig);

            /*
             * The archive services need access to an open metadata server to retrieve information about the asset they are analysing and
             * to store the resulting analysis results in Annotations.
             * Open metadata is accessed through the OMRS.
             */
            String             partnerServiceRootURL    = this.getPartnerServiceRootURL(engineServiceConfig);
            String             partnerServiceServerName = this.getPartnerServiceServerName(engineServiceConfig);
            List<EngineConfig> archiveEngines           = this.getEngines(engineServiceConfig);

            /*
             * Create the client for accessing the open metadata repositories.
             */
            if ((localServerName != null) && (localServerPassword != null))
            {
                restClient = new EnterpriseRepositoryServicesClient(partnerServiceServerName,
                                                                    partnerServiceRootURL,
                                                                    localServerUserId,
                                                                    localServerPassword,
                                                                    maxPageSize,
                                                                    localServerId);
            }
            else
            {
                restClient = new EnterpriseRepositoryServicesClient(partnerServiceServerName, partnerServiceRootURL, maxPageSize, localServerId);
            }


            /*
             * Create a archive handler for each of the archive engines.
             */
            Map<String, RepositoryGovernanceEngineHandler> archiveEngineHandlers = this.getRepositoryGovernanceEngineHandlers(archiveEngines,
                                                                                                                              partnerServiceServerName,
                                                                                                                              localServerUserId,
                                                                                                                              configurationClient,
                                                                                                                              governanceActionClient,
                                                                                                                              restClient,
                                                                                                                              maxPageSize);

            if (archiveEngineHandlers == null)
            {
                auditLog.logMessage(actionDescription, RepositoryGovernanceAuditCode.NO_REPOSITORY_GOVERNANCE_ENGINES_STARTED.getMessageDefinition(localServerName));

                throw new OMAGConfigurationErrorException(RepositoryGovernanceErrorCode.NO_REPOSITORY_GOVERNANCE_ENGINES_STARTED.getMessageDefinition(localServerName),
                                                          this.getClass().getName(),
                                                          methodName);
            }


            /*
             * Set up the REST APIs.
             */
            archiveManagerInstance = new RepositoryGovernanceInstance(localServerName,
                                                                      EngineServiceDescription.REPOSITORY_GOVERNANCE_OMES.getEngineServiceName(),
                                                                      auditLog,
                                                                      localServerUserId,
                                                                      maxPageSize,
                                                                      engineServiceConfig.getOMAGServerPlatformRootURL(),
                                                                      engineServiceConfig.getOMAGServerName(),
                                                                      archiveEngineHandlers);

            Map<String, GovernanceEngineHandler> governanceEngineHandlers = new HashMap<>();

            for (String engineName : archiveEngineHandlers.keySet())
            {
                if (engineName != null)
                {
                    governanceEngineHandlers.put(engineName, archiveEngineHandlers.get(engineName));
                }
            }

            return governanceEngineHandlers;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  RepositoryGovernanceAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
                                  error.toString(),
                                  error);

            throw new OMAGConfigurationErrorException(RepositoryGovernanceErrorCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      error);
        }
    }


    /**
     * Create the list of archive engine handlers.
     *
     * @param archiveEngines list of archive engines
     * @param accessServiceServerName Server Name for the OMRS
     * @param localServerUserId user id for this server to use if sending REST requests and processing inbound messages.
     * @param configurationClient client to retrieve configuration from
     * @param governanceActionClient client used by the engine host services to connect to the Governance Engine OMAS to manage governance actions
     * @param archiveEngineClient client for calling REST APIs and receiving events
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     * @return map of archive engine GUIDs to handlers
     * @throws OMAGConfigurationErrorException problem with config
     */
    private Map<String, RepositoryGovernanceEngineHandler>  getRepositoryGovernanceEngineHandlers(List<EngineConfig>                  archiveEngines,
                                                                                                  String                              accessServiceServerName,
                                                                                                  String                              localServerUserId,
                                                                                                  GovernanceEngineConfigurationClient configurationClient,
                                                                                                  GovernanceContextClient             governanceActionClient,
                                                                                                  EnterpriseRepositoryServicesClient  archiveEngineClient,
                                                                                                  int                                 maxPageSize) throws OMAGConfigurationErrorException
    {
        Map<String, RepositoryGovernanceEngineHandler> archiveEngineHandlers = new HashMap<>();

        for (EngineConfig   archiveEngine : archiveEngines)
        {
            if (archiveEngine != null)
            {
                /*
                 * Create a handler for the archive engine.
                 */
                RepositoryGovernanceEngineHandler  handler = new RepositoryGovernanceEngineHandler(archiveEngine,
                                                                                                   accessServiceServerName,
                                                                                                   localServerUserId,
                                                                                                   configurationClient,
                                                                                                   governanceActionClient,
                                                                                                   archiveEngineClient,
                                                                                                   auditLog,
                                                                                                   maxPageSize);

                archiveEngineHandlers.put(archiveEngine.getEngineQualifiedName(), handler);
            }
        }

        if (archiveEngineHandlers.isEmpty())
        {
            return null;
        }
        else
        {
            return archiveEngineHandlers;
        }
    }


    /**
     * Shutdown the engine service.
     */
    @Override
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        auditLog.logMessage(actionDescription, RepositoryGovernanceAuditCode.SERVER_SHUTTING_DOWN.getMessageDefinition(localServerName));

        archiveManagerInstance.shutdown();

        auditLog.logMessage(actionDescription, RepositoryGovernanceAuditCode.SERVER_SHUTDOWN.getMessageDefinition(localServerName));
    }
}
