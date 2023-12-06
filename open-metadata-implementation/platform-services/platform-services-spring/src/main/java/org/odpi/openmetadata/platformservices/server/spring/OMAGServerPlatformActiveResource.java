/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.platformservices.rest.ServerListResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerServicesListResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerStatusResponse;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformActiveServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OMAGServerPlatformActiveResource allow an external caller to determine which servers are active on the
 * platform and the services that are active within them.
 */


@RestController
@RequestMapping("/open-metadata/platform-services/users/{userId}/server-platform")

@Tag(name="Platform Services", description="The platform services provides the APIs for querying the Open Metadata and Governance (OMAG) " +
                                                   "Server Platform. It is able to start an stop OMAG Servers and discovering information " +
                                                   "about the OMAG Servers that the OMAG Server Platform is hosting.  " +
                                                   "It is also able to dynamically change the platform metadata security connector.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/services/platform-services/overview"))

public class OMAGServerPlatformActiveResource
{
    private final OMAGServerPlatformActiveServices  platformAPI = new OMAGServerPlatformActiveServices();


    /**
     * Return the list of access services that are registered (supported) in this OMAG Server Platform
     * and can be configured for a metadata server.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services/access-services")
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
    public RegisteredOMAGServicesResponse getRegisteredAccessServices(@Parameter(description="calling user") @PathVariable String userId)
    {
        return platformAPI.getRegisteredAccessServices(userId);
    }



    /**
     * Return the list of engine services that are implemented in this OMAG Server Platform
     * and can be configured for an engine host server.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services/engine-services")
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
    public RegisteredOMAGServicesResponse getRegisteredEngineServices(@Parameter(description="calling user") @PathVariable String userId)
    {
        return platformAPI.getRegisteredEngineServices(userId);
    }




    /**
     * Return the list of integration services that are implemented in this OMAG Server Platform
     * and can be configured for an integration daemon server.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services/integration-services")
    @Operation( summary = "getRegisteredIntegrationServices",
                description="Retrieve a list of integration services implemented in this platform",
                responses = {
                        @ApiResponse(responseCode = "200", description="list of service descriptions",
                                     content = @Content(
                                             mediaType ="application/json",
                                             schema = @Schema(implementation=RegisteredOMAGServicesResponse.class)
                                     )

                        )
                })
    public RegisteredOMAGServicesResponse getRegisteredIntegrationServices(@Parameter(description="calling user") @PathVariable String userId)
    {
        return platformAPI.getRegisteredIntegrationServices(userId);
    }



    /**
     * Return the list of view services that are registered (supported) in this OMAG Server Platform
     * and can be configured for a view server.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services/view-services")

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
    public RegisteredOMAGServicesResponse getRegisteredViewServices(@Parameter(description="calling user") @PathVariable String userId)
    {
        return platformAPI.getRegisteredViewServices(userId);
    }


    /**
     * Return the list of governance services that are registered (supported) in this OMAG Server Platform
     * and can be configured as part of a governance server.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services/governance-services")
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
    public RegisteredOMAGServicesResponse getRegisteredGovernanceServices(@Parameter(description="calling user") @PathVariable String userId)
    {
        return platformAPI.getRegisteredGovernanceServices(userId);
    }


    /**
     * Return the list of common services that are registered (supported) in this OMAG Server Platform
     * and can be configured as part of any server.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services/common-services")
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
    public RegisteredOMAGServicesResponse getRegisteredCommonServices(@Parameter(description="calling user") @PathVariable String userId)
    {
        return platformAPI.getRegisteredCommonServices(userId);
    }


    /**
     * Return the list of all services that are supported in this OMAG Server Platform.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    @GetMapping(path = "/registered-services")
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
    public RegisteredOMAGServicesResponse getAllRegisteredServices(@Parameter(description="calling user") @PathVariable String userId)
    {
        return platformAPI.getAllRegisteredServices(userId);
    }


    /**
     * Return the connector type for the requested connector provider after validating that the
     * connector provider is available on the OMAGServerPlatform's class path.  This method is for tools that are configuring
     * connectors into an Egeria server.  It does not validate that the connector will load and initialize.
     *
     * @param userId calling user
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean or exceptions that occur when trying to create the connector
     */
    @GetMapping(path = "/connector-types/{connectorProviderClassName}")
    @Operation( summary = "getConnectorType",
                description="Return the connector type for the requested connector provider after validating that the" +
                                    " connector provider is available on the OMAGServerPlatform's class path.  This method is for tools that are configuring" +
                                    " connectors into an Egeria server.  It does not validate that the connector will load and initialize.",
                responses = {
                        @ApiResponse(responseCode = "200",description="Connector type",
                                     content = @Content(
                                             mediaType ="application/json",
                                             schema = @Schema(implementation=ConnectorTypeResponse.class)
                                     )

                        )
                })
    public ConnectorTypeResponse getConnectorType(@Parameter(description="calling user")                         @PathVariable String userId,
                                                  @Parameter(description="name of the connector provider class") @PathVariable String connectorProviderClassName)
    {
        return platformAPI.getConnectorType(userId, connectorProviderClassName);
    }


    /**
     * Return a flag to indicate if this server has ever run on this OMAG Server Platform instance.
     *
     * @param userId calling user
     * @param serverName server of interest
     * @return flag
     */
    @GetMapping(path = "/servers/{serverName}/is-known")
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
    public BooleanResponse isServerKnown(@Parameter(description="calling user") @PathVariable String    userId,
                                         @Parameter(description="server name") @PathVariable String    serverName)
    {
        return platformAPI.isServerKnown(userId, serverName);
    }


    /**
     * Return the list of OMAG Servers that have run or are running in this OMAG Server Platform.
     *
     * @param userId calling user
     * @return list of OMAG server names
     */
    @GetMapping(path = "/servers")
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
    public ServerListResponse getKnownServerList(@Parameter(description="calling user") @PathVariable String userId)
    {
        return platformAPI.getKnownServerList(userId);
    }


    /**
     * Return the list of OMAG Servers that are active on this OMAG Server Platform.
     *
     * @param userId name of the user making the request
     * @return list of server names
     */
    @GetMapping(path = "/servers/active")
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
    public ServerListResponse getActiveServerList(@Parameter(description="calling user") @PathVariable String    userId)
    {
        return platformAPI.getActiveServerList(userId);
    }


    /**
     * Return information about when the server has been active.
     *
     * @param userId name of the user making the request
     * @param serverName name of the server of interest
     * @return details of the server status
     */
    @GetMapping(path = "/servers/{serverName}/status")

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
    public ServerStatusResponse getServerStatus(@Parameter(description="calling user") @PathVariable String    userId,
                                                @Parameter(description="server name")  @PathVariable String    serverName)
    {
        return platformAPI.getServerStatus(userId, serverName);
    }


    /**
     * Return the list of services that are active on a specific OMAG Server that is active on this OMAG Server Platform.
     *
     * @param userId name of the user making the request
     * @param serverName name of the server of interest
     * @return server name and list od services running within
     */
    @GetMapping(path = "/servers/{serverName}/services")
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
    public ServerServicesListResponse getActiveServicesForServer(@Parameter(description="calling user") @PathVariable String    userId,
                                                                 @Parameter(description="server name")  @PathVariable String    serverName)
    {
        return platformAPI.getActiveServicesForServer(userId, serverName);
    }
}
