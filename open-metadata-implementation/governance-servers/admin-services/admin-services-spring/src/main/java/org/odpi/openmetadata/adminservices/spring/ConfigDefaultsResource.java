/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ConfigDefaultsResource sets properties in the configuration document that are used as
 * default values when configuring the subsystems in an OMAG Server.  If these values
 * are updated after a subsystem is configured, they do not impact that subsystem's configuration.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class ConfigDefaultsResource
{
    private OMAGServerAdminServices adminAPI = new OMAGServerAdminServices();

    /**
     * Set up the default root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces.  It is a value that is sent to other servers to allow
     * then to call this server.
     *
     * The default value is "localhost:8080".
     *
     * ServerURLRoot is used as a default value during the configuration of the server's subsystems.
     * If it is updated after a subsystem is
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param url  String url.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/server-url-root")
    public VoidResponse setServerURLRoot(@PathVariable String userId,
                                         @PathVariable String serverName,
                                         @RequestParam String url)
    {
        return adminAPI.setServerURLRoot(userId, serverName, url);
    }


    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used for example, in the OMRS Topic Connector for each cohort, the in and out topics for each Access Service and
     * the local repositories event mapper.
     *
     * When the event bus is configured, it is used only on future configuration.  It does not effect
     * existing configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName local server name.
     * @param connectorProvider  connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot the common root of the topics used by the open metadata server.
     * @param configurationProperties  property name/value pairs used to configure the connection to the event bus connector
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * OMAGInvalidParameterException invalid serverName or serviceMode parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/event-bus")
    public VoidResponse setEventBus(@PathVariable                   String              userId,
                                    @PathVariable                   String              serverName,
                                    @RequestParam(required = false) String              connectorProvider,
                                    @RequestParam(required = false) String              topicURLRoot,
                                    @RequestBody(required = false)  Map<String, Object> configurationProperties)
    {
        return adminAPI.setEventBus(userId, serverName, connectorProvider, topicURLRoot, configurationProperties);
    }
}
