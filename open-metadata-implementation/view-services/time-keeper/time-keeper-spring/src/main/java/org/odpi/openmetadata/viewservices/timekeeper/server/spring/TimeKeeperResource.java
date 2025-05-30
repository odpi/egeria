/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.timekeeper.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.timekeeper.server.TimeKeeperRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The TimeKeeperResource provides part of the server-side implementation of the Time Keeper OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/time-keeper")

@Tag(name="API: Time Keeper OMVS", description="The Time Keeper OMVS provides APIs for managing context events.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/time-keeper/overview/"))

public class TimeKeeperResource
{
    private final TimeKeeperRESTServices restAPI = new TimeKeeperRESTServices();

    /**
     * Default constructor
     */
    public TimeKeeperResource()
    {
    }

}
