/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.MyProfileManagementInterface;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Asset;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.accessservices.communityprofile.rest.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * The Community Profile Open Metadata Access Service (OMAS) is used by tools and administrators to
 * maintain information associated with individuals and communities.  This interface covers the management
 * of a user's personal profile.
 */
public class MyProfileManagement implements MyProfileManagementInterface
{
    private String          serverName;     /* Initialized in constructor */
    private String          omasServerURL;  /* Initialized in constructor */
    private NullRequestBody nullRequestBody = new NullRequestBody();


    /**
     * Create a new CommunityProfile client.
     *
     * @param serverName name of the server to connect to
     * @param newServerURL the network address of the server running the OMAS REST servers
     */
    public MyProfileManagement(String     serverName,
                               String     newServerURL)
    {
        this.serverName = serverName;
        this.omasServerURL = newServerURL;
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
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/my-profile";


        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);

        MyProfileResponse restResult = callMyProfileGetRESTCall(methodName,
                                                                omasServerURL + urlTemplate,
                                                                userId);

        detectAndThrowInvalidParameterException(methodName, restResult);
        detectAndThrowNoProfileForUserException(methodName, restResult);
        detectAndThrowUserNotAuthorizedException(methodName, restResult);
        detectAndThrowPropertyServerException(methodName, restResult);

        return null;
    }


    /**
     * Create or update the profile for the requesting user.
     *
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
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
    public void updateMyProfile(String              userId,
                                String              employeeNumber,
                                String              fullName,
                                String              knownName,
                                String              jobTitle,
                                String              jobRoleDescription,
                                Map<String, Object> additionalProperties) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String   methodName = "updateMyProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/my-profile";

        final String   employeeNumberParameterName = "employeeNumber";
        final String   knownNameParameterName = "knownName";

        validateOMASServerURL(methodName);
        validateUserId(userId, methodName);
        validateName(employeeNumber, employeeNumberParameterName, methodName);
        validateName(knownName, knownNameParameterName, methodName);

        MyProfileRequestBody requestBody = new MyProfileRequestBody();
        requestBody.setEmployeeNumber(employeeNumber);
        requestBody.setFullName(fullName);
        requestBody.setKnownName(knownName);
        requestBody.setJobTitle(jobTitle);
        requestBody.setJobRoleDescription(jobRoleDescription);
        requestBody.setAdditionalProperties(additionalProperties);


        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       serverName,
                                                       userId);

        detectAndThrowInvalidParameterException(methodName, restResult);
        detectAndThrowUserNotAuthorizedException(methodName, restResult);
        detectAndThrowPropertyServerException(methodName, restResult);
    }



    /**
     * Return a list of assets that the specified user has added to their favorites list.
     *
     * @param userId     userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
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
    public List<Asset> getMyAssets(String    userId,
                                   String    assetCollectionGUID,
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
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws NotAnAssetException the guid is not recognized as an identifier for an asset.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void  addToMyAssets(String   userId,
                               String   assetCollectionGUID,
                               String   assetGUID) throws InvalidParameterException,
                                                          NoProfileForUserException,
                                                          NotAnAssetException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {

    }


    /**
     * Remove an asset from identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void  removeFromMyAssets(String   userId,
                                    String   assetCollectionGUID,
                                    String   assetGUID) throws InvalidParameterException,
                                                               NoProfileForUserException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {

    }


    /**
     * Throw an exception if a server URL has not been supplied on the constructor.
     *
     * @param methodName  name of the method making the call.
     *
     * @throws PropertyServerException the server URL is not set
     */
    private void validateOMASServerURL(String methodName) throws PropertyServerException
    {
        if (omasServerURL == null)
        {
            /*
             * It is not possible to retrieve a connection without knowledge of where the OMAS Server is located.
             */
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.SERVER_URL_NOT_SPECIFIED;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId      user name to validate
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the userId is null
     */
    private void validateUserId(String userId,
                                String methodName) throws InvalidParameterException
    {
        if (userId == null)
        {
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NULL_USER_ID;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                "userId");
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param guid           unique identifier to validate
     * @param guidParameter  name of the parameter that passed the guid.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    private void validateGUID(String guid,
                              String guidParameter,
                              String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NULL_GUID;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(guidParameter,
                                                                                     methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                guidParameter);
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param name           unique name to validate
     * @param nameParameter  name of the parameter that passed the name.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    private void validateName(String name,
                              String nameParameter,
                              String methodName) throws InvalidParameterException
    {
        if (name == null)
        {
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NULL_NAME;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(nameParameter,
                                                                                     methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                nameParameter);
        }
    }


    /**
     * Issue a GET REST call that returns a MyProfileManagement object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return MyProfileResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private MyProfileResponse callMyProfileGetRESTCall(String    methodName,
                                                       String    urlTemplate,
                                                       Object... params) throws PropertyServerException
    {
        return (MyProfileResponse)this.callGetRESTCall(methodName, MyProfileResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a Connection object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GUIDResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private GUIDResponse callGUIDGetRESTCall(String    methodName,
                                             String    urlTemplate,
                                             Object... params) throws PropertyServerException
    {
        return (GUIDResponse)this.callGetRESTCall(methodName, GUIDResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a AssetListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AssetListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private AssetListResponse callAssetListGetRESTCall(String    methodName,
                                                       String    urlTemplate,
                                                       Object... params) throws PropertyServerException
    {
        return (AssetListResponse)this.callGetRESTCall(methodName, AssetListResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private Object callGetRESTCall(String    methodName,
                                   Class     returnClass,
                                   String    urlTemplate,
                                   Object... params) throws PropertyServerException
    {
        try
        {
            RestTemplate restTemplate = new RestTemplate();

            return restTemplate.getForObject(urlTemplate, returnClass, params);
        }
        catch (Throwable error)
        {
            CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                     omasServerURL,
                                                                                                     error.getMessage());

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction(),
                                              error);
        }
    }


    /**
     * Issue a POST REST call that returns a guid object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return GUIDResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private GUIDResponse callGUIDPostRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object    requestBody,
                                              Object... params) throws PropertyServerException
    {
        return (GUIDResponse)this.callPostRESTCall(methodName,
                                                   GUIDResponse.class,
                                                   urlTemplate,
                                                   requestBody,
                                                   params);
    }


    /**
     * Issue a POST REST call that returns a VoidResponse object.  This is typically a create
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return VoidResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private VoidResponse callVoidPostRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object    requestBody,
                                              Object... params) throws PropertyServerException
    {
        return (VoidResponse)this.callPostRESTCall(methodName,
                                                   VoidResponse.class,
                                                   urlTemplate,
                                                   requestBody,
                                                   params);
    }


    /**
     * Issue a POST REST call that returns a VoidResponse object.  This is typically a create
     *
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return Object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private Object callPostRESTCall(String    methodName,
                                    Class     returnClass,
                                    String    urlTemplate,
                                    Object    requestBody,
                                    Object... params) throws PropertyServerException
    {
        try
        {
            RestTemplate  restTemplate = new RestTemplate();

            return restTemplate.postForObject(urlTemplate, requestBody, returnClass, params);
        }
        catch (Throwable error)
        {
            CommunityProfileErrorCode errorCode = CommunityProfileErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                     omasServerURL,
                                                                                                     error.getMessage());

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction(),
                                              error);
        }
    }


    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws InvalidParameterException encoded exception from the server
     */
    private void detectAndThrowInvalidParameterException(String                       methodName,
                                                         CommunityProfileOMASAPIResponse restResult) throws InvalidParameterException
    {
        final String   exceptionClassName = InvalidParameterException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String paramName = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  nameObject = exceptionProperties.get("parameterName");

                if (nameObject != null)
                {
                    paramName = (String)nameObject;
                }
            }
            throw new InvalidParameterException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                paramName);
        }
    }


    /**
     * Throw an PropertyServerException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws PropertyServerException encoded exception from the server
     */
    private void detectAndThrowPropertyServerException(String                       methodName,
                                                       CommunityProfileOMASAPIResponse restResult) throws PropertyServerException
    {
        final String   exceptionClassName = PropertyServerException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new PropertyServerException(restResult.getRelatedHTTPCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              restResult.getExceptionErrorMessage(),
                                              restResult.getExceptionSystemAction(),
                                              restResult.getExceptionUserAction());
        }
    }


    /**
     * Throw an NoProfileForUserException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called.
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws NoProfileForUserException encoded exception from the server
     */
    private void detectAndThrowNoProfileForUserException(String                       methodName,
                                                         CommunityProfileOMASAPIResponse restResult) throws NoProfileForUserException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String userId = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  userIdObject = exceptionProperties.get("userId");

                if (userIdObject != null)
                {
                    userId = (String)userIdObject;
                }
            }

            throw new NoProfileForUserException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                userId);
        }
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called.
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    private void detectAndThrowUserNotAuthorizedException(String                       methodName,
                                                          CommunityProfileOMASAPIResponse restResult) throws UserNotAuthorizedException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String userId = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  userIdObject = exceptionProperties.get("userId");

                if (userIdObject != null)
                {
                    userId = (String)userIdObject;
                }
            }

            throw new UserNotAuthorizedException(restResult.getRelatedHTTPCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 restResult.getExceptionErrorMessage(),
                                                 restResult.getExceptionSystemAction(),
                                                 restResult.getExceptionUserAction(),
                                                 userId);
        }
    }
}
