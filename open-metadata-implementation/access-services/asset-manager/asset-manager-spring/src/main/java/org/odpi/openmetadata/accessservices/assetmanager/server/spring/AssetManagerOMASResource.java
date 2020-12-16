/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ElementHeadersResponse;
import org.odpi.openmetadata.accessservices.assetmanager.server.AssetManagerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * Server-side REST API support for asset manager independent REST endpoints
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Asset Manager OMAS",
        description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
        externalDocs=@ExternalDocumentation(description="Asset Manager Open Metadata Access Service (OMAS)",
                url="https://egeria.odpi.org/open-metadata-implementation/access-services/asset-manager"))

public class AssetManagerOMASResource
{
    private AssetManagerRESTServices restAPI = new AssetManagerRESTServices();


    /**
     * Instantiates a new Asset Manager OMAS resource.
     */
    public AssetManagerOMASResource()
    {
    }


    /**
     * Return the connection object for the Asset Manager OMAS's out topic.
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

    public ConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }


    /**
     * Create information about the external asset manager.  This is represented as a software server capability
     * and all information that is specific to the external asset manager (such as the identifiers of the
     * metadata elements it stores) will be linked to it.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetManagerProperties description of the integration daemon (specify qualified name at a minimum)
     *
     * @return unique identifier of the asset management's software server capability or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/asset-managers")

    public GUIDResponse createExternalAssetManager(@PathVariable String                 serverName,
                                                   @PathVariable String                 userId,
                                                   @RequestBody  AssetManagerProperties assetManagerProperties)
    {
        return restAPI.createExternalAssetManager(serverName, userId, assetManagerProperties);
    }


    /**
     * Retrieve the unique identifier of the external asset manager from its qualified name.
     * Typically the qualified name comes from the integration connector configuration.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param qualifiedName unique name to use for the external asset
     *
     * @return unique identifier of the external asset manager's software server capability or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @GetMapping(path = "/asset-managers/by-name/{qualifiedName}")

    public GUIDResponse  getExternalAssetManagerGUID(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String qualifiedName)
    {
        return restAPI.getExternalAssetManagerGUID(serverName, userId, qualifiedName);
    }


    /**
     * Add the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/asset-managers/elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers/add")

    public VoidResponse addExternalIdentifier(@PathVariable String                        serverName,
                                              @PathVariable String                        userId,
                                              @PathVariable String                        openMetadataElementGUID,
                                              @PathVariable String                        openMetadataElementTypeName,
                                              @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.addExternalIdentifier(serverName, userId, openMetadataElementGUID, openMetadataElementTypeName, requestBody);
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/asset-managers/elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers/update")

    public VoidResponse updateExternalIdentifier(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        openMetadataElementGUID,
                                                 @PathVariable String                        openMetadataElementTypeName,
                                                 @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.updateExternalIdentifier(serverName, userId, openMetadataElementGUID, openMetadataElementTypeName, requestBody);
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/asset-managers/elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers/remove")

    public VoidResponse removeExternalIdentifier(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        openMetadataElementGUID,
                                                 @PathVariable String                        openMetadataElementTypeName,
                                                 @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.removeExternalIdentifier(serverName, userId, openMetadataElementGUID, openMetadataElementTypeName, requestBody);
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param requestBody details of the external identifier and its scope
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/asset-managers/elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers")

    public VoidResponse confirmSynchronization(@PathVariable String                        serverName,
                                               @PathVariable String                        userId,
                                               @PathVariable String                        openMetadataElementGUID,
                                               @PathVariable String                        openMetadataElementTypeName,
                                               @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.confirmSynchronization(serverName, userId, openMetadataElementGUID, openMetadataElementTypeName, requestBody);
    }


    /**
     * Retrieve the unique identifier of the external asset manager from its qualified name.
     * Typically the qualified name comes from the integration connector configuration.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody details of the external identifier
     *
     * @return list of linked elements, null if null or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/asset-managers/external-identifiers/open-metadata-elements")

    public ElementHeadersResponse getElementsForExternalIdentifier(@PathVariable String                        serverName,
                                                                   @PathVariable String                        userId,
                                                                   @RequestParam int                           startFrom,
                                                                   @RequestParam int                           pageSize,
                                                                   @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.getElementsForExternalIdentifier(serverName, userId, startFrom, pageSize, requestBody);
    }
}
