/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.engineservices.repositorygovernance.server.RepositoryGovernanceRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * RepositoryGovernanceResource provides the server-side catcher for REST calls using Spring that validate RepositoryGovernance Service implementations
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/engine-services/repository-governance")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Engine Host: Repository Governance OMES", description="The Repository Governance OMES provide the core subsystem for driving requests to govern open metadata repositories.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omes/repository-governance/overview/"))

public class RepositoryGovernanceResource
{
    private final RepositoryGovernanceRESTServices restAPI = new RepositoryGovernanceRESTServices();


    /**
     * Validate the connector and return its connector type.  The engine service does not need to
     * be running in the engine host in order for this call to be successful.  It only needs to be registered with the
     * engine host.
     *
     * @param serverName engine host server name
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
                                                     @PathVariable String connectorProviderClassName)
    {
        return restAPI.validateConnector(serverName, connectorProviderClassName);
    }
}
