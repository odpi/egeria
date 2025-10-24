/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.integration.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.frameworks.opengovernance.properties.IntegrationGroupElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.IntegrationGroupProperties;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RegisteredIntegrationConnectorElement;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The IntegrationGroupHandler is responsible for maintaining information about the integration connectors in an integration group.
 */
public class IntegrationGroupHandler
{
    protected String                     serverName;        /* Initialized in constructor */
    protected String                     serverUserId;      /* Initialized in constructor */
    protected AuditLog                   auditLog;          /* Initialized in constructor */
    protected int                        maxPageSize;       /* Initialized in constructor */
    protected String                     integrationGroupName;   /* Initialized in constructor */
    protected String                     integrationGroupGUID       = null;
    protected IntegrationGroupProperties integrationGroupProperties = null;
    private   IntegrationGroupElement    integrationGroupElement    = null;


    private final GovernanceConfigurationClient configurationClient;        /* Initialized in constructor */

    private final IntegrationConnectorCacheMap           integrationConnectorLookupTable;
    private final IntegrationContextManager              integrationContextManager;
    private List<IntegrationConnectorHandler>      connectorHandlers = new ArrayList<>();

    /**
     * Create a client-side object for managing an integration connector.
     *
     * @param integrationGroupName the properties of the integration group.
     * @param integrationConnectorCacheMap manages the dynamic map of connectors.
     * @param integrationContextManager details of the context manager that creates new integration contexts for the integration connectors.
     * @param serverName the name of the integration daemon server where the integration group is running
     * @param serverUserId user id for the server to use
     * @param configurationClient client to retrieve the configuration
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public IntegrationGroupHandler(String                                 integrationGroupName,
                                   IntegrationContextManager              integrationContextManager,
                                   IntegrationConnectorCacheMap           integrationConnectorCacheMap,
                                   String                                 serverName,
                                   String                                 serverUserId,
                                   GovernanceConfigurationClient          configurationClient,
                                   AuditLog                               auditLog,
                                   int                                    maxPageSize)
    {
        this.integrationGroupName            = integrationGroupName;
        this.integrationContextManager       = integrationContextManager;
        this.integrationConnectorLookupTable = integrationConnectorCacheMap;
        this.serverName                      = serverName;
        this.serverUserId                    = serverUserId;
        this.configurationClient             = configurationClient;
        this.auditLog                        = auditLog;
        this.maxPageSize                     = maxPageSize;
    }


    /**
     * Return the integration group name - used for error logging.
     *
     * @return integration group name
     */
    public String getIntegrationGroupName()
    {
        return integrationGroupName;
    }


    /**
     * Return a summary of the integration group
     *
     * @return integration group summary
     */
    public IntegrationGroupSummary getSummary()
    {
        IntegrationGroupSummary mySummary = new IntegrationGroupSummary();

        mySummary.setIntegrationGroupName(integrationGroupName);
        mySummary.setIntegrationGroupGUID(integrationGroupGUID);

        if (integrationGroupProperties != null)
        {
            mySummary.setIntegrationGroupDescription(integrationGroupProperties.getDescription());
        }

        List<IntegrationConnectorReport> connectorReports = new ArrayList<>();
        if (! connectorHandlers.isEmpty())
        {
            for (IntegrationConnectorHandler connectorHandler : connectorHandlers)
            {
                if (connectorHandler != null)
                {
                    IntegrationConnectorReport connectorReport = new IntegrationConnectorReport();

                    connectorReport.setConnectorId(connectorHandler.getIntegrationConnectorId());
                    connectorReport.setConnectorName(connectorHandler.getIntegrationConnectorName());
                    connectorReport.setConnectorStatus(connectorHandler.getIntegrationConnectorStatus());
                    connectorReport.setConnection(connectorHandler.getConnection());
                    connectorReport.setConnectorInstanceId(connectorHandler.getIntegrationConnectorInstanceId());
                    connectorReport.setFailingExceptionMessage(connectorHandler.getFailingExceptionMessage());
                    connectorReport.setStatistics(connectorHandler.getStatistics());
                    connectorReport.setLastStatusChange(connectorHandler.getLastStatusChange());
                    connectorReport.setLastRefreshTime(connectorHandler.getLastRefreshTime());
                    connectorReport.setMinMinutesBetweenRefresh(connectorHandler.getMinMinutesBetweenRefresh());

                    connectorReports.add(connectorReport);
                }
            }
        }

        if (! connectorReports.isEmpty())
        {
            mySummary.setIntegrationConnectorReports(connectorReports);
        }

        mySummary.setIntegrationGroupStatus(IntegrationGroupStatus.ASSIGNED);

        if (integrationGroupGUID != null)
        {
            mySummary.setIntegrationGroupStatus(IntegrationGroupStatus.CONFIGURING);
        }

        if (! connectorHandlers.isEmpty())
        {
            mySummary.setIntegrationGroupStatus(IntegrationGroupStatus.RUNNING);
        }

        return mySummary;
    }


    /**
     * Return the unique identifier for the integration group's entity stored in the metadata server (if known).
     *
     * @return string or null
     */
    public  String getIntegrationGroupGUID()
    {
        return integrationGroupGUID;
    }


    /**
     * Return the context manager.
     *
     * @return context manager or null
     */
    public IntegrationContextManager getContextManager()
    {
        return integrationContextManager;
    }


    /**
     * Request that the integration group refresh its configuration by calling the metadata server.
     * This request ensures that the latest configuration is in use.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user id is not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    public  void refreshConfig() throws InvalidParameterException,
                                        UserNotAuthorizedException,
                                        PropertyServerException
    {
        final String methodName = "refreshConfig";

        /*
         * Begin by extracting the properties for the integration group from the metadata server.
         * This method throws exceptions if there is a problem retrieving the integration group properties.
         */
        this.integrationGroupElement = configurationClient.getIntegrationGroupByName(serverUserId, integrationGroupName);

        if ((integrationGroupElement == null) ||
                    (integrationGroupElement.getElementHeader() == null) ||
                    (integrationGroupElement.getElementHeader().getType() == null) ||
                    (integrationGroupElement.getProperties() == null))
        {
            this.integrationGroupGUID       = null;
            this.integrationGroupProperties = null;

            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_INTEGRATION_GROUP_CONFIG.getMessageDefinition(integrationGroupName,
                                                                                                                                         configurationClient.getConfigurationServerName(),
                                                                                                                                         serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                integrationGroupName);
        }
        else
        {
            this.integrationGroupGUID       = integrationGroupElement.getElementHeader().getGUID();
            this.integrationGroupProperties = integrationGroupElement.getProperties();

            refreshAllConnectorConfig();
        }
    }


    /**
     * Request that the integration group refreshes its configuration for all integration connectors
     * by calling the metadata server. This request just ensures that the latest configuration
     * is in use.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user id is not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    private void refreshAllConnectorConfig() throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException
    {
        final String methodName = "refreshAllConnectorConfig";

        /*
         * Clear the lookup table - this will cause temporary failures in governance requests if services were already
         * configured.
         */
        final String actionDescription = "Retrieve all integration connector configuration";

        auditLog.logMessage(actionDescription,
                            IntegrationDaemonServicesAuditCode.CLEARING_ALL_INTEGRATION_CONNECTOR_CONFIG.getMessageDefinition(integrationGroupName));

        // integrationConnectorLookupTable.clear();

        Map<String, IntegrationConnectorHandler>  currentConnectors = new HashMap<>();

        int      startingFrom = 0;
        boolean  moreToReceive = true;

        while (moreToReceive)
        {
            List<RegisteredIntegrationConnectorElement> registeredIntegrationConnectors = configurationClient.getRegisteredIntegrationConnectors(serverUserId,
                                                                                                                                                 integrationGroupGUID,
                                                                                                                                                 startingFrom,
                                                                                                                                                 maxPageSize);

            if ((registeredIntegrationConnectors != null) && (! registeredIntegrationConnectors.isEmpty()))
            {
                for (RegisteredIntegrationConnectorElement registeredIntegrationConnectorElement : registeredIntegrationConnectors)
                {
                    IntegrationConnectorHandler matchingHandler = refreshRegisteredIntegrationConnector(registeredIntegrationConnectorElement, methodName);

                    if (matchingHandler != null)
                    {
                        currentConnectors.put(registeredIntegrationConnectorElement.getConnectorId(),
                                              matchingHandler);
                    }
                }

                startingFrom = startingFrom + maxPageSize;
            }
            else
            {
                moreToReceive = false;
            }
        }

        /*
         * The challenge is to remove the connectors that are running but no longer in the group.
         */
        if ((connectorHandlers == null) || (connectorHandlers.isEmpty()))
        {
            connectorHandlers = new ArrayList<>(currentConnectors.values());
        }
        else if (currentConnectors.isEmpty())
        {
            connectorHandlers = new ArrayList<>();
        }
        else
        {
            for (IntegrationConnectorHandler runningConnector : connectorHandlers)
            {
                boolean found = false;
                for (String connectorId : currentConnectors.keySet())
                {
                    if (connectorId.equals(runningConnector.getIntegrationConnectorId()))
                    {
                        found = true;
                        break;
                    }
                }

                if (! found)
                {
                    runningConnector.disconnectConnector(methodName);
                }
            }

            connectorHandlers = new ArrayList<>(currentConnectors.values());
        }

        auditLog.logMessage(actionDescription,
                            IntegrationDaemonServicesAuditCode.FINISHED_ALL_INTEGRATION_CONNECTOR_CONFIG.getMessageDefinition(integrationGroupName));
    }


    /**
     * Request that the integration group refreshes its configuration for a single integration connector
     * by calling the metadata server. This request just ensures that the latest configuration
     * is in use.
     *
     * @param integrationConnectorGUID unique identifier of the integration connector entity
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user id is not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    public  void refreshConnectorConfig(String  integrationConnectorGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "refreshConnectorConfig";

        if (integrationConnectorGUID != null)
        {
            RegisteredIntegrationConnectorElement registeredIntegrationConnector = configurationClient.getRegisteredIntegrationConnector(serverUserId,
                                                                                                                                         integrationGroupGUID,
                                                                                                                                         integrationConnectorGUID);

            this.refreshRegisteredIntegrationConnector(registeredIntegrationConnector, methodName);
        }
    }


    /**
     * Set up the connector cache for the registered connector.  An entry is added for each connectorId.
     *
     * @param registeredIntegrationConnectorElement description of the registered integration connector
     * @param methodName calling method for message
     * @return connector handler
     */
    private IntegrationConnectorHandler refreshRegisteredIntegrationConnector(RegisteredIntegrationConnectorElement registeredIntegrationConnectorElement,
                                                                              String                                methodName)
    {
        /*
         * It is possible that the details about the integration connector are still being built.  So ignore the new element if it is not complete.
         */
        if ((registeredIntegrationConnectorElement != null) &&
                (registeredIntegrationConnectorElement.getConnectorId() != null) &&
                (registeredIntegrationConnectorElement.getElementHeader() != null) &&
                (registeredIntegrationConnectorElement.getRegistrationProperties() != null) &&
                (registeredIntegrationConnectorElement.getProperties() != null) &&
                (registeredIntegrationConnectorElement.getProperties().getConnection() != null) &&
                (registeredIntegrationConnectorElement.getProperties().getConnection().getConnectorType() != null) &&
                (registeredIntegrationConnectorElement.getProperties().getConnection().getConnectorType().getConnectorProviderClassName() != null))
        {
            IntegrationConnectorHandler connectorHandler = integrationConnectorLookupTable.getHandlerByConnectorId(registeredIntegrationConnectorElement.getConnectorId());

            if (connectorHandler == null)
            {
                try
                {
                    String                    userId = serverUserId;

                        if (registeredIntegrationConnectorElement.getRegistrationProperties().getConnectorUserId() != null)
                        {
                            userId = registeredIntegrationConnectorElement.getRegistrationProperties().getConnectorUserId();
                        }

                        connectorHandler = new IntegrationConnectorHandler(registeredIntegrationConnectorElement.getConnectorId(),
                                                                           registeredIntegrationConnectorElement.getElementHeader().getGUID(),
                                                                           registeredIntegrationConnectorElement.getRegistrationProperties().getConnectorName(),
                                                                           userId,
                                                                           registeredIntegrationConnectorElement.getRegistrationProperties().getStartDate(),
                                                                           registeredIntegrationConnectorElement.getRegistrationProperties().getConnectorShutdownDate(),
                                                                           registeredIntegrationConnectorElement.getRegistrationProperties().getRefreshTimeInterval(),
                                                                           registeredIntegrationConnectorElement.getRegistrationProperties().getMetadataSourceQualifiedName(),
                                                                           registeredIntegrationConnectorElement.getProperties().getConnection(),
                                                                           registeredIntegrationConnectorElement.getProperties().getUsesBlockingCalls(),
                                                                           registeredIntegrationConnectorElement.getRegistrationProperties().getPermittedSynchronization(),
                                                                           registeredIntegrationConnectorElement.getRegistrationProperties().getGenerateConnectorActivityReports(),
                                                                           serverName,
                                                                           integrationContextManager,
                                                                           auditLog);
                        /*
                         * This is a local list for status reporting
                         */
                        connectorHandlers.add(connectorHandler);

                        /*
                         * This is the full list of connectors running in the integration daemon.
                         */
                        integrationConnectorLookupTable.putHandlerByConnectorId(registeredIntegrationConnectorElement.getConnectorId(),
                                                                                connectorHandler,
                                                                                false);
                }
                catch (Exception error)
                {
                    /*
                     * There is something wrong with the connector class
                     */
                    auditLog.logException(methodName,
                                          IntegrationDaemonServicesAuditCode.CONNECTOR_CHANGE_FAILED.getMessageDefinition(registeredIntegrationConnectorElement.getElementHeader().getGUID(),
                                                                                                                          error.getClass().getName(),
                                                                                                                          error.getMessage()),
                                          registeredIntegrationConnectorElement.toString(),
                                          error);
                }
            }
            else
            {
                connectorHandler.refreshConnectorDetails(registeredIntegrationConnectorElement);
            }

            return connectorHandler;
        }

        return null;
    }
}
