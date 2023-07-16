/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.connectormanager;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentHelper;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * OMRSEnterpriseConnectorManager provides the connectors for all the repositories in the connected metadata
 * repository cohorts to each of the registered connector consumers.  It supports:
 * <ul>
 *     <li>
 *         A single local repository connector.
 *     </li>
 *     <li>
 *         A remote repository connector for each of the other repositories in the open metadata repository cohort.
 *     </li>
 * </ul>
 * <p>
 * Connector instances are then passed to each of the registered connector consumers.
 * </p>
 * <p>
 * The operation of the OMRSEnterpriseConnectorManager can be thought of in terms of its 3 contract interfaces:
 * </p>
 * <ul>
 *     <li>
 *         OMRSConnectionConsumer is the interface for passing connections to the OMRSEnterpriseConnectorManager.
 *         New connections are validated by creating a test connector and the combination of the metadata collection id
 *         and connection are stored.  An instance of the connector is passed to each of the registered
 *         connector consumers.
 *     </li>
 *     <li>
 *         OMRSConnectorManager is the interface that enables OMRSConnectorConsumers to register with the federation
 *         manager.  When new connector consumers are stored their reference is stored and a uniqueId is
 *         returned to the connector consumer.  This id can be used to unregister from the connector manager.
 *     </li>
 *     <li>
 *         OMRSConnectorConsumer is the interface that the federation manager uses to pass connectors
 *         to each registered connector consumer.  The connector for the local repository is typically
 *         passed first (if it is available) followed by the remote connectors.
 *     </li>
 * </ul>
 * <p>
 * With these interfaces, the OMRSEnterpriseConnectorManager acts as a go between the OMRSCohortRegistry and
 * the EnterpriseOMRSRepositoryConnector instances.
 * </p>
 * <p>
 * Note: this class uses synchronized methods to ensure that no registration information is lost when the
 * server is operating multi-threaded.
 * </p>
 */
public class OMRSEnterpriseConnectorManager implements OMRSConnectionConsumer, OMRSConnectorManager
{

    private static final Logger log = LoggerFactory.getLogger(OMRSEnterpriseConnectorManager.class);

    private final boolean                           enterpriseAccessEnabled;
    private final int                               maxPageSize;
    private final OMRSRepositoryContentManager      repositoryContentManager;
    private final List<RegisteredConnector>         registeredRemoteConnectors   = new ArrayList<>();
    private final List<RegisteredConnectorConsumer> registeredConnectorConsumers = new ArrayList<>();
    private final AuditLog                          auditLog;
    private final String                            localServerUserId;
    private final String                            localServerPassword;

    private String                            localMetadataCollectionId    = null;
    private LocalOMRSRepositoryConnector      localRepositoryConnector     = null;

    /**
     * Constructor for the enterprise connector manager.
     *
     * @param enterpriseAccessEnabled boolean indicating whether the connector consumers should be
     *                                 informed of remote connectors.  If enterpriseAccessEnabled = true
     *                                 the connector consumers will be informed of remote connectors; otherwise
     *                                 they will not.
     * @param maxPageSize the maximum number of elements that can be requested on a page.
     * @param repositoryContentManager repository content manager used by the connectors.
     * @param auditLog audit log to act as a factory for connector audit logs.
     * @param localServerUserId userId for the local server
     * @param localServerPassword password for the local server
     */
    public OMRSEnterpriseConnectorManager(boolean                      enterpriseAccessEnabled,
                                          int                          maxPageSize,
                                          OMRSRepositoryContentManager repositoryContentManager,
                                          AuditLog                     auditLog,
                                          String                       localServerUserId,
                                          String                       localServerPassword)
    {
        this.enterpriseAccessEnabled = enterpriseAccessEnabled;
        this.maxPageSize = maxPageSize;
        this.repositoryContentManager = repositoryContentManager;
        this.auditLog = auditLog;
        this.localServerUserId = localServerUserId;
        this.localServerPassword = localServerPassword;
    }


    /**
     * Returns boolean indicating whether the enterprise connector manager should pass on details about remote
     * members of the cohort to the connector consumers or not.  (This capability allows the OMASs to be configured
     * to work only with the local repository and not perform federated queries.)
     *
     * @return boolean - true means that the registered connector consumers will receive information about
     * remote connectors enabling federated queries across the cohort members.  False means all queries
     * will be directed to the local repository only.
     */
    public boolean isEnterpriseAccessEnabled()
    {
        return enterpriseAccessEnabled;
    }


    /**
     * The disconnect processing involves disconnecting the local connector then unregistering all remote repositories with
     * each of the connector consumers. Each connector consumer will pass the disconnect() request to each of their repository
     * connector instances.
     *
     * If there is an error during disconnect of the local connector, it is logged here and re-thrown to generate an audit log entry.
     *
     * @throws ConnectorCheckedException exception from the connector
     */
    public void disconnect() throws ConnectorCheckedException
    {
        /*
         * Disconnect the local connector
         */
        if (localRepositoryConnector != null)
        {
            try
            {
                localRepositoryConnector.disconnect();
            }
            catch (Exception error)
            {
                log.error("Exception from disconnect of connector to metadata collection:" + localMetadataCollectionId + "  Error message was: " + error.getMessage());
                throw error;
            }
        }

        /*
         * Pass the disconnect request to each registered connector consumer. They will disconnect their (federated) remote connectors
         */
        for (RegisteredConnectorConsumer registeredConnectorConsumer : registeredConnectorConsumers)
        {
            registeredConnectorConsumer.getConnectorConsumer().disconnectAllConnectors();
        }
    }


    /**
     * Pass details of the connection for the local repository to the connection consumer.
     *
     * @param localMetadataCollectionId Unique identifier for the metadata collection
     * @param localRepositoryConnector connector to the local repository
     */
    public void setLocalConnector(String                       localMetadataCollectionId,
                                  LocalOMRSRepositoryConnector localRepositoryConnector)
    {

        /*
         * Connector is ok so save along with the metadata collection id.
         */
        this.localRepositoryConnector = localRepositoryConnector;
        this.localMetadataCollectionId = localMetadataCollectionId;

        /*
         * Pass the local connector to each registered connector consumer.
         */
        for (RegisteredConnectorConsumer registeredConnectorConsumer : registeredConnectorConsumers)
        {
            registeredConnectorConsumer.getConnectorConsumer().setLocalConnector(localMetadataCollectionId,
                                                                                 localRepositoryConnector);
        }
    }


    /**
     * Pass details of the connection for one of the remote repositories registered in a connected
     * open metadata repository cohort.
     *
     * @param cohortName name of the cohort adding the remote connection.
     * @param remoteServerName name of the remote server for this connection.
     * @param remoteServerType type of the remote server.
     * @param owningOrganizationName name of the organization the owns the remote server.
     * @param metadataCollectionId Unique identifier for the metadata collection
     * @param metadataCollectionName Display name for the metadata collection
     * @param remoteConnection Connection object providing properties necessary to create an
     *                         OMRSRepositoryConnector for the remote repository.
     * @throws ConnectionCheckedException there are invalid properties in the Connection
     * @throws ConnectorCheckedException there is a problem initializing the Connector
     */
    @Override
     public synchronized void addRemoteConnection(String         cohortName,
                                                  String         remoteServerName,
                                                  String         remoteServerType,
                                                  String         owningOrganizationName,
                                                  String         metadataCollectionId,
                                                  String         metadataCollectionName,
                                                  Connection remoteConnection) throws ConnectionCheckedException,
                                                                                      ConnectorCheckedException
    {
        final String   actionDescription = "Processing incoming registration request from remote cohort member";

        RegisteredConnector registeredConnector = this.getRegisteredConnector(metadataCollectionId);

        if (registeredConnector == null)
        {
            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.NEW_REMOTE_MEMBER.getMessageDefinition(remoteServerName,
                                                                                     cohortName,
                                                                                     metadataCollectionId,
                                                                                     metadataCollectionName,
                                                                                     remoteServerType,
                                                                                     owningOrganizationName),
                                remoteConnection.toString());
        }
        else if (registeredConnector.checkSameConnection(cohortName,
                                                         remoteServerName,
                                                         remoteServerType,
                                                         owningOrganizationName,
                                                         metadataCollectionId,
                                                         metadataCollectionName,
                                                         remoteConnection))
        {
            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.REMOTE_MEMBER_REFRESHED.getMessageDefinition(remoteServerName,
                                                                                           cohortName,
                                                                                           metadataCollectionId,
                                                                                           metadataCollectionName,
                                                                                           remoteServerType,
                                                                                           owningOrganizationName),
                                remoteConnection.toString());
            return;
        }
        else
        {
            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.REMOTE_MEMBER_UPDATED.getMessageDefinition(remoteServerName,
                                                                                         cohortName,
                                                                                         metadataCollectionId,
                                                                                         metadataCollectionName,
                                                                                         remoteServerType,
                                                                                         owningOrganizationName),
                                remoteConnection.toString());
        }


        /*
         * First test that this connection represents an OMRSRepositoryConnector.  If it does not then an exception
         * is thrown by getOMRSRepositoryConnector() to tell the caller there is a problem.
         */
        OMRSRepositoryConnector remoteConnector = this.getOMRSRepositoryConnector(remoteConnection,
                                                                                  remoteServerName,
                                                                                  remoteServerType,
                                                                                  owningOrganizationName,
                                                                                  metadataCollectionId,
                                                                                  metadataCollectionName);

        OMRSMetadataCollection metadataCollection;

        /*
         * Need to validate that this repository connector has a metadata collection.
         */
        try
        {
            metadataCollection = remoteConnector.getMetadataCollection();
        }
        catch (Exception  error)
        {
            metadataCollection = null;

            auditLog.logException(actionDescription,
                                  OMRSAuditCode.NEW_REMOTE_MEMBER_FAILURE.getMessageDefinition(remoteServerName,
                                                                                               cohortName,
                                                                                               error.getClass().getName(),
                                                                                               error.getMessage()),
                                  remoteConnection.toString(),
                                  error);
        }

        /*
         * Don't need the connector anymore - only created it to check that the connection was valid.
         */
        remoteConnector.disconnect();

        /*
         * Now test the metadata collection.
         */
        if (metadataCollection == null)
        {
            final String   methodName = "addRemoteConnection";

            throw new ConnectorCheckedException(OMRSErrorCode.NULL_COHORT_METADATA_COLLECTION.getMessageDefinition(cohortName, metadataCollectionId),
                                                this.getClass().getName(),
                                                methodName);
        }


        /*
         * Connector is ok so save the connection and metadata collection id.
         */
        if (registeredConnector == null)
        {
            registeredRemoteConnectors.add(new RegisteredConnector(cohortName,
                                                                   remoteServerName,
                                                                   remoteServerType,
                                                                   owningOrganizationName,
                                                                   metadataCollectionId,
                                                                   metadataCollectionName,
                                                                   remoteConnection));
        }
        else
        {
            /*
             * The connection has been checked - update the registered connector with the latest information.
             */
            registeredConnector.refresh(cohortName,
                                        remoteServerName,
                                        remoteServerType,
                                        owningOrganizationName,
                                        metadataCollectionId,
                                        metadataCollectionName,
                                        remoteConnection);
        }

        /*
         * Pass the remote connector to each registered connector consumer if enterprise access is enabled.
         */
        if (enterpriseAccessEnabled)
        {
            for (RegisteredConnectorConsumer registeredConnectorConsumer : registeredConnectorConsumers)
            {
                registeredConnectorConsumer.getConnectorConsumer().addRemoteConnector(metadataCollectionId,
                                                                                      this.getOMRSRepositoryConnector(remoteConnection,
                                                                                                                      remoteServerName,
                                                                                                                      remoteServerType,
                                                                                                                      owningOrganizationName,
                                                                                                                      metadataCollectionId,
                                                                                                                      metadataCollectionName));
            }

            this.printFederationList(actionDescription);
        }
    }


    /**
     * Retrieve the remote connector information for the supplied metadata collection id.
     *
     * @param metadataCollectionId unique identifier of the remote metadata collection
     * @return remote connector information (or null if the metadata collection id is new)
     */
    private RegisteredConnector getRegisteredConnector(String metadataCollectionId)
    {
        for (RegisteredConnector registeredConnector : registeredRemoteConnectors)
        {
            if (registeredConnector != null)
            {
                String remoteMetadataCollectionId = registeredConnector.getMetadataCollectionId();

                if (remoteMetadataCollectionId != null)
                {
                    if (remoteMetadataCollectionId.equals(metadataCollectionId))
                    {
                        return registeredConnector;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Log the current members of the "list".
     *
     * @param actionDescription what processing is this a part of
     */
    private void printFederationList(String actionDescription)
    {
        List<String>  serverList = new ArrayList<>();

        if (localRepositoryConnector != null)
        {
            serverList.add(localRepositoryConnector.getServerName());
        }

        for (RegisteredConnector registeredConnector : registeredRemoteConnectors)
        {
            if (registeredConnector != null)
            {
                serverList.add(registeredConnector.getServerName());
            }
        }

        auditLog.logMessage(actionDescription, OMRSAuditCode.FEDERATION_LIST.getMessageDefinition(serverList.toString()));
    }



    /**
     * Pass details that identify the connection for the repository that has left one of the open metadata repository cohorts.
     * Since any repository may be a member of multiple cohorts, we only remove it from the list if it is
     * the last connector for this repository to be removed.
     *
     * @param cohortName name of the cohort removing the remote connection.
     * @param metadataCollectionId Unique identifier for the metadata collection.
     */
    @Override
    public synchronized void removeRemoteConnection(String         cohortName,
                                                    String         metadataCollectionId)
    {
        final String   actionDescription = "Processing incoming unregistration request from remote cohort member";

        auditLog.logMessage(actionDescription, OMRSAuditCode.REMOVE_REMOTE_MEMBER.getMessageDefinition(cohortName, metadataCollectionId));

        /*
         * Remove the connector from the registered list and work out if the repository is still registered
         * after it has been removed from the specified cohort.
         */
        Iterator<RegisteredConnector>  iterator = registeredRemoteConnectors.iterator();
        int                            repositoryRegistrationCount = 0;

        while (iterator.hasNext())
        {
            RegisteredConnector registeredRemoteConnector = iterator.next();

            if (registeredRemoteConnector.getMetadataCollectionId().equals(metadataCollectionId))
            {
                /*
                 * Found a match for this repository. If the cohort matches too, remove it.  If the
                 * cohort does not match then increment the count of registrations that still exist.
                 */
                if (registeredRemoteConnector.getSource().equals(cohortName))
                {
                    iterator.remove();
                }
                else
                {
                    repositoryRegistrationCount ++;
                }
            }
        }

        /*
         * Remove the connector from the registered connector consumers if federation is enabled
         * and the repository is no longer registered through any cohort.
         */
        if ((enterpriseAccessEnabled) && (repositoryRegistrationCount == 0))
        {
            for (RegisteredConnectorConsumer registeredConnectorConsumer : registeredConnectorConsumers)
            {
                registeredConnectorConsumer.getConnectorConsumer().removeRemoteConnector(metadataCollectionId);
            }

            this.printFederationList(actionDescription);
        }
    }


    /**
     * Remove all the remote connections for the requested open metadata repository cohort.
     * Care must be taken to only remove the remote connectors from the registered connector consumers if the
     * remote connection is only registered with this cohort.
     *
     * @param cohortName name of the cohort
     */
    @Override
    public synchronized void removeCohort(String   cohortName)
    {
        /*
         * Step through the list of registered remote connections, building a list of metadata collection ids for
         * the cohort
         */
        ArrayList<String>    metadataCollectionIds = new ArrayList<>();

        for (RegisteredConnector  registeredRemoteConnector : registeredRemoteConnectors)
        {
            if (registeredRemoteConnector.getSource().equals(cohortName))
            {
                metadataCollectionIds.add(registeredRemoteConnector.getMetadataCollectionId());
            }
        }

        /*
         * Use the list of metadata collection ids to call removeRemoteConnection().  This will manage the
         * removal of the remote connectors from the connector consumers if it is uniquely registered in this
         * cohort.
         */
        for (String  metadataCollectionId : metadataCollectionIds)
        {
            this.removeRemoteConnection(cohortName, metadataCollectionId);
        }
    }


    /**
     * Register the supplied connector consumer with the connector manager.  During the registration
     * request, the connector manager will pass the connector to the local repository and
     * the connectors to all currently registered remote repositories.  Once successfully registered
     * the connector manager will call the connector consumer each time the repositories in the open
     * metadata repository cohort changes.
     *
     * @param connectorConsumer OMRSConnectorConsumer interested in details of the connectors to
     *                           all repositories registered in the open metadata repository cohort.
     * @return String identifier for the connectorConsumer used for the call to unregister.
     */
    public synchronized String registerConnectorConsumer(OMRSConnectorConsumer connectorConsumer)
    {
        /*
         * Store the new connector consumer.
         */
        RegisteredConnectorConsumer   registeredConnectorConsumer = new RegisteredConnectorConsumer(connectorConsumer);
        String                        connectorConsumerId = registeredConnectorConsumer.getConnectorConsumerId();

        registeredConnectorConsumers.add(registeredConnectorConsumer);


        /*
         * Pass the registered local connector to the new connector consumer (if available).
         */
        if (localRepositoryConnector != null)
        {
            connectorConsumer.setLocalConnector(this.localMetadataCollectionId,
                                                this.localRepositoryConnector);
        }

        /*
         * Pass each of the registered remote connectors (if any) to the new connector consumer
         * if federation is enabled.
         */
        if (enterpriseAccessEnabled)
        {
            for (RegisteredConnector registeredConnector : registeredRemoteConnectors)
            {
                try
                {
                    connectorConsumer.addRemoteConnector(registeredConnector.getMetadataCollectionId(),
                                                         getOMRSRepositoryConnector(registeredConnector.getConnection(),
                                                                                    registeredConnector.getServerName(),
                                                                                    registeredConnector.getServerType(),
                                                                                    registeredConnector.getOwningOrganizationName(),
                                                                                    registeredConnector.getMetadataCollectionId(),
                                                                                    registeredConnector.getMetadataCollectionName()));
                }
                catch (ConnectorCheckedException | ConnectionCheckedException error)
                {
                    final String actionDescription = "Registering an enterprise connector with the enterprise connector manager";
                    final String methodName = "registerConnectorConsumer";

                    auditLog.logException(actionDescription,
                                          OMRSAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                  methodName,
                                                                                                  error.getReportedErrorMessage()),
                                          error);
                }
            }
        }

        return connectorConsumerId;
    }


    /**
     * Unregister a connector consumer from the connector manager, so it is no longer informed of
     * changes to the open metadata repository cohort.
     *
     * @param connectorConsumerId String identifier of the connector consumer returned on the
     *                             registerConnectorConsumer.
     */
    public synchronized void unregisterConnectorConsumer(String   connectorConsumerId)
    {
        /*
         * Remove the connector consumer from the registered list.
         */
        Iterator<RegisteredConnectorConsumer> iterator = registeredConnectorConsumers.iterator();

        while(iterator.hasNext())
        {
            RegisteredConnectorConsumer registeredConnectorConsumer = iterator.next();

            if (registeredConnectorConsumer.getConnectorConsumerId().equals(connectorConsumerId))
            {
                iterator.remove();
                break;
            }
        }
    }


    /**
     * Private method to convert a Connection into an OMRS repository connector using the OCF ConnectorBroker.
     * The OCF ConnectorBroker is needed because the implementation of the OMRS connector is unknown and
     * may have come from a third party.   Thus, the official OCF protocol is followed to create the connector.
     * Any failure to create the connector is returned as an exception.
     *
     * @param connection Connection properties
     * @param serverName name of the server for this connection.
     * @param serverType type of the remote server.
     * @param owningOrganizationName name of the organization the owns the remote server.
     * @param metadataCollectionId metadata collection id for this repository
     * @param metadataCollectionName metadata collection name for this repository
     * @return OMRSRepositoryConnector for the connection
     */
    private OMRSRepositoryConnector getOMRSRepositoryConnector(Connection connection,
                                                               String     serverName,
                                                               String     serverType,
                                                               String     owningOrganizationName,
                                                               String     metadataCollectionId,
                                                               String     metadataCollectionName) throws ConnectionCheckedException,
                                                                                                         ConnectorCheckedException
    {
        String     methodName = "getOMRSRepositoryConnector";

        try
        {
            String repositoryName = null;

            /*
             * Add in the local server's userId and password if the remote server has not provided values.
             */
            if (connection != null)
            {
                if (connection.getUserId() == null)
                {
                    connection.setUserId(localServerUserId);
                }

                if (connection.getClearPassword() == null)
                {
                    connection.setClearPassword(localServerPassword);
                }

                repositoryName = connection.getDisplayName();
            }

            /*
             * Create the connector
             */
            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);
            Connector       connector       = connectorBroker.getConnector(connection);

            OMRSRepositoryConnector repositoryConnector = (OMRSRepositoryConnector) connector;

            if (repositoryName == null)
            {
                repositoryName = serverName;
            }

            repositoryConnector.setRepositoryName(repositoryName);
            repositoryConnector.setServerName(serverName);
            repositoryConnector.setServerType(serverType);
            repositoryConnector.setServerUserId(localServerUserId);
            repositoryConnector.setOrganizationName(owningOrganizationName);
            repositoryConnector.setMaxPageSize(maxPageSize);
            repositoryConnector.setRepositoryValidator(new OMRSRepositoryContentValidator(repositoryContentManager));
            repositoryConnector.setRepositoryHelper((new OMRSRepositoryContentHelper(repositoryContentManager)));
            repositoryConnector.setMetadataCollectionId(metadataCollectionId);
            repositoryConnector.setMetadataCollectionName(metadataCollectionName);
            repositoryConnector.start();

            return repositoryConnector;
        }
        catch (ConnectionCheckedException | ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception  error)
        {
            /*
             * If there is a problem initializing the connector then the ConnectorBroker will have created a
             * detailed exception already.  The only error case that this method has introduced is the cast
             * of the Connector to OMRSRepositoryConnector.  This could occur if the connector configured is a valid
             * OCF Connector but not an OMRSRepositoryConnector.
             */
            String  connectionName = "<null>";

            if (connection != null)
            {
                connectionName = connection.toString();
            }

            throw new ConnectorCheckedException(OMRSErrorCode.INVALID_OMRS_CONNECTION.getMessageDefinition(connectionName),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * RegisteredConnector holds the information about connecting to a repository in the open metadata repository
     * cohort.
     */
    private static class RegisteredConnector
    {
        private String     source;
        private String     serverName;
        private String     serverType;
        private String     owningOrganizationName;
        private String     metadataCollectionId;
        private String     metadataCollectionName;
        private Connection connection;


        /**
         * Constructor to set up registered connector.
         *
         * @param source name of the source of the connector.
         * @param serverName name of the server for this connection.
         * @param serverType type of the remote server.
         * @param owningOrganizationName name of the organization the owns the remote server.
         * @param metadataCollectionId unique identifier for the metadata collection that this connector accesses.
         * @param metadataCollectionName display name for the metadata collection that this connector accesses.
         * @param connection connection used to generate the connector
         */
        RegisteredConnector(String     source,
                            String     serverName,
                            String     serverType,
                            String     owningOrganizationName,
                            String     metadataCollectionId,
                            String     metadataCollectionName,
                            Connection connection)
        {
            this.source = source;
            this.serverName = serverName;
            this.serverType = serverType;
            this.owningOrganizationName = owningOrganizationName;
            this.metadataCollectionId = metadataCollectionId;
            this.metadataCollectionName = metadataCollectionName;
            this.connection = connection;
        }


        /**
         * Refresh values from remote member.
         *
         * @param source name of the source of the connector.
         * @param serverName name of the server for this connection.
         * @param serverType type of the remote server.
         * @param owningOrganizationName name of the organization the owns the remote server.
         * @param metadataCollectionId unique identifier for the metadata collection that this connector accesses.
         * @param metadataCollectionName display name for the metadata collection that this connector accesses.
         * @param connection connection used to generate the connector
         */
        void refresh(String     source,
                     String     serverName,
                     String     serverType,
                     String     owningOrganizationName,
                     String     metadataCollectionId,
                     String     metadataCollectionName,
                     Connection connection)
        {
            this.source = source;
            this.serverName = serverName;
            this.serverType = serverType;
            this.owningOrganizationName = owningOrganizationName;
            this.metadataCollectionId = metadataCollectionId;
            this.metadataCollectionName = metadataCollectionName;
            this.connection = connection;
        }


        /**
         * Return the source name for this connector. (Typically the cohort)
         *
         * @return String name
         */
        public String getSource()
        {
            return source;
        }


        /**
         * Return the name of the server that this connection is used to access.
         *
         * @return String name
         */
        public String getServerName()
        {
            return serverName;
        }


        /**
         * Return the type of server that this connection is used to access.
         *
         * @return String type name
         */
        public String getServerType()
        {
            return serverType;
        }


        /**
         * Return the name of the organization that owns the server that this connection is used to access.
         *
         * @return String name
         */
        public String getOwningOrganizationName()
        {
            return owningOrganizationName;
        }


        /**
         * Return the unique identifier for the metadata collection that this connector accesses.
         *
         * @return String identifier
         */
        public String getMetadataCollectionId()
        {
            return metadataCollectionId;
        }


        /**
         * Return the display name for the metadata collection that this connector accesses.
         *
         * @return String name
         */
        public String getMetadataCollectionName()
        {
            return metadataCollectionName;
        }


        /**
         * Return the connection used to generate the connector to the metadata repository.
         *
         * @return Connection properties
         */
        public Connection getConnection()
        {
            return connection;
        }


        /**
         * Check that the incoming connection is the same as the saved connection
         *
         * @param source name of the source of the connector.
         * @param serverName name of the server for this connection.
         * @param serverType type of the remote server.
         * @param owningOrganizationName name of the organization the owns the remote server.
         * @param metadataCollectionId unique identifier for the metadata collection that this connector accesses.
         * @param metadataCollectionName display name for the metadata collection that this connector accesses.
         * @param newConnection connection value to check
         * @return boolean true if the connection has not changed
         */
        public boolean checkSameConnection(String     source,
                                           String     serverName,
                                           String     serverType,
                                           String     owningOrganizationName,
                                           String     metadataCollectionId,
                                           String     metadataCollectionName,
                                           Connection newConnection)
        {
            if (connection.equals(newConnection))
            {
                refresh(source, serverName, serverType, owningOrganizationName, metadataCollectionId, metadataCollectionName, newConnection);

                return true;
            }

            return false;
        }
    }


    /**
     * RegisteredConnectorConsumer relates a connector consumer to an identifier.  It is used by
     * OMRSEnterpriseConnectorManager to manage the list of registered connector consumers.
     */
    private static class RegisteredConnectorConsumer
    {
        private final String                connectorConsumerId;
        private final OMRSConnectorConsumer connectorConsumer;


        /**
         * Constructor when the identifier of the connector consumer is known.
         *
         * @param connectorConsumerId unique identifier of the connection consumer
         * @param connectorConsumer connector consumer itself
         */
        RegisteredConnectorConsumer(String connectorConsumerId, OMRSConnectorConsumer connectorConsumer)
        {
            this.connectorConsumerId = connectorConsumerId;
            this.connectorConsumer = connectorConsumer;
        }


        /**
         * Constructor when the identifier for the connector consumer needs to be allocated.
         *
         * @param connectorConsumer connector consumer itself
         */
        RegisteredConnectorConsumer(OMRSConnectorConsumer connectorConsumer)
        {
            this.connectorConsumer = connectorConsumer;
            this.connectorConsumerId = UUID.randomUUID().toString();
        }


        /**
         * Return the unique identifier of the connector consumer.
         *
         * @return String identifier
         */
        String getConnectorConsumerId()
        {
            return connectorConsumerId;
        }


        /**
         * Return the registered connector consumer.
         *
         * @return connector consumer object ref
         */
        OMRSConnectorConsumer getConnectorConsumer()
        {
            return connectorConsumer;
        }
    }
}
