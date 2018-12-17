/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.admin;

import org.odpi.openmetadata.adminservices.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.OMAGServerOperationalServices;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * VirtualizerAdminResource provides the spring annotations for the server-side
 * implementation of the administrative interface for
 * the Virtualizer
 */

@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class VirtualizerAdminResource{

    private OMAGServerAdminServices       adminAPI            = new OMAGServerAdminServices();
    private OMAGServerOperationalServices operationalServices = new OMAGServerOperationalServices();

    /*
     * =============================================================
     * Help the client discover the type of the server
     */


    /**
     * Return the origin of this server implementation.
     *
     * @return Server Origin
     */
    @RequestMapping(method = RequestMethod.GET, path = "/server-origin")
    public String getServerOrigin() {
        return "Virtualizer";
    }


    /*
     * =============================================================
     * Configure server - basic options using defaults
     */

    /**
     * Set up the root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces.  The default value is "localhost:8080".
     *
     * @param userId     user that is issuing the request.
     * @param serverName local server name.
     * @param url        String url.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/server-url-root")
    public VoidResponse setServerURLRoot(@PathVariable String userId,
                                         @PathVariable String serverName,
                                         @RequestParam String url) {
        return adminAPI.setServerURLRoot(userId, serverName, url);
    }

    /**
     * Set up the descriptive type of the server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is "Open Metadata and Governance Server".
     *
     * @param userId     user that is issuing the request.
     * @param serverName local server name.
     * @param typeName   short description for the type of server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/server-type")
    public VoidResponse setServerType(@PathVariable String userId,
                                      @PathVariable String serverName,
                                      @RequestParam String typeName) {
        return adminAPI.setServerType(userId, serverName, typeName);
    }


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param userId     user that is issuing the request.
     * @param serverName local server name.
     * @param name       String name of the organization.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or organizationName parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/organization-name")
    public VoidResponse setOrganizationName(@PathVariable String userId,
                                            @PathVariable String serverName,
                                            @RequestParam String name) {
        return adminAPI.setOrganizationName(userId, serverName, name);
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param userId     - user that is issuing the request.
     * @param serverName - local server name.
     * @param id         - String user is for the server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/server-user-id")
    public VoidResponse setServerUserId(@PathVariable String userId,
                                        @PathVariable String serverName,
                                        @RequestParam String id) {
        return adminAPI.setServerUserId(userId, serverName, id);
    }


    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used in the OMRS Topic Connector for each cohort, the in and out topics for each Access Service and
     * the local repositories event mapper.
     *
     * @param userId               user that is issuing the request.
     * @param serverName           local server name.
     * @param connectorProvider    connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot         the common root of the topics used by the open metadata server.
     * @param additionalProperties property name/value pairs used to configure the connection to the event bus connector
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * OMAGInvalidParameterException invalid serverName or serviceMode parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/event-bus")
    public VoidResponse setEventBus(@PathVariable String userId,
                                    @PathVariable String serverName,
                                    @RequestParam(required = false) String connectorProvider,
                                    @RequestParam(required = false) String topicURLRoot,
                                    @RequestBody(required = false) Map<String, Object> additionalProperties) {
        return adminAPI.setEventBus(userId, serverName, connectorProvider, topicURLRoot, additionalProperties);
    }


}

