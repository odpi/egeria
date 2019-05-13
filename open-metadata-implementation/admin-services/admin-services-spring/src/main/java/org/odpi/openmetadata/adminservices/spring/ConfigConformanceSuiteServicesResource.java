/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGConformanceSuiteConfigServices;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * ConfigAccessServicesResource provides the configuration for setting up the Open Metadata
 * Conformance Suite services in an OMAG server.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class ConfigConformanceSuiteServicesResource
{
    private OMAGConformanceSuiteConfigServices adminAPI = new OMAGConformanceSuiteConfigServices();

    /**
     * Request that the conformance suite services are activated in this server to test the
     * support of the repository services running in the server named tutRepositoryServerName.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param tutRepositoryServerName name of the server that the repository workbench should test.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/conformance-suite-workbenches/repository-workbench/repositories/{tutRepositoryServerName}")

    public VoidResponse enableRepositoryConformanceSuiteWorkbench(@PathVariable String userId,
                                                                  @PathVariable String serverName,
                                                                  @PathVariable String tutRepositoryServerName)
    {
        return adminAPI.enableRepositoryConformanceSuiteWorkbench(userId, serverName, tutRepositoryServerName);
    }


    /**
     * Request that the conformance suite services are activated in this server to test the
     * support of the platform services running in the platform at tutPlatformRootURL.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody url of the OMAG platform to test.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/conformance-suite-workbenches/platform-workbench/platforms")

    public VoidResponse enablePlatformConformanceSuiteWorkbench(@PathVariable String         userId,
                                                                @PathVariable String         serverName,
                                                                @RequestBody  URLRequestBody requestBody)
    {
        return adminAPI.enablePlatformConformanceSuiteWorkbench(userId, serverName, requestBody);
    }


    /**
     * Request that the repository conformance suite tests are deactivated in this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/conformance-suite-workbenches/repository-workbench")
    public VoidResponse disableRepositoryConformanceSuiteServices(@PathVariable String    userId,
                                                                  @PathVariable String    serverName)
    {
        return adminAPI.disableRepositoryConformanceSuiteServices(userId, serverName);
    }


    /**
     * Request that the repository conformance suite tests are deactivated in this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/conformance-suite-workbenches/platform-workbench")
    public VoidResponse disablePlatformConformanceSuiteServices(@PathVariable String    userId,
                                                                @PathVariable String    serverName)
    {
        return adminAPI.disablePlatformConformanceSuiteServices(userId, serverName);
    }


    /**
     * Request that the conformance suite services are deactivated in this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/conformance-suite-workbenches")
    public VoidResponse disableAllConformanceSuiteWorkbenches(@PathVariable String    userId,
                                                              @PathVariable String    serverName)
    {
        return adminAPI.disableAllConformanceSuiteWorkbenches(userId, serverName);
    }
}
