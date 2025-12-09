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
     * @param defaultServerConfig values to include in every new configured server.
     * @return void response
     */
    @PostMapping(path = "/default-configuration-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setDefaultOMAGServerConfig",
            description="Override the default server configuration document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public  VoidResponse setDefaultOMAGServerConfig(@RequestBody  OMAGServerConfig defaultServerConfig)
    {
        return adminStoreAPI.setDefaultOMAGServerConfig(defaultServerConfig);
    }


    /**
     * Return the default server configuration document.
     *
     * @return OMAGServerConfig response
     */
    @GetMapping(path = "/default-configuration-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDefaultOMAGServerConfig",
            description="Return the default server configuration document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public OMAGServerConfigResponse getDefaultOMAGServerConfig()
    {
        return adminStoreAPI.getDefaultOMAGServerConfig();
    }


    /**
     * Clear the default configuration document.
     *
     * @return void
     */
    @DeleteMapping(path = "/default-configuration-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearDefaultOMAGServerConfig",
            description="Clear the default configuration document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public VoidResponse clearDefaultOMAGServerConfig()
    {
        return adminStoreAPI.clearDefaultOMAGServerConfig();
    }


    /**
     * Override the placeholder variables.
     *
     * @param placeholderVariables values to include in every new configured server.
     * @return void response
     */
    @PostMapping(path = "/placeholder-variables")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setPlaceholderVariables",
            description="Override the placeholder variables that will replace placeholders found in an OMAG server's configuration document at server start up.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public  VoidResponse setPlaceholderVariables(@RequestBody  Map<String, String> placeholderVariables)
    {
        return adminStoreAPI.setPlaceholderVariables(placeholderVariables);
    }


    /**
     * Return the placeholder variables.
     *
     * @return string map response
     */
    @GetMapping(path = "/placeholder-variables")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getPlaceholderVariables",
            description="Return the placeholder variables that will replace placeholders found in an OMAG server's configuration document at server start up.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public StringMapResponse getPlaceholderVariables()
    {
        return adminStoreAPI.getPlaceholderVariables();
    }


    /**
     * Clear the placeholder variables used whenever an OMAG Server is started.
     *
     * @return current setting of default server configuration
     */
    @DeleteMapping(path = "/placeholder-variables")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearPlaceholderVariables",
            description="Clear the placeholder variables used whenever an OMAG Server is started.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public VoidResponse clearPlaceholderVariables()
    {
        return adminStoreAPI.clearPlaceholderVariables();
    }


    /**
     * Override the default implementation or configuration of the configuration document store.
     *
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

    public VoidResponse setConfigurationStoreConnection(@RequestBody  Connection connection)
    {
        return adminStoreAPI.setConfigurationStoreConnection(connection);
    }


    /**
     * Return the connection object for the configuration store.  Null is returned if the server should use the default store.
     *
     * @return connection response
     */
    @GetMapping(path = "/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConfigurationStoreConnection",
               description="Return the connection object for the configuration store.  Null is returned if the server should use the default store.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document-store-connector/"))

    public ConnectionResponse getConfigurationStoreConnection()
    {
        return adminStoreAPI.getConfigurationStoreConnection();
    }


    /**
     * Clear the connection object for the configuration store which means the platform uses the default store.
     *
     * @return void response
     */
    @DeleteMapping(path = "/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearConfigurationStoreConnection",
               description="Clear the connection object for the configuration store which means the platform uses the default store.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document-store-connector/"))

    public  VoidResponse clearConfigurationStoreConnection()
    {
        return adminStoreAPI.clearConfigurationStoreConnection();
    }
}
