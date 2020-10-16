/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.IntegrationDaemonStatusResponse;
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
        externalDocs=@ExternalDocumentation(description="Integration Daemon Services",
                url="https://egeria.odpi.org/open-metadata-implementation/governance-servers/integration-daemon-services/"))

public class IntegrationDaemonResource
{
    private IntegrationDaemonRESTServices restAPI = new IntegrationDaemonRESTServices();


    /**
     * Process a refresh request.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @PostMapping(path = "/refresh")

    public VoidResponse refreshAllServices(@PathVariable                  String          serverName,
                                           @PathVariable                  String          userId,
                                           @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.refreshAllServices(serverName, userId, requestBody);
    }


    /**
     * Process a refresh request.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param serviceURLMarker integration service identifier
     * @param connectorName optional name of the connector to target - if no connector name is specified, all
     *                      connectors managed by this integration service are refreshed.
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @PostMapping(path = "/integration-service/{serviceURLMarker}/refresh")

    public VoidResponse refreshService(@PathVariable                   String   serverName,
                                       @PathVariable                   String   userId,
                                       @PathVariable                   String   serviceURLMarker,
                                       @RequestBody(required = false)  String   connectorName)
    {
        return restAPI.refreshService(serverName, userId, serviceURLMarker, connectorName);
    }

    /**
     * Request that the integration service shutdown and recreate its integration connectors.
     *
     * @param serverName name of the integration daemon
     * @param userId identifier of calling user
     * @param serviceURLMarker unique name of the integration service
     * @param connectorName name of a specific connector to refresh - if null all connectors are restarted.
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @PostMapping(path = "/integration-service/{serviceURLMarker}/restart")

    public  VoidResponse restartService(@PathVariable                   String   serverName,
                                        @PathVariable                   String   userId,
                                        @PathVariable                   String   serviceURLMarker,
                                        @RequestBody(required = false)  String   connectorName)
    {
        return restAPI.restartService(serverName, userId, serviceURLMarker, connectorName);
    }


    /**
     * Return a summary of each of the integration services' status.
     *
     * @param serverName integration daemon name
     * @param userId calling user
     * @return list of statuses - on for each assigned integration services or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    @PostMapping(path = "/status")

    public IntegrationDaemonStatusResponse getIntegrationDaemonStatus(String   serverName,
                                                                      String   userId)
    {
        return restAPI.getIntegrationDaemonStatus(serverName, userId);
    }
}
