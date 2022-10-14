/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.metadatahighway;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.odpi.openmetadata.repositoryservices.events.OpenMetadataEventsSecurity;
import org.odpi.openmetadata.repositoryservices.properties.CohortConnectionStatus;
import org.odpi.openmetadata.repositoryservices.properties.CohortDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenMetadataEventProtocolVersion;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.adminservices.configuration.properties.CohortConfig;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventProtocolVersion;
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
 * OMRSMetadataHighwayManager is responsible for managing the connectivity to each cohort that the local
 * server is a member of.
 */
public class OMRSMetadataHighwayManager
{
    private final List<OMRSCohortManager>      cohortManagers = new ArrayList<>();
    private final String                       localServerName;                    /* set in constructor */
    private final String                       localServerType;                    /* set in constructor */
    private final String                       localOrganizationName;              /* set in constructor */
    private final OMRSLocalRepository          localRepository;                    /* set in constructor */
    private final OMRSRepositoryContentManager localRepositoryContentManager;      /* set in constructor */
    private final OMRSConnectionConsumer       enterpriseAccessConnectionConsumer; /* set in constructor */
    private final OMRSTopicConnector           enterpriseAccessTopicConnector;     /* set in constructor */
    private final AuditLog                     auditLog;

    private static final Logger log = LoggerFactory.getLogger(OMRSMetadataHighwayManager.class);

    /**
     * Constructor taking the values that are used in every cohort.  Any of these values may be null.
     *
     * @param localServerName name of the local server.
     * @param localServerType descriptive type of the local server.
     * @param localOrganizationName name of the organization that owns the local server.
     * @param localRepository link to local repository may be null.
     * @param localRepositoryContentManager repository content manager associated with this server's operation
     *                                        and used in evaluating the type definitions (TypeDefs)
     *                                        passed around the cohort.
     * @param enterpriseAccessConnectionConsumer connection consumer for managing the connections of enterprise access.
     * @param enterpriseAccessTopicConnector connector for the OMRS Topic for enterprise access.
     * @param auditLog audit log for this component.
     */
    public OMRSMetadataHighwayManager(String                          localServerName,
                                      String                          localServerType,
                                      String                          localOrganizationName,
                                      OMRSLocalRepository             localRepository,
                                      OMRSRepositoryContentManager    localRepositoryContentManager,
                                      OMRSConnectionConsumer          enterpriseAccessConnectionConsumer,
                                      OMRSTopicConnector              enterpriseAccessTopicConnector,
                                      AuditLog                        auditLog)
    {
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;
        this.localRepository = localRepository;
        this.localRepositoryContentManager = localRepositoryContentManager;
        this.enterpriseAccessConnectionConsumer = enterpriseAccessConnectionConsumer;
        this.enterpriseAccessTopicConnector = enterpriseAccessTopicConnector;
        this.auditLog = auditLog;
    }


    /**
     * Initialize each cohort manager in turn.  Configuration errors will result in an exception and the initialization
     * process will halt.
     *
     * @param cohortConfigList list of cohorts to initialize
     */
    public void initialize(List<CohortConfig> cohortConfigList)
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
     * Set up a new security verifier (the cohort manager runs with a default verifier until this
     * method is called).
     *
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    public void setSecurityVerifier(OpenMetadataEventsSecurity securityVerifier)
    {
        if (securityVerifier != null)
        {
            /*
             * Loop through the existing cohort managers to set up the security verifier
             */
            for (OMRSCohortManager existingCohortManager : cohortManagers)
            {
                if (existingCohortManager != null)
                {
                    existingCohortManager.setSecurityVerifier(securityVerifier);
                }
            }
        }
    }


    /**
     * Initialize the components to connect the local repository to a cohort.
     *
     * @param cohortConfig description of cohort.
     * @return the status of the cohort
     */
    public CohortConnectionStatus connectToCohort(CohortConfig cohortConfig)
    {
        OMRSCohortManager cohortManager  = new OMRSCohortManager(auditLog.createNewAuditLog(OMRSAuditingComponent.COHORT_MANAGER));
        String            localMetadataCollectionId = null;
        String            localMetadataCollectionName = null;
        String            actionDescription = "Connect to Cohort";

        /*
         * Validate the cohort name exists
         */
        if (cohortConfig.getCohortName() == null)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_COHORT_NAME.getMessageDefinition(),
                                              this.getClass().getName(),
                                              actionDescription);
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
                    throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_COHORT_NAME.getMessageDefinition(cohortConfig.getCohortName()),
                                                      this.getClass().getName(),
                                                      actionDescription);
                }
            }
        }

        /*
         * Extract the local metadata collection id if there is a local repository
         */
        if (localRepository != null)
        {
            localMetadataCollectionId = localRepository.getMetadataCollectionId();
            localMetadataCollectionName = localRepository.getMetadataCollectionName();
        }

        /*
         * Create the resources needed by the cohort and initialize them in a cohort manager.
         */
        try
        {
            OMRSCohortRegistryStore cohortRegistryStore
                    = getCohortRegistryStore(cohortConfig.getCohortName(),
                                             cohortConfig.getCohortRegistryConnection());

            OMRSTopicConnector cohortSingleTopicConnector = null;
            OMRSTopicConnector cohortRegistrationTopicConnector = null;
            OMRSTopicConnector cohortTypesTopicConnector = null;
            OMRSTopicConnector cohortInstancesTopicConnector = null;

            if (cohortConfig.getCohortOMRSTopicConnection() != null)
            {
                cohortSingleTopicConnector = getTopicConnector(cohortConfig.getCohortName() + " (single)",
                                                               cohortConfig.getCohortOMRSTopicConnection(),
                                                               cohortConfig.getCohortOMRSTopicProtocolVersion());
            }

            if (cohortConfig.getCohortOMRSRegistrationTopicConnection() != null)
            {
                cohortRegistrationTopicConnector = getTopicConnector(cohortConfig.getCohortName() + " (registration)",
                                                                     cohortConfig.getCohortOMRSRegistrationTopicConnection(),
                                                                     cohortConfig.getCohortOMRSTopicProtocolVersion());
            }

            if (cohortConfig.getCohortOMRSTypesTopicConnection() != null)
            {
                cohortTypesTopicConnector = getTopicConnector(cohortConfig.getCohortName() + " (types)",
                                                              cohortConfig.getCohortOMRSTypesTopicConnection(),
                                                              cohortConfig.getCohortOMRSTopicProtocolVersion());
            }

            if (cohortConfig.getCohortOMRSInstancesTopicConnection() != null)
            {
                cohortInstancesTopicConnector = getTopicConnector(cohortConfig.getCohortName() + " (instances)",
                                                                  cohortConfig.getCohortOMRSInstancesTopicConnection(),
                                                                  cohortConfig.getCohortOMRSTopicProtocolVersion());
            }

            OMRSRepositoryEventExchangeRule inboundEventExchangeRule
                    = new OMRSRepositoryEventExchangeRule(cohortConfig.getEventsToProcessRule(),
                                                          cohortConfig.getSelectedTypesToProcess());

            cohortManager.initialize(cohortConfig.getCohortName(),
                                     localMetadataCollectionId,
                                     localMetadataCollectionName,
                                     localServerName,
                                     localServerType,
                                     localOrganizationName,
                                     localRepository,
                                     localRepositoryContentManager,
                                     enterpriseAccessConnectionConsumer,
                                     enterpriseAccessTopicConnector,
                                     cohortRegistryStore,
                                     cohortConfig.getCohortOMRSTopicConnection(),
                                     cohortSingleTopicConnector,
                                     cohortConfig.getCohortOMRSRegistrationTopicConnection(),
                                     cohortRegistrationTopicConnector,
                                     cohortConfig.getCohortOMRSTypesTopicConnection(),
                                     cohortTypesTopicConnector,
                                     cohortConfig.getCohortOMRSInstancesTopicConnection(),
                                     cohortInstancesTopicConnector,
                                     inboundEventExchangeRule);

            /*
             * The cohort manager is only added to the list if it initializes successfully.
             */
            cohortManagers.add(cohortManager);
        }
        catch (OMRSConfigErrorException  error)
        {
            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.COHORT_CONFIG_ERROR.getMessageDefinition(cohortConfig.getCohortName(), error.getReportedErrorMessage()));

            throw error;
        }
        catch (Exception error)
        {
            throw error;
        }

        return cohortManager.getCohortConnectionStatus();
    }


    /**
     * Return the common values used by this server to register with a cohort.
     *
     * @return local registration
     */
    public MemberRegistration getLocalRegistration()
    {
        for (OMRSCohortManager  existingCohortManager : cohortManagers)
        {
            if (existingCohortManager != null)
            {
                MemberRegistration localRegistration = existingCohortManager.getLocalRegistration();

                if (localRegistration != null)
                {
                    localRegistration.setRegistrationTime(null);
                    return localRegistration;
                }
            }
        }

        return null;
    }


    /**
     * Return the common values used by this server to register with a cohort.
     *
     * @param cohortName name of the cohort to extract the registration time from.
     * @return local registration
     */
    public MemberRegistration getLocalRegistration(String cohortName)
    {
        if (cohortName != null)
        {
            for (OMRSCohortManager existingCohortManager : cohortManagers)
            {
                if (existingCohortManager != null)
                {
                    if (cohortName.equals(existingCohortManager.getCohortName()))
                    {
                        return existingCohortManager.getLocalRegistration();
                    }
                }
            }
        }

        return null;
    }


    /**
     * Return the remote member of a specific cohort
     *
     * @param cohortName name of cohort
     * @return list of members
     */
    public List<MemberRegistration> getRemoteMembers(String   cohortName)
    {
        if (cohortName == null)
        {
            final String  actionDescription = "get remote members";

            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_COHORT_NAME.getMessageDefinition(),
                                              this.getClass().getName(),
                                              actionDescription);
        }

        for (OMRSCohortManager  existingCohortManager : cohortManagers)
        {
            if (existingCohortManager != null)
            {
                if (cohortName.equals(existingCohortManager.getCohortName()))
                {
                    return existingCohortManager.getRemoteMembers();
                }

            }
        }

        return null;
    }


    /**
     * Return the list of cohorts
     *
     * @return cohort descriptions
     */
    public List<CohortDescription>  getCohortDescriptions()
    {
        List<CohortDescription>  cohortDescriptions = new ArrayList<>();

        for (OMRSCohortManager  existingCohortManager : cohortManagers)
        {
            if (existingCohortManager != null)
            {
                cohortDescriptions.add(existingCohortManager.getCohortDescription());
            }
        }

        if (cohortDescriptions.isEmpty())
        {
            return null;
        }
        else
        {
            return cohortDescriptions;
        }
    }


    /**
     * Return the status of the named cohort.
     *
     * @param cohortName name of cohort
     * @return connection status if the cohort manager is not running then "NOT_INITIALIZED" is returned
     */
    public CohortConnectionStatus getCohortConnectionStatus(String   cohortName)
    {
        String actionDescription = "Get cohort status";

        if (cohortName == null)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_COHORT_NAME.getMessageDefinition(),
                                              this.getClass().getName(),
                                              actionDescription);
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
     * @param cohortName name of cohort
     * @param permanent is the local server permanently disconnecting from the cohort causes an unregistration
     *                  event to be sent to the other members.
     * @return boolean flag to indicate success.
     */
    public boolean disconnectFromCohort(String  cohortName, boolean permanent)
    {
        String actionDescription = "Disconnect cohort";

        if (cohortName == null)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_COHORT_NAME.getMessageDefinition(),
                                              this.getClass().getName(),
                                              actionDescription);
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
     * @param permanent indicates whether the cohort registry should unregister from the cohort
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
     * @param cohortName name of the cohort that this registry store is for
     * @param cohortRegistryConnection connection to the cluster registry store.
     * @return OMRSCohortRegistryStore connector
     */
    private OMRSCohortRegistryStore getCohortRegistryStore(String     cohortName,
                                                           Connection cohortRegistryConnection)
    {
        final String methodName = "getCohortRegistryStore()";

        try
        {
            ConnectorBroker         connectorBroker = new ConnectorBroker(auditLog);
            Connector               connector       = connectorBroker.getConnector(cohortRegistryConnection);

            return (OMRSCohortRegistryStore)connector;
        }
        catch (Exception   error)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Unable to create cohort registry store connector: " + error.toString());
            }

            /*
             * Throw runtime exception to indicate that the cohort registry is not available.
             */
            throw new OMRSConfigErrorException(OMRSErrorCode.NULL_REGISTRY_STORE.getMessageDefinition(cohortName),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }

    /**
     * Creates a topic connector using information from the supplied topic connection.  This connector supported
     * the Open Connector Framework (OCF) so it is possible to configure different connector implementations for
     * different event/messaging infrastructure.   If there is a problem with the connection information
     * that means a connector can not be created, an exception is thrown.
     *
     * @param cohortName name of the cohort that this registry store is for
     * @param topicConnection connection parameters
     * @param protocolVersion event protocol version
     * @return OMRSTopicConnector for managing communications with the event/messaging infrastructure.
     */
    private OMRSTopicConnector getTopicConnector(String                           cohortName,
                                                 Connection                       topicConnection,
                                                 OpenMetadataEventProtocolVersion protocolVersion)
    {
        try
        {
            ConnectorBroker    connectorBroker = new ConnectorBroker(auditLog);
            Connector          connector       = connectorBroker.getConnector(topicConnection);

            OMRSTopicConnector topicConnector  = (OMRSTopicConnector)connector;

            if (protocolVersion == OpenMetadataEventProtocolVersion.V1)
            {
                topicConnector.setEventProtocolLevel(OMRSEventProtocolVersion.V1);
            }

            return topicConnector;
        }
        catch (Exception   error)
        {
            String methodName = "getTopicConnector()";

            if (log.isDebugEnabled())
            {
                log.debug("Unable to create topic connector: " + error.toString());
            }

            auditLog.logMessage(methodName,
                                OMRSAuditCode.BAD_TOPIC_CONNECTION.getMessageDefinition(cohortName, error.getClass().getName(), error.getMessage()));

            throw new OMRSConfigErrorException(OMRSErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(cohortName),
                                               this.getClass().getName(),
                                               methodName,
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
