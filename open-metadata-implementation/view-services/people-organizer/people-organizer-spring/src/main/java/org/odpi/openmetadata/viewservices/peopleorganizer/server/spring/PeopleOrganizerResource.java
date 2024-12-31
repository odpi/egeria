/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.peopleorganizer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.peopleorganizer.server.PeopleOrganizerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The PeopleOrganizerResource provides part of the server-side implementation of the People Organizer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/people-organizer")

@Tag(name="API: People Organizer OMVS", description="The People Organizer OMVS provides APIs to maintain information about an organization.  This includes the definitions of teams, roles and organization structures.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/people-organizer/overview/"))

public class PeopleOrganizerResource
{
    private final PeopleOrganizerRESTServices restAPI = new PeopleOrganizerRESTServices();

    /**
     * Default constructor
     */
    public PeopleOrganizerResource()
    {
    }

}
