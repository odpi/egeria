/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
@RequestMapping("/open-metadata/admin-services/servers/{serverName}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Server Configuration", description="The server configuration administration services support the configuration" +
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
     * @param serverName name of server
     * @param delegatingUserId external userId making request
     * @return list of access service descriptions
     */
    @GetMapping(path = "/access-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConfiguredAccessServices",
               description="Return the list of access services that are configured for this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public RegisteredOMAGServicesResponse getConfiguredAccessServices(@PathVariable String serverName,
                                                                      @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.getConfiguredAccessServices(serverName, delegatingUserId);
    }


    /**
     * Return the configuration for the access services in this server.
     *
     * @param serverName name of server
     * @param delegatingUserId external userId making request
     * @return list of access service configurations
     */
    @GetMapping(path = "/access-services/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAccessServicesConfiguration",
               description="Return the detailed configuration for the access services in this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public AccessServicesResponse getAccessServicesConfiguration(@PathVariable String serverName,
                                                                 @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.getAccessServicesConfiguration(serverName, delegatingUserId);
    }


    /**
     * Enable a single access service.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @param serviceURLMarker string indicating which access service it is configuring
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/access-services/{serviceURLMarker}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="configureAccessService",
               description="Enable a single access service.  This access service will send notifications if it is part of its implementation.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public VoidResponse configureAccessService(@PathVariable                   String              serverName,
                                               @PathVariable                   String              serviceURLMarker,
                                               @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                               @RequestBody(required = false)  Map<String, Object> accessServiceOptions)
    {
        return adminAPI.configureAccessService(serverName, delegatingUserId, serviceURLMarker, accessServiceOptions);
    }


    /**
     * Enable all access services that are registered with this server platform.
     * The access services are set up to use the default event bus.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/access-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="configureAllAccessServices",
               description="Enable all access services that are registered with this server platform.  The access services will send notifications if it is part of its implementation.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))


    public VoidResponse configureAllAccessServices(@PathVariable                  String              serverName,
                                                   @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                   @RequestBody(required = false) Map<String, Object> accessServiceOptions)
    {
        return adminAPI.configureAllAccessServices(serverName, delegatingUserId, accessServiceOptions);
    }



    /**
     * Enable a single access service.
     * This version of the call does not set up the InTopic nor the OutTopic.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @param serviceURLMarker string indicating which access service it is configuring
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/access-services/{serviceURLMarker}/no-topics")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="configureAccessServiceNoTopics",
               description="Enable a single access service.  Notifications, if supported, are disabled.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public VoidResponse configureAccessServiceNoTopics(@PathVariable                  String              serverName,
                                                       @PathVariable                  String              serviceURLMarker,
                                                       @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                       @RequestBody(required = false) Map<String, Object> accessServiceOptions)
    {
        return adminAPI.configureAccessServiceNoTopics(serverName, delegatingUserId, serviceURLMarker, accessServiceOptions);
    }


    /**
     * Enable all access services that are registered with this server platform.
     * This version of the call does not set up the InTopic nor the OutTopic.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/access-services/no-topics")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="configureAllAccessServicesNoTopics",
               description="Enable all access services that are registered with this server platform.  Notifications, if supported, are disabled.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public VoidResponse configureAllAccessServicesNoTopics(@PathVariable                  String              serverName,
                                                           @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                           @RequestBody(required = false) Map<String, Object> accessServiceOptions)
    {
        return adminAPI.configureAllAccessServicesNoTopics(serverName, delegatingUserId, accessServiceOptions);
    }


    /**
     * Disable the access services.  This removes all configuration for the access services and disables the enterprise repository services.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/access-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearAllAccessServices",
               description="Disable the access services.  This removes all configuration for the access services and disables the enterprise repository services.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public VoidResponse clearAllAccessServices(@PathVariable String serverName,
                                               @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.clearAllAccessServices(serverName, delegatingUserId);
    }


    /**
     * Retrieve the config for an access service.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param serviceURLMarker string indicating which access service it is configuring
     * @return AccessServiceConfig response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @GetMapping(path = "/access-services/{serviceURLMarker}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAccessServiceConfig",
               description="Retrieve the config for an access service.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public AccessServiceConfigResponse getAccessServiceConfig(@PathVariable String serverName,
                                                              @PathVariable String serviceURLMarker,
                                                              @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.getAccessServiceConfig(serverName, delegatingUserId, serviceURLMarker);
    }


    /**
     * Retrieve the topic names for this access service.
     *
     * @param serverName            local server name.
     * @param delegatingUserId external userId making request
     * @param serviceURLMarker string indicating which access service it requested
     *
     * @return map of topic names or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @GetMapping(path = "/access-services/{serviceURLMarker}/topic-names")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAccessServiceTopicNames",
               description="Retrieve the topic names for this access service.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public StringMapResponse getAccessServiceTopicNames(@PathVariable String serverName,
                                                        @PathVariable String serviceURLMarker,
                                                        @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.getAccessServiceTopicNames(serverName, delegatingUserId, serviceURLMarker);
    }


    /**
     * Retrieve the topic names for all access services.
     *
     * @param serverName            local server name.
     * @param delegatingUserId external userId making request
     *
     * @return map of topic names or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @GetMapping(path = "/access-services/topic-names")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAllAccessServiceTopicNames",
               description="Retrieve the topic names for all access services.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public StringMapResponse  getAllAccessServiceTopicNames(@PathVariable String serverName,
                                                            @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.getAllAccessServiceTopicNames(serverName, delegatingUserId);
    }


    /**
     * Update the out topic name for a specific access service.
     *
     * @param serverName            local server name.
     * @param delegatingUserId external userId making request
     * @param serviceURLMarker string indicating which access service it requested
     * @param topicName string for new topic name
     *
     * @return map of topic names or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @PostMapping(path = "/access-services/{serviceURLMarker}/topic-names/out-topic")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="overrideAccessServiceOutTopicName",
               description="Update the out topic name for a specific access service.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public VoidResponse  overrideAccessServiceOutTopicName(@PathVariable String serverName,
                                                           @PathVariable String serviceURLMarker,
                                                           @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                           @RequestBody  String topicName)
    {
        return adminAPI.overrideAccessServiceOutTopicName(serverName, delegatingUserId, serviceURLMarker, topicName);
    }


    /**
     * Remove the config for an access service.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param serviceURLMarker string indicating which access service to clear
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/access-services/{serviceURLMarker}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearAccessService",
               description="Remove the config for an access service.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public VoidResponse clearAccessService(@PathVariable String serverName,
                                           @PathVariable String serviceURLMarker,
                                           @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.clearAccessService(serverName, delegatingUserId, serviceURLMarker);
    }


    /**
     * Set up the configuration for selected open metadata access services (OMASs).  This overrides the current configured values.
     *
     * @param serverName            local server name.
     * @param delegatingUserId external userId making request
     * @param accessServicesConfig  list of configuration properties for each access service.
     * @return void response or
     * UserNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or accessServicesConfig parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @PostMapping(path = "/access-services/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setAccessServicesConfig",
               description="Set up the configuration for selected open metadata access services (OMASs).  This overrides the current configured values.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public VoidResponse setAccessServicesConfig(@PathVariable String                    serverName,
                                                @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                @RequestBody  List<AccessServiceConfig> accessServicesConfig)
    {
        return adminAPI.setAccessServicesConfig(serverName, delegatingUserId, accessServicesConfig);
    }


    /**
     * Set up the default remote enterprise topic.  This allows a remote process to monitor enterprise topic events.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param configurationProperties additional properties for the cohort
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or null userId parameter.
     */
    @PostMapping(path = "/enterprise-access/remote-topic")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addRemoteEnterpriseTopic",
               description="Set up the default remote enterprise topic.  This allows a remote process to monitor enterprise topic events.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public VoidResponse addRemoteEnterpriseTopic(@PathVariable String               serverName,
                                                 @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                 @RequestBody  Map<String, Object>  configurationProperties)
    {
        return adminAPI.addRemoteEnterpriseTopic(serverName, delegatingUserId, configurationProperties);
    }


    /**
     * Set up the configuration that controls the enterprise repository services.  These services are part
     * of the Open Metadata Repository Services (OMRS).  They provide federated queries and federated event
     * notifications that cover metadata from the local repository plus any repositories connected via
     * open metadata repository cohorts.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @param enterpriseAccessConfig  enterprise repository services configuration properties.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or enterpriseAccessConfig parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @PostMapping(path = "/enterprise-access/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setEnterpriseAccessConfig",
               description="Set up the configuration that controls the enterprise repository services.  These services are part" +
                                   " of the Open Metadata Repository Services (OMRS).  They provide federated queries and federated event" +
                                   " notifications that cover metadata from the local repository plus any repositories connected via" +
                                   " open metadata repository cohorts.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omas/"))

    public VoidResponse setEnterpriseAccessConfig(@PathVariable String                 serverName,
                                                  @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                  @RequestBody  EnterpriseAccessConfig enterpriseAccessConfig)
    {
        return adminAPI.setEnterpriseAccessConfig(serverName, delegatingUserId, enterpriseAccessConfig);
    }
}
