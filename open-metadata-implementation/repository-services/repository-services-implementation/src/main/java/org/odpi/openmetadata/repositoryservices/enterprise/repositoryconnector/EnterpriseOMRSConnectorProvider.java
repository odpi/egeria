/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogReportingComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectorManager;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;

import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentHelper;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentValidator;

import java.util.UUID;


/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The EnterpriseOMRSConnectorProvider is the connector provider for the EnterpriseOMRSRepositoryConnector.
 *
 * It will return new instances of the EnterpriseOMRSRepositoryConnector as long as it is configured with the connector
 * manager.  This should happen at server startup, which means the exception due to a lack of connector
 * manager are unexpected.
 */
public class EnterpriseOMRSConnectorProvider extends OMRSRepositoryConnectorProviderBase
{
    private static final Logger log = LoggerFactory.getLogger(EnterpriseOMRSConnectorProvider.class);

    private final OMRSConnectorManager         connectorManager;
    private final OMRSRepositoryContentManager repositoryContentManager;
    private final String                       localServerName;
    private final String                       localServerType;
    private final OMRSAuditLog                 auditLog;
    private final String                       owningOrganizationName;
    private final String                       enterpriseMetadataCollectionId;
    private final String                       enterpriseMetadataCollectionName;
    private final String                       localMetadataCollectionId;



    /**
     * Set up the connector provider.  This call is used to control whether the EnterpriseOMRSConnectorProvider
     * produces connectors or not.  An EnterpriseOMRSRepositoryConnector needs the connector manager to maintain the
     * list of connectors to the repositories in the cohort.
     *
     * @param connectorManager manager of the list of connectors to remote repositories.
     * @param repositoryContentManager manager of lists of active and known types with associated helper methods
     * @param localServerName name of the local server for this connection.
     * @param localServerType type of the local server.
     * @param owningOrganizationName name of the organization the owns the remote server.
     * @param auditLog audit log for connectors.
     * @param enterpriseMetadataCollectionId unique identifier for the combined metadata collection covered by the
     *                                      connected open metadata repositories.
     * @param enterpriseMetadataCollectionName name of the combined metadata collection covered by the connected open
     *                                        metadata repositories.  Used for messages.
     * @param localMetadataCollectionId metadata collection id of local repository
     */
    public EnterpriseOMRSConnectorProvider(OMRSConnectorManager         connectorManager,
                                           OMRSRepositoryContentManager repositoryContentManager,
                                           String                       localServerName,
                                           String                       localServerType,
                                           String                       owningOrganizationName,
                                           OMRSAuditLog                 auditLog,
                                           String                       enterpriseMetadataCollectionId,
                                           String                       enterpriseMetadataCollectionName,
                                           String                       localMetadataCollectionId)
    {
        super();
        super.setConnectorComponentDescription(OMRSAuditingComponent.ENTERPRISE_REPOSITORY_CONNECTOR);


        Class<?>    connectorClass = EnterpriseOMRSRepositoryConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        this.connectorManager = connectorManager;
        this.repositoryContentManager = repositoryContentManager;
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.auditLog = auditLog;
        this.owningOrganizationName = owningOrganizationName;
        this.enterpriseMetadataCollectionId = enterpriseMetadataCollectionId;
        this.enterpriseMetadataCollectionName = enterpriseMetadataCollectionName;
        this.localMetadataCollectionId = localMetadataCollectionId;
    }


    /**
     * Creates a new instance of an EnterpriseOMRSRepositoryConnector based on the information in the supplied connection.
     *
     * @param connection connection that should have all the properties needed by the Connector Provider
     *                   to create a connector instance.
     * @return Connector instance of the connector.
     * @throws ConnectionCheckedException if there are missing or invalid properties in the connection
     * @throws ConnectorCheckedException if there are issues instantiating or initializing the connector
     */
    public Connector getConnector(ConnectionProperties connection) throws ConnectionCheckedException, ConnectorCheckedException
    {
        String   methodName = "getConnector";

        log.debug(methodName + " called");

        if (this.connectorManager == null)
        {
            /*
             * If the cohort is not connected then throw an exception to indicate that the repositories are offline.
             */
            throw new ConnectorCheckedException(OMRSErrorCode.COHORT_NOT_CONNECTED.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Create and initialize a new connector.
         */
        EnterpriseOMRSRepositoryConnector connector = new EnterpriseOMRSRepositoryConnector(this.connectorManager);

        connector.setAuditLog(auditLog.createNewAuditLog(OMRSAuditingComponent.ENTERPRISE_REPOSITORY_CONNECTOR));
        connector.initialize(this.getNewConnectorGUID(), connection);
        connector.setServerName(localServerName);
        connector.setServerType(localServerType);
        connector.setRepositoryName(enterpriseMetadataCollectionName);
        connector.setOrganizationName(owningOrganizationName);
        connector.setRepositoryHelper(new OMRSRepositoryContentHelper(repositoryContentManager));
        connector.setRepositoryValidator(new OMRSRepositoryContentValidator(repositoryContentManager));

        /*
         * Set the localMetadataCollectionId in the connector so that it is available during creation of the EnterpriseMetadataCollection, during setMetaadataCollectionId().
         */
        connector.setLocalMetadataCollectionId(localMetadataCollectionId);

        connector.setMetadataCollectionId(enterpriseMetadataCollectionId);
        connector.setMetadataCollectionName(enterpriseMetadataCollectionName);
        connector.initializeConnectedAssetProperties(new EnterpriseOMRSConnectorProperties(connector,
                                                                                           this.connectorManager,
                                                                                           enterpriseMetadataCollectionId,
                                                                                           enterpriseMetadataCollectionName));

        log.debug(methodName + " returns: " + connector.getConnectorInstanceId() + ", " + connection.getConnectionName());

        return connector;
    }
}
