/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.server.spring;

import org.odpi.openmetadata.accessservices.digitalservice.server.DigitalServiceRESTServices;
import org.odpi.openmetadata.accessservices.digitalservice.rest.DigitalServiceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.springframework.web.bind.annotation.*;


/**
 * The DigitalServiceResource provides the server-side implementation of the DigitalService Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/digital-service/users/{userId}")
public class DigitalServiceResource
{
    private DigitalServiceRESTServices restAPI = new DigitalServiceRESTServices();

    /**
     * Default constructor
     */
    public DigitalServiceResource()
    {
    }

    @PostMapping(path = "/digital-service")
    public GUIDResponse createDigitalService(@PathVariable("serverName") String serverName,
                                                     @PathVariable("userId") String userId,
                                                     @RequestBody DigitalServiceRequestBody requestBody) {
        return restAPI.createDigitalService(serverName, userId, requestBody);
    }


}
