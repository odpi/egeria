/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server.spring;

import org.odpi.openmetadata.accessservices.itinfrastructure.server.ITInfrastructureRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ITInfrastructureResource provides the server-side implementation of the IT Infrastructure Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/it-infrastructure/users/{userId}")
public class ITInfrastructureResource
{
    private ITInfrastructureRESTServices restAPI = new ITInfrastructureRESTServices();

    /**
     * Default constructor
     */
    public ITInfrastructureResource()
    {
    }

}
