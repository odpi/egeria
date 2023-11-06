/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.spring;

import org.odpi.openmetadata.accessservices.digitalservice.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.digitalservice.server.DigitalServiceRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * The DigitalServiceResource provides the server-side implementation of the DigitalServiceProperties Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/digital-service/users/{userId}")

@Tag(name="Metadata Access Server: Digital Service OMAS", description="The Digital Service OMAS provides services to aid the integration of tools involved in tracking the life cycle of an Egeria Digital Service",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/digital-service/overview/"))

public class DigitalServiceResource
{
    private final DigitalServiceRESTServices restAPI = new DigitalServiceRESTServices();

    /**
     * Default constructor
     */
    public DigitalServiceResource()
    {
    }

    @PostMapping(path = "/digital-service")
    public GUIDResponse createDigitalService(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createDigitalService(userId, serverName, requestBody);
    }


}
