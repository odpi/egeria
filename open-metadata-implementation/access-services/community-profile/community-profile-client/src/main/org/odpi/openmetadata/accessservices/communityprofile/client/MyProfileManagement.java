/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.MyPersonalProfileInterface;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AssetCollectionMember;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.accessservices.communityprofile.rest.*;

import java.util.List;
import java.util.Map;

/**
 * This client covers the management of a user's personal profile.
 */
public class MyProfileManagement implements MyPersonalProfileInterface
{
    private String     serverName;               /* Initialized in constructor */
    private String     omasServerURL;            /* Initialized in constructor */
    private RESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();
    private NullRequestBody         nullRequestBody         = new NullRequestBody();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param omasServerURL the network address of the server running the OMAS REST servers
     */
    public MyProfileManagement(String     serverName,
                               String     omasServerURL)
    {
        this.serverName = serverName;
        this.omasServerURL = omasServerURL;
        this.restClient = new RESTClient(serverName, omasServerURL);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param omasServerURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     */
    public MyProfileManagement(String     serverName,
                               String     omasServerURL,
                               String     userId,
                               String     password)
    {
        this.serverName = serverName;
        this.omasServerURL = omasServerURL;
        this.restClient = new RESTClient(serverName, omasServerURL, userId, password);
    }


    /**
     * Return the profile for this user.
     *
     * @param userId userId of the user making the request.
     *
     * @return profile object
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public PersonalProfile getMyProfile(String userId) throws InvalidParameterException,
                                                              NoProfileForUserException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String   methodName = "getMyProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/my-profile";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        PersonalProfileResponse restResult = restClient.callPersonalProfileGetRESTCall(methodName,
                                                                                       omasServerURL + urlTemplate,
                                                                                       userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowNoProfileForUserException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

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
    public int getMyKarmaPoints(String userId) throws InvalidParameterException,
                                                      NoProfileForUserException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException
    {
        final String   methodName = "getMyKarmaPoints";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/my-profile/karma-points";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        CountResponse restResult = restClient.callCountGetRESTCall(methodName,
                                                                   omasServerURL + urlTemplate,
                                                                   userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowNoProfileForUserException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getCount();
    }


    /**
     * Create or update the profile for the requesting user.
     *
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param profileProperties  properties about the individual for a new type that is the subclass of Person.
     * @param additionalProperties  additional properties about the individual.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setUpMyProfile(String              userId,
                                String              qualifiedName,
                                String              fullName,
                                String              knownName,
                                String              jobTitle,
                                String              jobRoleDescription,
                                Map<String, Object> profileProperties,
                                Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String   methodName = "updateMyProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/my-profile";

        final String   qualifiedNameParameterName = "qualifiedName";
        final String   knownNameParameterName = "knownName";

        invalidParameterHandler.validateOMASServerURL(omasServerURL, methodName);
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


        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Create or update the profile for the requesting user.
     *
     * @param userId the name of the calling user.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void      deleteMyProfile(String              userId,
                                     String              qualifiedName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String   methodName = "deleteMyProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/my-profile/delete";

        final String   qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateOMASServerURL(omasServerURL, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        PersonalProfileValidatorRequestBody requestBody = new PersonalProfileValidatorRequestBody();
        requestBody.setQualifiedName(qualifiedName);

        VoidResponse restResult = restClient. callVoidPostRESTCall(methodName,
                                                                   omasServerURL + urlTemplate,
                                                                   requestBody,
                                                                   serverName,
                                                                   userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
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
