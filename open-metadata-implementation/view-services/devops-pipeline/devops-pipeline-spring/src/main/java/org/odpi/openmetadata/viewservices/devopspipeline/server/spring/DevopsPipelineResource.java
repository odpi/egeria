/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.devopspipeline.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.devopspipeline.server.DevopsPipelineRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DevopsPipelineResource provides part of the server-side implementation of the Devops Pipeline OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/devops-pipeline")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="API: Devops Pipeline", description="Supports a devops engineer to maintain the metadata about the changing digital resources being deployed through devops pipelines.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/devops-pipeline/overview/"))

public class DevopsPipelineResource
{
    private final DevopsPipelineRESTServices restAPI = new DevopsPipelineRESTServices();

    /**
     * Default constructor
     */
    public DevopsPipelineResource()
    {
    }

    /**
     * Create a storage volume.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/storage-volumes")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createStorageVolume",
            description="Create a storage volume.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/storage-volume"))

    public GUIDResponse createStorageVolume(@PathVariable String serverName,
                                            @RequestBody (required = false)
                                            NewElementRequestBody requestBody)
    {
        return restAPI.createStorageVolume(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a storage volume using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/storage-volumes/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createStorageVolumeFromTemplate",
            description="Create a new metadata element to represent a storage volume using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/storage-volume"))

    public GUIDResponse createStorageVolumeFromTemplate(@PathVariable String serverName,
                                                        @RequestBody (required = false)
                                                        TemplateRequestBody requestBody)
    {
        return restAPI.createStorageVolumeFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a storage volume.
     *
     * @param serverName name of the server to route the request to
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/storage-volumes/{storageVolumeGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateStorageVolume",
            description="Update the properties of a storage volume.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/storage-volume"))

    public BooleanResponse updateStorageVolume(@PathVariable String serverName,
                                               @PathVariable String storageVolumeGUID,
                                               @RequestBody (required = false)
                                               UpdateElementRequestBody requestBody)
    {
        return restAPI.updateStorageVolume(serverName, storageVolumeGUID, requestBody);
    }


    /**
     * Delete a storage volume.
     *
     * @param serverName name of the server to route the request to
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/storage-volumes/{storageVolumeGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteStorageVolume",
            description="Delete a storage volume.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/storage-volume"))

    public VoidResponse deleteStorageVolume(@PathVariable String serverName,
                                            @PathVariable String storageVolumeGUID,
                                            @RequestBody (required = false)
                                            DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteStorageVolume(serverName, storageVolumeGUID, requestBody);
    }


    /**
     * Returns the list of storage volumes with a particular name.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/storage-volumes/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getStorageVolumesByName",
            description="Returns the list of storage volumes with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/storage-volume"))

    public OpenMetadataRootElementsResponse getStorageVolumesByName(@PathVariable String serverName,
                                                                    @RequestBody (required = false)
                                                                    FilterRequestBody requestBody)
    {
        return restAPI.getStorageVolumesByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of storage volume metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/storage-volumes/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findStorageVolumes",
            description="Retrieve the list of storage volume metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/storage-volume"))

    public OpenMetadataRootElementsResponse findStorageVolumes(@PathVariable String serverName,
                                                               @RequestBody (required = false)
                                                               SearchStringRequestBody requestBody)
    {
        return restAPI.findStorageVolumes(serverName, requestBody);
    }


    /**
     * Return the properties of a specific storage volume.
     *
     * @param serverName name of the server to route the request to
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/storage-volumes/{storageVolumeGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getStorageVolumeByGUID",
            description="Return the properties of a specific storage volume.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/storage-volume"))

    public OpenMetadataRootElementResponse getStorageVolumeByGUID(@PathVariable String serverName,
                                                                  @PathVariable String storageVolumeGUID,
                                                                  @RequestBody (required = false)
                                                                  GetRequestBody requestBody)
    {
        return restAPI.getStorageVolumeByGUID(serverName, storageVolumeGUID, requestBody);
    }



    /**
     * Attach a storage volume to the IT infrastructure that it provides storage for.
     *
     * @param serverName name of the server to route the request to
     * @param itInfrastructureGUID unique identifier of the element that the storage volume is attached to
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/it-infrastructure/{itInfrastructureGUID}/attached-storage/{storageVolumeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkAttachedStorage",
            description="Attach a storage volume to the IT infrastructure that it provides storage for.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/storage-volume"))

    public VoidResponse linkAttachedStorage(@PathVariable String serverName,
                                            @PathVariable String itInfrastructureGUID,
                                            @PathVariable String storageVolumeGUID,
                                            @RequestBody (required = false)
                                            NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkAttachedStorage(serverName, itInfrastructureGUID, storageVolumeGUID, requestBody);
    }



    /**
     * Detach a storage volume from the IT infrastructure that it provided storage for.
     *
     * @param serverName name of the server to route the request to
     * @param itInfrastructureGUID unique identifier of the element that the storage volume is attached to
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/it-infrastructure/{itInfrastructureGUID}/attached-storage/{storageVolumeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachAttachedStorage",
            description="Detach a storage volume from the IT infrastructure that it provided storage for.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/storage-volume"))

    public VoidResponse detachAttachedStorage(@PathVariable String serverName,
                                              @PathVariable String itInfrastructureGUID,
                                              @PathVariable String storageVolumeGUID,
                                              @RequestBody (required = false)
                                              DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachAttachedStorage(serverName, itInfrastructureGUID, storageVolumeGUID, requestBody);
    }



    /**
     * Attach a data store to the storage volume that its data is stored on.
     *
     * @param serverName name of the server to route the request to
     * @param dataStoreGUID unique identifier of the data store
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-stores/{dataStoreGUID}/stored-on/{storageVolumeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkStoredOn",
            description="Attach a data store to the storage volume that its data is stored on.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/storage-volume"))

    public VoidResponse linkStoredOn(@PathVariable String serverName,
                                     @PathVariable String dataStoreGUID,
                                     @PathVariable String storageVolumeGUID,
                                     @RequestBody (required = false)
                                     NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkStoredOn(serverName, dataStoreGUID, storageVolumeGUID, requestBody);
    }



    /**
     * Detach a data store from the storage volume that its data was stored on.
     *
     * @param serverName name of the server to route the request to
     * @param dataStoreGUID unique identifier of the data store
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-stores/{dataStoreGUID}/stored-on/{storageVolumeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachStoredOn",
            description="Detach a data store from the storage volume that its data was stored on.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/storage-volume"))

    public VoidResponse detachStoredOn(@PathVariable String serverName,
                                       @PathVariable String dataStoreGUID,
                                       @PathVariable String storageVolumeGUID,
                                       @RequestBody (required = false)
                                       DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachStoredOn(serverName, dataStoreGUID, storageVolumeGUID, requestBody);
    }

    /**
     * Create a network.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/networks")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createNetwork",
            description="Create a network.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public GUIDResponse createNetwork(@PathVariable String serverName,
                                      @RequestBody (required = false)
                                      NewElementRequestBody requestBody)
    {
        return restAPI.createNetwork(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a network using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/networks/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createNetworkFromTemplate",
            description="Create a new metadata element to represent a network using an existing metadata element as a template.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public GUIDResponse createNetworkFromTemplate(@PathVariable String serverName,
                                                  @RequestBody (required = false)
                                                  TemplateRequestBody requestBody)
    {
        return restAPI.createNetworkFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a network.
     *
     * @param serverName name of the server to route the request to
     * @param networkGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/networks/{networkGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateNetwork",
            description="Update the properties of a network.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public BooleanResponse updateNetwork(@PathVariable String serverName,
                                         @PathVariable String networkGUID,
                                         @RequestBody (required = false)
                                         UpdateElementRequestBody requestBody)
    {
        return restAPI.updateNetwork(serverName, networkGUID, requestBody);
    }


    /**
     * Delete a network.
     *
     * @param serverName name of the server to route the request to
     * @param networkGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/networks/{networkGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteNetwork",
            description="Delete a network.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public VoidResponse deleteNetwork(@PathVariable String serverName,
                                      @PathVariable String networkGUID,
                                      @RequestBody (required = false)
                                      DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteNetwork(serverName, networkGUID, requestBody);
    }


    /**
     * Returns the list of networks with a particular name.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/networks/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getNetworksByName",
            description="Returns the list of networks with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public OpenMetadataRootElementsResponse getNetworksByName(@PathVariable String serverName,
                                                              @RequestBody (required = false)
                                                              FilterRequestBody requestBody)
    {
        return restAPI.getNetworksByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of network metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/networks/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findNetworks",
            description="Retrieve the list of network metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public OpenMetadataRootElementsResponse findNetworks(@PathVariable String serverName,
                                                         @RequestBody (required = false)
                                                         SearchStringRequestBody requestBody)
    {
        return restAPI.findNetworks(serverName, requestBody);
    }


    /**
     * Return the properties of a specific network.
     *
     * @param serverName name of the server to route the request to
     * @param networkGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/networks/{networkGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getNetworkByGUID",
            description="Return the properties of a specific network.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public OpenMetadataRootElementResponse getNetworkByGUID(@PathVariable String serverName,
                                                            @PathVariable String networkGUID,
                                                            @RequestBody (required = false)
                                                            GetRequestBody requestBody)
    {
        return restAPI.getNetworkByGUID(serverName, networkGUID, requestBody);
    }


    /**
     * Create a network gateway.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/network-gateways")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createNetworkGateway",
            description="Create a network gateway.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public GUIDResponse createNetworkGateway(@PathVariable String serverName,
                                             @RequestBody (required = false)
                                             NewElementRequestBody requestBody)
    {
        return restAPI.createNetworkGateway(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a network gateway using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/network-gateways/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createNetworkGatewayFromTemplate",
            description="Create a new metadata element to represent a network gateway using an existing metadata element as a template.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public GUIDResponse createNetworkGatewayFromTemplate(@PathVariable String serverName,
                                                         @RequestBody (required = false)
                                                         TemplateRequestBody requestBody)
    {
        return restAPI.createNetworkGatewayFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a network gateway.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/network-gateways/{networkGatewayGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateNetworkGateway",
            description="Update the properties of a network gateway.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public BooleanResponse updateNetworkGateway(@PathVariable String serverName,
                                                @PathVariable String networkGatewayGUID,
                                                @RequestBody (required = false)
                                                UpdateElementRequestBody requestBody)
    {
        return restAPI.updateNetworkGateway(serverName, networkGatewayGUID, requestBody);
    }


    /**
     * Delete a network gateway.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/network-gateways/{networkGatewayGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteNetworkGateway",
            description="Delete a network gateway.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public VoidResponse deleteNetworkGateway(@PathVariable String serverName,
                                             @PathVariable String networkGatewayGUID,
                                             @RequestBody (required = false)
                                             DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteNetworkGateway(serverName, networkGatewayGUID, requestBody);
    }


    /**
     * Returns the list of network gateways with a particular name.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/network-gateways/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getNetworkGatewaysByName",
            description="Returns the list of network gateways with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public OpenMetadataRootElementsResponse getNetworkGatewaysByName(@PathVariable String serverName,
                                                                     @RequestBody (required = false)
                                                                     FilterRequestBody requestBody)
    {
        return restAPI.getNetworkGatewaysByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of network gateway metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/network-gateways/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findNetworkGateways",
            description="Retrieve the list of network gateway metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public OpenMetadataRootElementsResponse findNetworkGateways(@PathVariable String serverName,
                                                                @RequestBody (required = false)
                                                                SearchStringRequestBody requestBody)
    {
        return restAPI.findNetworkGateways(serverName, requestBody);
    }


    /**
     * Return the properties of a specific network gateway.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/network-gateways/{networkGatewayGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getNetworkGatewayByGUID",
            description="Return the properties of a specific network gateway.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public OpenMetadataRootElementResponse getNetworkGatewayByGUID(@PathVariable String serverName,
                                                                   @PathVariable String networkGatewayGUID,
                                                                   @RequestBody (required = false)
                                                                   GetRequestBody requestBody)
    {
        return restAPI.getNetworkGatewayByGUID(serverName, networkGatewayGUID, requestBody);
    }


    /**
     * Attach an endpoint to the network that it is visible in.
     *
     * @param serverName name of the server to route the request to
     * @param endpointGUID unique identifier
     * @param networkGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/{endpointGUID}/visible-endpoints/{networkGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkVisibleEndpoint",
            description="Attach an endpoint to the network that it is visible in.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public VoidResponse linkVisibleEndpoint(@PathVariable String serverName,
                                            @PathVariable String endpointGUID,
                                            @PathVariable String networkGUID,
                                            @RequestBody (required = false)
                                            NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkVisibleEndpoint(serverName, endpointGUID, networkGUID, requestBody);
    }


    /**
     * Detach an endpoint from the network that it was visible in.
     *
     * @param serverName name of the server to route the request to
     * @param endpointGUID unique identifier
     * @param networkGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/endpoints/{endpointGUID}/visible-endpoints/{networkGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachVisibleEndpoint",
            description="Detach an endpoint from the network that it was visible in.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public VoidResponse detachVisibleEndpoint(@PathVariable String serverName,
                                              @PathVariable String endpointGUID,
                                              @PathVariable String networkGUID,
                                              @RequestBody (required = false)
                                              DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachVisibleEndpoint(serverName, endpointGUID, networkGUID, requestBody);
    }


    /**
     * Attach a network gateway to a network that it connects to.  This multi-link relationship always creates a new relationship and returns its unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayGUID unique identifier
     * @param networkGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/network-gateways/{networkGatewayGUID}/network-gateway-links/{networkGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkNetworkGateway",
            description="Attach a network gateway to a network that it connects to.  This multi-link relationship always creates a new relationship and returns its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public GUIDResponse linkNetworkGateway(@PathVariable String serverName,
                                           @PathVariable String networkGatewayGUID,
                                           @PathVariable String networkGUID,
                                           @RequestBody (required = false)
                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkNetworkGateway(serverName, networkGatewayGUID, networkGUID, requestBody);
    }


    /**
     * Update the properties of a network gateway link.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayLinkGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/network-gateway-links/{networkGatewayLinkGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateNetworkGatewayLink",
            description="Update the properties of a network gateway link.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public VoidResponse updateNetworkGatewayLink(@PathVariable String serverName,
                                                 @PathVariable String networkGatewayLinkGUID,
                                                 @RequestBody (required = false)
                                                 UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateNetworkGatewayLink(serverName, networkGatewayLinkGUID, requestBody);
    }


    /**
     * Detach a network gateway from a network that it connected to.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayLinkGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/network-gateway-links/{networkGatewayLinkGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachNetworkGateway",
            description="Detach a network gateway from a network that it connected to.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/network"))

    public VoidResponse detachNetworkGateway(@PathVariable String serverName,
                                             @PathVariable String networkGatewayLinkGUID,
                                             @RequestBody (required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachNetworkGateway(serverName, networkGatewayLinkGUID, requestBody);
    }


    /**
     * Attach a host to the host cluster that manages it.
     *
     * @param serverName name of the server to route the request to
     * @param hostClusterGUID unique identifier of the host cluster
     * @param hostGUID unique identifier of the host that is managed by the cluster
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/host-clusters/{hostClusterGUID}/host-cluster-members/{hostGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkHostClusterMember",
            description="Attach a host to the host cluster that manages it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/host"))

    public VoidResponse linkHostClusterMember(@PathVariable String                     serverName,
                                              @PathVariable String                     hostClusterGUID,
                                              @PathVariable String                     hostGUID,
                                              @RequestBody (required = false)
                                              NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkHostClusterMember(serverName, hostClusterGUID, hostGUID, requestBody);
    }


    /**
     * Detach a host from the host cluster that managed it.
     *
     * @param serverName name of the server to route the request to
     * @param hostClusterGUID unique identifier of the host cluster
     * @param hostGUID unique identifier of the host that was managed by the cluster
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/host-clusters/{hostClusterGUID}/host-cluster-members/{hostGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachHostClusterMember",
            description="Detach a host from the host cluster that managed it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/host"))

    public VoidResponse detachHostClusterMember(@PathVariable String                        serverName,
                                                @PathVariable String                        hostClusterGUID,
                                                @PathVariable String                        hostGUID,
                                                @RequestBody (required = false)
                                                DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachHostClusterMember(serverName, hostClusterGUID, hostGUID, requestBody);
    }

    /**
     * Create an operating platform.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/operating-platforms")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createOperatingPlatform",
            description="Create an operating platform.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public GUIDResponse createOperatingPlatform(@PathVariable String serverName,
                                                @RequestBody (required = false)
                                                NewElementRequestBody requestBody)
    {
        return restAPI.createOperatingPlatform(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent an operating platform using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/operating-platforms/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createOperatingPlatformFromTemplate",
            description="Create a new metadata element to represent an operating platform using an existing metadata element as a template.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public GUIDResponse createOperatingPlatformFromTemplate(@PathVariable String serverName,
                                                            @RequestBody (required = false)
                                                            TemplateRequestBody requestBody)
    {
        return restAPI.createOperatingPlatformFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of an operating platform.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/operating-platforms/{operatingPlatformGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateOperatingPlatform",
            description="Update the properties of an operating platform.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public BooleanResponse updateOperatingPlatform(@PathVariable String serverName,
                                                   @PathVariable String operatingPlatformGUID,
                                                   @RequestBody (required = false)
                                                   UpdateElementRequestBody requestBody)
    {
        return restAPI.updateOperatingPlatform(serverName, operatingPlatformGUID, requestBody);
    }


    /**
     * Delete an operating platform.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/operating-platforms/{operatingPlatformGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteOperatingPlatform",
            description="Delete an operating platform.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public VoidResponse deleteOperatingPlatform(@PathVariable String serverName,
                                                @PathVariable String operatingPlatformGUID,
                                                @RequestBody (required = false)
                                                DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteOperatingPlatform(serverName, operatingPlatformGUID, requestBody);
    }


    /**
     * Returns the list of operating platforms with a particular name.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/operating-platforms/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getOperatingPlatformsByName",
            description="Returns the list of operating platforms with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public OpenMetadataRootElementsResponse getOperatingPlatformsByName(@PathVariable String serverName,
                                                                        @RequestBody (required = false)
                                                                        FilterRequestBody requestBody)
    {
        return restAPI.getOperatingPlatformsByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of operating platform metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/operating-platforms/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findOperatingPlatforms",
            description="Retrieve the list of operating platform metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public OpenMetadataRootElementsResponse findOperatingPlatforms(@PathVariable String serverName,
                                                                   @RequestBody (required = false)
                                                                   SearchStringRequestBody requestBody)
    {
        return restAPI.findOperatingPlatforms(serverName, requestBody);
    }


    /**
     * Return the properties of a specific operating platform.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/operating-platforms/{operatingPlatformGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getOperatingPlatformByGUID",
            description="Return the properties of a specific operating platform.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public OpenMetadataRootElementResponse getOperatingPlatformByGUID(@PathVariable String serverName,
                                                                      @PathVariable String operatingPlatformGUID,
                                                                      @RequestBody (required = false)
                                                                      GetRequestBody requestBody)
    {
        return restAPI.getOperatingPlatformByGUID(serverName, operatingPlatformGUID, requestBody);
    }


    /**
     * Attach an operating platform to the IT infrastructure that it is installed on.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier
     * @param itInfrastructureGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/operating-platforms/{operatingPlatformGUID}/operating-platform-use/{itInfrastructureGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkOperatingPlatformUse",
            description="Attach an operating platform to the IT infrastructure that it is installed on.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public VoidResponse linkOperatingPlatformUse(@PathVariable String serverName,
                                                 @PathVariable String operatingPlatformGUID,
                                                 @PathVariable String itInfrastructureGUID,
                                                 @RequestBody (required = false)
                                                 NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkOperatingPlatformUse(serverName, operatingPlatformGUID, itInfrastructureGUID, requestBody);
    }


    /**
     * Detach an operating platform from the IT infrastructure that it was installed on.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier
     * @param itInfrastructureGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/operating-platforms/{operatingPlatformGUID}/operating-platform-use/{itInfrastructureGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachOperatingPlatformUse",
            description="Detach an operating platform from the IT infrastructure that it was installed on.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public VoidResponse detachOperatingPlatformUse(@PathVariable String serverName,
                                                   @PathVariable String operatingPlatformGUID,
                                                   @PathVariable String itInfrastructureGUID,
                                                   @RequestBody (required = false)
                                                   DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachOperatingPlatformUse(serverName, operatingPlatformGUID, itInfrastructureGUID, requestBody);
    }


    /**
     * Attach an operating platform to the collection of software packages that it is packaged with.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier
     * @param collectionGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/operating-platforms/{operatingPlatformGUID}/operating-platform-manifests/{collectionGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkOperatingPlatformManifest",
            description="Attach an operating platform to the collection of software packages that it is packaged with.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public VoidResponse linkOperatingPlatformManifest(@PathVariable String serverName,
                                                      @PathVariable String operatingPlatformGUID,
                                                      @PathVariable String collectionGUID,
                                                      @RequestBody (required = false)
                                                      NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkOperatingPlatformManifest(serverName, operatingPlatformGUID, collectionGUID, requestBody);
    }


    /**
     * Detach an operating platform from a collection of software packages that it was packaged with.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier
     * @param collectionGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/operating-platforms/{operatingPlatformGUID}/operating-platform-manifests/{collectionGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachOperatingPlatformManifest",
            description="Detach an operating platform from a collection of software packages that it was packaged with.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public VoidResponse detachOperatingPlatformManifest(@PathVariable String serverName,
                                                        @PathVariable String operatingPlatformGUID,
                                                        @PathVariable String collectionGUID,
                                                        @RequestBody (required = false)
                                                        DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachOperatingPlatformManifest(serverName, operatingPlatformGUID, collectionGUID, requestBody);
    }


    /**
     * Attach an asset to the collection of software packages that it depends on when it is running.
     *
     * @param serverName name of the server to route the request to
     * @param assetGUID unique identifier
     * @param collectionGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetGUID}/software-package-dependencies/{collectionGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSoftwarePackageDependency",
            description="Attach an asset to the collection of software packages that it depends on when it is running.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public VoidResponse linkSoftwarePackageDependency(@PathVariable String serverName,
                                                      @PathVariable String assetGUID,
                                                      @PathVariable String collectionGUID,
                                                      @RequestBody (required = false)
                                                      NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSoftwarePackageDependency(serverName, assetGUID, collectionGUID, requestBody);
    }


    /**
     * Detach an asset from a collection of software packages that it no longer depends on.
     *
     * @param serverName name of the server to route the request to
     * @param assetGUID unique identifier
     * @param collectionGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetGUID}/software-package-dependencies/{collectionGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSoftwarePackageDependency",
            description="Detach an asset from a collection of software packages that it no longer depends on.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public VoidResponse detachSoftwarePackageDependency(@PathVariable String serverName,
                                                        @PathVariable String assetGUID,
                                                        @PathVariable String collectionGUID,
                                                        @RequestBody (required = false)
                                                        DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSoftwarePackageDependency(serverName, assetGUID, collectionGUID, requestBody);
    }


    /**
     * Classify an element to indicate that it describes a list of software packages.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/software-package-manifest")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setSoftwarePackageManifest",
            description="Classify an element to indicate that it describes a list of software packages.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public VoidResponse setSoftwarePackageManifest(@PathVariable String serverName,
                                                   @PathVariable String elementGUID,
                                                   @RequestBody (required = false)
                                                   NewClassificationRequestBody requestBody)
    {
        return restAPI.setSoftwarePackageManifest(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the software package manifest designation from the element.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/software-package-manifest/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearSoftwarePackageManifest",
            description="Remove the software package manifest designation from the element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/operating-platform"))

    public VoidResponse clearSoftwarePackageManifest(@PathVariable String serverName,
                                                     @PathVariable String elementGUID,
                                                     @RequestBody (required = false)
                                                     DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearSoftwarePackageManifest(serverName, elementGUID, requestBody);
    }

    /**
     * Classify a software server platform to say that it is a cloud platform.
     *
     * @param serverName name of the server to route the request to
     * @param softwareServerPlatformGUID unique identifier of the software server platform
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-server-platforms/{softwareServerPlatformGUID}/cloud-platform")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setAsCloudPlatform",
            description="Classify a software server platform to say that it is a cloud platform.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/cloud-platform"))

    public VoidResponse setAsCloudPlatform(@PathVariable String serverName,
                                           @PathVariable String softwareServerPlatformGUID,
                                           @RequestBody (required = false)
                                           NewClassificationRequestBody requestBody)
    {
        return restAPI.setAsCloudPlatform(serverName, softwareServerPlatformGUID, requestBody);
    }


    /**
     * Remove the cloud platform designation from a software server platform.
     *
     * @param serverName name of the server to route the request to
     * @param softwareServerPlatformGUID unique identifier of the software server platform
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-server-platforms/{softwareServerPlatformGUID}/cloud-platform/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearAsCloudPlatform",
            description="Remove the cloud platform designation from a software server platform.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/cloud-platform"))

    public VoidResponse clearAsCloudPlatform(@PathVariable String serverName,
                                             @PathVariable String softwareServerPlatformGUID,
                                             @RequestBody (required = false)
                                             DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearAsCloudPlatform(serverName, softwareServerPlatformGUID, requestBody);
    }

    /**
     * Classify a host to say that it is a cloud provider.
     *
     * @param serverName name of the server to route the request to
     * @param hostGUID unique identifier of the host
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/hosts/{hostGUID}/cloud-provider")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setHostAsCloudProvider",
            description="Classify a host to say that it is a cloud provider.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/cloud-platform"))

    public VoidResponse setHostAsCloudProvider(@PathVariable String serverName,
                                               @PathVariable String hostGUID,
                                               @RequestBody (required = false)
                                               NewClassificationRequestBody requestBody)
    {
        return restAPI.setHostAsCloudProvider(serverName, hostGUID, requestBody);
    }


    /**
     * Remove the cloud provider designation from a host.
     *
     * @param serverName name of the server to route the request to
     * @param hostGUID unique identifier of the host
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/hosts/{hostGUID}/cloud-provider/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearHostAsCloudProvider",
            description="Remove the cloud provider designation from a host.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/cloud-platform"))

    public VoidResponse clearHostAsCloudProvider(@PathVariable String serverName,
                                                 @PathVariable String hostGUID,
                                                 @RequestBody (required = false)
                                                 DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearHostAsCloudProvider(serverName, hostGUID, requestBody);
    }

    /**
     * Classify a software server to say that it is hosting a cloud tenant.
     *
     * @param serverName name of the server to route the request to
     * @param softwareServerGUID unique identifier of the software server
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-servers/{softwareServerGUID}/cloud-tenant")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setServerAsCloudTenant",
            description="Classify a software server to say that it is hosting a cloud tenant.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/cloud-platform"))

    public VoidResponse setServerAsCloudTenant(@PathVariable String serverName,
                                               @PathVariable String softwareServerGUID,
                                               @RequestBody (required = false)
                                               NewClassificationRequestBody requestBody)
    {
        return restAPI.setServerAsCloudTenant(serverName, softwareServerGUID, requestBody);
    }


    /**
     * Remove the cloud tenant designation from a software server.
     *
     * @param serverName name of the server to route the request to
     * @param softwareServerGUID unique identifier of the software server
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-servers/{softwareServerGUID}/cloud-tenant/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearServerAsCloudTenant",
            description="Remove the cloud tenant designation from a software server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/cloud-platform"))

    public VoidResponse clearServerAsCloudTenant(@PathVariable String serverName,
                                                 @PathVariable String softwareServerGUID,
                                                 @RequestBody (required = false)
                                                 DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearServerAsCloudTenant(serverName, softwareServerGUID, requestBody);
    }

    /**
     * Classify a software capability to say that it is a cloud service.
     *
     * @param serverName name of the server to route the request to
     * @param softwareCapabilityGUID unique identifier of the software capability
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-capabilities/{softwareCapabilityGUID}/cloud-service")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setCapabilityAsCloudService",
            description="Classify a software capability to say that it is a cloud service.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/cloud-platform"))

    public VoidResponse setCapabilityAsCloudService(@PathVariable String serverName,
                                                    @PathVariable String softwareCapabilityGUID,
                                                    @RequestBody (required = false)
                                                    NewClassificationRequestBody requestBody)
    {
        return restAPI.setCapabilityAsCloudService(serverName, softwareCapabilityGUID, requestBody);
    }


    /**
     * Remove the cloud service designation from a software capability.
     *
     * @param serverName name of the server to route the request to
     * @param softwareCapabilityGUID unique identifier of the software capability
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/software-capabilities/{softwareCapabilityGUID}/cloud-service/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearCapabilityAsCloudService",
            description="Remove the cloud service designation from a software capability.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/cloud-platform"))

    public VoidResponse clearCapabilityAsCloudService(@PathVariable String serverName,
                                                      @PathVariable String softwareCapabilityGUID,
                                                      @RequestBody (required = false)
                                                      DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearCapabilityAsCloudService(serverName, softwareCapabilityGUID, requestBody);
    }
}
