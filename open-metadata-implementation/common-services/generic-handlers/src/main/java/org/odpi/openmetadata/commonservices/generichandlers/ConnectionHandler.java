/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ConnectionHandler manages Connection objects.  These describe the network addresses where services are running.  They are used by connection
 * objects to describe the service that the connector should call.  They are linked to servers to show their network address where the services that
 * they are hosting are running.
 * 
 * Most OMASs that work with Connection objects use the Open Connector Framework (OCF) Bean since this can be passed to the OCF Connector
 * Broker to create an instance of a connector to the attached asset.  Therefore this handler has a default bean and converter which
 * is the one that works with the OCF bean.  The call can either use these values or override with their own bean/converter implementations.
 *
 * ConnectionHandler runs server-side in the OMAG Server Platform and retrieves Connection entities through the OMRSRepositoryConnector via the
 * generic handler and repository handler.
 */
public class ConnectionHandler<B> extends ReferenceableHandler<B>
{
    private EndpointHandler<OpenMetadataAPIDummyBean>       endpointHandler;
    private ConnectorTypeHandler<OpenMetadataAPIDummyBean>  connectorTypeHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public ConnectionHandler(OpenMetadataAPIGenericConverter<B> converter,
                             Class<B>                           beanClass,
                             String                             serviceName,
                             String                             serverName,
                             InvalidParameterHandler            invalidParameterHandler,
                             RepositoryHandler                  repositoryHandler,
                             OMRSRepositoryHelper               repositoryHelper,
                             String                             localServerUserId,
                             OpenMetadataServerSecurityVerifier securityVerifier,
                             List<String>                       supportedZones,
                             List<String>                       defaultZones,
                             List<String>                       publishZones,
                             AuditLog                           auditLog)

    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

        this.endpointHandler = new EndpointHandler<>(new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                     OpenMetadataAPIDummyBean.class,
                                                     serviceName,
                                                     serverName,
                                                     invalidParameterHandler,
                                                     repositoryHandler,
                                                     repositoryHelper,
                                                     localServerUserId,
                                                     securityVerifier,
                                                     supportedZones,
                                                     defaultZones,
                                                     publishZones,
                                                     auditLog);

        this.connectorTypeHandler = new ConnectorTypeHandler<>(new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                               OpenMetadataAPIDummyBean.class,
                                                               serviceName,
                                                               serverName,
                                                               invalidParameterHandler,
                                                               repositoryHandler,
                                                               repositoryHelper,
                                                               localServerUserId,
                                                               securityVerifier,
                                                               supportedZones,
                                                               defaultZones,
                                                               publishZones,
                                                               auditLog);
    }

    
    /*
     * ========================================================
     * This first set of methods work with OCF Connection Beans
     */

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
        final String displayNameParameter     = "displayName";

        if (connectionGUID != null)
        {
            /*
             * The connection object has a GUID in it.  This would typically be blank if the connection
             * is to be created.  The guid is accepted if an entity of the right type is found.
             * Otherwise it is ignored.
             */
            try
            {
                if (this.getEntityFromRepository(userId,
                                                 connectionGUID,
                                                 guidParameterName,
                                                 OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                 methodName) != null)
                {
                    return connectionGUID;
                }
            }
            catch (InvalidParameterException notFound)
            {
                /*
                 * Not found via the GUID so try something else
                 */
            }
        }

        String retrievedGUID = null;

        if (qualifiedName != null)
        {
            retrievedGUID = this.getBeanGUIDByUniqueName(userId,
                                                         qualifiedName,
                                                         qualifiedNameParameter,
                                                         OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                         OpenMetadataAPIMapper.CONNECTION_TYPE_GUID,
                                                         OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                         supportedZones,
                                                         methodName);
        }

        if ((retrievedGUID == null) && (displayName != null))
        {
            retrievedGUID = this.getBeanGUIDByUniqueName(userId,
                                                         displayName,
                                                         displayNameParameter,
                                                         OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                         OpenMetadataAPIMapper.CONNECTION_TYPE_GUID,
                                                         OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                         supportedZones,
                                                         methodName);
        }

        return retrievedGUID;
    }


    /**
     * Determine if the Connection object is stored in the repository and create it if it is not.
     * If the connection is located, there is no check that the connection values are equal to those in
     * the supplied object.
     *
     * @param userId calling userId
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param anchorGUID unique identifier of the anchor to add to the connection
     * @param assetGUID unique identifier of linked asset (or null)
     * @param assetGUIDParameterName  parameter name supplying assetGUID
     * @param assetTypeName type of asset
     * @param connection object to add
     * @param assetSummary description of the asset for the connection
     * @param methodName calling method
     *
     * @return unique identifier of the connection in the repository.
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  saveConnection(String     userId,
                                  String     externalSourceGUID,
                                  String     externalSourceName,
                                  String     anchorGUID,
                                  String     assetGUID,
                                  String     assetGUIDParameterName,
                                  String     assetTypeName,
                                  Connection connection,
                                  String     assetSummary,
                                  String     methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String connectionParameterName    = "connection";
        final String connectorTypeParameterName = "connection.connectorType";

        /*
         * The connection should always have a connector type - the endpoint is optional.
         */
        invalidParameterHandler.validateObject(connection, connectionParameterName, methodName);
        invalidParameterHandler.validateObject(connection.getConnectorType(), connectorTypeParameterName, methodName);

        String existingConnectionGUID = this.findConnection(userId,
                                                            connection.getGUID(),
                                                            connection.getQualifiedName(),
                                                            connection.getDisplayName(),
                                                            methodName);
        if (existingConnectionGUID == null)
        {
            return addConnection(userId,
                                 externalSourceGUID,
                                 externalSourceName,
                                 anchorGUID,
                                 assetGUID,
                                 assetGUIDParameterName,
                                 assetTypeName,
                                 connection,
                                 assetSummary,
                                 methodName);
        }
        else
        {
            return updateConnection(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    anchorGUID,
                                    assetGUID,
                                    assetGUIDParameterName,
                                    assetTypeName,
                                    existingConnectionGUID,
                                    connection,
                                    methodName);
        }
    }


    /**
     * Save the connectorType, endpoint and any embedded connections.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param anchorGUID unique identifier of the anchor entity if applicable
     * @param assetGUID unique identifier of linked asset (or null)
     * @param assetGUIDParameterName  parameter name supplying assetGUID
     * @param assetTypeName type of asset
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
                                                  String                   anchorGUID,
                                                  String                   assetGUID,
                                                  String                   assetGUIDParameterName,
                                                  String                   assetTypeName,
                                                  String                   connectionGUID,
                                                  Endpoint                 endpoint,
                                                  ConnectorType            connectorType,
                                                  List<EmbeddedConnection> embeddedConnections,
                                                  String                   methodName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String connectionGUIDParameterName = "connectionGUID";

        /*
         * Match the content in the repository with the connection description passed in on the parameters.
         * It updates the endpoint, then the connector type and then the embedded connections.
         */
        if (endpoint != null)
        {
            /*
             * This connection has an endpoint.
             */
            String endpointGUID = endpointHandler.saveEndpoint(userId, externalSourceGUID, externalSourceName, endpoint, methodName);

            if (endpointGUID != null)
            {
                /*
                 * Create a new relationship unless it already exists
                 */
                repositoryHandler.ensureRelationship(userId,
                                                     OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                     null,
                                                     null,
                                                     endpointGUID,
                                                     connectionGUID,
                                                     OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                                     OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME,
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
                                                           OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                           OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                                           OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                                           methodName);
        }

        if (connectorType != null)
        {
            String connectorTypeGUID = connectorTypeHandler.saveConnectorType(userId,
                                                                              externalSourceGUID,
                                                                              externalSourceName,
                                                                              connectorType,
                                                                              methodName);

            if (connectorTypeGUID != null)
            {
                repositoryHandler.ensureRelationship(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                     connectionGUID,
                                                     connectorTypeGUID,
                                                     OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
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
                                                           OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                           OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                           OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                                           methodName);
        }

        /*
         * Managing embedded connections is awkward.  This approach is a bit of a blunt instrument but handles the cases where the
         * virtual connection is being reorganized.
         */
        this.unlinkAllElements(userId,
                               false,
                               externalSourceGUID,
                               externalSourceName,
                               connectionGUID,
                               connectionGUIDParameterName,
                               OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                               supportedZones,
                               OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_GUID,
                               OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_NAME,
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
                                                                    assetGUID,
                                                                    null,
                                                                    null,
                                                                    assetTypeName,
                                                                    realConnection,
                                                                    null,
                                                                    methodName);

                    if (realConnection != null)
                    {
                        EmbeddedConnectionBuilder embeddedConnectionBuilder =
                                new EmbeddedConnectionBuilder(embeddedConnection.getPosition(),
                                                              embeddedConnection.getArguments(),
                                                              embeddedConnection.getDisplayName(),
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

                        repositoryHandler.createRelationship(userId,
                                                             OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
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
     * @param anchorGUID unique identifier ofr the anchor GUID if applicable
     * @param assetGUID unique identifier of linked asset (or null)
     * @param assetGUIDParameterName  parameter name supplying assetGUID
     * @param assetTypeName type of asset
     * @param connection object to add
     * @param assetSummary description of the asset for the connection
     * @param methodName calling method
     *
     * @return unique identifier of the connection in the repository.
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String  addConnection(String     userId,
                                  String     externalSourceGUID,
                                  String     externalSourceName,
                                  String     anchorGUID,
                                  String     assetGUID,
                                  String     assetGUIDParameterName,
                                  String     assetTypeName,
                                  Connection connection,
                                  String     assetSummary,
                                  String     methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String  connectionParameterName     = "connection";

        invalidParameterHandler.validateObject(connection.getConnectorType(), connectionParameterName, methodName);

        String                   connectionTypeGUID  = OpenMetadataAPIMapper.CONNECTION_TYPE_GUID;
        String                   connectionTypeName  = OpenMetadataAPIMapper.CONNECTION_TYPE_NAME;
        List<EmbeddedConnection> embeddedConnections = null;

        if (connection instanceof VirtualConnection)
        {
            connectionTypeGUID = OpenMetadataAPIMapper.VIRTUAL_CONNECTION_TYPE_GUID;
            connectionTypeName = OpenMetadataAPIMapper.VIRTUAL_CONNECTION_TYPE_NAME;

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
                                                                    OpenMetadataAPIMapper.CONNECTION_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                    connection.getExtendedProperties(),
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        if (anchorGUID != null)
        {
            connectionBuilder.setAnchors(userId, anchorGUID, methodName);
        }

        String connectionGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            connectionTypeGUID,
                                                            connectionTypeName,
                                                            connection.getQualifiedName(),
                                                            OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                            connectionBuilder,
                                                            methodName);

        if (connectionGUID != null)
        {
            this.saveAssociatedConnectionEntities(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  anchorGUID,
                                                  assetGUID,
                                                  assetGUIDParameterName,
                                                  assetTypeName,
                                                  connectionGUID,
                                                  connection.getEndpoint(),
                                                  connection.getConnectorType(),
                                                  embeddedConnections,
                                                  methodName);

            if (assetGUID != null)
            {
                InstanceProperties properties = null;

                if (assetSummary != null)
                {
                    properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                              null,
                                                                              OpenMetadataAPIMapper.ASSET_SUMMARY_PROPERTY_NAME,
                                                                              assetSummary,
                                                                              methodName);
                }

                this.linkElementToElement(userId,
                                          null,
                                          null,
                                          connectionGUID,
                                          connectionParameterName,
                                          OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          assetTypeName,
                                          OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                          OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                          properties,
                                          methodName);
            }
        }

        return connectionGUID;
    }


    /**
     * Update a stored connection.
     *
     * @param userId userId
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param anchorGUID unique identifier ofr the anchor GUID if applicable
     * @param assetGUID unique identifier of linked asset (or null)
     * @param assetGUIDParameterName  parameter name supplying assetGUID
     * @param assetTypeName type of asset
     * @param existingConnectionGUID unique identifier of the existing connection entity
     * @param connection new connection values
     * @param methodName calling method
     *
     * @return unique identifier of the connection in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String updateConnection(String     userId,
                                    String     externalSourceGUID,
                                    String     externalSourceName,
                                    String     anchorGUID,
                                    String     assetGUID,
                                    String     assetGUIDParameterName,
                                    String     assetTypeName,
                                    String     existingConnectionGUID,
                                    Connection connection,
                                    String     methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String  parameterName     = "connection";

        invalidParameterHandler.validateObject(connection.getConnectorType(), parameterName, methodName);

        String                   connectionTypeGUID  = OpenMetadataAPIMapper.CONNECTION_TYPE_GUID;
        String                   connectionTypeName  = OpenMetadataAPIMapper.CONNECTION_TYPE_NAME;
        List<EmbeddedConnection> embeddedConnections = null;

        if (connection instanceof VirtualConnection)
        {
            connectionTypeGUID = OpenMetadataAPIMapper.VIRTUAL_CONNECTION_TYPE_GUID;
            connectionTypeName = OpenMetadataAPIMapper.VIRTUAL_CONNECTION_TYPE_NAME;

            VirtualConnection  virtualConnection = (VirtualConnection)connection;

            embeddedConnections = virtualConnection.getEmbeddedConnections();
        }

        this.updateConnection(userId,
                              externalSourceGUID,
                              externalSourceName,
                              existingConnectionGUID,
                              parameterName,
                              connection.getQualifiedName(),
                              connection.getDisplayName(),
                              connection.getDescription(),
                              connection.getAdditionalProperties(),
                              connection.getSecuredProperties(),
                              connection.getConfigurationProperties(),
                              connection.getUserId(),
                              connection.getClearPassword(),
                              connection.getEncryptedPassword(),
                              connectionTypeGUID,
                              connectionTypeName,
                              connection.getExtendedProperties(),
                              methodName);


        this.saveAssociatedConnectionEntities(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              anchorGUID,
                                              assetGUID,
                                              assetGUIDParameterName,
                                              assetTypeName,
                                              existingConnectionGUID,
                                              connection.getEndpoint(),
                                              connection.getConnectorType(),
                                              embeddedConnections,
                                              methodName);

        return existingConnectionGUID;
    }



    /*
     * =========================================================
     * This next set of beans work with any bean implementation of a connection
     */
    
    /**
     * Creates a new connection, connects it to the asset and returns the unique identifier for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param assetGUID the unique identifier for the asset entity (null for stand alone connections)
     * @param assetGUIDParameterName the parameter supplying assetGUID
     * @param assetSummary brief description of the asset for the relationship between the asset and the connection
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the connection.
     * @param additionalProperties additional properties
     * @param securedProperties protected properties
     * @param configurationProperties  properties passed to configure underlying technologies
     * @param connectorUserId user identity that the connector should use
     * @param clearPassword password for the userId in clear text
     * @param encryptedPassword encrypted password that the connector needs to decrypt before use
     * @param connectorTypeGUID unique identifier of the connector type to used for this connection
     * @param connectorTypeGUIDParameterName the parameter supplying connectorTypeGUID
     * @param endpointGUID unique identifier of the endpoint to used for this connection
     * @param endpointGUIDParameterName the parameter supplying endpointGUID
     * @param methodName calling method
     *
     * @return GUID for new connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connection properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createConnection(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              assetGUID,
                                   String              assetGUIDParameterName,
                                   String              assetSummary,
                                   String              qualifiedName,
                                   String              displayName,
                                   String              description,
                                   Map<String, String> additionalProperties,
                                   Map<String, String> securedProperties,
                                   Map<String, Object> configurationProperties,
                                   String              connectorUserId,
                                   String              clearPassword,
                                   String              encryptedPassword,
                                   String              connectorTypeGUID,
                                   String              connectorTypeGUIDParameterName,
                                   String              endpointGUID,
                                   String              endpointGUIDParameterName,
                                   String              methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        return this.createConnection(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     assetGUID,
                                     assetGUIDParameterName,
                                     assetSummary,
                                     OpenMetadataAPIMapper.CONNECTION_TYPE_GUID,
                                     OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                     qualifiedName,
                                     displayName,
                                     description,
                                     additionalProperties,
                                     securedProperties,
                                     configurationProperties,
                                     connectorUserId,
                                     clearPassword,
                                     encryptedPassword,
                                     connectorTypeGUID,
                                     connectorTypeGUIDParameterName,
                                     endpointGUID,
                                     endpointGUIDParameterName,
                                     methodName);
    }


    /**
     * Creates a new virtual connection and returns the unique identifier for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param assetGUID the unique identifier for the asset entity (null for stand alone connections)
     * @param assetGUIDParameterName the parameter supplying assetGUID
     * @param assetSummary brief description of the asset for the relationship between the asset and the connection
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the connection.
     * @param additionalProperties additional properties
     * @param securedProperties protected properties
     * @param configurationProperties  properties passed to configure underlying technologies
     * @param connectorUserId user identity that the connector should use
     * @param clearPassword password for the userId in clear text
     * @param encryptedPassword encrypted password that the connector needs to decrypt before use
     * @param connectorTypeGUID unique identifier of the connector type to used for this connection
     * @param connectorTypeGUIDParameterName the parameter supplying connectorTypeGUID
     * @param methodName calling method
     *
     * @return GUID for new connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connection properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createVirtualConnection(String              userId,
                                          String              externalSourceGUID,
                                          String              externalSourceName,
                                          String              assetGUID,
                                          String              assetGUIDParameterName,
                                          String              assetSummary,
                                          String              qualifiedName,
                                          String              displayName,
                                          String              description,
                                          Map<String, String> additionalProperties,
                                          Map<String, String> securedProperties,
                                          Map<String, Object> configurationProperties,
                                          String              connectorUserId,
                                          String              clearPassword,
                                          String              encryptedPassword,
                                          String              connectorTypeGUID,
                                          String              connectorTypeGUIDParameterName,
                                          String              methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        return this.createConnection(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     assetGUID,
                                     assetGUIDParameterName,
                                     assetSummary,
                                     OpenMetadataAPIMapper.VIRTUAL_CONNECTION_TYPE_GUID,
                                     OpenMetadataAPIMapper.VIRTUAL_CONNECTION_TYPE_NAME,
                                     qualifiedName,
                                     displayName,
                                     description,
                                     additionalProperties,
                                     securedProperties,
                                     configurationProperties,
                                     connectorUserId,
                                     clearPassword,
                                     encryptedPassword,
                                     connectorTypeGUID,
                                     connectorTypeGUIDParameterName,
                                     null,
                                     null,
                                     methodName);
    }



    /**
     * Creates a new connection and returns the unique identifier for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param assetGUID the unique identifier for the asset entity (null for stand alone connections)
     * @param assetGUIDParameterName the parameter supplying assetGUID
     * @param assetSummary brief description of the asset for the relationship between the asset and the connection
     * @param connectionTypeId type identifier for the connection
     * @param connectionTypeName type name for the connection
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the connection.
     * @param additionalProperties additional properties
     * @param securedProperties protected properties
     * @param configurationProperties  properties passed to configure underlying technologies
     * @param connectorUserId user identity that the connector should use
     * @param clearPassword password for the userId in clear text
     * @param encryptedPassword encrypted password that the connector needs to decrypt before use
     * @param connectorTypeGUID unique identifier of the connector type to used for this connection
     * @param connectorTypeGUIDParameterName the parameter supplying connectorTypeGUID
     * @param endpointGUID unique identifier of the endpoint to used for this connection
     * @param endpointGUIDParameterName the parameter supplying endpointGUID
     * @param methodName calling method
     *
     * @return GUID for new connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connection properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  createConnection(String              userId,
                                    String              externalSourceGUID,
                                    String              externalSourceName,
                                    String              assetGUID,
                                    String              assetGUIDParameterName,
                                    String              assetSummary,
                                    String              connectionTypeId,
                                    String              connectionTypeName,
                                    String              qualifiedName,
                                    String              displayName,
                                    String              description,
                                    Map<String, String> additionalProperties,
                                    Map<String, String> securedProperties,
                                    Map<String, Object> configurationProperties,
                                    String              connectorUserId,
                                    String              clearPassword,
                                    String              encryptedPassword,
                                    String              connectorTypeGUID,
                                    String              connectorTypeGUIDParameterName,
                                    String              endpointGUID,
                                    String              endpointGUIDParameterName,
                                    String              methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String nameParameter = "qualifiedName";
        final String connectionGUIDParameterName = "connectionGUID";

        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        ConnectionBuilder builder = new ConnectionBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          additionalProperties,
                                                          securedProperties,
                                                          configurationProperties,
                                                          connectorUserId,
                                                          clearPassword,
                                                          encryptedPassword,
                                                          connectionTypeId,
                                                          connectionTypeName,
                                                          null,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        if (assetGUID != null)
        {
            builder.setAnchors(userId, assetGUID, methodName);
        }

        String connectionGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            connectionTypeId,
                                                            connectionTypeName,
                                                            qualifiedName,
                                                            OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                            builder,
                                                            methodName);

        if (connectionGUID != null)
        {
            if (assetGUID != null)
            {
                InstanceProperties relationshipProperties = null;

                if (assetSummary != null)
                {
                    relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          null,
                                                                                          OpenMetadataAPIMapper.ASSET_SUMMARY_PROPERTY_NAME,
                                                                                          assetSummary,
                                                                                          methodName);
                }

                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          connectionGUID,
                                          connectionGUIDParameterName,
                                          connectionTypeName,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                          OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                          OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                          relationshipProperties,
                                          methodName);
            }

            if (connectorTypeGUID != null)
            {
                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          connectionGUID,
                                          connectionGUIDParameterName,
                                          connectionTypeName,
                                          connectorTypeGUID,
                                          connectorTypeGUIDParameterName,
                                          OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                          OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                          OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                          null,
                                          methodName);
            }

            if (endpointGUID != null)
            {
                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          endpointGUID,
                                          endpointGUIDParameterName,
                                          OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                          connectionGUID,
                                          connectionGUIDParameterName,
                                          connectionTypeName,
                                          OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                          OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                          null,
                                          methodName);
            }
        }

        return connectionGUID;
    }


    /**
     * Link a connection into a virtual connection using the EmbeddedConnection relationship.  The properties associated with the
     * EmbeddedConnection relationship are typically used where the connection represents connectors that are part of a
     * dynamic process (such as in an open discovery pipeline).
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param virtualConnectionGUID unique identifier for the virtual connection
     * @param virtualConnectionGUIDParameterName parameter supplying virtualConnectionGUID
     * @param position position in the virtual connection
     * @param displayName name of the embedded connection
     * @param arguments arguments to use with the embedded connector when created as part of the virtual connector
     * @param embeddedConnectionGUID unique identifier for the embedded connection
     * @param embeddedConnectionGUIDParameterName parameter supplying embeddedConnectionGUID
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connection properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addEmbeddedConnection(String              userId,
                                      String              externalSourceGUID,
                                      String              externalSourceName,
                                      String              virtualConnectionGUID,
                                      String              virtualConnectionGUIDParameterName,
                                      int                 position,
                                      String              displayName,
                                      Map<String, Object> arguments,
                                      String              embeddedConnectionGUID,
                                      String              embeddedConnectionGUIDParameterName,
                                      String              methodName)  throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(virtualConnectionGUID, virtualConnectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(embeddedConnectionGUID, embeddedConnectionGUIDParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  OpenMetadataAPIMapper.POSITION_PROPERTY_NAME,
                                                                                  position,
                                                                                  methodName);

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if ((arguments != null) && (! arguments.isEmpty()))
        {
            properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.ARGUMENTS_PROPERTY_NAME,
                                                                   arguments,
                                                                   methodName);
        }

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  virtualConnectionGUID,
                                  virtualConnectionGUIDParameterName,
                                  OpenMetadataAPIMapper.VIRTUAL_CONNECTION_TYPE_NAME,
                                  embeddedConnectionGUID,
                                  embeddedConnectionGUIDParameterName,
                                  OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                  OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_GUID,
                                  OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_NAME,
                                  properties,
                                  methodName);
    }



    /**
     * Updates the properties of an existing connection.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connectionGUID         unique identifier for the connection
     * @param connectionGUIDParameterName parameter providing connectionGUID
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the connection.
     * @param additionalProperties additional properties
     * @param securedProperties protected properties
     * @param configurationProperties  properties passed to configure underlying technologies
     * @param connectorUserId user identity that the connector should use
     * @param clearPassword password for the userId in clear text
     * @param encryptedPassword encrypted password that the connector needs to decrypt before use
     * @param typeGUID identifier of the type that is a subtype of connection - or null to create standard type
     * @param typeName name of the type that is a subtype of connection - or null to create standard type
     * @param extendedProperties additional properties for the subtype
     * @param methodName      calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connection properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateConnection(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              connectionGUID,
                                   String              connectionGUIDParameterName,
                                   String              qualifiedName,
                                   String              displayName,
                                   String              description,
                                   Map<String, String> additionalProperties,
                                   Map<String, String> securedProperties,
                                   Map<String, Object> configurationProperties,
                                   String              connectorUserId,
                                   String              clearPassword,
                                   String              encryptedPassword,
                                   String              typeGUID,
                                   String              typeName,
                                   Map<String, Object> extendedProperties,
                                   String              methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String nameParameter = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        ConnectionBuilder builder = new ConnectionBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          additionalProperties,
                                                          securedProperties,
                                                          configurationProperties,
                                                          connectorUserId,
                                                          clearPassword,
                                                          encryptedPassword,
                                                          typeGUID,
                                                          typeName,
                                                          extendedProperties,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    connectionGUID,
                                    connectionGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    true,
                                    methodName);
    }


    /**
     * Count the number of informal connections attached to a supplied asset.
     *
     * @param userId     calling user
     * @param assetGUID identifier for the asset
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countConnections(String userId,
                                String assetGUID,
                                String methodName) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        return this.countAttachments(userId,
                                     assetGUID,
                                     OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                     OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                     OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                     methodName);
    }


    /**
     * Retrieve the list of connection objects attached to the requested asset object.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset object
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param methodName calling method
     * @return Connection bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public B getConnectionForAsset(String       userId,
                                   String       assetGUID,
                                   String       assetGUIDParameterName,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        return this.getConnectionForAsset(userId, assetGUID, assetGUIDParameterName, supportedZones, methodName);
    }


    /**
     * Retrieve the list of connection objects attached to the requested asset object.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset object
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param serviceSupportedZones list of supported zones for the calling service
     * @param methodName calling method
     * @return Connection bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public B getConnectionForAsset(String       userId,
                                   String       assetGUID,
                                   String       assetGUIDParameterName,
                                   List<String> serviceSupportedZones,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        /*
         * Each returned entity has been verified as readable by the user before it is returned.
         */
        List<EntityDetail> connectionEntities = this.getAttachedEntities(userId,
                                                                         assetGUID,
                                                                         assetGUIDParameterName,
                                                                         OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                         OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                         OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                         OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                         null,
                                                                         null,
                                                                         false,
                                                                         serviceSupportedZones,
                                                                         0,
                                                                         invalidParameterHandler.getMaxPagingSize(),
                                                                         methodName);

        EntityDetail selectedEntity = null;
        if (connectionEntities == null)
        {
            return null;
        }
        else if (connectionEntities.size() == 1)
        {
            selectedEntity = connectionEntities.get(0);
        }
        else
        {
            /*
             * Multiple connections have been returned.  The code below asks the security verifier to choose which one should be
             * returned to the caller.
             */
            List<org.odpi.openmetadata.metadatasecurity.properties.Connection> candidateConnections = new ArrayList<>();

            for (EntityDetail connectionEntity : connectionEntities)
            {
                if (connectionEntity != null)
                {
                    org.odpi.openmetadata.metadatasecurity.properties.Connection candidateConnection = super.getConnectionFromEntity(connectionEntity,
                                                                                                                                     methodName);

                    candidateConnections.add(candidateConnection);
                }
            }

            org.odpi.openmetadata.metadatasecurity.properties.Connection connection = securityVerifier.validateUserForAssetConnectionList(userId, null, candidateConnections);

            if (connection != null)
            {
                for (EntityDetail connectionEntity : connectionEntities)
                {
                    if ((connectionEntity != null) && (connectionEntity.getGUID() != null))
                    {
                        if (connectionEntity.getGUID().equals(connection.getGUID()))
                        {
                            selectedEntity = connectionEntity;
                        }
                    }
                }
            }
            else
            {
                throw new PropertyServerException(GenericHandlersErrorCode.MULTIPLE_CONNECTIONS_FOUND.getMessageDefinition(Integer.toString(candidateConnections.size()),
                                                                                                                           assetGUID,
                                                                                                                           methodName,
                                                                                                                           serverName),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }

        return getConnection(userId, selectedEntity, methodName);
    }


    /**
     * Retrieve a connection object.  This may be a standard connection or a virtual connection.  The method includes the
     * connector type, endpoint and any embedded connections in the returned bean.  (This is an alternative to calling
     * the standard generic handler methods that would only retrieve the properties of the Connection entity.)
     *
     * @param userId calling user
     * @param connectionEntity entity for root connection object
     * @param methodName calling method
     * @return connection object
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private B getConnection(String       userId,
                            EntityDetail connectionEntity,
                            String       methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        if ((connectionEntity != null) && (connectionEntity.getType() != null))
        {
            /*
             * The relationships are retrieved first.  It is not possible to follow the same pattern as SchemaType where
             * embedded instances are retrieve as beans and then assembled in the final bean at the end because of the problem of
             * matching the properties in the EmbeddedConnection relationship with the Connection bean it links to.
             * So the entire graph of instances for the connection are retrieved and passed to the converter.
             */
            List<Relationship> supplementaryRelationships = this.getEmbeddedRelationships(userId, connectionEntity, methodName);
            List<EntityDetail> supplementaryEntities = new ArrayList<>();

            if (supplementaryRelationships != null)
            {
                for (Relationship relationship : supplementaryRelationships)
                {
                    if ((relationship != null) && (relationship.getType() != null))
                    {
                        EntityProxy entityProxy = null;

                        if ((repositoryHelper.isTypeOf(serviceName,
                                                       relationship.getType().getTypeDefName(),
                                                       OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME))
                                || (repositoryHelper.isTypeOf(serviceName,
                                                              relationship.getType().getTypeDefName(),
                                                              OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_NAME)))
                        {
                            entityProxy = relationship.getEntityTwoProxy();
                        }
                        else if (repositoryHelper.isTypeOf(serviceName,
                                                           relationship.getType().getTypeDefName(),
                                                           OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME))
                        {
                            entityProxy = relationship.getEntityOneProxy();
                        }
                        if ((entityProxy != null) && (entityProxy.getGUID() != null) && (entityProxy.getType() != null))
                        {
                            final String entityGUIDParameterName = "embeddedRelationship proxy";
                            EntityDetail supplementaryEntity = this.getEntityFromRepository(userId,
                                                                                            entityProxy.getGUID(),
                                                                                            entityGUIDParameterName,
                                                                                            entityProxy.getType().getTypeDefName(),
                                                                                            methodName);
                            if (supplementaryEntity != null)
                            {
                                supplementaryEntities.add(supplementaryEntity);
                            }
                        }
                    }
                }
            }

            if (supplementaryEntities.isEmpty())
            {
                supplementaryEntities = null;
            }

            return converter.getNewComplexBean(beanClass, connectionEntity, supplementaryEntities, supplementaryRelationships, methodName);
        }

        return null;
    }


    /**
     * Recursively retrieve the list of relationships within a connection object.  The recursion occurs in VirtualConnections
     * that embed connections within themselves.
     *
     * @param userId calling user
     * @param connectionEntity entity for root connection object
     * @param methodName calling method
     * @return list of relationships
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private List<Relationship> getEmbeddedRelationships(String        userId,
                                                        EntitySummary connectionEntity,
                                                        String        methodName) throws PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        List<Relationship> supplementaryRelationships = new ArrayList<>();

        if ((connectionEntity != null) && (connectionEntity.getType() != null))
        {
            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           userId,
                                                                                           connectionEntity.getGUID(),
                                                                                           connectionEntity.getType().getTypeDefName(),
                                                                                           null,
                                                                                           null,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           methodName);


            while (iterator.moreToReceive())
            {
                Relationship relationship = iterator.getNext();

                if ((relationship != null) && (relationship.getType() != null))
                {
                    if ((repositoryHelper.isTypeOf(serviceName,
                                                   relationship.getType().getTypeDefName(),
                                                   OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME))
                        || (repositoryHelper.isTypeOf(serviceName,
                                                      relationship.getType().getTypeDefName(),
                                                     OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME))
                            || (repositoryHelper.isTypeOf(serviceName,
                                                          relationship.getType().getTypeDefName(),
                                                          OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME)))
                    {
                        supplementaryRelationships.add(relationship);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName,
                                                       relationship.getType().getTypeDefName(),
                                                       OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_NAME))
                    {
                        supplementaryRelationships.add(relationship);

                        EntityProxy embeddedConnectionEnd = relationship.getEntityTwoProxy();
                        if ((embeddedConnectionEnd != null) && (embeddedConnectionEnd.getGUID() != null))
                        {
                            List<Relationship> embeddedConnectionRelationships = this.getEmbeddedRelationships(userId,
                                                                                                               embeddedConnectionEnd,
                                                                                                               methodName);

                            if (embeddedConnectionRelationships != null)
                            {
                                supplementaryRelationships.addAll(embeddedConnectionRelationships);
                            }
                        }
                    }
                }
            }
        }

        if (supplementaryRelationships.isEmpty())
        {
            return null;
        }

        return supplementaryRelationships;
    }
    

    /**
     * Retrieve the list of connection objects attached to the requested asset object.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset object
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param serviceSupportedZones list of supported zones for the calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of Connection bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public List<B> getConnectionsForAsset(String       userId,
                                          String       assetGUID,
                                          String       assetGUIDParameterName,
                                          List<String> serviceSupportedZones,
                                          int          startingFrom,
                                          int          pageSize,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        /*
         * The generic handler calls to the security verifier as each connector is retrieved.
         */
        List<EntityDetail> connectionEntities = super.getAttachedEntities(userId,
                                                                          assetGUID,
                                                                          assetGUIDParameterName,
                                                                          OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                          null,
                                                                          null,
                                                                          false,
                                                                          serviceSupportedZones,
                                                                          startingFrom,
                                                                          pageSize,
                                                                          methodName);
        
        if ((connectionEntities != null) && (! connectionEntities.isEmpty()))
        {
            List<B> results = new ArrayList<>();
            
            for (EntityDetail entity : connectionEntities)
            {
                if (entity != null)
                {
                    B bean = this.getConnection(userId, entity, methodName);
                    
                    if (bean != null)
                    {
                        results.add(bean);
                    }
                }
            }
            
            if (! results.isEmpty())
            {
                return results;
            }
        }
        
        return null;
    }
}