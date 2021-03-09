/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.handlers.UserIdentityHandler;
import org.odpi.openmetadata.accessservices.communityprofile.rest.UserIdentityRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UserIdentityResource provides the APIs for maintaining user identities and their relationships with profiles.
 */
public class UserIdentityRESTServices
{
    static private CommunityProfileInstanceHandler instanceHandler = new CommunityProfileInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(UserIdentityRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

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
     * @param userId the name of the calling user.
     * @param requestBody userId for the new userIdentity.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse createUserIdentity(String                  serverName,
                                           String                  userId,
                                           UserIdentityRequestBody requestBody)
    {
        final String        methodName = "createUserIdentity";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String   qualifiedName = null;

            if (requestBody != null)
            {
                qualifiedName = requestBody.getQualifiedName();
            }
            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.addUserIdentity(userId, qualifiedName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Link a user identity to a profile.  This will fail if the user identity is already connected to
     * a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param profileGUID the profile to add the identity to.
     * @param requestBody additional userId for the profile.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse  addIdentityToProfile(String                  serverName,
                                              String                  userId,
                                              String                  profileGUID,
                                              UserIdentityRequestBody requestBody)
    {
        final String        methodName = "addIdentityToProfile";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String   qualifiedName = null;

            if (requestBody != null)
            {
                qualifiedName = requestBody.getQualifiedName();
            }
            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.addIdentityToProfile(userId, qualifiedName, profileGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Remove a user identity object.  This will fail if the profile would be left without an
     * associated user identity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param profileGUID profile to remove it from.
     * @param requestBody user identity to remove.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeIdentityFromProfile(String                  serverName,
                                                  String                  userId,
                                                  String                  profileGUID,
                                                  UserIdentityRequestBody requestBody)
    {
        final String        methodName = "removeIdentityFromProfile";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String   qualifiedName = null;

            if (requestBody != null)
            {
                qualifiedName = requestBody.getQualifiedName();
            }
            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeIdentityFromProfile(userId, qualifiedName, profileGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Remove a user identity object.  This will fail if a profile would be left without an
     * associated user identity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param requestBody user identity to remove.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeUserIdentity(String                  serverName,
                                           String                  userId,
                                           UserIdentityRequestBody requestBody)
    {
        final String        methodName = "removeUserIdentity";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String   qualifiedName = null;

            if (requestBody != null)
            {
                qualifiedName = requestBody.getQualifiedName();
            }

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeUserIdentity(userId, qualifiedName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }
}
