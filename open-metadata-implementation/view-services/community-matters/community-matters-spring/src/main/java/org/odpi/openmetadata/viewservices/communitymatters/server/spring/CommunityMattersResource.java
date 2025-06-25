/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.communitymatters.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.communitymatters.server.CommunityMattersRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The CommunityMattersResource provides part of the server-side implementation of the Community Matters OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/community-matters")

@Tag(name="API: Community Matters OMVS", description="The Community Matters OMVS provides APIs for managing the definition and members of the communities.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/community-matters/overview/"))

public class CommunityMattersResource
{
    private final CommunityMattersRESTServices restAPI = new CommunityMattersRESTServices();

    /**
     * Default constructor
     */
    public CommunityMattersResource()
    {
    }

}
