/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/open-metadata/admin-services/users/{userId}/stores")

@Tag(name="Administration Services - Set up Configuration Document Store",
     description="Dynamically change the connector that accesses the configuration document store.  This overrides the value set in the application.properties (which overrides the default Encrypted File Configuration Document Store).",
        externalDocs=@ExternalDocumentation(description="Further information",
        url="https://egeria-project.org/services/admin-services/overview/#dynamically-configuring-the-configuration-document-store-connector"))

public class ConfigStoreResource
{
    private final OMAGServerAdminStoreServices  adminStoreAPI = new OMAGServerAdminStoreServices();

    /**
     * Override the default server configuration document.
     *
     * @param userId calling user.
     * @param defaultServerConfig values to include in every new configured server.
     * @return void response
     */
    @PostMapping(path = "/default-configuration-document")

    @Operation(summary="setDefaultOMAGServerConfig",
            description="Override the default server configuration document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public  VoidResponse setDefaultOMAGServerConfig(@PathVariable String           userId,
                                                    @RequestBody  OMAGServerConfig defaultServerConfig)
    {
        return adminStoreAPI.setDefaultOMAGServerConfig(userId, defaultServerConfig);
    }


    /**
     * Return the default server configuration document.
     *
     * @param userId calling user
     * @return OMAGServerConfig response
     */
    @GetMapping(path = "/default-configuration-document")

    @Operation(summary="getDefaultOMAGServerConfig",
            description="Return the default server configuration document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public OMAGServerConfigResponse getDefaultOMAGServerConfig(@PathVariable String       userId)
    {
        return adminStoreAPI.getDefaultOMAGServerConfig(userId);
    }


    /**
     * Clear the default configuration document.
     *
     * @param userId calling user
     * @return void
     */
    @DeleteMapping(path = "/default-configuration-document")

    @Operation(summary="clearDefaultOMAGServerConfig",
            description="Clear the default configuration document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public VoidResponse clearDefaultOMAGServerConfig(@PathVariable String   userId)
    {
        return adminStoreAPI.clearDefaultOMAGServerConfig(userId);
    }


    /**
     * Override the placeholder variables.
     *
     * @param userId calling user.
     * @param placeholderVariables values to include in every new configured server.
     * @return void response
     */
    @PostMapping(path = "/placeholder-variables")

    @Operation(summary="setPlaceholderVariables",
            description="Override the placeholder variables that will replace placeholders found in an OMAG server's configuration document at server start up.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public  VoidResponse setPlaceholderVariables(@PathVariable String              userId,
                                                 @RequestBody  Map<String, String> placeholderVariables)
    {
        return adminStoreAPI.setPlaceholderVariables(userId, placeholderVariables);
    }


    /**
     * Return the placeholder variables.
     *
     * @param userId calling user
     * @return string map response
     */
    @GetMapping(path = "/placeholder-variables")

    @Operation(summary="getPlaceholderVariables",
            description="Return the placeholder variables that will replace placeholders found in an OMAG server's configuration document at server start up.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public StringMapResponse getPlaceholderVariables(@PathVariable String       userId)
    {
        return adminStoreAPI.getPlaceholderVariables(userId);
    }


    /**
     * Clear the placeholder variables used whenever an OMAG Server is started.
     *
     * @param userId calling user
     * @return current setting of default server configuration
     */
    @DeleteMapping(path = "/placeholder-variables")

    @Operation(summary="clearPlaceholderVariables",
            description="Clear the placeholder variables used whenever an OMAG Server is started.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public VoidResponse clearPlaceholderVariables(@PathVariable String   userId)
    {
        return adminStoreAPI.clearPlaceholderVariables(userId);
    }


    /**
     * Override the default implementation or configuration of the configuration document store.
     *
     * @param userId calling user.
     * @param connection connection used to create and configure the connector that interacts with
     *                   the real store.
     * @return void response
     */
    @PostMapping(path = "/connection")

    @Operation(summary="setConfigurationStoreConnection",
               description="Override the default implementation or configuration of the configuration document store.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document-store-connector/"))

    public VoidResponse setConfigurationStoreConnection(@PathVariable String     userId,
                                                        @RequestBody  Connection connection)
    {
        return adminStoreAPI.setConfigurationStoreConnection(userId, connection);
    }


    /**
     * Return the connection object for the configuration store.  Null is returned if the server should use the default store.
     *
     * @param userId calling user
     * @return connection response
     */
    @GetMapping(path = "/connection")

    @Operation(summary="getConfigurationStoreConnection",
               description="Return the connection object for the configuration store.  Null is returned if the server should use the default store.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document-store-connector/"))

    public ConnectionResponse getConfigurationStoreConnection(@PathVariable String       userId)
    {
        return adminStoreAPI.getConfigurationStoreConnection(userId);
    }


    /**
     * Clear the connection object for the configuration store which means the platform uses the default store.
     *
     * @param userId calling user
     * @return void response
     */
    @DeleteMapping(path = "/connection")

    @Operation(summary="clearConfigurationStoreConnection",
               description="Clear the connection object for the configuration store which means the platform uses the default store.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document-store-connector/"))

    public  VoidResponse clearConfigurationStoreConnection(@PathVariable String   userId)
    {
        return adminStoreAPI.clearConfigurationStoreConnection(userId);
    }
}
