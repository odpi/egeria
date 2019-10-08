/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datascience.server.spring;

import org.odpi.openmetadata.accessservices.datascience.server.DataScienceRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DataScienceResource provides the server-side implementation of the Data Science Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-science/users/{userId}")
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
