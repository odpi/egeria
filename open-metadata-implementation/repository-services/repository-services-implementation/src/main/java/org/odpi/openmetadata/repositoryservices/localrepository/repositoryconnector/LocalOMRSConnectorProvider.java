/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector;

import org.odpi.openmetadata.adminservices.configuration.properties.LocalRepositoryMode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperConnector;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventExchangeRule;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;

import java.util.UUID;


/**
 * LocalOMRSConnectorProvider implements the connector provider for LocalOMRSRepositoryConnector.   Since a server only
 * has one LocalOMRSRepositoryConnector, this connector provider returns the singleton connector for the local repository.
 */
public class LocalOMRSConnectorProvider extends ConnectorProvider
{
    private String                             localMetadataCollectionId       = null;
    private LocalRepositoryMode                localRepositoryMode             = null;
    private Connection                         localRepositoryRemoteConnection = null;
    private OMRSRepositoryEventMapperConnector realEventMapper                 = null;
    private OMRSRepositoryEventManager         outboundRepositoryEventManager  = null;
    private OMRSRepositoryContentManager       repositoryContentManager        = null;
    private OMRSRepositoryEventExchangeRule    saveExchangeRule                = null;
    private       LocalOMRSRepositoryConnector localRepositoryConnector = null;
    private final ConnectorType                connectorType            = null;
    private AuditLog                           auditLog                        = null;



    /**
     * Constructor used by OMRSOperationalServices during server start-up. It
     * provides the configuration information about the local server that is used to set up the
     * local repository connector.
     *
     * @param localMetadataCollectionId metadata collection id for the local repository
     * @param localRepositoryMode style of operation to perform
     * @param localRepositoryRemoteConnection connection object for creating a remote connector to this repository.
     * @param realEventMapper optional event mapper for local repository
     * @param outboundRepositoryEventManager event manager to call for outbound events.
     * @param repositoryContentManager repositoryContentManager for supporting OMRS in managing TypeDefs.
     * @param saveExchangeRule rule to determine what events to save to the local repository.
     * @param auditLog logging destination
     */
    public LocalOMRSConnectorProvider(String                             localMetadataCollectionId,
                                      LocalRepositoryMode                localRepositoryMode,
                                      Connection                         localRepositoryRemoteConnection,
                                      OMRSRepositoryEventMapperConnector realEventMapper,
                                      OMRSRepositoryEventManager         outboundRepositoryEventManager,
                                      OMRSRepositoryContentManager       repositoryContentManager,
                                      OMRSRepositoryEventExchangeRule    saveExchangeRule,
                                      AuditLog                           auditLog)
    {
        this.localMetadataCollectionId = localMetadataCollectionId;
        this.localRepositoryMode = localRepositoryMode;
        this.localRepositoryRemoteConnection = localRepositoryRemoteConnection;
        this.realEventMapper = realEventMapper;
        this.outboundRepositoryEventManager = outboundRepositoryEventManager;
        this.repositoryContentManager = repositoryContentManager;
        this.saveExchangeRule = saveExchangeRule;
        this.auditLog = auditLog;
    }


    /**
     * Constructor used by the OCF ConnectorBroker.  This approach will result in an exception
     * when getConnector is called because there is no localMetadataCollectionId (amongst other things).
     */
    public LocalOMRSConnectorProvider()
    {
        super();
    }


    /**
     * Returns the properties about the type of connector that this Connector Provider supports.
     *
     * @return properties including the name of the connector type, the connector provider class
     * and any specific connection properties that are recognized by this connector.
     */
    @Override
    public ConnectorType getConnectorType()
    {
        return connectorType;
    }


    /**
     * Creates a new instance of a connector based on the information in the supplied connection.
     *
     * @param realLocalConnection connection that should have all the properties needed by the Connector Provider
     *                              to create a connector instance.
     * @return Connector instance of the LocalOMRSRepositoryConnector wrapping the real local connector.
     * @throws ConnectionCheckedException if there are missing or invalid properties in the connection
     * @throws ConnectorCheckedException if there are issues instantiating or initializing the connector
     */
    @Override
    public synchronized Connector getConnector(Connection realLocalConnection) throws ConnectionCheckedException,
                                                                                      ConnectorCheckedException,
                                                                                      UserNotAuthorizedException
    {
        String methodName = "getConnector";

        if (localMetadataCollectionId == null)
        {
            /*
             * Throw checked exception to indicate that the local repository is not available.  This
             * is likely to be a configuration error.
             */
            throw new ConnectorCheckedException(OMRSErrorCode.LOCAL_REPOSITORY_CONFIGURATION_ERROR.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Only create one instance of the connector
         */
        if (localRepositoryConnector == null)
        {
            if (auditLog != null)
            {
                String connectorProviderName = "null";
                String configurationProperties = "null";
                String localRepositoryModeName = "null";

                if (localRepositoryMode != null)
                {
                    localRepositoryModeName = localRepositoryMode.getName();
                }

                if (realLocalConnection.getConnectorType() != null)
                {
                    connectorProviderName = realLocalConnection.getConnectorType().getConnectorProviderClassName();
                }

                if (realLocalConnection.getConfigurationProperties() != null)
                {
                    configurationProperties = realLocalConnection.getConfigurationProperties().toString();
                }

                auditLog.logMessage(methodName, OMRSAuditCode.CREATING_REAL_CONNECTOR.getMessageDefinition(localRepositoryModeName,
                                                                                                           connectorProviderName,
                                                                                                           configurationProperties));
            }

            OMRSRepositoryConnector realLocalConnector;

            /*
             * Any problems creating the connector to the local repository are passed to the caller as exceptions.
             */
            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);
            Connector       connector       = connectorBroker.getConnector(realLocalConnection);

            /*
             * Try casting the returned connector to a repository connector.  This should work unless the connection
             * passed is for a different type of connector.
             */
            try
            {
                realLocalConnector = (OMRSRepositoryConnector) connector;
            }
            catch (Exception error)
            {
                throw new ConnectionCheckedException(OMRSErrorCode.BAD_LOCAL_REPOSITORY_CONNECTION.getMessageDefinition(),
                                                     this.getClass().getName(),
                                                     methodName,
                                                     error);


            }

            if (auditLog != null)
            {
                auditLog.logMessage(methodName, OMRSAuditCode.NEW_REAL_CONNECTOR.getMessageDefinition());
            }

            /*
             * With the connection to the real local repository established it is possible to create the wrapper
             * for the local repository.  This wrapper is seen by most OMRS Components as the local repository
             * connector.  The exceptions are the inbound event processors that work with the real local connector.
             */
            localRepositoryConnector = new LocalOMRSRepositoryConnector(realLocalConnector,
                                                                        localRepositoryMode,
                                                                        realEventMapper,
                                                                        outboundRepositoryEventManager,
                                                                        repositoryContentManager,
                                                                        saveExchangeRule);
            localRepositoryConnector.initialize(this.getNewConnectorGUID(),
                                                new Connection(localRepositoryRemoteConnection));
        }

        return localRepositoryConnector;
    }


    /**
     * Each connector has a guid to make it easier to correlate log messages from the various components that
     * serve it.  It uses a type 4 (pseudo randomly generated) UUID.
     * The UUID is generated using a cryptographically strong pseudo random number generator.
     *
     * @return guid for a new connector instance
     */
    private String  getNewConnectorGUID()
    {
        UUID newUUID = UUID.randomUUID();

        return newUUID.toString();
    }
}
