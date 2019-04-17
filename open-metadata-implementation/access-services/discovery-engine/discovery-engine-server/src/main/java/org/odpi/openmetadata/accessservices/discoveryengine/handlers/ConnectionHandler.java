/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.handlers;

import org.odpi.openmetadata.accessservices.discoveryengine.builders.ConnectionBuilder;
import org.odpi.openmetadata.accessservices.discoveryengine.builders.EmbeddedConnectionBuilder;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.ConnectionConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.accessservices.discoveryengine.mappers.ConnectionMapper;
import org.odpi.openmetadata.accessservices.discoveryengine.mappers.EndpointMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * ConnectionHandler retrieves Connection objects from the property server.  It runs server-side in the AssetConsumer
 * OMAS and retrieves Connections through the OMRSRepositoryConnector.
 */
class ConnectionHandler
{
    private String                    serviceName;
    private OMRSRepositoryHelper      repositoryHelper;
    private String                    serverName;
    private InvalidParameterHandler   invalidParameterHandler = new InvalidParameterHandler();
    private BasicHandler              basicHandler;
    private EndpointHandler           endpointHandler;
    private ConnectorTypeHandler      connectorTypeHandler;


    /**
     * Construct the connection handler with information needed to work with Connection objects.
     *
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param repositoryHelper    helper utilities for managing repository services objects
     * @param basicHandler handler for interfacing with the repository services
     */
    ConnectionHandler(String                  serviceName,
                      String                  serverName,
                      OMRSRepositoryHelper    repositoryHelper,
                      BasicHandler            basicHandler)
    {
        this.serviceName = serviceName;
        this.repositoryHelper = repositoryHelper;
        this.serverName = serverName;
        this.basicHandler = basicHandler;

        this.endpointHandler = new EndpointHandler(serviceName,
                                                   serverName,
                                                   basicHandler,
                                                   repositoryHelper);
        this.connectorTypeHandler = new ConnectorTypeHandler(serviceName,
                                                             serverName,
                                                             basicHandler,
                                                             repositoryHelper);
    }


    /**
     * Find out if the connection object is already stored in the repository.  If the connection's
     * guid is set, it uses it to retrieve the entity.  If the GUID is not set, it tries the
     * fully qualified name.  If neither are set it throws an exception.
     *
     * @param userId calling user
     * @param connection object to find
     * @param methodName calling method
     *
     * @return unique identifier of the connection or null
     *
     * @throws InvalidParameterException the connection bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String findConnection(String               userId,
                                  Connection           connection,
                                  String               methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String  guidParameterName = "connection.getGUID";
        final String  qualifiedNameParameter = "connection.getQualifiedName";

        if (connection != null)
        {
            if (connection.getGUID() != null)
            {
                if (basicHandler.validateEntityGUID(userId,
                                                    connection.getGUID(),
                                                    ConnectionMapper.CONNECTION_TYPE_NAME,
                                                    methodName,
                                                    guidParameterName) != null)
                {
                    return connection.getGUID();
                }
            }

            invalidParameterHandler.validateName(connection.getQualifiedName(), qualifiedNameParameter, methodName);

            ConnectionBuilder connectionBuilder = new ConnectionBuilder(connection.getQualifiedName(),
                                                                        connection.getDisplayName(),
                                                                        connection.getDescription(),
                                                                        repositoryHelper,
                                                                        serviceName,
                                                                        serverName);

            EntityDetail existingConnection = basicHandler.getUniqueEntityByName(userId,
                                                                                 connection.getQualifiedName(),
                                                                                 qualifiedNameParameter,
                                                                                 connectionBuilder.getQualifiedNameInstanceProperties(methodName),
                                                                                 ConnectionMapper.CONNECTION_TYPE_GUID,
                                                                                 ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                                 methodName);
            if (existingConnection != null)
            {
                return existingConnection.getGUID();
            }
        }

        return null;
    }


    /**
     * Determine if the Connection object is stored in the repository and create it if it is not.
     * If the connection is located, there is no check that the connection values are equal to those in
     * the supplied object.
     *
     * @param userId calling userId
     * @param connection object to add
     *
     * @return unique identifier of the connection in the repository.
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String  saveConnection(String             userId,
                           Connection         connection) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String  methodName        = "saveConnection";

        String existingConnection = this.findConnection(userId, connection, methodName);
        if (existingConnection == null)
        {
            return addConnection(userId, connection);
        }
        else
        {
            return updateConnection(userId, existingConnection, connection);
        }
    }


    /**
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of connected connection
     * @param endpoint endpoint object or null
     * @param connectorType connector type object or null
     * @param embeddedConnections list of embedded connections or null - only for Virtual Connections
     * @param methodName calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private void saveAssociatedConnectionEntities(String                   userId,
                                                  String                   connectionGUID,
                                                  Endpoint                 endpoint,
                                                  ConnectorType            connectorType,
                                                  List<EmbeddedConnection> embeddedConnections,
                                                  String                   methodName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        if (endpoint != null)
        {
            String endpointGUID = endpointHandler.saveEndpoint(userId, endpoint);

            if (endpointGUID != null)
            {
                basicHandler.createRelationship(userId,
                                                ConnectionMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                                endpointGUID,
                                                connectionGUID,
                                                null,
                                                methodName);
            }
        }


        if (connectorType != null)
        {
            String connectorTypeGUID = connectorTypeHandler.saveConnectorType(userId, connectorType);

            if (connectorTypeGUID != null)
            {
                basicHandler.createRelationship(userId,
                                                ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                connectionGUID,
                                                connectorTypeGUID,
                                                null,
                                                methodName);
            }
        }

        if ((embeddedConnections != null) && (! embeddedConnections.isEmpty()))
        {
            for (EmbeddedConnection embeddedConnection : embeddedConnections)
            {
                if (embeddedConnection != null)
                {
                    Connection           realConnection = embeddedConnection.getEmbeddedConnection();
                    Map<String, Object>  arguments = embeddedConnection.getArguments();

                    String realConnectionGUID = this.saveConnection(userId, realConnection);

                    if (realConnection != null)
                    {
                        EmbeddedConnectionBuilder embeddedConnectionBuilder = new EmbeddedConnectionBuilder(arguments,
                                                                                                            repositoryHelper,
                                                                                                            serviceName);
                        basicHandler.createRelationship(userId,
                                                        ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                        connectionGUID,
                                                        realConnectionGUID,
                                                        embeddedConnectionBuilder.getInstanceProperties(methodName),
                                                        methodName);
                    }
                }
            }
        }
    }


    /**
     * Create a new Connection object and return its unique identifier (guid).
     * If the connection is located, there is no check that the connection values are equal to those in
     * the supplied object.
     *
     * @param userId calling userId
     * @param connection object to add
     *
     * @return unique identifier of the connection in the repository.
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String  addConnection(String             userId,
                                  Connection         connection) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String  methodName        = "addConnection";

        String                   connectionTypeGUID  = ConnectionMapper.CONNECTION_TYPE_GUID;
        String                   connectionTypeName  = ConnectionMapper.CONNECTION_TYPE_NAME;
        List<EmbeddedConnection> embeddedConnections = null;

        if (connection instanceof VirtualConnection)
        {
            connectionTypeGUID = ConnectionMapper.VIRTUAL_CONNECTION_TYPE_GUID;
            connectionTypeName = ConnectionMapper.VIRTUAL_CONNECTION_TYPE_NAME;

            VirtualConnection  virtualConnection = (VirtualConnection)connection;

            embeddedConnections = virtualConnection.getEmbeddedConnections();
        }

        ConnectionBuilder connectionBuilder = new ConnectionBuilder(connection.getQualifiedName(),
                                                                    connection.getDisplayName(),
                                                                    connection.getDescription(),
                                                                    connection.getAdditionalProperties(),
                                                                    connection.getSecuredProperties(),
                                                                    connection.getConfigurationProperties(),
                                                                    connection.getUserId(),
                                                                    connection.getClearPassword(),
                                                                    connection.getEncryptedPassword(),
                                                                    connection.getExtendedProperties(),
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        String connectionGUID = basicHandler.createEntity(userId,
                                                          connectionTypeGUID,
                                                          connectionTypeName,
                                                          connectionBuilder.getInstanceProperties(methodName),
                                                          methodName);

        this.saveAssociatedConnectionEntities(userId,
                                              connectionGUID,
                                              connection.getEndpoint(),
                                              connection.getConnectorType(),
                                              embeddedConnections,
                                              methodName);

        return connectionGUID;
    }


    /**
     * Update a stored connection.
     *
     * @param userId userId
     * @param existingConnectionGUID unique identifier of the existing connection entity
     * @param connection new connection values
     *
     * @return unique identifier of the connection in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String updateConnection(String      userId,
                                    String      existingConnectionGUID,
                                    Connection  connection) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String  methodName        = "updateConnection";

        String                   connectionTypeGUID  = ConnectionMapper.CONNECTION_TYPE_GUID;
        String                   connectionTypeName  = ConnectionMapper.CONNECTION_TYPE_NAME;
        List<EmbeddedConnection> embeddedConnections = null;

        if (connection instanceof VirtualConnection)
        {
            connectionTypeGUID = ConnectionMapper.VIRTUAL_CONNECTION_TYPE_GUID;
            connectionTypeName = ConnectionMapper.VIRTUAL_CONNECTION_TYPE_NAME;

            VirtualConnection  virtualConnection = (VirtualConnection)connection;

            embeddedConnections = virtualConnection.getEmbeddedConnections();
        }


        ConnectionBuilder connectionBuilder = new ConnectionBuilder(connection.getQualifiedName(),
                                                                    connection.getDisplayName(),
                                                                    connection.getDescription(),
                                                                    connection.getAdditionalProperties(),
                                                                    connection.getSecuredProperties(),
                                                                    connection.getConfigurationProperties(),
                                                                    connection.getUserId(),
                                                                    connection.getClearPassword(),
                                                                    connection.getEncryptedPassword(),
                                                                    connection.getExtendedProperties(),
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);
        basicHandler.updateEntity(userId,
                                  existingConnectionGUID,
                                  connectionTypeGUID,
                                  connectionTypeName,
                                  connectionBuilder.getInstanceProperties(methodName),
                                  methodName);

        this.saveAssociatedConnectionEntities(userId,
                                              existingConnectionGUID,
                                              connection.getEndpoint(),
                                              connection.getConnectorType(),
                                              embeddedConnections,
                                              methodName);

        return existingConnectionGUID;
    }


    /**
     * Remove the requested Connection if it is no longer connected to any other asset
     * definition.
     *
     * @param userId calling user
     * @param connectionGUID object to delete
     *
     * @throws InvalidParameterException the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    void     removeConnection(String                 userId,
                              String                 connectionGUID) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String  methodName = "removeConnection";
        final String  guidParameterName = "connectionGUID";

        Connection connection = this.getConnection(userId, connectionGUID);

        if (connection != null)
        {
            Endpoint endpoint = connection.getEndpoint();

            if (endpoint != null)
            {
                basicHandler.deleteRelationshipBetweenEntities(userId,
                                                               ConnectionMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                                               ConnectionMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                                               endpoint.getGUID(),
                                                               EndpointMapper.ENDPOINT_TYPE_NAME,
                                                               connectionGUID,
                                                               methodName);
                endpointHandler.removeEndpoint(userId, endpoint.getGUID());
            }


            ConnectorType connectorType = connection.getConnectorType();

            if (connectorType != null)
            {
                basicHandler.deleteRelationshipBetweenEntities(userId,
                                                               ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                               ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                                               connectionGUID,
                                                               ConnectionMapper.CONNECTION_TYPE_NAME,
                                                               connectorType.getGUID(),
                                                               methodName);
                connectorTypeHandler.removeConnectorType(userId, connectorType.getGUID());
            }

            if (connection instanceof VirtualConnection)
            {
                List<EmbeddedConnection> embeddedConnections = ((VirtualConnection) connection).getEmbeddedConnections();

                if (embeddedConnections != null)
                {
                    for (EmbeddedConnection embeddedConnection : embeddedConnections)
                    {
                        Connection realConnection = embeddedConnection.getEmbeddedConnection();

                        basicHandler.deleteRelationshipBetweenEntities(userId,
                                                                       ConnectionMapper.EMBEDDED_CONNECTION_TYPE_GUID,
                                                                       ConnectionMapper.EMBEDDED_CONNECTION_TYPE_NAME,
                                                                       connectionGUID,
                                                                       ConnectionMapper.VIRTUAL_CONNECTION_TYPE_NAME,
                                                                       realConnection.getGUID(),
                                                                       methodName);

                        if (realConnection != null)
                        {
                            this.removeConnection(userId, realConnection.getGUID());
                        }
                    }
                }
            }

            basicHandler.deleteEntityOnLastUse(userId,
                                               connectionGUID,
                                               guidParameterName,
                                               ConnectionMapper.CONNECTION_TYPE_GUID,
                                               ConnectionMapper.CONNECTION_TYPE_NAME,
                                               methodName);
        }
    }


    /**
     * Retrieve the requested connection object.
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection object.
     * @return Connection bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    Connection   getConnection(String                 userId,
                               String                 connectionGUID) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String  methodName = "getConnection";
        final String  guidParameterName = "connectionGUID";

        EntityDetail connectionEntity = basicHandler.getEntityByGUID(userId,
                                                                     connectionGUID,
                                                                     guidParameterName,
                                                                     ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                     methodName);

        Endpoint endpoint = null;
        Relationship relationship = basicHandler.getUniqueRelationshipByType(userId,
                                                                             connectionGUID,
                                                                             ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                             ConnectionMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                                                             ConnectionMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                                                             methodName);

        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityOneProxy();

            if (entityProxy != null)
            {
                endpoint = endpointHandler.getEndpoint(userId,
                                                       entityProxy.getGUID());
            }
        }

        ConnectorType connectorType = null;
        relationship = basicHandler.getUniqueRelationshipByType(userId,
                                                                connectionGUID,
                                                                ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                                ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                                                methodName);

        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityTwoProxy();

            if (entityProxy != null)
            {
                connectorType = connectorTypeHandler.getConnectorType(userId,
                                                                      entityProxy.getGUID());
            }
        }

        List<EmbeddedConnection>  embeddedConnections = new ArrayList<>();

        List<Relationship>  relationships = basicHandler.getRelationshipsByType(userId,
                                                                                connectionGUID,
                                                                                ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                                ConnectionMapper.EMBEDDED_CONNECTION_TYPE_GUID,
                                                                                ConnectionMapper.EMBEDDED_CONNECTION_TYPE_GUID,
                                                                                methodName);

        if (relationships != null)
        {
            for (Relationship embeddedConnectionRelationship : relationships)
            {
                if (embeddedConnectionRelationship != null)
                {
                    EmbeddedConnection  embeddedConnection = new EmbeddedConnection();

                    embeddedConnection.setArguments(repositoryHelper.getMapFromProperty(serviceName,
                                                                                        ConnectionMapper.ARGUMENTS_PROPERTY_NAME,
                                                                                        embeddedConnectionRelationship.getProperties(),
                                                                                        methodName));

                    EntityProxy entityProxy = embeddedConnectionRelationship.getEntityTwoProxy();

                    if (entityProxy != null)
                    {
                        embeddedConnection.setEmbeddedConnection(this.getConnection(userId, entityProxy.getGUID()));
                    }
                }
            }
        }


        ConnectionConverter connectionConverter = new ConnectionConverter(connectionEntity,
                                                                          endpoint,
                                                                          connectorType,
                                                                          embeddedConnections,
                                                                          repositoryHelper,
                                                                          methodName);

        return connectionConverter.getBean();
    }
}
