/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.server.spring;

import org.odpi.openmetadata.accessservices.dataprivacy.server.DataPrivacyRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DataPrivacyResource provides the server-side implementation of the Data Privacy Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-privacy/users/{userId}")
public class DataPrivacyResource
{
    private DataPrivacyRESTServices restAPI = new DataPrivacyRESTServices();

    /**
     * Default constructor
     */
    public DataPrivacyResource()
    {
    }

}
