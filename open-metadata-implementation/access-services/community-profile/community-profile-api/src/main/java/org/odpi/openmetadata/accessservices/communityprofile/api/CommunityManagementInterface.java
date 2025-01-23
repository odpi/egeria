/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorRoleElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CommunityElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The CommunityManagementInterface provides methods for managing communities, their membership and content.
 * Communities allow groups of subject-matter experts to work together and share content and ideas.
 */
public interface CommunityManagementInterface
{

    /**
     * Create a new metadata element to represent the community.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param communityProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createCommunity(String              userId,
                           String              externalSourceGUID,
                           String              externalSourceName,
                           CommunityProperties communityProperties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new community.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createCommunityFromTemplate(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       String             templateGUID,
                                       TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Update the metadata element representing a community.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param communityGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param communityProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateCommunity(String              userId,
                         String              externalSourceGUID,
                         String              externalSourceName,
                         String              communityGUID,
                         boolean             isMergeUpdate,
                         CommunityProperties communityProperties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Create a membership relationship between a community and a person role to show that anyone appointed to the role is a member of the community.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param communityGUID unique identifier of the community
     * @param membershipProperties describes the permissions that the role has in the community
     * @param personRoleGUID unique identifier of the role
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupCommunityRole(String                        userId,
                            String                        externalSourceGUID,
                            String                        externalSourceName,
                            String                        communityGUID,
                            CommunityMembershipProperties membershipProperties,
                            String                        personRoleGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Remove a membership relationship between a community and a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param communityGUID unique identifier of the community
     * @param personRoleGUID unique identifier of the role
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearCommunityRole(String userId,
                            String externalSourceGUID,
                            String externalSourceName,
                            String communityGUID,
                            String personRoleGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Remove the metadata element representing a community.  This will delete all anchored
     * elements such as comments.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param communityGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeCommunity(String userId,
                         String externalSourceGUID,
                         String externalSourceName,
                         String communityGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<CommunityElement> findCommunities(String userId,
                                           String searchString,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<CommunityElement> getCommunitiesByName(String userId,
                                                String name,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Retrieve the list of communities.
     *
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<CommunityElement> getCommunities(String userId,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Return information about the person roles linked to a community.
     *
     * @param userId calling user
     * @param communityGUID unique identifier for the community
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ActorRoleElement> getRolesForCommunity(String userId,
                                                String communityGUID,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Retrieve the community metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param communityGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    CommunityElement getCommunityByGUID(String userId,
                                        String communityGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;
}
