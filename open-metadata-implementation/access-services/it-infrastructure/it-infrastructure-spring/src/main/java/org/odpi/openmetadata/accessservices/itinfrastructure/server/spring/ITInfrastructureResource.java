/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.server.ITInfrastructureRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorTypesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.EmbeddedConnectionRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.EndpointResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.EndpointsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The ITInfrastructureResource provides the server-side implementation of the IT Infrastructure Open Metadata Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/it-infrastructure/users/{userId}")

@Tag(name="Metadata Access Server: IT Infrastructure OMAS",
     description="The IT Infrastructure OMAS provides APIs for tools and applications managing the IT infrastructure that supports the data assets.\n",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/it-infrastructure/overview/"))

public class ITInfrastructureResource
{
    private final ITInfrastructureRESTServices restAPI = new ITInfrastructureRESTServices();

    /**
     * Default constructor
     */
    public ITInfrastructureResource()
    {
    }



    /**
     * Return the description of this service.
     *
     * @param serverName name of the server to route the request to
     * @param userId identifier of calling user
     *
     * @return service description or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/description")

    public RegisteredOMAGServiceResponse getServiceDescription(@PathVariable String serverName,
                                                               @PathVariable String userId)
    {
        return restAPI.getServiceDescription(serverName, userId);
    }



    /**
     * Return the connection object for the IT Infrastructure OMAS's out topic.
     *
     * @param serverName name of the server to route the request to
     * @param userId identifier of calling user
     * @param callerId unique identifier for the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")

    public OCFConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }



    /**
     * Create a new metadata element to represent a connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections")

    public GUIDResponse createConnection(@PathVariable String                serverName,
                                         @PathVariable String                userId,
                                         @RequestBody  ConnectionRequestBody requestBody)
    {
        return restAPI.createConnection(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/from-template/{templateGUID}")

    public GUIDResponse createConnectionFromTemplate(@PathVariable String              serverName,
                                                     @PathVariable String              userId,
                                                     @PathVariable String              templateGUID,
                                                     @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createConnectionFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}")

    public VoidResponse updateConnection(@PathVariable String                serverName,
                                         @PathVariable String                userId,
                                         @PathVariable String                connectionGUID,
                                         @RequestParam boolean               isMergeUpdate,
                                         @RequestBody  ConnectionRequestBody requestBody)
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

    public VoidResponse setupConnectorType(@PathVariable String                    serverName,
                                           @PathVariable String                    userId,
                                           @PathVariable String                    connectionGUID,
                                           @PathVariable String                    connectorTypeGUID,
                                           @RequestBody  ExternalSourceRequestBody requestBody)
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

    public VoidResponse clearConnectorType(@PathVariable String                    serverName,
                                           @PathVariable String                    userId,
                                           @PathVariable String                    connectionGUID,
                                           @PathVariable String                    connectorTypeGUID,
                                           @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearConnectorType(serverName, userId, connectionGUID, connectorTypeGUID, requestBody);
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint  in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/endpoints/{endpointGUID}")

    public VoidResponse setupEndpoint(@PathVariable String                    serverName,
                                      @PathVariable String                    userId,
                                      @PathVariable String                    connectionGUID,
                                      @PathVariable String                    endpointGUID,
                                      @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.setupEndpoint(serverName, userId, connectionGUID, endpointGUID, requestBody);
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint  in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/endpoints/{endpointGUID}/delete")

    public VoidResponse clearEndpoint(@PathVariable String                    serverName,
                                      @PathVariable String                    userId,
                                      @PathVariable String                    connectionGUID,
                                      @PathVariable String                    endpointGUID,
                                      @RequestBody  ExternalSourceRequestBody requestBody)
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

    public VoidResponse clearEmbeddedConnection(@PathVariable String                    serverName,
                                                @PathVariable String                    userId,
                                                @PathVariable String                    connectionGUID,
                                                @PathVariable String                    embeddedConnectionGUID,
                                                @RequestBody  ExternalSourceRequestBody requestBody)
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
    @PostMapping(path = "assets/{assetGUID}/connections/{connectionGUID}")

    public VoidResponse setupAssetConnection(@PathVariable String                     serverName,
                                             @PathVariable String                     userId,
                                             @PathVariable String                     assetGUID,
                                             @PathVariable String                     connectionGUID,
                                             @RequestBody  AssetConnectionRequestBody requestBody)
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
    @PostMapping(path = "assets/{assetGUID}/connections/{connectionGUID}/delete")

    public VoidResponse clearAssetConnection(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    assetGUID,
                                             @PathVariable String                    connectionGUID,
                                             @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearAssetConnection(serverName, userId, assetGUID, connectionGUID, requestBody);
    }



    /**
     * Remove the metadata element representing a connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/delete")

    public VoidResponse removeConnection(@PathVariable String                    serverName,
                                         @PathVariable String                    userId,
                                         @PathVariable String                    connectionGUID,
                                         @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeConnection(serverName, userId, connectionGUID, requestBody);
    }


    /**
     * Retrieve the list of connection metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
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
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/by-name")

    public ConnectionsResponse   getConnectionsByName(@PathVariable String          serverName,
                                                      @PathVariable String          userId,
                                                      @RequestBody NameRequestBody requestBody,
                                                      @RequestParam int             startFrom,
                                                      @RequestParam int             pageSize)
    {
        return restAPI.getConnectionsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/connections/{guid}")

    public ConnectionResponse getConnectionByGUID(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String guid)
    {
        return restAPI.getConnectionByGUID(serverName, userId, guid);
    }


    /**
     * Create a new metadata element to represent a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the endpoint
     *
     * @return unique identifier of the new endpoint  or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types")

    public GUIDResponse createConnectorType(@PathVariable String                   serverName,
                                            @PathVariable String                   userId,
                                            @RequestBody  ConnectorTypeRequestBody requestBody)
    {
        return restAPI.createConnectorType(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a endpoint  using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new endpoint  or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/from-template/{templateGUID}")

    public GUIDResponse createConnectorTypeFromTemplate(@PathVariable String              serverName,
                                                        @PathVariable String              userId,
                                                        @PathVariable String              templateGUID,
                                                        @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createConnectorTypeFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/{connectorTypeGUID}")

    public VoidResponse updateConnectorType(@PathVariable String                   serverName,
                                            @PathVariable String                   userId,
                                            @PathVariable String                   connectorTypeGUID,
                                            @RequestParam boolean                  isMergeUpdate,
                                            @RequestBody  ConnectorTypeRequestBody requestBody)
    {
        return restAPI.updateConnectorType(serverName, userId, connectorTypeGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing a connector type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/{connectorTypeGUID}/delete")

    public VoidResponse removeConnectorType(@PathVariable String                    serverName,
                                            @PathVariable String                    userId,
                                            @PathVariable String                    connectorTypeGUID,
                                            @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeConnectorType(serverName, userId, connectorTypeGUID, requestBody);
    }


    /**
     * Retrieve the list of connector type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
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
     * Retrieve the list of connector type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
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
     * Retrieve the connector type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/connector-types/{guid}")

    public ConnectorTypeResponse getConnectorTypeByGUID(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String guid)
    {
        return restAPI.getConnectorTypeByGUID(serverName, userId, guid);
    }



    /* ============================================================================
     * An endpoint defines how to connect to a resource on the network
     */

    /**
     * Create a new metadata element to represent a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the endpoint
     *
     * @return unique identifier of the new endpoint  or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints")

    public GUIDResponse createEndpoint(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @RequestBody  EndpointRequestBody requestBody)
    {
        return restAPI.createEndpoint(serverName, userId, null, requestBody);
    }



    /**
     * Create a new metadata element to represent a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param infrastructureGUID unique identifier of the infrastructure element that has this endpoint
     * @param requestBody properties about the endpoint
     *
     * @return unique identifier of the new endpoint  or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/for-infrastructure/{infrastructureGUID}")

    public GUIDResponse createEndpoint(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              infrastructureGUID,
                                       @RequestBody  EndpointRequestBody requestBody)
    {
        return restAPI.createEndpoint(serverName, userId, infrastructureGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a endpoint  using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new endpoint  or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/network-address/{networkAddress}/from-template/{templateGUID}")

    public GUIDResponse createEndpointFromTemplate(@PathVariable String              serverName,
                                                   @PathVariable String              userId,
                                                   @PathVariable String              networkAddress,
                                                   @PathVariable String              templateGUID,
                                                   @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createEndpointFromTemplate(serverName, userId, null, networkAddress, templateGUID, requestBody);
    }



    /**
     * Create a new metadata element to represent a endpoint  using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param infrastructureGUID unique identifier of the infrastructure element that has this endpoint
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new endpoint  or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/for-infrastructure/{infrastructureGUID}/network-address/{networkAddress}/from-template/{templateGUID}")

    public GUIDResponse createEndpointFromTemplate(@PathVariable String              serverName,
                                                   @PathVariable String              userId,
                                                   @PathVariable String              infrastructureGUID,
                                                   @PathVariable String              networkAddress,
                                                   @PathVariable String              templateGUID,
                                                   @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createEndpointFromTemplate(serverName, userId, infrastructureGUID, networkAddress, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/{endpointGUID}")

    public VoidResponse updateEndpoint(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              endpointGUID,
                                       @RequestParam boolean             isMergeUpdate,
                                       @RequestBody  EndpointRequestBody requestBody)
    {
        return restAPI.updateEndpoint(serverName, userId, endpointGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/{endpointGUID}/delete")

    public VoidResponse removeEndpoint(@PathVariable String                    serverName,
                                       @PathVariable String                    userId,
                                       @PathVariable String                    endpointGUID,
                                       @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeEndpoint(serverName, userId, endpointGUID, requestBody);
    }



    /**
     * Retrieve the list of endpoint  metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
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
     * Retrieve the list of endpoint  metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
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
     * Retrieve the list of endpoint metadata elements with a matching network address.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody url to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/endpoints/by-network-address")

    public EndpointsResponse getEndpointsByNetworkAddress(String          serverName,
                                                          String          userId,
                                                          NameRequestBody requestBody,
                                                          int             startFrom,
                                                          int             pageSize)
    {
        return restAPI.getEndpointsByNetworkAddress(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of endpoint metadata elements that are attached to a specific infrastructure element.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param infrastructureGUID element to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/endpoints/for-infrastructure/{infrastructureGUID}")

    public EndpointsResponse getEndpointsForInfrastructure(@PathVariable String serverName,
                                                           @PathVariable String userId,
                                                           @PathVariable String infrastructureGUID,
                                                           @RequestParam int    startFrom,
                                                           @RequestParam int    pageSize)
    {
        return restAPI.getEndpointsForInfrastructure(serverName, userId, infrastructureGUID, startFrom, pageSize);
    }



    /**
     * Retrieve the endpoint  metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/endpoints/{guid}")

    public EndpointResponse getEndpointByGUID(@PathVariable String serverName,
                                              @PathVariable String userId,
                                              @PathVariable String guid)
    {
        return restAPI.getEndpointByGUID(serverName, userId, guid);
    }




    /* =====================================================================================================================
     * The software capability links assets to the hosting server.
     */


    /**
     * Create a new metadata element to represent a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param infrastructureManagerIsHome should the software capability be marked as owned by the infrastructure manager so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-capabilities")

    public GUIDResponse createSoftwareCapability(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @RequestParam boolean                       infrastructureManagerIsHome,
                                                 @RequestBody  SoftwareCapabilityRequestBody requestBody)
    {
        return restAPI.createSoftwareCapability(serverName, userId, infrastructureManagerIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent a software capability using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param infrastructureManagerIsHome should the software capability be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-capabilities/from-template/{templateGUID}")

    public GUIDResponse createSoftwareCapabilityFromTemplate(@PathVariable String              serverName,
                                                             @PathVariable String              userId,
                                                             @PathVariable String              templateGUID,
                                                             @RequestParam boolean             infrastructureManagerIsHome,
                                                             @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createSoftwareCapabilityFromTemplate(serverName, userId, templateGUID, infrastructureManagerIsHome, requestBody);
    }


    /**
     * Update the metadata element representing a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param capabilityGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for this element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-capabilities/{capabilityGUID}")

    public VoidResponse updateSoftwareCapability(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        capabilityGUID,
                                                 @RequestParam boolean                       isMergeUpdate,
                                                 @RequestBody  SoftwareCapabilityRequestBody requestBody)
    {
        return restAPI.updateSoftwareCapability(serverName, userId, capabilityGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param capabilityGUID unique identifier of the metadata element to remove
     * @param requestBody unique identifier of software capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-capabilities/{capabilityGUID}/delete")

    public VoidResponse removeSoftwareCapability(@PathVariable String                    serverName,
                                                 @PathVariable String                    userId,
                                                 @PathVariable String                    capabilityGUID,
                                                 @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeSoftwareCapability(serverName, userId, capabilityGUID, requestBody);
    }


    /**
     * Retrieve the list of software capability metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-capabilities/by-search-string")

    public SoftwareCapabilitiesResponse findSoftwareCapabilities(@PathVariable String                  serverName,
                                                                 @PathVariable String                  userId,
                                                                 @RequestParam int                     startFrom,
                                                                 @RequestParam int                     pageSize,
                                                                 @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findSoftwareCapabilities(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of software capability metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody values to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-capabilities/by-name")

    public SoftwareCapabilitiesResponse getSoftwareCapabilitiesByName(@PathVariable String          serverName,
                                                                      @PathVariable String          userId,
                                                                      @RequestParam int             startFrom,
                                                                      @RequestParam int             pageSize,
                                                                      @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getSoftwareCapabilitiesByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the software capability metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/software-capabilities/{guid}")

    public SoftwareCapabilityResponse getSoftwareCapabilityByGUID(@PathVariable String serverName,
                                                                  @PathVariable String userId,
                                                                  @PathVariable String guid)
    {
        return restAPI.getSoftwareCapabilityByGUID(serverName, userId, guid);
    }


    /*
     * A software capability works with assets
     */

    /**
     * Create a new metadata relationship to represent the use of an asset by a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param infrastructureManagerIsHome should the software capability be marked as owned by the infrastructure manager so others can not update?
     * @param capabilityGUID unique identifier of a software capability
     * @param assetGUID unique identifier of an asset
     * @param requestBody properties about the ServerAssetUse relationship
     *
     * @return unique identifier of the new ServerAssetUse relationship or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/server-asset-uses/software-capabilities/{capabilityGUID}/assets/{assetGUID}")

    public GUIDResponse createServerAssetUse(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    capabilityGUID,
                                             @PathVariable String                    assetGUID,
                                             @RequestParam boolean                   infrastructureManagerIsHome,
                                             @RequestBody  ServerAssetUseRequestBody requestBody)
    {
        return restAPI.createServerAssetUse(serverName, userId, capabilityGUID, assetGUID, infrastructureManagerIsHome, requestBody);
    }


    /**
     * Update the metadata relationship to represent the use of an asset by a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param serverAssetUseGUID unique identifier of the relationship between a software capability and an asset
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the ServerAssetUse relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/server-asset-uses/{serverAssetUseGUID}")

    public VoidResponse updateServerAssetUse(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    serverAssetUseGUID,
                                             @RequestParam boolean                   isMergeUpdate,
                                             @RequestBody  ServerAssetUseRequestBody requestBody)
    {
        return restAPI.updateServerAssetUse(serverName, userId, serverAssetUseGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata relationship to represent the use of an asset by a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param serverAssetUseGUID unique identifier of the relationship between a software capability and an asset
     * @param requestBody unique identifier of software capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/server-asset-uses/{serverAssetUseGUID}/delete")

    public VoidResponse removeServerAssetUse(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    serverAssetUseGUID,
                                             @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeServerAssetUse(serverName, userId, serverAssetUseGUID, requestBody);
    }


    /**
     * Return the list of server asset use relationships associated with a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param capabilityGUID unique identifier of the software capability to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody values to search for.
     *
     * @return list of matching relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/server-asset-uses/software-capabilities/{capabilityGUID}")

    public ServerAssetUsesResponse getServerAssetUsesForCapability(@PathVariable String             serverName,
                                                                   @PathVariable String             userId,
                                                                   @PathVariable String             capabilityGUID,
                                                                   @RequestParam int                startFrom,
                                                                   @RequestParam int                pageSize,
                                                                   @RequestBody  UseTypeRequestBody requestBody)
    {
        return restAPI.getServerAssetUsesForCapability(serverName, userId, capabilityGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Return the list of software capabilities that make use of a specific asset.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody values to search for
     *
     * @return list of matching relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/server-asset-uses/assets/{assetGUID}")

    public ServerAssetUsesResponse getCapabilityUsesForAsset(@PathVariable String             serverName,
                                                             @PathVariable String             userId,
                                                             @PathVariable String             assetGUID,
                                                             @RequestParam int                startFrom,
                                                             @RequestParam int                pageSize,
                                                             @RequestBody  UseTypeRequestBody requestBody)
    {
        return restAPI.getCapabilityUsesForAsset(serverName, userId, assetGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of relationships between a specific software capability and a specific asset.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param capabilityGUID unique identifier of a software capability
     * @param assetGUID unique identifier of an asset
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody effective time for the query
     *
     * @return list of matching relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/server-asset-uses/software-capabilities/{capabilityGUID}/assets/{assetGUID}/by-elements")

    public ServerAssetUsesResponse getServerAssetUsesForElements(@PathVariable String                   serverName,
                                                                 @PathVariable String                   userId,
                                                                 @PathVariable String                   capabilityGUID,
                                                                 @PathVariable String                   assetGUID,
                                                                 @RequestParam int                      startFrom,
                                                                 @RequestParam int                      pageSize,
                                                                 @RequestBody ResultsRequestBody requestBody)
    {
        return restAPI.getServerAssetUsesForElements(serverName, userId, capabilityGUID, assetGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the server asset use type relationship with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested relationship or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/server-asset-uses/{guid}")

    public ServerAssetUseResponse getServerAssetUseByGUID(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String guid)
    {
        return restAPI.getServerAssetUseByGUID(serverName, userId, guid);
    }
}
