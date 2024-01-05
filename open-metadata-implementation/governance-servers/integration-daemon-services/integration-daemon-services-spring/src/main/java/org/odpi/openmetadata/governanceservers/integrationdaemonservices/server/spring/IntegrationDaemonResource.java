/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.PropertiesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.*;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.IntegrationDaemonRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * IntegrationDaemonResource provides the server-side catcher for REST calls using Spring.
 * The integration daemon server routes these requests to the named integration services.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/integration-daemon/users/{userId}")

@Tag(name="Integration Daemon Services", description="The integration daemon services host one or more integration services that support " +
        "metadata exchange with third party technology.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/integration-daemon-services/"))

public class IntegrationDaemonResource
{
    private final IntegrationDaemonRESTServices restAPI = new IntegrationDaemonRESTServices();


    /**
     * Return a summary of each of the integration services' and integration groups' status.
     *
     * @param serverName integration daemon name
     * @param userId calling user
     * @return list of statuses - one for each assigned integration services or integration group
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @GetMapping(path = "/status")

    public IntegrationDaemonStatusResponse getIntegrationDaemonStatus(@PathVariable String   serverName,
                                                                      @PathVariable String   userId)
    {
        return restAPI.getIntegrationDaemonStatus(serverName, userId);
    }


    /**
     * Retrieve the configuration properties of the named connector.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param connectorName name of a specific connector
     *
     * @return properties map or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @GetMapping(path = "/integration-connectors/{connectorName}/configuration-properties")

    public PropertiesResponse getConfigurationProperties(@PathVariable String serverName,
                                                         @PathVariable String userId,
                                                         @PathVariable String connectorName)
    {
        return restAPI.getConfigurationProperties(serverName, userId, connectorName);
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param requestBody name of a specific connector or null for all connectors and the properties to change
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @PostMapping(path = "/integration-connectors/configuration-properties")

    public  VoidResponse updateConfigurationProperties(@PathVariable String                               serverName,
                                                       @PathVariable String                               userId,
                                                       @RequestBody  ConnectorConfigPropertiesRequestBody requestBody)
    {
        return restAPI.updateConfigurationProperties(serverName, userId, requestBody);
    }


    /**
     * Update the endpoint network address for a specific integration connector.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param connectorName name of a specific connector
     * @param requestBody name of a specific connector or null for all connectors and the properties to change
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @PostMapping(path = "/integration-connectors/{connectorName}/endpoint-network-address")

    public  VoidResponse updateEndpointNetworkAddress(@PathVariable String            serverName,
                                                      @PathVariable String            userId,
                                                      @PathVariable String            connectorName,
                                                      @RequestBody  StringRequestBody requestBody)
    {
        return restAPI.updateEndpointNetworkAddress(serverName, userId, connectorName, requestBody);
    }


    /**
     * Update the connection for a specific integration connector.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param connectorName name of a specific connector
     * @param requestBody new connection object
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @PostMapping(path = "/integration-connectors/{connectorName}/connection")

    public  VoidResponse updateConnectorConnection(@PathVariable String     serverName,
                                                   @PathVariable String     userId,
                                                   @PathVariable String     connectorName,
                                                   @RequestBody  Connection requestBody)
    {
        return restAPI.updateConnectorConnection(serverName, userId, connectorName,requestBody);
    }


    /**
     * Issue a refresh() request on all connectors running in the integration daemon, or a specific connector if the connector name is specified.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param requestBody optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are refreshed.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @PostMapping(path = "/integration-connectors/refresh")

    public VoidResponse refreshConnectors(@PathVariable                  String          serverName,
                                          @PathVariable                  String          userId,
                                          @RequestBody(required = false) NameRequestBody requestBody)
    {
        return restAPI.refreshConnectors(serverName, userId, requestBody);
    }


    /**
     * Issue a refresh() request on all connectors running in the integration daemon, or a specific connector if the connector name is specified.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param requestBody optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are refreshed.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @PostMapping(path = "/integration-connectors/restart")

    public VoidResponse restartConnectors(@PathVariable                  String          serverName,
                                          @PathVariable                  String          userId,
                                          @RequestBody(required = false) NameRequestBody requestBody)
    {
        return restAPI.restartConnectors(serverName, userId, requestBody);
    }


    /**
     * Process a refresh request.  This calls refresh on all connectors within the integration service.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param serviceURLMarker integration service identifier
     * @param requestBody optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are refreshed.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @PostMapping(path = "/integration-services/{serviceURLMarker}/refresh")

    public VoidResponse refreshService(@PathVariable                  String          serverName,
                                       @PathVariable                  String          userId,
                                       @PathVariable                  String          serviceURLMarker,
                                       @RequestBody(required = false) NameRequestBody requestBody)
    {
        return restAPI.refreshService(serverName, userId, serviceURLMarker, requestBody);
    }


    /**
     * Request that the integration service shutdown and recreate its integration connectors.
     *
     * @param serverName name of the integration daemon
     * @param userId identifier of calling user
     * @param serviceURLMarker unique name of the integration service
     * @param requestBody name of a specific connector to restart - if null all connectors are restarted.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @PostMapping(path = "/integration-services/{serviceURLMarker}/restart")

    public  VoidResponse restartService(@PathVariable                  String          serverName,
                                        @PathVariable                  String          userId,
                                        @PathVariable                  String          serviceURLMarker,
                                        @RequestBody(required = false) NameRequestBody requestBody)
    {
        return restAPI.restartService(serverName, userId, serviceURLMarker, requestBody);
    }




    /**
     * Return a summary of each of the integration services' status.
     *
     * @param serverName integration daemon name
     * @param userId calling user
     * @return list of statuses - on for each assigned integration services
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @GetMapping(path = "/integration-services/summary")

    public IntegrationServiceSummaryResponse getIntegrationServicesSummaries(@PathVariable String   serverName,
                                                                             @PathVariable String   userId)
    {
        return restAPI.getIntegrationServicesSummaries(serverName, userId);
    }


    /**
     * Retrieve the description and status of the requested integration group.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param integrationGroupName name of integration group of interest
     * @return list of statuses - on for each assigned integration groups or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    @GetMapping(path = "/integration-groups/{integrationGroupName}/summary")

    public IntegrationGroupSummaryResponse getIntegrationGroupSummary(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String integrationGroupName)
    {
        return restAPI.getIntegrationGroupSummary(serverName, userId, integrationGroupName);
    }



    /**
     * Return a summary of each of the integration groups running in the integration daemon.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @return list of statuses - one for each assigned integration groups
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    @GetMapping(path = "/integration-groups/summary")

    public IntegrationGroupSummariesResponse getIntegrationGroupSummaries(@PathVariable String serverName,
                                                                          @PathVariable String userId)
    {
        return restAPI.getIntegrationGroupSummaries(serverName, userId);
    }


    /**
     * Request that the integration group refresh its configuration by calling the metadata server.
     * Changes to the connector configuration will result in the affected connectors being restarted.
     * This request is useful if the metadata server has an outage, particularly while the
     * integration daemon is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server
     * @param userId identifier of calling user
     * @param integrationGroupName unique name of the integration group
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  IntegrationGroupException there was a problem detected by the integration group.
     */
    @GetMapping(path = "/integration-groups/{integrationGroupName}/refresh-config")

    public  VoidResponse refreshIntegrationGroupConfig(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String integrationGroupName)
    {
        return restAPI.refreshIntegrationGroupConfig(serverName, userId, integrationGroupName);
    }
}
