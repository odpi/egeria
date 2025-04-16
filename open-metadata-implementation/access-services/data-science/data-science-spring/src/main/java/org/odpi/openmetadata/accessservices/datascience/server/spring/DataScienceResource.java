/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datascience.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.datascience.server.DataScienceRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServiceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The DataScienceResource provides the server-side implementation of the Data Science Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-science/users/{userId}")

@Tag(name="Metadata Access Server: Data Science OMAS", description="The Data Science OMAS provides APIs and events for tools and applications focused on building all types of analytics models such as predictive models and machine learning models.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/data-science/overview/"))

public class DataScienceResource
{
    private final DataScienceRESTServices restAPI = new DataScienceRESTServices();

    /**
     * Default constructor
     */
    public DataScienceResource()
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
