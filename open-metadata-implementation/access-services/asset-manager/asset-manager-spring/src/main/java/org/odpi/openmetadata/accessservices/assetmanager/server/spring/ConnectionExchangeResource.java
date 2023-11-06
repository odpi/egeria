/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.accessservices.assetmanager.server.ConnectionExchangeRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConnectionExchangeResource is the server-side implementation of the Asset Manager OMAS's
 * support for connections, endpoints and connector types.  It matches the ConnectionExchangeClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Metadata Access Server: Asset Manager OMAS",
     description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/asset-manager/overview/"))

public class ConnectionExchangeResource
{
    private final ConnectionExchangeRESTServices restAPI= new ConnectionExchangeRESTServices();


    /**
     * Default constructor
     */
    public ConnectionExchangeResource()
    {
    }


    /* ======================================================================================
     * The Connection entity is the top level element to describe a connection.
     */

    /**
     * Create a new metadata element to represent the root of a connection.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this connection
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections")
    public GUIDResponse createConnection(@PathVariable String                serverName,
                                         @PathVariable String                userId,
                                         @RequestParam boolean               assetManagerIsHome,
                                         @RequestBody  ConnectionRequestBody requestBody)
    {
        return restAPI.createConnection(serverName, userId, assetManagerIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connection.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param assetManagerIsHome ensure that only the asset manager can update this connection
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/from-template/{templateGUID}")
    public GUIDResponse createConnectionFromTemplate(@PathVariable String              serverName,
                                                     @PathVariable String              userId,
                                                     @PathVariable String              templateGUID,
                                                     @RequestParam boolean             assetManagerIsHome,
                                                     @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createConnectionFromTemplate(serverName, userId, templateGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}")
    public VoidResponse updateConnection(@PathVariable String                serverName,
                                         @PathVariable String                userId,
                                         @PathVariable String                connectionGUID,
                                         @RequestParam boolean               isMergeUpdate,
                                         @RequestParam boolean               forLineage,
                                         @RequestParam boolean               forDuplicateProcessing,
                                         @RequestBody  ConnectionRequestBody requestBody)
    {
        return restAPI.updateConnection(serverName, userId, connectionGUID, isMergeUpdate, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship      
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/connector-types/{connectorTypeGUID}")
    public VoidResponse setupConnectorType(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @PathVariable String                  connectionGUID,
                                           @PathVariable String                  connectorTypeGUID,
                                           @RequestParam boolean                 assetManagerIsHome,
                                           @RequestParam boolean                 forLineage,
                                           @RequestParam boolean                 forDuplicateProcessing,
                                           @RequestBody  RelationshipRequestBody requestBody)

    {
        return restAPI.setupConnectorType(serverName, userId, connectionGUID, connectorTypeGUID, assetManagerIsHome, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/connector-types/{connectorTypeGUID}/remove")
    public VoidResponse clearConnectorType(@PathVariable String                        serverName,
                                           @PathVariable String                        userId,
                                           @PathVariable String                        connectionGUID,
                                           @PathVariable String                        connectorTypeGUID,
                                           @RequestParam boolean                       forLineage,
                                           @RequestParam boolean                       forDuplicateProcessing,
                                           @RequestBody  EffectiveTimeQueryRequestBody requestBody)

    {
        return restAPI.clearConnectorType(serverName, userId, connectionGUID, connectorTypeGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/endpoints/{endpointGUID}")
    public VoidResponse setupEndpoint(@PathVariable String                  serverName,
                                      @PathVariable String                  userId,
                                      @PathVariable String                  connectionGUID,
                                      @PathVariable String                  endpointGUID,
                                      @RequestParam boolean                 assetManagerIsHome,
                                      @RequestParam boolean                 forLineage,
                                      @RequestParam boolean                 forDuplicateProcessing,
                                      @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupEndpoint(serverName, userId, connectionGUID, endpointGUID, assetManagerIsHome, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/endpoints/{endpointGUID}/remove")
    public VoidResponse clearEndpoint(@PathVariable String                        serverName,
                                      @PathVariable String                        userId,
                                      @PathVariable String                        connectionGUID,
                                      @PathVariable String                        endpointGUID,
                                      @RequestParam boolean                       forLineage,
                                      @RequestParam boolean                       forDuplicateProcessing,
                                      @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearEndpoint(serverName, userId, connectionGUID, endpointGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/embedded-connections/{embeddedConnectionGUID}")
    public VoidResponse setupEmbeddedConnection(@PathVariable String                  serverName,
                                                @PathVariable String                  userId,
                                                @PathVariable String                  connectionGUID,
                                                @PathVariable String                  embeddedConnectionGUID,
                                                @RequestParam boolean                 assetManagerIsHome,
                                                @RequestParam boolean                 forLineage,
                                                @RequestParam boolean                 forDuplicateProcessing,
                                                @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupEmbeddedConnection(serverName, userId, connectionGUID, embeddedConnectionGUID, assetManagerIsHome, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/embedded-connections/{embeddedConnectionGUID}/remove")
    public VoidResponse clearEmbeddedConnection(@PathVariable String                        serverName,
                                                @PathVariable String                        userId,
                                                @PathVariable String                        connectionGUID,
                                                @PathVariable String                        embeddedConnectionGUID,
                                                @RequestParam boolean                       forLineage,
                                                @RequestParam boolean                       forDuplicateProcessing,
                                                @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearEmbeddedConnection(serverName, userId, connectionGUID, embeddedConnectionGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the  connection
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/{assetGUID}/connections/{connectionGUID}")
    public VoidResponse setupAssetConnection(@PathVariable String                  serverName,
                                             @PathVariable String                  userId,
                                             @PathVariable String                  assetGUID,
                                             @PathVariable String                  connectionGUID,
                                             @RequestParam boolean                 assetManagerIsHome,
                                             @RequestParam boolean                 forLineage,
                                             @RequestParam boolean                 forDuplicateProcessing,
                                             @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupAssetConnection(serverName, userId, assetManagerIsHome, assetGUID, connectionGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/{assetGUID}/connections/{connectionGUID}/remove")
    public VoidResponse clearAssetConnection(@PathVariable String                        serverName,
                                             @PathVariable String                        userId,
                                             @PathVariable String                        assetGUID,
                                             @PathVariable String                        connectionGUID,
                                             @RequestParam boolean                       forLineage,
                                             @RequestParam boolean                       forDuplicateProcessing,
                                             @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearAssetConnection(serverName, userId, assetGUID, connectionGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the metadata element representing a connection.  This will delete the connection and all anchored
     * elements such as schema and comments.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/remove")
    public VoidResponse removeConnection(@PathVariable String            serverName,
                                         @PathVariable String            userId,
                                         @PathVariable String            connectionGUID,
                                         @RequestParam boolean           forLineage,
                                         @RequestParam boolean           forDuplicateProcessing,
                                         @RequestBody  UpdateRequestBody requestBody)
    {
        return restAPI.removeConnection(serverName, userId, connectionGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of connection metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/by-search-string")
    public ConnectionsResponse findConnections(@PathVariable String                  serverName,
                                               @PathVariable String                  userId,
                                               @RequestParam int                     startFrom,
                                               @RequestParam int                     pageSize,
                                               @RequestParam boolean                 forLineage,
                                               @RequestParam boolean                 forDuplicateProcessing,
                                               @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findConnections(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of connection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/by-name")
    public ConnectionsResponse getConnectionsByName(@PathVariable String          serverName,
                                                    @PathVariable String          userId,
                                                    @RequestParam int             startFrom,
                                                    @RequestParam int             pageSize,
                                                    @RequestParam boolean         forLineage,
                                                    @RequestParam boolean         forDuplicateProcessing,
                                                    @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getConnectionsByName(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of connections created on behalf of the named asset manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/by-asset-manager")
    public ConnectionsResponse getConnectionsForAssetManager(@PathVariable String                        serverName,
                                                             @PathVariable String                        userId,
                                                             @RequestParam int                           startFrom,
                                                             @RequestParam int                           pageSize,
                                                             @RequestParam boolean                       forLineage,
                                                             @RequestParam boolean                       forDuplicateProcessing,
                                                             @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getConnectionsForAssetManager(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return matching metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/retrieve")
    public ConnectionResponse getConnectionByGUID(@PathVariable String                        serverName,
                                                  @PathVariable String                        userId,
                                                  @PathVariable String                        connectionGUID,
                                                  @RequestParam boolean                       forLineage,
                                                  @RequestParam boolean                       forDuplicateProcessing,
                                                  @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getConnectionByGUID(serverName, userId, connectionGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a new metadata element to represent a network endpoint.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this endpoint
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints")
    public GUIDResponse createEndpoint(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @RequestParam boolean             assetManagerIsHome,
                                       @RequestBody  EndpointRequestBody requestBody)
    {
        return restAPI.createEndpoint(serverName, userId, assetManagerIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent a network endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new endpoint.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/from-template/{templateGUID}")
    public GUIDResponse createEndpointFromTemplate(@PathVariable String              serverName,
                                                   @PathVariable String              userId,
                                                   @PathVariable String              templateGUID,
                                                   @RequestParam boolean             assetManagerIsHome,
                                                   @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createEndpointFromTemplate(serverName, userId, templateGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/{endpointGUID}")
    public VoidResponse updateEndpoint(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              endpointGUID,
                                       @RequestParam boolean             isMergeUpdate,
                                       @RequestParam boolean             forLineage,
                                       @RequestParam boolean             forDuplicateProcessing,
                                       @RequestBody  EndpointRequestBody requestBody)
    {
        return restAPI.updateEndpoint(serverName, userId, endpointGUID, isMergeUpdate, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the metadata element representing a network endpoint.  This will delete the endpoint and all anchored
     * elements such as schema and comments.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/{endpointGUID}/remove")
    public VoidResponse removeEndpoint(@PathVariable String            serverName,
                                       @PathVariable String            userId,
                                       @PathVariable String            endpointGUID,
                                       @RequestParam boolean           forLineage,
                                       @RequestParam boolean           forDuplicateProcessing,
                                       @RequestBody  UpdateRequestBody requestBody)
    {
        return restAPI.removeEndpoint(serverName, userId, endpointGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of network endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/by-search-string")
    public EndpointsResponse findEndpoints(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @RequestParam int                     startFrom,
                                           @RequestParam int                     pageSize,
                                           @RequestParam boolean                 forLineage,
                                           @RequestParam boolean                 forDuplicateProcessing,
                                           @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findEndpoints(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of network endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/by-name")
    public EndpointsResponse getEndpointsByName(@PathVariable String          serverName,
                                                @PathVariable String          userId,
                                                @RequestParam int             startFrom,
                                                @RequestParam int             pageSize,
                                                @RequestParam boolean         forLineage,
                                                @RequestParam boolean         forDuplicateProcessing,
                                                @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getEndpointsByName(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of endpoints created on behalf of the named asset manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return list of matching metadata elements or
     *
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/by-asset-manager")
    public EndpointsResponse getEndpointsForAssetManager(@PathVariable String                        serverName,
                                                         @PathVariable String                        userId,
                                                         @RequestParam int                           startFrom,
                                                         @RequestParam int                           pageSize,
                                                         @RequestParam boolean                       forLineage,
                                                         @RequestParam boolean                       forDuplicateProcessing,
                                                         @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getEndpointsForAssetManager(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the network endpoint metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endpointGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return matching metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/{endpointGUID}/retrieve")
    public EndpointResponse getEndpointByGUID(@PathVariable String                        serverName,
                                              @PathVariable String                        userId,
                                              @PathVariable String                        endpointGUID,
                                              @RequestParam boolean                       forLineage,
                                              @RequestParam boolean                       forDuplicateProcessing,
                                              @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getEndpointByGUID(serverName, userId, endpointGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a new metadata element to represent the root of an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types")
    public GUIDResponse createConnectorType(@PathVariable String                   serverName,
                                            @PathVariable String                   userId,
                                            @RequestParam boolean                  assetManagerIsHome,
                                            @RequestBody  ConnectorTypeRequestBody requestBody)
    {
        return restAPI.createConnectorType(serverName, userId, assetManagerIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/from-template/{templateGUID}")
    public GUIDResponse createConnectorTypeFromTemplate(@PathVariable String              serverName,
                                                        @PathVariable String              userId,
                                                        @PathVariable String              templateGUID,
                                                        @RequestParam boolean             assetManagerIsHome,
                                                        @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createConnectorTypeFromTemplate(serverName, userId, templateGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/{connectorTypeGUID}")
    public VoidResponse updateConnectorType(@PathVariable String                   serverName,
                                            @PathVariable String                   userId,
                                            @PathVariable String                   connectorTypeGUID,
                                            @RequestParam boolean                  isMergeUpdate,
                                            @RequestParam boolean                  forLineage,
                                            @RequestParam boolean                  forDuplicateProcessing,
                                            @RequestBody  ConnectorTypeRequestBody requestBody)
    {
        return restAPI.updateConnectorType(serverName, userId, connectorTypeGUID, isMergeUpdate, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the metadata element representing an asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/{connectorTypeGUID}/remove")
    public VoidResponse removeConnectorType(@PathVariable String            serverName,
                                            @PathVariable String            userId,
                                            @PathVariable String            connectorTypeGUID,
                                            @RequestParam boolean           forLineage,
                                            @RequestParam boolean           forDuplicateProcessing,
                                            @RequestBody  UpdateRequestBody requestBody)
    {
        return restAPI.removeConnectorType(serverName, userId, connectorTypeGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of connector type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/by-search-string")
    public ConnectorTypesResponse findConnectorTypes(@PathVariable String                  serverName,
                                                     @PathVariable String                  userId,
                                                     @RequestParam int                     startFrom,
                                                     @RequestParam int                     pageSize,
                                                     @RequestParam boolean                 forLineage,
                                                     @RequestParam boolean                 forDuplicateProcessing,
                                                     @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findConnectorTypes(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of connector type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/by-name")
    public ConnectorTypesResponse getConnectorTypesByName(@PathVariable String          serverName,
                                                          @PathVariable String          userId,
                                                          @RequestParam int             startFrom,
                                                          @RequestParam int             pageSize,
                                                          @RequestParam boolean         forLineage,
                                                          @RequestParam boolean         forDuplicateProcessing,
                                                          @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getConnectorTypesByName(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of assets created on behalf of the named asset manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/by-asset-manager")
    public ConnectorTypesResponse getConnectorTypesForAssetManager(@PathVariable String                        serverName,
                                                                   @PathVariable String                        userId,
                                                                   @RequestParam int                           startFrom,
                                                                   @RequestParam int                           pageSize,
                                                                   @RequestParam boolean                       forLineage,
                                                                   @RequestParam boolean                       forDuplicateProcessing,
                                                                   @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getConnectorTypesForAssetManager(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the connector type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return matching metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/{connectorTypeGUID}/retrieve")
    public ConnectorTypeResponse getConnectorTypeByGUID(@PathVariable String                        serverName,
                                                        @PathVariable String                        userId,
                                                        @PathVariable String                        connectorTypeGUID,
                                                        @RequestParam boolean                       forLineage,
                                                        @RequestParam boolean                       forDuplicateProcessing,
                                                        @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getConnectorTypeByGUID(serverName, userId, connectorTypeGUID, forLineage, forDuplicateProcessing, requestBody);
    }
}
