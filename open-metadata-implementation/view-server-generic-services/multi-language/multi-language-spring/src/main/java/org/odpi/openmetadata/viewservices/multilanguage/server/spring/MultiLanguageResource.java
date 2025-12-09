/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.multilanguage.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.multilanguage.server.MultiLanguageRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The MultiLanguageResource provides part of the server-side implementation of the Multi Language OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/multi-language")

@Tag(name="API: Multi Language", description="Supports the maintenance of translations of text attributes in open metadata.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/multi-language/overview/"))

public class MultiLanguageResource
{
    private final MultiLanguageRESTServices restAPI = new MultiLanguageRESTServices();

    /**
     * Default constructor
     */
    public MultiLanguageResource()
    {
    }

}
