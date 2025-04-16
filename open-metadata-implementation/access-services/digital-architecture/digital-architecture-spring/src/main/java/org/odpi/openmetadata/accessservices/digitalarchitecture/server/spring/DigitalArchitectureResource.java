/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.digitalarchitecture.server.DigitalArchitectureRESTServices;
import org.odpi.openmetadata.accessservices.digitalarchitecture.server.ReferenceDataRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ReferenceValueAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValuesImplProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValuesMappingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueProperties;
import org.springframework.web.bind.annotation.*;


/**
 * The DigitalArchitectureResource provides the server-side implementation of the Digital Architecture Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/digital-architecture/users/{userId}")

@Tag(name="Metadata Access Server: Digital Architecture OMAS",
        description="The Digital Architecture OMAS provides APIs for tools and applications managing the design of data structures, software and the IT infrastructure that supports the operations of the organization.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omas/digital-architecture/overview/"))

public class DigitalArchitectureResource
{
    private final DigitalArchitectureRESTServices restAPI = new DigitalArchitectureRESTServices();

    /**
     * Default constructor
     */
    public DigitalArchitectureResource()
    {
    }



    /**
     * Return the description of this service.
     *
     * @param serverName name of the server to route the request to
     * @param userId identifier of calling user
     *
     * @return service description or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/description")

    public RegisteredOMAGServiceResponse getServiceDescription(@PathVariable String serverName,
                                                               @PathVariable String userId)
    {
        return restAPI.getServiceDescription(serverName, userId);
    }


}
