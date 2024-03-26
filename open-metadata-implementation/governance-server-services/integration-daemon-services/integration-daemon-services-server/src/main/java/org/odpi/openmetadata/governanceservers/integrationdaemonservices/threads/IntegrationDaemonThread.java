/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.threads;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorCacheMap;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * IntegrationDaemonThread is the class responsible for managing executing integration connectors
 * within an integration daemon.  It manages the automated refresh of the connectors.
 * The connectors are also being refreshed through the REST API.
 */
public class IntegrationDaemonThread implements Runnable
{
    private static final Logger log = LoggerFactory.getLogger(IntegrationDaemonThread.class);

    private final String                            integrationDaemonName;
    private final IntegrationConnectorCacheMap      connectorHandlers;
    private final AuditLog                          auditLog;


    private final AtomicBoolean running = new AtomicBoolean(false);


    /**
     * Constructor provides access to the variables needed to run the connector.
     *
     * @param integrationDaemonName name of this integration daemon server
     * @param connectorHandlers wrapper for the connector.
     * @param auditLog logging destination
     */
    public IntegrationDaemonThread(String                       integrationDaemonName,
                                   IntegrationConnectorCacheMap connectorHandlers,
                                   AuditLog                     auditLog)
    {
        this.integrationDaemonName = integrationDaemonName;
        this.connectorHandlers     = connectorHandlers;
        this.auditLog              = auditLog;
    }


    /**
     * Requests that the integration daemon thread starts
     */
    public void start()
    {
        final String threadName = "::IntegrationDaemonThread";

        Thread worker = new Thread(this, integrationDaemonName + threadName);
        worker.start();
    }


    /**
     * Requests that the integration daemon thread shuts down.
     */
    public void stop()
    {
        running.set(false);
    }


    /**
     * This is the method that runs in the new thread when it is started.
     */
    @Override
    public void run()
    {
        final String actionDescription = "Periodic refresh of connector";

        running.set(true);

        auditLog.logMessage(actionDescription,
                            IntegrationDaemonServicesAuditCode.DAEMON_THREAD_STARTING.getMessageDefinition(integrationDaemonName));

        while (running.get())
        {
            Date now = new Date();

            List<IntegrationConnectorHandler> integrationConnectorHandlers = connectorHandlers.getIntegrationConnectorProcessingList();

            if (integrationConnectorHandlers != null)
            {
                for (IntegrationConnectorHandler connectorHandler : connectorHandlers.getIntegrationConnectorProcessingList())
                {
                    if (connectorHandler != null)
                    {
                        if (((connectorHandler.getStartDate() == null) || now.after(connectorHandler.getStartDate())) &&
                            ((connectorHandler.getStopDate() == null)  || now.before(connectorHandler.getStopDate())))
                        {
                            try
                            {
                                if (connectorHandler.getLastRefreshTime() == null)
                                {
                                    connectorHandler.refreshConnector(actionDescription, true);
                                }
                                else if (connectorHandler.getMinMinutesBetweenRefresh() > 0)
                                {
                                    long nextRefreshTime =
                                            connectorHandler.getLastRefreshTime().getTime() +
                                                    (connectorHandler.getMinMinutesBetweenRefresh() * 60000);

                                    if (nextRefreshTime < now.getTime())
                                    {
                                        connectorHandler.refreshConnector(actionDescription, false);
                                    }
                                }
                            }
                            catch (Exception error)
                            {
                                auditLog.logMessage(actionDescription,
                                                    IntegrationDaemonServicesAuditCode.DAEMON_THREAD_CONNECTOR_ERROR.getMessageDefinition(integrationDaemonName,
                                                                                                                                          error.getClass().getName(),
                                                                                                                                          error.getMessage()));
                            }
                        }
                    }
                }
            }

            waitToRetry();
        }

        auditLog.logMessage(actionDescription,
                            IntegrationDaemonServicesAuditCode.DAEMON_THREAD_TERMINATING.getMessageDefinition(integrationDaemonName));

    }


    /**
     * Wait before retrying ...
     */
    private void waitToRetry()
    {
        final int  sleepTime = 1000;

        try
        {
            Thread.sleep(sleepTime);
        }
        catch (Exception error)
        {
            log.error("Ignored exception from sleep - probably ok", error);
        }
    }
}
