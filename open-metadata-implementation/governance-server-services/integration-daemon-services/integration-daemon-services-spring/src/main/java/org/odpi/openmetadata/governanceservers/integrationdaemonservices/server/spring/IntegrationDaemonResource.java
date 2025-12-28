/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.*;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.IntegrationDaemonRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * IntegrationDaemonResource provides the server-side catcher for REST calls using Spring.
 * The integration daemon server routes these requests to the named integration services.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/integration-daemon")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Integration Daemon Services", description="The integration daemon services host one or more integration services that support " +
        "metadata exchange with third party technology.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/integration-daemon-services/"))

public class IntegrationDaemonResource
{
    private final IntegrationDaemonRESTServices restAPI = new IntegrationDaemonRESTServices();


    /**
     * Validate the connector and return its connector type.  The engine service does not need to
     * be running in the integration daemon in order for this call to be successful.  It only needs to be registered with the
     * integration daemon.
     *
     * @param serverName integration daemon server name
     * @param delegatingUserId external userId making request
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector type or
     *  InvalidParameterException the connector provider class name is not a valid connector fo this service
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException there was a problem detected by the integration service
     */
    @GetMapping(path = "/validate-connector/{connectorProviderClassName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="validateConnector",
            description="Validate the connector and return its connector type.  The engine service does not need to" +
                    " be running in the engine host in order for this call to be successful. " +
                    " The engine service only needs to be registered with the engine host.",
            externalDocs=@ExternalDocumentation(description="Governance Action Service",
                    url="https://egeria-project.org/concepts/governance-action-service"))

    public ConnectorReportResponse validateConnector(@PathVariable String serverName,
                                                     @PathVariable String connectorProviderClassName,
                                                     @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return restAPI.validateConnector(serverName, delegatingUserId, connectorProviderClassName);
    }


    /**
     * Pass an open lineage event to the integration daemon.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param serverName integration daemon server name
     * @param delegatingUserId external userId making request
     * @param event open lineage event to publish.
     */
    @PostMapping(path = "/publish-open-lineage-event")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="publishOpenLineageEvent",
            description="Sent an Open Lineage event to the integration daemon.  It will pass it on to the integration connectors that have" +
                    " registered a listener for open lineage events.",
            externalDocs=@ExternalDocumentation(description="Open Lineage Standard",
                    url="https://egeria-project.org/features/lineage-management/overview/#the-openlineage-standard"))

    VoidResponse publishOpenLineageEvent(@PathVariable String serverName,
                                         @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                         @RequestBody  String event)
    {
        return restAPI.publishOpenLineageEvent(serverName, delegatingUserId, event);
    }


    /**
     * Return the status of each of the integration services and integration groups running in the integration daemon.
     *
     * @param serverName integration daemon name
     * @param delegatingUserId external userId making request
     * @return list of statuses - one for each assigned integration services or integration group
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @GetMapping(path = "/status")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getIntegrationDaemonStatus",
            description="Return the status of each of the integration services and integration groups running in the integration daemon.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public IntegrationDaemonStatusResponse getIntegrationDaemonStatus(@PathVariable String   serverName,
                                                                      @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return restAPI.getIntegrationDaemonStatus(serverName, delegatingUserId);
    }


    /**
     * Retrieve the configuration properties of the named integration connector running in the integration daemon.
     *
     * @param serverName integration daemon server name
     * @param delegatingUserId external userId making request
     * @param connectorName name of a specific connector
     *
     * @return properties map or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @GetMapping(path = "/integration-connectors/{connectorName}/configuration-properties")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConfigurationProperties",
            description="Retrieve the configuration properties of the named integration connector running in the integration daemon.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public PropertiesResponse getConfigurationProperties(@PathVariable String serverName,
                                                         @PathVariable String connectorName,
                                                         @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return restAPI.getConfigurationProperties(serverName, delegatingUserId, connectorName);
    }


    /**
     * Update the configuration properties of the integration connectors, or specific integration connector if a
     * connector name is supplied.  This update is in memory and will not persist over a server restart.
     *
     * @param serverName integration daemon server name
     * @param delegatingUserId external userId making request
     * @param requestBody name of a specific connector or null for all connectors and the properties to change
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @PostMapping(path = "/integration-connectors/configuration-properties")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateConfigurationProperties",
            description="Update the configuration properties of the integration connectors, or specific integration connector if a connector name is supplied.  This update is in memory and will not persist over a server restart.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public  VoidResponse updateConfigurationProperties(@PathVariable String                               serverName,
                                                       @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                       @RequestBody  ConnectorConfigPropertiesRequestBody requestBody)
    {
        return restAPI.updateConfigurationProperties(serverName, delegatingUserId, requestBody);
    }


    /**
     * Update the endpoint network address for a specific integration connector.
     * This update is in memory and will not persist over a server restart.
     *
     * @param serverName integration daemon server name
     * @param delegatingUserId external userId making request
     * @param connectorName name of a specific connector
     * @param requestBody name of a specific connector or null for all connectors and the properties to change
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @PostMapping(path = "/integration-connectors/{connectorName}/endpoint-network-address")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateEndpointNetworkAddress",
            description="Update the endpoint network address for a specific integration connector.  This update is in memory and will not persist over a server restart.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public  VoidResponse updateEndpointNetworkAddress(@PathVariable String            serverName,
                                                      @PathVariable String            connectorName,
                                                      @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                      @RequestBody  StringRequestBody requestBody)
    {
        return restAPI.updateEndpointNetworkAddress(serverName, delegatingUserId, connectorName, requestBody);
    }


    /**
     * Update the connection for a specific integration connector.
     * This update is in memory and will not persist over a server restart.
     *
     * @param serverName integration daemon server name
     * @param delegatingUserId external userId making request
     * @param connectorName name of a specific connector
     * @param requestBody new connection object
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @PostMapping(path = "/integration-connectors/{connectorName}/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateConnectorConnection",
            description="Update the connection for a specific integration connector.  This update is in memory and will not persist over a server restart.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public  VoidResponse updateConnectorConnection(@PathVariable String     serverName,
                                                   @PathVariable String     connectorName,
                                                   @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                   @RequestBody  Connection requestBody)
    {
        return restAPI.updateConnectorConnection(serverName, delegatingUserId, connectorName, requestBody);
    }


    /**
     * Issue a refresh() request on all connectors running in the integration daemon, or a specific connector if the connector name is specified.
     *
     * @param serverName integration daemon server name
     * @param delegatingUserId external userId making request
     * @param requestBody optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are refreshed.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @PostMapping(path = "/integration-connectors/refresh")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="refreshConnectors",
            description="Issue a refresh() request on all connectors running in the integration daemon, or a specific connector if the connector name is specified.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public VoidResponse refreshConnectors(@PathVariable                  String          serverName,
                                          @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                          @RequestBody(required = false) NameRequestBody requestBody)
    {
        return restAPI.refreshConnectors(serverName, delegatingUserId, requestBody);
    }


    /**
     * Restart all connectors running in the integration daemon, or restart a specific connector if the connector name is specified.
     *
     * @param serverName integration daemon server name
     * @param delegatingUserId external userId making request
     * @param requestBody optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are refreshed.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @PostMapping(path = "/integration-connectors/restart")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="restartConnectors",
            description="Restart all connectors running in the integration daemon, or restart a specific connector if the connector name is specified.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public VoidResponse restartConnectors(@PathVariable                  String          serverName,
                                          @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                          @RequestBody(required = false) NameRequestBody requestBody)
    {
        return restAPI.restartConnectors(serverName, delegatingUserId, requestBody);
    }


    /**
     * Retrieve the description and status of the requested integration group.
     *
     * @param serverName integration daemon server name
     * @param delegatingUserId external userId making request
     * @param integrationGroupName name of integration group of interest
     * @return list of statuses - on for each assigned integration groups or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    @GetMapping(path = "/integration-groups/{integrationGroupName}/summary")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getIntegrationGroupSummary",
            description="Retrieve the description and status of the requested integration group.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public IntegrationGroupSummaryResponse getIntegrationGroupSummary(@PathVariable String serverName,
                                                                      @PathVariable String integrationGroupName,
                                                                      @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return restAPI.getIntegrationGroupSummary(serverName, delegatingUserId, integrationGroupName);
    }


    /**
     * Return a summary of each of the integration groups running in the integration daemon.
     *
     * @param serverName integration daemon server name
     * @param delegatingUserId external userId making request
     * @return list of statuses - one for each assigned integration groups
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    @GetMapping(path = "/integration-groups/summary")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getIntegrationGroupSummaries",
            description="Return a summary of each of the integration groups running in the integration daemon.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public IntegrationGroupSummariesResponse getIntegrationGroupSummaries(@PathVariable String serverName,
                                                                          @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return restAPI.getIntegrationGroupSummaries(serverName, delegatingUserId);
    }


    /**
     * Request that the integration group refresh its configuration by calling the metadata access server.
     * Changes to the connector configuration will result in the affected connectors being restarted.
     * This request is useful if the metadata access server has an outage, particularly while the
     * integration daemon is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server
     * @param delegatingUserId external userId making request
     * @param integrationGroupName unique name of the integration group
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  IntegrationGroupException there was a problem detected by the integration group.
     */
    @GetMapping(path = "/integration-groups/{integrationGroupName}/refresh-config")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="refreshIntegrationGroupConfig",
            description="Request that the integration group refresh its configuration by calling the metadata access server. " +
                    "Changes to the connector configuration will result in the affected connectors being restarted. " +
                    "This request is useful if the metadata access server has an outage, particularly while the " +
                    "integration daemon is initializing.  This request just ensures that the latest configuration " +
                    "is in use.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public  VoidResponse refreshIntegrationGroupConfig(@PathVariable String serverName,
                                                       @PathVariable String integrationGroupName,
                                                       @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return restAPI.refreshIntegrationGroupConfig(serverName, delegatingUserId, integrationGroupName);
    }
}
