/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.springframework.web.bind.annotation.*;


/**
 * OMAGServerConfigResource returns the current configuration document for the server.  If the
 * configuration document is not found, a new one is created.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class ConfigResource
{
    private OMAGServerAdminServices adminAPI = new OMAGServerAdminServices();

    /**
     * Return the stored configuration document for the server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return OMAGServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/configuration")
    public OMAGServerConfigResponse getCurrentConfiguration(@PathVariable String userId,
                                                            @PathVariable String serverName)
    {
        return adminAPI.getStoredConfiguration(userId, serverName);
    }

}
