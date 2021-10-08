/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.analytics.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.integrationservices.analytics.rest.AnalyticsIntegratorRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * AnalyticsIntegratorResource provides the server-side catcher for REST calls using Spring.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/integration-services/analytics-integrator/users/{userId}")

@Tag(name="Analytics Integrator OMIS", description="Supports the detection and creation of metadata for analytics tools.",
        externalDocs=@ExternalDocumentation(description="Analytics Integrator Open Metadata Integration Service (OMIS)",
                url="https://egeria.odpi.org/open-metadata-implementation/integration-services/analytics-integrator"))

public class AnalyticsIntegratorResource
{
    private AnalyticsIntegratorRESTServices restAPI = new AnalyticsIntegratorRESTServices();


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
     *
     *  InvalidParameterException the connector provider class name is not a valid connector fo this service
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException there was a problem detected by the integration service
     */
    @GetMapping(path = "/validate-connector/{connectorProviderClassName}")

    public ConnectorTypeResponse validateConnector(@PathVariable String serverName,
                                                   @PathVariable String userId,
                                                   @PathVariable String connectorProviderClassName)
    {
        return restAPI.validateConnector(serverName, userId, connectorProviderClassName);
    }
}
