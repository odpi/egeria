/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.admin;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogDestination;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventPublisher;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.adminservices.configuration.properties.CohortConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EnterpriseAccessConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.LocalRepositoryConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryServicesConfig;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveManager;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStoreConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperConnector;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectionConsumer;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSEnterpriseConnectorManager;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.EnterpriseOMRSConnection;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.EnterpriseOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventExchangeRule;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.EnterpriseOMRSConnectorProvider;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSConnectorProvider;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentHelper;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentValidator;
import org.odpi.openmetadata.repositoryservices.metadatahighway.OMRSMetadataHighwayManager;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSRepositoryRESTServices;

import java.util.ArrayList;
import java.util.List;


/**
 * OMRSOperationalServices provides the OMAG Server with access to the OMRS capabilities.
 * This includes managing the local metadata repository, connecting and disconnecting from the metadata
 * highway and supporting administrative
 * actions captured through the OMAG REST interface.
 * Examples of the types of capabilities offered by the OMRS Manager include:
 * <ul>
 *     <li>Initialize and Shutdown the OMRS</li>
 *     <li>See the state of the cohort</li>
 *     <li>See the state of the connectors</li>
 *     <li>View the audit log</li>
 *     <li>Load new connector JARs</li>
 *     <li>Connect/disconnect from the metadata highway</li>
 * </ul>
 */
public class OMRSOperationalServices
{
    /*
     * The audit log provides a verifiable record of the membership of the open metadata repository cohort and the
     * metadata exchange activity they are involved in.  The Logger is for standard debug.
     */
    private static final Logger       log      = LoggerFactory.getLogger(OMRSOperationalServices.class);

    private final String                         localServerName;               /* Initialized in constructor */
    private final String                         localServerType;               /* Initialized in constructor */
    private       String                         localMetadataCollectionName;   /* Initialized in constructor */
    private final String                         localOrganizationName;         /* Initialized in constructor */
    private final String                         localServerUserId;             /* Initialized in constructor */
    private final String                         localServerPassword;           /* Initialized in constructor */
    private final String                         localServerURL;                /* Initialized in constructor */
    private final int                            maxPageSize;                   /* Initialized in constructor */

    private String                         localMetadataCollectionId          = null;

    private OMRSRepositoryContentManager   localRepositoryContentManager       = null;
    private OMRSRepositoryEventManager     localRepositoryEventManager         = null;
    private OMRSMetadataHighwayManager     metadataHighwayManager              = null;
    private OMRSEnterpriseConnectorManager enterpriseConnectorManager          = null;
    private String                         enterpriseMetadataCollectionId      = null;
    private String                         enterpriseMetadataCollectionName    = null;
    private OMRSTopicConnector             enterpriseOMRSTopicConnector        = null;
    private OMRSTopicConnector             remoteEnterpriseOMRSTopicConnector  = null;
    private LocalOMRSRepositoryConnector   localRepositoryConnector            = null;
    private OMRSArchiveManager             archiveManager                      = null;
    private OMRSAuditLogDestination        auditLogDestination                 = null;
    private OMRSAuditLog                   auditLog                            = null;



    /**
     * Constructor used at server startup.
     *
     * @param localServerName name of the local server
     * @param localServerType type of the local server
     * @param organizationName name of the organization that owns the local server
     * @param localServerUserId user id for this server to use in outbound REST calls and
     *                          internal calls when processing inbound messages.
     * @param localServerPassword password for this server to use on outbound REST calls.
     * @param localServerURL URL root for this server.
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     */
    public OMRSOperationalServices(String                   localServerName,
                                   String                   localServerType,
                                   String                   organizationName,
                                   String                   localServerUserId,
                                   String                   localServerPassword,
                                   String                   localServerURL,
                                   int                      maxPageSize)
    {
        /*
         * Save details about the local server
         */
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = organizationName;
        this.localServerUserId = localServerUserId;
        this.localServerPassword = localServerPassword;
        this.localServerURL = localServerURL;
        this.maxPageSize = maxPageSize;
    }


    /**
     * Return the Enterprise OMRS Topic Connector.
     *
     * @return OMRSTopicConnector for use by the Conformance Test Services or Access Services.
     */
    public OMRSTopicConnector getEnterpriseOMRSTopicConnector()
    {
        return enterpriseOMRSTopicConnector;
    }


    /**
     * Create repository connector for an access service.
     *
     * @param callingServiceName name of the access service name.
     * @return a repository connector that is able to retrieve and maintain information from all connected repositories.
     */
    public OMRSRepositoryConnector getEnterpriseOMRSRepositoryConnector(String   callingServiceName)
    {
        final String    actionDescription = "getEnterpriseOMRSRepositoryConnector";

        if (enterpriseMetadataCollectionId != null)
        {
            EnterpriseOMRSConnectorProvider connectorProvider =
                    new EnterpriseOMRSConnectorProvider(enterpriseConnectorManager,
                                                        localRepositoryContentManager,
                                                        localServerName,
                                                        localServerType,
                                                        localOrganizationName,
                                                        auditLog.createNewAuditLog(OMRSAuditingComponent.ENTERPRISE_REPOSITORY_CONNECTOR),
                                                        enterpriseMetadataCollectionId,
                                                        enterpriseMetadataCollectionName,
                                                        localMetadataCollectionId);

            try
            {
                Connector connector = connectorProvider.getConnector(new EnterpriseOMRSConnection());

                EnterpriseOMRSRepositoryConnector omrsRepositoryConnector = (EnterpriseOMRSRepositoryConnector) connector;

                omrsRepositoryConnector.setCallingServiceName(callingServiceName);
                omrsRepositoryConnector.setMaxPageSize(maxPageSize);

                auditLog.logMessage(actionDescription, OMRSAuditCode.NEW_ENTERPRISE_CONNECTOR.getMessageDefinition(callingServiceName));

                omrsRepositoryConnector.start();

                return omrsRepositoryConnector;
            }
            catch (Exception error)
            {
                auditLog.logException(actionDescription,
                                      OMRSAuditCode.ENTERPRISE_CONNECTOR_FAILED.getMessageDefinition(callingServiceName,
                                                                                                     error.getClass().getName(),
                                                                                                     error.getMessage()),
                                      error);
            }
        }

        return null;
    }


    /**
     * Return the enterprise connector manager.  This is used by the conformance suite to get access to connectors
     * to registered members of the cohorts that this server is connected to.  That way it can exercise their
     * APIs and compare them with the events being received over the cohort topic.
     * This method is called after the OMRS is initialized.  The enterprise connector manager is created whether
     * there are OMASs activated or not.
     *
     * @return enterprise connector manager.
     */
    public OMRSEnterpriseConnectorManager  getEnterpriseConnectorManager()
    {
        return enterpriseConnectorManager;
    }


    /**
     * Create an audit log for an external component.
     *
     * @param componentId numerical identifier for the component
     * @param componentDevelopmentStatus development status
     * @param componentName display name for the component
     * @param componentDescription description of the component
     * @param componentWikiURL link to more information
     * @return new audit log object
     */
    public OMRSAuditLog  getAuditLog(int                        componentId,
                                     ComponentDevelopmentStatus componentDevelopmentStatus,
                                     String                     componentName,
                                     String                     componentDescription,
                                     String                     componentWikiURL)
    {
        return auditLog.createNewAuditLog(componentId, componentDevelopmentStatus, componentName, componentDescription, componentWikiURL);
    }



    /**
     * Initialize the OMRS component for the Open Metadata Repository Services (OMRS) for a basic server.
     * By that it means, for any type of server that is not a metadata server. In a basic server, only
     * the audit log is enabled.
     *
     * @param repositoryServicesConfig current configuration values
     * @param serverTypeClassification classification of server that will drive initialization
     */
    public void initializeAuditLog(RepositoryServicesConfig repositoryServicesConfig,
                                   String                   serverTypeClassification)
    {
        final String   actionDescription = "Initialize Open Metadata Repository Operational Services Audit Log";
        final String   methodName        = "initializeAuditLog";

        if (repositoryServicesConfig == null)
        {
            /*
             * Throw exception as without configuration information the OMRS can not start.
             */
           throw new OMRSLogicErrorException(OMRSErrorCode.NULL_CONFIG.getMessageDefinition(localServerName),
                                              this.getClass().getName(),
                                              methodName);
        }

        if (repositoryServicesConfig.getAuditLogConnections() == null)
        {
            /*
             * Throw exception as without audit log, the OMRS refuses to start.
             */
            throw new OMRSLogicErrorException(OMRSErrorCode.NO_AUDIT_LOG_DESTINATIONS.getMessageDefinition(localServerName),
                                              this.getClass().getName(),
                                              methodName);
        }

        /*
         * Initialize the audit log
         */
        auditLogDestination = new OMRSAuditLogDestination(localServerName,
                                                          localServerType,
                                                          localOrganizationName,
                                                          getAuditLogStores(repositoryServicesConfig.getAuditLogConnections()));

        auditLog = new OMRSAuditLog(auditLogDestination, OMRSAuditingComponent.OPERATIONAL_SERVICES);

        /*
         * Log that the OMRS has started.
         */
        auditLog.logMessage(actionDescription, OMRSAuditCode.OMRS_AUDIT_LOG_READY.getMessageDefinition(serverTypeClassification, localServerName));
    }


    /**
     * Initialize the OMRS component for the Open Metadata Repository Services (OMRS).  The configuration
     * is taken as is.  Any configuration errors are reported as exceptions.
     *
     * @param repositoryServicesConfig current configuration values
     * @throws RepositoryErrorException there is a problem accessing an open metadata archive
     */
    public void initializeCohortMember(RepositoryServicesConfig repositoryServicesConfig) throws RepositoryErrorException
    {
        final String   actionDescription = "Initialize Repository Services for Cohort Member";
        final String   methodName        = "initializeCohortMember";

        if (repositoryServicesConfig == null)
        {
            /*
             * Throw exception as without configuration information the OMRS can not start.
             */
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_CONFIG.getMessageDefinition(localServerName),
                                              this.getClass().getName(),
                                              methodName);
        }

        if (auditLog == null)
        {
            /*
             * Throw exception as without audit log, the OMRS refuses to start.
             */
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_AUDIT_LOG.getMessageDefinition(),
                                              this.getClass().getName(),
                                              methodName);
        }

        /*
         * Log that the OMRS is starting.  There is another Audit log message logged at the end of this method
         * to confirm that all the pieces started successfully.
         */
        auditLog.logMessage(actionDescription, OMRSAuditCode.OMRS_INITIALIZING.getMessageDefinition());


        /*
         * The audit log is present in all servers.  In addition, metadata servers have at least one additional
         * subsystem enabled. These are the enterprise access services, local repository and the metadata
         * highway (cohort services). Each group has its own config.
         */
        EnterpriseAccessConfig  enterpriseAccessConfig = repositoryServicesConfig.getEnterpriseAccessConfig();
        LocalRepositoryConfig   localRepositoryConfig  = repositoryServicesConfig.getLocalRepositoryConfig();
        List<CohortConfig>      cohortConfigList       = repositoryServicesConfig.getCohortConfigList();

        /*
         * The local repository is optional.  However, the repository content manager is still
         * used to manage the validation of TypeDefs and the creation of metadata instances.
         * It is loaded with any TypeDefs from the archives to seed its in-memory TypeDef cache.
         */
        localRepositoryContentManager
                = new OMRSRepositoryContentManager(localServerUserId,
                                                   auditLog.createNewAuditLog(OMRSAuditingComponent.REPOSITORY_CONTENT_MANAGER));

        /*
         * Begin with the enterprise repository services.  They are always needed since the
         * Open Metadata Access Services (OMAS) is dependent on them.  There are 2 modes of operation: local only
         * and enterprise access.  Enterprise access provide an enterprise view of metadata
         * across all the open metadata repository cohorts that this server connects to.
         * If EnterpriseAccessConfig is null, the enterprise repository services run in local only mode.
         * Otherwise, the supplied configuration properties enable it to be configured for enterprise access.
         *
         * The connector manager manages the list of connectors to metadata repositories that the enterprise
         * repository services will use.  The OMRS Topic is used to publish events from these repositories to support the
         * OMASs' event notifications.
         */
        enterpriseConnectorManager = initializeEnterpriseConnectorManager(enterpriseAccessConfig,
                                                                          maxPageSize,
                                                                          localRepositoryContentManager);
        enterpriseOMRSTopicConnector = initializeEnterpriseOMRSTopicConnector(enterpriseAccessConfig);
        remoteEnterpriseOMRSTopicConnector = initializeRemoteEnterpriseOMRSTopicConnector(enterpriseAccessConfig);

        /*
         * The archive manager loads pre-defined types and instances that are stored in open metadata archives.
         */
        archiveManager = initializeOpenMetadataArchives(repositoryServicesConfig.getOpenMetadataArchiveConnections());

        /*
         * Start up the local repository if one is configured.
         */
        if (localRepositoryConfig != null)
        {
            localMetadataCollectionId = localRepositoryConfig.getMetadataCollectionId();
            localMetadataCollectionName = localRepositoryConfig.getMetadataCollectionName();
            if (localMetadataCollectionName == null)
            {
                localMetadataCollectionName = localServerName;
            }

            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.LOCAL_REPOSITORY_INITIALIZING.getMessageDefinition(localMetadataCollectionName,
                                                                                                 localMetadataCollectionId));

            /*
             * Supports outbound events from the local repository
             */
            localRepositoryEventManager =
                    new OMRSRepositoryEventManager("local repository outbound",
                                                   new OMRSRepositoryEventExchangeRule(localRepositoryConfig.getEventsToSendRule(),
                                                                                       localRepositoryConfig.getSelectedTypesToSend()),
                                                   new OMRSRepositoryContentValidator(localRepositoryContentManager),
                                                   auditLog.createNewAuditLog(OMRSAuditingComponent.REPOSITORY_EVENT_MANAGER));

            /*
             * If the enterprise repository services topic is active, then register an event publisher for it.
             * This topic is active if the Open Metadata Access Services (OMASs) are active.
             */
            if (enterpriseOMRSTopicConnector != null)
            {
                OMRSRepositoryEventPublisher
                        enterpriseEventPublisher = new OMRSRepositoryEventPublisher("Local Repository to Local Enterprise",
                                                                                    enterpriseOMRSTopicConnector,
                                                                                    auditLog.createNewAuditLog(OMRSAuditingComponent.EVENT_PUBLISHER));

                this.localRepositoryEventManager.registerRepositoryEventProcessor(enterpriseEventPublisher);
            }

            /*
             * If the remote enterprise repository services topic is active, then register an event publisher for it.
             * This topic is active if the server is explicitly configured for it.
             */
            if (remoteEnterpriseOMRSTopicConnector != null)
            {
                OMRSRepositoryEventPublisher
                        enterpriseEventPublisher = new OMRSRepositoryEventPublisher("Local Repository to Remote Enterprise",
                                                                                    remoteEnterpriseOMRSTopicConnector,
                                                                                    auditLog.createNewAuditLog(OMRSAuditingComponent.EVENT_PUBLISHER));

                this.localRepositoryEventManager.registerRepositoryEventProcessor(enterpriseEventPublisher);
            }

            /*
             * Pass the local metadata collectionId to the AuditLog
             */
            auditLogDestination.setLocalMetadataCollectionId(localMetadataCollectionId);

            localRepositoryConnector = initializeLocalRepository(localRepositoryConfig);

            /*
             * Start processing of events for the local repository.
             */
            try
            {
                localRepositoryConnector.start();
            }
            catch (Exception error)
            {
                auditLog.logMessage(actionDescription,
                                    OMRSAuditCode.LOCAL_REPOSITORY_FAILED_TO_START.getMessageDefinition(error.getMessage()));

                throw new OMRSLogicErrorException(OMRSErrorCode.LOCAL_REPOSITORY_FAILED_TO_START.getMessageDefinition(localServerName,
                                                                                                                      error.getClass().getName(),
                                                                                                                      error.getMessage()),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  error);
            }
        }


        /*
         * This is the point at which the open metadata archives will be processed.  The archives are processed
         * using the same mechanisms as TypeDef/Instance events received from other members of the cohort.  This
         * is because the metadata in the archives is effectively reference metadata that is owned by the archive
         * and should not be updated in the local repository.
         *
         * Note that if the local repository is not configured then only TypeDefs are processed because there
         * is nowhere to store the instances.  The TypeDefs are used for validation of metadata that is passed to
         * the enterprise repository services.
         */
        if (localRepositoryConnector != null)
        {
            archiveManager.setLocalRepository(localMetadataCollectionId,
                                              localRepositoryContentManager,
                                              localRepositoryConnector.getIncomingInstanceEventProcessor());
        }
        else
        {
            archiveManager.setLocalRepository(localMetadataCollectionId,
                                              localRepositoryContentManager,
                                              null);
        }

        /*
         * Connect the local repository connector to the connector manager if they both exist.  This means
         * that enterprise repository requests will include metadata from the local repository.
         */
        if ((localRepositoryConnector != null) && (enterpriseConnectorManager != null))
        {
            enterpriseConnectorManager.setLocalConnector(localRepositoryConnector.getMetadataCollectionId(),
                                                         localRepositoryConnector);
        }

        /*
         * Local operation is ready, now connect to the metadata highway.
         */
        if (cohortConfigList != null)
        {
            auditLog.logMessage(actionDescription, OMRSAuditCode.METADATA_HIGHWAY_INITIALIZING.getMessageDefinition());

            metadataHighwayManager = initializeCohorts(localServerName,
                                                       localServerType,
                                                       localOrganizationName,
                                                       localRepositoryConnector,
                                                       localRepositoryContentManager,
                                                       enterpriseConnectorManager,
                                                       enterpriseOMRSTopicConnector,
                                                       cohortConfigList);
        }

        /*
         * Set up the OMRS REST Services with the local repository, so it is able to process incoming REST
         * calls.
         */
        OMRSRepositoryRESTServices.setServerRepositories(localServerName,
                                                         auditLog,
                                                         localRepositoryConnector,
                                                         this.getEnterpriseOMRSRepositoryConnector(OMRSAuditingComponent.REST_SERVICES.getComponentName()),
                                                         this.getRemoteEnterpriseOMRSTopicConnection(enterpriseAccessConfig),
                                                         metadataHighwayManager,
                                                         localServerURL,
                                                         auditLog.createNewAuditLog(OMRSAuditingComponent.REST_SERVICES),
                                                         maxPageSize);

        /*
         * All done and no exceptions :)
         */
        auditLog.logMessage(actionDescription, OMRSAuditCode.OMRS_INITIALIZED.getMessageDefinition());
    }


    /**
     * The local repository (if configured) has been started while the archives were loaded and the
     * cohorts initialized.  During this time, outbound repository events have been buffered.
     * Calling start() releases these buffered events into the cohort(s).
     */
    public void startOutboundEvents()
    {
        if (localRepositoryEventManager != null)
        {
            localRepositoryEventManager.start();
        }
    }


    /**
     * Initialize the OMRS component for the Open Metadata Repository Services (OMRS).  The configuration
     * is taken as is.  Any configuration errors are reported as exceptions.
     *
     * @param repositoryServicesConfig current configuration values
     */
    public void initializeGovernanceServer(RepositoryServicesConfig repositoryServicesConfig)
    {
        final String   actionDescription = "Initialize Repository Services for Governance Server";
        final String   methodName        = "initializeGovernanceServer";

        initializeSimpleServer(repositoryServicesConfig, actionDescription, methodName);
    }


    /**
     * Initialize the OMRS component for the Open Metadata Repository Services (OMRS).  The configuration
     * is taken as is.  Any configuration errors are reported as exceptions.
     *
     * @param repositoryServicesConfig current configuration values
     */
    public void initializeViewServer(RepositoryServicesConfig repositoryServicesConfig)
    {
        final String   actionDescription = "Initialize Repository Services for View Server";
        final String   methodName        = "initializeViewServer";

        initializeSimpleServer(repositoryServicesConfig, actionDescription, methodName);
    }


    /**
     * Initialize the OMRS component for the Open Metadata Repository Services (OMRS).  The configuration
     * is taken as is.  Any configuration errors are reported as exceptions.
     *
     * @param repositoryServicesConfig current configuration values
     */
    private void initializeSimpleServer(RepositoryServicesConfig repositoryServicesConfig,
                                        String                   actionDescription,
                                        String                   methodName)
    {
        if (repositoryServicesConfig == null)
        {
            /*
             * Throw exception as without configuration information the OMRS can not start.
             */
           throw new OMRSLogicErrorException(OMRSErrorCode.NULL_CONFIG.getMessageDefinition(localServerName),
                                              this.getClass().getName(),
                                              methodName);
        }

        if (auditLog == null)
        {
            /*
             * Throw exception as without audit log, the OMRS refuses to start.
             */
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_AUDIT_LOG.getMessageDefinition(),
                                              this.getClass().getName(),
                                              methodName);
        }

        /*
         * Log that the OMRS is starting.  There is another Audit log message logged at the end of this method
         * to confirm that all the pieces started successfully.
         */
        auditLog.logMessage(actionDescription, OMRSAuditCode.OMRS_INITIALIZING.getMessageDefinition());


        /*
         * Set up the OMRS REST Services with the local repository, so it is able to process incoming REST calls.
         */
        OMRSRepositoryRESTServices.setServerRepositories(localServerName,
                                                         auditLog,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         auditLog.createNewAuditLog(OMRSAuditingComponent.REST_SERVICES),
                                                         maxPageSize);

        /*
         * All done and no exceptions :)
         */
        auditLog.logMessage(actionDescription, OMRSAuditCode.OMRS_INITIALIZED.getMessageDefinition());
    }



    /**
     * Return the connector to the Enterprise OMRS Topic.  If null is returned it means the Enterprise OMRS Topic
     * is not needed.  A configuration error exception is thrown if there is a problem with the connection properties
     *
     * @param enterpriseAccessConfig configuration from the OMAG server
     * @return connector to the Enterprise OMRS Topic or null
     */
    private OMRSTopicConnector  initializeEnterpriseOMRSTopicConnector(EnterpriseAccessConfig  enterpriseAccessConfig)
    {
        OMRSTopicConnector    enterpriseOMRSTopicConnector = null;

        if (enterpriseAccessConfig != null)
        {
            Connection enterpriseOMRSTopicConnection = enterpriseAccessConfig.getEnterpriseOMRSTopicConnection();

            if (enterpriseOMRSTopicConnection != null)
            {
                enterpriseOMRSTopicConnector = getTopicConnector("Enterprise Access", enterpriseOMRSTopicConnection);

                /*
                 * This connector is started by admin services when all the Access Services have been started and have registered their listeners.
                 */
            }
        }

        return enterpriseOMRSTopicConnector;
    }


    /**
     * Return the connector to the Remote Enterprise OMRS Topic.  If null is returned it means the Remote Enterprise OMRS Topic
     * is not needed.  A configuration error exception is thrown if there is a problem with the connection properties
     *
     * @param enterpriseAccessConfig configuration from the OMAG server
     * @return connector to the Remote Enterprise OMRS Topic or null
     */
    private OMRSTopicConnector  initializeRemoteEnterpriseOMRSTopicConnector(EnterpriseAccessConfig  enterpriseAccessConfig)
    {
        final String methodName = "initializeRemoteEnterpriseOMRSTopicConnector";

        OMRSTopicConnector    enterpriseOMRSTopicConnector = null;

        if (enterpriseAccessConfig != null)
        {
            try
            {
                Connection enterpriseOMRSTopicConnection = enterpriseAccessConfig.getRemoteEnterpriseOMRSTopicConnection();

                if (enterpriseOMRSTopicConnection != null)
                {
                    enterpriseOMRSTopicConnector = getTopicConnector("Remote Enterprise Access", enterpriseOMRSTopicConnection);
                    enterpriseOMRSTopicConnector.start();
                }
            }
            catch (Exception error)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }

        return enterpriseOMRSTopicConnector;
    }


    /**
     * Return the connection of the Remote Enterprise OMRS Topic.  If null is returned it means the Remote Enterprise OMRS Topic
     * is not needed.
     *
     * @param enterpriseAccessConfig configuration from the OMAG server
     * @return connection for the Remote Enterprise OMRS Topic or null
     */
    private Connection getRemoteEnterpriseOMRSTopicConnection(EnterpriseAccessConfig  enterpriseAccessConfig)
    {
        if (enterpriseAccessConfig != null)
        {
            return enterpriseAccessConfig.getRemoteEnterpriseOMRSTopicConnection();
        }

        return null;
    }


    /**
     * Initialize the OMRSEnterpriseConnectorManager and the EnterpriseOMRSConnector class.  If the
     * enterprise access configuration is null it means federation is not enabled.  However, the enterprise
     * connector manager is still initialized to pass the local repository information to the Enterprise
     * OMRS repository connectors.
     *
     * @param enterpriseAccessConfig enterprise access configuration from the OMAG server
     * @param maxPageSize maximum number of results that can be returned
     * @param repositoryContentManager type knowledge base and other utilities
     * @return initialized OMRSEnterpriseConnectorManager object
     */
    private OMRSEnterpriseConnectorManager initializeEnterpriseConnectorManager(EnterpriseAccessConfig        enterpriseAccessConfig,
                                                                                int                           maxPageSize,
                                                                                OMRSRepositoryContentManager  repositoryContentManager)
    {
        OMRSEnterpriseConnectorManager   enterpriseConnectorManager;

        if (enterpriseAccessConfig == null)
        {
            /*
             * Federation is not enabled in this server
             */
            enterpriseConnectorManager = new OMRSEnterpriseConnectorManager(false,
                                                                            maxPageSize,
                                                                            repositoryContentManager,
                                                                            auditLog.createNewAuditLog(OMRSAuditingComponent.ENTERPRISE_CONNECTOR_MANAGER),
                                                                            localServerUserId,
                                                                            localServerPassword);
        }
        else
        {
            /*
             * Enterprise access is enabled in this server
             */
            final String   actionDescription = "Initialize Repository Operational Services";

            auditLog.logMessage(actionDescription, OMRSAuditCode.ENTERPRISE_ACCESS_INITIALIZING.getMessageDefinition());

            enterpriseConnectorManager = new OMRSEnterpriseConnectorManager(true,
                                                                            maxPageSize,
                                                                            repositoryContentManager,
                                                                            auditLog.createNewAuditLog(OMRSAuditingComponent.ENTERPRISE_CONNECTOR_MANAGER),
                                                                            localServerUserId,
                                                                            localServerPassword);

            /*
             * Save information about the enterprise metadata collection for the OMRSEnterpriseConnectorProvider class as
             * this information is needed by each instance of the EnterpriseOMRSConnector.
             */
            enterpriseMetadataCollectionId = enterpriseAccessConfig.getEnterpriseMetadataCollectionId();
            enterpriseMetadataCollectionName = enterpriseAccessConfig.getEnterpriseMetadataCollectionName();
        }

        return enterpriseConnectorManager;
    }


    /**
     * If the local repository is configured then set up the local repository connector.  The
     * information for the local repository's OMRS Repository Connector is configured as a OCF connection in
     * the local repository config.  In fact there are potentially 2 connections configured.  There is a connection
     * for remote access to the local repository and an optional connection for a locally optimized connector to use
     * within the local server.
     *
     * @param localRepositoryConfig local repository config.
     * @return wrapped OMRS Repository Connector
     */
    private LocalOMRSRepositoryConnector  initializeLocalRepository(LocalRepositoryConfig  localRepositoryConfig)
    {
        LocalOMRSRepositoryConnector  localRepositoryConnector = null;

        /*
         * If the local repository is configured then create the connector to the local repository and
         * configure it.  It is valid to have a server with no local repository.
         */
        if (localRepositoryConfig != null)
        {
            /*
             * Create the local repository's Connector Provider.  This is a special connector provider that
             * creates an OMRS Repository Connector that wraps the real OMRS Repository Connector.  The
             * outer OMRS Repository Connector manages events, audit logging and error handling.
             */
            LocalOMRSConnectorProvider localConnectorProvider =
                    new LocalOMRSConnectorProvider(localMetadataCollectionId,
                                                   localRepositoryConfig.getLocalRepositoryMode(),
                                                   localRepositoryConfig.getLocalRepositoryRemoteConnection(),
                                                   getLocalRepositoryEventMapper(localRepositoryConfig.getEventMapperConnection()),
                                                   localRepositoryEventManager,
                                                   localRepositoryContentManager,
                                                   new OMRSRepositoryEventExchangeRule(localRepositoryConfig.getEventsToSaveRule(),
                                                                                       localRepositoryConfig.getSelectedTypesToSave()),
                                                   auditLog);


            /*
             * Create the local repository's connector.  If there is no locally optimized connection, the
             * remote connection is used.
             */
            Connection                    localRepositoryConnection;

            if (localRepositoryConfig.getLocalRepositoryLocalConnection() != null)
            {
                localRepositoryConnection = localRepositoryConfig.getLocalRepositoryLocalConnection();
            }
            else
            {
                localRepositoryConnection = localRepositoryConfig.getLocalRepositoryRemoteConnection();
            }
            localRepositoryConnector = this.getLocalOMRSConnector(localRepositoryConnection,
                                                                  localConnectorProvider);
        }

        return localRepositoryConnector;
    }


    /**
     * Return an OMRS archive manager configured with the list of Open Metadata Archive Stores to use.
     *
     * @param openMetadataArchiveConnections connections to the open metadata archive stores
     * @return OMRS archive manager
     */
    private OMRSArchiveManager initializeOpenMetadataArchives(List<Connection>    openMetadataArchiveConnections)
    {
        ArrayList<OpenMetadataArchiveStoreConnector> openMetadataArchives = null;

        if (openMetadataArchiveConnections != null)
        {
            openMetadataArchives = new ArrayList<>();

            for (Connection archiveConnection : openMetadataArchiveConnections)
            {
                if (archiveConnection != null)
                {
                    /*
                     * Any problems creating the connectors will result in an exception.
                     */
                    openMetadataArchives.add(this.getOpenMetadataArchiveStore(archiveConnection));
                }
            }
        }

        return new OMRSArchiveManager(openMetadataArchives,
                                      auditLog.createNewAuditLog(OMRSAuditingComponent.ARCHIVE_MANAGER));
    }


    /**
     * A server can optionally connect to one or more open metadata repository cohorts.  There is one
     * CohortConfig for each cohort that the server is to connect to.  The communication between
     * members of a cohort is event-based.  The parameters provide supplied to the metadata highway manager
     * include values need to send compliant OMRS Events.
     *
     * @param localServerName the name of the local server. This value flows in OMRS Events.
     * @param localServerType the type of the local server. This value flows in OMRS Events.
     * @param localOrganizationName the name of the organization that owns this server.
     *                              This value flows in OMRS Events.
     * @param localRepositoryConnector the local repository connector is supplied if there is a local repository
     *                                 for this server.
     * @param localRepositoryContentManager repository content manager for this server
     * @param connectionConsumer the connection consumer is from the enterprise repository services.  It
     *                           receives connection information about the other members of the cohort(s)
     *                           to enable enterprise access.
     * @param enterpriseTopicConnector connector to the enterprise repository services Topic Connector.
     *                                 The cohorts replicate their events to the enterprise OMRS Topic so
     *                                 the Open Metadata Access Services (OMASs) can monitor changing metadata.
     * @param cohortConfigList list of cohorts to connect to (and the configuration to do it)
     * @return newly created and initialized metadata highway manager.
     */
    private OMRSMetadataHighwayManager  initializeCohorts(String                          localServerName,
                                                          String                          localServerType,
                                                          String                          localOrganizationName,
                                                          LocalOMRSRepositoryConnector    localRepositoryConnector,
                                                          OMRSRepositoryContentManager    localRepositoryContentManager,
                                                          OMRSConnectionConsumer          connectionConsumer,
                                                          OMRSTopicConnector              enterpriseTopicConnector,
                                                          List<CohortConfig>              cohortConfigList)
    {
        /*
         * The metadata highway manager is constructed with the values that are the same for every cohort.
         */
        OMRSMetadataHighwayManager  metadataHighwayManager = new OMRSMetadataHighwayManager(localServerName,
                                                                                            localServerType,
                                                                                            localOrganizationName,
                                                                                            localRepositoryConnector,
                                                                                            localRepositoryContentManager,
                                                                                            connectionConsumer,
                                                                                            enterpriseTopicConnector,
                                                                                            auditLog.createNewAuditLog(OMRSAuditingComponent.METADATA_HIGHWAY_MANAGER));

        /*
         * The metadata highway manager is initialized with the details specific to each cohort.
         */
        metadataHighwayManager.initialize(cohortConfigList);

        return metadataHighwayManager;
    }


    /**
     * Set up a new security verifier (the handler runs with a default verifier until this
     * method is called).
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    public void setSecurityVerifier(OpenMetadataServerSecurityVerifier securityVerifier)
    {
        if (securityVerifier != null)
        {
            if (localRepositoryConnector != null)
            {
                localRepositoryConnector.setSecurityVerifier(securityVerifier);
            }

            if (metadataHighwayManager != null)
            {
                metadataHighwayManager.setSecurityVerifier(securityVerifier);
            }
        }
    }


    /**
     * Add an open metadata archive to the local repository.
     *
     * @param serverName name of called server
     * @param openMetadataArchiveConnection connection to the archive
     * @param archiveSource descriptive name of the archive source
     * @throws InvalidParameterException the archive resource is not found
     * @throws RepositoryErrorException there is a problem with the archive manager
     */
    public void addOpenMetadataArchive(String        serverName,
                                       Connection    openMetadataArchiveConnection,
                                       String        archiveSource) throws InvalidParameterException,
                                                                           RepositoryErrorException
    {
        final String methodName = "addOpenMetadataArchive";
        final String serverNameParameterName = "serverName";

        if (archiveManager != null)
        {
            archiveManager.addOpenMetadataArchive(this.getOpenMetadataArchiveStore(openMetadataArchiveConnection),
                                                  archiveSource);
        }
        else
        {
            throw new InvalidParameterException(OMRSErrorCode.ARCHIVE_MANAGER_NOT_ACTIVE.getMessageDefinition(serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                serverNameParameterName);
        }
    }




    /**
     * Add an open metadata archive to the local repository.
     *
     * @param serverName name of called server
     * @param openMetadataArchive content the archive
     * @param archiveSource descriptive name of the archive source
     * @throws InvalidParameterException the archive resource is not found
     * @throws RepositoryErrorException there is a problem with the archive manager
     */
    public void addOpenMetadataArchive(String                   serverName,
                                       OpenMetadataArchiveStore openMetadataArchive,
                                       String                   archiveSource) throws InvalidParameterException,
                                                                                      RepositoryErrorException
    {
        final String methodName = "addOpenMetadataArchive";
        final String serverNameParameterName = "serverName";

        if (archiveManager != null)
        {
            archiveManager.addOpenMetadataArchive(openMetadataArchive, archiveSource);
        }
        else
        {
            throw new InvalidParameterException(OMRSErrorCode.ARCHIVE_MANAGER_NOT_ACTIVE.getMessageDefinition(serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                serverNameParameterName);
        }
    }


    /**
     * Shutdown the Open Metadata Repository Services.
     *
     * @param permanent boolean flag indicating whether this server permanently shutting down or not
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect(boolean   permanent)
    {
        /*
         * Log that the OMRS is disconnecting.  There is another Audit log message logged at the end of this method
         * to confirm that all the pieces disconnected successfully.
         */
        final String   actionDescription = "Disconnect Repository Operational Services";
        auditLog.logMessage(actionDescription, OMRSAuditCode.OMRS_DISCONNECTING.getMessageDefinition());

        OMRSRepositoryRESTServices.stopInboundRESTCalls(localServerName);

        if (metadataHighwayManager != null)
        {
            metadataHighwayManager.disconnect(permanent);
        }

        /*
         * Shutdown publishing events from all cohorts to external listeners
         */
        if (remoteEnterpriseOMRSTopicConnector != null)
        {
            try
            {
                remoteEnterpriseOMRSTopicConnector.disconnect();
            }
            catch (Exception  error)
            {
                auditLog.logMessage(actionDescription, OMRSAuditCode.ENTERPRISE_TOPIC_DISCONNECT_ERROR.getMessageDefinition("remote" ,
                                                                                                                            error.getClass().getName(),
                                                                                                                            error.getMessage()));
            }
        }

        /*
         * Shutdown publishing events from all cohorts to OMASs
         */
        if (enterpriseOMRSTopicConnector != null)
        {
            try
            {
                enterpriseOMRSTopicConnector.disconnect();
            }
            catch (Exception  error)
            {
                auditLog.logMessage(actionDescription, OMRSAuditCode.ENTERPRISE_TOPIC_DISCONNECT_ERROR.getMessageDefinition("local" ,
                                                                                                                            error.getClass().getName(),
                                                                                                                            error.getMessage()));
            }
        }

        /*
         * This will disconnect all repository connectors, both local and remote so federated queries are no longer enabled.
         */
        if (enterpriseConnectorManager != null)
        {
            try
            {
                enterpriseConnectorManager.disconnect();
            }
            catch (Exception  error)
            {
                auditLog.logMessage(actionDescription, OMRSAuditCode.ENTERPRISE_CONNECTOR_DISCONNECT_ERROR.getMessageDefinition(error.getMessage()));
            }
        }

        if (archiveManager != null)
        {
            archiveManager.close();
        }

        auditLog.logMessage(actionDescription, OMRSAuditCode.OMRS_DISCONNECTED.getMessageDefinition());

        return true;
    }


    /**
     * Return the connectors to the AuditLog store using the connection information supplied.  If there is a
     * problem with the connection information that means a connector can not be created, an exception is thrown.
     *
     * @param auditLogStoreConnections properties for the audit log stores
     * @return audit log store connector
     */
    private List<OMRSAuditLogStore>  getAuditLogStores(List<Connection> auditLogStoreConnections)
    {
        List<OMRSAuditLogStore>   auditLogStores = new ArrayList<>();

        for (Connection auditLogStoreConnection : auditLogStoreConnections)
        {
            auditLogStores.add(getAuditLogStore(auditLogStoreConnection));
        }

        if (auditLogStores.isEmpty())
        {
            return null;
        }
        else
        {
            return auditLogStores;
        }
    }


    /**
     * Return a connector to an audit log store.
     *
     * @param auditLogStoreConnection connection with the parameters of the audit log store
     * @return connector for audit log store.
     */
    private OMRSAuditLogStore getAuditLogStore(Connection   auditLogStoreConnection)
    {
        try
        {
            ConnectorBroker         connectorBroker = new ConnectorBroker();
            Connector               connector       = connectorBroker.getConnector(auditLogStoreConnection);

            connector.start();
            return (OMRSAuditLogStore)connector;
        }
        catch (Exception   error)
        {
            String methodName = "getAuditLogStore";

            log.debug("Unable to create audit log store connector: " + error);

            throw new OMRSConfigErrorException(OMRSErrorCode.NULL_AUDIT_LOG_STORE.getMessageDefinition(localServerName,
                                                                                                       error.getClass().getName(),
                                                                                                       error.getMessage()),
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
     * @param sourceName name of the user of this topic
     * @param topicConnection connection parameters
     * @return OMRSTopicConnector for managing communications with the event/messaging infrastructure.
     */
    private OMRSTopicConnector getTopicConnector(String     sourceName,
                                                 Connection topicConnection)
    {
        try
        {
            ConnectorBroker    connectorBroker = new ConnectorBroker(auditLog);
            Connector          connector       = connectorBroker.getConnector(topicConnection);

            return (OMRSTopicConnector)connector;
        }
        catch (Exception   error)
        {
            String methodName = "getTopicConnector";

            log.debug("Unable to create topic connector: " + error);

            auditLog.logMessage(methodName,
                                OMRSAuditCode.BAD_TOPIC_CONNECTION.getMessageDefinition(sourceName, error.getClass().getName(), error.getMessage()));

            throw new OMRSConfigErrorException(OMRSErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(sourceName),
                                               this.getClass().getName(),
                                               methodName,
                                               error);

        }
    }


    /**
     * Return the connector to an open metadata archive store.  Each connector instance can access a single
     * open metadata archive.  If there is a problem with the connection information
     * that means a connector can not be created, an exception is thrown.
     *
     * @param openMetadataArchiveStoreConnection properties used to create the connection
     * @return open metadata archive connector
     */
    private OpenMetadataArchiveStoreConnector  getOpenMetadataArchiveStore(Connection   openMetadataArchiveStoreConnection)
    {
        try
        {
            ConnectorBroker          connectorBroker = new ConnectorBroker(auditLog);
            Connector                connector       = connectorBroker.getConnector(openMetadataArchiveStoreConnection);

            return (OpenMetadataArchiveStoreConnector)connector;
        }
        catch (Exception   error)
        {
            String methodName = "getOpenMetadataArchiveStore";

            if (log.isDebugEnabled())
            {
                log.debug("Unable to create open metadata archive connector: " + error);
            }

            /*
             * Throw runtime exception to indicate that the open metadata archive store is not available.
             */
            auditLog.logMessage(methodName, OMRSAuditCode.BAD_ARCHIVE_STORE.getMessageDefinition(error.getClass().getName(), error.getMessage()));

            throw new OMRSConfigErrorException(OMRSErrorCode.NULL_ARCHIVE_STORE.getMessageDefinition(localServerName),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }


    /**
     * The local repository may need an event mapper to convert its proprietary events to OMRS Events.
     * An event mapper is implemented as an OMRSRepositoryEventMapper Connector, and it is initialized through the
     * OCF Connector Broker using an OCF connection.
     *
     * @param localRepositoryEventMapperConnection connection to the local repository's event mapper.
     * @return local repository's event mapper
     */
    private OMRSRepositoryEventMapperConnector getLocalRepositoryEventMapper(Connection   localRepositoryEventMapperConnection)
    {
        /*
         * If the event mapper is null it means the local repository does not need an event mapper.
         * This is not an error.
         */
        if (localRepositoryEventMapperConnection == null)
        {
            return null;
        }

        /*
         * The event mapper is a pluggable component that is implemented as an OCF connector.  Its configuration is
         * passed to it in a Connection object and the ConnectorBroker manages its creation and initialization.
         */
        try
        {
            ConnectorBroker           connectorBroker = new ConnectorBroker(auditLog);
            Connector                 connector       = connectorBroker.getConnector(localRepositoryEventMapperConnection);

            return (OMRSRepositoryEventMapperConnector)connector;
        }
        catch (Exception   error)
        {
            String methodName = "getLocalRepositoryEventMapper";

            log.debug("Unable to create local repository event mapper connector: " + error);

            /*
             * Throw runtime exception to indicate that the local repository's event mapper is not available.
             */
           auditLog.logMessage(methodName,
                                OMRSAuditCode.BAD_REAL_LOCAL_EVENT_MAPPER.getMessageDefinition(error.getClass().getName(), error.getMessage()));

            throw new OMRSConfigErrorException(OMRSErrorCode.NULL_EVENT_MAPPER.getMessageDefinition(localServerName),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }



    /**
     * Private method to convert a Connection into a LocalOMRSRepositoryConnector using the LocalOMRSConnectorProvider.
     * The supplied connection is for the real local connector.  LocalOMRSRepositoryConnector will create the
     * real local connector and ensure all requests it receives are passed to it.
     *
     * @param connection Connection properties for the real local connection
     * @param connectorProvider connector provider to create the repository connector
     * @return LocalOMRSRepositoryConnector wrapping the real local connector
     */
    private LocalOMRSRepositoryConnector getLocalOMRSConnector(Connection                       connection,
                                                               LocalOMRSConnectorProvider       connectorProvider)
    {
        String     methodName = "getLocalOMRSConnector";

        /*
         * Although the localOMRSConnector is an OMRSRepositoryConnector, its initialization is
         * managed directly with its connector provider (rather than using the connector broker) because it
         * needs access to a variety of OMRS components in order for it to support access to the local
         * repository by other OMRS components.  As such it needs more variables at initialization.
         */
        try
        {
            LocalOMRSRepositoryConnector localRepositoryConnector = (LocalOMRSRepositoryConnector)connectorProvider.getConnector(connection);

            localRepositoryConnector.setAuditLog(auditLog.createNewAuditLog(OMRSAuditingComponent.LOCAL_REPOSITORY_CONNECTOR));
            localRepositoryConnector.setMaxPageSize(maxPageSize);
            localRepositoryConnector.setServerName(localServerName);
            localRepositoryConnector.setServerType(localServerType);
            localRepositoryConnector.setServerUserId(localServerUserId);
            localRepositoryConnector.setOrganizationName(localOrganizationName);
            localRepositoryConnector.setRepositoryHelper(new OMRSRepositoryContentHelper(localRepositoryContentManager));
            localRepositoryConnector.setRepositoryValidator(new OMRSRepositoryContentValidator(localRepositoryContentManager));
            /*
             * Ensure that the metadataCollectionName is set before calling setMetadataCollectionId()
             * otherwise the connector will create the metadataCollection adopting the (default) server name.
             */
            localRepositoryConnector.setMetadataCollectionName(localMetadataCollectionName);
            localRepositoryConnector.setMetadataCollectionId(localMetadataCollectionId);


            return localRepositoryConnector;
        }
        catch (Exception  error)
        {
            /*
             * If there is a problem initializing the connector then the ConnectorBroker will have created a
             * detailed exception already.  The only error case that this method has introduced is the cast
             * of the Connector to OMRSRepositoryConnector.  This could occur if the connector configured is a valid
             * OCF Connector but not an OMRSRepositoryConnector.
             */
            auditLog.logException(methodName,
                                  OMRSAuditCode.BAD_REAL_LOCAL_REPOSITORY_CONNECTOR.getMessageDefinition(error.getClass().getName(),
                                                                                                         error.getMessage()),
                                  error);

            throw new OMRSConfigErrorException(OMRSErrorCode.BAD_REAL_LOCAL_REPOSITORY_CONNECTOR.getMessageDefinition(error.getClass().getName(),
                                                                                                                      error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }
}
