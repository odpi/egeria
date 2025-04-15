/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.api;


import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ConnectionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.EndpointElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;

import java.util.List;
import java.util.Map;

/**
 * ManageConnections provides methods to define connections and their supporting objects
 * The interface supports the following types of objects
 *
 * <ul>
 *     <li>Connections - the connections used to create connector instances that can access the connection.</li>
 *     <li>ConnectorTypes - description of a specific that can be used to access the connection.</li>
 *     <li>ConnectorCategories - the network information needed to access the connection.</li>
 *     <li>Endpoints - the network information needed to access the connection.</li>
 * </ul>
 */
public interface ConnectionManagerInterface
{

    /*
     * The Connection entity is the top level element to describe the information needed to create a connector to an asset.
     */

    /**
     * Create a new metadata element to represent the connection.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param connectionProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createConnection(String               userId,
                            String               dataManagerGUID,
                            String               dataManagerName,
                            ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createConnectionFromTemplate(String             userId,
                                        String             dataManagerGUID,
                                        String             dataManagerName,
                                        String             templateGUID,
                                        TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Update the metadata element representing a connection.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectionProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateConnection(String               userId,
                          String               dataManagerGUID,
                          String               dataManagerName,
                          String               connectionGUID,
                          boolean              isMergeUpdate,
                          ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupConnectorType(String  userId,
                            String  dataManagerGUID,
                            String  dataManagerName,
                            String  connectionGUID,
                            String  connectorTypeGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearConnectorType(String userId,
                            String dataManagerGUID,
                            String dataManagerName,
                            String connectionGUID,
                            String connectorTypeGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupEndpoint(String  userId,
                       String  dataManagerGUID,
                       String  dataManagerName,
                       String  connectionGUID,
                       String  endpointGUID) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException;


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearEndpoint(String userId,
                       String dataManagerGUID,
                       String dataManagerName,
                       String connectionGUID,
                       String endpointGUID) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException;


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param position which order should this connection be processed
     * @param arguments What additional properties should be passed to the embedded connector via the configuration properties
     * @param displayName what does this connector signify?
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupEmbeddedConnection(String              userId,
                                 String              dataManagerGUID,
                                 String              dataManagerName,
                                 String              connectionGUID,
                                 int                 position,
                                 String              displayName,
                                 Map<String, Object> arguments,
                                 String              embeddedConnectionGUID) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearEmbeddedConnection(String userId,
                                 String dataManagerGUID,
                                 String dataManagerName,
                                 String connectionGUID,
                                 String embeddedConnectionGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the  connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupAssetConnection(String  userId,
                              String  dataManagerGUID,
                              String  dataManagerName,
                              String  assetGUID,
                              String  connectionGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearAssetConnection(String userId,
                              String dataManagerGUID,
                              String dataManagerName,
                              String assetGUID,
                              String connectionGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Remove the metadata element representing a connection.  This will delete all anchored
     * elements such as comments.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeConnection(String userId,
                          String dataManagerGUID,
                          String dataManagerName,
                          String connectionGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ConnectionElement> findConnections(String userId,
                                            String searchString,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ConnectionElement> getConnectionsByName(String userId,
                                                 String name,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    ConnectionElement getConnectionByGUID(String userId,
                                          String connectionGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /*
     * The Endpoint entity describes the location of the resource.
     */

    /**
     * Create a new metadata element to represent the endpoint.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param endpointProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createEndpoint(String             userId,
                          String             dataManagerGUID,
                          String             dataManagerName,
                          EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Create a new metadata element to represent a endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new endpoint.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties descriptive properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createEndpointFromTemplate(String             userId,
                                      String             dataManagerGUID,
                                      String             dataManagerName,
                                      String             networkAddress,
                                      String             templateGUID,
                                      TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Update the metadata element representing a endpoint.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointGUID unique identifier of the metadata element to update
     * @param endpointProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateEndpoint(String             userId,
                        String             dataManagerGUID,
                        String             dataManagerName,
                        boolean            isMergeUpdate,
                        String             endpointGUID,
                        EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Remove the metadata element representing a endpoint.  This will delete the endpoint and all categories
     * and terms.
     *
     * @param userId calling user
     * @param dataManagerGUID unique identifier of software server capability representing the caller
     * @param dataManagerName unique name of software server capability representing the caller
     * @param endpointGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeEndpoint(String userId,
                        String dataManagerGUID,
                        String dataManagerName,
                        String endpointGUID) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException;


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<EndpointElement> findEndpoints(String userId,
                                        String searchString,
                                        int    startFrom,
                                        int    pageSize) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<EndpointElement> getEndpointsByName(String userId,
                                             String name,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param endpointGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    EndpointElement getEndpointByGUID(String userId,
                                      String endpointGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;



    /*
     * The ConnectorType entity describes a specific connector implementation.  The callers to data manager OMAS are not permitted to update
     * the connector types.  These are managed through the Digital Architecture OMAS or open metadata archive.
     */


    /**
     * Retrieve the list of connectorType metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ConnectorTypeElement> findConnectorTypes(String userId,
                                                  String searchString,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Retrieve the list of connectorType metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ConnectorTypeElement> getConnectorTypesByName(String userId,
                                                       String name,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Retrieve the connectorType metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    ConnectorTypeElement getConnectorTypeByGUID(String userId,
                                                String connectorTypeGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;

}
