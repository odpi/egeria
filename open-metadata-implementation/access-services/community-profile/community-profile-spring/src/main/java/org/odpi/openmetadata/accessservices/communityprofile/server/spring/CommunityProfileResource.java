/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.accessservices.communityprofile.server.CommunityProfileRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataSourceProperties;
import org.springframework.web.bind.annotation.*;

/**
 * CommunityProfileResource provides the APIs for maintaining the metadata sources.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}")

@Tag(name="Metadata Access Server: Community Profile OMAS", description="The Community Profile OMAS provides APIs and events for tools and applications that are managing information about people and the way they work together.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/community-profile/overview/"))

public class CommunityProfileResource
{
    private final CommunityProfileRESTServices restAPI = new CommunityProfileRESTServices();



    /**
     * Return the description of this service.
     *
     * @param serverName name of the server to route the request to
     * @param userId identifier of calling user
     *
     * @return service description or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/description")

    public RegisteredOMAGServiceResponse getServiceDescription(@PathVariable String serverName,
                                                               @PathVariable String userId)
    {
        return restAPI.getServiceDescription(serverName, userId);
    }


    /**
     * Return the connection object for the Community Profile OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")

    public OCFConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }


    /* ========================================================
     * The metadata source represents the third party technology this integration processing is connecting to
     */

    /**
     * Create information about the metadata source that is providing user profile information.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody description of the metadata source
     *
     * @return unique identifier of the user profile manager's software server capability or
     * InvalidParameterException  the bean requestBody are invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-sources")

    public GUIDResponse createMetadataSource(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @RequestBody  MetadataSourceProperties requestBody)
    {
        return restAPI.createMetadataSource(serverName, userId, requestBody);
    }


    /**
     * Retrieve the unique identifier of the software server capability that describes a metadata source.  This could be
     * a user profile manager, user access directory and/or a master data manager.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param qualifiedName unique name of the metadata source
     *
     * @return unique identifier of the integration daemon's software server capability or
     *  InvalidParameterException  the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException    problem accessing the property server
     */
    @GetMapping(path = "/metadata-sources/by-name/{qualifiedName}")

    public GUIDResponse getMetadataSourceGUID(@PathVariable String serverName,
                                              @PathVariable String userId,
                                              @PathVariable String qualifiedName)
    {
        return restAPI.getMetadataSourceGUID(serverName, userId, qualifiedName);
    }


    /**
     * Retrieve the properties of the software server capability that describes a metadata source.  This could be
     * a user profile manager, user access directory and/or a master data manager.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     *
     * @return unique identifier of the integration daemon's software server capability or
     *  InvalidParameterException  the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException    problem accessing the property server
     */
    @GetMapping(path = "/metadata-sources/{metadataSourceGUID}")

    public MetadataSourceResponse getMetadataSource(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String metadataSourceGUID)
    {
        return restAPI.getMetadataSource(serverName, userId, metadataSourceGUID);
    }


    /**
     * Update classification of the metadata source as being capable if managing user profiles.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-sources/{metadataSourceGUID}/user-profile-manager")

    public VoidResponse addUserProfileManagerClassification(@PathVariable String          serverName,
                                                            @PathVariable String          userId,
                                                            @PathVariable String          metadataSourceGUID,
                                                            @RequestBody (required = false)
                                                                          NullRequestBody requestBody)
    {
        return restAPI.addUserProfileManagerClassification(serverName, userId, metadataSourceGUID, requestBody);
    }


    /**
     * Update classification of the metadata source that is providing a user access directory information
     * such as the groups and access rights of a user Id.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-sources/{metadataSourceGUID}/user-access-directory")

    public VoidResponse addUserAccessDirectoryClassification(@PathVariable String          serverName,
                                                             @PathVariable String          userId,
                                                             @PathVariable String          metadataSourceGUID,
                                                             @RequestBody (required = false)
                                                                           NullRequestBody requestBody)
    {
        return restAPI.addUserAccessDirectoryClassification(serverName, userId, metadataSourceGUID, requestBody);
    }


    /**
     * Update classification of the metadata source that is a master data manager for user profile information.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-sources/{metadataSourceGUID}/master-data-manager")

    public VoidResponse addMasterDataManagerClassification(@PathVariable String          serverName,
                                                           @PathVariable String          userId,
                                                           @PathVariable String          metadataSourceGUID,
                                                           @RequestBody (required = false)
                                                                         NullRequestBody requestBody)
    {
        return restAPI.addMasterDataManagerClassification(serverName, userId, metadataSourceGUID, requestBody);
    }
}
