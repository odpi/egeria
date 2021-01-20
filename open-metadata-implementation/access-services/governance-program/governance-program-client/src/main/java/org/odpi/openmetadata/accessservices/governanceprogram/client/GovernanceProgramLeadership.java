/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;


import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceLeadershipInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReference;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceOfficerProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceProgramLeadership provides the client-side interface for the Governance Program Open Metadata Access Service (OMAS).
 * This client, manages all of the interaction with an open metadata repository.  It is initialized with the URL
 * of the server that is running the Open Metadata Access Services.  This server is responsible for locating and
 * managing the governance program definitions exchanged with this client.
 */
public class GovernanceProgramLeadership  implements GovernanceLeadershipInterface
{
    private String                      serverName;               /* Initialized in constructor */
    private String                      serverPlatformURLRoot;    /* Initialized in constructor */
    private GovernanceProgramRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();
    private AuditLog                auditLog                = null;

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
        this.auditLog              = auditLog;
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
        this.auditLog              = auditLog;
    }


    /**
     * Create the governance officer appointment.
     *
     * @param userId the name of the calling user.
     * @param governanceDomain  the governance domain for the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param appointmentContext  the context in which the governance officer is appointed.
     *                            This may be an organizational scope, location, or scope of assets.
     * @param title job title for the governance officer.
     * @param additionalProperties additional properties for the governance officer.
     * @param externalReferences links to addition information.  This could be, for example, the home page
     *                           for the governance officer, or details of the role.
     * @return Unique identifier (guid) of the governance officer.
     * @throws InvalidParameterException the governance domain, title or appointment id is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public String createGovernanceOfficer(String                     userId,
                                          GovernanceDomain           governanceDomain,
                                          String                     appointmentId,
                                          String                     appointmentContext,
                                          String                     title,
                                          Map<String, String>        additionalProperties,
                                          List<ExternalReference>    externalReferences) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String   methodName = "createGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers";

        final String   appointmentIdParameterName = "appointmentId";
        final String   titleParameterName = "title";
        final String   governanceDomainParameterName = "governanceDomain";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        invalidParameterHandler.validateName(title, titleParameterName, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceOfficerDetailsRequestBody  requestBody = new GovernanceOfficerDetailsRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);
        requestBody.setAppointmentId(appointmentId);
        requestBody.setAppointmentContext(appointmentContext);
        requestBody.setTitle(title);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExternalReferences(externalReferences);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);

        return restResult.getGUID();
    }


    /**
     * Update selected fields for the governance officer.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param governanceDomain  the governance domain for the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param appointmentContext  the context in which the governance officer is appointed.
     *                            This may be an organizational scope, location, or scope of assets.
     * @param title job title for the governance officer.
     * @param additionalProperties additional properties for the governance officer.
     * @param externalReferences links to addition information.  This could be, for example, the home page
     *                           for the governance officer, or details of the role.
     * @throws InvalidParameterException the title is null or the governanceDomain/appointmentId does not match the
     *                                   the existing values associated with the governanceOfficerGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void   updateGovernanceOfficer(String                     userId,
                                          String                     governanceOfficerGUID,
                                          GovernanceDomain           governanceDomain,
                                          String                     appointmentId,
                                          String                     appointmentContext,
                                          String                     title,
                                          Map<String, String>        additionalProperties,
                                          List<ExternalReference>    externalReferences)  throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String   methodName = "updateGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}";

        final String   guidParameterName = "governanceOfficerGUID";
        final String   appointmentIdParameterName = "appointmentId";
        final String   titleParameterName = "title";
        final String   governanceDomainParameterName = "governanceDomain";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        invalidParameterHandler.validateName(title, titleParameterName, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceOfficerDetailsRequestBody  requestBody = new GovernanceOfficerDetailsRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);
        requestBody.setAppointmentId(appointmentId);
        requestBody.setAppointmentContext(appointmentContext);
        requestBody.setTitle(title);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExternalReferences(externalReferences);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceOfficerGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
    }


    /**
     * Remove the requested governance officer.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param governanceDomain  the governance domain for the governance officer.
     * @throws InvalidParameterException the appointmentId or governance domain is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void   deleteGovernanceOfficer(String              userId,
                                          String              governanceOfficerGUID,
                                          String              appointmentId,
                                          GovernanceDomain    governanceDomain) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String   methodName = "deleteGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}/delete";

        final String   guidParameterName = "governanceOfficerGUID";
        final String   appointmentIdParameterName = "appointmentId";
        final String   governanceDomainParameterName = "governanceDomain";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, guidParameterName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceOfficerValidatorRequestBody  requestBody = new GovernanceOfficerValidatorRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);
        requestBody.setAppointmentId(appointmentId);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceOfficerGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
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
    public GovernanceOfficerProperties getGovernanceOfficerByGUID(String     userId,
                                                                  String     governanceOfficerGUID) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficerByGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}";

        final String   guidParameterName = "governanceOfficerGUID";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, guidParameterName, methodName);

        GovernanceOfficerResponse restResult = restClient.callGovernanceOfficerGetRESTCall(methodName,
                                                                                           serverPlatformURLRoot + urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           governanceOfficerGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);

        return restResult.getGovernanceOfficer();
    }


    /**
     * Retrieve a governance officer by unique appointment id.
     *
     * @param userId the name of the calling user.
     * @param appointmentId  the unique appointment identifier of the governance officer.
     * @return governance officer object
     * @throws InvalidParameterException the appointmentId or governance domain is either null or invalid.
     * @throws AppointmentIdNotUniqueException more than one governance officer entity was retrieved for this appointmentId.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public GovernanceOfficerProperties getGovernanceOfficerByAppointmentId(String     userId,
                                                                           String     appointmentId) throws InvalidParameterException,
                                                                                                         AppointmentIdNotUniqueException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficerByAppointmentId";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/by-appointment-id/{2}";

        final String   appointmentIdParameterName = "appointmentId";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(appointmentId, appointmentIdParameterName, methodName);

        GovernanceOfficerResponse restResult = restClient.callGovernanceOfficerGetRESTCall(methodName,
                                                                                           serverPlatformURLRoot + urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           appointmentId);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowAppointmentIdNotUniqueException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);

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
    public List<GovernanceOfficerProperties>  getGovernanceOfficers(String     userId) throws InvalidParameterException,
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

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);

        return restResult.getGovernanceOfficers();
    }


    /**
     * Return all of the currently appointed governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws InvalidParameterException the userId is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<GovernanceOfficerProperties>  getActiveGovernanceOfficers(String     userId) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficers";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/active";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceOfficerListResponse restResult = restClient.callGovernanceOfficerListGetRESTCall(methodName,
                                                                                                   serverPlatformURLRoot + urlTemplate,
                                                                                                   serverName,
                                                                                                   userId);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);

        return restResult.getGovernanceOfficers();
    }


    /**
     * Return all of the defined governance officers for a specific governance domain.  In a small organization
     * there is typically only one governance officer.   However a large organization may have multiple governance
     * officers, each with a different scope.  The governance officer with a null scope is the overall leader.
     *
     * @param userId the name of the calling user.
     * @param governanceDomain domain of interest
     * @return list of governance officer objects
     * @throws InvalidParameterException the governance domain is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<GovernanceOfficerProperties>  getGovernanceOfficersByDomain(String             userId,
                                                                            GovernanceDomain   governanceDomain) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficersByDomain";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/by-domain";

        final String   governanceDomainParameterName = "governanceDomain";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceDomainRequestBody  requestBody = new GovernanceDomainRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);

        GovernanceOfficerListResponse restResult = restClient.callGovernanceOfficerListPostRESTCall(methodName,
                                                                                                    serverPlatformURLRoot + urlTemplate,
                                                                                                    requestBody,
                                                                                                    serverName,
                                                                                                    userId);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);

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
        requestBody.setGUID(profileGUID);
        requestBody.setEffectiveDate(startDate);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceOfficerGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
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
        requestBody.setGUID(profileGUID);
        requestBody.setEffectiveDate(endDate);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceOfficerGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
    }
}
