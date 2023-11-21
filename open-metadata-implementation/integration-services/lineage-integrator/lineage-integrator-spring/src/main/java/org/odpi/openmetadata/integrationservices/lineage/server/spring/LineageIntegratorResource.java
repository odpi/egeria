/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.integrationservices.lineage.rest.LineageIntegratorRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * LineageIntegratorResource provides the server-side catcher for REST calls using Spring.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/integration-services/lineage-integrator/users/{userId}")

@Tag(name="Integration Daemon: Lineage Integrator OMIS", description="Supports the detection and creation of metadata for file systems and file servers.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omis/lineage-integrator/overview/"))

public class LineageIntegratorResource
{
    private final LineageIntegratorRESTServices restAPI = new LineageIntegratorRESTServices();


    /**
     * Validate the connector and return its connector type.  The integration service does not need to
     * be running in the integration daemon in order for this call to be successful.  It only needs to be registered with the
     * integration daemon.
     *
     * @param serverName integration daemon server name
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
               description="Validate the connector and return its connector type.  The integration service does not need to" +
                                   " be running in the integration daemon in order for this call to be successful. " +
                                   " The integration service only needs to be registered with the integration daemon.",
               externalDocs=@ExternalDocumentation(description="Integration Connectors",
                                                   url="https://egeria-project.org/concepts/integration-connector"))

    public ConnectorReportResponse validateConnector(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String connectorProviderClassName)
    {
        return restAPI.validateConnector(serverName, userId, connectorProviderClassName);
    }


    /**
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param event open lineage event to publish.
     */
    @PostMapping(path = "/api/v1/lineage")

    @Operation(summary="publishOpenLineageEvent",
               description="Sent an Open Lineage event to the integration daemon.  It will pass it on to the integration connectors that have" +
                                   " registered a listener for open lineage events.",
               externalDocs=@ExternalDocumentation(description="Open Lineage Standard",
                                                   url="https://egeria-project.org/features/lineage-management/overview/#the-openlineage-standard"))

    void publishOpenLineageEvent(@PathVariable String serverName,
                                 @PathVariable String userId,
                                 @RequestBody  String event)
    {
        restAPI.publishOpenLineageEvent(serverName, userId, event);
    }
}
