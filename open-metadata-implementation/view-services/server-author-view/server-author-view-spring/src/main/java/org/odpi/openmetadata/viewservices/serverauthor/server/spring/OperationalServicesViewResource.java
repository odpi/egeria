package org.odpi.openmetadata.viewservices.serverauthor.server.spring;/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorConfigurationResponse;
import org.odpi.openmetadata.viewservices.serverauthor.services.ServerAuthorViewRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * OperationalServicesResource provides the REST API for controlling the start up, management and
 * shutdown of services in the OMAG Server.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/server-author/users/{userId}/servers/{serverToBeConfiguredName}")

@Tag(name="Server Author OMVS", description="The Server Author OMVS is for user interfaces supporting the creating and editing of OMAG Server Configuration Documents.",
     externalDocs=@ExternalDocumentation(description="Further information",
                                         url="https://egeria-project.org/services/omvs/server-author/overview"))


public class OperationalServicesViewResource
{
    private final ServerAuthorViewRESTServices operationalServices = new ServerAuthorViewRESTServices();

    /*
     * ========================================================================================
     * Activate and deactivate the open metadata and governance capabilities in the OMAG Server
     */

    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param destinationPlatformName Name of the platform where the server lives
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @PostMapping(path = "/instance")

    @Operation(summary="Activate server with stored configuration document",
               description="Activate the named OMAG server using the appropriate configuration document found in the configuration store.",
               externalDocs=@ExternalDocumentation(description="Configuration Documents",
                    url="https://egeria-project.org/concepts/configuration-document"))

    public ServerAuthorConfigurationResponse activateWithStoredConfig(@PathVariable String userId,
                                                                      @PathVariable String serverName,
                                                                      @PathVariable String destinationPlatformName,
                                                                      @PathVariable String serverToBeConfiguredName)
    {
        return operationalServices.activateWithStoredConfig(userId, serverName, destinationPlatformName, serverToBeConfiguredName );
    }

    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param destinationPlatformName Name of the platform where the server lives
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "/instance")

    @Operation(summary="Shutdown server",
            description="Temporarily shutdown the named OMAG server.  This server can be restarted as a later time.")

    public ServerAuthorConfigurationResponse deactivateTemporarily(@PathVariable String  userId,
                                              @PathVariable String  serverName,
                                              @PathVariable String  destinationPlatformName,
                                              @PathVariable String  serverToBeConfiguredName)
    {
        return operationalServices.deactivateTemporarily(userId, serverName, destinationPlatformName, serverToBeConfiguredName);
    }


    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param destinationPlatformName Name of the platform where the server lives
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @DeleteMapping(path = "")

    @Operation(summary="Shutdown and delete server",
            description="Permanently shutdown the named OMAG server and delete its configuration.  The server will also be removed " +
                    "from any open metadata repository cohorts it has registered with.")

    public SuccessMessageResponse deactivatePermanently(@PathVariable String  userId,
                                              @PathVariable String  serverName,
                                              @PathVariable String  destinationPlatformName,
                                              @PathVariable String  serverToBeConfiguredName)
    {
        return operationalServices.deactivatePermanently(userId, serverName, destinationPlatformName, serverToBeConfiguredName);
    }


    /*
    /*deploy
     * =============================================================
     * Operational status and control
     */

    /**
     * Return the configuration used for the current active instance of the server.  Null is returned if
     * the server instance is not running.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param destinationPlatformName Name of the platform where the server lives
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return configuration properties used to initialize the server or null if not running or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @GetMapping(path = "/instance/configuration")

    @Operation(summary="Retrieve active server's running configuration",
            description="Retrieve the configuration document used to start a running instance of a server. The stored configuration " +
                    "document may have changed since the server was started.  This operation makes it possible to verify the " +
                    "configuration values actually being used in the running server. \n" +
                    "\n" +
                    "Null is returned if the server is not running.",
            externalDocs=@ExternalDocumentation(description="Configuration Documents",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public ServerAuthorConfigurationResponse getActiveConfiguration(@PathVariable String  userId,
                                                           @PathVariable String           serverName,
                                                           @PathVariable String           destinationPlatformName,
                                                           @PathVariable String           serverToBeConfiguredName)
    {
        return operationalServices.getActiveConfiguration(userId, serverName, destinationPlatformName, serverToBeConfiguredName);
    }

}
