/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;


import org.odpi.openmetadata.accessservices.assetmanager.client.ConnectionExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * ConnectionExchangeService is the context for managing connections and associated elements such as connector types and endpoints.
 */
public class ConnectionExchangeService
{
    private final ConnectionExchangeClient connectionExchangeClient;
    private final String                   userId;
    private final String                   externalSourceGUID;
    private final String                   externalSourceName;
    private final String                   connectorName;
    private final SynchronizationDirection synchronizationDirection;
    private final AuditLog                 auditLog;

    private boolean       forLineage             = false;
    private final boolean forDuplicateProcessing = false;

    /**
     * Create a new client to exchange connection content with open metadata.
     *
     * @param connectionExchangeClient client for exchange requests
     * @param synchronizationDirection direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param externalSourceGUID unique identifier of the software server capability for the external source
     * @param externalSourceName unique name of the software server capability for the external source
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    ConnectionExchangeService(ConnectionExchangeClient connectionExchangeClient,
                              SynchronizationDirection synchronizationDirection,
                              String                   userId,
                              String                   externalSourceGUID,
                              String                   externalSourceName,
                              String                   connectorName,
                              AuditLog                 auditLog)
    {
        this.connectionExchangeClient = connectionExchangeClient;
        this.synchronizationDirection = synchronizationDirection;
        this.userId                   = userId;
        this.externalSourceGUID       = externalSourceGUID;
        this.externalSourceName       = externalSourceName;
        this.connectorName            = connectorName;
        this.auditLog                 = auditLog;
    }


    /**
     * Return whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     * 
     * @return boolean flag
     */
    public boolean isForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     * 
     * @param forLineage boolean flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /* ======================================================================================
     * The Connection entity is the top level element to describe the properties used to initialize a connector.
     */

    /**
     * Create a new metadata element to represent the root of a connection.
     *
     * @param externalSourceIsHome ensure that only the external source can update this connection
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param connectionProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnection(boolean                      externalSourceIsHome,
                                   ExternalIdentifierProperties externalIdentifierProperties,
                                   ConnectionProperties         connectionProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createConnection";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return connectionExchangeClient.createConnection(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             externalSourceIsHome,
                                                             externalIdentifierProperties,
                                                             connectionProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connection.
     *
     * @param externalSourceIsHome ensure that only the external source can update this process
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectionFromTemplate(boolean                      externalSourceIsHome,
                                               String                       templateGUID,
                                               ExternalIdentifierProperties externalIdentifierProperties,
                                               TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "createConnectionFromTemplate";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return connectionExchangeClient.createConnectionFromTemplate(userId,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         externalSourceIsHome,
                                                                         templateGUID,
                                                                         externalIdentifierProperties,
                                                                         templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the metadata element representing an connection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the metadata element to update
     * @param connectionExternalIdentifier unique identifier of the connection in the  external source
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectionProperties new properties for this element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateConnection(String               userId,
                                 String               externalSourceGUID,
                                 String               externalSourceName,
                                 String               connectionGUID,
                                 String               connectionExternalIdentifier,
                                 boolean              isMergeUpdate,
                                 ConnectionProperties connectionProperties,
                                 Date                 effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "updateConnection";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.updateConnection(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      connectionGUID,
                                                      connectionExternalIdentifier,
                                                      isMergeUpdate,
                                                      connectionProperties,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param externalSourceIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupConnectorType(String  userId,
                                   boolean externalSourceIsHome,
                                   String  connectionGUID,
                                   String  connectorTypeGUID,
                                   Date    effectiveFrom,
                                   Date    effectiveTo,
                                   Date    effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "setupConnectorType";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.setupConnectorType(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        externalSourceIsHome,
                                                        connectionGUID,
                                                        connectorTypeGUID,
                                                        effectiveFrom,
                                                        effectiveTo,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConnectorType(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String connectionGUID,
                                   String connectorTypeGUID,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "clearConnectorType";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.clearConnectorType(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        connectionGUID,
                                                        connectorTypeGUID,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param externalSourceIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEndpoint(String  userId,
                              String  externalSourceGUID,
                              String  externalSourceName,
                              boolean externalSourceIsHome,
                              String  connectionGUID,
                              String  endpointGUID,
                              Date    effectiveFrom,
                              Date    effectiveTo,
                              Date    effectiveTime) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName  = "setupEndpoint";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.setupEndpoint(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   externalSourceIsHome,
                                                   connectionGUID,
                                                   endpointGUID,
                                                   effectiveFrom,
                                                   effectiveTo,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEndpoint(String userId,
                              String externalSourceGUID,
                              String externalSourceName,
                              String connectionGUID,
                              String endpointGUID,
                              Date   effectiveTime) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "clearEndpoint";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.clearEndpoint(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   connectionGUID,
                                                   endpointGUID,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param externalSourceIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param properties properties describing how to use the embedded connection such as: Which order should this connection be processed;
     * What additional properties should be passed to the embedded connector via the configuration properties;
     * What does this connector signify?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEmbeddedConnection(String                       userId,
                                        String                       externalSourceGUID,
                                        String                       externalSourceName,
                                        boolean                      externalSourceIsHome,
                                        String                       connectionGUID,
                                        String                       embeddedConnectionGUID,
                                        EmbeddedConnectionProperties properties,
                                        Date                         effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "setupEmbeddedConnection";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.setupEmbeddedConnection(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             externalSourceIsHome,
                                                             connectionGUID,
                                                             embeddedConnectionGUID,
                                                             properties,
                                                             effectiveTime,
                                                             forLineage,
                                                             forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEmbeddedConnection(String userId,
                                        String externalSourceGUID,
                                        String externalSourceName,
                                        String connectionGUID,
                                        String embeddedConnectionGUID,
                                        Date   effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "clearEmbeddedConnection";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.clearEmbeddedConnection(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             connectionGUID,
                                                             embeddedConnectionGUID,
                                                             effectiveTime,
                                                             forLineage,
                                                             forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param externalSourceIsHome ensure that only the asset manager can update this relationship
     * @param assetGUID unique identifier of the asset
     * @param properties summary of the asset that is stored in the relationship between the asset and the connection.
     * @param connectionGUID unique identifier of the  connection
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAssetConnection(String                    userId,
                                     String                    externalSourceGUID,
                                     String                    externalSourceName,
                                     boolean                   externalSourceIsHome,
                                     String                    assetGUID,
                                     String                    connectionGUID,
                                     AssetConnectionProperties properties,
                                     Date                      effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "setupAssetConnection";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.setupAssetConnection(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          externalSourceIsHome,
                                                          assetGUID,
                                                          connectionGUID,
                                                          properties,
                                                          effectiveTime,
                                                          forLineage,
                                                          forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAssetConnection(String userId,
                                     String externalSourceGUID,
                                     String externalSourceName,
                                     String assetGUID,
                                     String connectionGUID,
                                     Date   effectiveTime) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "clearAssetConnection";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.clearAssetConnection(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          assetGUID,
                                                          connectionGUID,
                                                          effectiveTime,
                                                          forLineage,
                                                          forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the metadata element representing a connection.  This will delete the connection and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the metadata element to remove
     * @param connectionExternalIdentifier unique identifier of the connection in the external source
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnection(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String connectionGUID,
                                 String connectionExternalIdentifier,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "removeConnection";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.removeConnection(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      connectionGUID,
                                                      connectionExternalIdentifier,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the list of connection metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> findConnections(String searchString,
                                                   int    startFrom,
                                                   int    pageSize,
                                                   Date   effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return connectionExchangeClient.findConnections(userId, externalSourceGUID, externalSourceName, searchString, startFrom, pageSize, effectiveTime, forLineage,
                                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of connection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> getConnectionsByName(String name,
                                                        int    startFrom,
                                                        int    pageSize,
                                                        Date   effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return connectionExchangeClient.getConnectionsByName(userId, externalSourceGUID, externalSourceName, name, startFrom, pageSize, effectiveTime, forLineage,
                                                             forDuplicateProcessing);
    }


    /**
     * Retrieve the list of connections created on behalf of the named external source.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> getConnectionsForAssetManager(int  startFrom,
                                                                 int  pageSize,
                                                                 Date effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return connectionExchangeClient.getConnectionsForAssetManager(userId, externalSourceGUID, externalSourceName, startFrom, pageSize, effectiveTime, forLineage,
                                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the requested metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionElement getConnectionByGUID(String userId,
                                                 String externalSourceGUID,
                                                 String externalSourceName,
                                                 String connectionGUID,
                                                 Date   effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return connectionExchangeClient.getConnectionByGUID(userId, externalSourceGUID, externalSourceName, connectionGUID, effectiveTime, forLineage,
                                                            forDuplicateProcessing);
    }


    /**
     * Create a new metadata element to represent the root of an endpoint.
     *
     * @param externalSourceIsHome ensure that only the external source can update this endpoint
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param endpointProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpoint(boolean                      externalSourceIsHome,
                                 ExternalIdentifierProperties externalIdentifierProperties,
                                 EndpointProperties           endpointProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "createEndpoint";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return connectionExchangeClient.createEndpoint(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           externalSourceIsHome,
                                                           externalIdentifierProperties,
                                                           endpointProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent an endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new endpoint.
     *
     * @param externalSourceIsHome ensure that only the external source can update this process
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpointFromTemplate(boolean                      externalSourceIsHome,
                                             String                       templateGUID,
                                             ExternalIdentifierProperties externalIdentifierProperties,
                                             TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "createEndpointFromTemplate";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return connectionExchangeClient.createEndpointFromTemplate(userId,
                                                                       externalSourceGUID,
                                                                       externalSourceName,
                                                                       externalSourceIsHome,
                                                                       templateGUID,
                                                                       externalIdentifierProperties,
                                                                       templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the metadata element representing an endpoint.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param connectorTypeGUID unique identifier of the metadata element to update
     * @param endpointExternalIdentifier unique identifier of the endpoint in the external source
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointProperties new properties for this element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateEndpoint(String              userId,
                               String              externalSourceGUID,
                               String              externalSourceName,
                               String              connectorTypeGUID,
                               String              endpointExternalIdentifier,
                               boolean             isMergeUpdate,
                               EndpointProperties  endpointProperties,
                               Date                effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "updateEndpoint";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.updateEndpoint(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    connectorTypeGUID,
                                                    endpointExternalIdentifier,
                                                    isMergeUpdate,
                                                    endpointProperties,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the metadata element representing a connector type.  This will delete the endpoint and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     * @param endpointExternalIdentifier unique identifier of the endpoint in the external source
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEndpoint(String userId,
                               String externalSourceGUID,
                               String externalSourceName,
                               String connectorTypeGUID,
                               String endpointExternalIdentifier,
                               Date   effectiveTime) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String methodName = "removeEndpoint";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.removeEndpoint(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    connectorTypeGUID,
                                                    endpointExternalIdentifier,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> findEndpoints(String searchString,
                                               int    startFrom,
                                               int    pageSize,
                                               Date   effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return connectionExchangeClient.findEndpoints(userId, externalSourceGUID, externalSourceName, searchString, startFrom, pageSize, effectiveTime, forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> getEndpointsByName(String name,
                                                    int    startFrom,
                                                    int    pageSize,
                                                    Date   effectiveTime) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return connectionExchangeClient.getEndpointsByName(userId, externalSourceGUID, externalSourceName, name, startFrom, pageSize, effectiveTime, forLineage,
                                                           forDuplicateProcessing);
    }


    /**
     * Retrieve the list of endpoints created on behalf of the named external source.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> getEndpointsForAssetManager(int  startFrom,
                                                             int  pageSize,
                                                             Date effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return connectionExchangeClient.getEndpointsForAssetManager(userId, externalSourceGUID, externalSourceName, startFrom, pageSize, effectiveTime, forLineage,
                                                                    forDuplicateProcessing);
    }


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param endpointGUID unique identifier of the requested metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointElement getEndpointByGUID(String userId,
                                             String externalSourceGUID,
                                             String externalSourceName,
                                             String endpointGUID,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return connectionExchangeClient.getEndpointByGUID(userId, externalSourceGUID, externalSourceName, endpointGUID, effectiveTime, forLineage,
                                                          forDuplicateProcessing);
    }


    /**
     * Create a new metadata element to represent the root of a connector type.
     *
     * @param externalSourceIsHome ensure that only the connectorType manager can update this connectorType
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param connectorTypeProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectorType(boolean                      externalSourceIsHome,
                                      ExternalIdentifierProperties externalIdentifierProperties,
                                      ConnectorTypeProperties      connectorTypeProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "createConnectorType";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return connectionExchangeClient.createConnectorType(userId,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                externalSourceIsHome,
                                                                externalIdentifierProperties,
                                                                connectorTypeProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent a connector type using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connectorType.
     *
     * @param externalSourceIsHome ensure that only the connectorType manager can update this process
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectorTypeFromTemplate(boolean                      externalSourceIsHome,
                                                  String                       templateGUID,
                                                  ExternalIdentifierProperties externalIdentifierProperties,
                                                  TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "createConnectorTypeFromTemplate";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return connectionExchangeClient.createConnectorTypeFromTemplate(userId,
                                                                            externalSourceGUID,
                                                                            externalSourceName,
                                                                            externalSourceIsHome,
                                                                            templateGUID,
                                                                            externalIdentifierProperties,
                                                                            templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the metadata element representing a connector type.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param connectorTypeGUID unique identifier of the metadata element to update
     * @param connectorTypeExternalIdentifier unique identifier of the connectorType in the external connectorType manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectorTypeProperties new properties for this element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateConnectorType(String                  userId,
                                    String                  externalSourceGUID,
                                    String                  externalSourceName,
                                    String                  connectorTypeGUID,
                                    String                  connectorTypeExternalIdentifier,
                                    boolean                 isMergeUpdate,
                                    ConnectorTypeProperties connectorTypeProperties,
                                    Date                    effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "updateConnectorType";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.updateConnectorType(userId,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         connectorTypeGUID,
                                                         connectorTypeExternalIdentifier,
                                                         isMergeUpdate,
                                                         connectorTypeProperties,
                                                         effectiveTime,
                                                         forLineage,
                                                         forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the metadata element representing a connector type.  This will delete the connectorType and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     * @param connectorTypeExternalIdentifier unique identifier of the connectorType in the external connectorType manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnectorType(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String connectorTypeGUID,
                                    String connectorTypeExternalIdentifier,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "removeConnectorType";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            connectionExchangeClient.removeConnectorType(userId,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         connectorTypeGUID,
                                                         connectorTypeExternalIdentifier,
                                                         effectiveTime,
                                                         forLineage,
                                                         forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the list of connectorType metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> findConnectorTypes(String searchString,
                                                         int    startFrom,
                                                         int    pageSize,
                                                         Date   effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return connectionExchangeClient.findConnectorTypes(userId, externalSourceGUID, externalSourceName, searchString, startFrom, pageSize, effectiveTime, forLineage,
                                                           forDuplicateProcessing);
    }


    /**
     * Retrieve the list of connectorType metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> getConnectorTypesByName(String name,
                                                              int    startFrom,
                                                              int    pageSize,
                                                              Date   effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return connectionExchangeClient.getConnectorTypesByName(userId, externalSourceGUID, externalSourceName, name, startFrom, pageSize, effectiveTime, forLineage,
                                                                forDuplicateProcessing);
    }


    /**
     * Retrieve the list of connectorTypes created on behalf of the named connectorType manager.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> getConnectorTypesForAssetManager(int  startFrom,
                                                                       int  pageSize,
                                                                       Date effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return connectionExchangeClient.getConnectorTypesForAssetManager(userId, externalSourceGUID, externalSourceName, startFrom, pageSize, effectiveTime, forLineage,
                                                                         forDuplicateProcessing);
    }


    /**
     * Retrieve the connectorType metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param dataAssetGUID unique identifier of the requested metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypeElement getConnectorTypeByGUID(String userId,
                                                       String externalSourceGUID,
                                                       String externalSourceName,
                                                       String dataAssetGUID,
                                                       Date   effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return connectionExchangeClient.getConnectorTypeByGUID(userId, externalSourceGUID, externalSourceName, dataAssetGUID, effectiveTime, forLineage,
                                                               forDuplicateProcessing);
    }
}
