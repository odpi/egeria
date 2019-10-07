/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.designmodel.server.spring;

import org.odpi.openmetadata.accessservices.designmodel.server.DesignModelRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DesignModelResource provides the server-side implementation of the Design Model Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-model/users/{userId}")
public class DesignModelResource
{
    private DesignModelRESTServices restAPI = new DesignModelRESTServices();

    /**
     * Default constructor
     */
    public DesignModelResource()
    {
    }

}
