/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.factory;

import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.spring.SpringRESTClientConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementOrigin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * RESTClientFactory is used to create an ew REST client
 */
public class RESTClientFactory
{
    private static final Logger log = LoggerFactory.getLogger(RESTClientFactory.class);

    private final Connection   clientConnection;

    /**
     * Constructor for unsecured client connector
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     */
    public RESTClientFactory(String serverName,
                             String serverPlatformURLRoot)
    {
        clientConnection = this.getSpringRESTClientConnection(serverName, serverPlatformURLRoot, null, null, null);
    }


    /**
     * Constructor for authenticated client connector
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @param secretsStoreConnectorMap map from authentication type to supplied secrets store
     */
    public RESTClientFactory(String                             serverName,
                             String                             serverPlatformURLRoot,
                             String                             userId,
                             String                             password,
                             Map<String, SecretsStoreConnector> secretsStoreConnectorMap)
    {
        clientConnection = this.getSpringRESTClientConnection(serverName, serverPlatformURLRoot, userId, password, secretsStoreConnectorMap);
    }


    /**
     * Return the connection object for a Spring based REST Client.  This connection object is built up from
     * information from the caller.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId userId of the calling server/client
     * @param password clear text password of the calling server/client
     * @param secretsStoreConnectorMap supplied secrets store
     * @return connection object
     */
    private Connection getSpringRESTClientConnection(String                             serverName,
                                                     String                             serverPlatformURLRoot,
                                                     String                             userId,
                                                     String                             password,
                                                     Map<String, SecretsStoreConnector> secretsStoreConnectorMap)
    {
        Connection  connection;

        if ((secretsStoreConnectorMap == null) || (!secretsStoreConnectorMap.isEmpty()))
        {
            connection = new Connection();
        }
        else
        {
            VirtualConnection  virtualConnection = new VirtualConnection();

            List<EmbeddedConnection> embeddedConnections = new ArrayList<>();
            for (String secretsConnectorName : secretsStoreConnectorMap.keySet())
            {
                EmbeddedConnection embeddedConnection = new EmbeddedConnection();

                embeddedConnection.setDisplayName(secretsConnectorName);

                SecretsStoreConnector secretsStoreConnector = secretsStoreConnectorMap.get(secretsConnectorName);
                AccessibleConnection  secretStoreConnection = new AccessibleConnection(secretsStoreConnector.getConnection());

                embeddedConnection.setEmbeddedConnection(secretStoreConnection.getConnectionBean());
            }

            virtualConnection.setEmbeddedConnections(embeddedConnections);

            connection = virtualConnection;
        }

        Endpoint endpoint = new Endpoint();

        endpoint.setAddress(serverPlatformURLRoot);
        endpoint.setDisplayName(serverName);
        endpoint.setQualifiedName(serverName);

        connection.setEndpoint(endpoint);
        connection.setConnectorType(getConnectorType(SpringRESTClientConnectorProvider.class.getName()));
        connection.setQualifiedName(endpoint.getAddress());

        connection.setUserId(userId);
        connection.setClearPassword(password);

        return connection;
    }


    /**
     * Return the connector type for the requested connector provider.  This is best used for connector providers that
     * can return their own connector type.  Otherwise, it makes one up.
     *
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean
     */
    private ConnectorType getConnectorType(String   connectorProviderClassName)
    {
        ConnectorType  connectorType = null;

        if (connectorProviderClassName != null)
        {
            try
            {
                Class<?>   connectorProviderClass = Class.forName(connectorProviderClassName);
                Object     potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

                ConnectorProvider connectorProvider = (ConnectorProvider)potentialConnectorProvider;

                connectorType = connectorProvider.getConnectorType();

                if (connectorType == null)
                {
                    connectorType = new ConnectorType();

                    ElementOrigin elementOrigin = new ElementOrigin();
                    elementOrigin.setOriginCategory(ElementOriginCategory.CONFIGURATION);
                    connectorType.setOrigin(elementOrigin);

                    connectorType.setType(ConnectorType.getConnectorTypeType());
                    connectorType.setGUID(UUID.randomUUID().toString());
                    connectorType.setQualifiedName(connectorProviderClassName);
                    connectorType.setDisplayName(connectorProviderClass.getSimpleName());
                    connectorType.setDescription("ConnectorType for " + connectorType.getDisplayName());
                    connectorType.setConnectorProviderClassName(connectorProviderClassName);
                }
            }
            catch (Exception classException)
            {
                log.error("Bad connectorProviderClassName: " + classException.getMessage());
            }
        }

        return connectorType;
    }


    /**
     * Retrieve the REST client connector.
     *
     * @return client connector
     * @throws Exception an unexpected exception - internal logic error as the parameters should have all been checked
     * before this call.
     */
    public RESTClientConnector getClientConnector() throws Exception
    {
        ConnectorBroker     connectorBroker = new ConnectorBroker();
        Connector           connector       = connectorBroker.getConnector(clientConnection);

        return (RESTClientConnector)connector;
    }


    /**
     * ProtectedConnection provides a subclass to Connection in order to extract protected values from the
     * connection in order to supply them to the Connector implementation.
     */
    private static class AccessibleConnection extends ConnectionProperties
    {
        AccessibleConnection(ConnectionProperties templateConnection)
        {
            super(templateConnection);
        }

        /**
         * Return a copy of the ConnectionBean.
         *
         * @return Connection bean
         */
        protected Connection getConnectionBean()
        {
            return super.getConnectionBean();
        }
    }
}
