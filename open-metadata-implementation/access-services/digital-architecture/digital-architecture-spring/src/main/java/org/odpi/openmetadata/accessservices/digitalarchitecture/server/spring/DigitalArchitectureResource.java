/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.digitalarchitecture.server.DigitalArchitectureRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The DigitalArchitectureResource provides the server-side implementation of the Digital Architecture Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/digital-architecture/users/{userId}")

@Tag(name="Metadata Access Server: Digital Architecture OMAS",
        description="The Digital Architecture OMAS provides APIs for tools and applications managing the design of data structures, software and the IT infrastructure that supports the operations of the organization.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omas/digital-architecture/overview/"))

public class DigitalArchitectureResource
{
    private final DigitalArchitectureRESTServices restAPI = new DigitalArchitectureRESTServices();

    /**
     * Default constructor
     */
    public DigitalArchitectureResource()
    {
    }

}
