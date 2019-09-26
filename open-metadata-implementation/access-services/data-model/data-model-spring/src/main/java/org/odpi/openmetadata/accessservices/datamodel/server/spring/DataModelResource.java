/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamodel.server.spring;

import org.odpi.openmetadata.accessservices.datamodel.server.DataModelRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DataModelResource provides the server-side implementation of the Data Model Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-model/users/{userId}")
public class DataModelResource
{
    private DataModelRESTServices restAPI = new DataModelRESTServices();

    /**
     * Default constructor
     */
    public DataModelResource()
    {
    }

}
