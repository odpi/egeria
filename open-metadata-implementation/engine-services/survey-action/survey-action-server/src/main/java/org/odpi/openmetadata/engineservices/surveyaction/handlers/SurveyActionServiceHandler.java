/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.handlers;

import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceContextClient;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyContext;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyActionGuard;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SurveyActionServiceHandler provides the support to run a survey action service.  A new instance is created for each request, and it is assigned its
 * own thread.
 */
public class SurveyActionServiceHandler extends GovernanceServiceHandler
{
    private final SurveyActionServiceConnector surveyActionService;
    private final SurveyContext surveyContext;

    /**
     * Constructor sets up the key parameters for running the survey action service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param surveyActionEngineProperties properties of the survey action engine - used for message logging
     * @param surveyActionEngineGUID unique Identifier of the survey action engine - used for message logging
     * @param engineHostUserId userId for making updates to the engine actions
     * @param engineActionGUID unique identifier of the governance action that triggered this governance service
     * @param engineActionClient client for processing governance actions
     * @param serviceRequestType requestType - used for message logging
     * @param surveyActionServiceGUID name of this survey action service - used for message logging
     * @param surveyActionServiceName name of this survey action service - used for message logging
     * @param surveyActionServiceConnector connector that does the work
     * @param surveyContext context for the connector
     * @param startDate date/time that the service should start
     * @param auditLog destination for log messages
     */
    SurveyActionServiceHandler(GovernanceEngineProperties surveyActionEngineProperties,
                               String                     surveyActionEngineGUID,
                               String                     engineHostUserId,
                               String                     engineActionGUID,
                               GovernanceContextClient    engineActionClient,
                               String                     serviceRequestType,
                               String                     surveyActionServiceGUID,
                               String                     surveyActionServiceName,
                               Connector                  surveyActionServiceConnector,
                               SurveyContext              surveyContext,
                               Date                       startDate,
                               AuditLog                   auditLog) throws InvalidParameterException
    {
        super(surveyActionEngineProperties,
              surveyActionEngineGUID,
              engineHostUserId,
              engineActionGUID,
              engineActionClient,
              serviceRequestType,
              surveyActionServiceGUID,
              surveyActionServiceName,
              surveyActionServiceConnector,
              startDate,
              auditLog);

        this.surveyContext = surveyContext;

        try
        {
            this.surveyActionService = (SurveyActionServiceConnector) surveyActionServiceConnector;
        }
        catch (Exception error)
        {
            final String surveyActionServiceConnectorParameterName = "surveyActionServiceConnector";
            final String actionDescription = "Cast connector to SurveyActionService";

            auditLog.logException(actionDescription,
                                  SurveyActionAuditCode.INVALID_SURVEY_ACTION_SERVICE.getMessageDefinition(surveyActionServiceName,
                                                                                                           serviceRequestType,
                                                                                                           error.getClass().getName(),
                                                                                                           error.getMessage()),
                                  error);
            throw new InvalidParameterException(SurveyActionErrorCode.INVALID_SURVEY_SERVICE.getMessageDefinition(surveyActionServiceName,
                                                                                                                  serviceRequestType,
                                                                                                                  error.getClass().getName(),
                                                                                                                  error.getMessage()),
                                                this.getClass().getName(),
                                                actionDescription,
                                                error,
                                                surveyActionServiceConnectorParameterName);
        }
    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        Date startTime;
        Date endTime;
        String surveyReportGUID = null;


        final String actionDescription = "Survey an Asset";

        try
        {
            super.waitForStartDate(engineHostUserId);

            surveyReportGUID = surveyContext.getAnnotationStore().getSurveyReportGUID();
            auditLog.logMessage(actionDescription,
                                SurveyActionAuditCode.SURVEY_ACTION_SERVICE_STARTING.getMessageDefinition(governanceServiceName,
                                                                                                          surveyContext.getAssetGUID(),
                                                                                                          serviceRequestType,
                                                                                                          governanceEngineProperties.getQualifiedName(),
                                                                                                          governanceEngineGUID,
                                                                                                          surveyReportGUID));

            surveyActionService.setSurveyContext(surveyContext);
            surveyActionService.setSurveyActionServiceName(governanceServiceName);

            startTime = new Date();
            surveyActionService.start();
            surveyContext.getAnnotationStore().setCompletionMessage(null);
            endTime = new Date();

            auditLog.logMessage(actionDescription,
                                SurveyActionAuditCode.SURVEY_ACTION_SERVICE_COMPLETE.getMessageDefinition(governanceServiceName,
                                                                                                          surveyContext.getAssetGUID(),
                                                                                                          serviceRequestType,
                                                                                                          Long.toString(endTime.getTime() - startTime.getTime()),
                                                                                                          surveyReportGUID));

            super.disconnect();
            surveyActionService.setSurveyContext(null);

            List<String> outputGuards = new ArrayList<>();
            outputGuards.add(SurveyActionGuard.SURVEY_COMPLETED.getName());

            List<NewActionTarget> newActionTargets = new ArrayList<>();
            NewActionTarget newActionTarget = new NewActionTarget();

            newActionTarget.setActionTargetGUID(surveyReportGUID);
            newActionTarget.setActionTargetName("surveyReport");

            newActionTargets.add(newActionTarget);

            super.recordCompletionStatus(CompletionStatus.ACTIONED, outputGuards, null, newActionTargets, "SUCCESS");
        }
        catch (Exception  error)
        {
            try
            {
                /*
                 * Try to log the completion message - may not work
                 */
                surveyContext.getAnnotationStore().setCompletionMessage(error.getMessage());

                List<String> outputGuards = new ArrayList<>();
                outputGuards.add(SurveyActionGuard.SURVEY_FAILED.getName());

                super.recordCompletionStatus(CompletionStatus.FAILED, outputGuards, null, null, error.getMessage());

                auditLog.logException(actionDescription,
                                      SurveyActionAuditCode.SURVEY_ACTION_SERVICE_FAILED.getMessageDefinition(governanceServiceName,
                                                                                                              error.getClass().getName(),
                                                                                                              surveyReportGUID,
                                                                                                              surveyContext.getAssetGUID(),
                                                                                                              serviceRequestType,
                                                                                                              governanceEngineProperties.getQualifiedName(),
                                                                                                              governanceEngineGUID,
                                                                                                              error.getMessage()),
                                      error.toString(),
                                      error);
            }
            catch (Exception ignore)
            {
                /*
                 * Ignore this exception - focus on first error.
                 */
            }

        }
    }
}
