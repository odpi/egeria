/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.metadatahighway;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.odpi.openmetadata.repositoryservices.events.OpenMetadataEventsSecurity;
import org.odpi.openmetadata.repositoryservices.properties.CohortConnectionStatus;
import org.odpi.openmetadata.repositoryservices.properties.CohortDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConnectorErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.OMRSCohortRegistryStore;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentValidator;
import org.odpi.openmetadata.repositoryservices.metadatahighway.cohortregistry.OMRSCohortRegistry;
import org.odpi.openmetadata.repositoryservices.eventmanagement.*;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectionConsumer;
import org.odpi.openmetadata.repositoryservices.localrepository.OMRSLocalRepository;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventExchangeRule;

import java.util.List;


/**
 * The OMRSCohortManager manages the components that connect to a single open metadata repository cohort.
 */
public class OMRSCohortManager
{
    private String                     cohortName                   = null;
    private OMRSTopicConnector         cohortTopicConnector         = null;
    private Connection                 cohortTopicConnection        = null;
    private OMRSRepositoryEventManager cohortRepositoryEventManager = null;
    private OMRSCohortRegistry         cohortRegistry               = null;
    private CohortConnectionStatus     cohortConnectionStatus       = CohortConnectionStatus.NOT_INITIALIZED;

    private String                       localMetadataCollectionId        = null;
    private OMRSRepositoryEventPublisher outboundRepositoryEventPublisher = null;

    private AuditLog                     auditLog;

    private static final Logger log = LoggerFactory.getLogger(OMRSCohortManager.class);


    /**
     * Main Constructor that relies on the initialization of variables in their declaration.
     *
     * @param auditLog audit log for this component.
     */
    OMRSCohortManager(AuditLog     auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * The constructor defines the minimum information necessary to connect to a cohort.  If these values
     * are not correctly configured, the constructor will throw an exception.
     *
     * @param cohortName name of the cohort.  This is a local name used for messages.
     * @param localMetadataCollectionId configured value for the local metadata collection id
     * @param localMetadataCollectionName display name for the local metadata collection
     * @param localServerName the name of the local server. It is a descriptive name for informational purposes.
     * @param localServerType the type of the local server.  It is a descriptive name for informational purposes.
     * @param localOrganizationName the name of the organization that owns the local server/repository.
     *                              It is a descriptive name for informational purposes.
     * @param localRepository link to the local repository may be null.
     * @param localRepositoryContentManager the content manager that stores information about the known types
     * @param connectionConsumer The connection consumer is a component interested in maintaining details of the
     *                           connections to each of the members of the open metadata repository cohort.  If it is
     *                           null, the cohort registry does not publish connections for members of the open
     *                           metadata repository cohort.
     * @param cohortRegistryStore the cohort registry store where details of members of the cohort are kept
     * @param cohortTopicConnector Connector to the cohort's OMRS Topic.
     * @param cohortTopicConnection Connection to the cohort's OMRS Topic.
     * @param enterpriseTopicConnector Connector to the federated OMRS Topic.
     * @param inboundEventExchangeRule rule for processing inbound events.
     */
    public void initialize(String                           cohortName,
                           String                           localMetadataCollectionId,
                           String                           localMetadataCollectionName,
                           String                           localServerName,
                           String                           localServerType,
                           String                           localOrganizationName,
                           OMRSLocalRepository              localRepository,
                           OMRSRepositoryContentManager     localRepositoryContentManager,
                           OMRSConnectionConsumer           connectionConsumer,
                           OMRSTopicConnector               enterpriseTopicConnector,
                           OMRSCohortRegistryStore          cohortRegistryStore,
                           Connection                       cohortTopicConnection,
                           OMRSTopicConnector               cohortTopicConnector,
                           OMRSRepositoryEventExchangeRule  inboundEventExchangeRule)
    {
        final String   actionDescription = "Initialize Cohort Manager";

        log.debug(actionDescription);

        try
        {
            this.cohortName = cohortName;

            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.COHORT_INITIALIZING.getMessageDefinition(cohortName));

            /*
             * Set up the config status.  It is updated multiple times during this method to help detect whether
             * underlying component are hanging in their initialization.  Most of these intermediary states are
             * unlikely to be seen.
             */
            this.cohortConnectionStatus = CohortConnectionStatus.INITIALIZING;

            this.cohortTopicConnection = cohortTopicConnection;
            this.cohortTopicConnector  = cohortTopicConnector;
            this.localMetadataCollectionId = localMetadataCollectionId;

            /*
             * Create the event manager for processing incoming events from the cohort's OMRS Topic.
             */
            this.cohortRepositoryEventManager = new OMRSRepositoryEventManager(cohortName + " cohort inbound",
                                                                               inboundEventExchangeRule,
                                                                               new OMRSRepositoryContentValidator(localRepositoryContentManager),
                                                                               auditLog.createNewAuditLog(OMRSAuditingComponent.REPOSITORY_EVENT_MANAGER));

            /*
             * Create an event publisher for the cohort registry to use to send registration requests.
             */
            OMRSRegistryEventPublisher outboundRegistryEventProcessor = new OMRSRegistryEventPublisher(cohortName,
                                                                                                       cohortTopicConnector,
                                                                                                       auditLog.createNewAuditLog(OMRSAuditingComponent.EVENT_PUBLISHER));

            /*
             * Create the cohort registry.
             */
            this.cohortRegistry = new OMRSCohortRegistry(auditLog.createNewAuditLog(OMRSAuditingComponent.COHORT_REGISTRY));

            /*
             * The presence/absence of the local repository affects the behaviour of the cohort registry.
             */
            if (localRepository != null)
            {
                /*
                 * The local repository is present so set up the CohortRegistry to play a full role in the protocol.
                 */
                this.cohortRegistry.initialize(cohortName,
                                               localMetadataCollectionId,
                                               localMetadataCollectionName,
                                               localRepository.getLocalRepositoryRemoteConnection(),
                                               localServerName,
                                               localServerType,
                                               localOrganizationName,
                                               outboundRegistryEventProcessor,
                                               cohortRegistryStore,
                                               connectionConsumer);

                OMRSRepositoryEventManager localRepositoryEventManager = localRepository.getOutboundRepositoryEventManager();

                if (localRepositoryEventManager != null)
                {
                    /*
                     * Register an event publisher with the local repository for this cohort.  This will mean
                     * other members of the cohort can receive events from the local server's repository.
                     */
                    outboundRepositoryEventPublisher = new OMRSRepositoryEventPublisher(cohortName,
                                                                                        cohortTopicConnector,
                                                                                        auditLog.createNewAuditLog(OMRSAuditingComponent.EVENT_PUBLISHER));

                    localRepositoryEventManager.registerRepositoryEventProcessor(outboundRepositoryEventPublisher);
                }

                /*
                 * Register the local repository's processors with the cohort's event manager.  This will
                 * route incoming repository events to the local repository.
                 */
                if (localRepository.getIncomingTypeDefEventProcessor() != null)
                {
                    this.cohortRepositoryEventManager.registerTypeDefProcessor(localRepository.getIncomingTypeDefEventProcessor());
                }
                if (localRepository.getIncomingInstanceEventProcessor() != null)
                {
                    this.cohortRepositoryEventManager.registerInstanceProcessor(localRepository.getIncomingInstanceEventProcessor());
                }
            }
            else /* no local repository */
            {
                /*
                 * If there is no local repository, then the cohort registry is focusing on managing registrations
                 * from remote members of the cohort to configure the enterprise access capability.
                 */
                this.cohortRegistry.initialize(cohortName,
                                               null,
                                               null,
                                               null,
                                               localServerName,
                                               localServerType,
                                               localOrganizationName,
                                               outboundRegistryEventProcessor,
                                               cohortRegistryStore,
                                               connectionConsumer);
            }

            /*
             * If the enterprise repositoryservices topic is active, then register an event publisher for it.
             * This topic is active if the Open Metadata Access Services (OMASs) are active.
             */
            if (enterpriseTopicConnector != null)
            {
                OMRSRepositoryEventPublisher enterpriseEventPublisher = new OMRSRepositoryEventPublisher("Cohort to Enterprise",
                                                                                                         enterpriseTopicConnector,
                                                                                                         auditLog.createNewAuditLog(OMRSAuditingComponent.EVENT_PUBLISHER));

                this.cohortRepositoryEventManager.registerRepositoryEventProcessor(enterpriseEventPublisher);
            }

            this.cohortConnectionStatus = CohortConnectionStatus.NEW;
        }
        catch (Exception error)
        {
            log.error("Unable to initialize cohort manager", error);
            this.cohortConnectionStatus = CohortConnectionStatus.CONFIGURATION_ERROR;

            auditLog.logException(actionDescription,
                                  OMRSAuditCode.COHORT_CONFIG_ERROR.getMessageDefinition(cohortName,
                                                                                         error.getClass().getName(),
                                                                                         error.getMessage()),
                                  error);
        }

        log.debug(actionDescription + " COMPLETE");
    }


    /**
     * Set up a new security verifier (the metadata collection runs with a default verifier until this
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
        final String actionDescription = "Initialize Security and Listener";

        if (outboundRepositoryEventPublisher != null)
        {
            outboundRepositoryEventPublisher.setSecurityVerifier(securityVerifier);
        }

        /*
         * Start the cohort's event manager so it is able to pass events.
         */
        if (this.cohortRepositoryEventManager != null)
        {
            this.cohortRepositoryEventManager.start();
        }

        /*
         * The cohort topic connector is used by the local cohort components to communicate with the other
         * members of the cohort.
         */
        if (cohortTopicConnector != null)
        {
            try
            {
                /*
                 * Create the event listener and register it with the cohort OMRS Topic.
                 */
                OMRSEventListener cohortEventListener = new OMRSEventListener(cohortName,
                                                                              localMetadataCollectionId,
                                                                              this.cohortRegistry,
                                                                              this.cohortRepositoryEventManager,
                                                                              securityVerifier,
                                                                              auditLog.createNewAuditLog(OMRSAuditingComponent.EVENT_LISTENER));
                cohortTopicConnector.registerListener(cohortEventListener, cohortName);
                cohortTopicConnector.start();
            }
            catch (Exception error)
            {
                log.error("Unable to initialize event listener", error);
                this.cohortConnectionStatus = CohortConnectionStatus.CONFIGURATION_ERROR;

                auditLog.logException(actionDescription,
                                      OMRSAuditCode.COHORT_CONFIG_ERROR.getMessageDefinition(cohortName,
                                                                                             error.getClass().getName(),
                                                                                             error.getMessage()),
                                      error);
            }

            /*
             * Once the event infrastructure is set up it is ok to send out registration requests to the
             * rest of the cohort.
             */
            this.cohortRegistry.connectToCohort();

            this.cohortConnectionStatus = CohortConnectionStatus.CONNECTED;
        }
    }



    /**
     * Return the name of the cohort.
     *
     * @return String name
     */
    public String getCohortName()
    {
        return cohortName;
    }


    /**
     * Return the local registration for this cohort.
     *
     * @return list of member registrations
     */
    MemberRegistration getLocalRegistration()
    {
        if (cohortRegistry != null)
        {
            return cohortRegistry.getLocalRegistration();
        }

        return null;
    }


    /**
     * Return the properties of the cohort.
     *
     * @return cohort description
     */
    CohortDescription getCohortDescription()
    {
        CohortDescription  description = new CohortDescription();

        description.setCohortName(cohortName);
        description.setTopicConnection(cohortTopicConnection);
        description.setConnectionStatus(cohortConnectionStatus);

        return description;
    }


    /**
     * Return the remote members for this cohort.
     *
     * @return list of member registrations
     */
    List<MemberRegistration> getRemoteMembers()
    {
        if (cohortRegistry != null)
        {
            return cohortRegistry.getRemoteMembers();
        }

        return null;
    }


    /**
     * Return the status of the connection with the metadata highway.
     *
     * @return CohortConnectionStatus
     */
    CohortConnectionStatus getCohortConnectionStatus()
    {
        return cohortConnectionStatus;
    }


    /**
     * Disconnect from the cohort.
     *
     * @param permanent flag indicating if the local repository should unregister from the cohort because it is
     *                  not going ot connect again.
     */
    public synchronized void  disconnect(boolean   permanent)
    {
        final String actionDescription = "Disconnect Cohort Manager";

        log.debug(actionDescription);

        try
        {
            cohortConnectionStatus = CohortConnectionStatus.DISCONNECTING;

            if (cohortRegistry != null)
            {
                cohortRegistry.disconnectFromCohort(permanent);
            }

            if (cohortTopicConnector != null)
            {
                cohortTopicConnector.disconnect();
            }

            cohortConnectionStatus = CohortConnectionStatus.DISCONNECTED;
        }
        catch (ConnectorCheckedException   error)
        {
            log.debug(actionDescription + " FAILED with connector checked exception");

            /*
             * Throw runtime exception to indicate that the cohort registry is not available.
             */
            throw new OMRSConnectorErrorException(OMRSErrorCode.COHORT_DISCONNECT_FAILED.getMessageDefinition(cohortName),
                                                  this.getClass().getName(),
                                                  actionDescription,
                                                  error);

        }
        catch (Exception  error)
        {
            log.debug(actionDescription + " FAILED with exception");

            throw error;
        }

        log.debug(actionDescription + " COMPLETE");
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMRSCohortManager{" +
                "cohortName='" + cohortName + '\'' +
                ", cohortConnectionStatus=" + cohortConnectionStatus +
                '}';
    }
}
