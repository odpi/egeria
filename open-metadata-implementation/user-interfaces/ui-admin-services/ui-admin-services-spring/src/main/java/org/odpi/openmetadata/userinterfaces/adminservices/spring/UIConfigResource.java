/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices.spring;


import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.openmetadata.userinterface.adminservices.rest.UIServerConfigResponse;
import org.odpi.openmetadata.userinterfaces.adminservices.UIServerAdminServices;
import org.springframework.web.bind.annotation.*;


/**
 * UIServerConfigResource returns the current configuration document for the server.  If the
 * configuration document is not found, a new one is created.
 */
@RestController
@RequestMapping("/open-metadata/ui-admin-services/users/{userId}/servers/{serverName}")
public class UIConfigResource
{
    private UIServerAdminServices adminAPI = new UIServerAdminServices();

    /**
     * Return the stored configuration document for the server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return UIServerConfig properties or
     * UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * UIInvalidParameterException invalid serverName parameter.
     */
    @GetMapping( path = "/configuration")
    public UIServerConfigResponse getCurrentConfiguration(@PathVariable String userId,
                                                          @PathVariable String serverName)
    {
        return adminAPI.getStoredConfiguration(userId, serverName);
    }


    /**
     * Set up the configuration properties for an UI Server in a single command.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param uiServerConfig  configuration for the server
     * @return void response or
     * UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * UIInvalidParameterException invalid serverName or UIServerConfig parameter.
     */
    @PostMapping( path = "/configuration")
    public VoidResponse setUIServerConfig(@PathVariable String         userId,
                                          @PathVariable String         serverName,
                                          @RequestBody  UIServerConfig uiServerConfig)
    {
        return adminAPI.setUIServerConfig(userId, serverName, uiServerConfig);
    }
    /**
     * Push the configuration for the server to another UI Server Platform.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param destinationPlatform  location of the platform where the config is to be deployed to
     * @return void response or
     * UINotAuthorizedException the supplied userId is not authorized to issue this command or
     * UIConfigurationErrorException there is a problem using the supplied configuration or
     * UIInvalidParameterException invalid serverName or destinationPlatform parameter.
     */
    @PostMapping( path = "/configuration/deploy")
    public VoidResponse deployUIServerConfig(@PathVariable String userId,
                                               @PathVariable String           serverName,
                                               @RequestBody URLRequestBody destinationPlatform)
    {
        return adminAPI.deployUIServerConfig(userId, serverName, destinationPlatform);
    }
}
