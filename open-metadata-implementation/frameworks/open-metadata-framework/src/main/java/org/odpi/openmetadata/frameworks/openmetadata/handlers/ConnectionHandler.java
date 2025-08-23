/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ConnectionHandler provides methods to define connections and link them to connector types and endpoints.
 */
public class ConnectionHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public ConnectionHandler(String             localServerName,
                             AuditLog           auditLog,
                             String             serviceName,
                             OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.CONNECTION.typeName);
    }


    /**
     * Create a new connection.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createConnection(String                                userId,
                                   NewElementOptions                     newElementOptions,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   ConnectionProperties                  properties,
                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "createConnection";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a connection using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new connection.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectionFromTemplate(String                 userId,
                                               TemplateOptions        templateOptions,
                                               String                 templateGUID,
                                               ElementProperties      replacementProperties,
                                               Map<String, String>    placeholderProperties,
                                               RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a connection.
     *
     * @param userId                 userId of user making request.
     * @param connectionGUID       unique identifier of the connection (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateConnection(String               userId,
                                 String               connectionGUID,
                                 UpdateOptions        updateOptions,
                                 ConnectionProperties properties) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName        = "updateConnection";
        final String guidParameterName = "connectionGUID";

        super.updateElement(userId,
                            connectionGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Create a ConnectionConnectorType relationship between a connection and a connector type.
     *
     * @param userId                 userId of user making request
     * @param connectionGUID       unique identifier of the connection
     * @param connectorTypeGUID unique identifier of the connector type
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConnectionConnectorType(String                            userId,
                                            String                            connectionGUID,
                                            String                            connectorTypeGUID,
                                            MetadataSourceOptions             metadataSourceOptions,
                                            ConnectionConnectorTypeProperties relationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        final String methodName            = "linkConnectionConnectorType";
        final String end1GUIDParameterName = "connectionGUID";
        final String end2GUIDParameterName = "connectorTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(connectionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(connectorTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName,
                                                        connectionGUID,
                                                        connectorTypeGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Remove the ConnectionConnectorType relationship between a connection and a connector type.
     *
     * @param userId                 userId of user making request.
     * @param connectionGUID       unique identifier of the connection
     * @param connectorTypeGUID           unique identifier of the connector type
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConnectionConnectorType(String        userId,
                                              String        connectionGUID,
                                              String        connectorTypeGUID,
                                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "detachConnectionConnectorType";

        final String end1GUIDParameterName = "connectionGUID";
        final String end2GUIDParameterName = "connectorTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(connectionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(connectorTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName,
                                                        connectorTypeGUID,
                                                        connectionGUID,
                                                        deleteOptions);
    }


    /**
     * Create a ConnectToEndpoint relationship between a connection and an endpoint.
     *
     * @param userId                 userId of user making request
     * @param connectionGUID          unique identifier of the connection
     * @param endpointGUID          unique identifier of the endpoint
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConnectionEndpoint(String                       userId,
                                       String                       connectionGUID,
                                       String                       endpointGUID,
                                       MetadataSourceOptions        metadataSourceOptions,
                                       ConnectionEndpointProperties relationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName            = "linkConnectionEndpoint";
        final String end1GUIDParameterName = "connectionGUID";
        final String end2GUIDParameterName = "endpointGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(connectionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(endpointGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName,
                                                        connectionGUID,
                                                        endpointGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Remove the ConnectToEndpoint relationship between a connection and an endpoint.
     *
     * @param userId                 userId of user making request.
     * @param connectionGUID          unique identifier of the connection
     * @param endpointGUID          unique identifier of the endpoint
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConnectionEndpoint(String        userId,
                                         String        connectionGUID,
                                         String        endpointGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "detachConnectionEndpoint";
        final String end1GUIDParameterName = "connectionGUID";
        final String end2GUIDParameterName = "endpointGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(connectionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(endpointGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName,
                                                        connectionGUID,
                                                        endpointGUID,
                                                        deleteOptions);
    }


    /**
     * Create an EmbeddedConnection relationship between a virtual connection and an embedded connection.
     *
     * @param userId                 userId of user making request
     * @param connectionGUID unique identifier of the virtual connection
     * @param embeddedConnectionGUID unique identifier of the embedded connection
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkEmbeddedConnection(String                       userId,
                                       String                       connectionGUID,
                                       String                       embeddedConnectionGUID,
                                       MetadataSourceOptions        metadataSourceOptions,
                                       EmbeddedConnectionProperties relationshipProperties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName            = "linkEmbeddedConnection";
        final String end1GUIDParameterName = "connectionGUID";
        final String end2GUIDParameterName = "embeddedConnectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(connectionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(embeddedConnectionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName,
                                                        connectionGUID,
                                                        embeddedConnectionGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Remove an EmbeddedConnection relationship between a virtual connection and an embedded connection.
     *
     * @param userId                 userId of user making request.
     * @param connectionGUID unique identifier of the virtual connection
     * @param embeddedConnectionGUID unique identifier of the embedded connection
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachEmbeddedConnection(String        userId,
                                         String        connectionGUID,
                                         String        embeddedConnectionGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "detachEmbeddedConnection";
        final String end1GUIDParameterName = "connectionGUID";
        final String end2GUIDParameterName = "embeddedConnectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(connectionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(embeddedConnectionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName,
                                                        connectionGUID,
                                                        embeddedConnectionGUID,
                                                        deleteOptions);
    }


    /**
     * Create an AssetConnection relationship between an asset and its connection.
     *
     * @param userId                 userId of user making request
     * @param assetGUID       unique identifier of the asset
     * @param connectionGUID            unique identifier of the connection
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAssetToConnection(String                    userId,
                                      String                    assetGUID,
                                      String                    connectionGUID,
                                      MetadataSourceOptions     metadataSourceOptions,
                                      AssetConnectionProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName            = "linkAssetToConnection";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "connectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(connectionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName,
                                                        assetGUID,
                                                        connectionGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an asset from one of its connections.
     *
     * @param userId                 userId of user making request.
     * @param assetGUID              unique identifier of the asset
     * @param connectionGUID          unique identifier of the connection
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssetFromConnection(String        userId,
                                          String        assetGUID,
                                          String        connectionGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "detachAssetFromConnection";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "connectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(connectionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName,
                                                        connectionGUID,
                                                        connectionGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a connection.
     *
     * @param userId                 userId of user making request.
     * @param connectionGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteConnection(String        userId,
                                 String        connectionGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName        = "deleteConnection";
        final String guidParameterName = "connectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(connectionGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, connectionGUID, deleteOptions);
    }


    /**
     * Returns the list of connections with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getConnectionsByName(String       userId,
                                                              String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName        = "getConnectionsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific connection.
     *
     * @param userId                 userId of user making request
     * @param connectionGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getConnectionByGUID(String     userId,
                                                       String     connectionGUID,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName  = "getConnectionByGUID";

        return super.getRootElementByGUID(userId, connectionGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of connections metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findConnections(String        userId,
                                                         String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "findConnections";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
