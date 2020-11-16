/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers;

import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationConnectorConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationServiceSummary;

import java.util.ArrayList;
import java.util.List;


/**
 * IntegrationServiceHandler provides the support for a specific integration service running in the
 * integration daemon.  This handler is responsible for setting up its connectors and responding to
 * REST API calls.  The management of the threads where the connectors are running is handled by the
 * IntegrationDaemonHandler.
 */
public class IntegrationServiceHandler
{
    private String                    localServerName;               /* Initialized in constructor */
    private IntegrationServiceConfig  serviceConfig;                 /* Initialized in constructor */
    private IntegrationContextManager contextManager;                /* Initialized in constructor */
    private AuditLog                  auditLog;                      /* Initialized in constructor */

    private List<IntegrationConnectorHandler>  connectorHandlers = new ArrayList<>();


    /**
     * Constructor passes the service config. It is just saved at this point. Interesting things
     * start to happen on the initialize() call
     *
     * @param localServerName name of the local server
     * @param serviceConfig configuration for this specific integration service
     * @param contextManager context manager instance for this integration service
     * @param auditLog logging destination
     */
    public IntegrationServiceHandler(String                    localServerName,
                                     IntegrationServiceConfig  serviceConfig,
                                     IntegrationContextManager contextManager,
                                     AuditLog                  auditLog)
    {
        this.localServerName       = localServerName;
        this.serviceConfig         = serviceConfig;
        this.contextManager        = contextManager;
        this.auditLog              = auditLog;
    }


    /**
     * Create connector handlers for each of the connections listed in the configuration and
     * return them to the caller.  When the connector handlers are returned, the integration connectors
     * within them are fully initialized and have their context set.  They are ready to be called
     * for start() which is done by the integration daemon handler when they are on the correct thread.
     *
     * @return list of initialized connection handlers or null if no working connectors
     */
    public List<IntegrationConnectorHandler> initialize()
    {
        List<IntegrationConnectorConfig> connectorConfigurationList = serviceConfig.getIntegrationConnectorConfigs();

        if (connectorConfigurationList != null)
        {
            for (IntegrationConnectorConfig connectorConfig : connectorConfigurationList)
            {
                if (connectorConfig != null)
                {
                    if (connectorConfig.getPermittedSynchronization() == null)
                    {
                        connectorConfig.setPermittedSynchronization(serviceConfig.getDefaultPermittedSynchronization());
                    }

                    IntegrationConnectorHandler connectorHandler = new IntegrationConnectorHandler(connectorConfig,
                                                                                                   serviceConfig.getIntegrationServiceFullName(),
                                                                                                   serviceConfig.getIntegrationServiceOptions(),
                                                                                                   localServerName,
                                                                                                   contextManager,
                                                                                                   auditLog);

                    if (connectorHandler.getIntegrationConnectorStatus() != IntegrationConnectorStatus.FAILED)
                    {
                        connectorHandlers.add(connectorHandler);
                    }
                }
            }
        }

        if (connectorHandlers.isEmpty())
        {
            final String actionDescription = "Initialize integration service";

            connectorHandlers = null;

            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.NO_INTEGRATION_CONNECTORS.
                                        getMessageDefinition(serviceConfig.getIntegrationServiceFullName()));
        }

        return connectorHandlers;
    }


    /**
     * Retrieve the status of this integration service.
     *
     * @return Status summary for this integration service
     */
    public IntegrationServiceSummary getSummary()
    {
        IntegrationServiceSummary summary = new IntegrationServiceSummary();

        summary.setIntegrationServiceId(serviceConfig.getIntegrationServiceId());
        summary.setIntegrationServiceFullName(serviceConfig.getIntegrationServiceFullName());
        summary.setIntegrationServiceURLMarker(serviceConfig.getIntegrationServiceURLMarker());
        summary.setIntegrationServiceDescription(serviceConfig.getIntegrationServiceDescription());
        summary.setIntegrationServiceWiki(serviceConfig.getIntegrationServiceWiki());

        List<IntegrationConnectorReport>  connectorReports = new ArrayList<>();
        if (! connectorHandlers.isEmpty())
        {
            for (IntegrationConnectorHandler connectorHandler : connectorHandlers)
            {
                if (connectorHandler != null)
                {
                    IntegrationConnectorReport connectorReport = new IntegrationConnectorReport();

                    connectorReport.setConnectorName(connectorHandler.getIntegrationConnectorName());
                    connectorReport.setConnectorStatus(connectorHandler.getIntegrationConnectorStatus());
                    connectorReport.setFailingExceptionMessage(connectorHandler.getFailingExceptionMessage());
                    connectorReport.setStatistics(connectorHandler.getStatistics());
                    connectorReport.setLastStatusChange(connectorHandler.getLastStatusChange());
                    connectorReport.setLastRefreshTime(connectorHandler.getLastRefreshTime());
                    connectorReport.setMinSecondsBetweenRefresh(connectorHandler.getMinSecondsBetweenRefresh());

                    connectorReports.add(connectorReport);
                }
            }
        }

        if (! connectorReports.isEmpty())
        {
            summary.setIntegrationConnectorReports(connectorReports);
        }

        return summary;
    }



    /**
     * Refresh all of the connectors, or a specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void refreshService(String connectorName) throws InvalidParameterException
    {
        final String actionDescription = "Refresh service REST API call";

        if (connectorName == null)
        {
            for (IntegrationConnectorHandler connectorHandler : connectorHandlers)
            {
                if (connectorHandler != null)
                {
                    connectorHandler.refreshConnector(actionDescription);
                }
            }
        }
        else
        {
            for (IntegrationConnectorHandler connectorHandler : connectorHandlers)
            {
                if (connectorHandler != null)
                {
                    if (connectorName.equals(connectorHandler.getIntegrationConnectorName()))
                    {
                        connectorHandler.refreshConnector(actionDescription);
                        return;
                    }
                }
            }

            final String parameterName = "connectorName";

            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_CONNECTOR_NAME.getMessageDefinition(connectorName,
                                                                                                                               serviceConfig.getIntegrationServiceFullName(),
                                                                                                                               localServerName),
                                                this.getClass().getName(),
                                                actionDescription,
                                                parameterName);
        }
    }


    /**
     * Restart all of the connectors, or a specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void restartService(String connectorName) throws InvalidParameterException
    {
        final String actionDescription = "Restart service REST API call";

        if (connectorName == null)
        {
            for (IntegrationConnectorHandler connectorHandler : connectorHandlers)
            {
                if (connectorHandler != null)
                {
                    connectorHandler.reinitializeConnector(actionDescription);
                }
            }
        }
        else
        {
            for (IntegrationConnectorHandler connectorHandler : connectorHandlers)
            {
                if (connectorHandler != null)
                {
                    if (connectorName.equals(connectorHandler.getIntegrationConnectorName()))
                    {
                        connectorHandler.reinitializeConnector(actionDescription);
                        return;
                    }
                }
            }

            final String parameterName = "connectorName";

            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_CONNECTOR_NAME.getMessageDefinition(connectorName,
                                                                                                                               serviceConfig.getIntegrationServiceFullName(),
                                                                                                                               localServerName),
                                                this.getClass().getName(),
                                                actionDescription,
                                                parameterName);
        }
    }


    /**
     * Step through all of the connector handlers and disconnect the integration connectors
     */
    public void shutdown()
    {
        final String actionDescription = "Server shutdown";

        for (IntegrationConnectorHandler connectorHandler : connectorHandlers)
        {
            if (connectorHandler != null)
            {
                connectorHandler.shutdown(actionDescription);
            }
        }
    }
}
