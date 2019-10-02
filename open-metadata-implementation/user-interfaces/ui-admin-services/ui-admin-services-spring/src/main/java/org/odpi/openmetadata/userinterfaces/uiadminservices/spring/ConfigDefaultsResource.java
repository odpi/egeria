/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.uiadminservices.spring;

import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.userinterfaces.uiadminservices.UIServerAdminServices;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ConfigDefaultsResource sets properties in the configuration document that are used as
 * default values when configuring the subsystems in an UI Server.  If these values
 * are updated after a subsystem is configured, they do not impact that subsystem's configuration.
 */
@RestController
@RequestMapping("/open-metadata/ui-admin-services/users/{userId}/servers/{serverName}")
public class ConfigDefaultsResource
{
    private UIServerAdminServices adminAPI = new UIServerAdminServices();

    /**
     * Set up the default root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces.  It is a value that is sent to other servers to allow
     * then to call this server.
     *
     * The default value is "localhost:8080".
     *
     * ServerURLRoot is used as a default value during the configuration of the server's subsystems.
     * If it is updated after a subsystem is configured then the new value is ignored.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param url  String url.
     * @return void response or
     * UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * UIInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/server-url-root")
    public VoidResponse setServerURLRoot(@PathVariable String userId,
                                         @PathVariable String serverName,
                                         @RequestParam String url)
    {
        return adminAPI.setServerURLRoot(userId, serverName, url);
    }
}
