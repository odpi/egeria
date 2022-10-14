/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server;


import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ContactMethodElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ITProfileElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ContactMethodRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ITProfileListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ITProfileRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ITProfileResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.UserIdentityListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.UserIdentityRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.UserIdentityResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ActorProfileHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ContactDetailsHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.UserIdentityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * The ITProfileRESTServices provides the server-side implementation of the IT Infrastructure Open Metadata
 * Assess Service (OMAS) capability for IT Profile management.  This interface provides support for creating IT profiles and
 * associated user roles in order to describe the structure of an organization.  It also links the profile to the Asset that is using the profile
 */
public class ITProfileRESTServices
{
    private static final ITInfrastructureInstanceHandler instanceHandler = new ITInfrastructureInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ITProfileRESTServices.class),
                                                                            instanceHandler.getServiceName());


    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public ITProfileRESTServices()
    {
    }


    /**
     * Create a definition of an actor profile.  This could be for the whole organization, a team, a person or a system.
     *
     * @param serverName called server
     * @param userId calling user
     * @param requestBody properties for a actor profile
     *
     * @return unique identifier of actor profile
     *
     *   InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public GUIDResponse createITProfile(String               serverName,
                                        String               userId,
                                        ITProfileRequestBody requestBody)
    {
        final String methodName = "createITProfile";
        final String profileGUIDParameterName = "profileGUID";
        final String userParameterName = "requestBody.getItUserId";
        final String itInfrastructureGUIDParameterName = "requestBody.getItInfrastructureGGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                ActorProfileHandler<ITProfileElement>    profileHandler      = instanceHandler.getITProfileHandler(userId, serverName, methodName);
                UserIdentityHandler<UserIdentityElement> userIdentityHandler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                
                /*
                 * Validate that the userId is unique
                 */
                EntityDetail userIdentity = userIdentityHandler.getEntityByUniqueQualifiedName(userId,
                                                                                               OpenMetadataAPIMapper.USER_IDENTITY_TYPE_GUID,
                                                                                               OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                                                                               requestBody.getItUserId(),
                                                                                               userParameterName,
                                                                                               false,
                                                                                               false,
                                                                                               new Date(),
                                                                                               methodName);
                
                String typeName = OpenMetadataAPIMapper.IT_PROFILE_TYPE_NAME;
                
                if (requestBody.getProperties().getTypeName() != null)
                {
                    typeName = requestBody.getProperties().getTypeName();
                }

                String profileGUID = profileHandler.createActorProfile(userId,
                                                                       requestBody.getExternalSourceGUID(),
                                                                       requestBody.getExternalSourceName(),
                                                                       requestBody.getProperties().getQualifiedName(),
                                                                       requestBody.getProperties().getKnownName(),
                                                                       requestBody.getProperties().getDescription(),
                                                                       requestBody.getProperties().getAdditionalProperties(),
                                                                       typeName,
                                                                       requestBody.getProperties().getExtendedProperties(),
                                                                       null,
                                                                       null,
                                                                       new Date(),
                                                                       methodName);

                if (userIdentity == null)
                {
                    userIdentityHandler.createUserIdentity(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           profileGUID,
                                                           profileGUIDParameterName,
                                                           "UserIdentity:" + requestBody.getItUserId(),
                                                           requestBody.getItUserId(),
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
                                                             requestBody.getExternalSourceGUID(),
                                                             requestBody.getExternalSourceName(),
                                                             profileGUID,
                                                             profileGUIDParameterName,
                                                             typeName,
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
                
                if (requestBody.getItInfrastructureGUID() != null)
                {
                    profileHandler.linkElementToElement(userId,
                                                        requestBody.getExternalSourceGUID(),
                                                        requestBody.getExternalSourceName(),
                                                        requestBody.getItInfrastructureGUID(),
                                                        itInfrastructureGUIDParameterName,
                                                        OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                        profileGUID,
                                                        profileGUIDParameterName,
                                                        typeName,
                                                        false,
                                                        false,
                                                        OpenMetadataAPIMapper.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP_TYPE_GUID,
                                                        OpenMetadataAPIMapper.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP_TYPE_NAME,
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
     * Update the definition of an actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param itProfileGUID unique identifier of actor profile
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *
     *   InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public VoidResponse updateITProfile(String               serverName,
                                        String               userId,
                                        String               itProfileGUID,
                                        boolean              isMergeUpdate,
                                        ITProfileRequestBody requestBody)
    {
        final String methodName        = "updateITProfile";
        final String guidParameterName = "itProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog      = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                ActorProfileHandler<ITProfileElement> handler = instanceHandler.getITProfileHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.updateActorProfile(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           itProfileGUID,
                                           guidParameterName,
                                           requestBody.getProperties().getQualifiedName(),
                                           requestBody.getProperties().getKnownName(),
                                           requestBody.getProperties().getDescription(),
                                           requestBody.getProperties().getAdditionalProperties(),
                                           requestBody.getProperties().getTypeName(),
                                           requestBody.getProperties().getExtendedProperties(),
                                           isMergeUpdate,
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
     * Remove the definition of an actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param itProfileGUID unique identifier of actor profile
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException guid or userId is null; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public VoidResponse deleteITProfile(String                    serverName,
                                        String                    userId,
                                        String                    itProfileGUID,
                                        MetadataSourceRequestBody requestBody)
    {
        final String methodName        = "deleteITProfile";
        final String guidParameterName = "itProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog      = null;

        try
        {
            if (requestBody != null)
            {
                ActorProfileHandler<ITProfileElement> handler = instanceHandler.getITProfileHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.removeActorProfile(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           itProfileGUID,
                                           guidParameterName,
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
     * Add a new contact method to the profile.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param itProfileGUID identifier of the profile to update.
     * @param requestBody properties of contact method.
     *
     * @return unique identifier (guid) for the new contact method.
     *
     *   InvalidParameterException the userId is null or invalid.  Another property is invalid.
     *   PropertyServerException there is a problem retrieving information from the property server(s).
     *   UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addContactMethod(String                   serverName,
                                         String                   userId,
                                         String                   itProfileGUID,
                                         ContactMethodRequestBody requestBody)
    {
        final String methodName        = "addContactMethod";
        final String guidParameterName = "itProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                ContactDetailsHandler<ContactMethodElement> handler = instanceHandler.getContactDetailsHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                int contactMethodTypeOrdinal = 0;

                if (requestBody.getProperties().getContactMethodType() != null)
                {
                    contactMethodTypeOrdinal = requestBody.getProperties().getContactMethodType().getOpenTypeOrdinal();
                }

                handler.createContactMethod(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            itProfileGUID,
                                            guidParameterName,
                                            requestBody.getProperties().getName(),
                                            requestBody.getProperties().getContactType(),
                                            contactMethodTypeOrdinal,
                                            requestBody.getProperties().getContactMethodService(),
                                            requestBody.getProperties().getContactMethodValue(),
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
     * Remove an obsolete contact method from the profile.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param contactMethodGUID unique identifier (guid) for the obsolete contact method.
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException the userId is null or invalid.  Another property is invalid.
     *   PropertyServerException there is a problem retrieving information from the property server(s).
     *   UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteContactMethod(String                    serverName,
                                            String                    userId,
                                            String                    contactMethodGUID,
                                            MetadataSourceRequestBody requestBody)
    {
        final String methodName                  = "deleteContactMethod";
        final String guidParameterName           = "contactMethodGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog      = null;

        try
        {
            ContactDetailsHandler<ContactMethodElement> handler = instanceHandler.getContactDetailsHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               contactMethodGUID,
                                               guidParameterName,
                                               OpenMetadataAPIMapper.CONTACT_DETAILS_TYPE_GUID,
                                               OpenMetadataAPIMapper.CONTACT_DETAILS_TYPE_NAME,
                                               null,
                                               null,
                                               false,
                                               false,
                                               null,
                                               methodName);
            }
            else
            {
                handler.deleteBeanInRepository(userId,
                                               null,
                                               null,
                                               contactMethodGUID,
                                               guidParameterName,
                                               OpenMetadataAPIMapper.CONTACT_DETAILS_TYPE_GUID,
                                               OpenMetadataAPIMapper.CONTACT_DETAILS_TYPE_NAME,
                                               null,
                                               null,
                                               false,
                                               false,
                                               null,
                                               methodName);
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
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param itProfileGUID unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     *   InvalidParameterException itProfileGUID or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ITProfileResponse getITProfileByGUID(String serverName,
                                                String userId,
                                                String itProfileGUID)
    {
        final String methodName        = "getITProfileByGUID";
        final String guidParameterName = "itProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ITProfileResponse response = new ITProfileResponse();
        AuditLog             auditLog = null;

        try
        {
            ActorProfileHandler<ITProfileElement> handler = instanceHandler.getITProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getActorProfileByGUID(userId,
                                                              itProfileGUID,
                                                              guidParameterName,
                                                              OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME,
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
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileUserId unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     *   InvalidParameterException actorProfileUserId or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ITProfileResponse getITProfileByUserId(String serverName,
                                                  String userId,
                                                  String actorProfileUserId)
    {
        final String methodName        = "getITProfileByGUID";
        final String nameParameterName = "actorProfileUserId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ITProfileResponse response = new ITProfileResponse();
        AuditLog             auditLog = null;

        try
        {
            ActorProfileHandler<ITProfileElement> handler = instanceHandler.getITProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getActorProfileForUser(userId,
                                                               actorProfileUserId,
                                                               nameParameterName,
                                                               OpenMetadataAPIMapper.IT_PROFILE_TYPE_NAME,
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
     * Return information about a named actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody unique name for the actor profile
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ITProfileListResponse getITProfileByName(String          serverName,
                                                    String          userId,
                                                    int             startFrom,
                                                    int             pageSize,
                                                    NameRequestBody requestBody)
    {
        final String methodName         = "getITProfileByName";
        final String nameParameterName  = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ITProfileListResponse response = new ITProfileListResponse();
        AuditLog                 auditLog = null;

        try
        {
            ActorProfileHandler<ITProfileElement> handler = instanceHandler.getITProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getActorProfilesByName(userId,
                                                                requestBody.getName(),
                                                                nameParameterName,
                                                                OpenMetadataAPIMapper.IT_PROFILE_TYPE_GUID,
                                                                OpenMetadataAPIMapper.IT_PROFILE_TYPE_NAME,
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


    /**
     * Retrieve the list of matching profiles for the search string.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody RegEx string to search for
     *
     * @return list of matching actor profiles
     *
     *   InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *   PropertyServerException the server is not available.
     *   UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public ITProfileListResponse findITProfile(String                  serverName,
                                               String                  userId,
                                               int                     startFrom,
                                               int                     pageSize,
                                               SearchStringRequestBody requestBody)
    {
        final String methodName                 = "findITProfile";
        final String searchStringParameterName  = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ITProfileListResponse response = new ITProfileListResponse();
        AuditLog                 auditLog = null;

        try
        {
            ActorProfileHandler<ITProfileElement> handler = instanceHandler.getITProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.findActorProfiles(userId,
                                                           requestBody.getSearchString(),
                                                           searchStringParameterName,
                                                           OpenMetadataAPIMapper.IT_PROFILE_TYPE_GUID,
                                                           OpenMetadataAPIMapper.IT_PROFILE_TYPE_NAME,
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


    /**
     * Create a UserIdentity.  This is not connected to a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user
     * @param requestBody userId for the new userIdentity
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createUserIdentity(String                  serverName,
                                           String                  userId,
                                           UserIdentityRequestBody requestBody)
    {
        final String methodName = "createUserIdentity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                String userIdentityGUID = handler.createUserIdentity(userId,
                                                                     requestBody.getExternalSourceGUID(),
                                                                     requestBody.getExternalSourceName(),
                                                                     null,
                                                                     null,
                                                                     requestBody.getProperties().getQualifiedName(),
                                                                     requestBody.getProperties().getUserId(),
                                                                     requestBody.getProperties().getDistinguishedName(),
                                                                     requestBody.getProperties().getAdditionalProperties(),
                                                                     requestBody.getProperties().getTypeName(),
                                                                     requestBody.getProperties().getExtendedProperties(),
                                                                     false,
                                                                     false,
                                                                     new Date(),
                                                                     methodName);

                response.setGUID(userIdentityGUID);
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
     * Update a UserIdentity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param requestBody updated properties for the new userIdentity
     *
     * @return void or
     *  InvalidParameterException one of the parameters is invalid.
     *  PropertyServerException  there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateUserIdentity(String                  serverName,
                                           String                  userId,
                                           String                  userIdentityGUID,
                                           boolean                 isMergeUpdate,
                                           UserIdentityRequestBody requestBody)
    {
        final String methodName        = "updateUserIdentity";
        final String guidParameterName = "userIdentityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.updateUserIdentity(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           userIdentityGUID,
                                           guidParameterName,
                                           requestBody.getProperties().getQualifiedName(),
                                           requestBody.getProperties().getUserId(),
                                           requestBody.getProperties().getDistinguishedName(),
                                           requestBody.getProperties().getAdditionalProperties(),
                                           requestBody.getProperties().getTypeName(),
                                           requestBody.getProperties().getExtendedProperties(),
                                           isMergeUpdate,
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
     * Remove a user identity object.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteUserIdentity(String                    serverName,
                                           String                    userId,
                                           String                    userIdentityGUID,
                                           MetadataSourceRequestBody requestBody)
    {
        final String methodName        = "deleteUserIdentity";
        final String guidParameterName = "userIdentityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.deleteUserIdentity(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           userIdentityGUID,
                                           guidParameterName,
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
     * Link a user identity to a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID the profile to add the identity to.
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse  addIdentityToProfile(String                    serverName,
                                              String                    userId,
                                              String                    userIdentityGUID,
                                              String                    profileGUID,
                                              MetadataSourceRequestBody requestBody)
    {
        final String methodName                    = "addIdentityToProfile";
        final String userIdentityGUIDParameterName = "userIdentityGUID";
        final String profileGUIDParameterName      = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.addIdentityToProfile(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             userIdentityGUID,
                                             userIdentityGUIDParameterName,
                                             profileGUID,
                                             profileGUIDParameterName,
                                             null,
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
     * Remove a user identity object.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID profile to remove it from.
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeIdentityFromProfile(String                    serverName,
                                                  String                    userId,
                                                  String                    userIdentityGUID,
                                                  String                    profileGUID,
                                                  MetadataSourceRequestBody requestBody)
    {
        final String methodName                    = "removeIdentityFromProfile";
        final String userIdentityGUIDParameterName = "userIdentityGUID";
        final String profileGUIDParameterName      = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.removeIdentifyFromProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  userIdentityGUID,
                                                  userIdentityGUIDParameterName,
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
     * Retrieve the list of user identity metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public UserIdentityListResponse findUserIdentities(String                  serverName,
                                                       String                  userId,
                                                       int                     startFrom,
                                                       int                     pageSize,
                                                       SearchStringRequestBody requestBody)
    {
        final String methodName                 = "findUserIdentities";
        final String searchStringParameterName  = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        UserIdentityListResponse response = new UserIdentityListResponse();
        AuditLog                 auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                List<UserIdentityElement> elements = handler.findBeans(userId,
                                                                       requestBody.getSearchString(),
                                                                       searchStringParameterName,
                                                                       OpenMetadataAPIMapper.USER_IDENTITY_TYPE_GUID,
                                                                       OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                                                       null,
                                                                       startFrom,
                                                                       pageSize,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName);
                response.setElements(elements);
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
     * Retrieve the list of user identity metadata elements with a matching qualified name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public UserIdentityListResponse getUserIdentitiesByName(String          serverName,
                                                            String          userId,
                                                            int             startFrom,
                                                            int             pageSize,
                                                            NameRequestBody requestBody)
    {
        final String methodName         = "getUserIdentitiesByName";
        final String nameParameterName  = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        UserIdentityListResponse response = new UserIdentityListResponse();
        AuditLog                 auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);


                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                List<UserIdentityElement> elements = handler.getUserIdentitiesByName(userId,
                                                                                     requestBody.getName(),
                                                                                     nameParameterName,
                                                                                     startFrom,
                                                                                     pageSize,
                                                                                     false,
                                                                                     false,
                                                                                     new Date(),
                                                                                     methodName);
                response.setElements(elements);
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
     * Retrieve the userIdentity metadata element with the supplied unique identifier.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param userIdentityGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public UserIdentityResponse getUserIdentityByGUID(String serverName,
                                                      String userId,
                                                      String userIdentityGUID)
    {
        final String methodName                    = "getUserIdentityByGUID";
        final String userIdentityGUIDParameterName = "userIdentityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        UserIdentityResponse response = new UserIdentityResponse();
        AuditLog             auditLog = null;

        try
        {
            UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            UserIdentityElement element = handler.getUserIdentityByGUID(userId,
                                                                        userIdentityGUID,
                                                                        userIdentityGUIDParameterName,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName);
            response.setElement(element);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

}
