/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.referencedata.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.viewservices.referencedata.server.ReferenceDataRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ReferenceDataResource provides part of the server-side implementation of the Reference Data OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/reference-data")

@Tag(name="API: Reference Data OMVS", description="The Reference Data OMVS provides APIs for retrieving configuration and status from servers and platforms.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/reference-data/overview/"))

public class ReferenceDataResource
{
    private final ReferenceDataRESTServices restAPI = new ReferenceDataRESTServices();

    /**
     * Default constructor
     */
    public ReferenceDataResource()
    {
    }

}
