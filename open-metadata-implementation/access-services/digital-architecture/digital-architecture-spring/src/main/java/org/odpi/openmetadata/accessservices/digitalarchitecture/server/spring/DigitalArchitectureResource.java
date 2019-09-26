/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server.spring;

import org.odpi.openmetadata.accessservices.digitalarchitecture.server.DigitalArchitectureRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DigitalArchitectureResource provides the server-side implementation of the Digital Architecture Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/digital-architecture/users/{userId}")
public class DigitalArchitectureResource
{
    private DigitalArchitectureRESTServices restAPI = new DigitalArchitectureRESTServices();

    /**
     * Default constructor
     */
    public DigitalArchitectureResource()
    {
    }

}
