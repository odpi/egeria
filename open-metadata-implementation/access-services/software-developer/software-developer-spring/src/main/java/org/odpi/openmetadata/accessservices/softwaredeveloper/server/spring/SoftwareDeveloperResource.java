/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.softwaredeveloper.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.softwaredeveloper.server.SoftwareDeveloperRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServiceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The SoftwareDeveloperResource provides the server-side implementation of the Software Developer Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/software-developer/users/{userId}")

@Tag(name="Metadata Access Server: Software Developer OMAS", description="The Software Developer OMAS provides APIs and events for software developer tools and applications that help developers make good use of the standards and best practices defined for the data landscape.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                                  url="https://egeria-project.org/services/omas/software-developer/overview/"))

public class SoftwareDeveloperResource
{
    private final SoftwareDeveloperRESTServices restAPI = new SoftwareDeveloperRESTServices();

    /**
     * Default constructor
     */
    public SoftwareDeveloperResource()
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
