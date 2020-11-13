/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.devops.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.devops.server.DevOpsRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The DevOpsResource provides the server-side implementation of the DevOps Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/dev-ops/users/{userId}")

@Tag(name="DevOps OMAS", description="The DevOps OMAS provides APIs and events for tools that play a role in a DevOps pipeline.", externalDocs=@ExternalDocumentation(description="DevOps Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/dev-ops/"))

public class DevOpsResource
{
    private DevOpsRESTServices restAPI = new DevOpsRESTServices();

    /**
     * Default constructor
     */
    public DevOpsResource()
    {
    }

}
