/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.privacyofficer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.privacyofficer.server.PrivacyOfficerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The PrivacyOfficerResource provides part of the server-side implementation of the Privacy Officer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/privacy-officer")

@Tag(name="API: Privacy Officer OMVS", description="The Privacy Officer OMVS provides APIs for supporting a Privacy Officer as they lead the data privacy governance program.  This builds on the capabilities of the Governance Officer OMVS.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/privacy-officer/overview/"))

public class PrivacyOfficerResource
{
    private final PrivacyOfficerRESTServices restAPI = new PrivacyOfficerRESTServices();

    /**
     * Default constructor
     */
    public PrivacyOfficerResource()
    {
    }

}
