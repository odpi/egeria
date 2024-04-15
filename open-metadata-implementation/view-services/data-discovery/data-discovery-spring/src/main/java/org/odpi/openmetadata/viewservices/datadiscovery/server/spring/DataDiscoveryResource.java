/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadiscovery.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.datadiscovery.server.DataDiscoveryRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DataDiscoveryResource provides part of the server-side implementation of the Data Discovery OMVS.
 * This interface provides access to an individual's personal profile.
 */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/data-discovery")

@Tag(name="API: Data Discovery OMVS", description="The Data Discovery OMVS provides APIs for retrieving and updating a user's personal profile.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/data-discovery/overview/"))

public class DataDiscoveryResource
{
    private final DataDiscoveryRESTServices restAPI = new DataDiscoveryRESTServices();

    /**
     * Default constructor
     */
    public DataDiscoveryResource()
    {
    }

}
