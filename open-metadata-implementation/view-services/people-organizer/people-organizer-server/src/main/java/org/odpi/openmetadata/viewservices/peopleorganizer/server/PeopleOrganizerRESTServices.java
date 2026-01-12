/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.peopleorganizer.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PeerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamStructureProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The PeopleOrganizerRESTServices provides the server-side implementation of the People Organizer Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class PeopleOrganizerRESTServices extends TokenController
{
    private static final PeopleOrganizerInstanceHandler instanceHandler = new PeopleOrganizerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(PeopleOrganizerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public PeopleOrganizerRESTServices()
    {
    }



    /**
     * Attach a person profile to one of its peers.
     *
     * @param serverName         name of called server
     * @param personOneGUID unique identifier of the first actor profile
     * @param personTwoGUID unique identifier of the second actor profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkPeerPerson(String                  serverName,
                                       String                  personOneGUID,
                                       String                  personTwoGUID,
                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkPeerPerson";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof PeerProperties peerProperties)
                {
                    handler.linkPeerPerson(userId,
                                           personOneGUID,
                                           personTwoGUID,
                                           requestBody,
                                           peerProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(PeerProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkPeerPerson(userId,
                                       personOneGUID,
                                       personTwoGUID,
                                       null,
                                       null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a person profile from one of its peers.
     *
     * @param serverName         name of called server
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachPeerPerson(String                        serverName,
                                         String                        personOneGUID,
                                         String                        personTwoGUID,
                                         DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachPeerPerson";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            handler.detachPeerPerson(userId, personOneGUID, personTwoGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Attach a super team to a subteam.
     *
     * @param serverName         name of called server
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkTeamStructure(String                  serverName,
                                          String                  superTeamGUID,
                                          String                  subteamGUID,
                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkTeamStructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof TeamStructureProperties teamStructureProperties)
                {
                    handler.linkTeamStructure(userId,
                                              superTeamGUID,
                                              subteamGUID,
                                              requestBody,
                                              teamStructureProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TeamStructureProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkTeamStructure(userId,
                                          superTeamGUID,
                                          subteamGUID,
                                          null,
                                          null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a super team from a subteam.
     *
     * @param serverName         name of called server
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachTeamStructure(String                        serverName,
                                            String                        superTeamGUID,
                                            String                        subteamGUID,
                                            DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachTeamStructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            handler.detachTeamStructure(userId, superTeamGUID, subteamGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
