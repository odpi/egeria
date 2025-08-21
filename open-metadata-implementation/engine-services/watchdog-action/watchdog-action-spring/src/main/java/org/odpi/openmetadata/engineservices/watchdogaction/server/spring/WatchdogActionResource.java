/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.watchdogaction.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.engineservices.watchdogaction.server.WatchdogActionRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * WatchdogActionResource provides the server-side catcher for REST calls using Spring that validated Discovery Service implementations
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/engine-services/watchdog-action/users/{userId}")

@Tag(name="Engine Host: Watchdog Action OMES", description="The Watchdog Action OMES provide the core subsystem for driving the monitoring of situations/events for notification types.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omes/watchdog-action/overview/"))

public class WatchdogActionResource
{
    private final WatchdogActionRESTServices restAPI = new WatchdogActionRESTServices();


    /**
     * Validate the connector and return its connector type.  The engine service does not need to
     * be running in the integration daemon in order for this call to be successful.  It only needs to be registered with the
     * engine host.
     *
     * @param serverName engine host server name
     * @param userId calling user
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector type or
     *  InvalidParameterException the connector provider class name is not a valid connector fo this service
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException there was a problem detected by the integration service
     */
    @GetMapping(path = "/validate-connector/{connectorProviderClassName}")

    @Operation(summary="validateConnector",
               description="Validate the connector and return its connector type.  The engine service does not need to" +
                                   " be running in the engine host in order for this call to be successful. " +
                                   " The engine service only needs to be registered with the engine host.",
               externalDocs=@ExternalDocumentation(description="Watchdog Action Service",
                                                   url="https://egeria-project.org/concepts/watchdog-action-service"))

    public ConnectorReportResponse validateConnector(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String connectorProviderClassName)
    {
        return restAPI.validateConnector(serverName, userId, connectorProviderClassName);
    }
}
