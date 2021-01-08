/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.spring;

import org.odpi.openmetadata.accessservices.digitalservice.server.DigitalServiceRESTServices;
import org.odpi.openmetadata.accessservices.digitalservice.rest.DigitalServiceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * The DigitalServiceResource provides the server-side implementation of the DigitalService Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/digital-service/users/{userId}")

@Tag(name="Digital Service OMAS", description="The Digital Service OMAS provides services to aid the integration of tools involved in tracking the life cycle of an Egeria Digital Service", externalDocs=@ExternalDocumentation(description="Digital Service Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/digital-service/"))

public class DigitalServiceResource
{
    private DigitalServiceRESTServices restAPI = new DigitalServiceRESTServices();

    /**
     * Default constructor
     */
    public DigitalServiceResource()
    {
    }

    @PostMapping(path = "/digital-service")
    public GUIDResponse createDigitalService(@PathVariable("serverName") String serverName,
                                             @PathVariable("userId") String userId,
                                             @RequestBody DigitalServiceRequestBody requestBody) {
        return restAPI.createDigitalService(userId, serverName, requestBody);
    }


}
