/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.OMRSInstanceRetrievalEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectorConsumer;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectorManager;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSRuntimeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * EnterpriseOMRSRepositoryConnector supports federating calls to multiple metadata repositories.  As a result,
 * its OMRSMetadataInstanceStore (EnterpriseOMRSMetadataCollection) returns metadata from all repositories in the
 * connected open metadata repository cohort(s).
 * <p>
 *     An instance of the EnterpriseOMRSRepositoryConnector is created by each Open Metadata Access Service (OMAS)
 *     using the OCF ConnectorBroker.  They use its metadata collection to retrieve and send the metadata they need.
 * </p>
 * <p>
 *     Each EnterpriseOMRSRepositoryConnector instance needs to maintain an up to date list of OMRS Connectors to all the
 *     repositories in the connected open metadata repository cohort(s).  It does by registering as an OMRSConnectorConsumer
 *     with the OMRSConnectorManager to be notified when connectors to new open metadata repositories are available.
 * </p>
 */
public class EnterpriseOMRSRepositoryConnector extends OMRSRepositoryConnector implements OMRSConnectorConsumer
{
    private final OMRSConnectorManager                connectorManager;
    private String                              connectorConsumerId;

    private LocalOMRSRepositoryConnector        localConnector            = null;
    private OMRSInstanceRetrievalEventProcessor localEventProcessor       = null;
    private String                              localMetadataCollectionId = null;
    private List<FederatedConnector>            remoteCohortConnectors    = new ArrayList<>();

    private String callingServiceName = null;

    private static final Logger log = LoggerFactory.getLogger(EnterpriseOMRSRepositoryConnector.class);

    /**
     * Constructor used by the EnterpriseOMRSConnectorProvider.
     *
     * @param connectorManager provides notifications as repositories register and unregister with the
     *                         cohorts.
     */
    EnterpriseOMRSRepositoryConnector(OMRSConnectorManager connectorManager)
    {
        super();
        this.connectorManager = connectorManager;
    }


    /**
     * Set up the unique id for this metadata collection.
     *
     * @param metadataCollectionId String unique Id
     */
    @Override
    public void setMetadataCollectionId(String     metadataCollectionId)
    {
        super.metadataCollectionId = metadataCollectionId;

        if (metadataCollectionId != null)
        {
            super.metadataCollection = new EnterpriseOMRSMetadataCollection(this,
                                                                            super.serverName,
                                                                            repositoryHelper,
                                                                            repositoryValidator,
                                                                            metadataCollectionId,
                                                                            localMetadataCollectionId,
                                                                            auditLog);
        }
    }

    /**
     * Set the unique id of the metadata collection that is collocated with the Enterprise Metadata Collection
     *
     * @param localMetadataCollectionId String unique Id
     */
    public void setLocalMetadataCollectionId(String localMetadataCollectionId)
    {
        this.localMetadataCollectionId = localMetadataCollectionId;
    }


    /**
     * Set up the name of the access service using this connector.
     *
     * @param callingServiceName string name
     */
    public void setCallingServiceName(String callingServiceName)
    {
        this.callingServiceName = callingServiceName;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        if (auditLog != null)
        {
            final String actionDescription = "start";

            auditLog.logMessage(actionDescription, OMRSAuditCode.STARTING_ENTERPRISE_CONNECTOR.getMessageDefinition(callingServiceName));
        }

        if (connectorManager != null)
        {
            this.connectorConsumerId = connectorManager.registerConnectorConsumer(this);
        }
        else
        {
            String   methodName = "start";

            throw new OMRSRuntimeException(OMRSErrorCode.INVALID_COHORT_CONFIG.getMessageDefinition(),
                                           this.getClass().getName(),
                                           methodName);
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem disconnecting the connector.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        if (auditLog != null)
        {
            final String actionDescription = "disconnect";

            auditLog.logMessage(actionDescription, OMRSAuditCode.DISCONNECTING_ENTERPRISE_CONNECTOR.getMessageDefinition(callingServiceName));
        }

        if ((connectorManager != null) && (connectorConsumerId != null))
        {
            connectorManager.unregisterConnectorConsumer(connectorConsumerId);
        }

        localConnector = null;
        remoteCohortConnectors = new ArrayList<>();
    }


    /**
     * Returns the metadata collection to the repository where the supplied classification can be updated, ie its home repository.
     *
     * @param instance instance to test
     * @param methodName name of method making the request (used for logging)
     * @return repository connector
     * @throws RepositoryErrorException home metadata collection is null
     */
    synchronized OMRSMetadataCollection getHomeMetadataCollection(Classification instance,
                                                                  String         methodName) throws RepositoryErrorException
    {
        OMRSRepositoryConnector repositoryConnector = this.getHomeConnector(instance, methodName);

        if (repositoryConnector != null)
        {
            return repositoryConnector.getMetadataCollection();
        }

        throw new RepositoryErrorException(OMRSErrorCode.NO_HOME_FOR_CLASSIFICATION.getMessageDefinition(methodName,
                                                                                                   instance.getName(),
                                                                                                   instance.getMetadataCollectionId()),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Returns the metadata collection to the repository where the supplied instance can be updated, ie its home repository.
     *
     * @param instance instance to test
     * @param methodName name of method making the request (used for logging)
     * @return repository connector
     * @throws RepositoryErrorException home metadata collection is null
     */
    synchronized OMRSMetadataCollection getHomeMetadataCollection(InstanceHeader instance,
                                                                  String         methodName) throws RepositoryErrorException
    {
        OMRSRepositoryConnector repositoryConnector = this.getHomeConnector(instance, methodName);

        if (repositoryConnector != null)
        {
            return repositoryConnector.getMetadataCollection();
        }

        throw new RepositoryErrorException(OMRSErrorCode.NO_HOME_FOR_INSTANCE.getMessageDefinition(methodName,
                                                                                                   instance.getGUID(),
                                                                                                   instance.getMetadataCollectionId()),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Returns the connector to the repository where the supplied classification can be updated, ie its home repository.
     *
     * @param classification classification to test
     * @param methodName name of method making the request (used for logging)
     * @return repository connector
     * @throws RepositoryErrorException home metadata collection is null
     */
    private OMRSRepositoryConnector  getHomeConnector(Classification classification,
                                                      String         methodName) throws RepositoryErrorException
    {
        this.validateRepositoryIsActive(methodName);

        repositoryValidator.validateHomeMetadataGUID(repositoryName, classification, methodName);

        String  instanceMetadataCollectionId = classification.getMetadataCollectionId();

        if (localMetadataCollectionId != null)
        {
            if (localMetadataCollectionId.equals(instanceMetadataCollectionId))
            {
                return localConnector;
            }

            if (classification.getReplicatedBy() != null)
            {
                if (localMetadataCollectionId.equals(classification.getReplicatedBy()))
                {
                    return localConnector;
                }
            }
        }

        for (FederatedConnector   remoteCohortConnector : remoteCohortConnectors)
        {
            if (remoteCohortConnector != null)
            {
                String remoteMetadataCollectionId = remoteCohortConnector.getMetadataCollectionId();

                if (remoteMetadataCollectionId != null)
                {
                    if (remoteMetadataCollectionId.equals(instanceMetadataCollectionId))
                    {
                        return remoteCohortConnector.getConnector();
                    }

                    if (remoteMetadataCollectionId.equals(classification.getReplicatedBy()))
                    {
                        return remoteCohortConnector.getConnector();
                    }
                }
            }
        }

        throw new RepositoryErrorException(OMRSErrorCode.NO_HOME_FOR_INSTANCE.getMessageDefinition(methodName,
                                                                                                   classification.getName(),
                                                                                                   instanceMetadataCollectionId),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Returns the connector to the repository where the supplied instance can be updated, ie its home repository.
     *
     * @param instance instance to test
     * @param methodName name of method making the request (used for logging)
     * @return repository connector
     * @throws RepositoryErrorException home metadata collection is null
     */
    private OMRSRepositoryConnector  getHomeConnector(InstanceHeader instance,
                                                      String         methodName) throws RepositoryErrorException
    {
        this.validateRepositoryIsActive(methodName);

        repositoryValidator.validateHomeMetadataGUID(repositoryName, instance, methodName);

        String  instanceMetadataCollectionId = instance.getMetadataCollectionId();

        if (localMetadataCollectionId != null)
        {
            if (localMetadataCollectionId.equals(instanceMetadataCollectionId))
            {
                return localConnector;
            }

            if (instance.getReplicatedBy() != null)
            {
                if (localMetadataCollectionId.equals(instance.getReplicatedBy()))
                {
                    return localConnector;
                }
            }
        }

        for (FederatedConnector   remoteCohortConnector : remoteCohortConnectors)
        {
            if (remoteCohortConnector != null)
            {
                String remoteMetadataCollectionId = remoteCohortConnector.getMetadataCollectionId();

                if (remoteMetadataCollectionId != null)
                {
                    if (remoteMetadataCollectionId.equals(instanceMetadataCollectionId))
                    {
                        return remoteCohortConnector.getConnector();
                    }

                    if (remoteMetadataCollectionId.equals(instance.getReplicatedBy()))
                    {
                        return remoteCohortConnector.getConnector();
                    }
                }
            }
        }

        throw new RepositoryErrorException(OMRSErrorCode.NO_HOME_FOR_INSTANCE.getMessageDefinition(methodName,
                                                                                                   instance.getGUID(),
                                                                                                   instanceMetadataCollectionId),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Returns the list of connectors with the home repository first, then the local connector then the remaining remote connectors.
     *
     * @param instance instance to test
     * @param methodName name of method making the request (used for logging)
     * @return list of repository connectors
     * @throws RepositoryErrorException home metadata collection is null
     */
    synchronized List<OMRSRepositoryConnector> getHomeLocalRemoteConnectors(InstanceHeader instance,
                                                                            String         methodName) throws RepositoryErrorException
    {
        this.validateRepositoryIsActive(methodName);

        repositoryValidator.validateHomeMetadataGUID(repositoryName, instance, methodName);

        String  instanceMetadataCollectionId = instance.getMetadataCollectionId();

        /*
         * Begin by separating the repositories into two lists - one with the home repository (if present)
         * and the non-home repositories.
         */
        List<OMRSRepositoryConnector>  remoteHomeRepository = new ArrayList<>();
        List<OMRSRepositoryConnector>  otherRemoteRepositories = new ArrayList<>();

        for (FederatedConnector   remoteCohortConnector : remoteCohortConnectors)
        {
            if (remoteCohortConnector != null)
            {
                String remoteMetadataCollectionId = remoteCohortConnector.getMetadataCollectionId();

                if (remoteMetadataCollectionId != null)
                {
                    if (remoteMetadataCollectionId.equals(instanceMetadataCollectionId))
                    {
                        remoteHomeRepository.add(remoteCohortConnector.getConnector());
                    }
                    else if (remoteMetadataCollectionId.equals(instance.getReplicatedBy()))
                    {
                        remoteHomeRepository.add(remoteCohortConnector.getConnector());
                    }
                    else
                    {
                        otherRemoteRepositories.add(remoteCohortConnector.getConnector());
                    }
                }
            }
        }

        List<OMRSRepositoryConnector>  resultList = remoteHomeRepository;

        if (localMetadataCollectionId != null)
        {
            resultList.add(localConnector);
        }

        resultList.addAll(otherRemoteRepositories);

        if (! resultList.isEmpty())
        {
            return resultList;
        }
        else
        {
            throw new RepositoryErrorException(OMRSErrorCode.NO_REPOSITORIES.getMessageDefinition(callingServiceName),
                                               this.getClass().getName(),
                                               methodName);
        }
    }

    /**
     * Return the federated connector for a metadata collection id
     *
     * @param metadataCollectionId unique id for the metadata collection
     * @return federated connector or null
     */
    private FederatedConnector getFederatedConnector(String metadataCollectionId)
    {
        for (FederatedConnector   remoteCohortConnector : remoteCohortConnectors)
        {
            if (remoteCohortConnector != null)
            {
                String remoteMetadataCollectionId = remoteCohortConnector.getMetadataCollectionId();

                if (remoteMetadataCollectionId != null)
                {
                    if (remoteMetadataCollectionId.equals(metadataCollectionId))
                    {
                        return remoteCohortConnector;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Returns the list of repository connectors that the EnterpriseOMRSRepositoryConnector is federating queries across.
     *
     * This method is used by this connector's metadata collection object on each request it processes.  This
     * means it always has the most up-to-date list of connectors to work with.
     *
     * @param methodName name of method making the request (used for logging)
     * @return OMRSRepositoryConnector List
     * @throws RepositoryErrorException the enterprise services are not available
     */
    synchronized List<OMRSRepositoryConnector> getCohortConnectors(String methodName) throws RepositoryErrorException
    {
        this.validateRepositoryIsActive(methodName);

        List<OMRSRepositoryConnector> cohortConnectors = new ArrayList<>();

        /*
         * Make sure the local connector is first.
         */
        if (localConnector != null)
        {
            cohortConnectors.add(localConnector);
        }

        /*
         * Now add the remote connectors.
         */
        for (FederatedConnector federatedConnector : remoteCohortConnectors)
        {
            cohortConnectors.add(federatedConnector.getConnector());
        }

        if (! cohortConnectors.isEmpty())
        {
            return cohortConnectors;
        }
        else
        {
            throw new RepositoryErrorException(OMRSErrorCode.NO_REPOSITORIES.getMessageDefinition(callingServiceName),
                                               this.getClass().getName(),
                                               methodName);
        }
    }


    /**
     * Request the refresh of this instance.
     *
     * @param entity retrieved instance
     */
    public void requestRefreshOfEntity(EntityDetail   entity)
    {
        if ((localEventProcessor != null) && (entity != null))
        {
            if (! localMetadataCollectionId.equals(entity.getMetadataCollectionId()))
            {
                localEventProcessor.processRetrievedEntityDetail(repositoryName,
                                                                 localMetadataCollectionId,
                                                                 entity);
            }
        }
    }


    /**
     * Request the refresh of this instance.
     *
     * @param relationship retrieved instance
     */
    public void requestRefreshOfRelationship(Relationship   relationship)
    {
        if ((localEventProcessor != null) && (relationship != null))
        {
            localEventProcessor.processRetrievedRelationship(repositoryName,
                                                             localMetadataCollectionId,
                                                             relationship);
        }
    }


    /**
     * Save the connector to the local repository.  This is passed from the OMRSConnectorManager.
     *
     * @param metadataCollectionId Unique identifier for the metadata collection.
     * @param localConnector OMRSRepositoryConnector object for the local repository.
     */
    @Override
    public synchronized void setLocalConnector(String                       metadataCollectionId,
                                               LocalOMRSRepositoryConnector localConnector)
    {
        this.localMetadataCollectionId = metadataCollectionId;
        this.localConnector = localConnector;

        if (localConnector != null)
        {
            this.localEventProcessor = localConnector.getIncomingInstanceRetrievalEventProcessor();
        }
    }


    /**
     * Pass the connector to one of the remote repositories in the metadata repository cohort.
     *
     * @param metadataCollectionId Unique identifier for the metadata collection.
     * @param remoteConnector OMRSRepositoryConnector object providing access to the remote repository.
     */
    @Override
    public synchronized void addRemoteConnector(String                  metadataCollectionId,
                                                OMRSRepositoryConnector remoteConnector)
    {
        if (remoteConnector != null)
        {
            FederatedConnector federatedConnector = getFederatedConnector(metadataCollectionId);
            if (federatedConnector == null)
            {
                remoteCohortConnectors.add(new FederatedConnector(metadataCollectionId, remoteConnector));

                if (auditLog != null)
                {
                    final String actionDescription = "Processing incoming registration request from remote cohort member";

                    auditLog.logMessage(actionDescription,
                                        OMRSAuditCode.NEW_REMOTE_MEMBER_DEPLOYED.getMessageDefinition(remoteConnector.getServerName(),
                                                                                                      metadataCollectionId,
                                                                                                      callingServiceName));
                }
            }
            else
            {
                federatedConnector.refresh(metadataCollectionId, remoteConnector);
                if (auditLog != null)
                {
                    final String actionDescription = "Processing incoming registration request from remote cohort member";

                    auditLog.logMessage(actionDescription,
                                        OMRSAuditCode.REMOTE_MEMBER_DEPLOY_REFRESH.getMessageDefinition(remoteConnector.getServerName(),
                                                                                                        metadataCollectionId,
                                                                                                        callingServiceName));
                }
            }
        }
    }


    /**
     * Pass the metadata collection id for a repository that has just left the metadata repository cohort.
     *
     * @param metadataCollectionId identifier of the metadata collection that is no longer available.
     */
    @Override
    public synchronized void removeRemoteConnector(String  metadataCollectionId)
    {
        Iterator<FederatedConnector> iterator = remoteCohortConnectors.iterator();

        while(iterator.hasNext())
        {
            FederatedConnector registeredConnector = iterator.next();

            if (registeredConnector.getMetadataCollectionId().equals(metadataCollectionId))
            {
                this.disconnectConnector(registeredConnector);
                iterator.remove();
            }
        }

        if (auditLog != null)
        {
            final String actionDescription = "Processing incoming registration request from remote cohort member";

            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.REMOTE_MEMBER_UNDEPLOYED.getMessageDefinition(metadataCollectionId,
                                                                                            callingServiceName));
        }
    }


    /**
     * The OMRS is about to shut down.
     * Call disconnect() on all registered remote connectors and stop calling them.
     * There is no need to disconnect the local connector - that is handled by the EnterpriseConnectorManager
     */
    @Override
    public synchronized void disconnectAllConnectors()
    {
        try
        {
            super.disconnect();
        }
        catch (Exception error)
        {
            /*
             * Nothing to do
             */
        }

        /*
         * Need to disconnect the remote connectors
         */

        if (remoteCohortConnectors != null)
        {
            for (FederatedConnector remoteConnector : remoteCohortConnectors)
            {
                if (remoteConnector != null)
                {
                    this.disconnectConnector(remoteConnector);
                }
            }
        }
    }


    /**
     * Issue a disconnect call on the supplied connector.
     *
     * @param connector connector to disconnect.
     */
    private void disconnectConnector(OMRSRepositoryConnector  connector)
    {
        if (connector != null)
        {
            try
            {
                connector.disconnect();
            }
            catch (Exception  error)
            {
                log.error("Exception from disconnect of connector to metadata collection:" + connector.getMetadataCollectionId() + "  Error message was: " + error.getMessage());
            }
        }
    }


    /**
     * Issue a disconnect call on the supplied connector.
     *
     * @param federatedConnector connector to disconnect.
     */
    private void disconnectConnector(FederatedConnector  federatedConnector)
    {
        OMRSRepositoryConnector    connector = null;

        if (federatedConnector != null)
        {
            connector = federatedConnector.getConnector();
        }

        if (connector != null)
        {
            disconnectConnector(connector);
        }
    }


    /**
     * FederatedConnector is a private class for storing details of each of the connectors to the repositories
     * in the open metadata repository cohort.
     */
    private static class FederatedConnector
    {
        private String                  metadataCollectionId;
        private OMRSRepositoryConnector connector;


        /**
         * Constructor to set up the details of a federated connector.
         *
         * @param metadataCollectionId unique identifier for the metadata collection accessed through the connector
         * @param connector connector for the repository
         */
        FederatedConnector(String metadataCollectionId, OMRSRepositoryConnector connector)
        {
            this.metadataCollectionId = metadataCollectionId;
            this.connector = connector;
        }


        /**
         * Refresh the details of a federated connector.
         *
         * @param metadataCollectionId unique identifier for the metadata collection accessed through the connector
         * @param connector connector for the repository
         */
        void refresh(String metadataCollectionId, OMRSRepositoryConnector connector)
        {
            this.metadataCollectionId = metadataCollectionId;
            this.connector = connector;
        }


        /**
         * Return the identifier for the metadata collection accessed through the connector.
         *
         * @return String identifier
         */
        public String getMetadataCollectionId()
        {
            return metadataCollectionId;
        }


        /**
         * Return the connector for the repository.
         *
         * @return OMRSRepositoryConnector object
         */
        public OMRSRepositoryConnector getConnector()
        {
            return connector;
        }


        /**
         * Return the metadata collection associated with the connector.
         *
         * @return OMRSMetadataInstanceStore object
         */
        public OMRSMetadataCollection getMetadataCollection()
        {
            if (connector != null)
            {
                try
                {
                    return connector.getMetadataCollection();
                }
                catch (Exception   error)
                {
                    return null;
                }
            }

            return null;
        }
    }
}
