/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnector;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContextRefreshProxy;
import org.odpi.openmetadata.frameworks.integration.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.integration.properties.RegisteredIntegrationConnectorElement;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.threads.IntegrationConnectorDedicatedThread;

import java.util.Date;
import java.util.Map;


/**
 * IntegrationConnectorReport provides information on the operation of a single connector within an integration
 * service.
 */
public class IntegrationConnectorHandler
{
    /*
     * These values are set in the constructor and do not change.
     */
    private final String                    integrationServiceFullName;
    private final String                    integrationDaemonName;
    private final String                    integrationConnectorId;
    private       String                    integrationConnectorGUID;
    private       String                    integrationConnectorName;
    private       String                    integrationConnectorUserId;
    private       Date                      startDate;
    private       Date                      stopDate;
    private       String                    metadataSourceQualifiedName;
    private       PermittedSynchronization  permittedSynchronization;
    private final boolean                   generateIntegrationReport;
    private       Connection                connection;
    private       boolean                   needDedicatedThread;
    private       long                      minMinutesBetweenRefresh;
    private final IntegrationContextManager contextManager;
    private final AuditLog                  auditLog;





    /*
     * These values change as the connector handler operates
     */
    private IntegrationContext                  integrationContext                  = null;
    private IntegrationContextRefreshProxy      integrationContextRefreshProxy      = null;
    private Connector                           genericConnector                    = null;
    private IntegrationConnector                integrationConnector                = null;
    private IntegrationConnectorDedicatedThread integrationConnectorDedicatedThread = null;
    private IntegrationConnectorStatus          integrationConnectorStatus          = null;
    private Date                                lastStatusChange                    = null;
    private String                              failingExceptionMessage             = null;
    private Date                                lastRefreshTime                     = null;


    /**
     * Constructor creates the integration connector and manages it state.
     *
     * @param connectorId identifier of the connector (for controlling event receipts)
     * @param connectorGUID unique identifier of metadata entity for the connector (maybe null)
     * @param connectorName name of the connector (for logging)
     * @param connectorUserId userId to query and maintain metadata on behalf of this connector
     * @param startDate earliest time that the connector can run.
     * @param stopDate latest time that the connector can run.
     * @param minMinutesBetweenRefresh minimum number of minutes between each call to the connector's refresh() method
     * @param metadataSourceQualifiedName qualified name of software capability entity describing metadata source being synchronized
     * @param connection connection used to create the integration connector
     * @param usesBlockingCalls does the connector use blocking calls (in which case engage() is called in a dedicated thread, rather than the
     *                          refresh() method).
     * @param permittedSynchronization what is the direction that metadata can be synchronized - affects the methods available through the context
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param integrationServiceFullName full name of the integration service - used for messages
     * @param integrationDaemonName name of the integration daemon - used for messages
     * @param contextManager the specialized context manager for this connector's integration service
     * @param auditLog logging destination
     */
    IntegrationConnectorHandler(String                     connectorId,
                                String                     connectorGUID,
                                String                     connectorName,
                                String                     connectorUserId,
                                Date                       startDate,
                                Date                       stopDate,
                                long                       minMinutesBetweenRefresh,
                                String                     metadataSourceQualifiedName,
                                Connection                 connection,
                                boolean                    usesBlockingCalls,
                                PermittedSynchronization   permittedSynchronization,
                                boolean                    generateIntegrationReport,
                                String                     integrationServiceFullName,
                                String                     integrationDaemonName,
                                IntegrationContextManager  contextManager,
                                AuditLog                   auditLog)
    {
        final String actionDescription = "Initializing integration connector";

        this.integrationServiceFullName  = integrationServiceFullName;
        this.integrationDaemonName       = integrationDaemonName;
        this.integrationConnectorId      = connectorId;
        this.integrationConnectorGUID    = connectorGUID;
        this.integrationConnectorName    = connectorName;
        this.integrationConnectorUserId  = connectorUserId;
        this.startDate                   = startDate;
        this.stopDate                    = stopDate;
        this.metadataSourceQualifiedName = metadataSourceQualifiedName;
        this.minMinutesBetweenRefresh    = minMinutesBetweenRefresh;
        this.connection                  = connection;
        this.needDedicatedThread         = usesBlockingCalls;
        this.permittedSynchronization    = permittedSynchronization;
        this.generateIntegrationReport   = generateIntegrationReport;
        this.contextManager              = contextManager;
        this.auditLog                    = auditLog;

        this.reinitializeConnector(actionDescription);
    }


    /**
     * Return the unique identifier of the connector from the configuration or the integration group definition.
     *
     * @return String name
     */
    public String getIntegrationConnectorId()
    {
        return integrationConnectorId;
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
     * Return the connection used to create the connector.
     *
     * @return connection
     */
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * Return the unique instance identifier of the connector.
     *
     * @return String name
     */
    public String getIntegrationConnectorInstanceId()
    {
        if (genericConnector != null)
        {
            return genericConnector.getConnectorInstanceId();
        }

        return null;
    }


    /**
     * Does the connector need to run in a dedicated thread.
     *
     * @return boolean
     */
    public boolean needsDedicatedThread()
    {
        return needDedicatedThread;
    }


    /**
     * Return the earliest time that this connector can run. (Null means no restriction.)
     *
     * @return date
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Return the latest time that this connector can run. (Null means no restriction.)
     *
     * @return date
     */
    public Date getStopDate()
    {
        return stopDate;
    }

    /**
     * Return the status for the integration connector.
     *
     * @return status object
     */
    public IntegrationConnectorStatus getIntegrationConnectorStatus()
    {
        return integrationConnectorStatus;
    }


    /**
     * Return the date/time when the status was last changed.
     *
     * @return timestamp
     */
    public Date getLastStatusChange()
    {
        return lastStatusChange;
    }


    /**
     * Return the message extracted from an exception returned by the connector.  This is only set if the connectorStatus
     * is FAILED.  The full exception is logged in the server's audit log.
     *
     * @return string message
     */
    public String getFailingExceptionMessage()
    {
        return failingExceptionMessage;
    }


    /**
     * Return the statistics logged by the connector.
     *
     * @return name value pairs for the statistics
     */
    public Map<String, Object> getStatistics()
    {
        if (genericConnector != null)
        {
            return genericConnector.getConnectorStatistics();
        }

        return null;
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
    public synchronized void reinitializeConnector(String actionDescription)
    {
        final String operationName = "initialize";

        this.disconnectConnector(actionDescription);
        this.resetConnectorHandler();

        auditLog.logMessage(actionDescription,
                            IntegrationDaemonServicesAuditCode.INTEGRATION_CONNECTOR_INITIALIZING.getMessageDefinition(integrationConnectorName,
                                                                                                                       integrationServiceFullName,
                                                                                                                       integrationDaemonName,
                                                                                                                       permittedSynchronization.getName()));

        genericConnector = null;
        integrationConnector = null;

        try
        {
            genericConnector = this.getConnector(connection, actionDescription);

            integrationConnector = (IntegrationConnector)genericConnector;
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

            processConfigException(actionDescription, operationName, error);
        }
        catch (Exception  error)
        {
            processConfigException(actionDescription, operationName, error);
        }


        try
        {
            this.integrationContext = contextManager.setContext(integrationConnectorId,
                                                                integrationConnectorName,
                                                                integrationConnectorUserId,
                                                                integrationConnector,
                                                                integrationConnectorGUID,
                                                                permittedSynchronization,
                                                                generateIntegrationReport,
                                                                metadataSourceQualifiedName);

            this.integrationContextRefreshProxy = new IntegrationContextRefreshProxy(this.integrationContext);

            this.updateStatus(IntegrationConnectorStatus.INITIALIZED);
        }
        catch (Exception  error)
        {
            processInitializeException(actionDescription, error);
        }

        try
        {
            if (needDedicatedThread)
            {
                integrationConnectorDedicatedThread = new IntegrationConnectorDedicatedThread(integrationDaemonName, this, auditLog);

                integrationConnectorDedicatedThread.start();
            }
        }
        catch (Exception error)
        {
            processConfigException(actionDescription, operationName, error);
        }
    }


    /**
     * Retrieve the configuration properties of the connector.
     *
     * @return property map
     */
    public synchronized Map<String, Object> getConfigurationProperties()
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
    public synchronized void updateConfigurationProperties(String              userId,
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
     * Update the endpoint network address for a specific integration connector.
     *
     * @param userId calling user
     * @param actionDescription external caller's activity
     * @param networkAddress name of a specific connector or null for all connectors and the properties to change
     */

    public void updateEndpointNetworkAddress(String userId,
                                             String actionDescription,
                                             String networkAddress)
    {
        if (connection != null)
        {
            Endpoint endpoint = connection.getEndpoint();

            if (endpoint != null)
            {
                endpoint.setAddress(networkAddress);

                connection.setEndpoint(endpoint);

                auditLog.logMessage(actionDescription,
                                    IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_ENDPOINT_UPDATE.getMessageDefinition(userId,
                                                                                                                             integrationConnectorName,
                                                                                                                             integrationDaemonName,
                                                                                                                             networkAddress));
            }
            else
            {
                auditLog.logMessage(actionDescription,
                                    IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_NO_ENDPOINT_TO_UPDATE.getMessageDefinition(userId,
                                                                                                                                   integrationConnectorName,
                                                                                                                                   integrationDaemonName));
            }

            this.reinitializeConnector(actionDescription);
        }
    }


    /**
     * Update the connection for a specific integration connector.
     *
     * @param userId calling user
     * @param actionDescription external caller's activity
     * @param connection new connection object
     */

    public  void updateConnectorConnection(String     userId,
                                           String     actionDescription,
                                           Connection connection)
    {
        this.connection = connection;

        auditLog.logMessage(actionDescription,
                            IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_ENDPOINT_UPDATE.getMessageDefinition(userId,
                                                                                                                     integrationConnectorName,
                                                                                                                     integrationDaemonName));

        this.reinitializeConnector(actionDescription);
    }


    /**
     * Call engage() method of the integration connector.  This is called in a separate thread.
     *
     * @param actionDescription external caller's activity
     */
    public synchronized void engageConnector(String   actionDescription)
    {
        final String operationName = "engage";

        try
        {
            if (integrationConnectorStatus == IntegrationConnectorStatus.INITIALIZE_FAILED)
            {
                this.reinitializeConnector(actionDescription);
            }
            if (integrationConnectorStatus == IntegrationConnectorStatus.INITIALIZED)
            {
                this.startConnector(actionDescription,
                                    IntegrationConnectorStatus.RUNNING);
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
     * Call refresh() on the connector provided it is in the correct state.
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
            if (integrationConnectorStatus == IntegrationConnectorStatus.INITIALIZE_FAILED)
            {
                this.reinitializeConnector(actionDescription);
            }
            if (integrationConnectorStatus == IntegrationConnectorStatus.INITIALIZED)
            {
                this.startConnector(actionDescription,
                                    IntegrationConnectorStatus.WAITING);
            }
            if (integrationConnectorStatus == IntegrationConnectorStatus.WAITING)
            {
                updateStatus(IntegrationConnectorStatus.REFRESHING);
                Date refreshStart = new Date();

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

                integrationContext.startRecording();
                integrationContextRefreshProxy.setRefreshInProgress(true);
                integrationConnector.refresh();
                integrationContextRefreshProxy.setRefreshInProgress(false);
                integrationContext.publishReport();
                updateStatus(IntegrationConnectorStatus.WAITING);

                if (auditLog != null)
                {
                    Date refreshEnd = new Date();

                    auditLog.logMessage(actionDescription,
                                        IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_REFRESH_COMPLETE.getMessageDefinition(integrationConnectorName,
                                                                                                                                  integrationDaemonName,
                                                                                                                                  Long.toString(refreshEnd.getTime() - refreshStart.getTime())));
                }
            }

            this.lastRefreshTime = new Date();
        }
        catch (Exception error)
        {
            processConnectorException(actionDescription, operationName, error);
        }
    }


    /**
     * Update the connector properties with as little disruption to the running connector.
     *
     * @param registeredIntegrationConnectorElement new configuration information from the server
     */
    public synchronized void updateConnectorDetails(RegisteredIntegrationConnectorElement registeredIntegrationConnectorElement)
    {
        final String methodName = "updateConnectorDetails";

        integrationConnectorGUID = registeredIntegrationConnectorElement.getElementHeader().getGUID();
        integrationConnectorName = registeredIntegrationConnectorElement.getRegistrationProperties().getConnectorName();
        integrationConnectorUserId = registeredIntegrationConnectorElement.getRegistrationProperties().getConnectorUserId();
        startDate = registeredIntegrationConnectorElement.getRegistrationProperties().getStartDate();
        stopDate = registeredIntegrationConnectorElement.getRegistrationProperties().getStopDate();
        metadataSourceQualifiedName = registeredIntegrationConnectorElement.getRegistrationProperties().getMetadataSourceQualifiedName();
        permittedSynchronization = registeredIntegrationConnectorElement.getRegistrationProperties().getPermittedSynchronization();
        minMinutesBetweenRefresh = registeredIntegrationConnectorElement.getRegistrationProperties().getRefreshTimeInterval();

        if ((! connection.equals(registeredIntegrationConnectorElement.getProperties().getConnection())) &&
                (registeredIntegrationConnectorElement.getProperties().getConnection() != null))
        {
            connection = registeredIntegrationConnectorElement.getProperties().getConnection();
            reinitializeConnector(methodName);
        }

        if (needDedicatedThread != registeredIntegrationConnectorElement.getProperties().getUsesBlockingCalls())
        {
            needDedicatedThread = registeredIntegrationConnectorElement.getProperties().getUsesBlockingCalls();
            reinitializeConnector(methodName);
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
     * @param nextStatus next integration connector status
     */
    private void startConnector(String                     actionDescription,
                                IntegrationConnectorStatus nextStatus)
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
                updateStatus(nextStatus);
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
    public void disconnectConnector(String   actionDescription)
    {
        final String operationName = "disconnect";

        try
        {
            if ((integrationConnectorStatus == IntegrationConnectorStatus.RUNNING) ||
                (integrationConnectorStatus == IntegrationConnectorStatus.FAILED))
            {
                if (integrationContext != null)
                {
                    integrationContext.disconnect();
                }
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
        this.genericConnector                    = null;
        this.integrationConnector                = null;
        this.integrationConnectorDedicatedThread = null;
        this.failingExceptionMessage             = null;
        this.lastRefreshTime                     = null;
    }


    /**
     * This private method ensures consistent logging of connector issues.
     *
     * @param actionDescription external caller's activity
     * @param operationName connector operation that failed
     * @param error resulting exception
     */
    private void processConfigException(String     actionDescription,
                                        String     operationName,
                                        Exception  error)
    {
        updateStatus(IntegrationConnectorStatus.CONFIG_FAILED);
        failingExceptionMessage = error.getMessage();

        if (error instanceof OCFCheckedExceptionBase)
        {
            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.CONFIG_ERROR.getMessageDefinition(integrationConnectorName,
                                                                                                     operationName,
                                                                                                     error.getClass().getName(),
                                                                                                     error.getMessage()));
        }
        else
        {
            auditLog.logException(actionDescription,
                                  IntegrationDaemonServicesAuditCode.CONFIG_ERROR.getMessageDefinition(integrationConnectorName,
                                                                                                       operationName,
                                                                                                       error.getClass().getName(),
                                                                                                       error.getMessage()),
                                  error);
        }
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
     * This private method ensures consistent logging of connector issues.
     *
     * @param actionDescription external caller's activity
     * @param error resulting exception
     */
    private void processInitializeException(String     actionDescription,
                                            Exception  error)
    {
        final String operationName = "setContext";

        updateStatus(IntegrationConnectorStatus.INITIALIZE_FAILED);
        failingExceptionMessage = error.getMessage();

        if (error instanceof OCFCheckedExceptionBase)
        {
            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.INITIALIZE_ERROR.getMessageDefinition(integrationConnectorName,
                                                                                                         operationName,
                                                                                                         error.getClass().getName(),
                                                                                                         error.getMessage()));
        }
        else
        {
            auditLog.logException(actionDescription,
                                  IntegrationDaemonServicesAuditCode.INITIALIZE_ERROR.getMessageDefinition(integrationConnectorName,
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
