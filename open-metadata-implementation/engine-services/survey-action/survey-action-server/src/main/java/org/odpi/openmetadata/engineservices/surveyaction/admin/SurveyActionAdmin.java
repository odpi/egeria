/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.admin;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionAuditCode;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionErrorCode;
import org.odpi.openmetadata.engineservices.surveyaction.server.SurveyActionInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.EngineServiceDefinition;

/**
 * SurveyActionAdmin is called during server start up to set up the Survey Action OMES.
 */
public class SurveyActionAdmin extends EngineServiceAdmin
{
    private SurveyActionInstance surveyActionInstance = null;

    /**
     * Initialize engine service.
     *
     * @param localServerName name of this server
     * @param auditLog link to the repository responsible for servicing the REST calls
     * @param localServerUserId user id for this server to use if sending REST requests and processing inbound messages
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     * @param engineServiceDefinition details of the options and the engines to run
     * @throws OMAGConfigurationErrorException an issue in the configuration prevented initialization
     */
    @Override
    public void initialize(String                  localServerName,
                           AuditLog                auditLog,
                           String                  localServerUserId,
                           int                     maxPageSize,
                           EngineServiceDefinition engineServiceDefinition) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize engine service";
        final String methodName        = "initialize";

        this.auditLog = auditLog;
        this.localServerName = localServerName;

        try
        {
            this.validateEngineDefinition(engineServiceDefinition);

            /*
             * The survey action services need access to an open metadata server to retrieve information about the asset they are analysing and
             * to store the resulting analysis results in Annotations.
             * Open metadata is accessed through the metadata access server.
             */
            auditLog.logMessage(actionDescription, SurveyActionAuditCode.ENGINE_SERVICE_INITIALIZING.getMessageDefinition(localServerName));


            /*
             * Set up the REST APIs.
             */
            surveyActionInstance = new SurveyActionInstance(localServerName,
                                                            EngineServiceDescription.SURVEY_ACTION_OMES.getEngineServiceName(),
                                                            auditLog,
                                                            localServerUserId,
                                                            maxPageSize);

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
