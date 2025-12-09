/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dataofficer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.dataofficer.server.DataOfficerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DataOfficerResource provides part of the server-side implementation of the Data Officer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/data-officer")

@Tag(name="API: Data Officer", description="Supports a Data Officer as they lead the data governance program.  This builds on the capabilities of the Governance Officer API.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/data-officer/overview/"))

public class DataOfficerResource
{
    private final DataOfficerRESTServices restAPI = new DataOfficerRESTServices();

    /**
     * Default constructor
     */
    public DataOfficerResource()
    {
    }

}
