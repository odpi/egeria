/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.templatemanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.templatemanager.server.TemplateManagerRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The TemplateManagerResource provides part of the server-side implementation of the Template Manager OMVS.
 */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/template-manager")

@Tag(name="API: Template Manager OMVS", description="The Template Manager OMVS provides APIs for retrieving, creating and maintaining templates.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/template-manager/overview/"))

public class TemplateManagerResource
{
    private final TemplateManagerRESTServices restAPI = new TemplateManagerRESTServices();

    /**
     * Default constructor
     */
    public TemplateManagerResource()
    {
    }


}
