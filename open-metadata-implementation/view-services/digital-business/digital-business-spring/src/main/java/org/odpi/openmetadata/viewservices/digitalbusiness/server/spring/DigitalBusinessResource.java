/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.digitalbusiness.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.digitalbusiness.server.DigitalBusinessRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DigitalBusinessResource provides part of the server-side implementation of the Digital Business OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/digital-business")

@Tag(name="API: Digital Business OMVS", description="The Digital Business OMVS provides APIs for managing context events.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/digital-business/overview/"))

public class DigitalBusinessResource
{
    private final DigitalBusinessRESTServices restAPI = new DigitalBusinessRESTServices();

    /**
     * Default constructor
     */
    public DigitalBusinessResource()
    {
    }

}
