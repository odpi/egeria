/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.admin;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceContextClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionAuditCode;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionErrorCode;
import org.odpi.openmetadata.engineservices.governanceaction.server.GovernanceActionInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;

import java.util.List;

/**
 * GovernanceActionAdmin is called during server start-up and initializes the Governance Action OMES.
 */
public class GovernanceActionAdmin extends EngineServiceAdmin
{
    private GovernanceActionInstance  governanceActionInstance = null;

    /**
     * Initialize engine service.
     *
     * @param localServerId unique identifier of this server
     * @param localServerName name of this server
     * @param auditLog link to the repository responsible for servicing the REST calls.
     * @param localServerUserId user id for this server to use if sending REST requests and processing inbound messages.
     * @param localServerPassword password for this server to use if sending REST requests.
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     * @param configurationClient client used to connect to the Governance Engine OMAS to retrieve the governance engine definitions.
     * @param serverClient client used to connect to the Governance Engine OMAS to manage governance actions
     * @param engineServiceConfig details of the options and the engines to run.
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
                           GovernanceContextClient             serverClient,
                           EngineServiceConfig                 engineServiceConfig,
                           GovernanceEngineMap                 governanceEngineMap) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize engine service";
        final String methodName        = "initialize";

        this.auditLog = auditLog;
        this.localServerName = localServerName;

        auditLog.logMessage(actionDescription, GovernanceActionAuditCode.ENGINE_SERVICE_INITIALIZING.getMessageDefinition(localServerName));

        try
        {
            this.validateConfigDocument(engineServiceConfig);

            /*
             * The governance action services need access to an open metadata server to retrieve information about the elements they are governing.
             * Open metadata is accessed through the Governance Action Engine OMAS.
             */
            String             accessServiceRootURL    = this.getPartnerServiceRootURL(engineServiceConfig);
            String             accessServiceServerName = this.getPartnerServiceServerName(engineServiceConfig);
            List<EngineConfig> governanceActionEngines = this.getEngines(engineServiceConfig);

            /*
             * Create an entry in the governance engine map for each configured engine.
             */
            governanceEngineMap.setGovernanceEngineProperties(governanceActionEngines,
                                                              accessServiceServerName,
                                                              accessServiceRootURL);

            /*
             * Set up the REST APIs.
             */
            governanceActionInstance = new GovernanceActionInstance(localServerName,
                                                                    EngineServiceDescription.GOVERNANCE_ACTION_OMES.getEngineServiceName(),
                                                                    auditLog,
                                                                    localServerUserId,
                                                                    maxPageSize,
                                                                    engineServiceConfig.getOMAGServerPlatformRootURL(),
                                                                    engineServiceConfig.getOMAGServerName());
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  GovernanceActionAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
                                  error.toString(),
                                  error);

            throw new OMAGConfigurationErrorException(GovernanceActionErrorCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
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

        auditLog.logMessage(actionDescription, GovernanceActionAuditCode.SERVER_SHUTTING_DOWN.getMessageDefinition(localServerName));

        governanceActionInstance.shutdown();

        auditLog.logMessage(actionDescription, GovernanceActionAuditCode.SERVER_SHUTDOWN.getMessageDefinition(localServerName));
    }
}
