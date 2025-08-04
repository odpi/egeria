/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionAuditCode;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionErrorCode;
import org.odpi.openmetadata.engineservices.governanceaction.listener.OpenMetadataOutTopicListener;
import org.odpi.openmetadata.engineservices.governanceaction.server.GovernanceActionInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataEventClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;


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
     * @param engineServiceConfig details of the options and the engines to run.
     * @param governanceEngineMap map of configured engines
     * @throws OMAGConfigurationErrorException an issue in the configuration prevented initialization
     */
    @SuppressWarnings(value = "deprecation")
    @Override
    public void initialize(String                              localServerId,
                           String                              localServerName,
                           AuditLog                            auditLog,
                           String                              localServerUserId,
                           String                              localServerPassword,
                           int                                 maxPageSize,
                           GovernanceConfigurationClient       configurationClient,
                           EngineServiceConfig                 engineServiceConfig,
                           GovernanceEngineMap                 governanceEngineMap) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize engine service";
        final String methodName        = "initialize";

        this.auditLog = auditLog;
        this.localServerName = localServerName;

        try
        {
            this.validateConfigDocument(engineServiceConfig);

            /*
             * The governance action services need access to an open metadata server to retrieve information about the elements they are governing.
             * Open metadata is accessed through the Governance Action Engine OMAS.
             */
            String             accessServiceRootURL    = this.getPartnerServiceRootURL(engineServiceConfig);
            String             accessServiceServerName = this.getPartnerServiceServerName(engineServiceConfig);

            auditLog.logMessage(actionDescription, GovernanceActionAuditCode.ENGINE_SERVICE_INITIALIZING.getMessageDefinition(localServerName,
                                                                                                                              accessServiceServerName,
                                                                                                                              accessServiceRootURL));

            /*
             * Create an entry in the governance engine map for each configured engine.
             */
            governanceEngineMap.setGovernanceEngineProperties(engineServiceConfig.getEngines(),
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

            OCFRESTClient governanceEngineRESTClient;

            if (localServerPassword != null)
            {
                governanceEngineRESTClient = new OCFRESTClient(accessServiceServerName,
                                                               accessServiceRootURL,
                                                               localServerUserId,
                                                               localServerPassword,
                                                               auditLog);
            }
            else
            {
                governanceEngineRESTClient = new OCFRESTClient(accessServiceServerName,
                                                               accessServiceRootURL,
                                                               auditLog);

            }

            OpenMetadataEventClient eventClient = new EgeriaOpenMetadataEventClient(accessServiceServerName,
                                                                                    accessServiceRootURL,
                                                                                    localServerUserId,
                                                                                    localServerPassword,
                                                                                    maxPageSize,
                                                                                    auditLog,
                                                                                    localServerId + localServerName + EngineServiceDescription.GOVERNANCE_ACTION_OMES.getEngineServiceName());

            eventClient.registerListener(localServerUserId, new OpenMetadataOutTopicListener(governanceEngineMap, auditLog));
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
