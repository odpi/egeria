/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminStoreServices;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringMapResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ConfigStoreResource provides the API to configure the connector that should be used to manage
 * configuration documents.  The default is to use a file for each configured OMAG server.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/stores")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Set up Configuration Document Store",
     description="Dynamically change the connector that accesses the configuration document store.  This overrides the value set in the application.properties (which overrides the default plain-text File Configuration Document Store).  These services are only needed when testing a new implementation of the configuration document store.",
        externalDocs=@ExternalDocumentation(description="Further information",
        url="https://egeria-project.org/services/admin-services/overview/#dynamically-configuring-the-configuration-document-store-connector"))

public class ConfigStoreResource
{
    private final OMAGServerAdminStoreServices  adminStoreAPI = new OMAGServerAdminStoreServices();

    /**
     * Override the default server configuration document.
     *
     * @param delegatingUserId external userId making request
     * @param defaultServerConfig values to include in every new configured server.
     * @return void response
     */
    @PostMapping(path = "/default-configuration-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setDefaultOMAGServerConfig",
            description="Override the default server configuration document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public  VoidResponse setDefaultOMAGServerConfig(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                    @RequestBody  OMAGServerConfig defaultServerConfig)
    {
        return adminStoreAPI.setDefaultOMAGServerConfig(delegatingUserId, defaultServerConfig);
    }


    /**
     * Return the default server configuration document.
     *
     * @param delegatingUserId external userId making request
     * @return OMAGServerConfig response
     */
    @GetMapping(path = "/default-configuration-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDefaultOMAGServerConfig",
            description="Return the default server configuration document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public OMAGServerConfigResponse getDefaultOMAGServerConfig(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminStoreAPI.getDefaultOMAGServerConfig(delegatingUserId);
    }


    /**
     * Clear the default configuration document.
     *
     * @param delegatingUserId external userId making request
     * @return void
     */
    @DeleteMapping(path = "/default-configuration-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearDefaultOMAGServerConfig",
            description="Clear the default configuration document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public VoidResponse clearDefaultOMAGServerConfig(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminStoreAPI.clearDefaultOMAGServerConfig(delegatingUserId);
    }


    /**
     * Override the placeholder variables.
     *
     * @param placeholderVariables values to include in every new configured server.
     * @param delegatingUserId external userId making request
     * @return void response
     */
    @PostMapping(path = "/placeholder-variables")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setPlaceholderVariables",
            description="Override the placeholder variables that will replace placeholders found in an OMAG server's configuration document at server start up.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public  VoidResponse setPlaceholderVariables(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                 @RequestBody  Map<String, String> placeholderVariables)
    {
        return adminStoreAPI.setPlaceholderVariables(delegatingUserId, placeholderVariables);
    }


    /**
     * Return the placeholder variables.
     *
     * @param delegatingUserId external userId making request
     * @return string map response
     */
    @GetMapping(path = "/placeholder-variables")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getPlaceholderVariables",
            description="Return the placeholder variables that will replace placeholders found in an OMAG server's configuration document at server start up.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public StringMapResponse getPlaceholderVariables(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminStoreAPI.getPlaceholderVariables(delegatingUserId);
    }


    /**
     * Clear the placeholder variables used whenever an OMAG Server is started.
     *
     * @param delegatingUserId external userId making request
     * @return current setting of default server configuration
     */
    @DeleteMapping(path = "/placeholder-variables")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearPlaceholderVariables",
            description="Clear the placeholder variables used whenever an OMAG Server is started.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public VoidResponse clearPlaceholderVariables(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminStoreAPI.clearPlaceholderVariables(delegatingUserId);
    }


    /**
     * Override the default implementation or configuration of the configuration document store.
     *
     * @param delegatingUserId external userId making request
     * @param connection connection used to create and configure the connector that interacts with
     *                   the real store.
     * @return void response
     */
    @PostMapping(path = "/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setConfigurationStoreConnection",
               description="Override the default implementation or configuration of the configuration document store.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document-store-connector/"))

    public VoidResponse setConfigurationStoreConnection(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                        @RequestBody  Connection connection)
    {
        return adminStoreAPI.setConfigurationStoreConnection(delegatingUserId, connection);
    }


    /**
     * Return the connection object for the configuration store.  Null is returned if the server should use the default store.
     *
     * @param delegatingUserId external userId making request
     * @return connection response
     */
    @GetMapping(path = "/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConfigurationStoreConnection",
               description="Return the connection object for the configuration store.  Null is returned if the server should use the default store.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document-store-connector/"))

    public ConnectionResponse getConfigurationStoreConnection(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminStoreAPI.getConfigurationStoreConnection(delegatingUserId);
    }


    /**
     * Clear the connection object for the configuration store which means the platform uses the default store.
     *
     * @param delegatingUserId external userId making request
     * @return void response
     */
    @DeleteMapping(path = "/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearConfigurationStoreConnection",
               description="Clear the connection object for the configuration store which means the platform uses the default store.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document-store-connector/"))

    public  VoidResponse clearConfigurationStoreConnection(@Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminStoreAPI.clearConfigurationStoreConnection(delegatingUserId);
    }
}
