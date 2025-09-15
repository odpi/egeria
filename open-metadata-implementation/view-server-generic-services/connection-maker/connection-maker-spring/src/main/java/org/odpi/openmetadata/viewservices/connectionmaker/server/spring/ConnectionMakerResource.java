/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.connectionmaker.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.viewservices.connectionmaker.server.ConnectionMakerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ConnectionMakerResource provides part of the server-side implementation of the Connection Maker OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")

@Tag(name="API: Connection Maker OMVS", description="The Connection Maker OMVS provides APIs for supporting the creation and editing of connections, connectorTypes and endpoints.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/connection-maker/overview/"))

public class ConnectionMakerResource
{
    private final ConnectionMakerRESTServices restAPI = new ConnectionMakerRESTServices();

    /**
     * Default constructor
     */
    public ConnectionMakerResource()
    {
    }


    /**
     * Create a connection.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the connection.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connections")

    @Operation(summary="createConnection",
            description="Create a connection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public GUIDResponse createConnection(@PathVariable String                               serverName,
                                         @PathVariable String             urlMarker,
                                         @RequestBody (required = false)
                                         NewElementRequestBody requestBody)
    {
        return restAPI.createConnection(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/from-template")
    @Operation(summary="createConnectionFromTemplate",
            description="Create a new metadata element to represent a connection using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public GUIDResponse createConnectionFromTemplate(@PathVariable
                                                     String              serverName,
                                                     @PathVariable String             urlMarker,
                                                     @RequestBody (required = false)
                                                     TemplateRequestBody requestBody)
    {
        return restAPI.createConnectionFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a connection.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param connectionGUID unique identifier of the connection (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connections/{connectionGUID}/update")
    @Operation(summary="updateConnection",
            description="Update the properties of a connection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse updateConnection(@PathVariable
                                         String                                  serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable
                                         String                                  connectionGUID,
                                         @RequestBody (required = false)
                                         UpdateElementRequestBody requestBody)
    {
        return restAPI.updateConnection(serverName, urlMarker, connectionGUID, requestBody);
    }


    /**
     * Create a ConnectionConnectorType relationship between a connection and a connector type.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID       unique identifier of the connection
     * @param connectorTypeGUID           unique identifier of the connector type
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connections/{connectionGUID}/connector-types/{connectorTypeGUID}/attach")
    @Operation(summary="linkConnectionConnectorType",
            description="Create a ConnectionConnectorType relationship between a connection and a connector type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse linkConnectionConnectorType(@PathVariable
                                                    String                     serverName,
                                                    @PathVariable String             urlMarker,
                                                    @PathVariable
                                                    String connectionGUID,
                                                    @PathVariable
                                                    String connectorTypeGUID,
                                                    @RequestBody (required = false)
                                                        NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkConnectionConnectorType(serverName, urlMarker, connectionGUID, connectorTypeGUID, requestBody);
    }


    /**
     * Remove the ConnectionConnectorType relationship between a connection and a connector type.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID       unique identifier of the connection
     * @param connectorTypeGUID           unique identifier of the connector type
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connections/{connectionGUID}/connector-types/{connectorTypeGUID}/detach")
    @Operation(summary="detachConnectionConnectorType",
            description="Remove the ConnectionConnectorType relationship between a connection and a connector type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse detachConnectionConnectorType(@PathVariable
                                                      String                    serverName,
                                                      @PathVariable String             urlMarker,
                                                      @PathVariable
                                                      String connectionGUID,
                                                      @PathVariable
                                                      String connectorTypeGUID,
                                                      @RequestBody (required = false)
                                                          DeleteRequestBody requestBody)
    {
        return restAPI.detachConnectionConnectorType(serverName, urlMarker, connectionGUID, connectorTypeGUID, requestBody);
    }


    /**
     * Create a ConnectToEndpoint relationship between a connection and an endpoint.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID unique identifier of the connection
     * @param endpointGUID unique identifier of the endpoint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connections/{connectionGUID}/endpoints/{endpointGUID}/attach")
    @Operation(summary="linkConnectionEndpoint",
            description="Create a ConnectToEndpoint relationship between a connection and an endpoint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse linkConnectionEndpoint(@PathVariable
                                               String                     serverName,
                                               @PathVariable String             urlMarker,
                                               @PathVariable
                                               String connectionGUID,
                                               @PathVariable
                                               String endpointGUID,
                                               @RequestBody (required = false)
                                                   NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkConnectionEndpoint(serverName, urlMarker, connectionGUID, endpointGUID, requestBody);
    }


    /**
     * Remove the ConnectToEndpoint relationship between a connection and an endpoint.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID          unique identifier of the connection
     * @param endpointGUID          unique identifier of the endpoint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connections/{connectionGUID}/endpoints/{endpointGUID}/detach")
    @Operation(summary="detachConnectionEndpoint",
            description="Remove the ConnectToEndpoint relationship between a connection and an endpoint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse detachConnectionEndpoint(@PathVariable
                                                 String                    serverName,
                                                 @PathVariable String             urlMarker,
                                                 @PathVariable
                                                 String connectionGUID,
                                                 @PathVariable
                                                 String endpointGUID,
                                                 @RequestBody (required = false)
                                                     DeleteRequestBody requestBody)
    {
        return restAPI.detachConnectionEndpoint(serverName, urlMarker, connectionGUID, endpointGUID, requestBody);
    }


    /**
     * Create an EmbeddedConnection relationship between a virtual connection and an embedded connection.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID unique identifier of the virtual connection
     * @param embeddedConnectionGUID unique identifier of the embedded connection
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connections/{connectionGUID}/embedded-connections/{embeddedConnectionGUID}/attach")
    @Operation(summary="linkEmbeddedConnection",
            description="Create an EmbeddedConnection relationship between a virtual connection and an embedded connection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse linkEmbeddedConnection(@PathVariable
                                               String                     serverName,
                                               @PathVariable String             urlMarker,
                                               @PathVariable
                                               String connectionGUID,
                                               @PathVariable
                                               String embeddedConnectionGUID,
                                               @RequestBody (required = false)
                                                   NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkEmbeddedConnection(serverName, urlMarker, connectionGUID, embeddedConnectionGUID, requestBody);
    }


    /**
     * Remove an EmbeddedConnection relationship between a virtual connection and an embedded connection.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID unique identifier of the virtual connection
     * @param embeddedConnectionGUID unique identifier of the embedded connection
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connections/{connectionGUID}/embedded-connections/{embeddedConnectionGUID}/detach")
    @Operation(summary="detachEmbeddedConnection",
            description="Remove the ConnectToEndpoint relationship between a connection and an endpoint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse detachEmbeddedConnection(@PathVariable
                                                 String                    serverName,
                                                 @PathVariable String             urlMarker,
                                                 @PathVariable
                                                 String connectionGUID,
                                                 @PathVariable
                                                 String embeddedConnectionGUID,
                                                 @RequestBody (required = false)
                                                     DeleteRequestBody requestBody)
    {
        return restAPI.detachEmbeddedConnection(serverName, urlMarker, connectionGUID, embeddedConnectionGUID, requestBody);
    }


    /**
     * Attach an asset to a connection.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param assetGUID       unique identifier of the asset
     * @param connectionGUID            unique identifier of the connection
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/connections/{connectionGUID}/attach")
    @Operation(summary="linkAssetToConnection",
            description="Attach an asset to a connection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse linkAssetToConnection(@PathVariable
                                              String                     serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String assetGUID,
                                              @PathVariable
                                              String connectionGUID,
                                              @RequestBody (required = false)
                                                  NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkAssetToConnection(serverName, urlMarker, assetGUID, connectionGUID, requestBody);
    }


    /**
     * Detach an asset from a connection.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param assetGUID       unique identifier of the asset
     * @param connectionGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/connections/{connectionGUID}/detach")
    @Operation(summary="detachAssetFromConnection",
            description="Detach an asset from a connection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse detachAssetFromConnection(@PathVariable
                                                  String                    serverName,
                                                  @PathVariable String             urlMarker,
                                                  @PathVariable
                                                  String assetGUID,
                                                  @PathVariable
                                                  String connectionGUID,
                                                  @RequestBody (required = false)
                                                      DeleteRequestBody requestBody)
    {
        return restAPI.detachAssetFromConnection(serverName, urlMarker, assetGUID, connectionGUID, requestBody);
    }


    /**
     * Attach an endpoint to an infrastructure asset.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param assetGUID             unique identifier of the infrastructure asset
     * @param endpointGUID            unique identifier of the endpoint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/endpoints/{endpointGUID}/attach")
    @Operation(summary="linkEndpointToITAsset",
            description="Attach an endpoint to an infrastructure asset.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse linkEndpointToITAsset(@PathVariable
                                              String                     serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String assetGUID,
                                              @PathVariable
                                              String endpointGUID,
                                              @RequestBody (required = false)
                                                  NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkEndpointToITAsset(serverName, urlMarker, assetGUID, endpointGUID, requestBody);
    }


    /**
     * Detach an endpoint from an infrastructure asset.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param assetGUID            unique identifier of the infrastructure asset
     * @param endpointGUID       unique identifier of the endpoint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/connections/{endpointGUID}/detach")
    @Operation(summary="detachEndpointFromITAsset",
            description="Detach an endpoint from an infrastructure asset.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse detachEndpointFromITAsset(@PathVariable
                                                  String                    serverName,
                                                  @PathVariable String             urlMarker,
                                                  @PathVariable
                                                  String assetGUID,
                                                  @PathVariable
                                                  String endpointGUID,
                                                  @RequestBody (required = false)
                                                      DeleteRequestBody requestBody)
    {
        return restAPI.detachEndpointFromITAsset(serverName, urlMarker, assetGUID, endpointGUID, requestBody);
    }

    /**
     * Delete a connection.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID  unique identifier of the element to delete* @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connections/{connectionGUID}/delete")
    @Operation(summary="deleteConnection",
            description="Delete a connection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public VoidResponse deleteConnection(@PathVariable
                                         String                    serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable
                                         String                    connectionGUID,
                                         @RequestBody (required = false)
                                             DeleteRequestBody requestBody)
    {
        return restAPI.deleteConnection(serverName, urlMarker, connectionGUID, requestBody);
    }


    /**
     * Returns the list of connections with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/by-name")
    @Operation(summary="getConnectionsByName",
            description="Returns the list of connections with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public OpenMetadataRootElementsResponse getConnectionsByName(@PathVariable
                                                                 String            serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @RequestBody (required = false)
                                                                 FilterRequestBody requestBody)
    {
        return restAPI.getConnectionsByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of connection metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/by-search-string")
    @Operation(summary="findConnections",
            description="Retrieve the list of connection metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public OpenMetadataRootElementsResponse findConnections(@PathVariable
                                                            String                  serverName,
                                                            @PathVariable String             urlMarker,
                                                            @RequestBody (required = false)
                                                            SearchStringRequestBody requestBody)
    {
        return restAPI.findConnections(serverName, urlMarker, requestBody);
    }


    /**
     * Return the properties of a specific connection.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param connectionGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connections/{connectionGUID}/retrieve")
    @Operation(summary="getConnectionByGUID",
            description="Return the properties of a specific connection.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connection"))

    public OpenMetadataRootElementResponse getConnectionByGUID(@PathVariable
                                                               String             serverName,
                                                               @PathVariable String             urlMarker,
                                                               @PathVariable
                                                               String             connectionGUID,
                                                               @RequestBody (required = false)
                                                                   GetRequestBody requestBody)
    {
        return restAPI.getConnectionByGUID(serverName, urlMarker, connectionGUID, requestBody);
    }


    /**
     * Create a connectorType.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the connectorType.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connector-types")

    @Operation(summary="createConnectorType",
            description="Create a connectorType.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connector-type"))

    public GUIDResponse createConnectorType(@PathVariable String                               serverName,
                                            @PathVariable String             urlMarker,
                                            @RequestBody (required = false)
                                            NewElementRequestBody requestBody)
    {
        return restAPI.createConnectorType(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a connectorType using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/from-template")
    @Operation(summary="createConnectorTypeFromTemplate",
            description="Create a new metadata element to represent a connectorType using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connector-type"))

    public GUIDResponse createConnectorTypeFromTemplate(@PathVariable
                                                        String              serverName,
                                                        @PathVariable String             urlMarker,
                                                        @RequestBody (required = false)
                                                        TemplateRequestBody requestBody)
    {
        return restAPI.createConnectorTypeFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a connectorType.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param connectorTypeGUID unique identifier of the connectorType (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connector-types/{connectorTypeGUID}/update")
    @Operation(summary="updateConnectorType",
            description="Update the properties of a connectorType.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connector-type"))

    public VoidResponse updateConnectorType(@PathVariable
                                            String                                  serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable
                                            String                                  connectorTypeGUID,
                                            @RequestBody (required = false)
                                            UpdateElementRequestBody requestBody)
    {
        return restAPI.updateConnectorType(serverName, urlMarker, connectorTypeGUID, requestBody);
    }


    /**
     * Delete a connectorType.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectorTypeGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/connector-types/{connectorTypeGUID}/delete")
    @Operation(summary="deleteConnectorType",
            description="Delete a connectorType.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connector-type"))

    public VoidResponse deleteConnectorType(@PathVariable
                                            String                    serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable
                                            String                    connectorTypeGUID,
                                            @RequestBody (required = false)
                                                DeleteRequestBody requestBody)
    {
        return restAPI.deleteConnectorType(serverName, urlMarker, connectorTypeGUID, requestBody);
    }


    /**
     * Returns the list of connectorTypes with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/by-name")
    @Operation(summary="getConnectorTypesByName",
            description="Returns the list of connector types with a particular value in either qualifiedName or displayName.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connector-type"))

    public OpenMetadataRootElementsResponse getConnectorTypesByName(@PathVariable
                                                                    String            serverName,
                                                                    @PathVariable String             urlMarker,
                                                                    @RequestBody (required = false)
                                                                    FilterRequestBody requestBody)
    {
        return restAPI.getConnectorTypesByName(serverName, urlMarker, requestBody);
    }


    /**
     * Returns the list of connector types with a particular connector provider class name.
     * Provide the name of the connector provider's class name (including package; but without .class)
     * in the filter.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/by-connector-provider-class-name")
    @Operation(summary="getConnectorTypesByConnectorProvider",
            description="Returns the list of connectorTypes with a particular connector provider class name. Provide the name of the connector provider's class name (including package; but without .class).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connector-type"))

    public OpenMetadataRootElementsResponse getConnectorTypesByConnectorProvider(@PathVariable
                                                                    String            serverName,
                                                                    @PathVariable String             urlMarker,
                                                                    @RequestBody (required = false)
                                                                    FilterRequestBody requestBody)
    {
        return restAPI.getConnectorTypesByConnectorProvider(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of connectorType metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/by-search-string")
    @Operation(summary="findConnectorTypes",
            description="Retrieve the list of connectorType metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connector-type"))

    public OpenMetadataRootElementsResponse findConnectorTypes(@PathVariable
                                                               String                  serverName,
                                                               @PathVariable String             urlMarker,
                                                               @RequestBody (required = false)
                                                               SearchStringRequestBody requestBody)
    {
        return restAPI.findConnectorTypes(serverName, urlMarker,  requestBody);
    }


    /**
     * Return the properties of a specific connectorType.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param connectorTypeGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/connector-types/{connectorTypeGUID}/retrieve")
    @Operation(summary="getConnectorTypeByGUID",
            description="Return the properties of a specific connectorType.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/connector-type"))

    public OpenMetadataRootElementResponse getConnectorTypeByGUID(@PathVariable
                                                                  String             serverName,
                                                                  @PathVariable String             urlMarker,
                                                                  @PathVariable
                                                                  String             connectorTypeGUID,
                                                                  @RequestBody (required = false)
                                                                      GetRequestBody requestBody)
    {
        return restAPI.getConnectorTypeByGUID(serverName, urlMarker, connectorTypeGUID, requestBody);
    }


    /**
     * Create an endpoint.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the endpoint.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/endpoints")

    @Operation(summary="createEndpoint",
            description="Create a endpoint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/endpoint"))

    public GUIDResponse createEndpoint(@PathVariable String                               serverName,
                                       @PathVariable String             urlMarker,
                                       @RequestBody (required = false)
                                       NewElementRequestBody requestBody)
    {
        return restAPI.createEndpoint(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent an endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/from-template")
    @Operation(summary="createEndpointFromTemplate",
            description="Create a new metadata element to represent a endpoint using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/endpoint"))

    public GUIDResponse createEndpointFromTemplate(@PathVariable
                                                   String              serverName,
                                                   @PathVariable String             urlMarker,
                                                   @RequestBody (required = false)
                                                   TemplateRequestBody requestBody)
    {
        return restAPI.createEndpointFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of an endpoint.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param endpointGUID unique identifier of the endpoint (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/endpoints/{endpointGUID}/update")
    @Operation(summary="updateEndpoint",
            description="Update the properties of a endpoint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/endpoint"))

    public VoidResponse updateEndpoint(@PathVariable
                                       String                                  serverName,
                                       @PathVariable String             urlMarker,
                                       @PathVariable
                                       String                                  endpointGUID,
                                       @RequestBody (required = false)
                                       UpdateElementRequestBody requestBody)
    {
        return restAPI.updateEndpoint(serverName, urlMarker, endpointGUID, requestBody);
    }


    /**
     * Delete a endpoint.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param endpointGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/endpoints/{endpointGUID}/delete")
    @Operation(summary="deleteEndpoint",
            description="Delete a endpoint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/endpoint"))

    public VoidResponse deleteEndpoint(@PathVariable
                                       String                    serverName,
                                       @PathVariable String             urlMarker,
                                       @PathVariable
                                       String                    endpointGUID,
                                       @RequestBody (required = false)
                                           DeleteRequestBody requestBody)
    {
        return restAPI.deleteEndpoint(serverName, urlMarker, endpointGUID, requestBody);
    }


    /**
     * Returns the list of endpoints with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/by-name")
    @Operation(summary="getEndpointsByName",
            description="Returns the list of endpoints with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/endpoint"))

    public OpenMetadataRootElementsResponse getEndpointsByName(@PathVariable
                                                               String            serverName,
                                                               @PathVariable String             urlMarker,
                                                               @RequestBody (required = false)
                                                               FilterRequestBody requestBody)
    {
        return restAPI.getEndpointsByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching networkAddress.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/by-network-address")
    @Operation(summary="getEndpointsByNetworkAddress",
            description="Retrieve the list of endpoint metadata elements with a matching networkAddress." +
                    "  There are no wildcards supported on this request.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/endpoint"))

    public OpenMetadataRootElementsResponse getEndpointsByNetworkAddress(@PathVariable
                                                                         String            serverName,
                                                                         @PathVariable String             urlMarker,
                                                                         @RequestBody (required = false)
                                                                         FilterRequestBody requestBody)
    {
        return restAPI.getEndpointsByNetworkAddress(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of endpoint metadata elements that are attached to a specific infrastructure element.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param infrastructureGUID element to search for
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{infrastructureGUID}/endpoints/retrieve")
    @Operation(summary="getEndpointsForInfrastructure",
            description="Retrieve the list of endpoint metadata elements that are attached to a specific infrastructure element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/endpoint"))

    public OpenMetadataRootElementsResponse getEndpointsForInfrastructure(@PathVariable String            serverName,
                                                                          @PathVariable String             urlMarker,
                                                                          @PathVariable String              infrastructureGUID,
                                                                          @RequestBody (required = false)
                                                                          ResultsRequestBody requestBody)
    {
        return restAPI.getEndpointsForInfrastructure(serverName, urlMarker, infrastructureGUID, requestBody);
    }


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/by-search-string")
    @Operation(summary="findEndpoints",
            description="Retrieve the list of endpoint metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/endpoint"))

    public OpenMetadataRootElementsResponse findEndpoints(@PathVariable
                                                          String                  serverName,
                                                          @PathVariable String             urlMarker,
                                                          @RequestBody (required = false)
                                                          SearchStringRequestBody requestBody)
    {
        return restAPI.findEndpoints(serverName, urlMarker, requestBody);
    }


    /**
     * Return the properties of a specific endpoint.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param endpointGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/{endpointGUID}/retrieve")
    @Operation(summary="getEndpointByGUID",
            description="Return the properties of a specific endpoint.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/endpoint"))

    public OpenMetadataRootElementResponse getEndpointByGUID(@PathVariable
                                                             String             serverName,
                                                             @PathVariable String             urlMarker,
                                                             @PathVariable
                                                             String             endpointGUID,
                                                             @RequestBody (required = false)
                                                                 GetRequestBody requestBody)
    {
        return restAPI.getEndpointByGUID(serverName, urlMarker, endpointGUID, requestBody);
    }
}