package org.odpi.openmetadata.viewservices.serverauthor.server.spring;/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorConfigurationResponse;
import org.odpi.openmetadata.viewservices.serverauthor.services.ServerAuthorViewRESTServices;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ConfigDefaultsResource sets properties in the configuration document that are used as
 * default values when configuring the subsystems in an OMAG Server.  If these values
 * are updated after a subsystem is configured, they do not impact that subsystem's configuration.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/server-author/users/{userId}/servers/{serverToBeConfiguredName}")

@Tag(name="View Server: Server Author OMVS", description="The Server Author OMVS is for user interfaces supporting the creating and editing of OMAG Server Configuration Documents.",
     externalDocs=@ExternalDocumentation(description="Further information",
                                         url="https://egeria-project.org/services/omvs/server-author/overview"))

public class ConfigDefaultsViewResource
{
    private final ServerAuthorViewRESTServices adminAPI = new ServerAuthorViewRESTServices();

    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used for example, in the OMRS Topic Connector for each cohort, the in and out topics for each Access Service and
     * possibly the local repository's event mapper.
     * <br><br>
     * When the event bus is configured, it is used only on future configuration.  It does not affect
     * existing configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param connectorProvider  connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot the common root of the topics used by the open metadata server.
     * @param configurationProperties  property name/value pairs used to configure the connection to the event bus connector
     * @return the current stored configuration
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * OMAGInvalidParameterException invalid serverName or serviceMode parameter.
     */
    @PostMapping(path = "/event-bus")
    public ServerAuthorConfigurationResponse setEventBus(@PathVariable                   String              userId,
                                                         @PathVariable                   String              serverName,
                                                         @PathVariable                   String              serverToBeConfiguredName,
                                                         @RequestParam(required = false) String              connectorProvider,
                                                         @RequestParam(required = false) String              topicURLRoot,
                                                         @RequestBody (required = false) Map<String, Object> configurationProperties)
    {
        return adminAPI.setEventBus(userId, serverName, serverToBeConfiguredName, connectorProvider, topicURLRoot, configurationProperties);
    }
}
