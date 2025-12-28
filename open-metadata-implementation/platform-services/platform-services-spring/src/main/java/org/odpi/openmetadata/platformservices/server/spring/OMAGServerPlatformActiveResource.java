/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;


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
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.platformservices.rest.ServerListResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerServicesListResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerStatusResponse;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformActiveServices;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * OMAGServerPlatformActiveResource allow an external caller to determine which servers are active on the
 * platform and the services that are active within them.
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

public class OMAGServerPlatformActiveResource
{
    private final OMAGServerPlatformActiveServices  platformAPI = new OMAGServerPlatformActiveServices();


    /**
     * Return the start time for this instance of the platform.
     *
     * @param delegatingUserId external userId making request
     * @return start date/time
     */
    @GetMapping(path = "/start-time")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getPlatformStartTime",
                description="Return the date/time that this platform started up",
                responses = {
                    @ApiResponse(responseCode = "200", description="ISO Date/Time",
                            content = @Content(
                                    mediaType ="application/json",
                                    schema = @Schema(implementation=Date.class)
                           )

                    )
                })
    public DateResponse getPlatformStartTime(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getPlatformStartTime(delegatingUserId);
    }



    /**
     * Retrieve the name of the organization running this platform.
     *
     * @param delegatingUserId external userId making request
     * @return String description
     */
    @GetMapping(path = "/organization-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getServerPlatformOrganizationName",
            description="Retrieve the name of the organization running this platform.",
            responses = {
                    @ApiResponse(responseCode = "200",description="OMAG Server Platform Owning Organization",
                            content = @Content(mediaType ="application/json"))
            })

    public StringResponse getServerPlatformOrganizationName(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getServerPlatformOrganizationName(delegatingUserId);
    }


    /**
     * Return the list of access services that are registered (supported) in this OMAG Server Platform
     * and can be configured for a metadata server.
     *
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services/access-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getRegisteredAccessServices",
            description="Retrieve a list of access services registered on this platform",
            responses = {
                    @ApiResponse(responseCode = "200", description="list of service descriptions",
                            content = @Content(
                                    mediaType ="application/json",
                                    schema = @Schema(implementation=RegisteredOMAGServicesResponse.class)
                            )

                    )
            })
    public RegisteredOMAGServicesResponse getRegisteredAccessServices(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getRegisteredAccessServices(delegatingUserId);
    }


    /**
     * Return the list of engine services that are implemented in this OMAG Server Platform
     * and can be configured for an engine host server.
     *
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services/engine-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getRegisteredEngineServices",
            description="Retrieve a list of engine services implemented in this platform",
            responses = {
                    @ApiResponse(responseCode = "200", description="list of service descriptions",
                            content = @Content(
                                    mediaType ="application/json",
                                    schema = @Schema(implementation=RegisteredOMAGServicesResponse.class)
                            )

                    )
            })
    public RegisteredOMAGServicesResponse getRegisteredEngineServices(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getRegisteredEngineServices(delegatingUserId);
    }


    /**
     * Return the list of view services that are registered (supported) in this OMAG Server Platform
     * and can be configured for a view server.
     *
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services/view-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getRegisteredViewServices",
            description="Retrieve a list of view services registered on this platform",
            responses = {
                    @ApiResponse(responseCode = "200", description="list of service descriptions",
                            content = @Content(
                                    mediaType ="application/json",
                                    schema = @Schema(implementation=RegisteredOMAGServicesResponse.class)
                            )

                    )
            })
    public RegisteredOMAGServicesResponse getRegisteredViewServices(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getRegisteredViewServices(delegatingUserId);
    }


    /**
     * Return the list of governance services that are registered (supported) in this OMAG Server Platform
     * and can be configured as part of a governance server.
     *
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services/governance-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getGovernanceServices",
            description="Retrieve a list of governance services registered on this platform",
            responses = {
                    @ApiResponse(responseCode = "200", description="list of service descriptions",
                            content = @Content(
                                    mediaType ="application/json",
                                    schema = @Schema(implementation=RegisteredOMAGServicesResponse.class)
                            )

                    )
            })
    public RegisteredOMAGServicesResponse getRegisteredGovernanceServices(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getRegisteredGovernanceServices(delegatingUserId);
    }


    /**
     * Return the list of common services that are registered (supported) in this OMAG Server Platform
     * and can be configured as part of any server.
     *
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services/common-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getCommonServices",
            description="Retrieve a list of common services registered on this platform",
            responses = {
                    @ApiResponse(responseCode = "200",description="list of service descriptions",
                            content = @Content(
                                    mediaType ="application/json",
                                    schema = @Schema(implementation=RegisteredOMAGServicesResponse.class)
                            )

                    )
            })
    public RegisteredOMAGServicesResponse getRegisteredCommonServices(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getRegisteredCommonServices(delegatingUserId);
    }


    /**
     * Return the list of all services that are supported in this OMAG Server Platform.
     *
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getAllServices",
            description="Retrieve a list of all services available on this platform",
            responses = {
                    @ApiResponse(responseCode = "200",description="list of service descriptions",
                            content = @Content(
                                    mediaType ="application/json",
                                    schema = @Schema(implementation=RegisteredOMAGServicesResponse.class)
                            )

                    )
            })
    public RegisteredOMAGServicesResponse getAllRegisteredServices(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getAllRegisteredServices(delegatingUserId);
    }


    /**
     * Return the connector type for the requested connector provider after validating that the
     * connector provider is available on the OMAGServerPlatform's class path.  This method is for tools that are configuring
     * connectors into an Egeria server.  It does not validate that the connector will load and initialize.
     *
     * @param connectorProviderClassName name of the connector provider class
     * @param delegatingUserId external userId making request
     * @return ConnectorType bean or exceptions that occur when trying to create the connector
     */
    @GetMapping(path = "/connector-types/{connectorProviderClassName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getConnectorType",
                description="Return the connector type for the requested connector provider after validating that the" +
                                    " connector provider is available on the OMAGServerPlatform's class path.  This method is for tools that are configuring" +
                                    " connectors into an Egeria server.  It does not validate that the connector will load and initialize.",
                responses = {
                        @ApiResponse(responseCode = "200",description="Connector type",
                                     content = @Content(
                                             mediaType ="application/json",
                                             schema = @Schema(implementation= OCFConnectorTypeResponse.class)
                                     )

                        )
                })
    public OCFConnectorTypeResponse getConnectorType(@Parameter(description="name of the connector provider class") @PathVariable String connectorProviderClassName,
                                                     @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getConnectorType(connectorProviderClassName, delegatingUserId);
    }


    /**
     * Return a flag to indicate if this server has ever run on this OMAG Server Platform instance.
     *
     * @param serverName server of interest
     * @param delegatingUserId external userId making request
     * @return flag
     */
    @GetMapping(path = "/servers/{serverName}/is-known")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "isServerKnown",
            description="Return a boolean indication if this server has ever run on this platform instance",
            responses = {
                    @ApiResponse(responseCode = "200",description="boolean flag",
                            content = @Content(
                                    mediaType ="application/json",
                                    schema = @Schema(implementation=RegisteredOMAGServicesResponse.class)
                            )

                    )
            })
    public BooleanResponse isServerKnown(@Parameter(description="server name") @PathVariable String    serverName,
                                         @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.isServerKnown(serverName, delegatingUserId);
    }


    /**
     * Return the list of OMAG Servers that have run or are running in this OMAG Server Platform.
     *
     * @param delegatingUserId external userId making request
     * @return list of OMAG server names
     */
    @GetMapping(path = "/servers")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getKnownServerList",
            description="Return the list of servers that have run or are running on this platform",
            responses = {
                    @ApiResponse(responseCode = "200",description="list of servers",
                            content = @Content(
                                    mediaType ="application/json",
                                    schema = @Schema(implementation=ServerListResponse.class)
                            )

                    )
            })
    public ServerListResponse getKnownServerList(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getKnownServerList(delegatingUserId);
    }


    /**
     * Return the list of OMAG Servers that are active on this OMAG Server Platform.
     *
     * @param delegatingUserId external userId making request
     * @return list of server names
     */
    @GetMapping(path = "/servers/active")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getActiveServerList",
            description="Return the list of servers that are active on this platform",
            responses = {
                    @ApiResponse(responseCode = "200",description="list of servers",
                            content = @Content(
                                    mediaType ="application/json",
                                    schema = @Schema(implementation= ServerListResponse.class)
                            )

                    )
            })
    public ServerListResponse getActiveServerList(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getActiveServerList(delegatingUserId);
    }


    /**
     * Return information about when the server has been active.
     *
     * @param serverName name of the server of interest
     * @param delegatingUserId external userId making request
     * @return details of the server status
     */
    @GetMapping(path = "/servers/{serverName}/status")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getServerStatus",
            description="Return information about when the server has been active",
            responses = {
                    @ApiResponse(responseCode = "200",description="details of server status",
                            content = @Content(
                                    mediaType ="application/json",
                            schema = @Schema(implementation=ServerStatusResponse.class)
                            )
                    )
            })
    public ServerStatusResponse getServerStatus(@Parameter(description="server name")  @PathVariable String    serverName,
                                                @Parameter(description="delegating user id")  @RequestParam(required = false) String    delegatingUserId)
    {
        return platformAPI.getServerStatus(serverName, delegatingUserId);
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

    @Operation( summary = "getActiveServicesForServer",
            description="Return the list of services that are active on the server on this platform",
            responses = {
                    @ApiResponse(responseCode = "200",description="details of server status",
                            content = @Content(
                                    mediaType ="application/json",
                                    schema = @Schema(implementation=ServerServicesListResponse.class)
                            )

                    )
            })
    public ServerServicesListResponse getActiveServicesForServer(@Parameter(description="server name")  @PathVariable String    serverName,
                                                                 @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return platformAPI.getActiveServicesForServer(serverName, delegatingUserId);
    }
}
