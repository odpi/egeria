/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.softwaredeveloper.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.softwaredeveloper.server.SoftwareDeveloperRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The SoftwareDeveloperResource provides the server-side implementation of the Software Developer Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/software-developer/users/{userId}")

@Tag(name="Software Developer OMAS", description="The Software Developer OMAS provides APIs and events for software developer tools and applications that help developers make good use of the standards and best practices defined for the data landscape." +
        "\n", externalDocs=@ExternalDocumentation(description="Software Developer Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/software-developer/"))

public class SoftwareDeveloperResource
{
    private SoftwareDeveloperRESTServices restAPI = new SoftwareDeveloperRESTServices();

    /**
     * Default constructor
     */
    public SoftwareDeveloperResource()
    {
    }

}
