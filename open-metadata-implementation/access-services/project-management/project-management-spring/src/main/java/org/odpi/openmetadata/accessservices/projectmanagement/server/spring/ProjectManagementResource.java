/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.server.spring;

import org.odpi.openmetadata.accessservices.projectmanagement.server.ProjectManagementRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ProjectManagementResource provides the server-side implementation of the Project Management Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/project-management/users/{userId}")
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
