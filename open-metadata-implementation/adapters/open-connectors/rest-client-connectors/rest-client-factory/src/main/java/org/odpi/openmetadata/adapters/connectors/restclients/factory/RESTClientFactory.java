/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.factory;

import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.spring.SpringRESTClientConnectorProvider;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RESTClientFactory is used to create an ew REST client
 */
public class RESTClientFactory
{
    private final Connection   clientConnection;
    private final AuditLog     auditLog;

    /**
     * Constructor for unsecured client connector
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param localServerSecretsStorePurpose purpose for the secrets collection
     * @param auditLog audit log
     * @throws NoSuchMethodException Unable to work with the connector provider class
     * @throws ClassNotFoundException  Connector provider class not in path
     * @throws InvocationTargetException  Unable to work with the connector provider class
     * @throws InstantiationException  Unable to work with the connector provider class
     * @throws IllegalAccessException  Unable to work with the connector provider class
     */
    public RESTClientFactory(String   serverName,
                             String   serverPlatformURLRoot,
                             String   localServerSecretsStoreProvider,
                             String   localServerSecretsStoreLocation,
                             String   localServerSecretsStoreCollection,
                             String   localServerSecretsStorePurpose,
                             AuditLog auditLog) throws ClassNotFoundException,
                                                                           InvocationTargetException,
                                                                           NoSuchMethodException,
                                                                           InstantiationException,
                                                                           IllegalAccessException
    {
        EmbeddedConnection secretsConnection = Connection.getSecretStoreConnection("Secrets for server " + serverName + " @ " + serverPlatformURLRoot,
                                                                                   localServerSecretsStoreProvider,
                                                                                   localServerSecretsStoreLocation,
                                                                                   localServerSecretsStoreCollection,
                                                                                   localServerSecretsStorePurpose);

        this.clientConnection = this.getSpringRESTClientConnection(serverName, serverPlatformURLRoot, secretsConnection);
        this.auditLog = auditLog;
    }


    /**
     * Constructor for authenticated client connector
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param secretsStoreConnectorMap map from authentication type to supplied secrets store
     * @param auditLog audit log
     * @throws NoSuchMethodException Unable to work with the connector provider class
     * @throws ClassNotFoundException  Connector provider class not in path
     * @throws InvocationTargetException  Unable to work with the connector provider class
     * @throws InstantiationException  Unable to work with the connector provider class
     * @throws IllegalAccessException  Unable to work with the connector provider class
     */
    public RESTClientFactory(String                             serverName,
                             String                             serverPlatformURLRoot,
                             Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                             AuditLog                           auditLog) throws ClassNotFoundException,
                                                                                 InvocationTargetException,
                                                                                 NoSuchMethodException,
                                                                                 InstantiationException,
                                                                                 IllegalAccessException
    {
        this.clientConnection = this.getSpringRESTClientConnection(serverName, serverPlatformURLRoot, secretsStoreConnectorMap);
        this.auditLog = auditLog;
    }


    /**
     * Return the connection object for a Spring based REST Client.  This connection object is built up from
     * information from the caller.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param secretsStoreConnectorMap supplied secrets store
     * @return connection object
     * @throws NoSuchMethodException Unable to work with the connector provider class
     * @throws ClassNotFoundException  Connector provider class not in path
     * @throws InvocationTargetException  Unable to work with the connector provider class
     * @throws InstantiationException  Unable to work with the connector provider class
     * @throws IllegalAccessException  Unable to work with the connector provider class
     */
    private Connection getSpringRESTClientConnection(String                             serverName,
                                                     String                             serverPlatformURLRoot,
                                                     Map<String, SecretsStoreConnector> secretsStoreConnectorMap) throws ClassNotFoundException,
                                                                                                                         InvocationTargetException,
                                                                                                                         NoSuchMethodException,
                                                                                                                         InstantiationException,
                                                                                                                         IllegalAccessException
    {
        Connection  connection;

        if ((secretsStoreConnectorMap == null) || (secretsStoreConnectorMap.isEmpty()))
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
                Connection  secretStoreConnection = secretsStoreConnector.getConnection();

                embeddedConnection.setEmbeddedConnection(secretStoreConnection);

                embeddedConnections.add(embeddedConnection);
            }

            virtualConnection.setEmbeddedConnections(embeddedConnections);

            connection = virtualConnection;
        }

        Endpoint endpoint = new Endpoint();

        endpoint.setNetworkAddress(serverPlatformURLRoot);
        endpoint.setDisplayName(serverName);
        endpoint.setQualifiedName(serverName);

        connection.setEndpoint(endpoint);
        connection.setConnectorType(Connection.getConnectorType(SpringRESTClientConnectorProvider.class.getName()));
        connection.setQualifiedName(endpoint.getNetworkAddress());

        return connection;
    }


    /**
     * Return the connection object for a Spring based REST Client.  This connection object is built up from
     * information from the caller.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param secretsStoreConnector supplied secrets store
     * @return connection object
     * @throws NoSuchMethodException Unable to work with the connector provider class
     * @throws ClassNotFoundException  Connector provider class not in path
     * @throws InvocationTargetException  Unable to work with the connector provider class
     * @throws InstantiationException  Unable to work with the connector provider class
     * @throws IllegalAccessException  Unable to work with the connector provider class
     */
    private Connection getSpringRESTClientConnection(String             serverName,
                                                     String             serverPlatformURLRoot,
                                                     EmbeddedConnection secretsStoreConnector) throws ClassNotFoundException,
                                                                                                      InvocationTargetException,
                                                                                                      NoSuchMethodException,
                                                                                                      InstantiationException,
                                                                                                      IllegalAccessException
    {
        Connection  connection;

        if (secretsStoreConnector == null)
        {
            connection = new Connection();
        }
        else
        {
            VirtualConnection  virtualConnection = new VirtualConnection();

            List<EmbeddedConnection> embeddedConnections = new ArrayList<>();

            embeddedConnections.add(secretsStoreConnector);

            virtualConnection.setEmbeddedConnections(embeddedConnections);

            connection = virtualConnection;
        }

        Endpoint endpoint = new Endpoint();

        endpoint.setNetworkAddress(serverPlatformURLRoot);
        endpoint.setDisplayName(serverName);
        endpoint.setQualifiedName(serverName);

        connection.setEndpoint(endpoint);
        connection.setConnectorType(Connection.getConnectorType(SpringRESTClientConnectorProvider.class.getName()));
        connection.setQualifiedName(endpoint.getNetworkAddress());

        return connection;
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
        ConnectorBroker     connectorBroker = new ConnectorBroker(auditLog);
        Connector           connector       = connectorBroker.getConnector(clientConnection);

        connector.start();
        return (RESTClientConnector)connector;
    }
}
