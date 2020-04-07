/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.projectmanagement.server.ProjectManagementRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The ProjectManagementResource provides the server-side implementation of the Project Management Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/project-management/users/{userId}")

@Tag(name="Project Managemen OMASt", description="The Project Management OMAS provides APIs and events for tools and applications that support project leaders - particularly those who are leading governance projects." +
        "\n", externalDocs=@ExternalDocumentation(description="Project Management Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/project-management/"))

public class ProjectManagementResource
{
    private ProjectManagementRESTServices restAPI = new ProjectManagementRESTServices();

    /**
     * Default constructor
     */
    public ProjectManagementResource()
    {
    }

}
