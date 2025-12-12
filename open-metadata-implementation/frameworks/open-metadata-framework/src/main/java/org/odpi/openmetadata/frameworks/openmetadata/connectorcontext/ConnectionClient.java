/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ConnectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with connection elements.
 */
public class ConnectionClient extends ConnectorContextClientBase
{
    private final ConnectionHandler connectionHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public ConnectionClient(ConnectorContextBase     parentContext,
                            String                   localServerName,
                            String                   localServiceName,
                            String                   connectorUserId,
                            String                   connectorGUID,
                            String                   externalSourceGUID,
                            String                   externalSourceName,
                            OpenMetadataClient       openMetadataClient,
                            AuditLog                 auditLog,
                            int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.connectionHandler = new ConnectionHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new connection.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createConnection(NewElementOptions                     newElementOptions,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   ConnectionProperties                  properties,
                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        String elementGUID = connectionHandler.createConnection(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a connection using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new connection.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing connection to copy (this will copy all the attachments such as nested content, schema
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
    public String createConnectionFromTemplate(TemplateOptions        templateOptions,
                                               String                 templateGUID,
                                               ElementProperties      replacementProperties,
                                               Map<String, String>    placeholderProperties,
                                               RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        String elementGUID = connectionHandler.createConnectionFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a connection.
     *
     * @param connectionGUID       unique identifier of the connection (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateConnection(String             connectionGUID,
                                    UpdateOptions      updateOptions,
                                    ConnectionProperties properties) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        boolean updateOccurred = connectionHandler.updateConnection(connectorUserId, connectionGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(connectionGUID);
        }

        return updateOccurred;
    }


    /**
     * Create a ConnectionConnectorType relationship between a connection and a connector type.
     *
     * @param connectionGUID       unique identifier of the connection
     * @param connectorTypeGUID unique identifier of the connector type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConnectionConnectorType(String                            connectionGUID,
                                            String                            connectorTypeGUID,
                                            MakeAnchorOptions                 makeAnchorOptions,
                                            ConnectionConnectorTypeProperties relationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        connectionHandler.linkConnectionConnectorType(connectorUserId, connectionGUID, connectorTypeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Remove the ConnectionConnectorType relationship between a connection and a connector type.
     *
     * @param connectionGUID       unique identifier of the connection
     * @param connectorTypeGUID           unique identifier of the connector type
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConnectionConnectorType(String        connectionGUID,
                                              String        connectorTypeGUID,
                                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        connectionHandler.detachConnectionConnectorType(connectorUserId, connectionGUID, connectorTypeGUID, deleteOptions);
    }


    /**
     * Create a ConnectToEndpoint relationship between a connection and an endpoint.
     *
     * @param connectionGUID          unique identifier of the connection
     * @param endpointGUID          unique identifier of the endpoint
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConnectionEndpoint(String                      connectionGUID,
                                       String                      endpointGUID,
                                       MakeAnchorOptions           makeAnchorOptions,
                                       ConnectToEndpointProperties relationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        connectionHandler.linkConnectionEndpoint(connectorUserId, connectionGUID, endpointGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Remove the ConnectToEndpoint relationship between a connection and an endpoint.
     *
     * @param connectionGUID          unique identifier of the connection
     * @param endpointGUID          unique identifier of the endpoint
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConnectionEndpoint(String        connectionGUID,
                                         String        endpointGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        connectionHandler.detachConnectionEndpoint(connectorUserId, connectionGUID, endpointGUID, deleteOptions);
    }


    /**
     * Create an EmbeddedConnection relationship between a virtual connection and an embedded connection.
     *
     * @param connectionGUID unique identifier of the virtual connection
     * @param embeddedConnectionGUID unique identifier of the embedded connection
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkEmbeddedConnection(String                       connectionGUID,
                                       String                       embeddedConnectionGUID,
                                       MakeAnchorOptions            makeAnchorOptions,
                                       EmbeddedConnectionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        connectionHandler.linkEmbeddedConnection(connectorUserId, connectionGUID, embeddedConnectionGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Remove an EmbeddedConnection relationship between a virtual connection and an embedded connection.
     *
     * @param connectionGUID unique identifier of the virtual connection
     * @param embeddedConnectionGUID unique identifier of the embedded connection
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachEmbeddedConnection(String        connectionGUID,
                                         String        embeddedConnectionGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        connectionHandler.detachEmbeddedConnection(connectorUserId, connectionGUID, embeddedConnectionGUID, deleteOptions);
    }


    /**
     * Create an AssetConnection relationship between an asset and its connection.
     *
     * @param assetGUID       unique identifier of the asset
     * @param connectionGUID            unique identifier of the connection
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAssetToConnection(String                    assetGUID,
                                      String                    connectionGUID,
                                      MakeAnchorOptions         metadataSourceOptions,
                                      AssetConnectionProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        connectionHandler.linkAssetToConnection(connectorUserId, assetGUID, connectionGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach an asset from one of its connections.
     *
     * @param assetGUID              unique identifier of the asset
     * @param connectionGUID          unique identifier of the connection
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssetFromConnection(String        assetGUID,
                                          String        connectionGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        connectionHandler.detachAssetFromConnection(connectorUserId, assetGUID, connectionGUID, deleteOptions);
    }


    /**
     * Delete a connection.
     *
     * @param connectionGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteConnection(String        connectionGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        connectionHandler.deleteConnection(connectorUserId, connectionGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(connectionGUID);
        }
    }


    /**
     * Returns the list of connections with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getConnectionsByName(String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        return connectionHandler.getConnectionsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific connection.
     *
     * @param connectionGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getConnectionByGUID(String     connectionGUID,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return connectionHandler.getConnectionByGUID(connectorUserId, connectionGUID, getOptions);
    }


    /**
     * Retrieve the list of connections metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned connections include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findConnections(String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return connectionHandler.findConnections(connectorUserId, searchString, searchOptions);
    }
}
