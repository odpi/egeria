/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.devops.server.spring;

import org.odpi.openmetadata.accessservices.devops.server.DevOpsRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DevOpsResource provides the server-side implementation of the DevOps Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/dev-ops/users/{userId}")
public class DevOpsResource
{
    private DevOpsRESTServices restAPI = new DevOpsRESTServices();

    /**
     * Default constructor
     */
    public DevOpsResource()
    {
    }

}
