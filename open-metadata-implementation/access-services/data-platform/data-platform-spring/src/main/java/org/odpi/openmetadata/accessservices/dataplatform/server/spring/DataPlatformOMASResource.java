/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server.spring;

import org.odpi.openmetadata.accessservices.dataplatform.responses.RegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataplatform.server.DataPlatformRestServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-platform/users/{userId}/")
public class DataPlatformOMASResource {

    private final DataPlatformRestServices restAPI;


    /**
     * Instantiates a new Data platform omas resource.
     *
     * @param restAPI the rest api
     */
    public DataPlatformOMASResource(DataPlatformRestServices restAPI) {
        this.restAPI = restAPI;
    }

    /**
     * Create a software server capability entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the entity
     *
     * @return unique identifier of the created process
     */
    @PostMapping(path = "/software-server-capabilities")
    public GUIDResponse createSoftwareServerCapability(@PathVariable("serverName") String serverName,
                                                       @PathVariable("userId") String userId,
                                                       @RequestBody RegistrationRequestBody requestBody) {
        return restAPI.createSoftwareServer(serverName, userId, requestBody);
    }

    /**
     * Return the unique identifier from a software server capability definition.
     *
     * @param serverName    name of server instance to call
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the software server capability
     *
     * @return unique identified of the software server
     */
    @GetMapping(path = "/software-server-capabilities/{qualifiedName}")
    public GUIDResponse getSoftwareServerCapabilityByQualifiedName(@PathVariable String serverName,
                                                                   @PathVariable String userId,
                                                                   @PathVariable String qualifiedName) {
        return restAPI.getSoftwareServerByQualifiedName(serverName, userId, qualifiedName);
    }

}