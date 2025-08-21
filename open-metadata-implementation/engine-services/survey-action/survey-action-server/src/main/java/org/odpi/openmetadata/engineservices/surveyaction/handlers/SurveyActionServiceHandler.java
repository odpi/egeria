/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.handlers;

import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyContext;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyActionGuard;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyActionTarget;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SurveyActionServiceHandler provides the support to run a survey action service.
 * A new instance is created for each request, and it is assigned its
 * own thread.
 */
public class SurveyActionServiceHandler extends GovernanceServiceHandler
{
    private final SurveyActionServiceConnector surveyActionService;
    private final SurveyContext                surveyContext;
    private final String                       assetGUID;

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
     * @param requestedStartDate date/time that the service should start
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
                               Date                       requestedStartDate,
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
              requestedStartDate,
              auditLog);

        this.surveyContext = surveyContext;

        try
        {
            this.surveyActionService = (SurveyActionServiceConnector) surveyActionServiceConnector;
            this.assetGUID           = surveyContext.getAssetGUID();
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

        CompletionStatus          completionStatus;
        List<String>              completionGuards;
        AuditLogMessageDefinition completionMessage;
        Map<String, String>       completionRequestParameters;
        List<NewActionTarget>     completionActionTargets;

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

            completionStatus            = surveyContext.getCompletionStatus();
            completionGuards            = surveyContext.getCompletionGuards();
            completionMessage           = surveyContext.getCompletionMessage();
            completionRequestParameters = surveyContext.getCompletionRequestParameters();
            completionActionTargets     = surveyContext.getCompletionActionTargets();

            if (completionStatus == null)
            {
                completionStatus = SurveyActionGuard.SURVEY_COMPLETED.getCompletionStatus();
            }

            if ((completionGuards == null) || (completionGuards.isEmpty()))
            {
                completionGuards = new ArrayList<>();
                completionGuards.add(SurveyActionGuard.SURVEY_COMPLETED.getName());
            }

            if (completionMessage == null)
            {
                completionMessage = SurveyActionAuditCode.SURVEY_ACTION_SERVICE_COMPLETE.getMessageDefinition(governanceServiceName,
                                                                                                              surveyContext.getAssetGUID(),
                                                                                                              serviceRequestType,
                                                                                                              Long.toString(endTime.getTime() - startTime.getTime()),
                                                                                                              surveyReportGUID);
            }
        }
        catch (Exception  error)
        {
            completionStatus            = surveyContext.getCompletionStatus();
            completionGuards            = surveyContext.getCompletionGuards();
            completionMessage           = surveyContext.getCompletionMessage();
            completionRequestParameters = surveyContext.getCompletionRequestParameters();
            completionActionTargets     = surveyContext.getCompletionActionTargets();

            if (completionStatus == null)
            {
                completionStatus = SurveyActionGuard.SURVEY_FAILED.getCompletionStatus();
            }

            if ((completionGuards == null) || (completionGuards.isEmpty()))
            {
                completionGuards = new ArrayList<>();
                completionGuards.add(SurveyActionGuard.SURVEY_FAILED.getName());
            }

            if (completionMessage == null)
            {
                completionMessage = SurveyActionAuditCode.SURVEY_ACTION_SERVICE_FAILED.getMessageDefinition(governanceServiceName,
                                                                                                            error.getClass().getName(),
                                                                                                            surveyReportGUID,
                                                                                                            assetGUID,
                                                                                                            serviceRequestType,
                                                                                                            governanceEngineProperties.getQualifiedName(),
                                                                                                            governanceEngineGUID,
                                                                                                            error.getMessage());
            }
        }

        try
        {
            if (completionActionTargets == null)
            {
                completionActionTargets = new ArrayList<>();
            }

            NewActionTarget reportActionTarget = new NewActionTarget();

            reportActionTarget.setActionTargetGUID(surveyReportGUID);
            reportActionTarget.setActionTargetName(SurveyActionTarget.SURVEY_REPORT.getName());

            completionActionTargets.add(reportActionTarget);

            MessageFormatter messageFormatter = new MessageFormatter();

            String messageText = messageFormatter.getFormattedMessage(completionMessage);

            auditLog.logMessage(actionDescription, completionMessage);
            surveyContext.getAnnotationStore().setCompletionMessage(messageText);

            super.recordCompletionStatus(completionStatus, completionGuards, completionRequestParameters, completionActionTargets, messageText);

            super.disconnect();
        }
        catch (Exception ignore)
        {
            /*
             * Ignore this exception - focus on first error.
             */
        }
    }
}
