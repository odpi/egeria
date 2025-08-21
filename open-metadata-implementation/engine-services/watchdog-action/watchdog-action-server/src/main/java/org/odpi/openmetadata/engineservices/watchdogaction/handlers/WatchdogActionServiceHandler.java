/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.watchdogaction.handlers;

import org.odpi.openmetadata.engineservices.watchdogaction.ffdc.WatchdogActionErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.watchdogaction.ffdc.WatchdogActionAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceConnector;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogContext;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * WatchdogActionServiceHandler provides the support to run a watchdog action service.
 * A new instance is created for each request, and it is assigned its
 * own thread.
 */
public class WatchdogActionServiceHandler extends GovernanceServiceHandler
{
    private final WatchdogActionServiceConnector watchdogActionService;
    private final WatchdogContext watchdogContext;
    private final String          notificationTypeGUID;

    /**
     * Constructor sets up the key parameters for running the watchdog action service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param watchdogActionEngineProperties properties of the watchdog action engine - used for message logging
     * @param watchdogActionEngineGUID unique Identifier of the watchdog action engine - used for message logging
     * @param engineHostUserId userId for making updates to the engine actions
     * @param engineActionGUID unique identifier of the governance action that triggered this governance service
     * @param engineActionClient client for processing governance actions
     * @param serviceRequestType requestType - used for message logging
     * @param watchdogActionServiceGUID name of this watchdog action service - used for message logging
     * @param watchdogActionServiceName name of this watchdog action service - used for message logging
     * @param watchdogActionServiceConnector connector that does the work
     * @param watchdogContext context for the connector
     * @param requestedStartDate date/time that the service should start
     * @param auditLog destination for log messages
     */
    WatchdogActionServiceHandler(GovernanceEngineProperties watchdogActionEngineProperties,
                                 String                     watchdogActionEngineGUID,
                                 String                     engineHostUserId,
                                 String                     engineActionGUID,
                                 GovernanceContextClient    engineActionClient,
                                 String                     serviceRequestType,
                                 String                     watchdogActionServiceGUID,
                                 String                     watchdogActionServiceName,
                                 Connector                  watchdogActionServiceConnector,
                                 WatchdogContext            watchdogContext,
                                 Date                       requestedStartDate,
                                 AuditLog                   auditLog) throws InvalidParameterException
    {
        super(watchdogActionEngineProperties,
              watchdogActionEngineGUID,
              engineHostUserId,
              engineActionGUID,
              engineActionClient,
              serviceRequestType,
              watchdogActionServiceGUID,
              watchdogActionServiceName,
              watchdogActionServiceConnector,
              requestedStartDate,
              auditLog);

        this.watchdogContext = watchdogContext;

        try
        {
            this.watchdogActionService = (WatchdogActionServiceConnector) watchdogActionServiceConnector;
            this.notificationTypeGUID  = watchdogContext.getNotificationTypeGUID();
        }
        catch (Exception error)
        {
            final String watchdogActionServiceConnectorParameterName = "watchdogActionServiceConnector";
            final String actionDescription = "Cast connector to WatchdogActionService";

            auditLog.logException(actionDescription,
                                  WatchdogActionAuditCode.INVALID_WATCHDOG_ACTION_SERVICE.getMessageDefinition(watchdogActionServiceName,
                                                                                                               serviceRequestType,
                                                                                                               error.getClass().getName(),
                                                                                                               error.getMessage()),
                                  error);
            throw new InvalidParameterException(WatchdogActionErrorCode.INVALID_WATCHDOG_SERVICE.getMessageDefinition(watchdogActionServiceName,
                                                                                                                      serviceRequestType,
                                                                                                                      error.getClass().getName(),
                                                                                                                      error.getMessage()),
                                                this.getClass().getName(),
                                                actionDescription,
                                                error,
                                                watchdogActionServiceConnectorParameterName);
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

        CompletionStatus          completionStatus;
        List<String>              completionGuards;
        AuditLogMessageDefinition completionMessage;
        Map<String, String>       completionRequestParameters;
        List<NewActionTarget>     completionActionTargets;

        final String actionDescription = "Monitor a notification type";

        try
        {
            super.waitForStartDate(engineHostUserId);

            auditLog.logMessage(actionDescription,
                                WatchdogActionAuditCode.WATCHDOG_ACTION_SERVICE_STARTING.getMessageDefinition(governanceServiceName,
                                                                                                              watchdogContext.getNotificationTypeGUID(),
                                                                                                              serviceRequestType,
                                                                                                              governanceEngineProperties.getQualifiedName(),
                                                                                                              governanceEngineGUID));

            watchdogActionService.setWatchdogContext(watchdogContext);
            watchdogActionService.setWatchdogActionServiceName(governanceServiceName);

            startTime = new Date();
            watchdogActionService.start();
            endTime = new Date();

            completionStatus            = watchdogContext.getCompletionStatus();
            completionGuards            = watchdogContext.getCompletionGuards();
            completionMessage           = watchdogContext.getCompletionMessage();
            completionRequestParameters = watchdogContext.getCompletionRequestParameters();
            completionActionTargets     = watchdogContext.getCompletionActionTargets();

            if (completionStatus == null)
            {
                completionStatus = WatchdogActionGuard.MONITORING_COMPLETED.getCompletionStatus();
            }

            if ((completionGuards == null) || (completionGuards.isEmpty()))
            {
                completionGuards = new ArrayList<>();
                completionGuards.add(WatchdogActionGuard.MONITORING_COMPLETED.getName());
            }

            if (completionMessage == null)
            {
                completionMessage = WatchdogActionAuditCode.WATCHDOG_ACTION_SERVICE_COMPLETE.getMessageDefinition(governanceServiceName,
                                                                                                                  watchdogContext.getNotificationTypeGUID(),
                                                                                                                  serviceRequestType,
                                                                                                                  Long.toString(endTime.getTime() - startTime.getTime()));
            }
        }
        catch (Exception  error)
        {
            completionStatus            = watchdogContext.getCompletionStatus();
            completionGuards            = watchdogContext.getCompletionGuards();
            completionMessage           = watchdogContext.getCompletionMessage();
            completionRequestParameters = watchdogContext.getCompletionRequestParameters();
            completionActionTargets     = watchdogContext.getCompletionActionTargets();

            if (completionStatus == null)
            {
                completionStatus = WatchdogActionGuard.MONITORING_FAILED.getCompletionStatus();
            }

            if ((completionGuards == null) || (completionGuards.isEmpty()))
            {
                completionGuards = new ArrayList<>();
                completionGuards.add(WatchdogActionGuard.MONITORING_FAILED.getName());
            }

            if (completionMessage == null)
            {
                completionMessage = WatchdogActionAuditCode.WATCHDOG_ACTION_SERVICE_FAILED.getMessageDefinition(governanceServiceName,
                                                                                                                error.getClass().getName(),
                                                                                                                notificationTypeGUID,
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

            MessageFormatter messageFormatter = new MessageFormatter();

            String messageText = messageFormatter.getFormattedMessage(completionMessage);

            auditLog.logMessage(actionDescription, completionMessage);

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
