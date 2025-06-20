/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.api.OrganizationManagementInterface;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorProfileGraphElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorRoleElement;


/**
 * OrganizationManagement is the client used by the Organization Integrator OMIS that is responsible with synchronizing organizational
 * structures, profiles rules and users with open metadata.
 */
public class OrganizationManagement extends CommunityProfileBaseClient implements OrganizationManagementInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           maximum value allowed for page size
     * @throws InvalidParameterException bad input parameters
     */
    public OrganizationManagement(String serverName,
                                  String serverPlatformURLRoot,
                                  int maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog              logging destination
     * @param maxPageSize           maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OrganizationManagement(String serverName,
                                  String serverPlatformURLRoot,
                                  AuditLog auditLog,
                                  int maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum value allowed for page size
     * @throws InvalidParameterException bad input parameters
     */
    public OrganizationManagement(String serverName,
                                  String serverPlatformURLRoot,
                                  String userId,
                                  String password,
                                  int maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param auditLog              logging destination
     * @param maxPageSize           maximum value allowed for page size
     * @throws InvalidParameterException bad input parameters
     */
    public OrganizationManagement(String serverName,
                                  String serverPlatformURLRoot,
                                  String userId,
                                  String password,
                                  AuditLog auditLog,
                                  int maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            pre-initialized REST client
     * @param maxPageSize           pre-initialized parameter limit
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public OrganizationManagement(String serverName,
                                  String serverPlatformURLRoot,
                                  CommunityProfileRESTClient restClient,
                                  int maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param userId             calling user
     * @param actorProfileUserId unique identifier for the actor profile
     * @return properties of the actor profile
     * @throws InvalidParameterException  actorProfileUserId or userId is null
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public ActorProfileGraphElement getActorProfileByUserId(String userId,
                                                            String actorProfileUserId) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName        = "getActorProfileByGUID";
        final String nameParameterName = "actorProfileUserId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(actorProfileUserId, nameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/profiles/user-ids/{2}";

        ActorProfileGraphResponse restResult = restClient.callActorProfileGetRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      serverName,
                                                                                      userId,
                                                                                      actorProfileUserId);

        return restResult.getElement();
    }


    /**
     * Return information about a specific person role.
     *
     * @param userId         calling user
     * @param personRoleGUID unique identifier for the person role
     * @return properties of the person role
     * @throws InvalidParameterException  personRoleGUID or userId is null
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public ActorRoleElement getPersonRoleByGUID(String userId,
                                                String personRoleGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName        = "getPersonRoleByGUID";
        final String guidParameterName = "personRoleGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/person-roles/{2}";

        ActorRoleResponse restResult = restClient.callPersonRoleGetRESTCall(methodName,
                                                                            urlTemplate,
                                                                            serverName,
                                                                            userId,
                                                                            personRoleGUID);

        return restResult.getElement();
    }


}
