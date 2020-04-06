/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datascience.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.datascience.server.DataScienceRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The DataScienceResource provides the server-side implementation of the Data Science Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-science/users/{userId}")

@Tag(name="Data Science OMAS", description="The Data Science OMAS provides APIs and events for tools and applications focused on building all types of analytics models such as predictive models and machine learning models.", externalDocs=@ExternalDocumentation(description="Data Science Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/data-science/"))

public class DataScienceResource
{
    private DataScienceRESTServices restAPI = new DataScienceRESTServices();

    /**
     * Default constructor
     */
    public DataScienceResource()
    {
    }

}
