/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.governanceservers.enginehostservices.rest.GovernanceEngineSummariesResponse;
import org.odpi.openmetadata.governanceservers.enginehostservices.rest.GovernanceEngineSummaryResponse;
import org.odpi.openmetadata.governanceservers.enginehostservices.server.EngineHostRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * EngineHostServicesResource provides the server-side catcher for REST calls using Spring.
 * The OMAG ServerPlatform routes these requests to the engine host services active in the server.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/engine-host")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Engine Host Services",
     description="The Engine Host Services provide the core subsystem for the Engine Host OMAG Server.",
     externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/engine-host-services/"))

public class EngineHostServicesResource
{
    private final EngineHostRESTServices restAPI = new EngineHostRESTServices();


    /**
     * Retrieve the description and status of the requested governance engine.
     *
     * @param serverName engine host server name
     * @param governanceEngineName name of governance engine of interest
     * @return list of statuses - on for each assigned governance engines or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    @GetMapping(path = "/governance-engines/{governanceEngineName}/summary")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getGovernanceEngineSummary",
            description="Retrieve the description and status of the requested governance engine.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-engine/"))

    public GovernanceEngineSummaryResponse getGovernanceEngineSummary(@PathVariable String serverName,
                                                                      @PathVariable String governanceEngineName)
    {
        return restAPI.getGovernanceEngineSummary(serverName, governanceEngineName);
    }



    /**
     * Return a summary of each of the governance engines running in the Engine Host.
     *
     * @param serverName engine host server name
     * @return list of statuses - on for each assigned governance engines or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    @GetMapping(path = "/governance-engines/summary")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getGovernanceEngineSummaries",
            description="Return a summary of each of the governance engines running in the Engine Host.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-engine/"))

    public GovernanceEngineSummariesResponse getGovernanceEngineSummaries(@PathVariable String serverName)
    {
        return restAPI.getGovernanceEngineSummaries(serverName);
    }


    /**
     * Request that the governance engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * governance server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server
     * @param governanceEngineName unique name of the governance engine
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  GovernanceEngineException there was a problem detected by the governance engine.
     */
    @GetMapping(path = "/governance-engines/{governanceEngineName}/refresh-config")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="refreshConfig",
            description="Request that the governance engine refresh its configuration by calling the metadata server. " +
                    "This request is useful if the metadata server has an outage, particularly while the " +
                    "governance server is initializing.  This request just ensures that the latest configuration " +
                    "is in use.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-engine-definition/"))

    public  VoidResponse refreshConfig(@PathVariable String serverName,
                                       @PathVariable String governanceEngineName)
    {
        return restAPI.refreshConfig(serverName, governanceEngineName);
    }


    /**
     * Request that all governance engines refresh their configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * governance server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  GovernanceEngineException there was a problem detected by the governance engine.
     */
    @GetMapping(path = "/governance-engines/refresh-config")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="refreshConfig",
            description="Request that all governance engines refresh their configuration by calling the metadata server. " +
                    "This request is useful if the metadata server has an outage, particularly while the " +
                    "governance server is initializing.  This request just ensures that the latest configuration " +
                    "is in use.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-engine-definition/"))

    public  VoidResponse refreshConfig(@PathVariable String serverName)
    {
        return restAPI.refreshConfig(serverName);
    }
}
