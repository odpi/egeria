/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.softwaredeveloper.server.spring;

import org.odpi.openmetadata.accessservices.softwaredeveloper.server.SoftwareDeveloperRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SoftwareDeveloperResource provides the server-side implementation of the Software Developer Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/software-developer/users/{userId}")
public class SoftwareDeveloperResource
{
    private SoftwareDeveloperRESTServices restAPI = new SoftwareDeveloperRESTServices();

    /**
     * Default constructor
     */
    public SoftwareDeveloperResource()
    {
    }

}
