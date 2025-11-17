/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector;

import org.odpi.openmetadata.adminservices.configuration.properties.LocalRepositoryMode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEventProcessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperConnector;
import org.odpi.openmetadata.repositoryservices.eventmanagement.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.localrepository.OMRSLocalRepository;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * LocalOMRSRepositoryConnector provides access the local metadata repository plus manages outbound
 * repository events.
 * <br><br>
 * It passes each request to both the real OMRS connector for the local metadata repository and an
 * OMRSEventPublisher.  The OMRSEventPublisher will use its configuration to decide if it needs to
 * pass on the request to the rest of the metadata repository cohort.
 */
public class LocalOMRSRepositoryConnector extends OMRSRepositoryConnector implements OMRSLocalRepository
{
    private static final String   repositoryEventMapperName = "LocalRepositoryEventMapper";
    private static final String   defaultRepositoryName     = "LocalRepository";

    /*
     * The repository content manager is the TypeDefManager for the Local OMRS Metadata Collection,
     * and the incoming TypeDef Event Processor for the Archive Manager and EventListener
     */
    private final OMRSTypeDefManager                  typeDefManager;
    private final OMRSTypeDefEventProcessor           incomingTypeDefEventProcessor;
    private final OMRSRepositoryEventManager          outboundRepositoryEventManager;
    private final OMRSRepositoryEventExchangeRule     saveExchangeRule;
    private final OMRSRepositoryConnector             realLocalConnector;
    private final OMRSRepositoryEventMapperConnector  realEventMapper;

    private LocalOMRSInstanceEventProcessor     incomingInstanceEventProcessor   = null;
    private OMRSInstanceRetrievalEventProcessor instanceRetrievalEventProcessor  = null;
    private boolean                             produceEventsForRealConnector    = true;

    private final Map<String, List<String>> remoteCohortMetadataCollectionIds = new HashMap<>();


    /**
     * Constructor used by the LocalOMRSConnectorProvider.  It provides the information necessary to run the
     * local repository.
     *
     * @param realLocalConnector connector to the local repository
     * @param localRepositoryMode what is the mode of the local repository - affects how event mappers are treated
     * @param realEventMapper optional event mapper for local repository
     * @param outboundRepositoryEventManager event manager to call for outbound events.
     * @param repositoryContentManager repositoryContentManager for supporting OMRS in managing TypeDefs.
     * @param saveExchangeRule rule to determine what events to save to the local repository.
     */
    protected LocalOMRSRepositoryConnector(OMRSRepositoryConnector            realLocalConnector,
                                           LocalRepositoryMode                localRepositoryMode,
                                           OMRSRepositoryEventMapperConnector realEventMapper,
                                           OMRSRepositoryEventManager         outboundRepositoryEventManager,
                                           OMRSRepositoryContentManager       repositoryContentManager,
                                           OMRSRepositoryEventExchangeRule    saveExchangeRule)
    {
        this.realLocalConnector = realLocalConnector;
        this.realEventMapper = realEventMapper;
        this.repositoryName = defaultRepositoryName;

        this.outboundRepositoryEventManager = outboundRepositoryEventManager;
        this.saveExchangeRule = saveExchangeRule;

        /*
         * The repository content manager is the TypeDefManager for the Local OMRS Metadata Collection,
         * and the incoming TypeDef Event Processor for the Archive Manager and EventListener
         */
        this.typeDefManager = repositoryContentManager;
        this.incomingTypeDefEventProcessor = repositoryContentManager;

        /*
         * Incoming events are processed directly with real local connector to avoid the outbound event
         * propagation managed by LocalOMRSMetadataCollection.
         */
        if (repositoryContentManager != null)
        {
            repositoryContentManager.setupEventProcessor(this,
                                                         outboundRepositoryEventManager);
        }


        /*
         * The local repository is not allowed to produce events for a repository proxy.
         * The event mapper is optional and only ever activated in a repository proxy.
         */
        if (localRepositoryMode == LocalRepositoryMode.REPOSITORY_PROXY)
        {
            produceEventsForRealConnector = false;

            /*
             * The realEventMapper is a plug-in component that handles repository events for
             * repository that have additional APIs for managing metadata and need their own mechanism for
             * sending OMRS Repository Events.  If there is no realEventMapper then the localOMRSMetadataCollection
             * will send the outbound repository events.
             */
            if (realEventMapper != null)
            {
                realEventMapper.initialize(repositoryEventMapperName,
                                           realLocalConnector);
                realEventMapper.setRepositoryEventProcessor(outboundRepositoryEventManager);
            }
        }

    }


    /**
     * Set up a new security verifier (the cohort manager runs with a default verifier until this
     * method is called).
     * <br><br>
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
            if (this.metadataCollection != null)
            {
                LocalOMRSMetadataCollection localOMRSMetadataCollection = (LocalOMRSMetadataCollection) this.metadataCollection;

                localOMRSMetadataCollection.setSecurityVerifier(securityVerifier);
            }
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        if (auditLog != null)
        {
            auditLog.logMessage(methodName, OMRSAuditCode.STARTING_REAL_CONNECTOR.getMessageDefinition());
        }

        if (realLocalConnector != null)
        {
            realLocalConnector.start();
        }

        if (auditLog != null)
        {
            auditLog.logMessage(methodName, OMRSAuditCode.STARTED_REAL_CONNECTOR.getMessageDefinition());
        }

        if (realEventMapper != null)
        {
            realEventMapper.start();
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        if (realLocalConnector  != null)
        {
            realLocalConnector.disconnect();
        }

        if (realEventMapper != null)
        {
            realEventMapper.disconnect();
        }
    }


    /*
     * ==============================
     * OMRSMetadataCollectionManager
     */

    /**
     * Set up a repository helper object for the repository connector to use.
     *
     * @param repositoryHelper helper object for building TypeDefs and metadata instances.
     */
    public void setRepositoryHelper(OMRSRepositoryContentHelper repositoryHelper)
    {
        super.setRepositoryHelper(repositoryHelper);

        if (realLocalConnector != null)
        {
            realLocalConnector.setRepositoryHelper(repositoryHelper);
        }

        if (realEventMapper != null)
        {
            realEventMapper.setRepositoryHelper(repositoryHelper);
        }
    }


    /**
     * Set up a repository validator for the repository connector to use.
     *
     * @param repositoryValidator validator object to check the validity of TypeDefs and metadata instances.
     */
    public void setRepositoryValidator(OMRSRepositoryContentValidator repositoryValidator)
    {
        super.setRepositoryValidator(repositoryValidator);

        if (realLocalConnector != null)
        {
            realLocalConnector.setRepositoryValidator(repositoryValidator);
        }

        if (realEventMapper != null)
        {
            realEventMapper.setRepositoryValidator(repositoryValidator);
        }
    }


    /**
     * Set up the maximum PageSize
     *
     * @param maxPageSize maximum number of elements that can be retrieved on a request.
     */
    @Override
    public void setMaxPageSize(int    maxPageSize)
    {
        super.setMaxPageSize(maxPageSize);

        if (realLocalConnector != null)
        {
            realLocalConnector.setMaxPageSize(maxPageSize);
        }
    }


    /**
     * Set up the name of the server where the metadata collection resides.
     *
     * @param serverName String name
     */
    @Override
    public void  setServerName(String      serverName)
    {
        super.setServerName(serverName);

        if (realLocalConnector != null)
        {
            realLocalConnector.setServerName(serverName);
        }

        if (realEventMapper != null)
        {
            realEventMapper.setServerName(serverName);
        }
    }


    /**
     * Set up the descriptive string describing the type of the server.  This might be the
     * name of the product, or similar identifier.
     *
     * @param serverType String server type
     */
    @Override
    public void setServerType(String serverType)
    {
        super.setServerType(serverType);

        if (realLocalConnector != null)
        {
            realLocalConnector.setServerType(serverType);
        }

        if (realEventMapper != null)
        {
            realEventMapper.setServerType(serverType);
        }
    }



    /**
     * Set up the name of the organization that runs/owns the server.
     *
     * @param organizationName String organization name
     */
    @Override
    public void setOrganizationName(String organizationName)
    {
        super.setOrganizationName(organizationName);

        if (realLocalConnector != null)
        {
            realLocalConnector.setOrganizationName(organizationName);
        }

        if (realEventMapper != null)
        {
            realEventMapper.setOrganizationName(organizationName);
        }
    }


    /**
     * Set up the list of metadata collection identifiers from the remote members of a connected cohort.
     * This is called from the cohort registry as it is handling the remote registrations.
     *
     * @param cohortName name of cohort
     * @param remoteCohortMetadataCollectionIds list of metadata collection ids
     */
    @Override
    public synchronized void setRemoteCohortMetadataCollectionIds(String       cohortName,
                                                                  List<String> remoteCohortMetadataCollectionIds)
    {
        this.remoteCohortMetadataCollectionIds.put(cohortName, remoteCohortMetadataCollectionIds);
    }


    /**
     * Is the metadata collection id for an active member of the cohort.  This is used to set up the
     * provenance type correctly (between LOCAL_COHORT and DEREGISTERED_REPOSITORY).
     *
     * @param metadataCollectionId id to test
     * @return boolean flag meaning that the metadata collection is recognized
     */
    @Override
    public synchronized boolean isActiveCohortMember(String metadataCollectionId)
    {
        if (metadataCollectionId == null)
        {
            return false;
        }

        if (metadataCollectionId.equals(this.metadataCollectionId))
        {
            return true;
        }

        for (String cohortName : remoteCohortMetadataCollectionIds.keySet())
        {
            List<String> cohortMetadataCollectionIds = remoteCohortMetadataCollectionIds.get(cohortName);

            if ((cohortMetadataCollectionIds != null) && (cohortMetadataCollectionIds.contains(metadataCollectionId)))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Set up the userId that the local server should use when processing events and there is no external user
     * driving the operation.
     *
     * @param localServerUserId string user id
     */
    @Override
    public void setServerUserId(String localServerUserId)
    {
        super.setServerUserId(localServerUserId);

        if (realLocalConnector != null)
        {
            realLocalConnector.setServerUserId(localServerUserId);
        }

        if (realEventMapper != null)
        {
            realEventMapper.setServerUserId(localServerUserId);
        }
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    public void setAuditLog(OMRSAuditLog auditLog)
    {
        super.setAuditLog(auditLog);

        if (realLocalConnector != null)
        {
            realLocalConnector.setAuditLog(auditLog);
        }
    }


    /**
     * Set up the unique id for this metadata collection.
     *
     * @param metadataCollectionId String unique Id
     */
    @Override
    public void setMetadataCollectionId(String     metadataCollectionId)
    {
        final String methodName = "setMetadataCollectionId";

        try
        {
            /*
             * This is typically where the metadata collection for the real repository is created.  This object issues the requests to the
             * storage (or remote metadata system) and this may be the first point of contact for the real connector.
             * Configuration errors are likely to emerge at this point.
             *
             * If the metadata collection id is null, the real metadata connector sets one up.  It is handled at this late stage
             * to provide a different local metadata collection id for each deployed instance of the default/quickstart connectors.
             * It also means that the real repository owns the metadata collection id and is not affected by re-creation of the docker
             * container instance.
             */
            realLocalConnector.setMetadataCollectionId(metadataCollectionId);

            if (realLocalConnector.getMetadataCollectionId() == null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.NULL_METADATA_COLLECTION_ID.getMessageDefinition(repositoryName),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            super.setMetadataCollectionId(realLocalConnector.getMetadataCollectionId());

            if (realEventMapper != null)
            {
                realEventMapper.setMetadataCollectionId(realLocalConnector.getMetadataCollectionId());
            }

            /*
             * Initialize the metadata collection only once the connector is properly set up.
             */
            metadataCollection = new LocalOMRSMetadataCollection(this,
                                                                 super.serverName,
                                                                 super.repositoryHelper,
                                                                 super.repositoryValidator,
                                                                 super.metadataCollectionId,
                                                                 this.getLocalServerName(),
                                                                 this.getLocalServerType(),
                                                                 this.getOrganizationName(),
                                                                 realLocalConnector.getMetadataCollection(), // If null exception caught
                                                                 outboundRepositoryEventManager,
                                                                 produceEventsForRealConnector,
                                                                 typeDefManager);

            LocalOMRSInstanceEventProcessor  localOMRSInstanceEventProcessor
                    = new LocalOMRSInstanceEventProcessor(super.metadataCollectionId,
                                                          super.serverName,
                                                          this,
                                                          super.repositoryHelper,
                                                          super.repositoryValidator,
                                                          saveExchangeRule,
                                                          produceEventsForRealConnector,
                                                          outboundRepositoryEventManager,
                                                          auditLog.createNewAuditLog(OMRSAuditingComponent.INSTANCE_EVENT_PROCESSOR));

            this.incomingInstanceEventProcessor = localOMRSInstanceEventProcessor;
            this.instanceRetrievalEventProcessor = localOMRSInstanceEventProcessor;
        }
        catch (Exception error)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_METADATA_COLLECTION.getMessageDefinition(repositoryName),
                                              this.getClass().getName(),
                                              methodName,
                                              error);
        }
    }


    /**
     * Explicitly set up the metadata collection name.
     *
     * @param metadataCollectionName display name of the metadata collection.
     */
    @Override
    public void setMetadataCollectionName(String metadataCollectionName)
    {
        super.setMetadataCollectionName(metadataCollectionName);

        if (realLocalConnector != null)
        {
            realLocalConnector.setMetadataCollectionName(metadataCollectionName);
        }
    }


    /**
     * Returns the metadata collection object that provides an OMRS abstraction of the metadata within
     * a metadata repository.
     *
     * @return OMRSMetadataInstanceStore metadata information retrieved from the metadata repository.
     * @throws RepositoryErrorException no metadata collection
     */
    @Override
    public OMRSMetadataCollection getMetadataCollection() throws RepositoryErrorException
    {
        final String      methodName = "getMetadataCollection";

        if (metadataCollection == null)
        {
            throw new RepositoryErrorException(OMRSErrorCode.NULL_METADATA_COLLECTION.getMessageDefinition(repositoryName),
                                               this.getClass().getName(),
                                               methodName);
        }

        return metadataCollection;
    }

    /*
     * ====================================
     * OMRSLocalRepository
     */

    /**
     * Returns the unique identifier (guid) of the local repository's metadata collection.
     *
     * @return String guid
     */
    @Override
    public String getMetadataCollectionId()
    {
        return super.metadataCollectionId;
    }


    /**
     * Returns the repository helper for this repository.
     *
     * @return repository helper
     */
    @Override
    public OMRSRepositoryHelper getRepositoryHelper()
    {
        return realLocalConnector.getRepositoryHelper();
    }


    /**
     * Returns the Connection to the local repository that can be used by remote servers to create
     * an OMRS repository connector to call this server in order to access the local repository.
     *
     * @return Connection object
     */
    @Override
    public Connection getLocalRepositoryRemoteConnection()
    {
        return new Connection(super.connectionBean);
    }


    /**
     * Return the event manager that the local repository uses to distribute events from the local repository.
     *
     * @return outbound repository event manager
     */
    @Override
    public OMRSRepositoryEventManager getOutboundRepositoryEventManager()
    {
        return outboundRepositoryEventManager;
    }


    /**
     * Return the TypeDef event processor that should be passed all incoming TypeDef events received
     * from the cohorts that this server is a member of.
     *
     * @return OMRSTypeDefEventProcessor for the local repository.
     */
    @Override
    public OMRSTypeDefEventProcessor getIncomingTypeDefEventProcessor()
    {
        return incomingTypeDefEventProcessor;
    }


    /**
     * Return the instance event processor that should be passed all incoming instance events received
     * from the cohorts that this server is a member of.
     *
     * @return OMRSInstanceEventProcessor for the local repository.
     */
    @Override
    public LocalOMRSInstanceEventProcessor getIncomingInstanceEventProcessor()
    {
        return incomingInstanceEventProcessor;
    }


    /**
     * Return the instance event processor that should be passed all incoming instance events received
     * from the cohorts that this server is a member of.
     *
     * @return OMRSInstanceEventProcessor for the local repository.
     */
    public OMRSInstanceRetrievalEventProcessor getIncomingInstanceRetrievalEventProcessor()
    {
        return instanceRetrievalEventProcessor;
    }


    /**
     * Return the local server name used for outbound events.
     *
     * @return String name
     */
    @Override
    public String getLocalServerName() { return super.serverName; }


    /**
     * Return the local server type used for outbound events.
     *
     * @return String name
     */
    @Override
    public String getLocalServerType() { return super.serverType; }


    /**
     * Return the name of the organization that owns this local repository.
     *
     * @return String name
     */
    @Override
    public String getOrganizationName() { return super.organizationName; }
}
