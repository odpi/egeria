/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;


import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.communityprofile.rest.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The PersonalProfilesRESTServices provides the server-side implementation of the CommunityProfile Open Metadata
 * Assess Service (OMAS).
 */
public class PersonalProfilesRESTServices
{
    static private CommunityProfileInstanceHandler instanceHandler = new CommunityProfileInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(PersonalProfilesRESTServices.class);

    /**
     * Default constructor
     */
    public PersonalProfilesRESTServices()
    {
    }


    /**
     * Create a personal profile for an individual who is to be appointed to a governance role but does not
     * have a profile in open metadata.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param requestBody properties about the individual.
     * @return Unique identifier for the personal profile or
     * InvalidParameterException the employee number or full name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GUIDResponse createPersonalProfile(String                     serverName,
                                              String                     userId,
                                              PersonalDetailsRequestBody requestBody)
    {
        final String        methodName = "createPersonalProfile";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();

        try
        {
            String              profileUserId = null;
            String              employeeNumber = null;
            String              fullName = null;
            String              knownName = null;
            String              jobTitle = null;
            String              jobRoleDescription = null;
            Map<String, Object> additionalProperties = null;

            if (requestBody != null)
            {
                profileUserId = requestBody.getUserId();
                employeeNumber = requestBody.getEmployeeNumber();
                fullName = requestBody.getFullName();
                knownName = requestBody.getKnownName();
                jobTitle = requestBody.getJobTitle();
                jobRoleDescription = requestBody.getJobRoleDescription();
                additionalProperties = requestBody.getAdditionalProperties();
            }

            PersonalProfilesHandler handler = new PersonalProfilesHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            response.setGUID(handler.createPersonalProfile(userId,
                                                           profileUserId,
                                                           employeeNumber,
                                                           fullName,
                                                           knownName,
                                                           jobTitle,
                                                           jobRoleDescription,
                                                           additionalProperties));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
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
     * Update properties for the personal properties.  Null values result in empty fields in the profile.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param requestBody properties about the individual.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid or
     * InvalidParameterException the full name is null or the employeeNumber does not match the profileGUID or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse updatePersonalProfile(String                     serverName,
                                              String                     userId,
                                              String                     profileGUID,
                                              PersonalDetailsRequestBody requestBody)
    {
        final String        methodName = "updatePersonalProfile";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            String              employeeNumber = null;
            String              fullName = null;
            String              knownName = null;
            String              jobTitle = null;
            String              jobRoleDescription = null;
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

            PersonalProfilesHandler handler = new PersonalProfilesHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            handler.updatePersonalProfile(userId,
                                          profileGUID,
                                          employeeNumber,
                                          fullName,
                                          knownName,
                                          jobTitle,
                                          jobRoleDescription,
                                          additionalProperties);
        }
        catch (UnrecognizedGUIDException error)
        {
            captureUnrecognizedGUIDException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
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
     * Delete the personal profile.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param requestBody personnel/serial/unique employee number of the individual.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid or
     * InvalidParameterException the employee number or full name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse   deletePersonalProfile(String                              serverName,
                                                String                              userId,
                                                String                              profileGUID,
                                                PersonalProfileValidatorRequestBody requestBody)
    {
        final String        methodName = "deletePersonalProfile";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            String              employeeNumber = null;

            if (requestBody != null)
            {
                employeeNumber = requestBody.getEmployeeNumber();
            }

            PersonalProfilesHandler handler = new PersonalProfilesHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            handler.deletePersonalProfile(userId,
                                          profileGUID,
                                          employeeNumber);
        }
        catch (UnrecognizedGUIDException error)
        {
            captureUnrecognizedGUIDException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
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
     * Retrieve a personal profile by guid.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @return personal profile object or
     * UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfileResponse getPersonalProfileByGUID(String        serverName,
                                                            String        userId,
                                                            String        profileGUID)
    {
        final String        methodName = "getPersonalProfileByGUID";

        log.debug("Calling method: " + methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();

        try
        {
            PersonalProfilesHandler handler = new PersonalProfilesHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            response.setPersonalProfile(handler.getPersonalProfileByGUID(userId, profileGUID));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (UnrecognizedGUIDException error)
        {
            captureUnrecognizedGUIDException(response, error);
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
     * Retrieve a personal profile by personnel/serial/unique employee number of the individual.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @return personal profile object or
     * InvalidParameterException the employee number or full name is null or
     * EmployeeNumberNotUniqueException more than one personal profile was found or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfileResponse getPersonalProfileByEmployeeNumber(String         serverName,
                                                                      String         userId,
                                                                      String         employeeNumber)
    {
        final String        methodName = "getPersonalProfileByEmployeeNumber";

        log.debug("Calling method: " + methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();

        try
        {
            PersonalProfilesHandler handler = new PersonalProfilesHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            response.setPersonalProfile(handler.getPersonalProfileByEmployeeNumber(userId, employeeNumber));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EmployeeNumberNotUniqueException error)
        {
            captureEmployeeNumberNotUniqueException(response, error);
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
     * Return a list of candidate personal profiles for an individual.  It matches on full name and known name.
     * The name may include wild card parameters.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param name name of individual.
     * @return list of personal profile objects or
     * InvalidParameterException the name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfileListResponse getPersonalProfilesByName(String        serverName,
                                                                 String        userId,
                                                                 String        name)
    {
        final String        methodName = "getPersonalProfilesByName";

        log.debug("Calling method: " + methodName);

        PersonalProfileListResponse response = new PersonalProfileListResponse();

        try
        {
            PersonalProfilesHandler handler = new PersonalProfilesHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            response.setPersonalProfiles(handler.getPersonalProfilesByName(userId, name));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
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
                                         String                                exceptionClassName)
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
                                         String                                exceptionClassName,
                                         Map<String, Object>                   exceptionProperties)
    {
        this.captureCheckedException(response, error, exceptionClassName);
        response.setExceptionProperties(exceptionProperties);
    }



    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureUnrecognizedGUIDException(CommunityProfileOMASAPIResponse     response,
                                                  UnrecognizedGUIDException            error)
    {
        String  guid = error.getGUID();
        String  expectedTypeName = error.getExpectedTypeName();

        if ((guid != null) || (expectedTypeName != null))
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("guid", guid);
            exceptionProperties.put("expectedTypeName", expectedTypeName);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }

        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureInvalidParameterException(CommunityProfileOMASAPIResponse response,
                                                  InvalidParameterException        error)
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
    private void captureEmployeeNumberNotUniqueException(CommunityProfileOMASAPIResponse response,
                                                         EmployeeNumberNotUniqueException error)
    {
        List<EntityDetail> duplicateProfiles = error.getDuplicateProfiles();

        if (duplicateProfiles != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("duplicateProfiles", duplicateProfiles);
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
                                                PropertyServerException              error)
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
                                                   UserNotAuthorizedException       error)
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
