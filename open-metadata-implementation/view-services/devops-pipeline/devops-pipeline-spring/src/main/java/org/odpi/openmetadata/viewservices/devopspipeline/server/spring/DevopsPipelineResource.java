/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.devopspipeline.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.devopspipeline.server.DevopsPipelineRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DevopsPipelineResource provides part of the server-side implementation of the Devops Pipeline OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/devops-pipeline")

@Tag(name="API: Devops Pipeline OMVS", description="The Devops Pipeline OMVS provides APIs for supporting a devops engineer to maintain the metadata about the changing digital resources being deployed through devops pipelines.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/devops-pipeline/overview/"))

public class DevopsPipelineResource
{
    private final DevopsPipelineRESTServices restAPI = new DevopsPipelineRESTServices();

    /**
     * Default constructor
     */
    public DevopsPipelineResource()
    {
    }

}
