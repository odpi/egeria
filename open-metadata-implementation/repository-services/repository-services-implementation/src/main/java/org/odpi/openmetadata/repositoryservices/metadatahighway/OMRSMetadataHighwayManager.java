/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.metadatahighway;

import org.apache.log4j.Logger;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.Connection;
import org.odpi.openmetadata.adminservices.configuration.properties.CohortConfig;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.OMRSCohortRegistryStore;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;
import org.odpi.openmetadata.repositoryservices.eventmanagement.*;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectionConsumer;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.localrepository.OMRSLocalRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * OMRSMetadataHighwayManager is responsible for managing the connectivity to to each cohort that the local
 * server is a member of.
 */
public class OMRSMetadataHighwayManager
{
    private List<OMRSCohortManager>      cohortManagers = new ArrayList<>();
    private String                       localServerName;                    /* set in constructor */
    private String                       localServerType;                    /* set in constructor */
    private String                       localOrganizationName;              /* set in constructor */
    private OMRSLocalRepository          localRepository;                    /* set in constructor */
    private OMRSRepositoryContentManager localRepositoryContentManager;      /* set in constructor */
    private OMRSConnectionConsumer       enterpriseAccessConnectionConsumer; /* set in constructor */
    private OMRSTopicConnector           enterpriseAccessTopicConnector;     /* set in constructor */


    private static final OMRSAuditLog auditLog = new OMRSAuditLog(OMRSAuditingComponent.METADATA_HIGHWAY_MANAGER);

    private static final Logger log = Logger.getLogger(OMRSMetadataHighwayManager.class);

    /**
     * Constructor taking the values that are used in every cohort.  Any of these values may be null.
     *
     * @param localServerName - name of the local server.
     * @param localServerType - descriptive type of the local server.
     * @param localOrganizationName - name of the organization that owns the local server.
     * @param localRepository - link to local repository - may be null.
     * @param localRepositoryContentManager - repository content manager associated with this server's operation
     *                                        and used in evaluating the type definitions (TypeDefs)
     *                                        passed around the cohort.
     * @param enterpriseAccessConnectionConsumer - connection consumer for managing the connections of enterprise access.
     * @param enterpriseAccessTopicConnector - connector for the OMRS Topic for enterprise access.
     */
    public OMRSMetadataHighwayManager(String                          localServerName,
                                      String                          localServerType,
                                      String                          localOrganizationName,
                                      OMRSLocalRepository             localRepository,
                                      OMRSRepositoryContentManager    localRepositoryContentManager,
                                      OMRSConnectionConsumer          enterpriseAccessConnectionConsumer,
                                      OMRSTopicConnector              enterpriseAccessTopicConnector)
    {
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;
        this.localRepository = localRepository;
        this.localRepositoryContentManager = localRepositoryContentManager;
        this.enterpriseAccessConnectionConsumer = enterpriseAccessConnectionConsumer;
        this.enterpriseAccessTopicConnector = enterpriseAccessTopicConnector;
    }


    /**
     * Initialize each cohort manager in turn.  Configuration errors will result in an exception and the initialization
     * process will halt.
     *
     * @param cohortConfigList - list of cohorts to initialize
     */
    public void initialize(List<CohortConfig>   cohortConfigList)
    {
        if (cohortConfigList != null)
        {
            /*
             * Loop through the configured cohorts
             */
            for (CohortConfig  cohortConfig : cohortConfigList)
            {
                this.connectToCohort(cohortConfig);
            }
        }
    }


    /**
     * Initialize the components to connect the local repository to a cohort.
     *
     * @param cohortConfig - description of cohort.
     * @return the status of the cohort
     */
    public  CohortConnectionStatus connectToCohort(CohortConfig         cohortConfig)
    {
        OMRSCohortManager cohortManager  = new OMRSCohortManager();
        String            localMetadataCollectionId = null;
        String            actionDescription = "Connect to Cohort";

        /*
         * Validate the cohort name exists
         */
        if (cohortConfig.getCohortName() == null)
        {
            OMRSErrorCode errorCode = OMRSErrorCode.NULL_COHORT_NAME;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage();

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              actionDescription,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        /*
         * Loop through the existing cohort managers to make sure the new cohort name is unique
         */
        for (OMRSCohortManager existingCohortManager : cohortManagers)
        {
            if (existingCohortManager != null)
            {
                if (cohortConfig.getCohortName().equals(existingCohortManager.getCohortName()))
                {
                    OMRSErrorCode errorCode = OMRSErrorCode.DUPLICATE_COHORT_NAME;
                    String        errorMessage = errorCode.getErrorMessageId()
                                               + errorCode.getFormattedErrorMessage(cohortConfig.getCohortName());

                    throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      actionDescription,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
                }
            }
        }

        /*
         * Extract the local metadata collection id if there is a local repository
         */
        if (localRepository != null)
        {
            localMetadataCollectionId = localRepository.getMetadataCollectionId();
        }

        /*
         * Create the resources needed by the cohort and initialize them in a cohort manager.
         */
        try
        {
            OMRSCohortRegistryStore cohortRegistryStore
                    = getCohortRegistryStore(cohortConfig.getCohortName(),
                                             cohortConfig.getCohortRegistryConnection());

            OMRSTopicConnector cohortTopicConnector
                    = getTopicConnector(cohortConfig.getCohortName(),
                                        cohortConfig.getCohortOMRSTopicConnection());

            OMRSRepositoryEventExchangeRule inboundEventExchangeRule
                    = new OMRSRepositoryEventExchangeRule(cohortConfig.getCohortName() + " Events To Process",
                                                          localRepositoryContentManager,
                                                          cohortConfig.getEventsToProcessRule(),
                                                          cohortConfig.getSelectedTypesToProcess());

            cohortManager.initialize(cohortConfig.getCohortName(),
                                     localMetadataCollectionId,
                                     localServerName,
                                     localServerType,
                                     localOrganizationName,
                                     localRepository,
                                     localRepositoryContentManager,
                                     enterpriseAccessConnectionConsumer,
                                     enterpriseAccessTopicConnector,
                                     cohortRegistryStore,
                                     cohortTopicConnector,
                                     cohortConfig.getCohortOMRSTopicProtocolVersion(),
                                     inboundEventExchangeRule);

            /*
             * The cohort manager is only added to the list if it initializes successfully.
             */
            cohortManagers.add(cohortManager);
        }
        catch (OMRSConfigErrorException  error)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.COHORT_CONFIG_ERROR;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(cohortConfig.getCohortName()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            throw error;
        }
        catch (Throwable    error)
        {
            throw error;
        }

        return cohortManager.getCohortConnectionStatus();
    }


    /**
     * Return the status of the named cohort.
     *
     * @param cohortName name of cohort
     * @return connection status - if the cohort manager is not running then "NOT_INITIALIZED" is returned
     */
    public CohortConnectionStatus getCohortConnectionStatus(String   cohortName)
    {
        String actionDescription = "Get cohort status";

        if (cohortName == null)
        {
            OMRSErrorCode errorCode = OMRSErrorCode.NULL_COHORT_NAME;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage();

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              actionDescription,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        for (OMRSCohortManager  existingCohortManager : cohortManagers)
        {
            if (existingCohortManager != null)
            {
                if (cohortName.equals(existingCohortManager.getCohortName()))
                {
                    return existingCohortManager.getCohortConnectionStatus();
                }
            }
        }

        /*
         * No cohort manager was found so return not initialized.
         */
        return CohortConnectionStatus.NOT_INITIALIZED;
    }


    /**
     * Disconnect communications from a specific cohort.
     *
     * @param cohortName - name of cohort
     * @param permanent - is the local server permanently disconnecting from the cohort - causes an unregistration
     *                  event to be sent to the other members.
     * @return boolean flag to indicate success.
     */
    public boolean disconnectFromCohort(String  cohortName, boolean permanent)
    {
        String actionDescription = "Disconnect cohort";

        if (cohortName == null)
        {
            OMRSErrorCode errorCode = OMRSErrorCode.NULL_COHORT_NAME;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage();

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              actionDescription,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        for (OMRSCohortManager  existingCohortManager : cohortManagers)
        {
            if (existingCohortManager != null)
            {
                if (cohortName.equals(existingCohortManager.getCohortName()))
                {
                    existingCohortManager.disconnect(permanent);
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Disconnect from all cohorts.
     *
     * @param permanent - indicates whether the cohort registry should unregister from the cohort
     *                  and clear its registry store or just disconnect from the event topic.
     */
    public void disconnect(boolean  permanent)
    {
        final String   actionDescription = "Disconnecting from metadata highway";

        if (log.isDebugEnabled())
        {
            log.debug(actionDescription);
        }

        for (OMRSCohortManager cohortManager : cohortManagers)
        {
            if (cohortManager != null)
            {
                cohortManager.disconnect(permanent);
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug(actionDescription + " COMPLETE");
        }
    }


    /**
     * Create a connector to the cohort registry store. If there is a problem with the connection information
     * that means a connector can not be created, an exception is thrown.
     *
     * @param cohortName - name of the cohort that this registry store is for
     * @param cohortRegistryConnection - connection to the cluster registry store.
     * @return OMRSCohortRegistryStore connector
     */
    private OMRSCohortRegistryStore getCohortRegistryStore(String     cohortName,
                                                           Connection cohortRegistryConnection)
    {
        final String methodName = "getCohortRegistryStore()";

        try
        {
            ConnectorBroker         connectorBroker = new ConnectorBroker();
            Connector               connector       = connectorBroker.getConnector(cohortRegistryConnection);

            return (OMRSCohortRegistryStore)connector;
        }
        catch (Throwable   error)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Unable to create cohort registry store connector: " + error.toString());
            }

            /*
             * Throw runtime exception to indicate that the cohort registry is not available.
             */
            OMRSErrorCode errorCode = OMRSErrorCode.NULL_REGISTRY_STORE;
            String errorMessage = errorCode.getErrorMessageId()
                                + errorCode.getFormattedErrorMessage(cohortName);

            throw new OMRSConfigErrorException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction(),
                                               error);
        }
    }

    /**
     * Creates a topic connector using information from the supplied topic connection.  This connector supported
     * the Open Connector Framework (OCF) so it is possible to configure different connector implementations for
     * different event/messaging infrastructure.   If there is a problem with the connection information
     * that means a connector can not be created, an exception is thrown.
     *
     * @param cohortName - name of the cohort that this registry store is for
     * @param topicConnection - connection parameters
     * @return OMRSTopicConnector for managing communications with the event/messaging infrastructure.
     */
    private OMRSTopicConnector getTopicConnector(String     cohortName,
                                                 Connection topicConnection)
    {
        try
        {
            ConnectorBroker    connectorBroker = new ConnectorBroker();
            Connector          connector       = connectorBroker.getConnector(topicConnection);

            return (OMRSTopicConnector)connector;
        }
        catch (Throwable   error)
        {
            String methodName = "getTopicConnector()";

            if (log.isDebugEnabled())
            {
                log.debug("Unable to create topic connector: " + error.toString());
            }

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_TOPIC_CONNECTOR;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage(cohortName);

            throw new OMRSConfigErrorException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction(),
                                               error);
        }
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMRSMetadataHighwayManager{" +
                "cohortManagers=" + cohortManagers +
                ", localServerName='" + localServerName + '\'' +
                ", localServerType='" + localServerType + '\'' +
                ", localOrganizationName='" + localOrganizationName + '\'' +
                ", localRepository=" + localRepository +
                ", localRepositoryContentManager=" + localRepositoryContentManager +
                ", enterpriseAccessConnectionConsumer=" + enterpriseAccessConnectionConsumer +
                ", enterpriseAccessTopicConnector=" + enterpriseAccessTopicConnector +
                '}';
    }
}
