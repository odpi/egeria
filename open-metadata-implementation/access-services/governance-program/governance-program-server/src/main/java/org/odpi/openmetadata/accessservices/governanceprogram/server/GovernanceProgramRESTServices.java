/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReference;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The GovernanceProgramRESTServices provides the server-side implementation of the GovernanceProgram Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class GovernanceProgramRESTServices
{
    static private GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(GovernanceProgramRESTServices.class);

    /**
     * Default constructor
     */
    public GovernanceProgramRESTServices()
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
                                              PersonalDetailsRequestBody requestBody)
    {
        final String        methodName = "createPersonalProfile";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();

        try
        {
            String              profileUserId = null;
            String              employeeNumber = null;
            String              fullName = null;
            String              knownName = null;
            String              jobTitle = null;
            String              jobRoleDescription = null;
            Map<String, String> additionalProperties = null;

            if (requestBody != null)
            {
                profileUserId = requestBody.getUserId();
                employeeNumber = requestBody.getEmployeeNumber();
                fullName = requestBody.getFullName();
                knownName = requestBody.getKnownName();
                jobTitle = requestBody.getJobTitle();
                jobRoleDescription = requestBody.getJobRoleDescription();
                additionalProperties = requestBody.getAdditionalProperties();
            }

            PersonalProfileHandler   handler = new PersonalProfileHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            response.setGUID(handler.createPersonalProfile(userId,
                                                           profileUserId,
                                                           employeeNumber,
                                                           fullName,
                                                           knownName,
                                                           jobTitle,
                                                           jobRoleDescription,
                                                           additionalProperties));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
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
     * UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid or
     * InvalidParameterException the full name is null or the employeeNumber does not match the profileGUID or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse   updatePersonalProfile(String                     serverName,
                                                String                     userId,
                                                String                     profileGUID,
                                                PersonalDetailsRequestBody requestBody)
    {
        final String        methodName = "updatePersonalProfile";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            String              employeeNumber = null;
            String              fullName = null;
            String              knownName = null;
            String              jobTitle = null;
            String              jobRoleDescription = null;
            Map<String, String> additionalProperties = null;

            if (requestBody != null)
            {
                employeeNumber = requestBody.getEmployeeNumber();
                fullName = requestBody.getFullName();
                knownName = requestBody.getKnownName();
                jobTitle = requestBody.getJobTitle();
                jobRoleDescription = requestBody.getJobRoleDescription();
                additionalProperties = requestBody.getAdditionalProperties();
            }

            PersonalProfileHandler   handler = new PersonalProfileHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            handler.updatePersonalProfile(userId,
                                          profileGUID,
                                          employeeNumber,
                                          fullName,
                                          knownName,
                                          jobTitle,
                                          jobRoleDescription,
                                          additionalProperties);
        }
        catch (UnrecognizedGUIDException error)
        {
            captureUnrecognizedGUIDException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
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

        try
        {
            String              employeeNumber = null;

            if (requestBody != null)
            {
                employeeNumber = requestBody.getEmployeeNumber();
            }

            PersonalProfileHandler   handler = new PersonalProfileHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            handler.deletePersonalProfile(userId,
                                          profileGUID,
                                          employeeNumber);
        }
        catch (UnrecognizedGUIDException error)
        {
            captureUnrecognizedGUIDException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
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

        try
        {
            PersonalProfileHandler   handler = new PersonalProfileHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            response.setPersonalProfile(handler.getPersonalProfileByGUID(userId, profileGUID));
        }
        catch (UnrecognizedGUIDException error)
        {
            captureUnrecognizedGUIDException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
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
    public PersonalProfileResponse getPersonalProfileByEmployeeNumber(String         serverName,
                                                                      String         userId,
                                                                      String         employeeNumber)
    {
        final String        methodName = "getPersonalProfileByEmployeeNumber";

        log.debug("Calling method: " + methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();

        try
        {
            PersonalProfileHandler   handler = new PersonalProfileHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            response.setPersonalProfile(handler.getPersonalProfileByEmployeeNumber(userId, employeeNumber));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (EmployeeNumberNotUniqueException error)
        {
            captureEmployeeNumberNotUniqueException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
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
     * @return list of personal profile objects or
     * InvalidParameterException the name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfileListResponse getPersonalProfilesByName(String        serverName,
                                                                 String        userId,
                                                                 String        name)
    {
        final String        methodName = "getPersonalProfilesByName";

        log.debug("Calling method: " + methodName);

        PersonalProfileListResponse response = new PersonalProfileListResponse();

        try
        {
            PersonalProfileHandler   handler = new PersonalProfileHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            response.setPersonalProfiles(handler.getPersonalProfilesByName(userId, name));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Create the governance officer appointment.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param requestBody  properties of the governance officer.
     * @return Unique identifier (guid) of the governance officer or
     * InvalidParameterException the governance domain or appointment id is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GUIDResponse createGovernanceOfficer(String                               serverName,
                                                String                               userId,
                                                GovernanceOfficerDetailsRequestBody  requestBody)

    {
        final String        methodName = "createGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();

        GovernanceDomain           governanceDomain = null;
        String                     appointmentId = null;
        String                     appointmentContext = null;
        String                     title = null;
        Map<String, String>        additionalProperties = null;
        List<ExternalReference>    externalReferences = null;

        if (requestBody != null)
        {
            governanceDomain = requestBody.getGovernanceDomain();
            appointmentId = requestBody.getAppointmentId();
            appointmentContext = requestBody.getAppointmentContext();
            title = requestBody.getTitle();
            additionalProperties = requestBody.getAdditionalProperties();
            externalReferences = requestBody.getExternalReferences();
        }

        try
        {
            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(instanceHandler.getAccessServiceName(),
                                                                              instanceHandler.getRepositoryConnector(serverName));

            response.setGUID(handler.createGovernanceOfficer(userId,
                                                             governanceDomain,
                                                             appointmentId,
                                                             appointmentContext,
                                                             title,
                                                             additionalProperties,
                                                             externalReferences));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }


        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Update selected fields for the governance officer.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody  properties of the governance officer
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * InvalidParameterException the title is null or the governanceDomain/appointmentId does not match the
     *                           the existing values associated with the governanceOfficerGUID or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse   updateGovernanceOfficer(String                               serverName,
                                                  String                               userId,
                                                  String                               governanceOfficerGUID,
                                                  GovernanceOfficerDetailsRequestBody  requestBody)
    {
        final String        methodName = "updateGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        GovernanceDomain           governanceDomain = null;
        String                     appointmentId = null;
        String                     appointmentContext = null;
        String                     title = null;
        Map<String, String>        additionalProperties = null;
        List<ExternalReference>    externalReferences = null;

        if (requestBody != null)
        {
            governanceDomain = requestBody.getGovernanceDomain();
            appointmentId = requestBody.getAppointmentId();
            appointmentContext = requestBody.getAppointmentContext();
            title = requestBody.getTitle();
            additionalProperties = requestBody.getAdditionalProperties();
            externalReferences = requestBody.getExternalReferences();
        }

        try
        {
            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(instanceHandler.getAccessServiceName(),
                                                                              instanceHandler.getRepositoryConnector(serverName));

            handler.updateGovernanceOfficer(userId,
                                            governanceOfficerGUID,
                                            governanceDomain,
                                            appointmentId,
                                            appointmentContext,
                                            title,
                                            additionalProperties,
                                            externalReferences);
        }
        catch (UnrecognizedGUIDException error)
        {
            captureUnrecognizedGUIDException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Remove the requested governance officer.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody  properties to verify this is the right governance officer
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * InvalidParameterException the appointmentId or governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse   deleteGovernanceOfficer(String                                 serverName,
                                                  String                                 userId,
                                                  String                                 governanceOfficerGUID,
                                                  GovernanceOfficerValidatorRequestBody  requestBody)
    {
        final String        methodName = "deleteGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            GovernanceDomain    governanceDomain = null;
            String              appointmentId = null;

            if (requestBody != null)
            {
                governanceDomain = requestBody.getGovernanceDomain();
                appointmentId = requestBody.getAppointmentId();
            }

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(instanceHandler.getAccessServiceName(),
                                                                              instanceHandler.getRepositoryConnector(serverName));

            handler.deleteGovernanceOfficer(userId,
                                            governanceOfficerGUID,
                                            appointmentId,
                                            governanceDomain);
        }
        catch (UnrecognizedGUIDException error)
        {
            captureUnrecognizedGUIDException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Retrieve a governance officer description by unique guid.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @return governance officer object or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerResponse getGovernanceOfficerByGUID(String     serverName,
                                                                String     userId,
                                                                String     governanceOfficerGUID)
    {
        final String        methodName = "getGovernanceOfficerByGUID";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerResponse response = new GovernanceOfficerResponse();

        try
        {
            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(instanceHandler.getAccessServiceName(),
                                                                              instanceHandler.getRepositoryConnector(serverName));

            response.setGovernanceOfficer(handler.getGovernanceOfficerByGUID(userId, governanceOfficerGUID));
        }
        catch (UnrecognizedGUIDException error)
        {
            captureUnrecognizedGUIDException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Retrieve a governance officer by unique appointment id.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param appointmentId  the unique appointment identifier of the governance officer.
     * @return governance officer object or
     * InvalidParameterException the appointmentId or governance domain is either null or invalid or
     * AppointmentIdNotUniqueException more than one governance officer entity was retrieved for this appointmentId
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerResponse   getGovernanceOfficerByAppointmentId(String     serverName,
                                                                           String     userId,
                                                                           String     appointmentId)
    {
        final String        methodName = "getGovernanceOfficerByAppointmentId";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerResponse response = new GovernanceOfficerResponse();

        try
        {
            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(instanceHandler.getAccessServiceName(),
                                                                              instanceHandler.getRepositoryConnector(serverName));

            response.setGovernanceOfficer(handler.getGovernanceOfficerByAppointmentId(userId, appointmentId));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (AppointmentIdNotUniqueException error)
        {
            captureAppointmentIdNotUniqueException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return all of the defined governance officers.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @return list of governance officer objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerListResponse  getGovernanceOfficers(String     serverName,
                                                                String     userId)
    {
        final String        methodName = "getGovernanceOfficers";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerListResponse response = new GovernanceOfficerListResponse();

        try
        {
            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(instanceHandler.getAccessServiceName(),
                                                                              instanceHandler.getRepositoryConnector(serverName));

            response.setGovernanceOfficers(handler.getGovernanceOfficers(userId));
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return all of the defined governance officers.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @return list of governance officer objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerListResponse  getActiveGovernanceOfficers(String     serverName,
                                                                      String     userId)
    {
        final String        methodName = "getActiveGovernanceOfficers";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerListResponse response = new GovernanceOfficerListResponse();

        try
        {
            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(instanceHandler.getAccessServiceName(),
                                                                              instanceHandler.getRepositoryConnector(serverName));

            response.setGovernanceOfficers(handler.getActiveGovernanceOfficers(userId));
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return all of the defined governance officers for a specific governance domain.  In a small organization
     * there is typically only one governance officer.   However a large organization may have multiple governance
     * officers, each with a different scope.  The governance officer with a null scope is the overall leader.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param requestBody domain of interest.
     * @return list of governance officer objects or
     * InvalidParameterException the governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerListResponse  getGovernanceOfficersByDomain(String                        serverName,
                                                                        String                        userId,
                                                                        GovernanceDomainRequestBody   requestBody)
    {
        final String        methodName = "getGovernanceOfficersByDomain";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerListResponse response = new GovernanceOfficerListResponse();


        try
        {
            GovernanceDomain  governanceDomain = null;

            if (requestBody != null)
            {
                governanceDomain = requestBody.getGovernanceDomain();
            }

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(instanceHandler.getAccessServiceName(),
                                                                              instanceHandler.getRepositoryConnector(serverName));

            response.setGovernanceOfficers(handler.getGovernanceOfficersByDomain(userId, governanceDomain));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Link a person to a governance officer.  Only one person may be appointed at any one time.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody unique identifier for the profile
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse appointGovernanceOfficer(String                  serverName,
                                                 String                  userId,
                                                 String                  governanceOfficerGUID,
                                                 AppointmentRequestBody  requestBody)
    {
        final String        methodName = "appointGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            String              profileGUID = null;
            Date                startDate   = null;

            if (requestBody != null)
            {
                profileGUID = requestBody.getGUID();
                startDate   = requestBody.getEffectiveDate();
            }

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(instanceHandler.getAccessServiceName(),
                                                                              instanceHandler.getRepositoryConnector(serverName));

            handler.appointGovernanceOfficer(userId,
                                             governanceOfficerGUID,
                                             profileGUID,
                                             startDate);
        }
        catch (UnrecognizedGUIDException error)
        {
            captureUnrecognizedGUIDException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Unlink a person from a governance officer appointment.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody unique identifier for the profile.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid or
     * InvalidParameterException the profile is not linked to this governance officer or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse relieveGovernanceOfficer(String                  serverName,
                                                 String                  userId,
                                                 String                  governanceOfficerGUID,
                                                 AppointmentRequestBody  requestBody)
    {
        final String        methodName = "relieveGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            String              profileGUID = null;
            Date                endDate     = null;

            if (requestBody != null)
            {
                profileGUID = requestBody.getGUID();
                endDate     = requestBody.getEffectiveDate();
            }

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(instanceHandler.getAccessServiceName(),
                                                                              instanceHandler.getRepositoryConnector(serverName));

            handler.relieveGovernanceOfficer(userId,
                                             governanceOfficerGUID,
                                             profileGUID,
                                             endDate);
        }
        catch (UnrecognizedGUIDException error)
        {
            captureUnrecognizedGUIDException(response, error);
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /* ==========================
     * Support methods
     * ==========================
     */


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     */
    private void captureCheckedException(GovernanceProgramOMASAPIResponse      response,
                                         GovernanceProgramCheckedExceptionBase error,
                                         String                                exceptionClassName)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     * @param exceptionProperties map of properties stored in the exception to help with diagnostics
     */
    private void captureCheckedException(GovernanceProgramOMASAPIResponse      response,
                                         GovernanceProgramCheckedExceptionBase error,
                                         String                                exceptionClassName,
                                         Map<String, Object>                   exceptionProperties)
    {
        this.captureCheckedException(response, error, exceptionClassName);
        response.setExceptionProperties(exceptionProperties);
    }



    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureUnrecognizedGUIDException(GovernanceProgramOMASAPIResponse     response,
                                                  UnrecognizedGUIDException            error)
    {
        String  guid = error.getGUID();
        String  expectedTypeName = error.getExpectedTypeName();

        if ((guid != null) || (expectedTypeName != null))
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("guid", guid);
            exceptionProperties.put("expectedTypeName", expectedTypeName);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }

        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureInvalidParameterException(GovernanceProgramOMASAPIResponse response,
                                                  InvalidParameterException        error)
    {
        String  parameterName = error.getParameterName();

        if (parameterName != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("parameterName", parameterName);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureEmployeeNumberNotUniqueException(GovernanceProgramOMASAPIResponse response,
                                                         EmployeeNumberNotUniqueException error)
    {
        List<EntityDetail> duplicateProfiles = error.getDuplicateProfiles();

        if (duplicateProfiles != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("duplicateProfiles", duplicateProfiles);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureAppointmentIdNotUniqueException(GovernanceProgramOMASAPIResponse response,
                                                        AppointmentIdNotUniqueException  error)
    {
        List<EntityDetail> duplicatePosts = error.getDuplicatePosts();

        if (duplicatePosts != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("duplicatePosts", duplicatePosts);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void capturePropertyServerException(GovernanceProgramOMASAPIResponse     response,
                                                PropertyServerException              error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureUserNotAuthorizedException(GovernanceProgramOMASAPIResponse response,
                                                   UserNotAuthorizedException       error)
    {
        String  userId = error.getUserId();

        if (userId != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("userId", userId);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }
}
