/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers;

import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationConnectorConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnector;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.threads.IntegrationConnectorDedicatedThread;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * IntegrationConnectorReport provides information on the operation of a single connector within an integration
 * service.
 */
public class IntegrationConnectorHandler implements Serializable
{
    private static final long    serialVersionUID = 1L;

    /*
     * These values are set in the constructor and do not change.
     */
    private String                    integrationServiceFullName;
    private Map<String, Object>       integrationServiceOptions;
    private String                    integrationDaemonName;
    private String                    integrationConnectorId;
    private String                    integrationConnectorName;
    private String                    metadataSourceQualifiedName;
    private PermittedSynchronization  permittedSynchronization;
    private Connection                connection;
    private boolean                   needDedicatedThread;
    private long                      minMinutesBetweenRefresh;
    private IntegrationContextManager contextManager;
    private AuditLog                  auditLog;


    /*
     * These values change as the connector handler operates
     */
    private IntegrationConnector                integrationConnector                = null;
    private IntegrationConnectorDedicatedThread integrationConnectorDedicatedThread = null;
    private IntegrationConnectorStatus          integrationConnectorStatus          = null;
    private Date                                lastStatusChange                    = null;
    private String                              failingExceptionMessage             = null;
    private Map<String, Object>                 statistics                          = null;
    private Date                                lastRefreshTime                     = null;


    /**
     * Constructor creates the integration connector and manages it state.
     *
     * @param integrationConnectorConfig  the configuration for this connector
     * @param integrationServiceFullName full name of the integration service - used for messages
     * @param integrationServiceOptions options from the integration service configuration
     * @param integrationDaemonName name of the integration daemon - used for messages
     * @param contextManager the specialized context manager ofr this connector's integration service
     * @param auditLog logging destination
     */
    IntegrationConnectorHandler(IntegrationConnectorConfig integrationConnectorConfig,
                                String                     integrationServiceFullName,
                                Map<String, Object>        integrationServiceOptions,
                                String                     integrationDaemonName,
                                IntegrationContextManager  contextManager,
                                AuditLog                   auditLog)
    {
        final String actionDescription = "Initializing integration connector";

        this.integrationServiceFullName  = integrationServiceFullName;
        this.integrationServiceOptions   = integrationServiceOptions;
        this.integrationDaemonName       = integrationDaemonName;
        this.integrationConnectorId      = integrationConnectorConfig.getConnectorId();
        this.integrationConnectorName    = integrationConnectorConfig.getConnectorName();
        this.metadataSourceQualifiedName = integrationConnectorConfig.getMetadataSourceQualifiedName();
        this.minMinutesBetweenRefresh    = integrationConnectorConfig.getRefreshTimeInterval();
        this.connection                  = integrationConnectorConfig.getConnection();
        this.needDedicatedThread         = integrationConnectorConfig.getUsesBlockingCalls();
        this.permittedSynchronization    = integrationConnectorConfig.getPermittedSynchronization();
        this.contextManager              = contextManager;
        this.auditLog                    = auditLog;

        this.reinitializeConnector(actionDescription);
    }


    /**
     * Return the name of the connector.  This name is used for routing refresh calls to the connector as well
     * as being used for diagnostics.  Ideally it should be unique amongst the connectors for the integration service.
     *
     * @return String name
     */
    public String getIntegrationConnectorName()
    {
        return integrationConnectorName;
    }


    /**
     * Return the status for the integration connector.
     *
     * @return status object
     */
    IntegrationConnectorStatus getIntegrationConnectorStatus()
    {
        return integrationConnectorStatus;
    }


    /**
     * Return the date/time when the status was last changed.
     *
     * @return timestamp
     */
    Date getLastStatusChange()
    {
        return lastStatusChange;
    }


    /**
     * Return the message extracted from an exception returned by the connector.  This is only set if the connectorStatus
     * is FAILED.  The full exception is logged in the server's audit log.
     *
     * @return string message
     */
    String getFailingExceptionMessage()
    {
        return failingExceptionMessage;
    }


    /**
     * Return the statistics logged by the connector through the context.
     *
     * @return name value pairs for the statistics
     */
    Map<String, Object> getStatistics()
    {
        return statistics;
    }


    /**
     * Return the date/time when the connector was last refreshed.  Null means it has never been refreshed.
     *
     * @return timestamp
     */
    public Date getLastRefreshTime()
    {
        return lastRefreshTime;
    }


    /**
     * Return the configured minimum time between calls to refresh.  This gives an indication of when the
     * next refresh is due.  Null means refresh is only called at server start up and in response to an API request.
     *
     * @return count
     */
    public long getMinMinutesBetweenRefresh()
    {
        return minMinutesBetweenRefresh;
    }


    /**
     * Return the connector described in the connection object.
     *
     * @param connection connection contains properties for connector
     * @param actionDescription used in message
     * @return connector instance
     * @throws ConnectionCheckedException the connection is invalid
     * @throws ConnectorCheckedException the connector is not able to initialize properly
     */
    private Connector getConnector(Connection  connection,
                                   String      actionDescription) throws ConnectionCheckedException, ConnectorCheckedException
    {
        ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);

        try
        {
            return connectorBroker.getConnector(connection);
        }
        catch (ConnectionCheckedException | ConnectorCheckedException error)
        {
            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.BAD_INTEGRATION_CONNECTION.getMessageDefinition(integrationConnectorName,
                                                                                                                   integrationServiceFullName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()));
            throw error;
        }
    }


    /**
     * Initialize a new integration connector instance.  This is called from the constructor and during restart.
     *
     * @param actionDescription description of caller's operation
     */
    synchronized void reinitializeConnector(String    actionDescription)
    {
        final String operationName = "initialize";

        this.disconnectConnector(actionDescription);
        this.resetConnectorHandler();

        auditLog.logMessage(actionDescription,
                            IntegrationDaemonServicesAuditCode.INTEGRATION_CONNECTOR_INITIALIZING.getMessageDefinition(integrationConnectorName,
                                                                                                                       integrationServiceFullName,
                                                                                                                       integrationDaemonName,
                                                                                                                       permittedSynchronization.getName()));

        Connector genericConnector = null;

        try
        {
            genericConnector = this.getConnector(connection, actionDescription);

            integrationConnector = (IntegrationConnector)genericConnector;

            contextManager.setContext(integrationConnectorId,
                                      integrationConnectorName,
                                      metadataSourceQualifiedName,
                                      integrationConnector,
                                      permittedSynchronization,
                                      integrationServiceOptions);

            this.updateStatus(IntegrationConnectorStatus.INITIALIZED);

            if (needDedicatedThread)
            {
                integrationConnectorDedicatedThread = new IntegrationConnectorDedicatedThread(integrationDaemonName,
                                                                                              this,
                                                                                              auditLog);

                integrationConnectorDedicatedThread.start();
            }
        }
        catch (ClassCastException  error)
        {
            String connectorClassName = null;

            if (genericConnector != null)
            {
                connectorClassName = genericConnector.getClass().getName();
            }

            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.NOT_INTEGRATION_CONNECTOR.getMessageDefinition(integrationConnectorName,
                                                                                                                  connectorClassName,
                                                                                                                  IntegrationConnector.class.getCanonicalName()));

            processConnectorException(actionDescription, operationName, error);
        }
        catch (Exception  error)
        {
            processConnectorException(actionDescription, operationName, error);
        }
    }


    /**
     * Retrieve the configuration properties of the connector.
     *
     * @return property map
     */
    synchronized Map<String, Object> getConfigurationProperties()
    {
        if (connection != null)
        {
            return connection.getConfigurationProperties();
        }

        return null;
    }


    /**
     * Update the configuration properties of the connector and restart it.
     *
     * @param userId external caller
     * @param actionDescription external caller's activity
     * @param isMergeUpdate should the properties be merged into the existing properties or replace them
     * @param configurationProperties new configuration properties
     */
    synchronized void updateConfigurationProperties(String              userId,
                                                    String              actionDescription,
                                                    boolean             isMergeUpdate,
                                                    Map<String, Object> configurationProperties)
    {
        if (connection != null)
        {
            Map<String, Object>  connectionConfigurationProperties = connection.getConfigurationProperties();

            if (connectionConfigurationProperties != null)
            {
                if (isMergeUpdate)
                {
                    if (configurationProperties != null)
                    {
                        connectionConfigurationProperties.putAll(configurationProperties);
                    }
                }
                else
                {
                    connectionConfigurationProperties = configurationProperties;
                }
            }
            else
            {
                connectionConfigurationProperties = configurationProperties;
            }

            connection.setConfigurationProperties(connectionConfigurationProperties);

            if (connectionConfigurationProperties != null)
            {
                String propertyNames = "<null>";

                if ((configurationProperties != null) && (! configurationProperties.isEmpty()))
                {
                    propertyNames = configurationProperties.keySet().toString();
                }

                auditLog.logMessage(actionDescription,
                                    IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_CONFIG_PROPS_UPDATE.getMessageDefinition(userId,
                                                                                                                                 integrationConnectorName,
                                                                                                                                 integrationDaemonName,
                                                                                                                                 propertyNames));
            }
            else
            {
                auditLog.logMessage(actionDescription,
                                    IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_CONFIG_PROPS_CLEARED.getMessageDefinition(userId,
                                                                                                                                  integrationConnectorName,
                                                                                                                                  integrationDaemonName));
            }

            this.reinitializeConnector(actionDescription);
        }
    }


    /**
     * Call the engage() method of the integration connector.  This
     *
     * @param actionDescription external caller's activity
     */
    public synchronized void engageConnector(String   actionDescription)
    {
        final String operationName = "engage";

        try
        {
            if (integrationConnectorStatus == IntegrationConnectorStatus.INITIALIZED)
            {
                this.startConnector(actionDescription);
            }
            if (integrationConnectorStatus == IntegrationConnectorStatus.RUNNING)
            {
                integrationConnector.engage();
            }
        }
        catch (Exception error)
        {
            processConnectorException(actionDescription, operationName, error);
        }
    }


    /**
     * Call refresh on the connector provided it is in the correct state.
     *
     * @param actionDescription external caller's activity
     * @param firstCall is this the first call to refresh?
     */
    public synchronized void refreshConnector(String   actionDescription,
                                              boolean  firstCall)
    {
        final String operationName = "refresh";

        try
        {
            if (integrationConnectorStatus == IntegrationConnectorStatus.INITIALIZED)
            {
                this.startConnector(actionDescription);
            }
            if (integrationConnectorStatus == IntegrationConnectorStatus.RUNNING)
            {
                if (auditLog != null)
                {
                    if (firstCall)
                    {
                        auditLog.logMessage(actionDescription,
                                            IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_FIRST_REFRESH.getMessageDefinition(integrationConnectorName,
                                                                                                                                   integrationDaemonName));
                    }
                    else
                    {
                        auditLog.logMessage(actionDescription,
                                            IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_REFRESH.getMessageDefinition(integrationConnectorName,
                                                                                                                             integrationDaemonName));
                    }
                }

                integrationConnector.refresh();
            }

            this.lastRefreshTime = new Date();
        }
        catch (Exception error)
        {
            processConnectorException(actionDescription, operationName, error);
        }
    }


    /**
     * Shutdown the connector handler.
     *
     * @param actionDescription external caller's activity
     */
    public synchronized void shutdown(String   actionDescription)
    {
        this.disconnectConnector(actionDescription);
        this.resetConnectorHandler();
    }


    /**
     * Start the connector running.
     *
     * @param actionDescription external caller's activity
     */
    private void startConnector(String   actionDescription)
    {
        final String operationName = "start";

        try
        {
            if (integrationConnectorStatus == IntegrationConnectorStatus.INITIALIZED)
            {
                if (integrationConnector instanceof IntegrationConnectorBase)
                {
                    integrationConnector.setConnectorName(integrationConnectorName);
                }

                integrationConnector.start();
                updateStatus(IntegrationConnectorStatus.RUNNING);
            }
        }
        catch (Exception  error)
        {
            processConnectorException(actionDescription, operationName, error);
        }
    }


    /**
     * Request that the connector instance releases its resources.
     *
     * @param actionDescription external caller's activity
     */
    private void disconnectConnector(String   actionDescription)
    {
        final String operationName = "disconnect";

        try
        {
            if ((integrationConnectorStatus == IntegrationConnectorStatus.RUNNING) ||
                (integrationConnectorStatus == IntegrationConnectorStatus.FAILED))
            {
                integrationConnector.disconnect();
                updateStatus(IntegrationConnectorStatus.STOPPED);
            }
        }
        catch (Exception error)
        {
            processConnectorException(actionDescription, operationName, error);
        }
    }


    /**
     * This private method ensures that the instance variables are reset for a new connector instance.
     */
    private void resetConnectorHandler()
    {
        if (integrationConnectorDedicatedThread != null)
        {
            integrationConnectorDedicatedThread.stop();
        }

        this.updateStatus(null);
        this.integrationConnector                = null;
        this.integrationConnectorDedicatedThread = null;
        this.failingExceptionMessage             = null;
        this.statistics                          = null;
        this.lastRefreshTime                     = null;
    }


    /**
     * This private method ensures consistent logging of connector issues.
     *
     * @param actionDescription external caller's activity
     * @param operationName connector operation that failed
     * @param error resulting exception
     */
    private void processConnectorException(String     actionDescription,
                                           String     operationName,
                                           Exception  error)
    {
        updateStatus(IntegrationConnectorStatus.FAILED);
        failingExceptionMessage = error.getMessage();

        if (error instanceof OCFCheckedExceptionBase)
        {
            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.CONNECTOR_ERROR.getMessageDefinition(integrationConnectorName,
                                                                                                        operationName,
                                                                                                        error.getClass().getName(),
                                                                                                        error.getMessage()));
        }
        else
        {
            auditLog.logException(actionDescription,
                                  IntegrationDaemonServicesAuditCode.CONNECTOR_ERROR.getMessageDefinition(integrationConnectorName,
                                                                                                          operationName,
                                                                                                          error.getClass().getName(),
                                                                                                          error.getMessage()),
                                  error);
        }
    }


    /**
     * This private method makes sure that the timestamp is updated with the status.
     *
     * @param newStatus new value
     */
    private void updateStatus(IntegrationConnectorStatus  newStatus)
    {
        if (newStatus == null)
        {
            this.integrationConnectorStatus = null;
            this.lastStatusChange           = null;
        }
        else
        {
            this.integrationConnectorStatus = newStatus;
            this.lastStatusChange           = new Date();
        }
    }
}
