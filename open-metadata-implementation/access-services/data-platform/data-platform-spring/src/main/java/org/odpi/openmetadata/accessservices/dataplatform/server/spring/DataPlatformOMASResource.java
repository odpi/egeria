/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.dataplatform.server.DataPlatformRESTServices;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.springframework.web.bind.annotation.*;

/**
 * Server-side REST API support for data platform independent REST endpoints
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-platform/users/{userId}")

@Tag(name="Data Platform OMAS",
        description="The Data Platform OMAS provides APIs for tools and applications wishing to manage metadata relating to data platforms.",
        externalDocs=@ExternalDocumentation(description="Data Platform Open Metadata Access Service (OMAS)",
                url="https://egeria.odpi.org/open-metadata-implementation/access-services/data-platform/"))

public class DataPlatformOMASResource
{
    private DataPlatformRESTServices restAPI = new DataPlatformRESTServices();


    /**
     * Instantiates a new Data Platform OMAS resource.
     */
    public DataPlatformOMASResource()
    {
    }

    /**
     * Return the connection object for the Discovery Engine OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "topics/out-topic-connection")

    public ConnectionResponse getOutTopicConnection(@PathVariable String                        serverName,
                                                    @PathVariable String                        userId)
    {
        return restAPI.getOutTopicConnection(serverName, userId);
    }
}
