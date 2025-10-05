/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.frameworkservices.oif.rest.MetadataOriginRequestBody;
import org.odpi.openmetadata.frameworkservices.oif.server.OpenIntegrationRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * OpenIntegrationResource supports the REST APIs for running the Open Integration Service.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/{serviceURLMarker}/open-integration/users/{userId}")

@Tag(name="Metadata Access Services: Open Integration Service",
     description="Provides support for the context used by integration connectors.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/oif-metadata-management/"))


public class OpenIntegrationResource
{
    private final OpenIntegrationRESTServices restAPI = new OpenIntegrationRESTServices();


    /**
     * Retrieve the unique identifier of the metadata collection representing a metadata source.
     *
     * @param serverName name of the server to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "metadata-sources/by-name")

    @Operation(summary="getMetadataSourceGUID",
            description="Retrieve the unique identifier of the metadata collection representing a metadata source.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/metadata-collection/"))

    public GUIDResponse getMetadataSourceGUID(@PathVariable String          serverName,
                                              @PathVariable String          serviceURLMarker,
                                              @PathVariable String          userId,
                                              @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getMetadataSourceGUID(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a software capability.  This describes the metadata source.
     *
     * @param serverName name of the server to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody properties of the software capability
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-sources/new")

    @Operation(summary="createMetadataSource",
            description="Create a new metadata element to represent a metadata collection.  This describes the metadata source.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/metadata-collection/"))

    public GUIDResponse createMetadataSource(@PathVariable String                    serverName,
                                             @PathVariable String                    serviceURLMarker,
                                             @PathVariable String                    userId,
                                             @RequestBody MetadataOriginRequestBody requestBody)
    {
        return restAPI.createMetadataSource(serverName, serviceURLMarker, userId, requestBody);
    }
}
