/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.PersonalProfileUniverse;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileListResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileValidatorRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ActorProfileHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.UserIdentityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * The PersonalProfileRESTServices provides the server-side implementation of the Community Profile Open Metadata
 * Assess Service (OMAS) capability for managing personal profiles.  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class PersonalProfileRESTServices
{
    static private final CommunityProfileInstanceHandler instanceHandler = new CommunityProfileInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(PersonalProfileRESTServices.class),
                                                                            instanceHandler.getServiceName());


    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

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
        final String methodName = "createPersonalProfile";
        final String profileGUIDParameterName = "profileGUID";
        final String userParameterName = "requestBody.getProfileUserId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                ActorProfileHandler<PersonalProfileUniverse> profileHandler      = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);
                UserIdentityHandler<UserIdentityElement>     userIdentityHandler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                /*
                 * Validate that the userId is unique
                 */
                EntityDetail userIdentity = userIdentityHandler.getEntityByUniqueQualifiedName(userId,
                                                                                               OpenMetadataAPIMapper.USER_IDENTITY_TYPE_GUID,
                                                                                               OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                                                                               requestBody.getProfileUserId(),
                                                                                               userParameterName,
                                                                                               false,
                                                                                               false,
                                                                                               new Date(),
                                                                                               methodName);

                Map<String, Object> extendedProperties = new HashMap<>();

                extendedProperties.put(OpenMetadataAPIMapper.FULL_NAME_PROPERTY_NAME, requestBody.getFullName());
                extendedProperties.put(OpenMetadataAPIMapper.JOB_TITLE_PROPERTY_NAME, requestBody.getJobTitle());
                String profileGUID = profileHandler.createActorProfile(userId,
                                                                   requestBody.getOriginatingSystemGUID(),
                                                                   requestBody.getOriginatingSystemName(),
                                                                   requestBody.getQualifiedName(),
                                                                   requestBody.getKnownName(),
                                                                   requestBody.getJobRoleDescription(),
                                                                   requestBody.getAdditionalProperties(),
                                                                   OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                                                   extendedProperties,
                                                                   null,
                                                                   null,
                                                                   new Date(),
                                                                   methodName);

                if (userIdentity == null)
                {
                    userIdentityHandler.createUserIdentity(userId,
                                                           requestBody.getOriginatingSystemGUID(),
                                                           requestBody.getOriginatingSystemName(),
                                                           profileGUID,
                                                           profileGUIDParameterName,
                                                           requestBody.getQualifiedName(),
                                                           requestBody.getProfileUserId(),
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName);
                }
                else
                {
                    userIdentityHandler.linkElementToElement(userId,
                                                             requestBody.getOriginatingSystemGUID(),
                                                             requestBody.getOriginatingSystemName(),
                                                             profileGUID,
                                                             profileGUIDParameterName,
                                                             OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME,
                                                             userIdentity.getGUID(),
                                                             userParameterName,
                                                             OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                                             false,
                                                             false,
                                                             OpenMetadataAPIMapper.PROFILE_IDENTITY_RELATIONSHIP_TYPE_GUID,
                                                             OpenMetadataAPIMapper.PROFILE_IDENTITY_RELATIONSHIP_TYPE_NAME,
                                                             (InstanceProperties) null,
                                                             null,
                                                             null,
                                                             new Date(),
                                                             methodName);
                }

                response.setGUID(profileGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
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
        final String methodName = "updatePersonalProfile";
        final String profileGUIDParameterName = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog      = null;

        try
        {
            if (requestBody != null)
            {
                Map<String, Object> extendedProperties = new HashMap<>();

                extendedProperties.put(OpenMetadataAPIMapper.FULL_NAME_PROPERTY_NAME, requestBody.getFullName());
                extendedProperties.put(OpenMetadataAPIMapper.JOB_TITLE_PROPERTY_NAME, requestBody.getJobTitle());

                ActorProfileHandler<PersonalProfileUniverse> handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.updateActorProfile(userId,
                                           requestBody.getOriginatingSystemGUID(),
                                           requestBody.getOriginatingSystemName(),
                                           profileGUID,
                                           profileGUIDParameterName,
                                           requestBody.getQualifiedName(),
                                           requestBody.getKnownName(),
                                           requestBody.getJobRoleDescription(),
                                           requestBody.getAdditionalProperties(),
                                           OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                           extendedProperties,
                                           false,
                                           null,
                                           null,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
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
        final String methodName = "deletePersonalProfile";
        final String profileGUIDParameterName = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                ActorProfileHandler<PersonalProfileUniverse> handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.removeActorProfile(userId,
                                           requestBody.getOriginatingSystemGUID(),
                                           requestBody.getOriginatingSystemName(),
                                           profileGUID,
                                           profileGUIDParameterName,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
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
        final String methodName = "getPersonalProfileForUser";
        final String userParameterName = "profileUserId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();
        AuditLog                auditLog = null;

        try
        {
            ActorProfileHandler<PersonalProfileUniverse> handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setPersonalProfile(handler.getActorProfileForUser(userId,
                                                                       profileUserId,
                                                                       userParameterName,
                                                                       OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
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
        final String methodName = "getPersonalProfileByGUID";
        final String profileGUIDParameterName = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();
        AuditLog                auditLog = null;

        try
        {
            ActorProfileHandler<PersonalProfileUniverse> handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setPersonalProfile(handler.getActorProfileByGUID(userId,
                                                                      profileGUID,
                                                                      profileGUIDParameterName,
                                                                      OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve a personal profile by personnel/serial/unique employee number of the individual.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param requestBody name information
     *
     * @return personal profile object or
     * InvalidParameterException the employee number or full name is null or
     * EmployeeNumberNotUniqueException more than one personal profile was found or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfileResponse getPersonalProfileByQualifiedName(String          serverName,
                                                                     String          userId,
                                                                     NameRequestBody requestBody)
    {
        final String methodName = "getPersonalProfilesByName";
        final String nameParameterName = "getPersonalProfilesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();
        AuditLog                auditLog = null;

        try
        {
            ActorProfileHandler<PersonalProfileUniverse> handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setPersonalProfile(handler.getActorProfileByUniqueName(userId,
                                                                            requestBody.getName(),
                                                                            nameParameterName,
                                                                            OpenMetadataAPIMapper.PERSON_TYPE_GUID,
                                                                            OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                                                            false,
                                                                            false,
                                                                            new Date(),
                                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return a list of candidate personal profiles for an individual.  It matches on full name and known name.
     * The name may include wild card parameters.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param requestBody name information
     *
     * @return list of personal profile objects or
     * InvalidParameterException the name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfileListResponse getPersonalProfilesByName(String          serverName,
                                                                 String          userId,
                                                                 int             startFrom,
                                                                 int             pageSize,
                                                                 NameRequestBody requestBody)
    {
        final String methodName = "getPersonalProfilesByName";
        final String nameParameterName = "getPersonalProfilesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonalProfileListResponse response = new PersonalProfileListResponse();
        AuditLog                    auditLog = null;

        try
        {
            ActorProfileHandler<PersonalProfileUniverse> handler = instanceHandler.getPersonalProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setPersonalProfiles(handler.getActorProfilesByName(userId,
                                                                        requestBody.getName(),
                                                                        nameParameterName,
                                                                        OpenMetadataAPIMapper.PERSON_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.PERSON_TYPE_NAME,
                                                                        startFrom,
                                                                        pageSize,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
