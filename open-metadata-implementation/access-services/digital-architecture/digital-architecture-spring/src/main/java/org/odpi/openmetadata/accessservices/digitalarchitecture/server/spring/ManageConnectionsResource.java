/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.*;
import org.odpi.openmetadata.accessservices.digitalarchitecture.server.ConnectionRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.*;

/**
 * ManageConnectionsResource provides the API operations to create and maintain connections.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/digital-architecture/users/{userId}")

@Tag(name="Metadata Access Server: Digital Architecture OMAS",
        description="The Digital Architecture OMAS provides APIs for tools and applications managing the design of data structures, software and the IT infrastructure that supports the operations of the organization.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omas/digital-architecture/overview/"))

public class ManageConnectionsResource
{
    private final ConnectionRESTServices restAPI = new ConnectionRESTServices();


    /**
     * Default constructor
     */
    public ManageConnectionsResource()
    {
    }


    /*
     * ==============================================
     * Manage Connections
     * ==============================================
     */


    /**
     * Create a new metadata element to represent a connection. 
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param requestBody properties to store
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connections")

    public GUIDResponse createConnection(@PathVariable String               serverName,
                                         @PathVariable String               userId,
                                         @RequestBody ConnectionProperties requestBody)
    {
        return restAPI.createConnection(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connection.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connections/from-template/{templateGUID}")

    public GUIDResponse createConnectionFromTemplate(@PathVariable String             serverName,
                                                     @PathVariable String             userId,
                                                     @PathVariable String             templateGUID,
                                                     @RequestBody TemplateProperties templateProperties)
    {
        return restAPI.createConnectionFromTemplate(serverName, userId, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a connection.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectionGUID       unique identifier of the metadata element to update
     * @param requestBody new properties for this element
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connections/{connectionGUID}/update")

    public VoidResponse updateConnection(@PathVariable String               serverName,
                                         @PathVariable String               userId,
                                         @PathVariable String               connectionGUID,
                                         @RequestParam boolean              isMergeUpdate,
                                         @RequestBody  ConnectionProperties requestBody)
    {
        return restAPI.updateConnection(serverName, userId, connectionGUID, isMergeUpdate, requestBody);
    }



    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/connector-types/{connectorTypeGUID}")

    public VoidResponse setupConnectorType(@PathVariable                  String          serverName,
                                           @PathVariable                  String          userId,
                                           @PathVariable                  String          connectionGUID,
                                           @PathVariable                  String          connectorTypeGUID,
                                           @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.setupConnectorType(serverName, userId, connectionGUID, connectorTypeGUID, requestBody);
    }


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/connector-types/{connectorTypeGUID}/delete")

    public VoidResponse clearConnectorType(@PathVariable String           serverName,
                                           @PathVariable String           userId,
                                           @PathVariable String           connectionGUID,
                                           @PathVariable String           connectorTypeGUID,
                                           @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.clearConnectorType(serverName, userId, connectionGUID, connectorTypeGUID, requestBody);
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/endpoints/{endpointGUID}")

    public VoidResponse setupEndpoint(@PathVariable                  String          serverName,
                                      @PathVariable                  String          userId,
                                      @PathVariable                  String          connectionGUID,
                                      @PathVariable                  String          endpointGUID,
                                      @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.setupEndpoint(serverName, userId, connectionGUID, endpointGUID, requestBody);
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/endpoints/{endpointGUID}/delete")

    public VoidResponse clearEndpoint(@PathVariable String          serverName,
                                      @PathVariable String          userId,
                                      @PathVariable String          connectionGUID,
                                      @PathVariable String          endpointGUID,
                                      @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.clearEndpoint(serverName, userId, connectionGUID, endpointGUID, requestBody);
    }


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     * @param requestBody data manager identifiers and properties for the embedded connection
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/embedded-connections/{embeddedConnectionGUID}")

    public VoidResponse setupEmbeddedConnection(@PathVariable String                        serverName,
                                                @PathVariable String                        userId,
                                                @PathVariable String                        connectionGUID,
                                                @PathVariable String                        embeddedConnectionGUID,
                                                @RequestBody  EmbeddedConnectionRequestBody requestBody)
    {
        return restAPI.setupEmbeddedConnection(serverName, userId, connectionGUID, embeddedConnectionGUID, requestBody);
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/embedded-connections/{embeddedConnectionGUID}/delete")

    public VoidResponse clearEmbeddedConnection(@PathVariable String          serverName,
                                                @PathVariable String          userId,
                                                @PathVariable String          connectionGUID,
                                                @PathVariable String          embeddedConnectionGUID,
                                                @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.clearEmbeddedConnection(serverName, userId, connectionGUID, embeddedConnectionGUID, requestBody);
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the  connection
     * @param requestBody data manager identifiers and asset summary
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetGUID}/connections/{connectionGUID}")

    public VoidResponse setupAssetConnection(@PathVariable String            serverName,
                                             @PathVariable String            userId,
                                             @PathVariable String            assetGUID,
                                             @PathVariable String            connectionGUID,
                                             @RequestBody  StringRequestBody requestBody)
    {
        return restAPI.setupAssetConnection(serverName, userId, assetGUID, connectionGUID, requestBody);
    }


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetGUID}/connections/{connectionGUID}/delete")

    public VoidResponse clearAssetConnection(@PathVariable String          serverName,
                                             @PathVariable String          userId,
                                             @PathVariable String          assetGUID,
                                             @PathVariable String          connectionGUID,
                                             @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.clearAssetConnection(serverName, userId, assetGUID, connectionGUID, requestBody);
    }



    /**
     * Remove the metadata element representing a connection.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param connectionGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connections/{connectionGUID}/delete")

    public VoidResponse removeConnection(@PathVariable                  String          serverName,
                                         @PathVariable                  String          userId,
                                         @PathVariable                  String          connectionGUID,
                                         @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeConnection(serverName, userId, connectionGUID, requestBody);
    }


    /**
     * Retrieve the list of connection metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connections/by-search-string")

    public ConnectionsResponse findConnections(@PathVariable String                  serverName,
                                               @PathVariable String                  userId,
                                               @RequestBody  SearchStringRequestBody requestBody,
                                               @RequestParam int                     startFrom,
                                               @RequestParam int                     pageSize)
    {
        return restAPI.findConnections(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of connection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connections/by-name")

    public ConnectionsResponse getConnectionsByName(@PathVariable String          serverName,
                                                    @PathVariable String          userId,
                                                    @RequestBody  NameRequestBody requestBody,
                                                    @RequestParam int             startFrom,
                                                    @RequestParam int             pageSize)
    {
        return restAPI.getConnectionsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param connectionGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/connections/{connectionGUID}")

    public ConnectionResponse getConnectionByGUID(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String connectionGUID)
    {
        return restAPI.getConnectionByGUID(serverName, userId, connectionGUID);
    }



    /*
     * ==============================================
     * Manage Endpoints
     * ==============================================
     */


    /**
     * Create a new metadata element to represent an endpoint. Classifications can be added later to define the
     * type of endpoint.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param endpointProperties properties to store
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/endpoints")

    public GUIDResponse createEndpoint(@PathVariable String             serverName,
                                       @PathVariable String             userId,
                                       @RequestBody  EndpointProperties endpointProperties)
    {
        return restAPI.createEndpoint(serverName, userId, endpointProperties);
    }


    /**
     * Create a new metadata element to represent an endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new endpoint.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param networkAddress     location of the endpoint in the network
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/endpoints/network-address/{networkAddress}/from-template/{templateGUID}")

    public GUIDResponse createEndpointFromTemplate(@PathVariable String             serverName,
                                                   @PathVariable String             userId,
                                                   @PathVariable String             networkAddress,
                                                   @PathVariable String             templateGUID,
                                                   @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createEndpointFromTemplate(serverName, userId, networkAddress, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a endpoint.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointGUID       unique identifier of the metadata element to update
     * @param endpointProperties new properties for this element
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/endpoints/{endpointGUID}/update")

    public VoidResponse updateEndpoint(@PathVariable String             serverName,
                                       @PathVariable String             userId,
                                       @PathVariable String             endpointGUID,
                                       @RequestParam boolean            isMergeUpdate,
                                       @RequestBody  EndpointProperties endpointProperties)
    {
        return restAPI.updateEndpoint(serverName, userId, endpointGUID, isMergeUpdate, endpointProperties);
    }


    /**
     * Remove the metadata element representing a endpoint.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/endpoints/{endpointGUID}/delete")

    public VoidResponse removeEndpoint(@PathVariable                  String          serverName,
                                       @PathVariable                  String          userId,
                                       @PathVariable                  String          endpointGUID,
                                       @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeEndpoint(serverName, userId, endpointGUID, requestBody);
    }


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/endpoints/by-search-string")

    public EndpointsResponse findEndpoints(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @RequestBody  SearchStringRequestBody requestBody,
                                           @RequestParam int                     startFrom,
                                           @RequestParam int                     pageSize)
    {
        return restAPI.findEndpoints(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/endpoints/by-name")

    public EndpointsResponse getEndpointsByName(@PathVariable String          serverName,
                                                @PathVariable String          userId,
                                                @RequestBody  NameRequestBody requestBody,
                                                @RequestParam int             startFrom,
                                                @RequestParam int             pageSize)
    {
        return restAPI.getEndpointsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param endpointGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/endpoints/{endpointGUID}")

    public EndpointResponse getEndpointByGUID(@PathVariable String serverName,
                                              @PathVariable String userId,
                                              @PathVariable String endpointGUID)
    {
        return restAPI.getEndpointByGUID(serverName, userId, endpointGUID);
    }



    /*
     * ==============================================
     * Manage ConnectorTypes
     * ==============================================
     */


    /**
     * Create a new metadata element to represent a connectorType. Classifications can be added later to define the
     * type of connectorType.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param connectorTypeProperties properties to store
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connector-types")

    public GUIDResponse createConnectorType(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @RequestBody  ConnectorTypeProperties connectorTypeProperties)
    {
        return restAPI.createConnectorType(serverName, userId, connectorTypeProperties);
    }


    /**
     * Create a new metadata element to represent a connectorType using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connectorType.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connector-types/from-template/{templateGUID}")

    public GUIDResponse createConnectorTypeFromTemplate(@PathVariable String             serverName,
                                                        @PathVariable String             userId,
                                                        @PathVariable String             templateGUID,
                                                        @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createConnectorTypeFromTemplate(serverName, userId, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a connectorType.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectorTypeGUID       unique identifier of the metadata element to update
     * @param connectorTypeProperties new properties for this element
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connector-types/{connectorTypeGUID}/update")

    public VoidResponse updateConnectorType(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @PathVariable String                  connectorTypeGUID,
                                            @RequestParam boolean                 isMergeUpdate,
                                            @RequestBody  ConnectorTypeProperties connectorTypeProperties)
    {
        return restAPI.updateConnectorType(serverName, userId, connectorTypeGUID, isMergeUpdate, connectorTypeProperties);
    }


    /**
     * Remove the metadata element representing a connectorType.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connector-types/{connectorTypeGUID}/delete")

    public VoidResponse removeConnectorType(@PathVariable                  String          serverName,
                                            @PathVariable                  String          userId,
                                            @PathVariable                  String          connectorTypeGUID,
                                            @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeConnectorType(serverName, userId, connectorTypeGUID, requestBody);
    }


    /**
     * Retrieve the list of connectorType metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connector-types/by-search-string")

    public ConnectorTypesResponse findConnectorTypes(@PathVariable String                  serverName,
                                                     @PathVariable String                  userId,
                                                     @RequestBody  SearchStringRequestBody requestBody,
                                                     @RequestParam int                     startFrom,
                                                     @RequestParam int                     pageSize)
    {
        return restAPI.findConnectorTypes(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of connectorType metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/connector-types/by-name")

    public ConnectorTypesResponse getConnectorTypesByName(@PathVariable String          serverName,
                                                          @PathVariable String          userId,
                                                          @RequestBody  NameRequestBody requestBody,
                                                          @RequestParam int             startFrom,
                                                          @RequestParam int             pageSize)
    {
        return restAPI.getConnectorTypesByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the connectorType metadata element with the supplied unique identifier.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/connector-types/{connectorTypeGUID}")

    public ConnectorTypeResponse getConnectorTypeByGUID(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String connectorTypeGUID)
    {
        return restAPI.getConnectorTypeByGUID(serverName, userId, connectorTypeGUID);
    }

}
