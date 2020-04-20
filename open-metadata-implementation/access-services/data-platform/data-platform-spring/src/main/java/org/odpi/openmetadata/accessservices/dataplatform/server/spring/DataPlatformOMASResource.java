/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.dataplatform.responses.DataPlatformOMASAPIResponse;
import org.odpi.openmetadata.accessservices.dataplatform.responses.DataPlatformRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataplatform.responses.DeployedDatabaseSchemaRequestBody;
import org.odpi.openmetadata.accessservices.dataplatform.server.DataPlatformRestServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.springframework.web.bind.annotation.*;

/**
 * The type Data platform omas resource.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-platform/users/{userId}")

@Tag(name="Data Platform OMAS", description="The Data Platform OMAS provides APIs for tools and applications wishing to register new data assets. It provides the ability to register the host, platform and location of the data platform itself along with the data assets it hosts.", externalDocs=@ExternalDocumentation(description="Data Platform Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/data-platform/"))

public class DataPlatformOMASResource {

    private DataPlatformRestServices restAPI = new DataPlatformRestServices();

    /**
     * Instantiates a new Data Platform omas resource.
     */
    public DataPlatformOMASResource() {
    }

    /**
     * Create a software server capability entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the entity
     * @return unique identifier of the created process
     */
    @PostMapping(path = "/registration")
    public GUIDResponse createExternalDataPlatform(@PathVariable("serverName") String serverName,
                                                   @PathVariable("userId") String userId,
                                                   @RequestBody DataPlatformRegistrationRequestBody requestBody) {
        return restAPI.createExternalDataPlatform(serverName, userId, requestBody);
    }

    /**
     * Return the software server capability entity from an external data platform by qualified name.
     *
     * @param serverName    the server name
     * @param userId        the user id
     * @param qualifiedName the qualified name
     * @return the software server capability by qualified name
     */
    @GetMapping(path = "/software-server-capability/{qualifiedName}")
    public DataPlatformOMASAPIResponse getExternalDataPlatformByQualifiedName(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String qualifiedName) {
        return restAPI.getExternalDataPlatformByQualifiedName(serverName, userId, qualifiedName);
    }

    @PostMapping(path = "/deployed-database-schema")
    public GUIDResponse createDeployedDatabaseSchema(@PathVariable("serverName") String serverName,
                                                     @PathVariable("userId") String userId,
                                                     @RequestBody DeployedDatabaseSchemaRequestBody requestBody) {
        return restAPI.createDeployedDatabaseSchema(serverName, userId, requestBody);
    }


}
