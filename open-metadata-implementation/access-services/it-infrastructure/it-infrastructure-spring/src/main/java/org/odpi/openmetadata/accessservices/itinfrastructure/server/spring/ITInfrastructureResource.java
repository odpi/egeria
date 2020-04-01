/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.itinfrastructure.server.ITInfrastructureRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The ITInfrastructureResource provides the server-side implementation of the IT Infrastructure Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/it-infrastructure/users/{userId}")

@Tag(name="IT Infrastructure OMAS", description="The IT Infrastructure OMAS provides APIs for tools and applications managing the IT infrastructure that supports the data assets." +
        "\n", externalDocs=@ExternalDocumentation(description="IT Infrastructure Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/it-infrastructure/"))

public class ITInfrastructureResource
{
    private ITInfrastructureRESTServices restAPI = new ITInfrastructureRESTServices();

    /**
     * Default constructor
     */
    public ITInfrastructureResource()
    {
    }

}
