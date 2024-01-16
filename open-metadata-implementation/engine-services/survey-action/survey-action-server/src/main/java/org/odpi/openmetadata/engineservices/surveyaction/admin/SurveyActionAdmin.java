/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.admin;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceContextClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.stewardshipaction.client.ConnectedAssetClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionAuditCode;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionErrorCode;
import org.odpi.openmetadata.engineservices.surveyaction.handlers.SurveyActionEngineHandler;
import org.odpi.openmetadata.engineservices.surveyaction.server.SurveyActionInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SurveyActionAdmin is called during server start up to set up the Survey Action OMES.
 */
public class SurveyActionAdmin extends EngineServiceAdmin
{
    private SurveyActionInstance surveyActionInstance = null;

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
                                                           GovernanceContextClient             governanceActionClient,
                                                           EngineServiceConfig                 engineServiceConfig) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize engine service";
        final String methodName        = "initialize";

        this.auditLog = auditLog;
        this.localServerName = localServerName;

        auditLog.logMessage(actionDescription, SurveyActionAuditCode.ENGINE_SERVICE_INITIALIZING.getMessageDefinition(localServerName));

        try
        {
            this.validateConfigDocument(engineServiceConfig);

            /*
             * The survey action services need access to an open metadata server to retrieve information about the asset they are analysing and
             * to store the resulting analysis results in Annotations.
             * Open metadata is accessed through the Stewardship Action OMAS.
             */
            String             accessServiceRootURL    = this.getPartnerServiceRootURL(engineServiceConfig);
            String             accessServiceServerName = this.getPartnerServiceServerName(engineServiceConfig);
            List<EngineConfig> surveyActionEngines        = this.getEngines(engineServiceConfig);

            /*
             * Create a survey action handler for each of the survey action engines.
             */
            Map<String, SurveyActionEngineHandler> surveyActionEngineHandlers = this.getSurveyActionEngineHandlers(surveyActionEngines,
                                                                                                                   accessServiceRootURL,
                                                                                                                   accessServiceServerName,
                                                                                                                   localServerUserId,
                                                                                                                   localServerPassword,
                                                                                                                   configurationClient,
                                                                                                                   governanceActionClient,
                                                                                                                   maxPageSize);

            if (surveyActionEngineHandlers == null)
            {
                auditLog.logMessage(actionDescription, SurveyActionAuditCode.NO_SURVEY_ACTION_ENGINES_STARTED.getMessageDefinition(localServerName));

                throw new OMAGConfigurationErrorException(SurveyActionErrorCode.NO_SURVEY_ENGINES_STARTED.getMessageDefinition(localServerName),
                                                          this.getClass().getName(),
                                                          methodName);
            }


            /*
             * Set up the REST APIs.
             */
            surveyActionInstance = new SurveyActionInstance(localServerName,
                                                            EngineServiceDescription.SURVEY_ACTION_OMES.getEngineServiceName(),
                                                            auditLog,
                                                            localServerUserId,
                                                            maxPageSize,
                                                            engineServiceConfig.getOMAGServerPlatformRootURL(),
                                                            engineServiceConfig.getOMAGServerName(),
                                                            surveyActionEngineHandlers);

            Map<String, GovernanceEngineHandler> governanceEngineHandlers = new HashMap<>();

            for (String engineName : surveyActionEngineHandlers.keySet())
            {
                if (engineName != null)
                {
                    governanceEngineHandlers.put(engineName, surveyActionEngineHandlers.get(engineName));
                }
            }

            return governanceEngineHandlers;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  SurveyActionAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
                                  error.toString(),
                                  error);

            throw new OMAGConfigurationErrorException(SurveyActionErrorCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      error);
        }
    }


    /**
     * Create the list of survey action engine handlers.
     *
     * @param surveyActionEngines list of survey action engines
     * @param accessServiceRootURL URL Root for the platform where Stewardship Action OMAS is running
     * @param accessServiceServerName Server Name where Stewardship Action OMAS is running
     * @param localServerUserId user id for this server to use if sending REST requests and processing inbound messages.
     * @param localServerPassword password for this server to use if sending REST requests
     * @param configurationClient client to retrieve configuration from
     * @param engineActionClient client used by the engine host services to connect to the Governance Engine OMAS to manage engine actions
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     * @return map of survey action engine GUIDs to handlers
     * @throws OMAGConfigurationErrorException problem with config
     */
    private Map<String, SurveyActionEngineHandler> getSurveyActionEngineHandlers(List<EngineConfig>                  surveyActionEngines,
                                                                                 String                              accessServiceRootURL,
                                                                                 String                              accessServiceServerName,
                                                                                 String                              localServerUserId,
                                                                                 String                              localServerPassword,
                                                                                 GovernanceEngineConfigurationClient configurationClient,
                                                                                 GovernanceContextClient             engineActionClient,
                                                                                 int                                 maxPageSize) throws OMAGConfigurationErrorException
    {
        final String methodName = "getSurveyActionEngineHandlers";

        Map<String, SurveyActionEngineHandler> surveyActionEngineHandlers = new HashMap<>();

        for (EngineConfig   surveyActionEngine : surveyActionEngines)
        {
            if (surveyActionEngine != null)
            {
                ConnectedAssetClient surveyActionEngineClient;
                OpenMetadataClient   openMetadataClient;
                try
                {
                    if (localServerPassword == null)
                    {
                        surveyActionEngineClient = new ConnectedAssetClient(accessServiceServerName,
                                                                            accessServiceRootURL);

                        openMetadataClient = new OpenMetadataStoreClient(accessServiceServerName,
                                                                         accessServiceRootURL);
                    }
                    else
                    {
                        surveyActionEngineClient = new ConnectedAssetClient(accessServiceServerName,
                                                                            accessServiceRootURL,
                                                                            localServerUserId,
                                                                            localServerPassword);

                        openMetadataClient = new OpenMetadataStoreClient(accessServiceServerName,
                                                                         accessServiceRootURL,
                                                                         localServerUserId,
                                                                         localServerPassword);
                    }
                }
                catch (Exception  error)
                {
                    /*
                     * Unable to create a client to the Discovery Engine.  This is a config problem that is not possible to
                     * work around so shut down the server.
                     */
                    throw new OMAGConfigurationErrorException(SurveyActionErrorCode.NO_STEWARDSHIP_ACTION_CLIENT.getMessageDefinition(localServerName,
                                                                                                                                      surveyActionEngine.getEngineQualifiedName(),
                                                                                                                                      error.getClass().getName(),
                                                                                                                                      error.getMessage()),
                                                              this.getClass().getName(),
                                                              methodName);
                }

                /*
                 * Create a handler for the survey action engine.
                 */
                SurveyActionEngineHandler handler = new SurveyActionEngineHandler(surveyActionEngine,
                                                                                  accessServiceServerName,
                                                                                  localServerUserId,
                                                                                  configurationClient,
                                                                                  engineActionClient,
                                                                                  surveyActionEngineClient,
                                                                                  openMetadataClient,
                                                                                  auditLog,
                                                                                  maxPageSize);

                surveyActionEngineHandlers.put(surveyActionEngine.getEngineQualifiedName(), handler);
            }
        }

        if (surveyActionEngineHandlers.isEmpty())
        {
            return null;
        }
        else
        {
            return surveyActionEngineHandlers;
        }
    }


    /**
     * Shutdown the engine service.
     */
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        auditLog.logMessage(actionDescription, SurveyActionAuditCode.SERVER_SHUTTING_DOWN.getMessageDefinition(localServerName));

        surveyActionInstance.shutdown();

        auditLog.logMessage(actionDescription, SurveyActionAuditCode.SERVER_SHUTDOWN.getMessageDefinition(localServerName));
    }
}
