/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.handlers.PersonalProfileHandler;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileListResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileValidatorRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * The PersonalProfileRESTServices provides the server-side implementation of the Community Profile Open Metadata
 * Assess Service (OMAS) capability for managing personal profiles.  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class PersonalProfileRESTServices
{
    static private CommunityProfileInstanceHandler instanceHandler = new CommunityProfileInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(PersonalProfileRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public PersonalProfileRESTServices()
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
                                              PersonalProfileRequestBody requestBody)
    {
        final String        methodName = "createPersonalProfile";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String              profileUserId = null;
            String              qualifiedName = null;
            String              fullName = null;
            String              knownName = null;
            String              jobTitle = null;
            String              jobRoleDescription = null;
            Map<String, String> additionalProperties = null;

            if (requestBody != null)
            {
                profileUserId = requestBody.getProfileUserId();
                qualifiedName = requestBody.getQualifiedName();
                fullName = requestBody.getFullName();
                knownName = requestBody.getKnownName();
                jobTitle = requestBody.getJobTitle();
                jobRoleDescription = requestBody.getJobRoleDescription();
                additionalProperties = requestBody.getAdditionalProperties();
            }

            PersonalProfileHandler handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUID(handler.createPersonalProfile(userId,
                                                           profileUserId,
                                                           qualifiedName,
                                                           fullName,
                                                           knownName,
                                                           jobTitle,
                                                           jobRoleDescription,
                                                           additionalProperties,
                                                           methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * NoProfileForUserException there is no profile for the user or
     * InvalidParameterException the full name is null or the qualifiedName does not match the profileGUID or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse updatePersonalProfile(String                     serverName,
                                              String                     userId,
                                              String                     profileGUID,
                                              PersonalProfileRequestBody requestBody)
    {
        final String        methodName = "updatePersonalProfile";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog      = null;

        try
        {
            String              qualifiedName = null;
            String              fullName = null;
            String              knownName = null;
            String              jobTitle = null;
            String              jobRoleDescription = null;
            Map<String, Object> extendedProperties = null;
            Map<String, String> additionalProperties = null;

            if (requestBody != null)
            {
                qualifiedName = requestBody.getQualifiedName();
                fullName = requestBody.getFullName();
                knownName = requestBody.getKnownName();
                jobTitle = requestBody.getJobTitle();
                jobRoleDescription = requestBody.getJobRoleDescription();
                extendedProperties = requestBody.getProfileProperties();
                additionalProperties = requestBody.getAdditionalProperties();
            }

            PersonalProfileHandler handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.updatePersonalProfile(userId,
                                          profileGUID,
                                          qualifiedName,
                                          fullName,
                                          knownName,
                                          jobTitle,
                                          jobRoleDescription,
                                          extendedProperties,
                                          additionalProperties,
                                          methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
        AuditLog     auditLog = null;

        try
        {
            String              employeeNumber = null;

            if (requestBody != null)
            {
                employeeNumber = requestBody.getQualifiedName();
            }

            PersonalProfileHandler handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.deletePersonalProfile(userId,
                                          profileGUID,
                                          employeeNumber,
                                          methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the profile for this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of the user making the request.
     * @param profileUserId userId for person that profile belongs to
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public PersonalProfileResponse getPersonalProfileForUser(String serverName,
                                                             String userId,
                                                             String profileUserId)
    {
        final String   methodName = "getPersonalProfileForUser";

        log.debug("Calling method: " + methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();
        AuditLog                auditLog = null;

        try
        {
            PersonalProfileHandler handler = instanceHandler.getPersonalProfileHandler(serverName, userId, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setPersonalProfile(handler.getPersonalProfile(userId, profileUserId, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
        AuditLog                auditLog = null;

        try
        {
            PersonalProfileHandler handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setPersonalProfile(handler.getPersonalProfileByGUID(userId, profileGUID, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
    public PersonalProfileResponse getPersonalProfileByQualifiedName(String         serverName,
                                                                     String         userId,
                                                                     String         employeeNumber)
    {
        final String        methodName = "getPersonalProfileByQualifiedName";

        log.debug("Calling method: " + methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();
        AuditLog                auditLog = null;

        try
        {
            PersonalProfileHandler handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setPersonalProfile(handler.getPersonalProfileByQualifiedName(userId, employeeNumber, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     *
     * @return list of personal profile objects or
     * InvalidParameterException the name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfileListResponse getPersonalProfilesByName(String        serverName,
                                                                 String        userId,
                                                                 String        name,
                                                                 int           startFrom,
                                                                 int           pageSize)
    {
        final String        methodName = "getPersonalProfilesByName";

        log.debug("Calling method: " + methodName);

        PersonalProfileListResponse response = new PersonalProfileListResponse();
        AuditLog                    auditLog = null;

        try
        {
            PersonalProfileHandler handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setPersonalProfiles(handler.getPersonalProfilesByName(userId, name, startFrom, pageSize, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }
}
