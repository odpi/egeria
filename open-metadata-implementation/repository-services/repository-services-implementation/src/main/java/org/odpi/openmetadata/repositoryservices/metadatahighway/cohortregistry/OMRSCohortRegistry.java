/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.metadatahighway.cohortregistry;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEventProcessor;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.OMRSCohortRegistryStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectionConsumer;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSRuntimeException;
import org.odpi.openmetadata.repositoryservices.localrepository.OMRSLocalRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * OMRSCohortRegistry manages the local server's registration into a cohort and receives registration
 * requests from other servers in the cohort.  This management involves:
 * <ul>
 *     <li>
 *         Sending and receiving registry events that contain registration information about the members
 *         of the cohort.
 *     </li>
 *     <li>
 *         Maintaining details of the local server's and other remote server's registration information
 *         in the cohort registry store to use for server restart.
 *     </li>
 *     <li>
 *         Configuring the federation services (OMRS Connection Manager and Enterprise OMRS Connector) with
 *         information about the other servers in the cohort as they register and unregister from the
 *         cohort.
 *     </li>
 * </ul>
 * Within a server, there is a single instance of the cohort registry for each cohort that the server joins.
 */
public class OMRSCohortRegistry extends OMRSRegistryEventProcessor
{
    /*
     * Local name of the cohort.  This is used for messages rather than being part of the protocol.
     */
    private String     cohortName = null;

    /*
     * These variables describe the local server's properties.
     */
    private String              localMetadataCollectionId       = null;
    private String              localMetadataCollectionName     = null;
    private OMRSLocalRepository localRepository                 = null;
    private Connection          localRepositoryRemoteConnection = null;
    private String              localServerName                 = null;
    private String              localServerType                 = null;
    private String              localOrganizationName           = null;

    /*
     * The registry store is used to save information about the members of the open metadata repository cohort.
     */
    private OMRSCohortRegistryStore registryStore = null;

    /*
     * The event publisher is used to send events to the rest of the open metadata repository cohort.
     */
    private OMRSRegistryEventProcessor   outboundRegistryEventProcessor = null;

    /*
     * The connection consumer supports components such as the EnterpriseOMRSRepositoryConnector that need to maintain a
     * list of remote partners that are part of the open metadata repository cohort.
     */
    private OMRSConnectionConsumer       connectionConsumer = null;

    /*
     * The audit log provides a verifiable record of the membership of the open metadata repository cohort and the
     * metadata exchange activity they are involved in.  The Logger is for standard debug.
     */
    private final AuditLog auditLog;


    /**
     * Default constructor that relies on the initialization of variables in the declaration.
     *
     * @param auditLog audit log for this component.
     */
    public OMRSCohortRegistry(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Validate that any metadata collection id previously used by the local server to register with the
     * open metadata repository cohort matches the local metadata collection id passed in the configuration
     * properties.
     *
     * @param configuredLocalMetadataCollectionId configured value for the local metadata collection id may be null
     *                                  if no local repository.
     */
    private  void   validateLocalMetadataCollectionId(String         configuredLocalMetadataCollectionId)
    {
        String methodName = "validateLocalMetadataCollectionId()";

        if (this.registryStore == null)
        {
            /*
             * Throw exception as the cohort registry store is not available.
             */
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_REGISTRY_STORE.getMessageDefinition(cohortName),
                                              this.getClass().getName(),
                                              methodName);
        }

        MemberRegistration localRegistration = registryStore.retrieveLocalRegistration();

        if (localRegistration != null)
        {
            String storedLocalMetadataCollectionId = localRegistration.getMetadataCollectionId();

            if (storedLocalMetadataCollectionId != null)
            {
                /*
                 * There is a stored local metadata collection id which is going to be used.  There is a consistency check
                 * to ensure this stored local id is the same as the configured local metadata collection id.
                 *
                 * If it is not the same, the administrator has changed the configured value after the server
                 * registered with the cohort.   The message on the audit log explains that the new value will be
                 * ignored until the local repository is un-registered with the old metadata collection id, and then it
                 * can be registered with the new metadata collection id.
                 */

                if (!storedLocalMetadataCollectionId.equals(configuredLocalMetadataCollectionId))
                {
                    if (configuredLocalMetadataCollectionId == null)
                    {
                        /*
                         * The change in the configuration is to remove the local repository.  This means
                         * the local server should simply unregister from the cohort.
                         */
                        this.unRegisterLocalRepositoryWithCohort(localRegistration);
                        registryStore.removeLocalRegistration();

                    }
                    else
                    {
                        /*
                         * The configured value is different from the value used to register with this cohort.
                         * This is a situation that could potentially damage the metadata integrity across the cohort.
                         * Hence, the exception.
                         */
                        throw new OMRSConfigErrorException(OMRSErrorCode.INVALID_LOCAL_METADATA_COLLECTION_ID.getMessageDefinition(cohortName,
                                                                                                                                   localServerName,
                                                                                                                                   storedLocalMetadataCollectionId,
                                                                                                                                   configuredLocalMetadataCollectionId),
                                                           this.getClass().getName(),
                                                           methodName);
                    }
                }
            }
        }
    }


    /**
     * Initialize the cohort registry object.  The parameters passed control its behavior.
     *
     * @param cohortName the name of the cohort that this cohort registry is communicating with.
     * @param localMetadataCollectionId configured value for the local metadata collection id may be null
     *                                  if no local repository.
     * @param localMetadataCollectionName display name for the local metadata collection
     * @param localRepository the optional local repository.
     * @param localServerName the name of the local server. It is a descriptive name for informational purposes.
     * @param localServerType the type of the local server.  It is a descriptive name for informational purposes.
     * @param localOrganizationName the name of the organization that owns the local server/repository.
     *                              It is a descriptive name for informational purposes.
     * @param registryEventProcessor used to send outbound registry events to the cohort.
     * @param cohortRegistryStore the cohort registry store where details of members of the cohort are kept.
     * @param connectionConsumer The connection consumer is a component interested in maintaining details of the
     *                           connections to each of the members of the open metadata repository cohort.  If it is
     *                           null, the cohort registry does not publish connections for members of the open
     *                           metadata repository cohort.
     */
    public void initialize(String                     cohortName,
                           String                     localMetadataCollectionId,
                           String                     localMetadataCollectionName,
                           OMRSLocalRepository        localRepository,
                           String                     localServerName,
                           String                     localServerType,
                           String                     localOrganizationName,
                           OMRSRegistryEventProcessor registryEventProcessor,
                           OMRSCohortRegistryStore    cohortRegistryStore,
                           OMRSConnectionConsumer     connectionConsumer)
    {
        String actionDescription = "Initialize cohort registry";

        if (cohortRegistryStore == null)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_REGISTRY_STORE.getMessageDefinition(cohortName),
                                              this.getClass().getName(),
                                              actionDescription);
        }
        this.registryStore = cohortRegistryStore;

        /*
         * Save information about the local server
         */
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;

        /*
         * Save the cohort name for messages and the registry event processor for sending outbound events.
         */
        this.cohortName = cohortName;
        this.outboundRegistryEventProcessor = registryEventProcessor;

        /*
         * Verify that the configured local metadata collection id matches the one stored in the registry store.
         * This will throw an exception if there are unresolvable differences.
         */
        this.validateLocalMetadataCollectionId(localMetadataCollectionId);
        this.localMetadataCollectionId = localMetadataCollectionId;
        this.localMetadataCollectionName = localMetadataCollectionName;

        /*
         * Save the connection consumer.  This component needs details of the current connections it should use
         * to contact various members of the cluster (including the local server). It needs an initial
         * upload of the member's connections and then ongoing notifications for any changes in the membership.
         */
        this.connectionConsumer = connectionConsumer;

        /*
         * Save the connections to the local repository.  The localRepositoryRemoteConnection is used
         * in the registration request that this repository sends out. A null value tells the
         * other cohort members that this server does not have a local repository.
         */
        this.localRepository = localRepository;
        if (localRepository != null)
        {
            this.localRepositoryRemoteConnection = localRepository.getLocalRepositoryRemoteConnection();
        }
    }


    /**
     * A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the
     * open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration
     * event has been sent.
     *
     * If the server has already registered in the past, it sends a registration request.
     */
    public synchronized void  connectToCohort()
    {
        if (registryStore == null)
        {
            /*
             * Throw exception as the cohort registry store is not available.
             */
            final String methodName = "connectToCohort";

            throw new OMRSRuntimeException(OMRSErrorCode.NULL_REGISTRY_STORE.getMessageDefinition(),
                                           this.getClass().getName(),
                                           methodName);
        }

        /*
         * Extract member registration information from the cohort registry store.  If there is
         * no local registration, it means the local repository is not currently registered with the metadata
         * repository cohort.
         */
        MemberRegistration localRegistration = registryStore.retrieveLocalRegistration();

        if (localRegistration == null)
        {
            localRegistration = new MemberRegistration();
        }

        /*
         * Fill in the local registration with details from this server.  Any value from the local repository
         * can change except the localMetadataCollectionId and this value has already been validated.
         */
        localRegistration.setMetadataCollectionId(localMetadataCollectionId);
        localRegistration.setMetadataCollectionName(localMetadataCollectionName);
        localRegistration.setServerName(localServerName);
        localRegistration.setServerType(localServerType);
        localRegistration.setOrganizationName(localOrganizationName);
        localRegistration.setRepositoryConnection(localRepositoryRemoteConnection);


        /*
         * If the local metadata collection id is null it means there is no local repository.  No registration
         * is required.   If there is a local repository, a registration request is sent when first connecting
         * and a re-registration request is sent on subsequent restarts.
         */
        if (localMetadataCollectionId != null)
        {
            /*
             * Save basic information about the local registration.  This is done on each restart in case information
             * about the local repository has changed.
             */
            registryStore.saveLocalRegistration(localRegistration);

            /*
             * Work out if this server's repository has been registered with this cohort before.
             */
            if (localRegistration.getRegistrationTime() == null)
            {
                /*
                 * This repository has never registered with the open metadata repository cohort, so send registration
                 * request.
                 */
                localRegistration.setRegistrationTime(new Date());

                if (this.registerLocalRepositoryWithCohort(localRegistration))
                {
                    /*
                     * Successfully registered so save the local registration to the registry store so that the
                     * registration time is recorded.
                     */
                    registryStore.saveLocalRegistration(localRegistration);
                }
            }
            else /* already registered */
            {
                /*
                 * No new registration is required but the cohort registry sends a re-registration request to
                 * ensure remote members have the latest information about this server.
                 */
                this.reRegisterLocalRepositoryWithCohort(localRegistration);
            }
        }

        /*
         * Now read the remote registrations from the registry store and publish them to the connection consumer.
         * This configures the enterprise connectors used by the OMASs with the members of the cohort known from
         * the previous connection to the cohort.
         */
        if (connectionConsumer != null)
        {
            /*
             * Extract remote member registrations from the cohort registry store and register each one with the
             * connection consumer.
             */
            List<MemberRegistration> remoteRegistrations = registryStore.retrieveRemoteRegistrations();

            if (remoteRegistrations != null)
            {
                for (MemberRegistration  remoteMember : remoteRegistrations)
                {
                    if ((remoteMember != null) && (remoteMember.getRepositoryConnection() != null))
                    {
                        this.registerRemoteConnectionWithConsumer(remoteMember.getMetadataCollectionId(),
                                                                  remoteMember.getMetadataCollectionName(),
                                                                  remoteMember.getServerName(),
                                                                  remoteMember.getServerType(),
                                                                  remoteMember.getOrganizationName(),
                                                                  remoteMember.getRepositoryConnection());
                    }
                }
            }
        }

        /*
         * Finally, request that the other members of the cohort send this server their details in case something has
         * changed.  These are processed asynchronously and update the connection consumer as required.
         */
        this.requestRegistrationRefreshFromCohort(localRegistration);
    }


    /**
     * Return the local registration for this cohort.
     *
     * @return list of member registrations
     */
    public synchronized MemberRegistration getLocalRegistration()
    {
        if (registryStore != null)
        {
            return new MemberRegistration(registryStore.retrieveLocalRegistration());
        }

        return null;
    }


    /**
     * Return the remote members for this cohort.
     *
     * @return list of member registrations
     */
    public synchronized List<MemberRegistration> getRemoteMembers()
    {
        if (registryStore != null)
        {
            return registryStore.retrieveRemoteRegistrations();
        }

        return null;
    }


    /**
     * Close the connection to the registry store.
     *
     * @param unregister boolean flag indicating whether the disconnection also includes unregistration from the cohort.  If it is set
     *                  to true, the OMRS Cohort will inform the other members of the cohort that it is leaving and remove all information
     *                   about the cohort from the cohort registry store.
     */
    public synchronized void disconnectFromCohort(boolean   unregister)
    {
        final String  actionDescription = "Disconnect from Cohort";

        if (registryStore != null)
        {
            if (unregister)
            {
                MemberRegistration  localRegistration = registryStore.retrieveLocalRegistration();

                auditLog.logMessage(actionDescription, OMRSAuditCode.COHORT_PERMANENTLY_DISCONNECTING.getMessageDefinition(cohortName));

                if (localRegistration != null)
                {
                    this.unRegisterLocalRepositoryWithCohort(localRegistration);
                }

                registryStore.clearAllRegistrations();
                if (localRepository != null)
                {
                    localRepository.setRemoteCohortMetadataCollectionIds(cohortName, null);
                }

                if (connectionConsumer != null)
                {
                    connectionConsumer.removeCohort(cohortName);
                }
            }
            else
            {
                auditLog.logMessage(actionDescription, OMRSAuditCode.COHORT_DISCONNECTING.getMessageDefinition(cohortName));
            }

            registryStore.close();
        }
    }


    /**
     * Send a registration event to the open metadata repository cohort.  This means the
     * server has never registered with the cohort before.
     *
     * @param localRegistration details of the local server that are needed to build the event
     * @return boolean indicating whether the repository registered successfully or not.
     */
    private boolean registerLocalRepositoryWithCohort(MemberRegistration   localRegistration)
    {
        final String    actionDescription = "Registering with cohort";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.REGISTERED_WITH_COHORT.getMessageDefinition(cohortName, localMetadataCollectionId));

        return outboundRegistryEventProcessor.processRegistrationEvent(cohortName,
                                                                       localRegistration.getMetadataCollectionId(),
                                                                       localRegistration.getMetadataCollectionName(),
                                                                       localRegistration.getServerName(),
                                                                       localRegistration.getServerType(),
                                                                       localRegistration.getOrganizationName(),
                                                                       localRegistration.getRegistrationTime(),
                                                                       localRegistration.getRepositoryConnection());
    }


    /**
     * Send a registration event to the open metadata repository cohort.  This means the
     * server has never registered with the cohort before.
     *
     * @param localRegistration details of the local server that are needed to build the event
     */
    private void reRegisterLocalRepositoryWithCohort(MemberRegistration   localRegistration)
    {
        final String    actionDescription = "ReRegistering with cohort";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.RE_REGISTERED_WITH_COHORT.getMessageDefinition(cohortName, localMetadataCollectionId));

        outboundRegistryEventProcessor.processReRegistrationEvent(cohortName,
                                                                  localRegistration.getMetadataCollectionId(),
                                                                  localRegistration.getMetadataCollectionName(),
                                                                  localRegistration.getServerName(),
                                                                  localRegistration.getServerType(),
                                                                  localRegistration.getOrganizationName(),
                                                                  localRegistration.getRegistrationTime(),
                                                                  localRegistration.getRepositoryConnection());
    }


    /**
     * Request that the remote members of the cohort send details of their registration to enable the local
     * server to ensure it has details of every member.  There are two use cases.  It may have missed a
     * registration event from a remote member because it was not online for some time.
     * Alternatively, it may not have a local repository and so can not trigger the reRegistration events
     * with its own registration events.
     *
     * @param localRegistration information needed to send the refresh request
     */
    private void requestRegistrationRefreshFromCohort(MemberRegistration   localRegistration)
    {
        final String    actionDescription = "Re-registering with cohort";

        auditLog.logMessage(actionDescription, OMRSAuditCode.REFRESH_REGISTRATION_REQUEST_WITH_COHORT.getMessageDefinition(cohortName));

        outboundRegistryEventProcessor.processRegistrationRefreshRequest(cohortName,
                                                                         localRegistration.getServerName(),
                                                                         localRegistration.getServerType(),
                                                                         localRegistration.getOrganizationName());
    }


    /**
     * Unregister from the Cohort.
     *
     * @param localRegistration details of the local registration
     */
    private void unRegisterLocalRepositoryWithCohort(MemberRegistration   localRegistration)
    {
        final String    actionDescription = "Unregistering from cohort";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.UNREGISTERING_FROM_COHORT.getMessageDefinition(cohortName, localRegistration.getMetadataCollectionId()));

        outboundRegistryEventProcessor.processUnRegistrationEvent(cohortName,
                                                                  localRegistration.getMetadataCollectionId(),
                                                                  localRegistration.getMetadataCollectionName(),
                                                                  localRegistration.getServerName(),
                                                                  localRegistration.getServerType(),
                                                                  localRegistration.getOrganizationName());
    }


    /**
     * Register a new remote connection with the OMRSConnectionConsumer.  If there is a problem with the
     * remote connection, a bad connection registry event is sent to the remote repository.
     *
     * @param remoteMetadataCollectionId id of the remote repository's metadata collection
     * @param remoteMetadataCollectionName display name of the remote repository's metadata collection
     * @param remoteServerName name of the remote server.
     * @param remoteServerType type of the remote server.
     * @param owningOrganizationName name of the organization the owns the remote server.
     * @param remoteRepositoryConnection connection used to create a connector to call the remote repository.
     */
    private void registerRemoteConnectionWithConsumer(String      remoteMetadataCollectionId,
                                                      String      remoteMetadataCollectionName,
                                                      String      remoteServerName,
                                                      String      remoteServerType,
                                                      String      owningOrganizationName,
                                                      Connection  remoteRepositoryConnection)
    {
        final String    actionDescription = "Receiving registration request";

        if (connectionConsumer != null)
        {
            /*
             * An exception is thrown if the remote connection is bad.
             */
            try
            {
                connectionConsumer.addRemoteConnection(cohortName,
                                                       remoteServerName,
                                                       remoteServerType,
                                                       owningOrganizationName,
                                                       remoteMetadataCollectionId,
                                                       remoteMetadataCollectionName,
                                                       remoteRepositoryConnection);
            }
            catch (OCFCheckedExceptionBase error)
            {
                auditLog.logMessage(actionDescription,
                                    OMRSAuditCode.OUTGOING_BAD_CONNECTION.getMessageDefinition(cohortName,
                                                                                               remoteRepositoryConnection.getQualifiedName(),
                                                                                               remoteServerName,
                                                                                               remoteMetadataCollectionId,
                                                                                               error.getReportedErrorMessage()),
                                    error.getReportedErrorMessage());

                if (outboundRegistryEventProcessor != null)
                {
                    outboundRegistryEventProcessor.processBadConnectionEvent(cohortName,
                                                                             localMetadataCollectionId,
                                                                             localMetadataCollectionName,
                                                                             localServerName,
                                                                             localServerType,
                                                                             localOrganizationName,
                                                                             remoteMetadataCollectionId,
                                                                             remoteRepositoryConnection,
                                                                             error.getReportedErrorMessage());
                }
            }
            catch (Exception  error)
            {
                auditLog.logMessage(actionDescription,
                                    OMRSAuditCode.OUTGOING_BAD_CONNECTION.getMessageDefinition(cohortName,
                                                                                               remoteRepositoryConnection.getQualifiedName(),
                                                                                               remoteServerName,
                                                                                               remoteMetadataCollectionId,
                                                                                               error.getMessage()),
                                    error.getMessage());

                if (outboundRegistryEventProcessor != null)
                {
                    outboundRegistryEventProcessor.processBadConnectionEvent(cohortName,
                                                                             localMetadataCollectionId,
                                                                             localMetadataCollectionName,
                                                                             localServerName,
                                                                             localServerType,
                                                                             localOrganizationName,
                                                                             remoteMetadataCollectionId,
                                                                             remoteRepositoryConnection,
                                                                             error.getMessage());
                }
            }
        }
    }

    /**
     * Unregister a remote connection from the OMRSConnectionConsumer.
     *
     * @param remoteMetadataCollectionId id of the remote repository
     */
    private void unRegisterRemoteConnectionWithConsumer(String remoteMetadataCollectionId)
    {
        if (connectionConsumer != null)
        {
            connectionConsumer.removeRemoteConnection(cohortName, remoteMetadataCollectionId);
        }
    }


    /*
     * =============================
     * OMRSRegistryEventProcessor
     */


    /**
     * Process the information from either a registration or reregistration request.
     *
     * @param originatorMetadataCollectionId unique identifier for the metadata collection that is registering with the cohort.
     * @param originatorMetadataCollectionName display for the metadata collection that is registering with the cohort.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param registrationTimestamp the time that the server/repository issued the registration request.
     * @param remoteConnection the Connection properties for the connector used to call the registering server.
     */
    private void actionInboundRegistration(String      originatorMetadataCollectionId,
                                           String      originatorMetadataCollectionName,
                                           String      originatorServerName,
                                           String      originatorServerType,
                                           String      originatorOrganizationName,
                                           Date        registrationTimestamp,
                                           Connection  remoteConnection)
    {
        /*
         * Store information about the remote repository in the cohort registry store.
         */
        MemberRegistration remoteRegistration = new MemberRegistration();

        remoteRegistration.setMetadataCollectionId(originatorMetadataCollectionId);
        remoteRegistration.setMetadataCollectionName(originatorMetadataCollectionName);
        remoteRegistration.setServerName(originatorServerName);
        remoteRegistration.setServerType(originatorServerType);
        remoteRegistration.setOrganizationName(originatorOrganizationName);
        remoteRegistration.setRegistrationTime(registrationTimestamp);
        remoteRegistration.setRepositoryConnection(remoteConnection);

        registryStore.saveRemoteRegistration(remoteRegistration);

        if (localRepository != null)
        {
            localRepository.setRemoteCohortMetadataCollectionIds(cohortName, this.getRemoteCohortMetadataCollectionIds());
        }

        if (remoteConnection != null)
        {
            /*
             * Pass the new remote connection to the connection consumer.
             */
            this.registerRemoteConnectionWithConsumer(originatorMetadataCollectionId,
                                                      originatorMetadataCollectionName,
                                                      originatorServerName,
                                                      originatorServerType,
                                                      originatorOrganizationName,
                                                      remoteConnection);
        }
    }


    /**
     * Construct the list of metadata collection identifiers for the remote members of this cohort.
     *
     * @return null or list
     */
    private List<String> getRemoteCohortMetadataCollectionIds()
    {
        if (registryStore != null)
        {
            List<String>             remoteMetadataCollectionIds = new ArrayList<>();
            List<MemberRegistration> remoteMembers = registryStore.retrieveRemoteRegistrations();

            if (remoteMembers != null)
            {
                for (MemberRegistration remoteMember : remoteMembers)
                {
                    if (remoteMember != null)
                    {
                        remoteMetadataCollectionIds.add(remoteMember.getMetadataCollectionId());
                    }
                }
            }

            return remoteMetadataCollectionIds;
        }

        return null;
    }

    /**
     * Check that the registry store is available.
     *
     * @param eventName calling method
     * @param originatingServerName server name
     * @return boolean true if registry store is available, false if not
     */
    private boolean verifyRegistryStore(String   eventName,
                                        String   originatingServerName)
    {
        if (registryStore != null)
        {
            return true;
        }
        else
        {
            auditLog.logMessage(eventName,
                                OMRSAuditCode.MISSING_MEMBER_REGISTRATION.getMessageDefinition(eventName, cohortName, originatingServerName));
            return false;
        }
    }


    /**
     * Introduces a new server/repository to the metadata repository cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection that is registering with the cohort.
     * @param originatorMetadataCollectionName display for the metadata collection that is registering with the cohort.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param registrationTimestamp the time that the server/repository issued the registration request.
     * @param remoteConnection the Connection properties for the connector used to call the registering server.
     */
    public synchronized boolean processRegistrationEvent(String      sourceName,
                                                         String      originatorMetadataCollectionId,
                                                         String      originatorMetadataCollectionName,
                                                         String      originatorServerName,
                                                         String      originatorServerType,
                                                         String      originatorOrganizationName,
                                                         Date        registrationTimestamp,
                                                         Connection  remoteConnection)
    {
        final String    actionDescription = "Receiving Registration event";
        final String    eventName = "Registration";

        if (verifyRegistryStore(eventName, originatorServerName))
        {
            actionInboundRegistration(originatorMetadataCollectionId,
                                      originatorMetadataCollectionName,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      registrationTimestamp,
                                      remoteConnection);

            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.NEW_MEMBER_IN_COHORT.getMessageDefinition(cohortName,
                                                                                        originatorServerName,
                                                                                        originatorMetadataCollectionId));

            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * Refreshes the other servers in the cohort with the originator server's registration.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection that is registering with the cohort.
     * @param originatorMetadataCollectionName display name for the metadata collection that is registering with the cohort.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param registrationTimestamp the time that the server/repository first registered with the cohort.
     * @param remoteConnection the Connection properties for the connector used to call the registering server.
     */
    public synchronized boolean processReRegistrationEvent(String         sourceName,
                                                           String         originatorMetadataCollectionId,
                                                           String         originatorMetadataCollectionName,
                                                           String         originatorServerName,
                                                           String         originatorServerType,
                                                           String         originatorOrganizationName,
                                                           Date           registrationTimestamp,
                                                           Connection     remoteConnection)
    {
        final String    actionDescription = "Receiving ReRegistration event";
        final String    eventName = "Re-Registration";

        if (verifyRegistryStore(eventName, originatorServerName))
        {
            actionInboundRegistration(originatorMetadataCollectionId,
                                      originatorMetadataCollectionName,
                                      originatorServerName,
                                      originatorServerType,
                                      originatorOrganizationName,
                                      registrationTimestamp,
                                      remoteConnection);

            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.REFRESHED_MEMBER_IN_COHORT.getMessageDefinition(cohortName,
                                                                                              originatorServerName,
                                                                                              originatorMetadataCollectionId));

            return true;
        }
        else
        {
            return false;
        }
    }



    /**
     * Requests that the other servers in the cohort send re-registration events.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     */
    public synchronized boolean processRegistrationRefreshRequest(String    sourceName,
                                                                  String    originatorServerName,
                                                                  String    originatorServerType,
                                                                  String    originatorOrganizationName)
    {
        final String    actionDescription = "Receiving Registration Refresh event";
        final String    eventName = "Registration Refresh";

        if (verifyRegistryStore(eventName, originatorServerName))
        {
            MemberRegistration localRegistration = registryStore.retrieveLocalRegistration();

            if (localRegistration != null)
            {
                auditLog.logMessage(actionDescription,
                                    OMRSAuditCode.REFRESHING_REGISTRATION_WITH_COHORT.getMessageDefinition(cohortName,
                                                                                                           localMetadataCollectionId,
                                                                                                           originatorServerName));

                return outboundRegistryEventProcessor.processReRegistrationEvent(cohortName,
                                                                                 localRegistration.getMetadataCollectionId(),
                                                                                 localRegistration.getMetadataCollectionName(),
                                                                                 localRegistration.getServerName(),
                                                                                 localRegistration.getServerType(),
                                                                                 localRegistration.getOrganizationName(),
                                                                                 localRegistration.getRegistrationTime(),
                                                                                 localRegistration.getRepositoryConnection());
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }


    /**
     * A server/repository is being removed from the metadata repository cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId metadata collectionId of originator.
     * @param originatorMetadataCollectionName display name of metadata collection of originator.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     */
    public synchronized boolean processUnRegistrationEvent(String    sourceName,
                                                           String    originatorMetadataCollectionId,
                                                           String    originatorMetadataCollectionName,
                                                           String    originatorServerName,
                                                           String    originatorServerType,
                                                           String    originatorOrganizationName)
    {
        final String    actionDescription = "Receiving unregistration event";
        final String    eventName = "UnRegistration";

        if (verifyRegistryStore(eventName, originatorServerName))
        {

            /*
             * Remove the remote member from the registry store.
             */
            registryStore.removeRemoteRegistration(originatorMetadataCollectionId);
            if (localRepository != null)
            {
                localRepository.setRemoteCohortMetadataCollectionIds(cohortName, this.getRemoteCohortMetadataCollectionIds());
            }

            /*
             * Pass the new remote connection to the connection consumer.
             */
            this.unRegisterRemoteConnectionWithConsumer(originatorMetadataCollectionId);

            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.MEMBER_LEFT_COHORT.getMessageDefinition(originatorServerName,
                                                                                      originatorMetadataCollectionId,
                                                                                      cohortName));

            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * There is more than one member of the open metadata repository cohort that is using the same metadata
     * collection id.  This means that their metadata instances can be updated in more than one server and there
     * is a potential for data integrity issues.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId metadata collectionId of originator.
     * @param originatorMetadataCollectionName display name of  metadata collection of originator.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param conflictingMetadataCollectionId unique identifier for the metadata collection that is registering with the cohort.
     * @param errorMessage details of the conflict
     */
    public synchronized void    processConflictingCollectionIdEvent(String  sourceName,
                                                                    String  originatorMetadataCollectionId,
                                                                    String  originatorMetadataCollectionName,
                                                                    String  originatorServerName,
                                                                    String  originatorServerType,
                                                                    String  originatorOrganizationName,
                                                                    String  conflictingMetadataCollectionId,
                                                                    String  errorMessage)
    {
        if (conflictingMetadataCollectionId != null)
        {
            final String    actionDescription = "Receiving Conflicting Metadata Collection Id event";

            if (conflictingMetadataCollectionId.equals(localMetadataCollectionId))
            {
                auditLog.logMessage(actionDescription,
                                    OMRSAuditCode.INCOMING_CONFLICTING_LOCAL_METADATA_COLLECTION_ID.getMessageDefinition(cohortName,
                                                                                                                         originatorServerName,
                                                                                                                         originatorMetadataCollectionId,
                                                                                                                         conflictingMetadataCollectionId));
            }
            else
            {
                auditLog.logMessage(actionDescription,
                                    OMRSAuditCode.INCOMING_CONFLICTING_METADATA_COLLECTION_ID.getMessageDefinition(cohortName,
                                                                                                                   conflictingMetadataCollectionId));
            }
        }
    }


    /**
     * A connection to one of the members of the open metadata repository cohort is not usable by one of the members.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId metadata collectionId of originator.
     * @param originatorMetadataCollectionName display name of metadata collection of originator.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId id for the repository with the bad remote connection.
     * @param remoteRepositoryConnection the Connection properties for the connector used to call the registering server.
     * @param errorMessage details of the error that occurs when the connection is used.
     */
    public synchronized void    processBadConnectionEvent(String     sourceName,
                                                          String     originatorMetadataCollectionId,
                                                          String     originatorMetadataCollectionName,
                                                          String     originatorServerName,
                                                          String     originatorServerType,
                                                          String     originatorOrganizationName,
                                                          String     targetMetadataCollectionId,
                                                          Connection remoteRepositoryConnection,
                                                          String     errorMessage)
    {
        if (targetMetadataCollectionId != null)
        {
            if (targetMetadataCollectionId.equals(localMetadataCollectionId))
            {
                /*
                 * The event is directed to this server.
                 */
                final String    actionDescription = "Receiving Bad Connection event";
                auditLog.logMessage(actionDescription,
                                    OMRSAuditCode.INCOMING_BAD_CONNECTION.getMessageDefinition(cohortName,
                                                                                               originatorServerName,
                                                                                               originatorMetadataCollectionId,
                                                                                               remoteRepositoryConnection.getQualifiedName()),
                                    remoteRepositoryConnection.toString());
            }
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
        return "OMRSCohortRegistry{" +
                "cohortName='" + cohortName + '\'' +
                ", localMetadataCollectionId='" + localMetadataCollectionId + '\'' +
                ", localMetadataCollectionName='" + localMetadataCollectionName + '\'' +
                ", localRepositoryRemoteConnection=" + localRepositoryRemoteConnection +
                ", localServerName='" + localServerName + '\'' +
                ", localServerType='" + localServerType + '\'' +
                ", localOrganizationName='" + localOrganizationName + '\'' +
                ", registryStore=" + registryStore +
                ", outboundRegistryEventProcessor=" + outboundRegistryEventProcessor +
                ", connectionConsumer=" + connectionConsumer +
                '}';
    }
}
