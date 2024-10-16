/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexplorer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.metadataexplorer.server.MetadataExplorerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The MetadataExplorerResource provides part of the server-side implementation of the Metadata Explorer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/metadata-explorer")

@Tag(name="API: Metadata Explorer OMVS", description="The Metadata Explorer OMVS provides APIs for supporting the search, query and retrieval of open metadata.  It is an advanced API for users that understand the [Open Metadata Types](https://egeria-project.org/types/).",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

public class MetadataExplorerResource
{
    private final MetadataExplorerRESTServices restAPI = new MetadataExplorerRESTServices();

    /**
     * Default constructor
     */
    public MetadataExplorerResource()
    {
    }

}
