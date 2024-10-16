/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.securityofficer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.securityofficer.server.SecurityOfficerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SecurityOfficerResource provides part of the server-side implementation of the Security Officer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/security-officer")

@Tag(name="API: Security Officer OMVS", description="The Security Officer OMVS provides APIs for supporting a Security Officer as they lead the security governance program.  This builds on the capabilities of the Governance Officer OMVS.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/security-officer/overview/"))

public class SecurityOfficerResource
{
    private final SecurityOfficerRESTServices restAPI = new SecurityOfficerRESTServices();

    /**
     * Default constructor
     */
    public SecurityOfficerResource()
    {
    }

}
