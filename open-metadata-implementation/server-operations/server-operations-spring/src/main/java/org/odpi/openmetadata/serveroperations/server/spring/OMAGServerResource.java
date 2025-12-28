/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serveroperations.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.serveroperations.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerServicesListResponse;
import org.odpi.openmetadata.serveroperations.server.OMAGServerOperationalServices;
import org.springframework.web.bind.annotation.*;

/**
 * OMAGServerResource provides the REST API for controlling the start-up, management and
 * shutdown of services in the OMAG Server.
 */
@RestController
@RequestMapping("/open-metadata/server-operations")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

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
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @return configuration properties used to initialize the server or null if not running or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @GetMapping(path = "/servers/{serverName}/instance/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActiveConfiguration",
            description="Retrieve the configuration document used to start a running instance of a server. The stored configuration " +
                    "document may have changed since the server was started.  This operation makes it possible to verify the " +
                    "configuration values actually being used in the running server. " +
                                "An InvalidParameterException is returned if the server is not running.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document"))

    public OMAGServerConfigResponse getActiveConfiguration(@PathVariable String serverName,
                                                           @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return serverOperationalServices.getActiveConfiguration(serverName, delegatingUserId);
    }



    /**
     * Return the status for the current active instance of the server.  Null is returned if
     * the server instance is not running.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @return status of the server or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or not running or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @GetMapping(path = "/servers/{serverName}/instance/status")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActiveServerStatus",
               description="Retrieve the status for a running instance of a server. This includes the status of each running " +
                       "service. It is used to verify that the required services are running. " +
                       "An InvalidParameterException is returned if the server is not running.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/omag-server"))

    public OMAGServerStatusResponse getActiveServerStatus(@PathVariable String serverName,
                                                          @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return serverOperationalServices.getActiveServerStatus(serverName, delegatingUserId);
    }


    /**
     * Return the list of services that are active on a specific OMAG Server that is active on this OMAG Server Platform.
     *
     * @param serverName name of the server of interest
     * @param delegatingUserId external userId making request
     * @return server name and list od services running within
     */
    @GetMapping(path = "/servers/{serverName}/services")
    @SecurityRequirement(name = "BearerAuthorization")

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

    public ServerServicesListResponse getActiveServices(@Parameter(description="server name")  @PathVariable String    serverName,
                                                        @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return serverOperationalServices.getActiveServices(serverName, delegatingUserId);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param fileName name of the open metadata archive file.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or fileName parameter.
     */
    @PostMapping(path = "/servers/{serverName}/instance/open-metadata-archives/file")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addOpenMetadataArchiveFile",
               description="An open metadata archive contains metadata types and instances.  This operation loads an open metadata " +
                                   "archive that is readable through the connector identified by the connection.  " +
                                   "It can be used with OMAG servers that are of type Open Metadata Store.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/open-metadata-archives/"))

    public VoidResponse addOpenMetadataArchiveFile(@PathVariable String serverName,
                                                   @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                   @RequestBody  String fileName)
    {
        return serverOperationalServices.addOpenMetadataArchiveFile(serverName, delegatingUserId, fileName);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param connection connection for the open metadata archive.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or connection parameter.
     */
    @PostMapping(path = "/servers/{serverName}/instance/open-metadata-archives/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addOpenMetadataArchive",
               description="An open metadata archive contains metadata types and instances.  This operation loads an open metadata " +
                                   "archive that is readable through the connector identified by the connection.  " +
                                   "It can be used with OMAG servers that are of type Open Metadata Store.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/open-metadata-archives/"))

    public VoidResponse addOpenMetadataArchive(@PathVariable String     serverName,
                                               @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                               @RequestBody  Connection connection)
    {
        return serverOperationalServices.addOpenMetadataArchive(serverName, delegatingUserId, connection);
    }



    /**
     * Add a new open metadata archive to running repository.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param openMetadataArchive openMetadataArchive for the open metadata archive.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or openMetadataArchive parameter.
     */
    @PostMapping(path = "/servers/{serverName}/instance/open-metadata-archives/archive-content")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addOpenMetadataArchiveContent",
            description="An open metadata archive contains metadata types and instances.  This operation loads the supplied open metadata " +
                    "archive into the local repository.  It can be used with OMAG servers that are of type Open Metadata Store.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/open-metadata-archives/"))

    public VoidResponse addOpenMetadataArchive(@PathVariable String             serverName,
                                               @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                               @RequestBody OpenMetadataArchive openMetadataArchive)
    {
        return serverOperationalServices.addOpenMetadataArchive(serverName, delegatingUserId, openMetadataArchive);
    }
}
