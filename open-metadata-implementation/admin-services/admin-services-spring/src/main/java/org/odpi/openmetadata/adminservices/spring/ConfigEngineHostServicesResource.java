/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminForEngineHostServices;
import org.odpi.openmetadata.adminservices.rest.EngineHostServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigEngineServicesResource provides the configuration for setting up the Open Metadata Engine
 * Services (OMESs).
 */
@RestController
@RequestMapping("/open-metadata/admin-services/servers/{serverName}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/servers/"))

public class ConfigEngineHostServicesResource
{
    private final OMAGServerAdminForEngineHostServices adminAPI = new OMAGServerAdminForEngineHostServices();


    /**
     * Add another governance engine the list of governance engines for an engine host OMAG server.
     *
     * @param serverName  local server name.
     * @param engine  definition of a single engine
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * InvalidParameterException invalid serverName parameter.
     */
    @PostMapping("/engine")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addGovernanceEngine",
            description="Add another governance engine the list of governance engines for an engine host OMAG server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-engine/"))

    public VoidResponse addEngine(@PathVariable String       serverName,
                                  @RequestBody  EngineConfig engine)
    {
        return adminAPI.addEngine(serverName, engine);
    }


    /**
     * Return the configuration of the specialist services for an Engine Host OMAG Server.
     *
     * @param serverName name of server
     * @return response containing the engine host services configuration
     */
    @GetMapping( "/governance-engines")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getGovernanceEngines",
            description="Return the configuration of the specialist services for an Engine Host OMAG Server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-host/"))

    public EngineHostServicesResponse getEngineHostServicesConfiguration(@PathVariable String serverName)
    {
        return adminAPI.getEngineHostServicesConfiguration(serverName);
    }


    /**
     * Set up the configuration of the specialist services for an Engine Host OMAG Server in a single call.  This overrides the current values.
     *
     * @param serverName  local server name.
     * @param governanceEngines full configuration for the engine host server.
     * @return void response
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * InvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/governance-engines")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setGovernanceEngines",
            description="Set up the list of governance engines for an engine host.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-host/"))

    public VoidResponse setEngineHostServicesConfig(@PathVariable String             serverName,
                                                    @RequestBody  List<EngineConfig> governanceEngines)
    {
        return adminAPI.setEngineHostServicesConfig(serverName, governanceEngines);
    }


    /**
     * Remove the configuration of the specialist services for an Engine Host OMAG Server in a single call.  This overrides the current values.
     *
     * @param serverName  local server name.
     * @return void response
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * InvalidParameterException invalid serverName parameter.
     */
    @DeleteMapping(path = "/governance-engines")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearGovernanceEngines",
            description="Remove the governance engines for an Engine Host OMAG Server in a single call.  This overrides the current values.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-host/"))

    public VoidResponse clearEngineHostServicesConfig(@PathVariable String serverName)
    {
        return adminAPI.clearEngineHostServicesConfig(serverName);
    }
}
