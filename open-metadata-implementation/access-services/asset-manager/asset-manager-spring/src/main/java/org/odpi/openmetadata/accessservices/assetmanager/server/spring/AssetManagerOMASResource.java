/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.server.AssetManagerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionResponse;
import org.springframework.web.bind.annotation.*;

/**
 * Server-side REST API support for data manager independent REST endpoints
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
    @GetMapping(path = "/topics/out-topic-connection")

    public ConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }

}
