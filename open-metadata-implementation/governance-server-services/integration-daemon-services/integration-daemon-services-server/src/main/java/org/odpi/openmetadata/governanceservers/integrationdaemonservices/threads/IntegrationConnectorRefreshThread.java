/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.threads;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * IntegrationConnectorRefreshThread is the class responsible for managing executing refresh() calls to
 * a single integration connector within an integration daemon.  It manages the automated refresh of the connector.
 * The connector may also be refreshed through the REST API.
 */
public class IntegrationConnectorRefreshThread implements Runnable
{
    private static final Logger log = LoggerFactory.getLogger(IntegrationConnectorRefreshThread.class);

    private final String                            integrationDaemonName;
    private final IntegrationConnectorHandler       connectorHandler;
    private final AuditLog                          auditLog;


    private final AtomicBoolean running = new AtomicBoolean(false);


    /**
     * Constructor provides access to the variables needed to run the connector.
     *
     * @param integrationDaemonName name of this integration daemon server
     * @param connectorHandler wrapper for the connector.
     * @param auditLog logging destination
     */
    public IntegrationConnectorRefreshThread(String                      integrationDaemonName,
                                             IntegrationConnectorHandler connectorHandler,
                                             AuditLog                    auditLog)
    {
        this.integrationDaemonName = integrationDaemonName;
        this.connectorHandler      = connectorHandler;
        this.auditLog              = auditLog;
    }


    /**
     * Requests that the integration daemon thread starts
     */
    public void start()
    {
        final String threadName = "::IntegrationConnectorRefreshThread:" + connectorHandler.getIntegrationConnectorName();

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
                            IntegrationDaemonServicesAuditCode.REFRESH_THREAD_STARTING.getMessageDefinition(connectorHandler.getIntegrationConnectorName()));

        while (running.get())
        {
            Date now = new Date();

            if (((connectorHandler.getStartDate() == null) || now.after(connectorHandler.getStartDate())) &&
                ((connectorHandler.getStopDate() == null)  || now.before(connectorHandler.getStopDate())))
            {
                try
                {
                    Date lastRefreshTime = connectorHandler.getLastRefreshTime();
                    long minMinutedBetweenRefresh = connectorHandler.getMinMinutesBetweenRefresh();

                    if (lastRefreshTime == null)
                    {
                        connectorHandler.refreshConnector(actionDescription);
                    }
                    else if (minMinutedBetweenRefresh > 0)
                    {
                        long nextRefreshTime = lastRefreshTime.getTime() + (minMinutedBetweenRefresh * 60000);

                        if (nextRefreshTime < now.getTime())
                        {
                            connectorHandler.refreshConnector(actionDescription);
                        }
                    }
                }
                catch (Exception | NoClassDefFoundError error)
                {
                    auditLog.logException(actionDescription,
                                          IntegrationDaemonServicesAuditCode.REFRESH_THREAD_CONNECTOR_ERROR.getMessageDefinition(connectorHandler.getIntegrationConnectorName(),
                                                                                                                                 error.getClass().getName(),
                                                                                                                                 error.getMessage()),
                                          error);
                }
            }

            waitToRetry();
        }

        auditLog.logMessage(actionDescription,
                            IntegrationDaemonServicesAuditCode.REFRESH_THREAD_TERMINATING.getMessageDefinition(connectorHandler.getIntegrationConnectorName()));

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
