/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.platformservices.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.platformservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.platformservices.server.OMAGServerOperationalServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OMAGServerOperationResource provides the REST API for controlling the start up, management and
 * shutdown of services in the OMAG Server.
 */
@RestController
@RequestMapping("/open-metadata/platform-services/users/{userId}/server-platform")

@Tag(name="Platform Services", description="The platform services provides the APIs for querying the Open Metadata and Governance (OMAG) " +
                                                   "Server Platform. It is able to start an stop OMAG Servers and discovering information " +
                                                   "about the OMAG Servers that the OMAG Server Platform is hosting.  " +
                                                   "It is also able to dynamically change the platform metadata security connector.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/services/platform-services/overview"))

public class OMAGServerOperationResource
{
    private final OMAGServerOperationalServices operationalServices = new OMAGServerOperationalServices();

    /*
     * ========================================================================================
     * Activate and deactivate the open metadata and governance capabilities in the OMAG Server
     */

    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @PostMapping(path = "/servers/{serverName}/instance")

    @Operation(summary="activateWithStoredConfig",
               description="Activate the named OMAG server using the appropriate configuration document found in the configuration store.",
            externalDocs=@ExternalDocumentation(description="Configuration Documents",
                    url="https://egeria-project.org/concepts/configuration-document"))

    public SuccessMessageResponse activateWithStoredConfig(@PathVariable String userId,
                                                           @PathVariable String serverName)
    {
        return operationalServices.activateWithStoredConfig(userId, serverName);
    }


    /**
     * Activate the open metadata and governance services using the supplied configuration
     * document.
     *
     * @param userId  user that is issuing the request
     * @param configuration  properties used to initialize the services
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @PostMapping(path = "/servers/{serverName}/instance/configuration")

    @Operation(summary="activateWithSuppliedConfig",
            description="Activate the named OMAG server using the supplied configuration document. This configuration " +
                    "document is added to the configuration store, over-writing any previous configuration for this server.",
            externalDocs=@ExternalDocumentation(description="Configuration Documents",
                    url="https://egeria-project.org/concepts/configuration-document"))

    public SuccessMessageResponse activateWithSuppliedConfig(@PathVariable String           userId,
                                                             @PathVariable String           serverName,
                                                             @RequestBody  OMAGServerConfig configuration)
    {
        return operationalServices.activateWithSuppliedConfig(userId, serverName, configuration);
    }


    /**
     * Temporarily shutdown the named OMAG server.  This server can be restarted as a later time.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/servers/{serverName}/instance")

    @Operation(summary="shutdownServer",
            description="Temporarily shutdown the named OMAG server.  This server can be restarted as a later time.")

    public VoidResponse shutdownServer(@PathVariable String  userId,
                                       @PathVariable String  serverName)
    {
        return operationalServices.shutdownServer(userId, serverName);
    }


    /**
     * Temporarily shutdown all running servers.
     *
     * @param userId  user that is issuing the request
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/servers/instance")

    @Operation(summary="shutdownAllServers",
               description="Shutdown all active servers.  They can be restarted as a later time.")

    public VoidResponse shutdownAllServers(@PathVariable String  userId)
    {
        return operationalServices.shutdownAllServers(userId);
    }


    /**
     * Permanently deactivate any active servers and unregister from
     * any cohorts.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/servers/{serverName}")

    @Operation(summary="shutdownAndUnregisterServer",
            description="Shutdown the named OMAG server.  The server will also be removed " +
                    "from any open metadata repository cohorts it has registered with.")

    public VoidResponse shutdownAndUnregisterServer(@PathVariable String  userId,
                                                    @PathVariable String  serverName)
    {
        return operationalServices.shutdownAndUnregisterServer(userId, serverName);
    }


    /**
     * Shutdown any active servers and unregister them from
     * any cohorts.
     *
     * @param userId  user that is issuing the request
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/servers")

    @Operation(summary="shutdownAndUnregisterAllServers",
               description="Shutdown all OMAG Servers that are active on the platform.  The servers will also be removed " +
                                   "from any open metadata repository cohorts they are registered with.")

    public VoidResponse shutdownAndUnregisterAllServers(@PathVariable String  userId)
    {
        return operationalServices.shutdownAndUnregisterAllServers(userId);
    }


    /**
     * Shutdown the platform.
     *
     * @param userId  user that is issuing the request
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/instance")

    @Operation(summary="shutdownPlatform",
               description="Shutdown this OMAG Server Platform.")

    public VoidResponse shutdownPlatform(@PathVariable String  userId)
    {
        return operationalServices.shutdownPlatform(userId);
    }


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
                    "configuration values actually being used in the running server. \n" +
                    "\n" +
                    "An InvalidParameterException is returned if the server is not running.",
            externalDocs=@ExternalDocumentation(description="Configuration Documents",
                    url="https://egeria-project.org/concepts/configuration-document"))

    public OMAGServerConfigResponse getActiveConfiguration(@PathVariable String           userId,
                                                           @PathVariable String           serverName)
    {
        return operationalServices.getActiveConfiguration(userId, serverName);
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
               description="Retrieve the status for a running instance of a server. The stored configuration " +
                                   "document may have changed since the server was started.  This operation makes it possible to verify that " +
                                   "all the services. \n" +
                                   "\n" +
                                   "An InvalidParameterException is returned if the server is not running.",
               externalDocs=@ExternalDocumentation(description="OMAG Server",
                                                   url="https://egeria-project.org/concepts/omag-server"))

    public OMAGServerStatusResponse getActiveServerStatus(@PathVariable String userId,
                                                          @PathVariable String serverName)
    {
        return operationalServices.getActiveServerStatus(userId, serverName);
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
               externalDocs=@ExternalDocumentation(description="Open Metadata Archives",
                    url="https://egeria-project.org/concepts/open-metadata-archives/"))

    public VoidResponse addOpenMetadataArchiveFile(@PathVariable String userId,
                                                   @PathVariable String serverName,
                                                   @RequestBody  String fileName)
    {
        return operationalServices.addOpenMetadataArchiveFile(userId, serverName, fileName);
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
            externalDocs=@ExternalDocumentation(description="Open Metadata Archives",
                    url="https://egeria-project.org/concepts/open-metadata-archives/"))

    public VoidResponse addOpenMetadataArchive(@PathVariable String     userId,
                                               @PathVariable String     serverName,
                                               @RequestBody  Connection connection)
    {
        return operationalServices.addOpenMetadataArchive(userId, serverName, connection);
    }
}
