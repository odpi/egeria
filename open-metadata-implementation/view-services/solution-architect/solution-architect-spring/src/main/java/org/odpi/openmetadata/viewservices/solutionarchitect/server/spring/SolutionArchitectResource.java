/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.solutionarchitect.server.SolutionArchitectRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SolutionArchitectResource provides part of the server-side implementation of the Solution Architect OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/solution-architect")

@Tag(name="API: Solution Architect OMVS", description="The Solution Architect OMVS is a REST API designed to support user interfaces (UIs) relating to the definition and display of solution blueprints and their supporting solution components along with the relevant information supply chains.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/solution-architect/overview/"))

public class SolutionArchitectResource
{
    private final SolutionArchitectRESTServices restAPI = new SolutionArchitectRESTServices();

    /**
     * Default constructor
     */
    public SolutionArchitectResource()
    {
    }

}
