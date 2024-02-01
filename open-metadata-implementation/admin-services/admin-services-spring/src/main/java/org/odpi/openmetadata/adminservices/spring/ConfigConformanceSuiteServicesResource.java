/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.server.OMAGConformanceSuiteConfigServices;
import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryConformanceWorkbenchConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryPerformanceWorkbenchConfig;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * ConfigAccessServicesResource provides the configuration for setting up the Open Metadata
 * Conformance Suite services in an OMAG server.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/servers/"))

public class ConfigConformanceSuiteServicesResource
{
    private final OMAGConformanceSuiteConfigServices adminAPI = new OMAGConformanceSuiteConfigServices();

    /**
     * Request that the conformance suite services are activated in this server to test the
     * support of the repository services running in the server named tutRepositoryServerName.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param repositoryConformanceWorkbenchConfig configuration for the repository conformance workbench.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unexpected exception.
     */
    @PostMapping(path = "/conformance-suite-workbenches/repository-workbench/repositories")

    @Operation(summary="enableRepositoryConformanceSuiteWorkbench",
               description="Request that the conformance suite services are activated in this server to test the" +
                                   " support of the repository services running in the server named tutRepositoryServerName.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/guides/cts/overview/"))

    public VoidResponse enableRepositoryConformanceSuiteWorkbench(@PathVariable String                               userId,
                                                                  @PathVariable String                               serverName,
                                                                  @RequestBody  RepositoryConformanceWorkbenchConfig repositoryConformanceWorkbenchConfig)
    {
        return adminAPI.enableRepositoryConformanceSuiteWorkbench(userId, serverName, repositoryConformanceWorkbenchConfig);
    }


    /**
     * Request that the conformance suite services are activated in this server to test the
     * performance of the repository services running in the server named tutRepositoryServerName.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param repositoryPerformanceWorkbenchConfig configuration for the repository performance workbench.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unexpected exception.
     */
    @PostMapping(path = "/conformance-suite-workbenches/repository-workbench/performance")

    @Operation(summary="enableRepositoryPerformanceSuiteWorkbench",
               description="Request that the conformance suite services are activated in this server to test the" +
                                   " performance of the repository services running in the server named tutRepositoryServerName.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/guides/cts/overview/"))

    public VoidResponse enableRepositoryPerformanceSuiteWorkbench(@PathVariable String                               userId,
                                                                  @PathVariable String                               serverName,
                                                                  @RequestBody  RepositoryPerformanceWorkbenchConfig repositoryPerformanceWorkbenchConfig)
    {
        return adminAPI.enableRepositoryPerformanceSuiteWorkbench(userId, serverName, repositoryPerformanceWorkbenchConfig);
    }


    /**
     * Request that the conformance suite services are activated in this server to test the
     * support of the platform services running in the platform at tutPlatformRootURL.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody url of the OMAG platform to test.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unexpected exception.
     */
    @PostMapping(path = "/conformance-suite-workbenches/platform-workbench/platforms")

    @Operation(summary="enablePlatformConformanceSuiteWorkbench",
               description="Request that the conformance suite services are activated in this server to test the" +
                                   " support of the platform services running in the server named tutPlatformRootURL.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/guides/cts/overview/"))

    public VoidResponse enablePlatformConformanceSuiteWorkbench(@PathVariable String         userId,
                                                                @PathVariable String         serverName,
                                                                @RequestBody  URLRequestBody requestBody)
    {
        return adminAPI.enablePlatformConformanceSuiteWorkbench(userId, serverName, requestBody);
    }


    /**
     * Request that the repository conformance suite tests are deactivated in this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unexpected exception.
     */
    @DeleteMapping(path = "/conformance-suite-workbenches/repository-workbench")

    @Operation(summary="disableRepositoryConformanceSuiteServices",
               description="Request that the repository conformance suite tests are deactivated in this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/guides/cts/overview/"))

    public VoidResponse disableRepositoryConformanceSuiteServices(@PathVariable String    userId,
                                                                  @PathVariable String    serverName)
    {
        return adminAPI.disableRepositoryConformanceSuiteServices(userId, serverName);
    }


    /**
     * Request that the platform conformance suite tests are deactivated in this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unexpected exception.
     */
    @DeleteMapping(path = "/conformance-suite-workbenches/platform-workbench")

    @Operation(summary="disablePlatformConformanceSuiteServices",
               description="Request that the platform conformance suite tests are deactivated in this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/guides/cts/overview/"))

    public VoidResponse disablePlatformConformanceSuiteServices(@PathVariable String    userId,
                                                                @PathVariable String    serverName)
    {
        return adminAPI.disablePlatformConformanceSuiteServices(userId, serverName);
    }


    /**
     * Request that all the conformance suite services are deactivated in this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unexpected exception.
     */
    @DeleteMapping(path = "/conformance-suite-workbenches")

    @Operation(summary="disableAllConformanceSuiteWorkbenches",
               description="Request that all the conformance suite services are deactivated in this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/guides/cts/overview/"))

    public VoidResponse disableAllConformanceSuiteWorkbenches(@PathVariable String    userId,
                                                              @PathVariable String    serverName)
    {
        return adminAPI.disableAllConformanceSuiteWorkbenches(userId, serverName);
    }
}
