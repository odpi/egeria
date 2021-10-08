/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetConnectionRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectionRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectionResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectionsResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectorTypeRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectorTypesResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EmbeddedConnectionRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EndpointRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EndpointResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EndpointsResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.server.ITInfrastructureRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
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

@Tag(name="IT Infrastructure OMAS",
     description="The IT Infrastructure OMAS provides APIs for tools and applications managing the IT infrastructure that supports the data assets.\n",
     externalDocs=@ExternalDocumentation(description="IT Infrastructure Open Metadata Access Service (OMAS)",
                                         url="https://egeria.odpi.org/open-metadata-implementation/access-services/it-infrastructure/"))

public class ITInfrastructureResource
{
    private ITInfrastructureRESTServices restAPI = new ITInfrastructureRESTServices();

    /**
     * Default constructor
     */
    public ITInfrastructureResource()
    {
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
                                           @RequestBody  MetadataSourceRequestBody requestBody)
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
                                           @RequestBody  MetadataSourceRequestBody requestBody)
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
                                      @RequestBody  MetadataSourceRequestBody requestBody)
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
                                      @RequestBody  MetadataSourceRequestBody requestBody)
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
                                                @RequestBody  MetadataSourceRequestBody requestBody)
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
                                             @RequestBody  MetadataSourceRequestBody requestBody)
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
                                         @RequestBody  MetadataSourceRequestBody requestBody)
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
                                               @RequestBody SearchStringRequestBody requestBody,
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
                                            @RequestBody  MetadataSourceRequestBody requestBody)
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
                                       @RequestBody  MetadataSourceRequestBody requestBody)
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

}
