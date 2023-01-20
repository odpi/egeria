/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminForAccessServices;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EnterpriseAccessConfig;
import org.odpi.openmetadata.adminservices.rest.AccessServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.AccessServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringMapResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ConfigAccessServicesResource provides the configuration for setting up the Open Metadata Access
 * Services (OMASs).
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/servers/"))

public class ConfigAccessServicesResource
{
    private final OMAGServerAdminForAccessServices adminAPI = new OMAGServerAdminForAccessServices();


    /**
     * Return the list of access services that are configured for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return list of access service descriptions
     */
    @GetMapping(path = "/access-services")
    public RegisteredOMAGServicesResponse getConfiguredAccessServices(@PathVariable String userId,
                                                                      @PathVariable String serverName)
    {
        return adminAPI.getConfiguredAccessServices(userId, serverName);
    }


    /**
     * Return the configuration for the access services in this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return list of access service configurations
     */
    @GetMapping(path = "/access-services/configuration")
    public AccessServicesResponse getAccessServicesConfiguration(@PathVariable String userId,
                                                                 @PathVariable String serverName)
    {
        return adminAPI.getAccessServicesConfiguration(userId, serverName);
    }


    /**
     * Enable a single access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @param serviceURLMarker string indicating which access service it is configuring
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/access-services/{serviceURLMarker}")
    public VoidResponse configureAccessService(@PathVariable                   String              userId,
                                               @PathVariable                   String              serverName,
                                               @PathVariable                   String              serviceURLMarker,
                                               @RequestBody(required = false)  Map<String, Object> accessServiceOptions)
    {
        return adminAPI.configureAccessService(userId, serverName, serviceURLMarker, accessServiceOptions);
    }


    /**
     * Enable all access services that are registered with this server platform.
     * The access services are set up to use the default event bus.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/access-services")
    public VoidResponse configureAllAccessServices(@PathVariable                  String              userId,
                                                   @PathVariable                  String              serverName,
                                                   @RequestBody(required = false) Map<String, Object> accessServiceOptions)
    {
        return adminAPI.configureAllAccessServices(userId, serverName, accessServiceOptions);
    }



    /**
     * Enable a single access service.
     * This version of the call does not set up the InTopic nor the OutTopic.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @param serviceURLMarker string indicating which access service it is configuring
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/access-services/{serviceURLMarker}/no-topics")
    public VoidResponse configureAccessServiceNoTopics(@PathVariable                  String              userId,
                                                       @PathVariable                  String              serverName,
                                                       @PathVariable                  String              serviceURLMarker,
                                                       @RequestBody(required = false) Map<String, Object> accessServiceOptions)
    {
        return adminAPI.configureAccessServiceNoTopics(userId, serverName, serviceURLMarker, accessServiceOptions);
    }


    /**
     * Enable all access services that are registered with this server platform.
     * This version of the call does not set up the InTopic nor the OutTopic.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/access-services/no-topics")
    public VoidResponse configureAllAccessServicesNoTopics(@PathVariable                  String              userId,
                                                           @PathVariable                  String              serverName,
                                                           @RequestBody(required = false) Map<String, Object> accessServiceOptions)
    {
        return adminAPI.configureAllAccessServicesNoTopics(userId, serverName, accessServiceOptions);
    }


    /**
     * Disable the access services.  This removes all configuration for the access services
     * and disables the enterprise repository services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/access-services")
    public VoidResponse clearAllAccessServices(@PathVariable String          userId,
                                               @PathVariable String          serverName)
    {
        return adminAPI.clearAllAccessServices(userId, serverName);
    }


    /**
     * Retrieve the config for an access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker string indicating which access service it is configuring
     * @return AccessServiceConfig response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @GetMapping(path = "/access-services/{serviceURLMarker}")
    public AccessServiceConfigResponse getAccessServiceConfig(@PathVariable String userId,
                                                              @PathVariable String serverName,
                                                              @PathVariable String serviceURLMarker)
    {
        return adminAPI.getAccessServiceConfig(userId, serverName, serviceURLMarker);
    }


    /**
     * Retrieve the topic names for this access service
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param serviceURLMarker string indicating which access service it requested
     *
     * @return map of topic names or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @GetMapping(path = "/access-services/{serviceURLMarker}/topic-names")
    public StringMapResponse getAccessServiceTopicNames(@PathVariable String userId,
                                                        @PathVariable String serverName,
                                                        @PathVariable String serviceURLMarker)
    {
        return adminAPI.getAccessServiceTopicNames(userId, serverName, serviceURLMarker);
    }


    /**
     * Retrieve the topic names for all access services
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     *
     * @return map of topic names or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @GetMapping(path = "/access-services/topic-names")
    public StringMapResponse  getAllAccessServiceTopicNames(@PathVariable String userId,
                                                            @PathVariable String serverName)
    {
        return adminAPI.getAllAccessServiceTopicNames(userId, serverName);
    }


    /**
     * Update the in topic name for a specific access service.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param serviceURLMarker string indicating which access service it requested
     * @param topicName string for new topic name
     *
     * @return map of topic names or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @PostMapping(path = "/access-services/{serviceURLMarker}/topic-names/in-topic")
    public VoidResponse  overrideAccessServiceInTopicName(@PathVariable String userId,
                                                          @PathVariable String serverName,
                                                          @PathVariable String serviceURLMarker,
                                                          @RequestBody  String topicName)
    {
        return adminAPI.overrideAccessServiceInTopicName(userId, serverName, serviceURLMarker, topicName);
    }


    /**
     * Update the out topic name for a specific access service.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param serviceURLMarker string indicating which access service it requested
     * @param topicName string for new topic name
     *
     * @return map of topic names or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @PostMapping(path = "/access-services/{serviceURLMarker}/topic-names/out-topic")
    public VoidResponse  overrideAccessServiceOutTopicName(@PathVariable String userId,
                                                           @PathVariable String serverName,
                                                           @PathVariable String serviceURLMarker,
                                                           @RequestBody  String topicName)
    {
        return adminAPI.overrideAccessServiceOutTopicName(userId, serverName, serviceURLMarker, topicName);
    }


    /**
     * Remove the config for an access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker string indicating which access service to clear
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/access-services/{serviceURLMarker}")
    public VoidResponse clearAccessService(@PathVariable String userId,
                                           @PathVariable String serverName,
                                           @PathVariable String serviceURLMarker)
    {
        return adminAPI.clearAccessService(userId, serverName, serviceURLMarker);
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
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @PostMapping(path = "/access-services/configuration")
    public VoidResponse setAccessServicesConfig(@PathVariable String                    userId,
                                                @PathVariable String                    serverName,
                                                @RequestBody  List<AccessServiceConfig> accessServicesConfig)
    {
        return adminAPI.setAccessServicesConfig(userId, serverName, accessServicesConfig);
    }


    /**
     * Set up the default remote enterprise topic.  This allows a remote process to monitor enterprise topic events.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param configurationProperties additional properties for the cohort
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or null userId parameter.
     */
    @PostMapping(path = "/enterprise-access/remote-topic")
    public VoidResponse addRemoteEnterpriseTopic(@PathVariable String               userId,
                                                 @PathVariable String               serverName,
                                                 @RequestBody  Map<String, Object>  configurationProperties)
    {
        return adminAPI.addRemoteEnterpriseTopic(userId, serverName, configurationProperties);
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
     * OMAGInvalidParameterException invalid serverName or enterpriseAccessConfig parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @PostMapping(path = "/enterprise-access/configuration")
    public VoidResponse setEnterpriseAccessConfig(@PathVariable String                 userId,
                                                  @PathVariable String                 serverName,
                                                  @RequestBody  EnterpriseAccessConfig enterpriseAccessConfig)
    {
        return adminAPI.setEnterpriseAccessConfig(userId, serverName, enterpriseAccessConfig);
    }
}
