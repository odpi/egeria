/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.watchdogaction.handlers;

import org.odpi.openmetadata.engineservices.watchdogaction.ffdc.WatchdogActionAuditCode;
import org.odpi.openmetadata.engineservices.watchdogaction.ffdc.WatchdogActionErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.opengovernance.controls.Guard;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceConnector;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogContext;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.Collections;
import java.util.Date;

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
            watchdogActionService = (WatchdogActionServiceConnector) watchdogActionServiceConnector;

            watchdogActionService.setWatchdogContext(watchdogContext);
            watchdogActionService.setAuditLog(auditLog);
            watchdogActionService.setWatchdogActionServiceName(governanceServiceName);

            /*
             * This tests the validity of the notification type
             */
            notificationTypeGUID  = watchdogContext.getNotificationTypeGUID();
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

            startTime = new Date();
            watchdogActionService.start();
            endTime = new Date();

            CompletionStatus completionStatus = watchdogContext.getCompletionStatus();

            if (completionStatus == null)
            {
                auditLog.logMessage(actionDescription,
                                    WatchdogActionAuditCode.WATCHDOG_ACTION_SERVICE_RETURNED.getMessageDefinition(governanceServiceName,
                                                                                                                  watchdogContext.getNotificationTypeGUID(),
                                                                                                                  serviceRequestType));
            }
            else
            {
                auditLog.logMessage(actionDescription,
                                    WatchdogActionAuditCode.WATCHDOG_ACTION_SERVICE_COMPLETE.getMessageDefinition(governanceServiceName,
                                                                                                                  watchdogContext.getNotificationTypeGUID(),
                                                                                                                  serviceRequestType,
                                                                                                                  Long.toString(endTime.getTime() - startTime.getTime())));
                super.disconnect();
            }
        }
        catch (Exception  error)
        {
            AuditLogMessageDefinition completionMessage = WatchdogActionAuditCode.WATCHDOG_ACTION_SERVICE_FAILED.getMessageDefinition(governanceServiceName,
                                                                                                                                      error.getClass().getName(),
                                                                                                                                      notificationTypeGUID,
                                                                                                                                      serviceRequestType,
                                                                                                                                      governanceEngineProperties.getQualifiedName(),
                                                                                                                                      governanceEngineGUID,
                                                                                                                                      error.getMessage());

            auditLog.logException(actionDescription, completionMessage, error.toString(), error);

            try
            {
                CompletionStatus completionStatus = watchdogContext.getCompletionStatus();

                if (completionStatus == null)
                {
                    watchdogContext.recordCompletionStatus(Guard.SERVICE_FAILED.getCompletionStatus(), Collections.singletonList(Guard.SERVICE_FAILED.getName()), null, null, completionMessage);
                    super.disconnect();
                }
            }
            catch (Exception statusError)
            {
                auditLog.logException(actionDescription,
                                      WatchdogActionAuditCode.EXC_ON_ERROR_STATUS_UPDATE.getMessageDefinition(governanceEngineProperties.getDisplayName(),
                                                                                                              governanceServiceName,
                                                                                                              statusError.getClass().getName(),
                                                                                                              statusError.getMessage()),
                                      statusError.toString(),
                                      statusError);
            }
        }
    }
}
