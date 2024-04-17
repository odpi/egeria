/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serveroperations.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.serveroperations.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerServicesListResponse;
import org.odpi.openmetadata.serveroperations.server.OMAGServerOperationalServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OMAGServerResource provides the REST API for controlling the start-up, management and
 * shutdown of services in the OMAG Server.
 */
@RestController
@RequestMapping("/open-metadata/server-operations/users/{userId}")

@Tag(name="Server Operations", description="Server operations provides the APIs for querying the status of a running " +
                                                   "Open Metadata and Governance (OMAG) server.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/services/server-operations"))

public class OMAGServerResource
{
    private final OMAGServerOperationalServices         serverOperationalServices   = new OMAGServerOperationalServices();

    /*
     * =============================================================
     * Operational status and control
     */

    /**
     * Return the configuration used for the current active instance of the server.  Null is returned if
     * the server instance is not running.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return configuration properties used to initialize the server or null if not running or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @GetMapping(path = "/servers/{serverName}/instance/configuration")

    @Operation(summary="getActiveConfiguration",
            description="Retrieve the configuration document used to start a running instance of a server. The stored configuration " +
                    "document may have changed since the server was started.  This operation makes it possible to verify the " +
                    "configuration values actually being used in the running server. " +
                                "An InvalidParameterException is returned if the server is not running.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document"))

    public OMAGServerConfigResponse getActiveConfiguration(@PathVariable String           userId,
                                                           @PathVariable String           serverName)
    {
        return serverOperationalServices.getActiveConfiguration(userId, serverName);
    }



    /**
     * Return the status for the current active instance of the server.  Null is returned if
     * the server instance is not running.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return status of the server or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or not running or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @GetMapping(path = "/servers/{serverName}/instance/status")

    @Operation(summary="getActiveServerStatus",
               description="Retrieve the status for a running instance of a server. This includes the status of each running " +
                       "service. It is used to verify that the required services are running. " +
                       "An InvalidParameterException is returned if the server is not running.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/omag-server"))

    public OMAGServerStatusResponse getActiveServerStatus(@PathVariable String userId,
                                                          @PathVariable String serverName)
    {
        return serverOperationalServices.getActiveServerStatus(userId, serverName);
    }


    /**
     * Return the list of services that are active on a specific OMAG Server that is active on this OMAG Server Platform.
     *
     * @param userId name of the user making the request
     * @param serverName name of the server of interest
     * @return server name and list od services running within
     */
    @GetMapping(path = "/servers/{serverName}/services")
    @Operation( summary = "getActiveServiceListForServer",
                description="Return the list of services that are active on the named server",
                responses = {
                        @ApiResponse(responseCode = "200", description="List of server services",
                                     content = @Content(
                                             mediaType ="application/json",
                                             schema = @Schema(implementation= ServerServicesListResponse.class)
                                     )

                        )
                },
                externalDocs=@ExternalDocumentation(description="Further Information",
                                                    url="https://egeria-project.org/concepts/omag-server"))

    public ServerServicesListResponse getActiveServices(@Parameter(description="calling user") @PathVariable String    userId,
                                                        @Parameter(description="server name")  @PathVariable String    serverName)
    {
        return serverOperationalServices.getActiveServices(userId, serverName);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param fileName name of the open metadata archive file.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or fileName parameter.
     */
    @PostMapping(path = "/servers/{serverName}/instance/open-metadata-archives/file")

    @Operation(summary="addOpenMetadataArchiveFile",
               description="An open metadata archive contains metadata types and instances.  This operation loads an open metadata " +
                                   "archive that is readable through the connector identified by the connection.  " +
                                   "It can be used with OMAG servers that are of type Cohort Member.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/open-metadata-archives/"))

    public VoidResponse addOpenMetadataArchiveFile(@PathVariable String userId,
                                                   @PathVariable String serverName,
                                                   @RequestBody  String fileName)
    {
        return serverOperationalServices.addOpenMetadataArchiveFile(userId, serverName, fileName);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connection connection for the open metadata archive.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or connection parameter.
     */
    @PostMapping(path = "/servers/{serverName}/instance/open-metadata-archives/connection")

    @Operation(summary="addOpenMetadataArchive",
               description="An open metadata archive contains metadata types and instances.  This operation loads an open metadata " +
                                   "archive that is readable through the connector identified by the connection.  " +
                                   "It can be used with OMAG servers that are of type Cohort Member.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/open-metadata-archives/"))

    public VoidResponse addOpenMetadataArchive(@PathVariable String     userId,
                                               @PathVariable String     serverName,
                                               @RequestBody  Connection connection)
    {
        return serverOperationalServices.addOpenMetadataArchive(userId, serverName, connection);
    }
}
