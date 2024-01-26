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
import org.odpi.openmetadata.engineservices.repositorygovernance.server.RepositoryGovernanceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;

import java.util.List;

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
     * @param governanceEngineMap map of configured engines
     * @throws OMAGConfigurationErrorException an issue in the configuration prevented initialization
     */
    @Override
    public void initialize(String                              localServerId,
                           String                              localServerName,
                           AuditLog                            auditLog,
                           String                              localServerUserId,
                           String                              localServerPassword,
                           int                                 maxPageSize,
                           GovernanceEngineConfigurationClient configurationClient,
                           GovernanceContextClient             governanceActionClient,
                           EngineServiceConfig                 engineServiceConfig,
                           GovernanceEngineMap                 governanceEngineMap) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize engine service";
        final String methodName        = "initialize";

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
            List<EngineConfig> engineConfigs            = this.getEngines(engineServiceConfig);

            /*
             * Create an entry in the governance engine map for each configured engine.
             */
            governanceEngineMap.setGovernanceEngineProperties(engineConfigs,
                                                              partnerServiceServerName,
                                                              partnerServiceRootURL);


            /*
             * Set up the REST APIs.
             */
            archiveManagerInstance = new RepositoryGovernanceInstance(localServerName,
                                                                      EngineServiceDescription.REPOSITORY_GOVERNANCE_OMES.getEngineServiceName(),
                                                                      auditLog,
                                                                      localServerUserId,
                                                                      maxPageSize,
                                                                      engineServiceConfig.getOMAGServerPlatformRootURL(),
                                                                      engineServiceConfig.getOMAGServerName());
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
