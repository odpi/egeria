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
import org.odpi.openmetadata.adminservices.rest.BasicServerPropertiesResponse;
import org.odpi.openmetadata.adminservices.rest.ServerPropertiesRequestBody;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.rest.ServerTypeClassificationResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * ConfigPropertiesResource provides part of the server-side implementation of the administrative interface for
 * an Open Metadata and Governance (OMAG) Server.  In particular, this resource supports the configuration
 * of the server name, server type and organization name.
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


public class ConfigPropertiesResource
{
    private final OMAGServerAdminServices adminAPI = new OMAGServerAdminServices();


    /**
     * Return the derived server type that is created from the classification of the server configuration.
     *
     * @param serverName  local server name.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or serverType parameter.
     */
    @GetMapping(path = "/server-type-classification")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getServerTypeClassification",
               description="Return the derived server type that is created from the classification of the server configuration.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/omag-server/#types-of-omag-server"))

    public ServerTypeClassificationResponse getServerTypeClassification(@PathVariable String serverName)
    {
        return adminAPI.getServerTypeClassification(serverName);
    }


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param serverName  local server name.
     * @param name  String name of the organization.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or organizationName parameter.
     */
    @PostMapping(path = "/organization-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setOrganizationName",
               description="Set up the name of the organization that is running this server.  This value is added to distributed events to" +
                                   " make it easier to understand the source of events.  The default value is null.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/omag-server/"))

    public VoidResponse setOrganizationName(@PathVariable String serverName,
                                            @RequestParam String name)
    {
        return adminAPI.setOrganizationName(serverName, name);
    }


    /**
     * Set up the description of this server. The default value is null.
     *
     * @param serverName  local server description.
     * @param description  String description of the server.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or organizationName parameter.
     */
    @PostMapping(path = "/server-description")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setServerDescription",
               description="Set up the description of this server. The default value is null.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/omag-server/"))

    public VoidResponse setServerDescription(@PathVariable String serverName,
                                             @RequestBody  String description)
    {
        return adminAPI.setServerDescription(serverName, description);
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param serverName - local server name.
     * @param id - String user is for the server.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @PostMapping(path = "/server-user-id")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setServerUserId",
               description="Set up the description of this server. The default value is null.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/omag-server/"))

    public VoidResponse setServerUserId(@PathVariable String serverName,
                                        @RequestParam String id)
    {
        return adminAPI.setServerUserId(serverName, id);
    }



    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.
     *
     * @param serverName  local server name.
     * @param limit  max number of elements that can be returned on a request.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or maxPageSize parameter.
     */
    @PostMapping(path = "/max-page-size")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setMaxPageSize",
               description="Set an upper limit in the page size that can be requested on a REST call to the server.  The default value is 1000.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/omag-server/"))

    public VoidResponse setMaxPageSize(@PathVariable String  serverName,
                                       @RequestParam int     limit)
    {
        return adminAPI.setMaxPageSize(serverName, limit);
    }


    /**
     * Set up the basic server properties in a single request.
     *
     * @param serverName  local server name.
     * @param requestBody property details
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or maxPageSize parameter.
     */
    @PostMapping(path = "/server-properties")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setBasicServerProperties",
               description="Set up the basic server properties in a single request.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/omag-server/"))

    public VoidResponse setBasicServerProperties(@PathVariable String                      serverName,
                                                 @RequestBody  ServerPropertiesRequestBody requestBody)
    {
        return adminAPI.setBasicServerProperties(serverName, requestBody);
    }


    /**
     * Get the basic server properties in a single request.
     *
     * @param serverName  local server name.
     * @return properties response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or maxPageSize parameter.
     */
    @GetMapping(path = "/server-properties")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getBasicServerProperties",
            description="Return the basic server properties in a single request.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/omag-server/"))

    public BasicServerPropertiesResponse getBasicServerProperties(@PathVariable String serverName)
    {
        return adminAPI.getBasicServerProperties(serverName);
    }
}
