/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;


import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.communityprofile.rest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * The CommunityProfileRESTServices provides the server-side implementation of the CommunityProfile Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class MyProfileRESTServices
{
    private static CommunityProfileInstanceHandler   instanceHandler     = new CommunityProfileInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(MyProfileRESTServices.class);


    /**
     * Default constructor
     */
    public MyProfileRESTServices()
    {
    }



    /**
     * Return the profile for this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of the user making the request.
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * NoProfileForUserException the user does not have a profile or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public MyProfileResponse getMyProfile(String serverName,
                                          String userId)
    {
        final String   methodName = "getMyProfile";

        log.debug("Calling method: " + methodName);

        MyProfileResponse  response = new MyProfileResponse();

        try
        {
            MyProfileHandler   handler = new MyProfileHandler(instanceHandler.getAccessServiceName(),
                                                              instanceHandler.getRepositoryConnector(serverName));

            response.setPersonalProfile(handler.getMyProfile(userId));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (NoProfileForUserException error)
        {
            captureNoProfileForUserException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Create or update the profile for the requesting user.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody properties for the new profile.
     * @return void response or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateMyProfile(String               serverName,
                                        String               userId,
                                        MyProfileRequestBody requestBody)
    {
        final String   methodName = "updateMyProfile";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            String              employeeNumber       = null;
            String              fullName             = null;
            String              knownName            = null;
            String              jobTitle             = null;
            String              jobRoleDescription   = null;
            Map<String, Object> additionalProperties = null;

            if (requestBody != null)
            {
                employeeNumber = requestBody.getEmployeeNumber();
                fullName = requestBody.getFullName();
                knownName = requestBody.getKnownName();
                jobTitle = requestBody.getJobTitle();
                jobRoleDescription = requestBody.getJobRoleDescription();
                additionalProperties = requestBody.getAdditionalProperties();
            }

            MyProfileHandler   handler = new MyProfileHandler(instanceHandler.getAccessServiceName(),
                                                              instanceHandler.getRepositoryConnector(serverName));

            handler.updateMyProfile(userId, employeeNumber, fullName, knownName, jobTitle, jobRoleDescription, additionalProperties);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return a list of assets that the specified user has added to their favorites list.
     *
     * @param serverName name of the server instances for this request
     * @param userId     userId of user making request.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of asset details or
     * InvalidParameterException one of the parameters is invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetListResponse getMyAssets(String    serverName,
                                         String    userId,
                                         int       startFrom,
                                         int       pageSize)
    {
        // todo
        return null;
    }


    /**
     * Add an asset to the identified user's list of favorite assets.
     *
     * @param serverName name of the server instances for this request
     * @param userId          userId of user making request.
     * @param assetGUID       unique identifier of the asset.
     * @param nullRequestBody null request body
     *
     * @return void response or
     * InvalidParameterException one of the parameters is invalid or
     * PropertyServerException there is a problem updating information in the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse  addToMyAssets(String           serverName,
                                       String           userId,
                                       String           assetGUID,
                                       NullRequestBody nullRequestBody)
    {
        // todo
        return null;
    }


    /**
     * Remove an asset from identified user's list of favorite assets.
     *
     * @param serverName name of the server instances for this request
     * @param userId          userId of user making request.
     * @param assetGUID       unique identifier of the asset.
     * @param nullRequestBody null request body
     *
     * @return void response or
     * InvalidParameterException one of the parameters is invalid or
     * PropertyServerException there is a problem updating information in the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse  removeFromMyAssets(String           serverName,
                                            String           userId,
                                            String           assetGUID,
                                            NullRequestBody  nullRequestBody)
    {
        // todo
        return null;

    }

    /* ==========================
     * Support methods
     * ==========================
     */


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     */
    private void captureCheckedException(CommunityProfileOMASAPIResponse response,
                                         CommunityProfileCheckedExceptionBase error,
                                         String                               exceptionClassName)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     * @param exceptionProperties map of properties stored in the exception to help with diagnostics
     */
    private void captureCheckedException(CommunityProfileOMASAPIResponse      response,
                                         CommunityProfileCheckedExceptionBase error,
                                         String                            exceptionClassName,
                                         Map<String, Object>               exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionProperties(exceptionProperties);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureInvalidParameterException(CommunityProfileOMASAPIResponse response,
                                                  InvalidParameterException    error)
    {
        String  parameterName = error.getParameterName();

        if (parameterName != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("parameterName", parameterName);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void capturePropertyServerException(CommunityProfileOMASAPIResponse     response,
                                                PropertyServerException          error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureUserNotAuthorizedException(CommunityProfileOMASAPIResponse response,
                                                   UserNotAuthorizedException   error)
    {
        String  userId = error.getUserId();

        if (userId != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("userId", userId);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureNoProfileForUserException(CommunityProfileOMASAPIResponse response,
                                                  NoProfileForUserException    error)
    {
        String  userId = error.getUserId();

        if (userId != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("userId", userId);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }
}
