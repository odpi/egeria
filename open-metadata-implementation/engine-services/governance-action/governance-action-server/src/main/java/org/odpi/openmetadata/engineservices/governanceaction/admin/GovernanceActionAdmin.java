/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.admin;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineEventClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.rest.GovernanceEngineRESTClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.engineservices.governanceaction.context.GovernanceListenerManager;
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
    private GovernanceListenerManager governanceListenerManager = null;

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
     * @throws OMAGConfigurationErrorException an issue in the configuration prevented initialization
     */
    public Map<String, GovernanceEngineHandler> initialize(String                              localServerId,
                                                           String                              localServerName,
                                                           AuditLog                            auditLog,
                                                           String                              localServerUserId,
                                                           String                              localServerPassword,
                                                           int                                 maxPageSize,
                                                           GovernanceEngineConfigurationClient configurationClient,
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
            this.validateConfigDocument(engineServiceConfig, methodName);

            /*
             * The governance action services need access to an open metadata server to retrieve information about the asset they are analysing and
             * to store the resulting analysis results in Annotations.
             * Open metadata is accessed through the Governance Action Engine OMAS.
             */
            String             accessServiceRootURL    = this.getAccessServiceRootURL(engineServiceConfig);
            String             accessServiceServerName = this.getAccessServiceServerName(engineServiceConfig);
            List<EngineConfig> governanceActionEngines = this.getEngines(engineServiceConfig);

            /*
             * Create the client for accessing the open metadata repositories.
             */
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
            }
            catch (InvalidParameterException error)
            {
                throw new OMAGConfigurationErrorException(error.getReportedErrorMessage(), error);
            }

            GovernanceEngineEventClient eventClient = new GovernanceEngineEventClient(accessServiceServerName,
                                                                                      accessServiceRootURL,
                                                                                      restClient,
                                                                                      maxPageSize,
                                                                                      auditLog,
                                                                                      localServerId);
            governanceListenerManager = new GovernanceListenerManager(eventClient);

            /*
             * Create a governance action handler for each of the governance action engines.
             */
            Map<String, GovernanceActionEngineHandler> governanceActionEngineHandlers = this.getGovernanceActionEngineHandlers(governanceActionEngines,
                                                                                                                               accessServiceRootURL,
                                                                                                                               accessServiceServerName,
                                                                                                                               localServerUserId,
                                                                                                                               configurationClient,
                                                                                                                               restClient,
                                                                                                                               governanceListenerManager,
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
     * Create the list of governance action engine handlers.
     *
     * @param governanceActionEngines list of governance action engines
     * @param accessServiceRootURL URL Root for the Governance Action Engine OMAS
     * @param accessServiceServerName Server Name for the Governance Action Engine OMAS
     * @param localServerUserId user id for this server to use if sending REST requests and processing inbound messages.
     * @param configurationClient client to retrieve configuration from
     * @param restClient client for calling REST APIs
     * @param governanceListenerManager listener manager for watchdog listeners
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     * @return map of governance action engine GUIDs to handlers
     * @throws OMAGConfigurationErrorException problem with config
     */
    private Map<String, GovernanceActionEngineHandler>  getGovernanceActionEngineHandlers(List<EngineConfig>                  governanceActionEngines,
                                                                                          String                              accessServiceRootURL,
                                                                                          String                              accessServiceServerName,
                                                                                          String                              localServerUserId,
                                                                                          GovernanceEngineConfigurationClient configurationClient,
                                                                                          GovernanceEngineRESTClient          restClient,
                                                                                          GovernanceListenerManager           governanceListenerManager,
                                                                                          int                                 maxPageSize) throws OMAGConfigurationErrorException
    {
        final String methodName        = "getGovernanceActionEngineHandlers";

        Map<String, GovernanceActionEngineHandler> governanceActionEngineHandlers = new HashMap<>();

        for (EngineConfig   governanceActionEngine : governanceActionEngines)
        {
            if (governanceActionEngine != null)
            {
                GovernanceEngineClient governanceEngineClient;
                try
                {
                    governanceEngineClient = new GovernanceEngineClient(accessServiceServerName,
                                                                        accessServiceRootURL,
                                                                        restClient,
                                                                        maxPageSize,
                                                                        auditLog);

                }
                catch (Throwable  error)
                {
                    /*
                     * Unable to create a client to the Governance Action Engine.  This is a config problem that is not possible to
                     * work around so shut down the server.
                     */
                    throw new OMAGConfigurationErrorException(GovernanceActionErrorCode.NO_GOVERNANCE_ACTION_ENGINE_CLIENT.getMessageDefinition(localServerName,
                                                                                                                                     governanceActionEngine.getEngineQualifiedName(),
                                                                                                                                     error.getClass().getName(),
                                                                                                                                     error.getMessage()),
                                                              this.getClass().getName(),
                                                              methodName);
                }

                /*
                 * Create a handler for the governance action engine.
                 */
                GovernanceActionEngineHandler  handler = new GovernanceActionEngineHandler(governanceActionEngine,
                                                                                           localServerName,
                                                                                           accessServiceServerName,
                                                                                           accessServiceRootURL,
                                                                                           localServerUserId,
                                                                                           configurationClient,
                                                                                           governanceEngineClient,
                                                                                           governanceListenerManager,
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
     * Shutdown the view service.
     */
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        auditLog.logMessage(actionDescription, GovernanceActionAuditCode.SERVER_SHUTTING_DOWN.getMessageDefinition(localServerName));

        governanceActionInstance.shutdown();
        governanceListenerManager.shutdown();

        auditLog.logMessage(actionDescription, GovernanceActionAuditCode.SERVER_SHUTDOWN.getMessageDefinition(localServerName));
    }
}
