/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.api;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.AssetCollectionMember;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.CommunityCollectionMember;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ProjectCollectionMember;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * MyFavouriteCollectionsInterface covers the management of the MyAssets, MyProjects and MyCommunities
 * collections associated with a person's personal profile.
 */
public interface MyFavouriteCollectionsInterface
{
    /**
     * Return a list of assets that the specified user has added to their favorites list.
     *
     * @param userId     userId of user making request.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of asset details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<AssetCollectionMember> getMyAssets(String userId,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException;


    /**
     * Add an asset to the identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  addToMyAssets(String userId,
                        String assetGUID) throws InvalidParameterException,
                                                 PropertyServerException,
                                                 UserNotAuthorizedException;


    /**
     * Remove an asset from identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  removeFromMyAssets(String userId,
                             String assetGUID) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;


    /**
     * Return a list of projects that the specified user has added to their favorites list.
     *
     * @param userId     userId of user making request.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of project details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<ProjectCollectionMember> getMyProjects(String userId,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException;


    /**
     * Add a project to the identified user's list of favorite projects.
     *
     * @param userId     userId of user making request.
     * @param projectGUID  unique identifier of the project.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  addToMyProjects(String userId,
                          String projectGUID) throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException;


    /**
     * Remove a project from identified user's list of favorite projects.
     *
     * @param userId     userId of user making request.
     * @param projectGUID  unique identifier of the project.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  removeFromMyProjects(String userId,
                               String projectGUID) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;


    /**
     * Return a list of communities that the specified user has added to their favorites list.
     *
     * @param userId     userId of user making request.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of community details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CommunityCollectionMember> getMyCommunities(String userId,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException;


    /**
     * Add a community to the identified user's list of favorite communities.
     *
     * @param userId     userId of user making request.
     * @param communityGUID  unique identifier of the community.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  addToMyCommunities(String userId,
                             String communityGUID) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;


    /**
     * Remove a community from identified user's list of favorite communities.
     *
     * @param userId     userId of user making request.
     * @param communityGUID  unique identifier of the community.
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException there is a problem updating information in the property server(s)
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request
     */
    void  removeFromMyCommunities(String userId,
                                  String communityGUID) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException;

}
