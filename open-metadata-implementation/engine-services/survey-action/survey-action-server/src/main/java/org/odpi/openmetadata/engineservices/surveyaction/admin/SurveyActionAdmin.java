/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.engineservices.surveyaction.server.SurveyActionInstance;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionAuditCode;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;

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
     * @param engineServiceConfig details of the options and the engines to run
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
             * The survey action services need access to an open metadata server to retrieve information about the asset they are analysing and
             * to store the resulting analysis results in Annotations.
             * Open metadata is accessed through the metadata access server.
             */
            String             accessServiceRootURL    = this.getPartnerServiceRootURL(engineServiceConfig);
            String             accessServiceServerName = this.getPartnerServiceServerName(engineServiceConfig);

            auditLog.logMessage(actionDescription, SurveyActionAuditCode.ENGINE_SERVICE_INITIALIZING.getMessageDefinition(localServerName,
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
            surveyActionInstance = new SurveyActionInstance(localServerName,
                                                            EngineServiceDescription.SURVEY_ACTION_OMES.getEngineServiceName(),
                                                            auditLog,
                                                            localServerUserId,
                                                            maxPageSize,
                                                            engineServiceConfig.getOMAGServerPlatformRootURL(),
                                                            engineServiceConfig.getOMAGServerName());

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
     * Shutdown the engine service.
     */
    @Override
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        auditLog.logMessage(actionDescription, SurveyActionAuditCode.SERVER_SHUTTING_DOWN.getMessageDefinition(localServerName));

        surveyActionInstance.shutdown();

        auditLog.logMessage(actionDescription, SurveyActionAuditCode.SERVER_SHUTDOWN.getMessageDefinition(localServerName));
    }
}
