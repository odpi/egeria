/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.designmodel.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.designmodel.server.DesignModelRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The DesignModelResource provides the server-side implementation of the Design Model Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-model/users/{userId}")

@Tag(name="Metadata Access Server: Design Model OMAS", description="The Design Model OMAS supports the management of design model intellectual property that has either been provided as standard or created in a software architecture and design modeling tool.",
     externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/omas/design-model/overview/"))

public class DesignModelResource
{
    private DesignModelRESTServices restAPI = new DesignModelRESTServices();

    /**
     * Default constructor
     */
    public DesignModelResource()
    {
    }

}
