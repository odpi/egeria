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
import org.odpi.openmetadata.adminservices.rest.EventBusConfigResponse;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ConfigDefaultsResource sets properties in the configuration document that are used as
 * default values when configuring the subsystems in an OMAG Server.  If these values
 * are updated after a subsystem is configured, they do not impact that subsystem's configuration.
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

public class ConfigDefaultsResource
{
    private final OMAGServerAdminServices adminAPI = new OMAGServerAdminServices();


    /**
     * Set up the default root URL for this server's platform that is used to construct full URL paths to calls for
     * this server's REST interfaces.  It is a value that is sent to other servers to allow
     * them to call this server.
     * The default value is "https://localhost:9443".
     * ServerRootURL is used as a default value during the configuration of the server's subsystems.
     * If it is updated after a subsystem is configured then the new value is ignored.
     *
     * @param serverName  local server name.
     * @param requestBody  String url.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @PostMapping(path = "/server-url-root-for-caller")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setServerRootURL",
               description="Set up the default root URL for this server that is used to construct full URL paths to calls for" +
                                   " this server's REST interfaces.  It is a value that is sent to other servers to allow" +
                                   " them to call this server." +
                                   " The default value is \"https://localhost:9443\"." +
                                   " ServerURLRoot is used as a default value during the configuration of the server's subsystems." +
                                   " If it is updated after a subsystem is configured then the new value is ignored.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/guides/admin/servers/configuring-a-metadata-access-store/#set-the-server-url-root"))

    public VoidResponse setServerRootURL(@PathVariable String         serverName,
                                         @RequestBody  URLRequestBody requestBody)
    {
        return adminAPI.setServerRootURL(serverName, requestBody);
    }


    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used in the OMRS Topic Connector for each cohort, the open metadata out topic and
     * the local repositories' event mapper.
     * When the event bus is configured, it is used only on future configuration.  It does not affect
     * existing configuration.
     * If openMetadataOutTopic is null, a default connection for this topic is created.  It can be removed using
     * clearOpenMetadataOutTopic
     *
     * @param serverName local server name.
     * @param connectorProvider  connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot the common root of the topics used by the open metadata server.
     * @param configurationProperties  property name/value pairs used to configure the connection to the event bus connector
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * InvalidParameterException invalid serverName or serviceMode parameter.
     */
    @PostMapping(path = "/event-bus")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setEventBus",
               description="Set up the default event bus for embedding in event-driven connector.   The resulting connector will" +
                                   " be used for example, in the OMRS Topic Connector for each cohort, the in and out topics for each Access Service and" +
                                   " possibly the local repository's event mapper." +
                                   " When the event bus is configured, it is used only on future configuration.  It does not effect" +
                                   " existing configuration.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/guides/admin/servers/configuring-a-metadata-access-store/#set-up-the-default-event-bus"))

    public VoidResponse setEventBus(@PathVariable                   String              serverName,
                                    @RequestParam(required = false) String              connectorProvider,
                                    @RequestParam(required = false) String              topicURLRoot,
                                    @RequestBody (required = false) Map<String, Object> configurationProperties)
    {
        return adminAPI.setEventBus(serverName, connectorProvider, topicURLRoot, configurationProperties);
    }


    /**
     * Return the current configuration for the event bus.
     *
     * @param serverName local server name.
     * @return event bus config response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * InvalidParameterException invalid serverName parameter.
     */
    @GetMapping(path = "/event-bus")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getEventBus",
               description="Return the current configuration for the event bus.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/guides/admin/servers/configuring-a-metadata-access-store/#set-up-the-default-event-bus"))

    public EventBusConfigResponse getEventBus(@PathVariable String serverName)
    {
        return adminAPI.getEventBus(serverName);
    }


    /**
     * Delete the current configuration for the event bus.
     * This does not impact that existing configuration for the server, only future configuration requests.
     *
     * @param serverName local server name.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * InvalidParameterException invalid serverName parameter.
     */
    @DeleteMapping(path = "/event-bus")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteEventBus",
               description="Delete the current configuration for the event bus.  This does not impact that existing configuration for the server, only future configuration requests.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/guides/admin/servers/configuring-a-metadata-access-store/#set-up-the-default-event-bus"))

    public VoidResponse deleteEventBus(@PathVariable String serverName)
    {
        return adminAPI.deleteEventBus(serverName);
    }
}
