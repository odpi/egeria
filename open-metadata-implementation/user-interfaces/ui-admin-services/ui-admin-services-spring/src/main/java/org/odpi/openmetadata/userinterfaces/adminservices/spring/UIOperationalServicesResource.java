/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices.spring;

import org.odpi.openmetadata.adminservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.openmetadata.userinterface.adminservices.rest.UIServerConfigResponse;
import org.odpi.openmetadata.userinterfaces.adminservices.UIServerOperationalServices;
import org.springframework.web.bind.annotation.*;

/**
 * UIOperationalServicesResource provides the REST API for controlling the start up, management and
 * shutdown of services in the OMAG Server.
 */
@RestController
@RequestMapping("/open-metadata/ui-admin-services/users/{userId}/servers/{serverName}")
public class UIOperationalServicesResource
{
    private UIServerOperationalServices operationalServices = new UIServerOperationalServices();

    /*
     * ========================================================================================
     * Activate and deactivate the open metadata and governance capabilities in the OMAG Server
     */

    /**
     * Activate the open metadata and governance services using the stored configuration information.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @PostMapping( path = "/instance")
    public SuccessMessageResponse activateWithStoredConfig(@PathVariable String userId,
                                                           @PathVariable String serverName)
    {
        return operationalServices.activateWithStoredConfig(userId, serverName);
    }


    /**
     * Activate the open metadata and governance services using the supplied configuration
     * document.
     *
     * @param userId  user that is issuing the request
     * @param configuration  properties used to initialize the services
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @PostMapping( path = "/instance/configuration")
    public SuccessMessageResponse activateWithSuppliedConfig(@PathVariable String           userId,
                                                             @PathVariable String           serverName,
                                                             @RequestBody UIServerConfig configuration)
    {
        return operationalServices.activateWithSuppliedConfig(userId, serverName, configuration);
    }


    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/instance")
    public VoidResponse deactivateTemporarily(@PathVariable String  userId,
                                              @PathVariable String  serverName)
    {
        return operationalServices.deactivateTemporarily(userId, serverName);
    }


    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "")
    public VoidResponse deactivatePermanently(@PathVariable String  userId,
                                              @PathVariable String  serverName)
    {
        return operationalServices.deactivatePermanently(userId, serverName);
    }


    /*
     * =============================================================
     * Operational status and control
     */

    /**
     * Return the configuration used for the current active instance of the server.  Null is returned if
     * the server instance is not running.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return configuration properties used to initialize the server or null if not running or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    @GetMapping( path = "/instance/configuration")
    public UIServerConfigResponse getActiveConfiguration(@PathVariable String           userId,
                                                         @PathVariable String           serverName)
    {
        return operationalServices.getActiveConfiguration(userId, serverName);
    }

}
