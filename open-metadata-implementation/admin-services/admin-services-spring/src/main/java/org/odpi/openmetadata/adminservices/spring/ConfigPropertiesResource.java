/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.rest.ServerTypeClassificationResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * ConfigPropertiesResource provides part of the server-side implementation of the administrative interface for
 * an Open Metadata and Governance (OMAG) Server.  In particular, this resource supports the configuration
 * of the server name, server type and organization name.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
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
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    @GetMapping(path = "/server-type-classification")
    public ServerTypeClassificationResponse getServerTypeClassification(@PathVariable String userId,
                                                                        @PathVariable String serverName)
    {
        return adminAPI.getServerTypeClassification(userId, serverName);
    }


    /**
     * Set up the descriptive type of the server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is "Open Metadata and Governance Server".
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param typeName  short description for the type of server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    @PostMapping(path = "/server-type")
    public VoidResponse setServerType(@PathVariable String userId,
                                      @PathVariable String serverName,
                                      @RequestParam String typeName)
    {
        return adminAPI.setServerType(userId, serverName, typeName);
    }


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param name  String name of the organization.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or organizationName parameter.
     */
    @PostMapping(path = "/organization-name")
    public VoidResponse setOrganizationName(@PathVariable String userId,
                                            @PathVariable String serverName,
                                            @RequestParam String name)
    {
        return adminAPI.setOrganizationName(userId, serverName, name);
    }

    /**
     * Set up the description of this server. The default value is null.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server description.
     * @param description  String description of the server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or organizationName parameter.
     */
    @PostMapping(path = "/server-description")
    public VoidResponse setServerDescription(@PathVariable String userId,
                                             @PathVariable String serverName,
                                             @RequestBody String description)
    {
        return adminAPI.setServerDescription(userId, serverName, description);
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param userId - user that is issuing the request.
     * @param serverName - local server name.
     * @param id - String user is for the server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @PostMapping(path = "/server-user-id")
    public VoidResponse setServerUserId(@PathVariable String userId,
                                        @PathVariable String serverName,
                                        @RequestParam String id)
    {
        return adminAPI.setServerUserId(userId, serverName, id);
    }


    /**
     * Set up the password to use when the server is issuing REST calls.  If this value is set then the
     * serverUserId and this password are both embedded in the HTTP header.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param password  String password for the server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @PostMapping(path = "/server-user-password")
    public VoidResponse setServerPassword(@PathVariable String userId,
                                          @PathVariable String serverName,
                                          @RequestParam String password)
    {
        return adminAPI.setServerPassword(userId, serverName, password);
    }


    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param limit  max number of elements that can be returned on a request.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or maxPageSize parameter.
     */
    @PostMapping(path = "/max-page-size")
    public VoidResponse setMaxPageSize(@PathVariable String  userId,
                                       @PathVariable String  serverName,
                                       @RequestParam int     limit)
    {
        return adminAPI.setMaxPageSize(userId, serverName, limit);
    }
}
