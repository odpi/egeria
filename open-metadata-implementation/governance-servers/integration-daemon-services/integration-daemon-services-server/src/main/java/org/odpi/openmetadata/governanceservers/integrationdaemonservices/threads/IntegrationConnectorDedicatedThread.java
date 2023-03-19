/* SPDX-License-Identifier: Apache 2.0 */
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
 * IntegrationConnectorDedicatedThread is the thread that runs a connector that issues
 */
public class IntegrationConnectorDedicatedThread implements Runnable
{
    private static final Logger log = LoggerFactory.getLogger(IntegrationConnectorDedicatedThread.class);

    private final String                      integrationDaemonName;
    private final IntegrationConnectorHandler connectorHandler;
    private final AuditLog                    auditLog;

    private final AtomicBoolean running = new AtomicBoolean(false);


    /**
     * Constructor provides access to the variables needed to run the connector.
     *
     * @param integrationDaemonName name of this integration daemon server
     * @param connectorHandler wrapper for the connector.
     * @param auditLog logging destination
     */
    public IntegrationConnectorDedicatedThread(String                      integrationDaemonName,
                                               IntegrationConnectorHandler connectorHandler,
                                               AuditLog                    auditLog)
    {
        this.integrationDaemonName = integrationDaemonName;
        this.connectorHandler      = connectorHandler;
        this.auditLog              = auditLog;
    }


    /**
     * Requests that the integration connector dedicated thread starts
     */
    public void start()
    {
        final String threadName = "::DedicatedConnectorThread::";

        Thread worker = new Thread(this, integrationDaemonName + threadName + connectorHandler.getIntegrationConnectorName());
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
     * Called when the new thread is running
     */
    @Override
    public void run()
    {
        final String actionDescription = "Run dedicated connector thread";

        running.set(true);

        auditLog.logMessage(actionDescription,
                            IntegrationDaemonServicesAuditCode.CONNECTOR_THREAD_STARTING.getMessageDefinition(connectorHandler.getIntegrationConnectorName(),
                                                                                                              integrationDaemonName));

        while (running.get())
        {
            Date now = new Date();

            if (((connectorHandler.getStartDate() == null) || now.after(connectorHandler.getStartDate())) &&
                ((connectorHandler.getStopDate() == null)  || now.before(connectorHandler.getStopDate())))
            {
                connectorHandler.engageConnector(actionDescription);

                auditLog.logMessage(actionDescription,
                                    IntegrationDaemonServicesAuditCode.ENGAGE_RETURNED.getMessageDefinition(connectorHandler.getIntegrationConnectorName(),
                                                                                                            integrationDaemonName));

            }

            waitToRetry();
        }

        auditLog.logMessage(actionDescription,
                            IntegrationDaemonServicesAuditCode.CONNECTOR_THREAD_TERMINATING.getMessageDefinition(connectorHandler.getIntegrationConnectorName(),
                                                                                                                 integrationDaemonName));
    }


    /**
     * Wait before retrying ...
     */
    private void waitToRetry()
    {
        long  sleepTime = 1000;

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
