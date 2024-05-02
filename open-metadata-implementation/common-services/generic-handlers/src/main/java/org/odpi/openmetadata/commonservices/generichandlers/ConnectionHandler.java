/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ConnectionHandler manages Connection objects.  These describe the network addresses where services are running.  They are used by connection
 * objects to describe the service that the connector should call.  They are linked to servers to show their network address where the services that
 * they are hosting are running.
 * <br>
 * Most OMASs that work with Connection objects use the Open Connector Framework (OCF) Bean since this can be passed to the OCF Connector
 * Broker to create an instance of a connector to the attached asset.  Therefore, this handler has a default bean and converter which
 * is the one that works with the OCF bean.  The call can either use these values or override with their own bean/converter implementations.
 * <br>
 * ConnectionHandler runs server-side in the OMAG Server Platform and retrieves Connection entities through the OMRSRepositoryConnector via the
 * generic handler and repository handler.
 */
public class ConnectionHandler<B> extends ReferenceableHandler<B>
{
    private final EndpointHandler<OpenMetadataAPIDummyBean>       endpointHandler;
    private final ConnectorTypeHandler<OpenMetadataAPIDummyBean>  connectorTypeHandler;

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
     * @param displayName human-readable name
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return unique identifier of the connection or null
     *
     * @throws InvalidParameterException the connection bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String findConnection(String  userId,
                                  String  connectionGUID,
                                  String  qualifiedName,
                                  String  displayName,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
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
             * Otherwise, it is ignored.
             */
            try
            {
                if (this.getEntityFromRepository(userId,
                                                 connectionGUID,
                                                 guidParameterName,
                                                 OpenMetadataType.CONNECTION_TYPE_NAME,
                                                 null,
                                                 null,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 supportedZones,
                                                 effectiveTime,
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
                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                         OpenMetadataType.CONNECTION_TYPE_GUID,
                                                         OpenMetadataType.CONNECTION_TYPE_NAME,
                                                         false,
                                                         false,
                                                         supportedZones,
                                                         effectiveTime,
                                                         methodName);
        }

        if ((retrievedGUID == null) && (displayName != null))
        {
            retrievedGUID = this.getBeanGUIDByUniqueName(userId,
                                                         displayName,
                                                         displayNameParameter,
                                                         OpenMetadataProperty.DISPLAY_NAME.name,
                                                         OpenMetadataType.CONNECTION_TYPE_GUID,
                                                         OpenMetadataType.CONNECTION_TYPE_NAME,
                                                         false,
                                                         false,
                                                         supportedZones,
                                                         effectiveTime,
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
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID unique identifier of the anchor to add to the connection
     * @param assetGUID unique identifier of linked asset (or null)
     * @param assetGUIDParameterName  parameter name supplying assetGUID
     * @param assetTypeName type of asset
     * @param parentQualifiedName qualified name of associated asset/connection
     * @param connection object to add
     * @param assetSummary description of the asset for the connection
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for this service
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the connection in the repository.
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  saveConnection(String       userId,
                                  String       externalSourceGUID,
                                  String       externalSourceName,
                                  String       anchorGUID,
                                  String       assetGUID,
                                  String       assetGUIDParameterName,
                                  String       assetTypeName,
                                  String       parentQualifiedName,
                                  Connection   connection,
                                  String       assetSummary,
                                  boolean      forLineage,
                                  boolean      forDuplicateProcessing,
                                  List<String> serviceSupportedZones,
                                  Date         effectiveTime,
                                  String       methodName) throws InvalidParameterException,
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
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            effectiveTime,
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
                                 parentQualifiedName,
                                 connection,
                                 assetSummary,
                                 forLineage,
                                 forDuplicateProcessing,
                                 serviceSupportedZones,
                                 effectiveTime,
                                 methodName);
        }
        else
        {
            return updateConnection(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    anchorGUID,
                                    existingConnectionGUID,
                                    connection,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    effectiveTime,
                                    methodName);
        }
    }


    /**
     * Save the connectorType, endpoint and any embedded connections.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID unique identifier of the anchor entity if applicable
     * @param connectionGUID unique identifier of connected connection
     * @param connectionQualifiedName connection qualified name
     * @param endpoint endpoint object or null
     * @param connectorType connector type object or null
     * @param embeddedConnections list of embedded connections or null - only for Virtual Connections
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for this service
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                                  String                   connectionGUID,
                                                  String                   connectionQualifiedName,
                                                  Endpoint                 endpoint,
                                                  ConnectorType            connectorType,
                                                  List<EmbeddedConnection> embeddedConnections,
                                                  boolean                  forLineage,
                                                  boolean                  forDuplicateProcessing,
                                                  List<String>             serviceSupportedZones,
                                                  Date                     effectiveTime,
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
            String endpointGUID = endpointHandler.saveEndpoint(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               connectionQualifiedName,
                                                               endpoint,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);

            if (endpointGUID != null)
            {
                /*
                 * Create a new relationship unless it already exists
                 */
                repositoryHandler.ensureRelationship(userId,
                                                     OpenMetadataType.ENDPOINT_TYPE_NAME,
                                                     null,
                                                     null,
                                                     endpointGUID,
                                                     connectionGUID,
                                                     OpenMetadataType.CONNECTION_ENDPOINT_TYPE_GUID,
                                                     OpenMetadataType.CONNECTION_ENDPOINT_TYPE_NAME,
                                                     null,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
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
                                                           OpenMetadataType.CONNECTION_TYPE_NAME,
                                                           OpenMetadataType.CONNECTION_ENDPOINT_TYPE_GUID,
                                                           OpenMetadataType.CONNECTION_ENDPOINT_TYPE_NAME,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);
        }

        if (connectorType != null)
        {
            String connectorTypeGUID = connectorTypeHandler.saveConnectorType(userId,
                                                                              externalSourceGUID,
                                                                              externalSourceName,
                                                                              connectionQualifiedName,
                                                                              connectorType,
                                                                              methodName);

            if (connectorTypeGUID != null)
            {
                repositoryHandler.ensureRelationship(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     OpenMetadataType.CONNECTION_TYPE_NAME,
                                                     connectionGUID,
                                                     connectorTypeGUID,
                                                     OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                     OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                                     null,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
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
                                                           OpenMetadataType.CONNECTION_TYPE_NAME,
                                                           OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                                           OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
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
                               OpenMetadataType.CONNECTION_TYPE_NAME,
                               forLineage,
                               forDuplicateProcessing,
                               serviceSupportedZones,
                               OpenMetadataType.EMBEDDED_CONNECTION_TYPE_GUID,
                               OpenMetadataType.EMBEDDED_CONNECTION_TYPE_NAME,
                               effectiveTime,
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
                                                                    anchorGUID,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    connectionQualifiedName,
                                                                    realConnection,
                                                                    null,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    serviceSupportedZones,
                                                                    effectiveTime,
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
                                                             OpenMetadataType.EMBEDDED_CONNECTION_TYPE_GUID,
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
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID unique identifier ofr the anchor GUID if applicable
     * @param assetGUID unique identifier of linked asset (or null)
     * @param assetGUIDParameterName  parameter name supplying assetGUID
     * @param assetTypeName type of asset
     * @param parentQualifiedName qualified name of parent asset/connection
     * @param connection object to add
     * @param assetSummary description of the asset for the connection
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the connection in the repository.
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String  addConnection(String       userId,
                                  String       externalSourceGUID,
                                  String       externalSourceName,
                                  String       anchorGUID,
                                  String       assetGUID,
                                  String       assetGUIDParameterName,
                                  String       assetTypeName,
                                  String       parentQualifiedName,
                                  Connection   connection,
                                  String       assetSummary,
                                  boolean      forLineage,
                                  boolean      forDuplicateProcessing,
                                  List<String> serviceSupportedZones,
                                  Date         effectiveTime,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String  connectionParameterName     = "connection";
        final String  anchorGUIDParameterName     = "anchorGUID";

        invalidParameterHandler.validateObject(connection.getConnectorType(), connectionParameterName, methodName);

        String                   connectionTypeGUID  = OpenMetadataType.CONNECTION_TYPE_GUID;
        String                   connectionTypeName  = OpenMetadataType.CONNECTION_TYPE_NAME;
        List<EmbeddedConnection> embeddedConnections = null;

        if (connection instanceof VirtualConnection virtualConnection)
        {
            connectionTypeGUID = OpenMetadataType.VIRTUAL_CONNECTION_TYPE_GUID;
            connectionTypeName = OpenMetadataType.VIRTUAL_CONNECTION_TYPE_NAME;

            embeddedConnections = virtualConnection.getEmbeddedConnections();
        }

        String anchorTypeName = null;

        if (anchorGUID != null)
        {
            EntityDetail anchorEntity = this.getEntityFromRepository(userId,
                                                                     anchorGUID,
                                                                     anchorGUIDParameterName,
                                                                     OpenMetadataType.REFERENCEABLE.typeName,
                                                                     null,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

            if (anchorEntity != null)
            {
                anchorTypeName = anchorEntity.getType().getTypeDefName();
            }
        }

        String connectionQualifiedName = connection.getQualifiedName();

        if ((connectionQualifiedName == null) && (parentQualifiedName != null))
        {
            connectionQualifiedName = parentQualifiedName + "-" +connectionTypeName;
        }

        ConnectionBuilder connectionBuilder = new ConnectionBuilder(connectionQualifiedName,
                                                                    connection.getDisplayName(),
                                                                    connection.getDescription(),
                                                                    connection.getAdditionalProperties(),
                                                                    connection.getSecuredProperties(),
                                                                    connection.getConfigurationProperties(),
                                                                    connection.getUserId(),
                                                                    connection.getClearPassword(),
                                                                    connection.getEncryptedPassword(),
                                                                    OpenMetadataType.CONNECTION_TYPE_GUID,
                                                                    OpenMetadataType.CONNECTION_TYPE_NAME,
                                                                    connection.getExtendedProperties(),
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        if (anchorGUID != null)
        {
            connectionBuilder.setAnchors(userId, anchorGUID, anchorTypeName, methodName);
        }

        String connectionGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            connectionTypeGUID,
                                                            connectionTypeName,
                                                            connectionBuilder,
                                                            effectiveTime,
                                                            methodName);

        if (connectionGUID != null)
        {
            this.saveAssociatedConnectionEntities(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  anchorGUID,
                                                  connectionGUID,
                                                  connectionQualifiedName,
                                                  connection.getEndpoint(),
                                                  connection.getConnectorType(),
                                                  embeddedConnections,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  serviceSupportedZones,
                                                  effectiveTime,
                                                  methodName);

            if (assetGUID != null)
            {
                InstanceProperties properties = null;

                if (assetSummary != null)
                {
                    properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                              null,
                                                                              OpenMetadataType.ASSET_SUMMARY_PROPERTY_NAME,
                                                                              assetSummary,
                                                                              methodName);
                }

                this.linkElementToElement(userId,
                                          null,
                                          null,
                                          connectionGUID,
                                          connectionParameterName,
                                          OpenMetadataType.CONNECTION_TYPE_NAME,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          assetTypeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          serviceSupportedZones,
                                          OpenMetadataType.CONNECTION_TO_ASSET_TYPE_GUID,
                                          OpenMetadataType.CONNECTION_TO_ASSET_TYPE_NAME,
                                          properties,
                                          null,
                                          null,
                                          effectiveTime,
                                          methodName);
            }
        }

        return connectionGUID;
    }


    /**
     * Update a stored connection.
     *
     * @param userId userId
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID unique identifier ofr the anchor GUID if applicable
     * @param existingConnectionGUID unique identifier of the existing connection entity
     * @param connection new connection values
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for this service
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the connection in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String updateConnection(String       userId,
                                    String       externalSourceGUID,
                                    String       externalSourceName,
                                    String       anchorGUID,
                                    String       existingConnectionGUID,
                                    Connection   connection,
                                    boolean      forLineage,
                                    boolean      forDuplicateProcessing,
                                    List<String> serviceSupportedZones,
                                    Date         effectiveTime,
                                    String       methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String  parameterName = "connection";

        invalidParameterHandler.validateObject(connection.getConnectorType(), parameterName, methodName);

        String                   connectionTypeName  = OpenMetadataType.CONNECTION_TYPE_NAME;
        List<EmbeddedConnection> embeddedConnections = null;

        if (connection instanceof VirtualConnection virtualConnection)
        {
            connectionTypeName = OpenMetadataType.VIRTUAL_CONNECTION_TYPE_NAME;

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
                              connectionTypeName,
                              connection.getExtendedProperties(),
                              false,
                              null,
                              null,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);


        this.saveAssociatedConnectionEntities(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              anchorGUID,
                                              existingConnectionGUID,
                                              connection.getQualifiedName(),
                                              connection.getEndpoint(),
                                              connection.getConnectorType(),
                                              embeddedConnections,
                                              forLineage,
                                              forDuplicateProcessing,
                                              serviceSupportedZones,
                                              effectiveTime,
                                              methodName);

        return existingConnectionGUID;
    }



    /*
     * =========================================================
     * This next set of beans work with any bean implementation of a connection
     */


    /**
     * If possible, create a connection for the supplied asset.  The new connection is linked to the asset.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param assetGUID         unique identifier of the asset
     * @param assetGUIDParameterName parameter name supplying the asset guid
     * @param assetTypeName type of asset being created
     * @param assetQualifiedName fully qualified path name of the asset
     * @param anchorEndpointToAsset set to true if the network address is unique for the asset and should not be reused. False if this is an endpoint
     *                              that is relevant for multiple assets.
     * @param configurationProperties configuration properties for the connection
     * @param connectorProviderClassName Java class name for the connector provider
     * @param networkAddress the network address (typically the URL but this depends on the protocol)
     * @param protocol the name of the protocol to use to connect to the endpoint
     * @param encryptionMethod encryption method to use when passing data to this endpoint
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addAssetConnection(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              assetGUID,
                                   String              assetGUIDParameterName,
                                   String              assetTypeName,
                                   String              assetQualifiedName,
                                   boolean             anchorEndpointToAsset,
                                   Map<String, Object> configurationProperties,
                                   String              connectorProviderClassName,
                                   String              networkAddress,
                                   String              protocol,
                                   String              encryptionMethod,
                                   Date                effectiveFrom,
                                   Date                effectiveTo,
                                   boolean             forLineage,
                                   boolean             forDuplicateProcessing,
                                   List<String>        serviceSupportedZones,
                                   Date                effectiveTime,
                                   String              methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";

        String connectorTypeGUID;

        if (connectorProviderClassName != null)
        {
            /*
             * The caller has provided the connector provider class name so that is used. If an existing connector type is found, it is
             * used, otherwise a new connector type is created.  Notice it is not anchored to the asset.
             */
            String connectorTypeName = assetTypeName + ":" + assetQualifiedName + " ConnectorType";

            connectorTypeGUID = connectorTypeHandler.getConnectorTypeForConnection(userId,
                                                                                   null,
                                                                                   null,
                                                                                   assetGUID,
                                                                                   connectorTypeName,
                                                                                   connectorTypeName,
                                                                                   null,
                                                                                   assetTypeName,
                                                                                   null,
                                                                                   connectorProviderClassName,
                                                                                   OpenMetadataType.CONNECTOR_FRAMEWORK_NAME_DEFAULT,
                                                                                   OpenMetadataType.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   effectiveTime,
                                                                                   methodName);
        }
        else
        {
            /*
             * No explicit connector provider class name is provided so look up if there is a standard connector type for this asset type.
             */
            connectorTypeGUID = connectorTypeHandler.getConnectorTypeForAsset(userId, assetTypeName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        if (connectorTypeGUID != null)
        {
            final String endpointGUIDParameterName = "endpointGUID";
            final String endpointDescription       = "Access information to connect to the actual asset: ";

            /*
             * A connector type is present, so it is possible to create a connection.
             */
            String endpointName   = assetQualifiedName + " Endpoint";
            String connectionName = assetQualifiedName + " Connection";

            String anchorGUID = null;

            /*
             * The endpoint is anchored to the asset if the caller requests.
             */
            if (anchorEndpointToAsset)
            {
                anchorGUID = assetGUID;
            }

            String endpointGUID = endpointHandler.getEndpointForConnection(userId,
                                                                           externalSourceGUID,
                                                                           externalSourceName,
                                                                           anchorGUID,
                                                                           endpointName,
                                                                           endpointName,
                                                                           endpointDescription + networkAddress,
                                                                           networkAddress,
                                                                           protocol,
                                                                           encryptionMethod,
                                                                           null,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           effectiveTime,
                                                                           methodName);
            this.createConnection(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  assetGUID,
                                  assetGUIDParameterName,
                                  null,
                                  connectionName,
                                  connectionName,
                                  null,
                                  null,
                                  null,
                                  configurationProperties,
                                  null,
                                  null,
                                  null,
                                  OpenMetadataType.CONNECTION_TYPE_NAME,
                                  null,
                                  connectorTypeGUID,
                                  connectorTypeGUIDParameterName,
                                  endpointGUID,
                                  endpointGUIDParameterName,
                                  effectiveFrom,
                                  effectiveTo,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  effectiveTime,
                                  methodName);
        }
    }



    /**
     * Creates a new connection, connects it to the asset and returns the unique identifier for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param assetGUID the unique identifier for the asset entity (null for standalone connections)
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
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for
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
                                   Date                effectiveFrom,
                                   Date                effectiveTo,
                                   boolean             forLineage,
                                   boolean             forDuplicateProcessing,
                                   List<String>        serviceSupportedZones,
                                   Date                effectiveTime,
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
                                     qualifiedName,
                                     displayName,
                                     description,
                                     additionalProperties,
                                     securedProperties,
                                     configurationProperties,
                                     connectorUserId,
                                     clearPassword,
                                     encryptedPassword,
                                     OpenMetadataType.CONNECTION_TYPE_NAME,
                                     null,
                                     connectorTypeGUID,
                                     connectorTypeGUIDParameterName,
                                     endpointGUID,
                                     endpointGUIDParameterName,
                                     effectiveFrom,
                                     effectiveTo,
                                     forLineage,
                                     forDuplicateProcessing,
                                     serviceSupportedZones,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Creates a new connection and returns the unique identifier for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param assetGUID the unique identifier for the asset entity (null for standalone connections)
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
     * @param connectionTypeName type name for the connection
     * @param extendedProperties parameters from a subtype
     * @param connectorTypeGUID unique identifier of the connector type to used for this connection
     * @param connectorTypeGUIDParameterName the parameter supplying connectorTypeGUID
     * @param endpointGUID unique identifier of the endpoint to used for this connection
     * @param endpointGUIDParameterName the parameter supplying endpointGUID
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for
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
                                    String              qualifiedName,
                                    String              displayName,
                                    String              description,
                                    Map<String, String> additionalProperties,
                                    Map<String, String> securedProperties,
                                    Map<String, Object> configurationProperties,
                                    String              connectorUserId,
                                    String              clearPassword,
                                    String              encryptedPassword,
                                    String              connectionTypeName,
                                    Map<String, Object> extendedProperties,
                                    String              connectorTypeGUID,
                                    String              connectorTypeGUIDParameterName,
                                    String              endpointGUID,
                                    String              endpointGUIDParameterName,
                                    Date                effectiveFrom,
                                    Date                effectiveTo,
                                    boolean             forLineage,
                                    boolean             forDuplicateProcessing,
                                    List<String>        serviceSupportedZones,
                                    Date                effectiveTime,
                                    String              methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String nameParameter = "qualifiedName";
        final String connectionGUIDParameterName = "connectionGUID";

        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        String connectionTypeId = invalidParameterHandler.validateTypeName(connectionTypeName,
                                                                           OpenMetadataType.CONNECTION_TYPE_NAME,
                                                                           serviceName,
                                                                           methodName,
                                                                           repositoryHelper);

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
                                                          extendedProperties,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        if (assetGUID != null)
        {
            EntityDetail  assetEntity = repositoryHandler.getEntityByGUID(userId,
                                                                          assetGUID,
                                                                          assetGUIDParameterName,
                                                                          OpenMetadataType.ASSET.typeName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);
            if (assetEntity != null)
            {
                builder.setAnchors(userId, assetGUID, assetEntity.getType().getTypeDefName(), methodName);
            }
        }

        String connectionGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            connectionTypeId,
                                                            connectionTypeName,
                                                            builder,
                                                            effectiveTime,
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
                                                                                          OpenMetadataType.ASSET_SUMMARY_PROPERTY_NAME,
                                                                                          assetSummary,
                                                                                          methodName);
                }

                this.uncheckedLinkElementToElement(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   connectionGUID,
                                                   connectionGUIDParameterName,
                                                   connectionTypeName,
                                                   assetGUID,
                                                   assetGUIDParameterName,
                                                   OpenMetadataType.ASSET.typeName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   serviceSupportedZones,
                                                   OpenMetadataType.ASSET_TO_CONNECTION_TYPE_GUID,
                                                   OpenMetadataType.ASSET_TO_CONNECTION_TYPE_NAME,
                                                   this.setUpEffectiveDates(relationshipProperties, effectiveFrom,effectiveTo),
                                                   effectiveTime,
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
                                          OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          serviceSupportedZones,
                                          OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                          OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                          null,
                                          effectiveFrom,
                                          effectiveTo,
                                          effectiveTime,
                                          methodName);
            }

            if (endpointGUID != null)
            {
                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          endpointGUID,
                                          endpointGUIDParameterName,
                                          OpenMetadataType.ENDPOINT_TYPE_NAME,
                                          connectionGUID,
                                          connectionGUIDParameterName,
                                          connectionTypeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          serviceSupportedZones,
                                          OpenMetadataType.CONNECTION_ENDPOINT_TYPE_GUID,
                                          OpenMetadataType.CONNECTION_ENDPOINT_TYPE_NAME,
                                          null,
                                          effectiveFrom,
                                          effectiveTo,
                                          effectiveTime,
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
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param virtualConnectionGUID unique identifier for the virtual connection
     * @param virtualConnectionGUIDParameterName parameter supplying virtualConnectionGUID
     * @param position position in the virtual connection
     * @param displayName name of the embedded connection
     * @param arguments arguments to use with the embedded connector when created as part of the virtual connector
     * @param embeddedConnectionGUID unique identifier for the embedded connection
     * @param embeddedConnectionGUIDParameterName parameter supplying embeddedConnectionGUID
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for
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
                                      Date                effectiveFrom,
                                      Date                effectiveTo,
                                      boolean             forLineage,
                                      boolean             forDuplicateProcessing,
                                      List<String>        serviceSupportedZones,
                                      Date                effectiveTime,
                                      String              methodName)  throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(virtualConnectionGUID, virtualConnectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(embeddedConnectionGUID, embeddedConnectionGUIDParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  OpenMetadataType.POSITION_PROPERTY_NAME,
                                                                                  position,
                                                                                  methodName);

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                                      displayName,
                                                                      methodName);
        }

        if ((arguments != null) && (! arguments.isEmpty()))
        {
            properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataType.ARGUMENTS_PROPERTY_NAME,
                                                                   arguments,
                                                                   methodName);
        }

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  virtualConnectionGUID,
                                  virtualConnectionGUIDParameterName,
                                  OpenMetadataType.VIRTUAL_CONNECTION_TYPE_NAME,
                                  embeddedConnectionGUID,
                                  embeddedConnectionGUIDParameterName,
                                  OpenMetadataType.CONNECTION_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  OpenMetadataType.EMBEDDED_CONNECTION_TYPE_GUID,
                                  OpenMetadataType.EMBEDDED_CONNECTION_TYPE_NAME,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param connectionGUIDParameterName parameter for connectionGUID
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     * @param embeddedConnectionGUIDParameterName parameter for embeddedConnectionGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEmbeddedConnection(String       userId,
                                         String       externalSourceGUID,
                                         String       externalSourceName,
                                         String       connectionGUID,
                                         String       connectionGUIDParameterName,
                                         String       embeddedConnectionGUID,
                                         String       embeddedConnectionGUIDParameterName,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         List<String> serviceSupportedZones,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      connectionGUID,
                                      connectionGUIDParameterName,
                                      OpenMetadataType.VIRTUAL_CONNECTION_TYPE_NAME,
                                      embeddedConnectionGUID,
                                      embeddedConnectionGUIDParameterName,
                                      OpenMetadataType.CONNECTION_TYPE_GUID,
                                      OpenMetadataType.CONNECTION_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      serviceSupportedZones,
                                      OpenMetadataType.EMBEDDED_CONNECTION_TYPE_GUID,
                                      OpenMetadataType.EMBEDDED_CONNECTION_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateGUIDParameterName parameter name for templateGUID
     * @param qualifiedName unique name for the element - used in other configuration
     * @param qualifiedNameParameterName parameter name for qualifiedName
     * @param displayName short display name for the new element
     * @param description description of the new element
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectionFromTemplate(String userId,
                                               String externalSourceGUID,
                                               String externalSourceName,
                                               String templateGUID,
                                               String templateGUIDParameterName,
                                               String qualifiedName,
                                               String qualifiedNameParameterName,
                                               String displayName,
                                               String description,
                                               String methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        ConnectionBuilder builder = new ConnectionBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataType.CONNECTION_TYPE_GUID,
                                           OpenMetadataType.CONNECTION_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                           builder,
                                           supportedZones,
                                           true,
                                           false,
                                           null,
                                           methodName);
    }


    /**
     * Updates the properties of an existing connection.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
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
     * @param typeName name of the type that is a subtype of connection - or null to create standard type
     * @param extendedProperties additional properties for the subtype
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
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
                                   String              typeName,
                                   Map<String, Object> extendedProperties,
                                   boolean             isMergeUpdate,
                                   Date                effectiveFrom,
                                   Date                effectiveTo,
                                   boolean             forLineage,
                                   boolean             forDuplicateProcessing,
                                   Date                effectiveTime,
                                   String              methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String nameParameter = "qualifiedName";

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.CONNECTION_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

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

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    connectionGUID,
                                    connectionGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param connectionGUID unique identifier of the connection
     * @param connectionGUIDParameterName parameter for connectionGUID
     * @param connectorTypeGUID unique identifier of the connector type
     * @param connectorTypeGUIDParameterName parameter for connectorTypeGUID
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addConnectionConnectorType(String  userId,
                                           String  externalSourceGUID,
                                           String  externalSourceName,
                                           String  connectionGUID,
                                           String  connectionGUIDParameterName,
                                           String  connectorTypeGUID,
                                           String  connectorTypeGUIDParameterName,
                                           Date    effectiveFrom,
                                           Date    effectiveTo,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  connectionGUID,
                                  connectionGUIDParameterName,
                                  OpenMetadataType.CONNECTION_TYPE_NAME,
                                  connectorTypeGUID,
                                  connectorTypeGUIDParameterName,
                                  OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                  OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove the relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param connectionGUID unique identifier of the connection
     * @param connectionGUIDParameterName parameter for connectionGUID
     * @param connectorTypeGUID unique identifier of the connector type
     * @param connectorTypeGUIDParameterName parameter for connectorTypeGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnectionConnectorType(String  userId,
                                              String  externalSourceGUID,
                                              String  externalSourceName,
                                              String  connectionGUID,
                                              String  connectionGUIDParameterName,
                                              String  connectorTypeGUID,
                                              String  connectorTypeGUIDParameterName,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      connectionGUID,
                                      connectionGUIDParameterName,
                                      OpenMetadataType.CONNECTION_TYPE_NAME,
                                      connectorTypeGUID,
                                      connectorTypeGUIDParameterName,
                                      OpenMetadataType.CONNECTOR_TYPE_TYPE_GUID,
                                      OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                      OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param connectionGUID unique identifier of the connection
     * @param connectionGUIDParameterName parameter for connectionGUID
     * @param endpointGUID unique identifier of the endpoint
     * @param endpointGUIDParameterName parameter for endpointGUID
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addConnectionEndpoint(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  connectionGUID,
                                      String  connectionGUIDParameterName,
                                      String  endpointGUID,
                                      String  endpointGUIDParameterName,
                                      Date    effectiveFrom,
                                      Date    effectiveTo,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  endpointGUID,
                                  endpointGUIDParameterName,
                                  OpenMetadataType.ENDPOINT_TYPE_NAME,
                                  connectionGUID,
                                  connectionGUIDParameterName,
                                  OpenMetadataType.CONNECTION_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.CONNECTION_ENDPOINT_TYPE_GUID,
                                  OpenMetadataType.CONNECTION_ENDPOINT_TYPE_NAME,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove the relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectionGUIDParameterName parameter for connectionGUID
     * @param endpointGUID unique identifier of the connector type in the external data manager
     * @param endpointGUIDParameterName parameter for endpointGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnectionEndpoint(String  userId,
                                         String  externalSourceGUID,
                                         String  externalSourceName,
                                         String  connectionGUID,
                                         String  connectionGUIDParameterName,
                                         String  endpointGUID,
                                         String  endpointGUIDParameterName,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      endpointGUID,
                                      endpointGUIDParameterName,
                                      OpenMetadataType.ENDPOINT_TYPE_NAME,
                                      connectionGUID,
                                      connectionGUIDParameterName,
                                      OpenMetadataType.CONNECTION_TYPE_GUID,
                                      OpenMetadataType.CONNECTION_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      OpenMetadataType.CONNECTION_ENDPOINT_TYPE_GUID,
                                      OpenMetadataType.CONNECTION_ENDPOINT_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter for assetGUID

     * @param assetSummary summary of the asset that is stored in the relationship between the asset and the connection.
     * @param connectionGUID unique identifier of the  connection
     * @param connectionGUIDParameterName parameter for connectionGUID
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addConnectionToAsset(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  connectionGUID,
                                     String  connectionGUIDParameterName,
                                     String  assetGUID,
                                     String  assetGUIDParameterName,
                                     String  assetSummary,
                                     Date    effectiveFrom,
                                     Date    effectiveTo,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataType.ASSET_SUMMARY_PROPERTY_NAME,
                                                                                     assetSummary,
                                                                                     methodName);
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  connectionGUID,
                                  connectionGUIDParameterName,
                                  OpenMetadataType.CONNECTION_TYPE_NAME,
                                  assetGUID,
                                  assetGUIDParameterName,
                                  OpenMetadataType.ASSET.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.CONNECTION_TO_ASSET_TYPE_GUID,
                                  OpenMetadataType.CONNECTION_TO_ASSET_TYPE_NAME,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);

        EntityDetail  assetEntity = repositoryHandler.getEntityByGUID(userId,
                                                                      assetGUID,
                                                                      assetGUIDParameterName,
                                                                      OpenMetadataType.ASSET.typeName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);

        if (assetEntity != null)
        {
            this.addAnchorsClassification(userId,
                                          connectionGUID,
                                          connectionGUIDParameterName,
                                          OpenMetadataType.CONNECTION_TYPE_NAME,
                                          assetGUID,
                                          assetEntity.getType().getTypeDefName(),
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
        }
    }


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter for assetGUID
     * @param connectionGUID unique identifier of the connection
     * @param connectionGUIDParameterName parameter for connectionGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnectionToAsset(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  connectionGUID,
                                        String  connectionGUIDParameterName,
                                        String  assetGUID,
                                        String  assetGUIDParameterName,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      connectionGUID,
                                      connectionGUIDParameterName,
                                      OpenMetadataType.CONNECTION_TYPE_NAME,
                                      assetGUID,
                                      assetGUIDParameterName,
                                      OpenMetadataType.ASSET.typeGUID,
                                      OpenMetadataType.ASSET.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      OpenMetadataType.CONNECTION_TO_ASSET_TYPE_GUID,
                                      OpenMetadataType.CONNECTION_TO_ASSET_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Remove the metadata element.  This will delete all elements anchored to it.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param guid unique identifier of the metadata element to remove
     * @param guidParameterName parameter supplying the guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnection(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  guid,
                                 String  guidParameterName,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    guid,
                                    guidParameterName,
                                    OpenMetadataType.CONNECTION_TYPE_GUID,
                                    OpenMetadataType.CONNECTION_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of connection objects attached to the requested asset object.
     * This includes the endpoint and connector type.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset object
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param serviceSupportedZones list of supported zones for the calling service
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
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
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        /*
         * This checks the asset is visible to the user.
         */
        EntityDetail assetEntity = getEntityFromRepository(userId,
                                                           assetGUID,
                                                           assetGUIDParameterName,
                                                           OpenMetadataType.ASSET.typeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           serviceSupportedZones,
                                                           effectiveTime,
                                                           methodName);

        /*
         * Each returned entity has been verified as readable by the user before it is returned.
         */
        List<EntityDetail> connectionEntities = this.getAttachedEntities(userId,
                                                                         assetEntity,
                                                                         assetGUIDParameterName,
                                                                         OpenMetadataType.ASSET.typeName,
                                                                         OpenMetadataType.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                         OpenMetadataType.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                         OpenMetadataType.CONNECTION_TYPE_NAME,
                                                                         null,
                                                                         null,
                                                                         1,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         serviceSupportedZones,
                                                                         0,
                                                                         invalidParameterHandler.getMaxPagingSize(),
                                                                         effectiveTime,
                                                                         methodName);

        EntityDetail selectedEntity;

        if (connectionEntities == null)
        {
            return null;
        }
        else
        {
            selectedEntity = securityVerifier.selectConnection(userId, assetEntity, connectionEntities, repositoryHelper, serviceName, methodName);
        }

        return getFullConnection(userId, selectedEntity, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Retrieve a connection object.  This may be a standard connection or a virtual connection.  The method includes the
     * connector type, endpoint and any embedded connections in the returned bean.  (This is an alternative to calling
     * the standard generic handler methods that would only retrieve the properties of the Connection entity.)
     *
     * @param userId calling user
     * @param connectionEntity entity for root connection object
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return connection object
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private B getFullConnection(String       userId,
                                EntityDetail connectionEntity,
                                boolean      forLineage,
                                boolean      forDuplicateProcessing,
                                Date         effectiveTime,
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
            List<Relationship> supplementaryRelationships = this.getEmbeddedRelationships(userId, connectionEntity, forLineage, forDuplicateProcessing, effectiveTime, methodName);
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
                                                       OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_NAME))
                                    || (repositoryHelper.isTypeOf(serviceName,
                                                                  relationship.getType().getTypeDefName(),
                                                                  OpenMetadataType.EMBEDDED_CONNECTION_TYPE_NAME)))
                        {
                            entityProxy = relationship.getEntityTwoProxy();
                        }
                        else if (repositoryHelper.isTypeOf(serviceName,
                                                           relationship.getType().getTypeDefName(),
                                                           OpenMetadataType.CONNECTION_ENDPOINT_TYPE_NAME))
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
                                                                                            null,
                                                                                            null,
                                                                                            forLineage,
                                                                                            forDuplicateProcessing,
                                                                                            supportedZones,
                                                                                            effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return list of relationships
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private List<Relationship> getEmbeddedRelationships(String        userId,
                                                        EntitySummary connectionEntity,
                                                        boolean       forLineage,
                                                        boolean       forDuplicateProcessing,
                                                        Date          effectiveTime,
                                                        String        methodName) throws PropertyServerException,
                                                                                         UserNotAuthorizedException,
                                                                                         InvalidParameterException
    {
        List<Relationship> supplementaryRelationships = new ArrayList<>();

        if ((connectionEntity != null) && (connectionEntity.getType() != null))
        {
            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           invalidParameterHandler,
                                                                                           userId,
                                                                                           connectionEntity.getGUID(),
                                                                                           connectionEntity.getType().getTypeDefName(),
                                                                                           null,
                                                                                           null,
                                                                                           0,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           effectiveTime,
                                                                                           methodName);


            while (iterator.moreToReceive())
            {
                Relationship relationship = iterator.getNext();

                if ((relationship != null) && (relationship.getType() != null))
                {
                    if ((repositoryHelper.isTypeOf(serviceName,
                                                   relationship.getType().getTypeDefName(),
                                                   OpenMetadataType.CONNECTION_ENDPOINT_TYPE_NAME))
                                || (repositoryHelper.isTypeOf(serviceName,
                                                              relationship.getType().getTypeDefName(),
                                                              OpenMetadataType.CONNECTION_CONNECTOR_TYPE_TYPE_NAME))
                                || (repositoryHelper.isTypeOf(serviceName,
                                                              relationship.getType().getTypeDefName(),
                                                              OpenMetadataType.CONNECTION_TO_ASSET_TYPE_NAME)))
                    {
                        supplementaryRelationships.add(relationship);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName,
                                                       relationship.getType().getTypeDefName(),
                                                       OpenMetadataType.EMBEDDED_CONNECTION_TYPE_NAME))
                    {
                        supplementaryRelationships.add(relationship);

                        EntityProxy embeddedConnectionEnd = relationship.getEntityTwoProxy();
                        if ((embeddedConnectionEnd != null) && (embeddedConnectionEnd.getGUID() != null))
                        {
                            List<Relationship> embeddedConnectionRelationships = this.getEmbeddedRelationships(userId,
                                                                                                               embeddedConnectionEnd,
                                                                                                               forLineage,
                                                                                                               forDuplicateProcessing,
                                                                                                               effectiveTime,
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
     * Convert a list of connection entities into a list of beans.
     * 
     * @param userId calling user
     * @param entities retrieved entities
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *                   
     * @return list of beans
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private List<B> getFullConnections(String             userId,
                                       List<EntityDetail> entities,
                                       boolean            forLineage,
                                       boolean            forDuplicateProcessing,
                                       Date               effectiveTime,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        List<B> results = new ArrayList<>();

        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    B bean = this.getFullConnection(userId, entity, forLineage, forDuplicateProcessing, effectiveTime, methodName);

                    if (bean != null)
                    {
                        results.add(bean);
                    }
                }
            }
        }

        if (! results.isEmpty())
        {
            return results;
        }

        return null;
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of Connection beans
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
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        /*
         * The generic handler calls to the security verifier as each connector is retrieved.
         */
        List<EntityDetail> entities = super.getAttachedEntities(userId,
                                                                assetGUID,
                                                                assetGUIDParameterName,
                                                                OpenMetadataType.ASSET.typeName,
                                                                OpenMetadataType.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                OpenMetadataType.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                OpenMetadataType.CONNECTION_TYPE_NAME,
                                                                null,
                                                                null,
                                                                1,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                serviceSupportedZones,
                                                                startingFrom,
                                                                pageSize,
                                                                effectiveTime,
                                                                methodName);

        return getFullConnections(userId, entities, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression. This only retrieves the connection entity.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findConnections(String  userId,
                                   String  searchString,
                                   String  searchStringParameterName,
                                   int     startFrom,
                                   int     pageSize,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date     effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        List<EntityDetail> entities = this.findEntities(userId,
                                                        searchString,
                                                        searchStringParameterName,
                                                        OpenMetadataType.CONNECTION_TYPE_GUID,
                                                        OpenMetadataType.CONNECTION_TYPE_NAME,
                                                        null,
                                                        null,
                                                        null,
                                                        startFrom,
                                                        pageSize,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);

        return getFullConnections(userId, entities, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified name or display name.
     * There are no wildcards supported on this request. This only retrieves the connection entity.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getConnectionsByName(String userId,
                                        String  name,
                                        String  nameParameterName,
                                        int     startFrom,
                                        int     pageSize,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.DISPLAY_NAME.name);
        
        List<EntityDetail> entities =  this.getEntitiesByValue(userId,
                                                               name,
                                                               nameParameterName,
                                                               OpenMetadataType.CONNECTION_TYPE_GUID,
                                                               OpenMetadataType.CONNECTION_TYPE_NAME,
                                                               specificMatchPropertyNames,
                                                               true,
                                                               false,
                                                               null,
                                                               null,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               supportedZones,
                                                               null,
                                                               startFrom,
                                                               pageSize,
                                                               effectiveTime,
                                                               methodName);

        return getFullConnections(userId, entities, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getConnectionByGUID(String  userId,
                                 String  guid,
                                 String  guidParameterName,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           guid,
                                                           guidParameterName,
                                                           OpenMetadataType.CONNECTION_TYPE_NAME,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           supportedZones,
                                                           effectiveTime,
                                                           methodName);

        if (entity != null)
        {
            return getFullConnection(userId, entity, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        return null;
    }
}
