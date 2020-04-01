/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.stewardshipaction.server.StewardshipActionRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The StewardshipActionResource provides the server-side implementation of the Stewardship Action Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/stewardship-action/users/{userId}")

@Tag(name="Stewardship Action OMAS", description="The Stewardship Action OMAS provides APIs and events for tools and applications focused on resolving issues detected in the data landscape.", externalDocs=@ExternalDocumentation(description="Stewardship Action Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/stewardship-action/"))

public class StewardshipActionResource
{
    private StewardshipActionRESTServices restAPI = new StewardshipActionRESTServices();

    /**
     * Default constructor
     */
    public StewardshipActionResource()
    {
    }

}
