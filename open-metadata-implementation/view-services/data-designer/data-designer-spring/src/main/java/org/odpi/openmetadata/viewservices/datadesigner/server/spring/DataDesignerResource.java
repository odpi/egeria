/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadesigner.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.datadesigner.server.DataDesignerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DataDesignerResource provides part of the server-side implementation of the Data Designer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/data-designer")

@Tag(name="API: Data Designer OMVS", description="The Data Designer OMVS provides APIs for building schemas for new data assets.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/data-designer/overview/"))

public class DataDesignerResource
{
    private final DataDesignerRESTServices restAPI = new DataDesignerRESTServices();

    /**
     * Default constructor
     */
    public DataDesignerResource()
    {
    }

}
