/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.server.spring;

import org.odpi.openmetadata.accessservices.stewardshipaction.server.StewardshipActionRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The StewardshipActionResource provides the server-side implementation of the Stewardship Action Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/stewardship-action/users/{userId}")
public class StewardshipActionResource
{
    private StewardshipActionRESTServices restAPI = new StewardshipActionRESTServices();

    /**
     * Default constructor
     */
    public StewardshipActionResource()
    {
    }

}
