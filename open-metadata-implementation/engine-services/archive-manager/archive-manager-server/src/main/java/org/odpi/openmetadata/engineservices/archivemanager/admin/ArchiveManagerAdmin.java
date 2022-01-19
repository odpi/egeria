/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.archivemanager.admin;


import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.engineservices.archivemanager.ffdc.ArchiveManagerAuditCode;
import org.odpi.openmetadata.engineservices.archivemanager.ffdc.ArchiveManagerErrorCode;
import org.odpi.openmetadata.engineservices.archivemanager.handlers.ArchiveEngineHandler;
import org.odpi.openmetadata.engineservices.archivemanager.server.ArchiveManagerInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.repositoryservices.clients.EnterpriseRepositoryServicesClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchiveManagerAdmin extends EngineServiceAdmin
{
    private ArchiveManagerInstance archiveManagerInstance = null;

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
    public Map<String, GovernanceEngineHandler> initialize(String                              localServerId,
                                                           String                              localServerName,
                                                           AuditLog                            auditLog,
                                                           String                              localServerUserId,
                                                           String                              localServerPassword,
                                                           int                                 maxPageSize,
                                                           GovernanceEngineConfigurationClient configurationClient,
                                                           GovernanceEngineClient              governanceActionClient,
                                                           EngineServiceConfig                 engineServiceConfig) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize engine service";
        final String methodName        = "initialize";

        EnterpriseRepositoryServicesClient restClient;

        this.auditLog = auditLog;
        this.localServerName = localServerName;

        auditLog.logMessage(actionDescription, ArchiveManagerAuditCode.ENGINE_SERVICE_INITIALIZING.getMessageDefinition(localServerName));

        try
        {
            this.validateConfigDocument(engineServiceConfig);

            /*
             * The archive services need access to an open metadata server to retrieve information about the asset they are analysing and
             * to store the resulting analysis results in Annotations.
             * Open metadata is accessed through the Archive Engine OMAS.
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
            Map<String, ArchiveEngineHandler> archiveEngineHandlers = this.getArchiveEngineHandlers(archiveEngines,
                                                                                                    partnerServiceServerName,
                                                                                                    localServerUserId,
                                                                                                    configurationClient,
                                                                                                    governanceActionClient,
                                                                                                    restClient,
                                                                                                    maxPageSize);

            if (archiveEngineHandlers == null)
            {
                auditLog.logMessage(actionDescription, ArchiveManagerAuditCode.NO_ARCHIVE_ENGINES_STARTED.getMessageDefinition(localServerName));

                throw new OMAGConfigurationErrorException(ArchiveManagerErrorCode.NO_ARCHIVE_ENGINES_STARTED.getMessageDefinition(localServerName),
                                                          this.getClass().getName(),
                                                          methodName);
            }


            /*
             * Set up the REST APIs.
             */
            archiveManagerInstance = new ArchiveManagerInstance(localServerName,
                                                                EngineServiceDescription.ARCHIVE_MANAGER_OMES.getEngineServiceName(),
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
                                  ArchiveManagerAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
                                  error.toString(),
                                  error);

            throw new OMAGConfigurationErrorException(ArchiveManagerErrorCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      error);
        }
    }


    /**
     * Create the list of archive engine handlers.
     *
     * @param archiveEngines list of archive engines
     * @param accessServiceServerName Server Name for the Archive Engine OMAS
     * @param localServerUserId user id for this server to use if sending REST requests and processing inbound messages.
     * @param configurationClient client to retrieve configuration from
     * @param governanceActionClient client used by the engine host services to connect to the Governance Engine OMAS to manage governance actions
     * @param archiveEngineClient client for calling REST APIs and receiving events
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     * @return map of archive engine GUIDs to handlers
     * @throws OMAGConfigurationErrorException problem with config
     */
    private Map<String, ArchiveEngineHandler>  getArchiveEngineHandlers(List<EngineConfig>                  archiveEngines,
                                                                        String                              accessServiceServerName,
                                                                        String                              localServerUserId,
                                                                        GovernanceEngineConfigurationClient configurationClient,
                                                                        GovernanceEngineClient              governanceActionClient,
                                                                        EnterpriseRepositoryServicesClient  archiveEngineClient,
                                                                        int                                 maxPageSize) throws OMAGConfigurationErrorException
    {
        Map<String, ArchiveEngineHandler> archiveEngineHandlers = new HashMap<>();

        for (EngineConfig   archiveEngine : archiveEngines)
        {
            if (archiveEngine != null)
            {
                /*
                 * Create a handler for the archive engine.
                 */
                ArchiveEngineHandler  handler = new ArchiveEngineHandler(archiveEngine,
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
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        auditLog.logMessage(actionDescription, ArchiveManagerAuditCode.SERVER_SHUTTING_DOWN.getMessageDefinition(localServerName));

        archiveManagerInstance.shutdown();

        auditLog.logMessage(actionDescription, ArchiveManagerAuditCode.SERVER_SHUTDOWN.getMessageDefinition(localServerName));
    }
}
