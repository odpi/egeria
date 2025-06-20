/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.ActorProfileGraphResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ActorRoleResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ActorProfileHandler;
import org.odpi.openmetadata.commonservices.generichandlers.PersonRoleHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorProfileGraphElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorRoleElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The OrganizationRESTServices provides the server-side implementation of the Community Profile Open Metadata
 * Assess Service (OMAS) capability for organization management.  This interface provides support for creating all types of actor profiles and
 * associated user roles in order to describe the structure of an organization.
 */
public class OrganizationRESTServices
{
    static private final CommunityProfileInstanceHandler instanceHandler = new CommunityProfileInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OrganizationRESTServices.class),
                                                                            instanceHandler.getServiceName());


    private final RESTExceptionHandler   restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public OrganizationRESTServices()
    {
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileUserId unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *   InvalidParameterException actorProfileUserId or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ActorProfileGraphResponse getActorProfileByUserId(String serverName,
                                                             String userId,
                                                             String actorProfileUserId)
    {
        final String methodName        = "getActorProfileByGUID";
        final String nameParameterName = "actorProfileUserId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfileGraphResponse response = new ActorProfileGraphResponse();
        AuditLog                  auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileGraphElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getActorProfileForUser(userId,
                                                               actorProfileUserId,
                                                               nameParameterName,
                                                               OpenMetadataType.ACTOR_PROFILE.typeName,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a specific person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param personRoleGUID unique identifier for the person role
     *
     * @return properties of the person role
     *   InvalidParameterException personRoleGUID or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ActorRoleResponse getPersonRoleByGUID(String serverName,
                                                 String userId,
                                                 String personRoleGUID)
    {
        final String methodName        = "getPersonRoleByGUID";
        final String guidParameterName = "personRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorRoleResponse response = new ActorRoleResponse();
        AuditLog          auditLog = null;

        try
        {
            PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getPersonRoleByGUID(userId,
                                                            personRoleGUID,
                                                            guidParameterName,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
