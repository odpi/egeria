/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceofficer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.governanceofficer.server.GovernanceOfficerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The GovernanceOfficerResource provides part of the server-side implementation of the Governance Officer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/governance-officer")

@Tag(name="API: Governance Officer OMVS", description="The Governance Officer OMVS provides APIs for supporting the creation and editing of a new governance domain.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/governance-officer/overview/"))

public class GovernanceOfficerResource
{
    private final GovernanceOfficerRESTServices restAPI = new GovernanceOfficerRESTServices();

    /**
     * Default constructor
     */
    public GovernanceOfficerResource()
    {
    }

}
