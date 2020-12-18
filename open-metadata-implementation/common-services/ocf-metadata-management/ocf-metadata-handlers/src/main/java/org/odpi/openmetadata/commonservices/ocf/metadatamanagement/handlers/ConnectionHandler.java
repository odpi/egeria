/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ConnectionBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.EmbeddedConnectionBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.ConnectionConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * ConnectionHandler retrieves Connection objects from the property server.  It runs server-side in the
 * the OMAG Server Platform and retrieves Connections through the OMRSRepositoryConnector.
 */
public class ConnectionHandler extends RootHandler
{
    private EndpointHandler         endpointHandler;
    private ConnectorTypeHandler    connectorTypeHandler;


    /**
     * Construct the connection handler with information needed to work with Connection objects.
     *
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler handler for interfacing with the repository services
     * @param repositoryHelper    helper utilities for managing repository services objects
     */
    public ConnectionHandler(String                  serviceName,
                             String                  serverName,
                             InvalidParameterHandler invalidParameterHandler,
                             RepositoryHandler       repositoryHandler,
                             OMRSRepositoryHelper    repositoryHelper)
    {
        super(serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper);

        this.endpointHandler = new EndpointHandler(serviceName,
                                                   serverName,
                                                   invalidParameterHandler,
                                                   repositoryHandler,
                                                   repositoryHelper);

        this.connectorTypeHandler = new ConnectorTypeHandler(serviceName,
                                                             serverName,
                                                             invalidParameterHandler,
                                                             repositoryHandler,
                                                             repositoryHelper);
    }


    /**
     * Find out if the connection object is already stored in the repository.  If the connection's
     * guid is set, it uses it to retrieve the entity.  If the GUID is not set, it tries the
     * fully qualified name.  If neither are set it returns null.
     *
     * @param userId calling user
     * @param connectionGUID unique Id
     * @param qualifiedName unique name
     * @param displayName human readable name
     * @param methodName calling method
     *
     * @return unique identifier of the connection or null
     *
     * @throws InvalidParameterException the connection bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String findConnection(String               userId,
                                  String               connectionGUID,
                                  String               qualifiedName,
                                  String               displayName,
                                  String               methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String guidParameterName        = "connectionGUID";
        final String qualifiedNameParameter   = "qualifiedName";

        if (connectionGUID != null)
        {
            /*
             * The connection object has a GUID in it.  This would typically be blank if the connection
             * is to be created.  The guid is accepted if an entity of the right type is found.
             * Otherwise it is ignored.
             */
            if (repositoryHandler.isEntityKnown(userId,
                                                connectionGUID,
                                                ConnectionMapper.CONNECTION_TYPE_NAME,
                                                methodName,
                                                guidParameterName) != null)
            {
                return connectionGUID;
            }
        }

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        ConnectionBuilder connectionBuilder = new ConnectionBuilder(qualifiedName,
                                                                    displayName,
                                                                    null,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        /*
         * The qualified name should be unique.
         */
        EntityDetail existingConnection = repositoryHandler.getUniqueEntityByName(userId,
                                                                                  qualifiedName,
                                                                                  qualifiedNameParameter,
                                                                                  connectionBuilder.getQualifiedNameInstanceProperties(methodName),
                                                                                  ConnectionMapper.CONNECTION_TYPE_GUID,
                                                                                  ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                                  methodName);
        if (existingConnection != null)
        {
            return existingConnection.getGUID();
        }

        return null;
    }


    /**
     * Find out if the connection object is already stored in the repository.  If the connection's
     * guid is set, it uses it to retrieve the entity.  If the GUID is not set, it tries the
     * fully qualified name.  If neither are set it throws an exception.
     *
     * @param userId calling user
     * @param qualifiedName unique name
     * @param displayName human readable name
     * @param methodName calling method
     *
     * @return unique identifier of the connection or null
     *
     * @throws InvalidParameterException the connection bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String findConnectionByName(String               userId,
                                        String               qualifiedName,
                                        String               displayName,
                                        String               methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        String connectionGUID = this.findConnection(userId, null, qualifiedName, displayName, methodName);

        if (connectionGUID != null)
        {
            return connectionGUID;
        }

        /*
         * Now try to find using the display name - this is not guaranteed to be unique so an exception may occur.
         */
        if (displayName != null)
        {
            final String qualifiedNameParameter   = "qualifiedName";

            ConnectionBuilder connectionBuilder = new ConnectionBuilder(qualifiedName,
                                                                        displayName,
                                                                        null,
                                                                        repositoryHelper,
                                                                        serviceName,
                                                                        serverName);

            EntityDetail existingConnection = repositoryHandler.getUniqueEntityByName(userId,
                                                                                      qualifiedName,
                                                                                      qualifiedNameParameter,
                                                                                      connectionBuilder.getNameInstanceProperties(methodName),
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
        if (connection != null)
        {
            return this.findConnection(userId,
                                       connection.getGUID(),
                                       connection.getQualifiedName(),
                                       connection.getDisplayName(),
                                       methodName);
        }

        return null;
    }


    /**
     * Determine if the Connection object is stored in the repository and create it if it is not.
     * If the connection is located, there is no check that the connection values are equal to those in
     * the supplied object.
     *
     * @param userId calling userId
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connection object to add
     *
     * @return unique identifier of the connection in the repository.
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  saveConnection(String      userId,
                                  String      externalSourceGUID,
                                  String      externalSourceName,
                                  Connection  connection) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName                 = "saveConnection";
        final String connectionParameterName    = "connection";
        final String connectorTypeParameterName = "connection.connectorType";

        /*
         * The connection should always have a connector type - the endpoint is optional.
         */
        invalidParameterHandler.validateObject(connection, connectionParameterName, methodName);
        invalidParameterHandler.validateObject(connection.getConnectorType(), connectorTypeParameterName, methodName);

        String existingConnection = this.findConnection(userId, connection, methodName);
        if (existingConnection == null)
        {
            return addConnection(userId, externalSourceGUID, externalSourceName, connection);
        }
        else
        {
            return updateConnection(userId, externalSourceGUID, externalSourceName, existingConnection, connection);
        }
    }


    /**
     * Save the connectorType, endpoint and any embedded connections.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
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
                                                  String                   externalSourceGUID,
                                                  String                   externalSourceName,
                                                  String                   connectionGUID,
                                                  Endpoint                 endpoint,
                                                  ConnectorType            connectorType,
                                                  List<EmbeddedConnection> embeddedConnections,
                                                  String                   methodName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        /*
         * Match the content in the repository with the connection description passed in on the parameters.
         * It updates the endpoint, then the connector type and then the embedded connections.
         */
        if (endpoint != null)
        {
            /*
             * This connection has an endpoint.
             */
            String endpointGUID = endpointHandler.saveEndpoint(userId, externalSourceGUID, externalSourceName, endpoint);

            if (endpointGUID != null)
            {
                /*
                 * Create a new relationship unless it already exists
                 */
                repositoryHandler.ensureRelationship(userId,
                                                     EndpointMapper.ENDPOINT_TYPE_NAME,
                                                     null,
                                                     null,
                                                     endpointGUID,
                                                     connectionGUID,
                                                     ConnectionMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                                     ConnectionMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                                     null,
                                                     methodName);
            }
        }
        else
        {
            /*
             * There should be no relationships between the connection and the endpoint.  So this would normally be a no-op.
             * The only time this would do something is if the connection is already defined and this update is to remove the Endpoint.
             * Since we don't know the original GUID for the endpoint, and a connection can only be connected to one endpoint,
             * it is safe to delete any relationships to endpoints from this connection.
             */
            repositoryHandler.removeAllRelationshipsOfType(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           connectionGUID,
                                                           ConnectionMapper.CONNECTION_TYPE_NAME,
                                                           ConnectionMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                                           ConnectionMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                                           methodName);
        }

        if (connectorType != null)
        {
            String connectorTypeGUID = connectorTypeHandler.saveConnectorType(userId, externalSourceGUID, externalSourceName, connectorType);

            if (connectorTypeGUID != null)
            {
                repositoryHandler.ensureRelationship(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     ConnectionMapper.CONNECTION_TYPE_NAME,
                                                     connectionGUID,
                                                     connectorTypeGUID,
                                                     ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                     ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                                     null,
                                                     methodName);
            }
        }
        else
        {
            /*
             * There should be no relationships between the connection and the connector type.  So this would normally be a no-op.
             * The only time this would do something is if the connection is already defined and this update is to remove the connectorType.
             * Since we don't know the original GUID for the connector type, and a connection can only be connected to one connectorType,
             * it is safe to delete any relationships to connectorTypes from this connection.
             */
            repositoryHandler.removeAllRelationshipsOfType(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           connectionGUID,
                                                           ConnectionMapper.CONNECTION_TYPE_NAME,
                                                           ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                           ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                                           methodName);
        }

        /*
         * Managing embedded connections is awkward.  This approach is a bit of a blunt instrument but handles the cases where the
         * virtual connection is being reorganized.
         */
        repositoryHandler.removeAllRelationshipsOfType(userId,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       connectionGUID,
                                                       ConnectionMapper.CONNECTION_TYPE_NAME,
                                                       ConnectionMapper.EMBEDDED_CONNECTION_TYPE_GUID,
                                                       ConnectionMapper.EMBEDDED_CONNECTION_TYPE_NAME,
                                                       methodName);

        if ((embeddedConnections != null) && (! embeddedConnections.isEmpty()))
        {
            for (EmbeddedConnection embeddedConnection : embeddedConnections)
            {
                if (embeddedConnection != null)
                {
                    Connection realConnection = embeddedConnection.getEmbeddedConnection();

                    String realConnectionGUID = this.saveConnection(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    realConnection);

                    if (realConnection != null)
                    {
                        EmbeddedConnectionBuilder embeddedConnectionBuilder = new EmbeddedConnectionBuilder(embeddedConnection.getArguments(),
                                                                                                            embeddedConnection.getDisplayName(),
                                                                                                            repositoryHelper,
                                                                                                            serviceName);
                        repositoryHandler.createRelationship(userId,
                                                             ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                             externalSourceGUID,
                                                             externalSourceName,
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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connection object to add
     *
     * @return unique identifier of the connection in the repository.
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String  addConnection(String     userId,
                                  String     externalSourceGUID,
                                  String     externalSourceName,
                                  Connection connection) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String  methodName        = "addConnection";
        final String  parameterName     = "connection.connectorType";

        invalidParameterHandler.validateObject(connection.getConnectorType(), parameterName, methodName);

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

        String connectionGUID = repositoryHandler.createEntity(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               connectionTypeGUID,
                                                               connectionTypeName,
                                                               connectionBuilder.getInstanceProperties(methodName),
                                                               methodName);

        this.saveAssociatedConnectionEntities(userId,
                                              externalSourceGUID,
                                              externalSourceName,
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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
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
                                    String      externalSourceGUID,
                                    String      externalSourceName,
                                    String      existingConnectionGUID,
                                    Connection  connection) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String  methodName        = "updateConnection";
        final String  parameterName     = "connection.connectorType";

        invalidParameterHandler.validateObject(connection.getConnectorType(), parameterName, methodName);

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
        repositoryHandler.updateEntityProperties(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 existingConnectionGUID,
                                                 connectionTypeGUID,
                                                 connectionTypeName,
                                                 connectionBuilder.getInstanceProperties(methodName),
                                                 methodName);

        this.saveAssociatedConnectionEntities(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              existingConnectionGUID,
                                              connection.getEndpoint(),
                                              connection.getConnectorType(),
                                              embeddedConnections,
                                              methodName);

        return existingConnectionGUID;
    }


    /**
     * Remove the requested Connection and any associated objects if they are no longer connected to
     * anything else.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connectionGUID object to delete
     *
     * @throws InvalidParameterException the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void     removeConnection(String userId,
                                     String externalSourceGUID,
                                     String externalSourceName,
                                     String connectionGUID) throws InvalidParameterException,
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
                repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    ConnectionMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                                                    ConnectionMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                                                    endpoint.getGUID(),
                                                                    EndpointMapper.ENDPOINT_TYPE_NAME,
                                                                    connectionGUID,
                                                                    methodName);
                endpointHandler.removeEndpoint(userId, externalSourceGUID, externalSourceName, endpoint.getGUID());
            }


            ConnectorType connectorType = connection.getConnectorType();

            if (connectorType != null)
            {
                repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                                    ConnectionMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                                                    connectionGUID,
                                                                    ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                    connectorType.getGUID(),
                                                                    methodName);
                connectorTypeHandler.removeConnectorType(userId, externalSourceGUID, externalSourceName, connectorType.getGUID());
            }

            if (connection instanceof VirtualConnection)
            {
                List<EmbeddedConnection> embeddedConnections = ((VirtualConnection) connection).getEmbeddedConnections();

                if (embeddedConnections != null)
                {
                    for (EmbeddedConnection embeddedConnection : embeddedConnections)
                    {
                        Connection realConnection = embeddedConnection.getEmbeddedConnection();

                        if (realConnection != null)
                        {
                            repositoryHandler.removeRelationshipBetweenEntities(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    ConnectionMapper.EMBEDDED_CONNECTION_TYPE_GUID,
                                    ConnectionMapper.EMBEDDED_CONNECTION_TYPE_NAME,
                                    connectionGUID,
                                    ConnectionMapper.VIRTUAL_CONNECTION_TYPE_NAME,
                                    realConnection.getGUID(),
                                    methodName);

                            this.removeConnection(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  realConnection.getGUID());
                        }
                    }
                }
            }

            repositoryHandler.removeEntityOnLastUse(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
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
    public Connection   getConnection(String                 userId,
                                      String                 connectionGUID) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String  methodName = "getConnection";
        final String  guidParameterName = "connectionGUID";

        EntityDetail connectionEntity = repositoryHandler.getEntityByGUID(userId,
                                                                          connectionGUID,
                                                                          guidParameterName,
                                                                          ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                          methodName);

        Endpoint endpoint = null;
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
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
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
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

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
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

                    embeddedConnections.add(embeddedConnection);
                }
            }
        }

        if (embeddedConnections.isEmpty())
        {
            embeddedConnections = null;
        }

        ConnectionConverter connectionConverter = new ConnectionConverter(connectionEntity,
                                                                          endpoint,
                                                                          connectorType,
                                                                          embeddedConnections,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

        return connectionConverter.getBean();
    }


    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param userId  String - userId of user making request.
     * @param name  this may be the qualifiedName or displayName of the connection.
     * @param methodName calling method
     *
     * @return Connection retrieved from property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Connection getConnectionByName(String   userId,
                                          String   name,
                                          String   methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final  String   nameParameter = "name";

        invalidParameterHandler.validateName(name, nameParameter, methodName);

        String connectionGUID = findConnectionByName(userId,
                                                     name,
                                                     name,
                                                     methodName);

        if (connectionGUID != null)
        {
            return getConnection(userId, connectionGUID);
        }

        return null;
    }
}
