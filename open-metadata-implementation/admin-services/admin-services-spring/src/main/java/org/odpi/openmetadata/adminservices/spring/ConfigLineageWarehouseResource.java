/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.server.OMAGServerConfigOpenLineage;
import org.odpi.openmetadata.adminservices.configuration.properties.LineageWarehouseConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * ConfigLineageWarehouseResource provides the configuration for setting up the Lineage Warehouse server.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/servers/"))

public class ConfigLineageWarehouseResource
{
    private final OMAGServerConfigOpenLineage adminAPI = new OMAGServerConfigOpenLineage();


    /**
     * Set up the lineage warehouse services configuration.
     *
     * @param userId             user that is issuing the request.
     * @param serverName         local server name.
     * @param lineageWarehouseConfig configuration properties for open lineage server
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @PostMapping(path = "/lineage-warehouse/configuration")

    @Operation(summary="setLineageWarehouseServices",
               description="Set up the lineage warehouse services configuration.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/lineage-warehouse/"))

    public VoidResponse setLineageWarehouseServices(@PathVariable String                 userId,
                                                    @PathVariable String                 serverName,
                                                    @RequestBody  LineageWarehouseConfig lineageWarehouseConfig)
    {
        return adminAPI.setLineageWarehouseServices(userId, serverName, lineageWarehouseConfig);
    }


    /**
     * Remove the lineage warehouse services from the server configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     */
    @DeleteMapping(path = "/lineage-warehouse/configuration")

    @Operation(summary="removeLineageWarehouseServices",
               description="Set up the lineage warehouse services configuration.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/lineage-warehouse/"))

    public VoidResponse removeLineageWarehouseServices(@PathVariable String userId,
                                                       @PathVariable String serverName)
    {
        return adminAPI.removeLineageWarehouseServices(userId, serverName);
    }
}
