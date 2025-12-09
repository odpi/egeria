/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.watchdogaction.admin;

import org.odpi.openmetadata.governanceservers.enginehostservices.registration.EngineServiceDefinition;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.engineservices.watchdogaction.ffdc.WatchdogActionAuditCode;
import org.odpi.openmetadata.engineservices.watchdogaction.ffdc.WatchdogActionErrorCode;
import org.odpi.openmetadata.engineservices.watchdogaction.server.WatchdogActionInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;

/**
 * WatchdogActionAdmin is called during server start up to set up the Watchdog Action OMES.
 */
public class WatchdogActionAdmin extends EngineServiceAdmin
{
    private WatchdogActionInstance  watchdogActionInstance = null;

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
             * The watchdog action services need access to an open metadata server to retrieve information about the
             * notification types they are analysing and to send notifications to the subscribers
             * Open metadata is accessed through the metadata access store.
             */
            auditLog.logMessage(actionDescription, WatchdogActionAuditCode.ENGINE_SERVICE_INITIALIZING.getMessageDefinition(localServerName));

            /*
             * Set up the REST APIs.
             */
            watchdogActionInstance = new WatchdogActionInstance(localServerName,
                                                                EngineServiceDescription.WATCHDOG_ACTION_OMES.getEngineServiceName(),
                                                                auditLog,
                                                                localServerUserId,
                                                                maxPageSize);
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  WatchdogActionAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
                                  error.toString(),
                                  error);

            throw new OMAGConfigurationErrorException(WatchdogActionErrorCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
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

        auditLog.logMessage(actionDescription, WatchdogActionAuditCode.SERVER_SHUTTING_DOWN.getMessageDefinition(localServerName));

        watchdogActionInstance.shutdown();

        auditLog.logMessage(actionDescription, WatchdogActionAuditCode.SERVER_SHUTDOWN.getMessageDefinition(localServerName));
    }
}
