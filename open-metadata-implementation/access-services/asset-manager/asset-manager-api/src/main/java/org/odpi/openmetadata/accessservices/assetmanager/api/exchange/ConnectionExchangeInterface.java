/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.api.exchange;


import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EmbeddedConnectionProperties;

import java.util.Date;
import java.util.List;

/**
 * ConnectionExchangeInterface provides methods to define connections and their supporting objects
 *
 * The interface supports the following types of objects
 *
 * <ul>
 *     <li>Connections - the connections used to create connector instances that can access the connection.</li>
 *     <li>ConnectorTypes - description of a specific that can be used to access the connection.</li>
 *     <li>ConnectorCategories - the network information needed to access the connection.</li>
 *     <li>Endpoints - the network information needed to access the connection.</li>
 * </ul>
 */
public interface ConnectionExchangeInterface
{

    /*
     * The Connection entity is the top level element to describe the information needed to create a connector to an asset.
     */

    /**
     * Create a new metadata element to represent the connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param connectionProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createConnection(String                       userId,
                            String                       assetManagerGUID,
                            String                       assetManagerName,
                            boolean                      assetManagerIsHome,
                            ExternalIdentifierProperties externalIdentifierProperties,
                            ConnectionProperties         connectionProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this element
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
    String createConnectionFromTemplate(String                       userId,
                                        String                       assetManagerGUID,
                                        String                       assetManagerName,
                                        boolean                      assetManagerIsHome,
                                        String                       templateGUID,
                                        ExternalIdentifierProperties externalIdentifierProperties,
                                        TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Update the metadata element representing a connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param connectionGUID unique identifier of the metadata element to update
     * @param connectionExternalIdentifier unique identifier of the connection in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param connectionProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateConnection(String               userId,
                          String               assetManagerGUID,
                          String               assetManagerName,
                          String               connectionGUID,
                          String               connectionExternalIdentifier,
                          boolean              isMergeUpdate,
                          ConnectionProperties connectionProperties,
                          Date                 effectiveTime,
                          boolean              forLineage,
                          boolean              forDuplicateProcessing) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupConnectorType(String  userId,
                            String  assetManagerGUID,
                            String  assetManagerName,
                            boolean assetManagerIsHome,
                            String  connectionGUID,
                            String  connectorTypeGUID,
                            Date    effectiveFrom,
                            Date    effectiveTo,
                            Date    effectiveTime,
                            boolean forLineage,
                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearConnectorType(String  userId,
                            String  assetManagerGUID,
                            String  assetManagerName,
                            String  connectionGUID,
                            String  connectorTypeGUID,
                            Date    effectiveTime,
                            boolean forLineage,
                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupEndpoint(String  userId,
                       String  assetManagerGUID,
                       String  assetManagerName,
                       boolean assetManagerIsHome,
                       String  connectionGUID,
                       String  endpointGUID,
                       Date    effectiveFrom,
                       Date    effectiveTo,
                       Date    effectiveTime,
                       boolean forLineage,
                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearEndpoint(String  userId,
                       String  assetManagerGUID,
                       String  assetManagerName,
                       String  connectionGUID,
                       String  endpointGUID,
                       Date    effectiveTime,
                       boolean forLineage,
                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param properties properties describing how to use the embedded connection such as: Which order should this connection be processed;
     * What additional properties should be passed to the embedded connector via the configuration properties;
     * What does this connector signify?
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupEmbeddedConnection(String                       userId,
                                 String                       assetManagerGUID,
                                 String                       assetManagerName,
                                 boolean                      assetManagerIsHome,
                                 String                       connectionGUID,
                                 String                       embeddedConnectionGUID,
                                 EmbeddedConnectionProperties properties,
                                 Date                         effectiveTime,
                                 boolean                      forLineage,
                                 boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearEmbeddedConnection(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 String  connectionGUID,
                                 String  embeddedConnectionGUID,
                                 Date    effectiveTime,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the  connection
     * @param properties summary of the asset that is stored in the relationship between the asset and the connection.
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupAssetConnection(String                     userId,
                              String                     assetManagerGUID,
                              String                     assetManagerName,
                              boolean                    assetManagerIsHome,
                              String                     assetGUID,
                              String                     connectionGUID,
                              AssetConnectionProperties properties,
                              Date                       effectiveTime,
                              boolean                    forLineage,
                              boolean                    forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearAssetConnection(String  userId,
                              String  assetManagerGUID,
                              String  assetManagerName,
                              String  assetGUID,
                              String  connectionGUID,
                              Date    effectiveTime,
                              boolean forLineage,
                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Remove the metadata element representing a connection.  This will delete all anchored
     * elements such as comments.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param connectionGUID unique identifier of the metadata element to remove
     * @param connectionExternalIdentifier unique identifier of the connection in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeConnection(String  userId,
                          String  assetManagerGUID,
                          String  assetManagerName,
                          String  connectionGUID,
                          String  connectionExternalIdentifier,
                          Date    effectiveTime,
                          boolean forLineage,
                          boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ConnectionElement> findConnections(String  userId,
                                            String  assetManagerGUID,
                                            String  assetManagerName,
                                            String  searchString,
                                            int     startFrom,
                                            int     pageSize,
                                            Date    effectiveTime,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ConnectionElement> getConnectionsByName(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 String  name,
                                                 int     startFrom,
                                                 int     pageSize,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Retrieve the list of assets created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ConnectionElement> getConnectionsForAssetManager(String  userId,
                                                          String  assetManagerGUID,
                                                          String  assetManagerName,
                                                          int     startFrom,
                                                          int     pageSize,
                                                          Date    effectiveTime,
                                                          boolean forLineage,
                                                          boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param connectionGUID unique identifier of the requested metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    ConnectionElement getConnectionByGUID(String  userId,
                                          String  assetManagerGUID,
                                          String  assetManagerName,
                                          String  connectionGUID,
                                          Date    effectiveTime,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /*
     * The Endpoint entity describes the location of the resource.
     */

    /**
     * Create a new metadata element to represent the endpoint.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param endpointProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createEndpoint(String                       userId,
                          String                       assetManagerGUID,
                          String                       assetManagerName,
                          boolean                      assetManagerIsHome,
                          ExternalIdentifierProperties externalIdentifierProperties,
                          EndpointProperties           endpointProperties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Create a new metadata element to represent an endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new endpoint.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this element
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
    String createEndpointFromTemplate(String                       userId,
                                      String                       assetManagerGUID,
                                      String                       assetManagerName,
                                      boolean                      assetManagerIsHome,
                                      String                       templateGUID,
                                      ExternalIdentifierProperties externalIdentifierProperties,
                                      TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Update the metadata element representing an endpoint.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param endpointGUID unique identifier of the metadata element to update
     * @param endpointExternalIdentifier unique identifier of the endpoint in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointProperties new properties for this element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateEndpoint(String             userId,
                        String             assetManagerGUID,
                        String             assetManagerName,
                        String             endpointGUID,
                        String             endpointExternalIdentifier,
                        boolean            isMergeUpdate,
                        EndpointProperties endpointProperties,
                        Date               effectiveTime,
                        boolean            forLineage,
                        boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Remove the metadata element representing an endpoint.  This will delete the endpoint and all categories
     * and terms.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param endpointGUID unique identifier of the metadata element to remove
     * @param endpointExternalIdentifier unique identifier of the endpoint in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeEndpoint(String  userId,
                        String  assetManagerGUID,
                        String  assetManagerName,
                        String  endpointGUID,
                        String  endpointExternalIdentifier,
                        Date    effectiveTime,
                        boolean forLineage,
                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<EndpointElement> findEndpoints(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  searchString,
                                        int     startFrom,
                                        int     pageSize,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<EndpointElement> getEndpointsByName(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  name,
                                             int     startFrom,
                                             int     pageSize,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Retrieve the list of glossaries created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<EndpointElement> getEndpointsForAssetManager(String  userId,
                                                      String  assetManagerGUID,
                                                      String  assetManagerName,
                                                      int     startFrom,
                                                      int     pageSize,
                                                      Date    effectiveTime,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param endpointGUID unique identifier of the requested metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    EndpointElement getEndpointByGUID(String  userId,
                                      String  assetManagerGUID,
                                      String  assetManagerName,
                                      String  endpointGUID,
                                      Date    effectiveTime,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;



    /*
     * The ConnectorType entity describes a specific connector implementation.
     */


    /**
     * Create a new metadata element to represent the root of a connectorType.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param connectorTypeProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createConnectorType(String                       userId,
                               String                       assetManagerGUID,
                               String                       assetManagerName,
                               boolean                      assetManagerIsHome,
                               ExternalIdentifierProperties externalIdentifierProperties,
                               ConnectorTypeProperties      connectorTypeProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Create a new metadata element to represent a connectorType using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connectorType.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this element
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
    String createConnectorTypeFromTemplate(String                       userId,
                                           String                       assetManagerGUID,
                                           String                       assetManagerName,
                                           boolean                      assetManagerIsHome,
                                           String                       templateGUID,
                                           ExternalIdentifierProperties externalIdentifierProperties,
                                           TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException;


    /**
     * Update the metadata element representing a connectorType.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param connectorTypeGUID unique identifier of the metadata element to update
     * @param connectorTypeExternalIdentifier unique identifier of the connectorType in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectorTypeProperties new properties for this element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateConnectorType(String                  userId,
                             String                  assetManagerGUID,
                             String                  assetManagerName,
                             String                  connectorTypeGUID,
                             String                  connectorTypeExternalIdentifier,
                             boolean                 isMergeUpdate,
                             ConnectorTypeProperties connectorTypeProperties,
                             Date                    effectiveTime,
                             boolean                 forLineage,
                             boolean                 forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Remove the metadata element representing a connectorType.  This will delete the connectorType and all categories
     * and terms.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     * @param connectorTypeExternalIdentifier unique identifier of the connectorType in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeConnectorType(String  userId,
                             String  assetManagerGUID,
                             String  assetManagerName,
                             String  connectorTypeGUID,
                             String  connectorTypeExternalIdentifier,
                             Date    effectiveTime,
                             boolean forLineage,
                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Retrieve the list of connectorType metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ConnectorTypeElement> findConnectorTypes(String  userId,
                                                  String  assetManagerGUID,
                                                  String  assetManagerName,
                                                  String  searchString,
                                                  int     startFrom,
                                                  int     pageSize,
                                                  Date    effectiveTime,
                                                  boolean forLineage,
                                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Retrieve the list of connectorType metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ConnectorTypeElement> getConnectorTypesByName(String  userId,
                                                       String  assetManagerGUID,
                                                       String  assetManagerName,
                                                       String  name,
                                                       int     startFrom,
                                                       int     pageSize,
                                                       Date    effectiveTime,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Retrieve the list of glossaries created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ConnectorTypeElement> getConnectorTypesForAssetManager(String  userId,
                                                                String  assetManagerGUID,
                                                                String  assetManagerName,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                Date    effectiveTime,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException;


    /**
     * Retrieve the connectorType metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param openMetadataGUID unique identifier of the requested metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    ConnectorTypeElement getConnectorTypeByGUID(String  userId,
                                                String  assetManagerGUID,
                                                String  assetManagerName,
                                                String  openMetadataGUID,
                                                Date    effectiveTime,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;
}
