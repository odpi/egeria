/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.admin;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.rest.GovernanceEngineRESTClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionAuditCode;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionErrorCode;
import org.odpi.openmetadata.engineservices.governanceaction.handlers.GovernanceActionEngineHandler;
import org.odpi.openmetadata.engineservices.governanceaction.server.GovernanceActionInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @throws OMAGConfigurationErrorException an issue in the configuration prevented initialization
     */
    public Map<String, GovernanceEngineHandler> initialize(String                              localServerId,
                                                           String                              localServerName,
                                                           AuditLog                            auditLog,
                                                           String                              localServerUserId,
                                                           String                              localServerPassword,
                                                           int                                 maxPageSize,
                                                           GovernanceEngineConfigurationClient configurationClient,
                                                           GovernanceEngineClient              serverClient,
                                                           EngineServiceConfig                 engineServiceConfig) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize engine service";
        final String methodName        = "initialize";

        GovernanceEngineRESTClient restClient;

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
            String             accessServiceRootURL    = this.getAccessServiceRootURL(engineServiceConfig);
            String             accessServiceServerName = this.getAccessServiceServerName(engineServiceConfig);
            List<EngineConfig> governanceActionEngines = this.getEngines(engineServiceConfig);

            /*
             * Create the client for accessing the open metadata repositories.
             */
            GovernanceEngineClient governanceEngineClient;

            try
            {
                if ((localServerName != null) && (localServerPassword != null))
                {
                    restClient = new GovernanceEngineRESTClient(accessServiceServerName,
                                                                accessServiceRootURL,
                                                                localServerUserId,
                                                                localServerPassword);
                }
                else
                {
                    restClient = new GovernanceEngineRESTClient(accessServiceServerName, accessServiceRootURL);
                }

                governanceEngineClient = new GovernanceEngineClient(accessServiceServerName,
                                                                    accessServiceRootURL,
                                                                    restClient,
                                                                    maxPageSize);
            }
            catch (InvalidParameterException error)
            {
                throw new OMAGConfigurationErrorException(error.getReportedErrorMessage(), error);
            }


            /*
             * Create a governance action handler for each of the governance action engines.
             */
            Map<String, GovernanceActionEngineHandler> governanceActionEngineHandlers = this.getGovernanceActionEngineHandlers(governanceActionEngines,
                                                                                                                               accessServiceRootURL,
                                                                                                                               accessServiceServerName,
                                                                                                                               localServerUserId,
                                                                                                                               configurationClient,
                                                                                                                               serverClient,
                                                                                                                               governanceEngineClient,
                                                                                                                               maxPageSize);

            if (governanceActionEngineHandlers == null)
            {
                auditLog.logMessage(actionDescription, GovernanceActionAuditCode.NO_GOVERNANCE_ACTION_ENGINES_STARTED.getMessageDefinition(localServerName));

                throw new OMAGConfigurationErrorException(GovernanceActionErrorCode.NO_GOVERNANCE_ACTION_ENGINES_STARTED.getMessageDefinition(localServerName),
                                                          this.getClass().getName(),
                                                          methodName);
            }


            /*
             * Set up the REST APIs.
             */
            governanceActionInstance = new GovernanceActionInstance(localServerName,
                                                                    EngineServiceDescription.GOVERNANCE_ACTION_OMES.getEngineServiceName(),
                                                                    auditLog,
                                                                    localServerUserId,
                                                                    maxPageSize,
                                                                    engineServiceConfig.getOMAGServerPlatformRootURL(),
                                                                    engineServiceConfig.getOMAGServerName(),
                                                                    governanceActionEngineHandlers);

            Map<String, GovernanceEngineHandler> governanceEngineHandlers = new HashMap<>();

            for (String engineName : governanceActionEngineHandlers.keySet())
            {
                if (engineName != null)
                {
                    governanceEngineHandlers.put(engineName, governanceActionEngineHandlers.get(engineName));
                }
            }

            return governanceEngineHandlers;
        }
        catch (Throwable error)
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
     * Create the list of governance action engine handlers.  Note that a new governance engine client is created for the engine services
     * in case the metadata server used by the engine services is different from the one configured for the engine host services to manage
     * configuration etc.  This is particularly important when processing assets since the visibility to assets may be different from server to
     * server due to the supportedZones configuration.
     *
     * @param governanceActionEngines list of governance action engines
     * @param accessServiceRootURL URL Root for the Governance Action Engine OMAS
     * @param accessServiceServerName Server Name for the Governance Action Engine OMAS
     * @param localServerUserId user id for this server to use if sending REST requests and processing inbound messages.
     * @param configurationClient client to retrieve configuration from
     * @param serverClient client to control the execution of governance action requests
     * @param governanceEngineClient client for calling REST APIs
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     * @return map of governance action engine GUIDs to handlers
     */
    private Map<String, GovernanceActionEngineHandler>  getGovernanceActionEngineHandlers(List<EngineConfig>                  governanceActionEngines,
                                                                                          String                              accessServiceRootURL,
                                                                                          String                              accessServiceServerName,
                                                                                          String                              localServerUserId,
                                                                                          GovernanceEngineConfigurationClient configurationClient,
                                                                                          GovernanceEngineClient              serverClient,
                                                                                          GovernanceEngineClient              governanceEngineClient,
                                                                                          int                                 maxPageSize)
    {
        Map<String, GovernanceActionEngineHandler> governanceActionEngineHandlers = new HashMap<>();

        for (EngineConfig   governanceActionEngine : governanceActionEngines)
        {
            if (governanceActionEngine != null)
            {
                /*
                 * Create a handler for the governance action engine.
                 */
                GovernanceActionEngineHandler  handler = new GovernanceActionEngineHandler(governanceActionEngine,
                                                                                           localServerName,
                                                                                           accessServiceServerName,
                                                                                           accessServiceRootURL,
                                                                                           localServerUserId,
                                                                                           configurationClient,
                                                                                           serverClient,
                                                                                           governanceEngineClient,
                                                                                           auditLog,
                                                                                           maxPageSize);

                governanceActionEngineHandlers.put(governanceActionEngine.getEngineQualifiedName(), handler);
            }
        }

        if (governanceActionEngineHandlers.isEmpty())
        {
            return null;
        }
        else
        {
            return governanceActionEngineHandlers;
        }
    }


    /**
     * Shutdown the engine service.
     */
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        auditLog.logMessage(actionDescription, GovernanceActionAuditCode.SERVER_SHUTTING_DOWN.getMessageDefinition(localServerName));

        governanceActionInstance.shutdown();

        auditLog.logMessage(actionDescription, GovernanceActionAuditCode.SERVER_SHUTDOWN.getMessageDefinition(localServerName));
    }
}
