/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceLeadershipInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceOfficerAppointee;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceOfficerElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceOfficerHistory;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceOfficerProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * GovernanceProgramLeadership provides the client-side interface for the Governance Program Open Metadata Access Service (OMAS).
 * This client, manages all of the interaction with an open metadata repository.  It is initialized with the URL
 * of the server that is running the Open Metadata Access Services.  This server is responsible for locating and
 * managing the governance program definitions exchanged with this client.
 */
public class GovernanceProgramLeadership implements GovernanceLeadershipInterface
{
    private String                      serverName;               /* Initialized in constructor */
    private String                      serverPlatformURLRoot;    /* Initialized in constructor */
    private GovernanceProgramRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private NullRequestBody         nullRequestBody         = new NullRequestBody();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceProgramLeadership(String serverName,
                                       String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceProgramLeadership(String     serverName,
                                       String     serverPlatformURLRoot,
                                       String     userId,
                                       String     password) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceProgramLeadership(String   serverName,
                                       String   serverPlatformURLRoot,
                                       int      maxPageSize,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceProgramLeadership(String     serverName,
                                       String     serverPlatformURLRoot,
                                       String     userId,
                                       String     password,
                                       int        maxPageSize,
                                       AuditLog   auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create the governance officer role.
     *
     * @param userId the name of the calling user.
     * @param properties the description of the governance officer role
     *
     * @return Unique identifier (guid) of the governance officer
     *
     * @throws InvalidParameterException the governance domain, title or appointment id is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public String createGovernanceOfficer(String                      userId,
                                          GovernanceOfficerProperties properties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String   methodName = "createGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers";

        final String   roleIdParameterName = "roleId";
        final String   titleParameterName = "title";
        final String   propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getRoleId(), roleIdParameterName, methodName);
        invalidParameterHandler.validateName(properties.getTitle(), titleParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  properties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update selected fields for the governance officer.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties the description of the governance officer role
     *
     * @throws InvalidParameterException the title is null or the governanceDomain/appointmentId does not match the
     *                                   the existing values associated with the governanceOfficerGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void   updateGovernanceOfficer(String                      userId,
                                          String                      governanceOfficerGUID,
                                          boolean                     isMergeUpdate,
                                          GovernanceOfficerProperties properties)  throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String   methodName = "updateGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}?isMergeUpdate={3}";

        final String   guidParameterName = "governanceOfficerGUID";
        final String   roleIdParameterName = "roleId";
        final String   titleParameterName = "title";
        final String   propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getRoleId(), roleIdParameterName, methodName);
            invalidParameterHandler.validateName(properties.getTitle(), titleParameterName, methodName);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        governanceOfficerGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the requested governance officer.
     *
     * @param userId the name of the calling user
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer
     *
     * @throws InvalidParameterException the appointmentId or governance domain is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void   deleteGovernanceOfficer(String userId,
                                          String governanceOfficerGUID) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String   methodName = "deleteGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}/delete";
        final String   guidParameterName = "governanceOfficerGUID";

        invalidParameterHandler.validateGUID(governanceOfficerGUID, guidParameterName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        governanceOfficerGUID);
    }


    /**
     * Retrieve a governance officer description by unique guid.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @return governance officer object
     * @throws InvalidParameterException the unique identifier of the governance officer is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public GovernanceOfficerElement getGovernanceOfficerByGUID(String userId,
                                                               String governanceOfficerGUID) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficerByGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}";

        final String   guidParameterName = "governanceOfficerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, guidParameterName, methodName);

        GovernanceOfficerResponse restResult = restClient.callGovernanceOfficerGetRESTCall(methodName,
                                                                                           serverPlatformURLRoot + urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           governanceOfficerGUID);

        return restResult.getGovernanceOfficer();
    }


    /**
     * Retrieve a governance officer description by unique guid.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @return governance officer object
     * @throws InvalidParameterException the unique identifier of the governance officer is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public GovernanceOfficerHistory getGovernanceOfficerHistoryByGUID(String userId,
                                                                      String governanceOfficerGUID) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficerHistoryByGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}/history";

        final String   guidParameterName = "governanceOfficerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, guidParameterName, methodName);

        GovernanceOfficerHistoryResponse restResult = restClient.callGovernanceOfficerHistoryGetRESTCall(methodName,
                                                                                                         serverPlatformURLRoot + urlTemplate,
                                                                                                         serverName,
                                                                                                         userId,
                                                                                                         governanceOfficerGUID);

        return restResult.getGovernanceOfficer();
    }


    /**
     * Retrieve a governance officer by unique appointment id.
     *
     * @param userId the name of the calling user.
     * @param roleId  the unique appointment identifier of the governance officer.
     * @return governance officer object
     *
     * @throws InvalidParameterException the appointmentId or governance domain is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public GovernanceOfficerElement getGovernanceOfficerByRoleId(String userId,
                                                                 String roleId) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficerByRoleId";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/by-appointment-id/{2}";

        final String   appointmentIdParameterName = "appointmentId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(roleId, appointmentIdParameterName, methodName);

        GovernanceOfficerResponse restResult = restClient.callGovernanceOfficerGetRESTCall(methodName,
                                                                                           serverPlatformURLRoot + urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           roleId);

        return restResult.getGovernanceOfficer();
    }


    /**
     * Return all of the defined governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws InvalidParameterException the userId is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<GovernanceOfficerElement>  getGovernanceOfficers(String     userId) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficers";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceOfficerListResponse restResult = restClient.callGovernanceOfficerListGetRESTCall(methodName,
                                                                                                   serverPlatformURLRoot + urlTemplate,
                                                                                                   serverName,
                                                                                                   userId);

        return restResult.getGovernanceOfficers();
    }


    /**
     * Return all of the currently appointed governance officers.
     *
     * @param userId the name of the calling user
     *
     * @return list of governance officer objects
     *
     * @throws InvalidParameterException the userId is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<GovernanceOfficerAppointee>  getActiveGovernanceOfficers(String userId) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficers";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/active";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceOfficerAppointeeListResponse restResult = restClient.callGovernanceOfficerAppointeeListGetRESTCall(methodName,
                                                                                                                     serverPlatformURLRoot + urlTemplate,
                                                                                                                     serverName,
                                                                                                                     userId);

        return restResult.getGovernanceOfficers();
    }


    /**
     * Return all of the defined governance officers for a specific governance domain.  In a small organization
     * there is typically only one governance officer.   However a large organization may have multiple governance
     * officers, each with a different scope.  The governance officer with a null scope is the overall leader.
     *
     * @param userId the name of the calling user
     * @param domainIdentifier domain of interest
     *
     * @return list of governance officer objects
     *
     * @throws InvalidParameterException the governance domain is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<GovernanceOfficerAppointee>  getGovernanceOfficersByDomain(String userId,
                                                                           int domainIdentifier) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficersByDomain";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/by-domain/{2}";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceOfficerAppointeeListResponse restResult = restClient.callGovernanceOfficerAppointeeListGetRESTCall(methodName,
                                                                                                                     serverPlatformURLRoot + urlTemplate,
                                                                                                                     serverName,
                                                                                                                     userId,
                                                                                                                     domainIdentifier);

        return restResult.getGovernanceOfficers();
    }


    /**
     * Link a person to a governance officer.  Only one person may be appointed at any one time.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param profileGUID unique identifier for the profile.
     * @param startDate the official start date of the appointment - null means effective immediately.
     * @throws InvalidParameterException the unique identifier of the governance officer or profile is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void appointGovernanceOfficer(String  userId,
                                         String  governanceOfficerGUID,
                                         String  profileGUID,
                                         Date    startDate) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String   methodName = "appointGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}/appoint";

        final String   governanceOfficerGUIDParameterName = "governanceOfficerGUID";
        final String   profileGUIDParameterName = "profileGUID";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(profileGUID, profileGUIDParameterName, methodName);

        AppointmentRequestBody  requestBody = new AppointmentRequestBody();
        requestBody.setProfileGUID(profileGUID);
        requestBody.setEffectiveDate(startDate);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        governanceOfficerGUID);
    }


    /**
     * Unlink a person from a governance officer appointment.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param profileGUID unique identifier for the profile.
     * @param endDate the official end of the appointment - null means effective immediately.
     * @throws InvalidParameterException the profile is not linked to this governance officer.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void relieveGovernanceOfficer(String  userId,
                                         String  governanceOfficerGUID,
                                         String  profileGUID,
                                         Date    endDate) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName = "relieveGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}/relieve";

        final String   governanceOfficerGUIDParameterName = "governanceOfficerGUID";
        final String   profileGUIDParameterName = "profileGUID";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(profileGUID, profileGUIDParameterName, methodName);

        AppointmentRequestBody  requestBody = new AppointmentRequestBody();
        requestBody.setProfileGUID(profileGUID);
        requestBody.setEffectiveDate(endDate);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        governanceOfficerGUID);
    }
}
