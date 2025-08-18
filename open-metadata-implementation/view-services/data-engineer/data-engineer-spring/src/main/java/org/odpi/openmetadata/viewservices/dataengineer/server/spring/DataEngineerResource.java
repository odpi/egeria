/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dataengineer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.dataengineer.server.DataEngineerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DataEngineerResource provides part of the server-side implementation of the Data Engineer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/data-engineer")

@Tag(name="API: Data Engineer OMVS", description="The Data Engineer OMVS provides APIs for managing data pipelines and reference data.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/data-engineer/overview/"))

public class DataEngineerResource
{
    private final DataEngineerRESTServices restAPI = new DataEngineerRESTServices();

    /**
     * Default constructor
     */
    public DataEngineerResource()
    {
    }

}
