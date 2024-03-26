/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.handlers;

import org.odpi.openmetadata.accessservices.assetmanager.converters.ConnectionConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.ConnectorTypeConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.EndpointConverter;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetConnectionProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ConnectionProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.EmbeddedConnectionProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.EndpointProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.RelationshipProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TemplateProperties;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectorTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EndpointHandler;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * ConnectionExchangeHandler is the server-side for managing Connections, Connector Types and Endpoints.
 */
public class ConnectionExchangeHandler extends ExchangeHandlerBase
{
    private final ConnectionHandler<ConnectionElement>       connectionHandler;
    private final ConnectorTypeHandler<ConnectorTypeElement> connectorTypeHandler;
    private final EndpointHandler<EndpointElement>           endpointHandler;

    private final static String connectionGUIDParameterName    = "connectionGUID";
    private final static String connectorTypeGUIDParameterName = "connectorTypeGUID";
    private final static String endpointGUIDParameterName      = "endpointGUID";

    private final static String searchStringParameterName = "searchString";
    private final static String nameParameterName         = "name";
    private final static String entityParameterName       = "entity";
    private final static String entityGUIDParameterName   = "entity.getGUID()";

    /**
     * Construct the data asset exchange handler with information needed to work with asset related objects
     * for Asset Manager OMAS.
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve instances from.
     * @param defaultZones list of zones that the access service should set in all new instances.
     * @param publishZones list of zones that the access service sets up in published instances.
     * @param auditLog destination for audit log events.
     */
    public ConnectionExchangeHandler(String                             serviceName,
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
        super(serviceName,
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

        connectionHandler = new ConnectionHandler<>(new ConnectionConverter<>(repositoryHelper, serviceName, serverName),
                                                    ConnectionElement.class,
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

        connectorTypeHandler = new ConnectorTypeHandler<>(new ConnectorTypeConverter<>(repositoryHelper, serviceName, serverName),
                                                          ConnectorTypeElement.class,
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

        endpointHandler = new EndpointHandler<>(new EndpointConverter<>(repositoryHelper, serviceName, serverName),
                                                EndpointElement.class,
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


    /* ========================================================
     * Managing the externalIds and related correlation properties.
     */



    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToConnections(String                  userId,
                                                       String                  assetManagerGUID,
                                                       String                  assetManagerName,
                                                       List<ConnectionElement> results,
                                                       boolean                 forLineage,
                                                       boolean                 forDuplicateProcessing,
                                                       Date                    effectiveTime,
                                                       String                  methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        if (results != null)
        {
            for (MetadataElement element : results)
            {
                if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
                {
                    element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                element.getElementHeader().getGUID(),
                                                                                connectionGUIDParameterName,
                                                                                OpenMetadataType.CONNECTION_TYPE_NAME,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName));
                }
            }
        }
    }


    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToConnectorTypes(String                     userId,
                                                          String                     assetManagerGUID,
                                                          String                     assetManagerName,
                                                          List<ConnectorTypeElement> results,
                                                          boolean                    forLineage,
                                                          boolean                    forDuplicateProcessing,
                                                          Date                       effectiveTime,
                                                          String                     methodName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        if ((results != null) && (assetManagerGUID != null))
        {
            for (MetadataElement element : results)
            {
                if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
                {
                    element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                element.getElementHeader().getGUID(),
                                                                                connectorTypeGUIDParameterName,
                                                                                OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName));
                }
            }
        }
    }


    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToEndpoints(String                userId,
                                                     String                assetManagerGUID,
                                                     String                assetManagerName,
                                                     List<EndpointElement> results,
                                                     boolean               forLineage,
                                                     boolean               forDuplicateProcessing,
                                                     Date                  effectiveTime,
                                                     String                methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        if ((results != null) && (assetManagerGUID != null))
        {
            for (EndpointElement element : results)
            {
                if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
                {
                    element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                element.getElementHeader().getGUID(),
                                                                                endpointGUIDParameterName,
                                                                                OpenMetadataType.ENDPOINT_TYPE_NAME,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName));

                    this.getSupplementaryProperties(element.getElementHeader().getGUID(),
                                                    endpointGUIDParameterName,
                                                    OpenMetadataType.ENDPOINT_TYPE_NAME,
                                                    element.getEndpointProperties(),
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
                }
            }
        }
    }



    /* ======================================================================================
     * The Connection entity is the top level element to describe a connection.
     */

    /**
     * Create a new metadata element to represent the root of a connection.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this connection
     * @param connectionProperties properties to store
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnection(String                        userId,
                                   MetadataCorrelationProperties correlationProperties,
                                   boolean                       assetManagerIsHome,
                                   ConnectionProperties          connectionProperties,
                                   String                        methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String propertiesParameterName     = "connectionProperties";
        final String qualifiedNameParameterName  = "connectionProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(connectionProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(connectionProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String connectionGUID = connectionHandler.createConnection(userId,
                                                                   this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                   this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   connectionProperties.getQualifiedName(),
                                                                   connectionProperties.getDisplayName(),
                                                                   connectionProperties.getDescription(),
                                                                   connectionProperties.getAdditionalProperties(),
                                                                   connectionProperties.getSecuredProperties(),
                                                                   connectionProperties.getConfigurationProperties(),
                                                                   connectionProperties.getUserId(),
                                                                   connectionProperties.getClearPassword(),
                                                                   connectionProperties.getEncryptedPassword(),
                                                                   connectionProperties.getTypeName(),
                                                                   connectionProperties.getExtendedProperties(),
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   connectionProperties.getEffectiveFrom(),
                                                                   connectionProperties.getEffectiveTo(),
                                                                   false,
                                                                   false,
                                                                   connectionHandler.getSupportedZones(),
                                                                   null,
                                                                   methodName);

        if (connectionGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          connectionGUID,
                                          connectionGUIDParameterName,
                                          OpenMetadataType.PROCESS.typeName,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return connectionGUID;
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connection.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this connection
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectionFromTemplate(String                        userId,
                                               MetadataCorrelationProperties correlationProperties,
                                               boolean                       assetManagerIsHome,
                                               String                        templateGUID,
                                               TemplateProperties            templateProperties,
                                               String                        methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String connectionGUID = connectionHandler.createConnectionFromTemplate(userId,
                                                                               this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                               this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                               templateGUID,
                                                                               templateGUIDParameterName,
                                                                               templateProperties.getQualifiedName(),
                                                                               qualifiedNameParameterName,
                                                                               templateProperties.getDisplayName(),
                                                                               templateProperties.getDescription(),
                                                                               methodName);

        if (connectionGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          connectionGUID,
                                          connectionGUIDParameterName,
                                          OpenMetadataType.PROCESS.typeName,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return connectionGUID;
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param connectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectionProperties new properties for this element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateConnection(String                        userId,
                                 MetadataCorrelationProperties correlationProperties,
                                 String                        connectionGUID,
                                 boolean                       isMergeUpdate,
                                 ConnectionProperties          connectionProperties,
                                 boolean                       forLineage,
                                 boolean                       forDuplicateProcessing,
                                 Date                          effectiveTime,
                                 String                        methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String connectionGUIDParameterName = "connectionGUID";
        final String propertiesParameterName     = "connectionProperties";
        final String qualifiedNameParameterName  = "connectionProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(connectionProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(connectionProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        this.validateExternalIdentifier(userId,
                                        connectionGUID,
                                        connectionGUIDParameterName,
                                        OpenMetadataType.CONNECTION_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        connectionHandler.updateConnection(userId,
                                           this.getExternalSourceGUID(correlationProperties),
                                           this.getExternalSourceName(correlationProperties),
                                           connectionGUID,
                                           connectionGUIDParameterName,
                                           connectionProperties.getQualifiedName(),
                                           connectionProperties.getDisplayName(),
                                           connectionProperties.getDescription(),
                                           connectionProperties.getAdditionalProperties(),
                                           connectionProperties.getSecuredProperties(),
                                           connectionProperties.getConfigurationProperties(),
                                           connectionProperties.getUserId(),
                                           connectionProperties.getClearPassword(),
                                           connectionProperties.getEncryptedPassword(),
                                           connectionProperties.getTypeName(),
                                           connectionProperties.getExtendedProperties(),
                                           isMergeUpdate,
                                           connectionProperties.getEffectiveFrom(),
                                           connectionProperties.getEffectiveTo(),
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param properties properties for the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupConnectorType(String                 userId,
                                   String                 assetManagerGUID,
                                   String                 assetManagerName,
                                   boolean                assetManagerIsHome,
                                   String                 connectionGUID,
                                   String                 connectorTypeGUID,
                                   RelationshipProperties properties,
                                   boolean                forLineage,
                                   boolean                forDuplicateProcessing,
                                   Date                   effectiveTime,
                                   String                 methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String connectionGUIDParameterName    = "connectionGUID";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, connectorTypeGUIDParameterName, methodName);

        if (properties != null)
        {
            connectionHandler.addConnectionConnectorType(userId,
                                                         this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                         this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                         connectionGUID,
                                                         connectionGUIDParameterName,
                                                         connectorTypeGUID,
                                                         connectorTypeGUIDParameterName,
                                                         properties.getEffectiveFrom(),
                                                         properties.getEffectiveTo(),
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         effectiveTime,
                                                         methodName);
        }
        else
        {
            connectionHandler.addConnectionConnectorType(userId,
                                                         this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                         this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                         connectionGUID,
                                                         connectionGUIDParameterName,
                                                         connectorTypeGUID,
                                                         connectorTypeGUIDParameterName,
                                                         null,
                                                         null,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         effectiveTime,
                                                         methodName);
        }
    }


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConnectorType(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   String  connectionGUID,
                                   String  connectorTypeGUID,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String connectionGUIDParameterName    = "connectionGUID";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, connectorTypeGUIDParameterName, methodName);

        connectionHandler.removeConnectionConnectorType(userId,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        connectionGUID,
                                                        connectionGUIDParameterName,
                                                        connectorTypeGUID,
                                                        connectorTypeGUIDParameterName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param properties properties for the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEndpoint(String                 userId,
                              String                 assetManagerGUID,
                              String                 assetManagerName,
                              boolean                assetManagerIsHome,
                              String                 connectionGUID,
                              String                 endpointGUID,
                              RelationshipProperties properties,
                              boolean                forLineage,
                              boolean                forDuplicateProcessing,
                              Date                   effectiveTime,
                              String                 methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String connectionGUIDParameterName = "connectionGUID";
        final String endpointGUIDParameterName   = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, endpointGUIDParameterName, methodName);

        if (properties != null)
        {
            connectionHandler.addConnectionEndpoint(userId,
                                                    this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                    this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                    connectionGUID,
                                                    connectionGUIDParameterName,
                                                    endpointGUID,
                                                    endpointGUIDParameterName,
                                                    properties.getEffectiveFrom(),
                                                    properties.getEffectiveTo(),
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
        }
        else
        {
            connectionHandler.addConnectionEndpoint(userId,
                                                    this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                    this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                    connectionGUID,
                                                    connectionGUIDParameterName,
                                                    endpointGUID,
                                                    endpointGUIDParameterName,
                                                    null,
                                                    null,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
        }
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEndpoint(String  userId,
                              String  assetManagerGUID,
                              String  assetManagerName,
                              String  connectionGUID,
                              String  endpointGUID,
                              boolean forLineage,
                              boolean forDuplicateProcessing,
                              Date    effectiveTime,
                              String  methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String connectionGUIDParameterName = "connectionGUID";
        final String endpointGUIDParameterName   = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, endpointGUIDParameterName, methodName);

        connectionHandler.removeConnectionEndpoint(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   connectionGUID,
                                                   connectionGUIDParameterName,
                                                   endpointGUID,
                                                   endpointGUIDParameterName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
    }


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param properties properties describing how to use the embedded connection such as: Which order should this connection be processed;
     * What additional properties should be passed to the embedded connector via the configuration properties;
     * What does this connector signify?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEmbeddedConnection(String                       userId,
                                        String                       assetManagerGUID,
                                        String                       assetManagerName,
                                        boolean                      assetManagerIsHome,
                                        String                       connectionGUID,
                                        String                       embeddedConnectionGUID,
                                        EmbeddedConnectionProperties properties,
                                        boolean                      forLineage,
                                        boolean                      forDuplicateProcessing,
                                        Date                         effectiveTime,
                                        String                       methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String connectionGUIDParameterName         = "connectionGUID";
        final String embeddedConnectionGUIDParameterName = "embeddedConnectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(embeddedConnectionGUID, embeddedConnectionGUIDParameterName, methodName);

        if (properties != null)
        {
            connectionHandler.addEmbeddedConnection(userId,
                                                    this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                    this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                    connectionGUID,
                                                    connectionGUIDParameterName,
                                                    properties.getPosition(),
                                                    properties.getDisplayName(),
                                                    properties.getArguments(),
                                                    embeddedConnectionGUID,
                                                    embeddedConnectionGUIDParameterName,
                                                    properties.getEffectiveFrom(),
                                                    properties.getEffectiveTo(),
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    connectionHandler.getSupportedZones(),
                                                    effectiveTime,
                                                    methodName);
        }
        else
        {
            connectionHandler.addEmbeddedConnection(userId,
                                                    this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                    this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                    connectionGUID,
                                                    connectionGUIDParameterName,
                                                    0,
                                                    null,
                                                    null,
                                                    embeddedConnectionGUID,
                                                    embeddedConnectionGUIDParameterName,
                                                    null,
                                                    null,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    connectionHandler.getSupportedZones(),
                                                    effectiveTime,
                                                    methodName);
        }
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEmbeddedConnection(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  connectionGUID,
                                        String  embeddedConnectionGUID,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String connectionGUIDParameterName         = "connectionGUID";
        final String embeddedConnectionGUIDParameterName = "embeddedConnectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(embeddedConnectionGUID, embeddedConnectionGUIDParameterName, methodName);

        connectionHandler.removeEmbeddedConnection(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   connectionGUID,
                                                   connectionGUIDParameterName,
                                                   embeddedConnectionGUID,
                                                   embeddedConnectionGUIDParameterName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   connectionHandler.getSupportedZones(),
                                                   effectiveTime,
                                                   methodName);
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param assetGUID unique identifier of the asset
     * @param properties summary of the asset that is stored in the relationship between the asset and the connection.
     * @param connectionGUID unique identifier of the  connection
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAssetConnection(String                    userId,
                                     String                    assetManagerGUID,
                                     String                    assetManagerName,
                                     boolean                   assetManagerIsHome,
                                     String                    assetGUID,
                                     String                    connectionGUID,
                                     AssetConnectionProperties properties,
                                     boolean                   forLineage,
                                     boolean                   forDuplicateProcessing,
                                     Date                      effectiveTime,
                                     String                    methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String assetGUIDParameterName      = "assetGUID";
        final String connectionGUIDParameterName = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);

        if (properties != null)
        {
            connectionHandler.addConnectionToAsset(userId,
                                                   this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                   this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                   connectionGUID,
                                                   connectionGUIDParameterName,
                                                   assetGUID,
                                                   assetGUIDParameterName,
                                                   properties.getAssetSummary(),
                                                   properties.getEffectiveFrom(),
                                                   properties.getEffectiveTo(),
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
        }
        else
        {
            connectionHandler.addConnectionToAsset(userId,
                                                   this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                   this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                   connectionGUID,
                                                   connectionGUIDParameterName,
                                                   assetGUID,
                                                   assetGUIDParameterName,
                                                   null,
                                                   null,
                                                   null,
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
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAssetConnection(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  assetGUID,
                                     String  connectionGUID,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String assetGUIDParameterName      = "assetGUID";
        final String connectionGUIDParameterName = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);

        connectionHandler.removeConnectionToAsset(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  connectionGUID,
                                                  connectionGUIDParameterName,
                                                  assetGUID,
                                                  assetGUIDParameterName,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  effectiveTime,
                                                  methodName);
    }


    /**
     * Remove the metadata element representing a connection.  This will delete the connection and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param connectionGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnection(String                        userId,
                                 MetadataCorrelationProperties correlationProperties,
                                 String                        connectionGUID,
                                 boolean                       forLineage,
                                 boolean                       forDuplicateProcessing,
                                 Date                          effectiveTime,
                                 String                        methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String connectionGUIDParameterName = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        connectionGUID,
                                        connectionGUIDParameterName,
                                        OpenMetadataType.CONNECTION_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        connectionHandler.removeConnection(userId,
                                           this.getExternalSourceGUID(correlationProperties),
                                           this.getExternalSourceName(correlationProperties),
                                           connectionGUID,
                                           connectionGUIDParameterName,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }



    /**
     * Retrieve the list of connection metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> findConnections(String  userId,
                                                   String  assetManagerGUID,
                                                   String  assetManagerName,
                                                   String  searchString,
                                                   int     startFrom,
                                                   int     pageSize,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        List<ConnectionElement> results = connectionHandler.findConnections(userId,
                                                                            searchString,
                                                                            searchStringParameterName,
                                                                            startFrom,
                                                                            pageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveTime,
                                                                            methodName);

        addCorrelationPropertiesToConnections(userId,
                                              assetManagerGUID,
                                              assetManagerName,
                                              results,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);

        return results;
    }


    /**
     * Retrieve the list of connection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> getConnectionsByName(String  userId,
                                                        String  assetManagerGUID,
                                                        String  assetManagerName,
                                                        String  name,
                                                        int     startFrom,
                                                        int     pageSize,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing,
                                                        Date    effectiveTime,
                                                        String  methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        List<ConnectionElement> results = connectionHandler.getConnectionsByName(userId,
                                                                                 name,
                                                                                 nameParameterName,
                                                                                 startFrom,
                                                                                 pageSize,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 effectiveTime,
                                                                                 methodName);

        addCorrelationPropertiesToConnections(userId,
                                              assetManagerGUID,
                                              assetManagerName,
                                              results,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);

        return results;
    }


    /**
     * Retrieve the list of connections created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> getConnectionsForAssetManager(String  userId,
                                                                 String  assetManagerGUID,
                                                                 String  assetManagerName,
                                                                 int     startFrom,
                                                                 int     pageSize,
                                                                 boolean forLineage,
                                                                 boolean forDuplicateProcessing,
                                                                 Date    effectiveTime,
                                                                 String  methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String assetManagerGUIDParameterName = "assetManagerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<ConnectionElement> results = new ArrayList<>();

        List<EntityDetail> entities = externalIdentifierHandler.getElementEntitiesForScope(userId,
                                                                                           assetManagerGUID,
                                                                                           assetManagerGUIDParameterName,
                                                                                           OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                                           OpenMetadataType.CONNECTION_TYPE_NAME,
                                                                                           startFrom,
                                                                                           validatedPageSize,
                                                                                           effectiveTime,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           methodName);

        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    ConnectionElement element = connectionHandler.getBeanFromEntity(userId, entity, entityParameterName, methodName);

                    if (element != null)
                    {
                        element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                    entity.getGUID(),
                                                                                    entityGUIDParameterName,
                                                                                    OpenMetadataType.CONNECTION_TYPE_NAME,
                                                                                    assetManagerGUID,
                                                                                    assetManagerName,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    effectiveTime,
                                                                                    methodName));

                        results.add(element);
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the requested metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionElement getConnectionByGUID(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 String  connectionGUID,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String guidParameterName = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, guidParameterName, methodName);

        ConnectionElement element = connectionHandler.getConnectionByGUID(userId,
                                                                          connectionGUID,
                                                                          connectionGUIDParameterName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

        if (element != null)
        {
            element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        element.getElementHeader().getGUID(),
                                                                        connectionGUIDParameterName,
                                                                        OpenMetadataType.CONNECTION_TYPE_NAME,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName));
        }

        return element;
    }


    /**
     * Create a new metadata element to represent a network endpoint.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this endpoint
     * @param endpointProperties properties to store
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpoint(String                        userId,
                                 MetadataCorrelationProperties correlationProperties,
                                 boolean                       assetManagerIsHome,
                                 EndpointProperties            endpointProperties,
                                 String                        methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String propertiesParameterName     = "endpointProperties";
        final String qualifiedNameParameterName  = "endpointProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(endpointProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(endpointProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String endpointGUID = endpointHandler.createEndpoint(userId,
                                                             this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                             this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                             null,
                                                             endpointProperties.getQualifiedName(),
                                                             endpointProperties.getTechnicalName(),
                                                             endpointProperties.getTechnicalDescription(),
                                                             endpointProperties.getAddress(),
                                                             endpointProperties.getProtocol(),
                                                             endpointProperties.getEncryptionMethod(),
                                                             endpointProperties.getAdditionalProperties(),
                                                             endpointProperties.getTypeName(),
                                                             endpointProperties.getExtendedProperties(),
                                                             endpointProperties.getEffectiveFrom(),
                                                             endpointProperties.getEffectiveTo(),
                                                             null,
                                                             methodName);

        if (endpointGUID != null)
        {
            this.maintainSupplementaryProperties(userId,
                                                 endpointGUID,
                                                 endpointGUIDParameterName,
                                                 OpenMetadataType.ENDPOINT_TYPE_NAME,
                                                 endpointProperties.getQualifiedName(),
                                                 endpointProperties,
                                                 true,
                                                 false,
                                                 false,
                                                 null,
                                                 methodName);

            this.createExternalIdentifier(userId,
                                          endpointGUID,
                                          endpointGUIDParameterName,
                                          OpenMetadataType.ENDPOINT_TYPE_NAME,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return endpointGUID;
    }


    /**
     * Create a new metadata element to represent a network endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new endpoint.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpointFromTemplate(String                        userId,
                                             MetadataCorrelationProperties correlationProperties,
                                             boolean                       assetManagerIsHome,
                                             String                        templateGUID,
                                             TemplateProperties            templateProperties,
                                             String                        methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String endpointGUID = endpointHandler.createEndpointFromTemplate(userId,
                                                                         this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                         this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                         templateGUID,
                                                                         templateProperties.getQualifiedName(),
                                                                         templateProperties.getDisplayName(),
                                                                         templateProperties.getDescription(),
                                                                         templateProperties.getNetworkAddress(),
                                                                         methodName);
        if (endpointGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          endpointGUID,
                                          endpointGUIDParameterName,
                                          OpenMetadataType.ENDPOINT_TYPE_NAME,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return endpointGUID;
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointProperties new properties for this element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateEndpoint(String                        userId,
                               MetadataCorrelationProperties correlationProperties,
                               String                        endpointGUID,
                               boolean                       isMergeUpdate,
                               EndpointProperties            endpointProperties,
                               boolean                       forLineage,
                               boolean                       forDuplicateProcessing,
                               Date                          effectiveTime,
                               String                        methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String endpointGUIDParameterName   = "endpointGUID";
        final String propertiesParameterName     = "endpointProperties";
        final String qualifiedNameParameterName  = "endpointProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, endpointGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(endpointProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(endpointProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        endpointHandler.updateEndpoint(userId,
                                       this.getExternalSourceGUID(correlationProperties),
                                       this.getExternalSourceName(correlationProperties),
                                       endpointGUID,
                                       endpointGUIDParameterName,
                                       endpointProperties.getQualifiedName(),
                                       endpointProperties.getTechnicalName(),
                                       endpointProperties.getTechnicalDescription(),
                                       endpointProperties.getAddress(),
                                       endpointProperties.getProtocol(),
                                       endpointProperties.getEncryptionMethod(),
                                       endpointProperties.getAdditionalProperties(),
                                       endpointProperties.getTypeName(),
                                       endpointProperties.getExtendedProperties(),
                                       isMergeUpdate,
                                       endpointProperties.getEffectiveFrom(),
                                       endpointProperties.getEffectiveTo(),
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime,
                                       methodName);

        this.maintainSupplementaryProperties(userId,
                                             endpointGUID,
                                             endpointGUIDParameterName,
                                             OpenMetadataType.ENDPOINT_TYPE_NAME,
                                             endpointProperties.getQualifiedName(),
                                             endpointProperties,
                                             isMergeUpdate,
                                             false,
                                             false,
                                             null,
                                             methodName);
    }


    /**
     * Remove the metadata element representing a network endpoint.  This will delete the endpoint and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param endpointGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEndpoint(String                        userId,
                               MetadataCorrelationProperties correlationProperties,
                               String                        endpointGUID,
                               boolean                       forLineage,
                               boolean                       forDuplicateProcessing,
                               Date                          effectiveTime,
                               String                        methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String connectorTypeGUIDParameterName = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, connectorTypeGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        endpointGUID,
                                        endpointGUIDParameterName,
                                        OpenMetadataType.ENDPOINT_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        endpointHandler.removeEndpoint(userId,
                                       this.getExternalSourceGUID(correlationProperties),
                                       this.getExternalSourceName(correlationProperties),
                                       endpointGUID,
                                       endpointGUIDParameterName,
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Retrieve the list of network endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> findEndpoints(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  searchString,
                                               int     startFrom,
                                               int     pageSize,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        List<EndpointElement> results = endpointHandler.findEndpoints(userId,
                                                                      searchString,
                                                                      searchStringParameterName,
                                                                      startFrom,
                                                                      pageSize,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);

        addCorrelationPropertiesToEndpoints(userId,
                                            assetManagerGUID,
                                            assetManagerName,
                                            results,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

        return results;
    }


    /**
     * Retrieve the list of network endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> getEndpointsByName(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
                                                    String  name,
                                                    int     startFrom,
                                                    int     pageSize,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing,
                                                    Date    effectiveTime,
                                                    String  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        List<EndpointElement> results = endpointHandler.getEndpointsByName(userId,
                                                                           name,
                                                                           nameParameterName,
                                                                           startFrom,
                                                                           pageSize,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           effectiveTime,
                                                                           methodName);

        addCorrelationPropertiesToEndpoints(userId,
                                            assetManagerGUID,
                                            assetManagerName,
                                            results,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

        return results;
    }


    /**
     * Retrieve the list of endpoints created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> getEndpointsForAssetManager(String  userId,
                                                             String  assetManagerGUID,
                                                             String  assetManagerName,
                                                             int     startFrom,
                                                             int     pageSize,
                                                             boolean forLineage,
                                                             boolean forDuplicateProcessing,
                                                             Date    effectiveTime,
                                                             String  methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String assetManagerGUIDParameterName = "assetManagerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<EndpointElement> results = new ArrayList<>();

        List<EntityDetail> entities = externalIdentifierHandler.getElementEntitiesForScope(userId,
                                                                                           assetManagerGUID,
                                                                                           assetManagerGUIDParameterName,
                                                                                           OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                                           OpenMetadataType.ENDPOINT_TYPE_NAME,
                                                                                           startFrom,
                                                                                           validatedPageSize,
                                                                                           effectiveTime,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           methodName);

        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    EndpointElement element = endpointHandler.getBeanFromEntity(userId, entity, entityParameterName, methodName);

                    if (element != null)
                    {
                        element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                    entity.getGUID(),
                                                                                    entityGUIDParameterName,
                                                                                    OpenMetadataType.ENDPOINT_TYPE_NAME,
                                                                                    assetManagerGUID,
                                                                                    assetManagerName,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    effectiveTime,
                                                                                    methodName));

                        this.getSupplementaryProperties(element.getElementHeader().getGUID(),
                                                        endpointGUIDParameterName,
                                                        OpenMetadataType.ENDPOINT_TYPE_NAME,
                                                        element.getEndpointProperties(),
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);

                        results.add(element);
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Retrieve the network endpoint metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param endpointGUID unique identifier of the requested metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointElement getEndpointByGUID(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  endpointGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        EndpointElement element = endpointHandler.getEndpointByGUID(userId,
                                                                    endpointGUID,
                                                                    endpointGUIDParameterName,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);

        if (element != null)
        {
            element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        element.getElementHeader().getGUID(),
                                                                        entityGUIDParameterName,
                                                                        OpenMetadataType.ENDPOINT_TYPE_NAME,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName));

            this.getSupplementaryProperties(element.getElementHeader().getGUID(),
                                            endpointGUIDParameterName,
                                            OpenMetadataType.ENDPOINT_TYPE_NAME,
                                            element.getEndpointProperties(),
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
        }

        return element;
    }


    /**
     * Create a new metadata element to represent the root of an asset.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param connectorTypeProperties properties to store
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectorType(String                        userId,
                                      MetadataCorrelationProperties correlationProperties,
                                      boolean                       assetManagerIsHome,
                                      ConnectorTypeProperties       connectorTypeProperties,
                                      String                        methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String propertiesParameterName     = "connectorTypeProperties";
        final String qualifiedNameParameterName  = "connectorTypeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(connectorTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(connectorTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String connectorTypeGUID = connectorTypeHandler.createConnectorType(userId,
                                                                            this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                            this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                            null,
                                                                            connectorTypeProperties.getQualifiedName(),
                                                                            connectorTypeProperties.getDisplayName(),
                                                                            connectorTypeProperties.getDescription(),
                                                                            connectorTypeProperties.getSupportedAssetTypeName(),
                                                                            connectorTypeProperties.getExpectedDataFormat(),
                                                                            connectorTypeProperties.getConnectorProviderClassName(),
                                                                            connectorTypeProperties.getConnectorFrameworkName(),
                                                                            connectorTypeProperties.getConnectorInterfaceLanguage(),
                                                                            connectorTypeProperties.getConnectorInterfaces(),
                                                                            connectorTypeProperties.getTargetTechnologySource(),
                                                                            connectorTypeProperties.getTargetTechnologyName(),
                                                                            connectorTypeProperties.getTargetTechnologyInterfaces(),
                                                                            connectorTypeProperties.getTargetTechnologyVersions(),
                                                                            connectorTypeProperties.getRecognizedAdditionalProperties(),
                                                                            connectorTypeProperties.getRecognizedSecuredProperties(),
                                                                            connectorTypeProperties.getRecognizedConfigurationProperties(),
                                                                            connectorTypeProperties.getAdditionalProperties(),
                                                                            connectorTypeProperties.getTypeName(),
                                                                            connectorTypeProperties.getExtendedProperties(),
                                                                            connectorTypeProperties.getEffectiveFrom(),
                                                                            connectorTypeProperties.getEffectiveTo(),
                                                                            null,
                                                                            methodName);

        if (connectorTypeGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          connectorTypeGUID,
                                          connectorTypeGUIDParameterName,
                                          OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return connectorTypeGUID;
    }


    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectorTypeFromTemplate(String                        userId,
                                                  MetadataCorrelationProperties correlationProperties,
                                                  boolean                       assetManagerIsHome,
                                                  String                        templateGUID,
                                                  TemplateProperties            templateProperties,
                                                  String                        methodName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String connectorTypeGUID = connectorTypeHandler.createConnectorTypeFromTemplate(userId,
                                                                                        this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                                        this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                                        templateGUID,
                                                                                        templateProperties.getQualifiedName(),
                                                                                        templateProperties.getDisplayName(),
                                                                                        templateProperties.getDescription(),
                                                                                        methodName);
        if (connectorTypeGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          connectorTypeGUID,
                                          connectorTypeGUIDParameterName,
                                          OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return connectorTypeGUID;
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param connectorTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectorTypeProperties new properties for this element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateConnectorType(String                        userId,
                                    MetadataCorrelationProperties correlationProperties,
                                    String                        connectorTypeGUID,
                                    boolean                       isMergeUpdate,
                                    ConnectorTypeProperties       connectorTypeProperties,
                                    boolean                       forLineage,
                                    boolean                       forDuplicateProcessing,
                                    Date                          effectiveTime,
                                    String                        methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";
        final String propertiesParameterName        = "connectorTypeProperties";
        final String qualifiedNameParameterName     = "connectorTypeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, connectorTypeGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(connectorTypeProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(connectorTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        this.validateExternalIdentifier(userId,
                                        connectorTypeGUID,
                                        connectorTypeGUIDParameterName,
                                        OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        connectorTypeHandler.updateConnectorType(userId,
                                                 this.getExternalSourceGUID(correlationProperties),
                                                 this.getExternalSourceName(correlationProperties),
                                                 connectorTypeGUID,
                                                 connectorTypeGUIDParameterName,
                                                 connectorTypeProperties.getQualifiedName(),
                                                 connectorTypeProperties.getDisplayName(),
                                                 connectorTypeProperties.getDescription(),
                                                 connectorTypeProperties.getSupportedAssetTypeName(),
                                                 connectorTypeProperties.getExpectedDataFormat(),
                                                 connectorTypeProperties.getConnectorProviderClassName(),
                                                 connectorTypeProperties.getConnectorFrameworkName(),
                                                 connectorTypeProperties.getConnectorInterfaceLanguage(),
                                                 connectorTypeProperties.getConnectorInterfaces(),
                                                 connectorTypeProperties.getTargetTechnologySource(),
                                                 connectorTypeProperties.getTargetTechnologyName(),
                                                 connectorTypeProperties.getTargetTechnologyInterfaces(),
                                                 connectorTypeProperties.getTargetTechnologyVersions(),
                                                 connectorTypeProperties.getRecognizedAdditionalProperties(),
                                                 connectorTypeProperties.getRecognizedSecuredProperties(),
                                                 connectorTypeProperties.getRecognizedConfigurationProperties(),
                                                 connectorTypeProperties.getAdditionalProperties(),
                                                 connectorTypeProperties.getTypeName(),
                                                 connectorTypeProperties.getExtendedProperties(),
                                                 connectorTypeProperties.getEffectiveFrom(),
                                                 connectorTypeProperties.getEffectiveTo(),
                                                 isMergeUpdate,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
    }


    /**
     * Remove the metadata element representing an asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnectorType(String                        userId,
                                    MetadataCorrelationProperties correlationProperties,
                                    String                        connectorTypeGUID,
                                    boolean                       forLineage,
                                    boolean                       forDuplicateProcessing,
                                    Date                          effectiveTime,
                                    String                        methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, connectorTypeGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        connectorTypeGUID,
                                        connectorTypeGUIDParameterName,
                                        OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        connectorTypeHandler.removeConnectorType(userId,
                                                 this.getExternalSourceGUID(correlationProperties),
                                                 this.getExternalSourceName(correlationProperties),
                                                 connectorTypeGUID,
                                                 connectorTypeGUIDParameterName,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
    }


    /**
     * Retrieve the list of connector type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> findConnectorTypes(String  userId,
                                                         String  assetManagerGUID,
                                                         String  assetManagerName,
                                                         String  searchString,
                                                         int     startFrom,
                                                         int     pageSize,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveTime,
                                                         String  methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        List<ConnectorTypeElement> results = connectorTypeHandler.findConnectorTypes(userId,
                                                                                     searchString,
                                                                                     searchStringParameterName,
                                                                                     startFrom,
                                                                                     pageSize,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     effectiveTime,
                                                                                     methodName);

        addCorrelationPropertiesToConnectorTypes(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 results,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);

        return results;
    }


    /**
     * Retrieve the list of connector type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> getConnectorTypesByName(String  userId,
                                                              String  assetManagerGUID,
                                                              String  assetManagerName,
                                                              String  name,
                                                              int     startFrom,
                                                              int     pageSize,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    effectiveTime,
                                                              String  methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        List<ConnectorTypeElement> results = connectorTypeHandler.getConnectorTypesByName(userId,
                                                                                          name,
                                                                                          nameParameterName,
                                                                                          startFrom,
                                                                                          pageSize,
                                                                                          forLineage,
                                                                                          forDuplicateProcessing,
                                                                                          effectiveTime,
                                                                                          methodName);

        addCorrelationPropertiesToConnectorTypes(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 results,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);

        return results;
    }


    /**
     * Retrieve the list of assets created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> getConnectorTypesForAssetManager(String  userId,
                                                                       String  assetManagerGUID,
                                                                       String  assetManagerName,
                                                                       int     startFrom,
                                                                       int     pageSize,
                                                                       boolean forLineage,
                                                                       boolean forDuplicateProcessing,
                                                                       Date    effectiveTime,
                                                                       String  methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String assetManagerGUIDParameterName = "assetManagerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<ConnectorTypeElement> results = new ArrayList<>();

        List<EntityDetail> entities = externalIdentifierHandler.getElementEntitiesForScope(userId,
                                                                                           assetManagerGUID,
                                                                                           assetManagerGUIDParameterName,
                                                                                           OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                                           OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME,
                                                                                           startFrom,
                                                                                           validatedPageSize,
                                                                                           effectiveTime,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           methodName);

        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    ConnectorTypeElement element = connectorTypeHandler.getBeanFromEntity(userId, entity, entityParameterName, methodName);

                    if (element != null)
                    {
                        element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                    entity.getGUID(),
                                                                                    entityGUIDParameterName,
                                                                                    OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME,
                                                                                    assetManagerGUID,
                                                                                    assetManagerName,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    effectiveTime,
                                                                                    methodName));

                        results.add(element);
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Retrieve the connector type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypeElement getConnectorTypeByGUID(String  userId,
                                                       String  assetManagerGUID,
                                                       String  assetManagerName,
                                                       String  openMetadataGUID,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveTime,
                                                       String  methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String guidParameterName = "openMetadataGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(openMetadataGUID, guidParameterName, methodName);

        ConnectorTypeElement element = connectorTypeHandler.getConnectorTypeByGUID(userId,
                                                                                   openMetadataGUID,
                                                                                   guidParameterName,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   effectiveTime,
                                                                                   methodName);

        if (element != null)
        {
            element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        element.getElementHeader().getGUID(),
                                                                        connectorTypeGUIDParameterName,
                                                                        OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName));
        }

        return element;
    }
}
