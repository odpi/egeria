/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformOperationalServices;
import org.odpi.openmetadata.serveroperations.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.serveroperations.rest.SuccessMessageResponse;
import org.odpi.openmetadata.serveroperations.server.OMAGServerOperationalServices;
import org.springframework.web.bind.annotation.*;

/**
 * OMAGServerOperationResource provides the REST API for controlling the start-up, management and
 * shutdown of services in the OMAG Server.
 */
@RestController
@RequestMapping("/open-metadata/platform-services/server-platform")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Platform Services", description="The platform services provides the APIs for querying the Open Metadata and Governance (OMAG) " +
                                                   "Server Platform. It is able to start an stop OMAG Servers and discovering information " +
                                                   "about the OMAG Servers that the OMAG Server Platform is hosting.  " +
                                                   "It is also able to dynamically change the platform metadata security connector.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/services/platform-services/overview"))

public class OMAGServerOperationResource
{
    private final OMAGServerOperationalServices         serverOperationalServices   = new OMAGServerOperationalServices();
    private final OMAGServerPlatformOperationalServices platformOperationalServices = new OMAGServerPlatformOperationalServices();

    /*
     * ========================================================================================
     * Activate and deactivate the open metadata and governance capabilities in the OMAG Server
     */

    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @PostMapping(path = "/servers/{serverName}/instance")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="activateWithStoredConfig",
               description="Activate the named OMAG server using the appropriate configuration document found in the configuration store.",
            externalDocs=@ExternalDocumentation(description="Configuration Documents",
                    url="https://egeria-project.org/concepts/configuration-document"))

    public SuccessMessageResponse activateWithStoredConfig(@PathVariable String serverName,
                                                           @RequestParam (required = false) String delegatingUserId)
    {
        return serverOperationalServices.activateWithStoredConfig(serverName, delegatingUserId);
    }


    /**
     * Activate the open metadata and governance services using the supplied configuration
     * document.
     *
     * @param configuration  properties used to initialize the services
     * @param delegatingUserId external userId making request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @PostMapping(path = "/servers/{serverName}/instance/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="activateWithSuppliedConfig",
            description="Activate the named OMAG server using the supplied configuration document. This configuration " +
                    "document is added to the configuration store, over-writing any previous configuration for this server.",
            externalDocs=@ExternalDocumentation(description="Configuration Documents",
                    url="https://egeria-project.org/concepts/configuration-document"))

    public SuccessMessageResponse activateWithSuppliedConfig(@PathVariable String           serverName,
                                                             @RequestParam(required = false) String delegatingUserId,
                                                             @RequestBody  OMAGServerConfig configuration)
    {
        return serverOperationalServices.activateWithSuppliedConfig(serverName, delegatingUserId, configuration);
    }


    /**
     * Temporarily shutdown the named OMAG server.  This server can be restarted as a later time.
     *
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/servers/{serverName}/instance")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="shutdownServer",
            description="Temporarily shutdown the named OMAG server.  This server can be restarted as a later time.")

    public VoidResponse shutdownServer(@PathVariable String  serverName,
                                       @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return serverOperationalServices.shutdownServer(serverName, delegatingUserId);
    }


    /**
     * Temporarily shutdown all running servers.
     *
     * @param delegatingUserId external userId making request
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/servers/instance")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="shutdownAllServers",
               description="Shutdown all active servers.  They can be restarted as a later time.")

    public VoidResponse shutdownAllServers(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformOperationalServices.shutdownAllServers(delegatingUserId);
    }


    /**
     * Permanently deactivate any active servers and unregister from
     * any cohorts.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/servers/{serverName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="shutdownAndUnregisterServer",
            description="Shutdown the named OMAG server.  The server will also be removed " +
                    "from any open metadata repository cohorts it has registered with.")

    public VoidResponse shutdownAndUnregisterServer(@PathVariable String  serverName,
                                                    @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return serverOperationalServices.shutdownAndUnregisterServer(serverName, delegatingUserId);
    }


    /**
     * Shutdown any active servers and unregister them from
     * any cohorts.
     *
     * @param delegatingUserId external userId making request
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/servers")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="shutdownAndUnregisterAllServers",
               description="Shutdown all OMAG Servers that are active on the platform.  The servers will also be removed " +
                                   "from any open metadata repository cohorts they are registered with.")

    public VoidResponse shutdownAndUnregisterAllServers(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformOperationalServices.shutdownAndUnregisterAllServers(delegatingUserId);
    }


    /**
     * Shutdown the platform.
     *
     * @param delegatingUserId external userId making request
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/instance")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="shutdownPlatform",
               description="Shutdown this OMAG Server Platform.")

    public VoidResponse shutdownPlatform(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformOperationalServices.shutdownPlatform(delegatingUserId);
    }


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
                    "configuration values actually being used in the running server. An InvalidParameterException is returned if the server is not running.",
            externalDocs=@ExternalDocumentation(description="Configuration Documents",
                    url="https://egeria-project.org/concepts/configuration-document"))

    public OMAGServerConfigResponse getActiveConfiguration(@PathVariable String           serverName,
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
               description="Retrieve the status for a running instance of a server. The stored configuration " +
                                   "document may have changed since the server was started.  This operation makes it possible to verify that " +
                                   "all the services. An InvalidParameterException is returned if the server is not running.",
               externalDocs=@ExternalDocumentation(description="OMAG Server",
                                                   url="https://egeria-project.org/concepts/omag-server"))

    public OMAGServerStatusResponse getActiveServerStatus(@PathVariable String serverName,
                                                          @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return serverOperationalServices.getActiveServerStatus(serverName, delegatingUserId);
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
                                   "It can be used with OMAG servers that are of type Cohort Member.",
               externalDocs=@ExternalDocumentation(description="Open Metadata Archives",
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
                                   "It can be used with OMAG servers that are of type Cohort Member.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Archives",
                    url="https://egeria-project.org/concepts/open-metadata-archives/"))

    public VoidResponse addOpenMetadataArchive(@PathVariable String     serverName,
                                               @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                               @RequestBody  Connection connection)
    {
        return serverOperationalServices.addOpenMetadataArchive(serverName, delegatingUserId, connection);
    }
}
