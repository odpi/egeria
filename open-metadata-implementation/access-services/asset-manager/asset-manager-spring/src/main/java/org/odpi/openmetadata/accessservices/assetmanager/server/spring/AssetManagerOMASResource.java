/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.accessservices.assetmanager.server.AssetManagerRESTServices;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.AssetManagerProperties;
import org.springframework.web.bind.annotation.*;

/**
 * Server-side REST API support for asset manager independent REST endpoints
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Metadata Access Server: Asset Manager OMAS",
        description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omas/asset-manager/overview"))

public class AssetManagerOMASResource
{
    private final AssetManagerRESTServices restAPI = new AssetManagerRESTServices();


    /**
     * Instantiates a new Asset Manager OMAS resource.
     */
    public AssetManagerOMASResource()
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

    public OCFConnectionResponse getOutTopicConnection(@PathVariable String serverName,
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
                                                   @RequestBody AssetManagerProperties assetManagerProperties)
    {
        return restAPI.createExternalAssetManager(serverName, userId, assetManagerProperties);
    }


    /**
     * Retrieve the unique identifier of the external asset manager from its qualified name.
     * Typically, the qualified name comes from the integration connector configuration.
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
}
