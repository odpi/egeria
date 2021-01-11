/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.api.MyPersonalProfileInterface;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AssetCollectionMember;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethod;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethodType;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.accessservices.communityprofile.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.CountResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * This client covers the management of a user's personal profile.
 */
public class MyProfileManagement implements MyPersonalProfileInterface
{
    private String                     serverName;               /* Initialized in constructor */
    private String                     omasServerURL;            /* Initialized in constructor */
    private CommunityProfileRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler              invalidParameterHandler = new InvalidParameterHandler();
    private CommunityProfileRESTExceptionHandler exceptionHandler        = new CommunityProfileRESTExceptionHandler();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param omasServerURL the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException bad input parameters
     */
    public MyProfileManagement(String     serverName,
                               String     omasServerURL) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);

        this.serverName = serverName;
        this.omasServerURL = omasServerURL;
        this.restClient = new CommunityProfileRESTClient(serverName, omasServerURL);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param omasServerURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public MyProfileManagement(String     serverName,
                               String     omasServerURL,
                               String     userId,
                               String     password) throws  InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);

        this.serverName = serverName;
        this.omasServerURL = omasServerURL;
        this.restClient = new CommunityProfileRESTClient(serverName, omasServerURL, userId, password);
    }


    /**
     * Return the profile for this user.
     *
     * @param userId userId of the user making the request.
     *
     * @return profile object
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public PersonalProfile getMyProfile(String userId) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String   methodName = "getMyProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/my-profile";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        PersonalProfileResponse restResult = restClient.callPersonalProfileGetRESTCall(methodName,
                                                                                       omasServerURL + urlTemplate,
                                                                                       userId);

        return restResult.getPersonalProfile();
    }


    /**
     * Return the total karma points for this user.
     *
     * @param userId userId of the user making the request.
     *
     * @return int
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public long getMyKarmaPoints(String userId) throws InvalidParameterException,
                                                       NoProfileForUserException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        final String   methodName = "getMyKarmaPoints";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/my-profile/karma-points";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        CountResponse restResult = restClient.callCountGetRESTCall(methodName,
                                                                   omasServerURL + urlTemplate,
                                                                   userId);

        exceptionHandler.detectAndThrowNoProfileForUserException(methodName, restResult);

        return restResult.getCount();
    }


    /**
     * Create or update the profile for the requesting user.
     *
     * @param userId calling user
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void setUpMyProfile(String              userId,
                               String              qualifiedName,
                               String              fullName,
                               String              knownName,
                               String              jobTitle,
                               String              jobRoleDescription,
                               Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String   methodName = "setUpMyProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/my-profile";

        final String   qualifiedNameParameterName = "qualifiedName";
        final String   knownNameParameterName = "knownName";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(knownName, knownNameParameterName, methodName);

        MyProfileRequestBody requestBody = new MyProfileRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setFullName(fullName);
        requestBody.setKnownName(knownName);
        requestBody.setJobTitle(jobTitle);
        requestBody.setJobRoleDescription(jobRoleDescription);
        requestBody.setAdditionalProperties(additionalProperties);


        restClient.callGUIDPostRESTCall(methodName,
                                        omasServerURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId);
    }


    /**
     * Delete the profile for the requesting user.
     *
     * @param userId the name of the calling user.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void      deleteMyProfile(String              userId,
                                     String              qualifiedName) throws InvalidParameterException,
                                                                               NoProfileForUserException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String   methodName = "deleteMyProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/my-profile/delete";

        final String   qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        PersonalProfileValidatorRequestBody requestBody = new PersonalProfileValidatorRequestBody();
        requestBody.setQualifiedName(qualifiedName);

        restClient. callVoidPostRESTCall(methodName,
                                         omasServerURL + urlTemplate,
                                         requestBody,
                                         serverName,
                                         userId);
    }



    /**
     * Return the list of contact methods for this user.
     *
     * @param UserId userId of the user making the request.
     *
     * @return list of contact methods
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<ContactMethod> getMyContactDetails(String UserId) throws InvalidParameterException,
                                                                         NoProfileForUserException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Add a new contact method to the requesting user's profile.
     *
     * @param userId the name of the calling user.
     * @param type type of contact method.
     * @param service service for the contact method.
     * @param value account name for the service.
     *
     * @return unique identifier (guid) for the new contact method.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String addMyContactMethod(String              userId,
                                     ContactMethodType   type,
                                     String              service,
                                     String              value) throws InvalidParameterException,
                                                                       NoProfileForUserException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Remove an obsolete contact method from the requesting user's profile.
     *
     * @param userId the name of the calling user.
     * @param contactMethodGUID unique identifier (guid) for the obsolete contact method.
     * @param type type of contact method. This is used to confirm that the GUID is the right one.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void deleteMyContactMethod(String            userId,
                                      String            contactMethodGUID,
                                      ContactMethodType type) throws InvalidParameterException,
                                                                     NoProfileForUserException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
    }


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
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<AssetCollectionMember> getMyAssets(String    userId,
                                                   int       startFrom,
                                                   int       pageSize) throws InvalidParameterException,
                                                              NoProfileForUserException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Add an asset to the identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void  addToMyAssets(String   userId,
                               String   assetGUID) throws InvalidParameterException,
                                                          NoProfileForUserException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {

    }


    /**
     * Remove an asset from identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void  removeFromMyAssets(String   userId,
                                    String   assetGUID) throws InvalidParameterException,
                                                               NoProfileForUserException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {

    }
}
