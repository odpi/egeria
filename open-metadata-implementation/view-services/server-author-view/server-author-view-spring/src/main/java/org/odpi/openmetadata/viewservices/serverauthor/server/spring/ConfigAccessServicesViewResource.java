/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.serverauthor.server.spring;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.configuration.properties.EnterpriseAccessConfig;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorConfigurationResponse;
import org.odpi.openmetadata.viewservices.serverauthor.services.ServerAuthorViewRESTServices;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ConfigAccessServicesResource provides the configuration for setting up the Open Metadata Access
 * Services (OMASs).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/server-author/users/{userId}/servers/{serverToBeConfiguredName}")

@Tag(name="Server Author OMVS", description="The Server Author OMVS is for user interfaces supporting the creating and editing of OMAG Server Configuration Documents.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/services/omvs/server-author/overview"))

public class ConfigAccessServicesViewResource
{
    private final ServerAuthorViewRESTServices adminAPI = new ServerAuthorViewRESTServices();



    /**
     * Enable a single access service.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @param serviceURLMarker string indicating which access service it is configuring
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/access-services/{serviceURLMarker}")
    public ServerAuthorConfigurationResponse configureAccessService(@PathVariable                   String              userId,
                                               @PathVariable                   String              serverName,
                                               @PathVariable                   String serverToBeConfiguredName,
                                               @PathVariable                   String              serviceURLMarker,
                                               @RequestBody(required = false)  Map<String, Object> accessServiceOptions)
    {
        return adminAPI.configureAccessService(userId, serverName, serverToBeConfiguredName, serviceURLMarker, accessServiceOptions);
    }


    /**
     * Disable a single access service.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param serviceURLMarker string indicating which access service it is configuring
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @DeleteMapping(path = "/access-services/{serviceURLMarker}")
    public ServerAuthorConfigurationResponse disableAccessService(  @PathVariable                   String              userId,
                                                                    @PathVariable                   String              serverName,
                                                                    @PathVariable                   String              serverToBeConfiguredName,
                                                                    @PathVariable                   String              serviceURLMarker)
    {
        return adminAPI.disableAccessService(userId, serverName, serverToBeConfiguredName, serviceURLMarker);
    }


    /**
     * Enable all access services that are registered with this server platform.
     * The access services are set up to use the default event bus.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @return the current stored configuration
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/access-services")
    public ServerAuthorConfigurationResponse configureAllAccessServices(@PathVariable                  String              userId,
                                                                        @PathVariable                  String              serverName,
                                                                        @PathVariable                  String              serverToBeConfiguredName,
                                                                        @RequestBody(required = false) Map<String, Object> accessServiceOptions)
    {
        return adminAPI.configureAllAccessServices(userId, serverName, serverToBeConfiguredName, accessServiceOptions);
    }

    /**
     * Set up the configuration that controls the enterprise repository services.  These services are part
     * of the Open Metadata Repository Services (OMRS).  They provide federated queries and federated event
     * notifications that cover metadata from the local repository plus any repositories connected via
     * open metadata repository cohorts.
     *
     * @param userId                   user that is issuing the request
     * @param serverName               local server name
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param enterpriseAccessConfig  enterprise repository services configuration properties.
     * @return the current stored configuration
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or enterpriseAccessConfig parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @PostMapping(path = "/enterprise-access/configuration")
    public ServerAuthorConfigurationResponse setEnterpriseAccessConfig(@PathVariable String                 userId,
                                                  @PathVariable String                 serverName,
                                                  @PathVariable String serverToBeConfiguredName,
                                                  @RequestBody  EnterpriseAccessConfig enterpriseAccessConfig)
    {
        return adminAPI.setEnterpriseAccessConfig(userId, serverName, serverToBeConfiguredName, enterpriseAccessConfig);
    }
}
