/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ProfileIdentityProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentityProperties;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.UserIdentityListResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.UserIdentityResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.UserIdentityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * UserIdentityResource provides the APIs for maintaining user identities and their relationships with profiles.
 */
public class UserIdentityRESTServices
{
    private static final CommunityProfileInstanceHandler instanceHandler = new CommunityProfileInstanceHandler();

    private static final  RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(UserIdentityRESTServices.class),
                                                                             instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public UserIdentityRESTServices()
    {
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
    public GUIDResponse createUserIdentity(String                   serverName,
                                           String                   userId,
                                           ReferenceableRequestBody requestBody)
    {
        final String methodName = "createUserIdentity";
        final String profileGUIDParameterName = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof UserIdentityProperties)
                {
                    UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                    UserIdentityProperties userIdentityProperties = (UserIdentityProperties) requestBody.getProperties();

                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    String userIdentityGUID = handler.createUserIdentity(userId,
                                                                         requestBody.getExternalSourceGUID(),
                                                                         requestBody.getExternalSourceName(),
                                                                         requestBody.getParentGUID(),
                                                                         profileGUIDParameterName,
                                                                         userIdentityProperties.getQualifiedName(),
                                                                         userIdentityProperties.getUserId(),
                                                                         userIdentityProperties.getDistinguishedName(),
                                                                         userIdentityProperties.getAdditionalProperties(),
                                                                         userIdentityProperties.getTypeName(),
                                                                         userIdentityProperties.getExtendedProperties(),
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName);

                    response.setGUID(userIdentityGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(UserIdentityProperties.class.getName(), methodName);
                }
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
    public VoidResponse updateUserIdentity(String                   serverName,
                                           String                   userId,
                                           String                   userIdentityGUID,
                                           boolean                  isMergeUpdate,
                                           ReferenceableRequestBody requestBody)
    {
        final String methodName        = "updateUserIdentity";
        final String guidParameterName = "userIdentityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof UserIdentityProperties)
                {
                    UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                    UserIdentityProperties userIdentityProperties = (UserIdentityProperties) requestBody.getProperties();

                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    handler.updateUserIdentity(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               userIdentityGUID,
                                               guidParameterName,
                                               userIdentityProperties.getQualifiedName(),
                                               userIdentityProperties.getUserId(),
                                               userIdentityProperties.getDistinguishedName(),
                                               userIdentityProperties.getAdditionalProperties(),
                                               userIdentityProperties.getTypeName(),
                                               userIdentityProperties.getExtendedProperties(),
                                               isMergeUpdate,
                                               userIdentityProperties.getEffectiveFrom(),
                                               userIdentityProperties.getEffectiveTo(),
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
                                           ExternalSourceRequestBody requestBody)
    {
        final String methodName        = "deleteUserIdentity";
        final String guidParameterName = "userIdentityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
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
                handler.deleteUserIdentity(userId,
                                           null,
                                           null,
                                           userIdentityGUID,
                                           guidParameterName,
                                           false,
                                           false,
                                           new Date(),
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
    public VoidResponse  addIdentityToProfile(String                  serverName,
                                              String                  userId,
                                              String                  userIdentityGUID,
                                              String                  profileGUID,
                                              RelationshipRequestBody requestBody)
    {
        final String methodName                    = "addIdentityToProfile";
        final String userIdentityGUIDParameterName = "userIdentityGUID";
        final String profileGUIDParameterName      = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProfileIdentityProperties)
                {
                    ProfileIdentityProperties properties = (ProfileIdentityProperties)requestBody.getProperties();
                    handler.addIdentityToProfile(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 userIdentityGUID,
                                                 userIdentityGUIDParameterName,
                                                 profileGUID,
                                                 profileGUIDParameterName,
                                                 properties.getRoleTypeName(),
                                                 properties.getRoleGUID(),
                                                 properties.getDescription(),
                                                 requestBody.getProperties().getEffectiveFrom(),
                                                 requestBody.getProperties().getEffectiveTo(),
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
                }
                else if (requestBody.getProperties() == null)
                {
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
                    restExceptionHandler.handleInvalidPropertiesObject(ProfileIdentityProperties.class.getName(), methodName);
                }
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
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse  updateIdentityProfile(String                  serverName,
                                               String                  userId,
                                               String                  userIdentityGUID,
                                               String                  profileGUID,
                                               boolean                 isMergeUpdate,
                                               RelationshipRequestBody requestBody)
    {
        final String methodName                    = "updateIdentityProfile";
        final String userIdentityGUIDParameterName = "userIdentityGUID";
        final String profileGUIDParameterName      = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProfileIdentityProperties)
                {
                    ProfileIdentityProperties properties = (ProfileIdentityProperties)requestBody.getProperties();
                    handler.updateIdentityProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  profileGUID,
                                                  profileGUIDParameterName,
                                                  userIdentityGUID,
                                                  userIdentityGUIDParameterName,
                                                  properties.getRoleTypeName(),
                                                  properties.getRoleGUID(),
                                                  properties.getDescription(),
                                                  requestBody.getProperties().getEffectiveFrom(),
                                                  requestBody.getProperties().getEffectiveTo(),
                                                  isMergeUpdate,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateIdentityProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  profileGUID,
                                                  profileGUIDParameterName,
                                                  userIdentityGUID,
                                                  userIdentityGUIDParameterName,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  isMergeUpdate,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ProfileIdentityProperties.class.getName(), methodName);
                }
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
                                                  ExternalSourceRequestBody requestBody)
    {
        final String methodName                    = "removeIdentityFromProfile";
        final String userIdentityGUIDParameterName = "userIdentityGUID";
        final String profileGUIDParameterName      = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
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
                handler.removeIdentifyFromProfile(userId,
                                                  null,
                                                  null,
                                                  userIdentityGUID,
                                                  userIdentityGUIDParameterName,
                                                  profileGUID,
                                                  profileGUIDParameterName,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);            }
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
