/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerAdminForAccessServices;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EnterpriseAccessConfig;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * OMAGServerAccessServicesAdminResource provides the configuration for setting up the Open Metadata Access
 * Services (OMASs).
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class OMAGServerAccessServicesAdminResource
{
    private OMAGServerAdminForAccessServices adminAPI = new OMAGServerAdminForAccessServices();

    /**
     * Enable all access services that are installed into this server.  The access services are set up to use the
     * default event bus
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/access-services")
    public VoidResponse enableAccessServices(@PathVariable                  String              userId,
                                             @PathVariable                  String              serverName,
                                             @RequestBody(required = false) Map<String, Object> accessServiceOptions)
    {
        return adminAPI.enableAccessServices(userId, serverName, accessServiceOptions);
    }


    /**
     * Disable the access services.  This removes all configuration for the access services
     * and disables the enterprise repository services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/access-services")
    public VoidResponse disableAccessServices(@PathVariable String          userId,
                                              @PathVariable String          serverName)
    {
        return adminAPI.disableAccessServices(userId, serverName);
    }


    /**
     * Set up the configuration for selected open metadata access services (OMASs).  This overrides
     * the current default values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param accessServicesConfig  list of configuration properties for each access service.
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/access-services/configuration")
    public VoidResponse setAccessServicesConfig(@PathVariable String                    userId,
                                                @PathVariable String                    serverName,
                                                @RequestBody List<AccessServiceConfig> accessServicesConfig)
    {
        return adminAPI.setAccessServicesConfig(userId, serverName, accessServicesConfig);
    }


    /**
     * Set up the configuration that controls the enterprise repository services.  These services are part
     * of the Open Metadata Repository Services (OMRS).  They provide federated queries and federated event
     * notifications that cover metadata from the local repository plus any repositories connected via
     * open metadata repository cohorts.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param enterpriseAccessConfig  enterprise repository services configuration properties.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or enterpriseAccessConfig parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/enterprise-access/configuration")
    public VoidResponse setEnterpriseAccessConfig(@PathVariable String                 userId,
                                                  @PathVariable String                 serverName,
                                                  @RequestBody EnterpriseAccessConfig enterpriseAccessConfig)
    {
        return adminAPI.setEnterpriseAccessConfig(userId, serverName, enterpriseAccessConfig);
    }
}
