/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.accessservices.governanceprogram.admin.GovernanceProgramAdmin;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReference;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
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
    static private String                  accessServiceName   = null;
    static private OMRSRepositoryConnector repositoryConnector = null;

    private static final Logger log = LoggerFactory.getLogger(GovernanceProgramRESTServices.class);

    /**
     * Provide a connector to the REST Services.
     *
     * @param accessServiceName  name of this access service
     * @param repositoryConnector  OMRS Repository Connector to the property server.
     */
    static public void setRepositoryConnector(String                   accessServiceName,
                                              OMRSRepositoryConnector  repositoryConnector)
    {
        GovernanceProgramRESTServices.accessServiceName = accessServiceName;
        GovernanceProgramRESTServices.repositoryConnector = repositoryConnector;
    }


    /**
     * Default constructor
     */
    public GovernanceProgramRESTServices()
    {
        AccessServiceDescription   myDescription = AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS;
        AccessServiceRegistration  myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                                                                                  myDescription.getAccessServiceName(),
                                                                                  myDescription.getAccessServiceDescription(),
                                                                                  myDescription.getAccessServiceWiki(),
                                                                                  AccessServiceOperationalStatus.ENABLED,
                                                                                  GovernanceProgramAdmin.class.getName());
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
    }


    /**
     * Create a personal profile for an individual who is to be appointed to a governance role but does not
     * have a profile in open metadata.
     *
     * @param userId the name of the calling user.
     * @param requestBody properties about the individual.
     * @return Unique identifier for the personal profile or
     * InvalidParameterException the employee number or full name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GUIDResponse createPersonalProfile(String                     userId,
                                              PersonalDetailsRequestBody requestBody)
    {
        final String        methodName = "createPersonalProfile";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            String              employeeNumber = null;
            String              fullName = null;
            String              knownName = null;
            String              jobTitle = null;
            String              jobRoleDescription = null;
            Map<String, Object> additionalProperties = null;

            if (requestBody != null)
            {
                employeeNumber = requestBody.getEmployeeNumber();
                fullName = requestBody.getFullName();
                knownName = requestBody.getKnownName();
                jobTitle = requestBody.getJobTitle();
                jobRoleDescription = requestBody.getJobRoleDescription();
                additionalProperties = requestBody.getAdditionalProperties();
            }

            PersonalProfileHandler   handler = new PersonalProfileHandler(accessServiceName,
                                                                          repositoryConnector);

            response.setGUID(handler.createPersonalProfile(userId,
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
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param requestBody properties about the individual.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid or
     * InvalidParameterException the full name is null or the employeeNumber does not match the profileGUID or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse   updatePersonalProfile(String                     userId,
                                                String                     profileGUID,
                                                PersonalDetailsRequestBody requestBody)
    {
        final String        methodName = "updatePersonalProfile";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            String              employeeNumber = null;
            String              fullName = null;
            String              knownName = null;
            String              jobTitle = null;
            String              jobRoleDescription = null;
            Map<String, Object> additionalProperties = null;

            if (requestBody != null)
            {
                employeeNumber = requestBody.getEmployeeNumber();
                fullName = requestBody.getFullName();
                knownName = requestBody.getKnownName();
                jobTitle = requestBody.getJobTitle();
                jobRoleDescription = requestBody.getJobRoleDescription();
                additionalProperties = requestBody.getAdditionalProperties();
            }

            PersonalProfileHandler   handler = new PersonalProfileHandler(accessServiceName,
                                                                          repositoryConnector);

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
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param requestBody personnel/serial/unique employee number of the individual.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid or
     * InvalidParameterException the employee number or full name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse   deletePersonalProfile(String                              userId,
                                                String                              profileGUID,
                                                PersonalProfileValidatorRequestBody requestBody)
    {
        final String        methodName = "deletePersonalProfile";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            String              employeeNumber = null;

            if (requestBody != null)
            {
                employeeNumber = requestBody.getEmployeeNumber();
            }

            PersonalProfileHandler   handler = new PersonalProfileHandler(accessServiceName,
                                                                          repositoryConnector);

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
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @return personal profile object or
     * UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfileResponse getPersonalProfileByGUID(String        userId,
                                                            String        profileGUID)
    {
        final String        methodName = "getPersonalProfileByGUID";

        log.debug("Calling method: " + methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();

        try
        {
            this.validateInitialization(methodName);

            PersonalProfileHandler   handler = new PersonalProfileHandler(accessServiceName,
                                                                          repositoryConnector);

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
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @return personal profile object or
     * InvalidParameterException the employee number or full name is null or
     * EmployeeNumberNotUniqueException more than one personal profile was found or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfileResponse getPersonalProfileByEmployeeNumber(String         userId,
                                                                      String         employeeNumber)
    {
        final String        methodName = "getPersonalProfileByEmployeeNumber";

        log.debug("Calling method: " + methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();

        try
        {
            this.validateInitialization(methodName);

            PersonalProfileHandler   handler = new PersonalProfileHandler(accessServiceName,
                                                                          repositoryConnector);

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
     * @param userId the name of the calling user.
     * @param name name of individual.
     * @return list of personal profile objects or
     * InvalidParameterException the name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfileListResponse getPersonalProfilesByName(String        userId,
                                                                 String        name)
    {
        final String        methodName = "getPersonalProfilesByName";

        log.debug("Calling method: " + methodName);

        PersonalProfileListResponse response = new PersonalProfileListResponse();

        try
        {
            this.validateInitialization(methodName);

            PersonalProfileHandler   handler = new PersonalProfileHandler(accessServiceName,
                                                                          repositoryConnector);

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
     * @param userId the name of the calling user.
     * @param requestBody  properties of the governance officer.
     * @return Unique identifier (guid) of the governance officer or
     * InvalidParameterException the governance domain or appointment id is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GUIDResponse createGovernanceOfficer(String                               userId,
                                                GovernanceOfficerDetailsRequestBody  requestBody)

    {
        final String        methodName = "createGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();

        GovernanceDomain           governanceDomain = null;
        String                     appointmentId = null;
        String                     appointmentContext = null;
        String                     title = null;
        Map<String, Object>        additionalProperties = null;
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
            this.validateInitialization(methodName);

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(accessServiceName,
                                                                              repositoryConnector);

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
    public VoidResponse   updateGovernanceOfficer(String                               userId,
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
        Map<String, Object>        additionalProperties = null;
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
            this.validateInitialization(methodName);

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(accessServiceName,
                                                                              repositoryConnector);

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
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody  properties to verify this is the right governance officer
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * InvalidParameterException the appointmentId or governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse   deleteGovernanceOfficer(String                                 userId,
                                                  String                                 governanceOfficerGUID,
                                                  GovernanceOfficerValidatorRequestBody  requestBody)
    {
        final String        methodName = "deleteGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            GovernanceDomain    governanceDomain = null;
            String              appointmentId = null;

            if (requestBody != null)
            {
                governanceDomain = requestBody.getGovernanceDomain();
                appointmentId = requestBody.getAppointmentId();
            }

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(accessServiceName,
                                                                              repositoryConnector);

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
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @return governance officer object or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerResponse getGovernanceOfficerByGUID(String     userId,
                                                                String     governanceOfficerGUID)
    {
        final String        methodName = "getGovernanceOfficerByGUID";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerResponse response = new GovernanceOfficerResponse();

        try
        {
            this.validateInitialization(methodName);

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(accessServiceName,
                                                                              repositoryConnector);

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
     * @param userId the name of the calling user.
     * @param appointmentId  the unique appointment identifier of the governance officer.
     * @return governance officer object or
     * InvalidParameterException the appointmentId or governance domain is either null or invalid or
     * AppointmentIdNotUniqueException more than one governance officer entity was retrieved for this appointmentId
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerResponse   getGovernanceOfficerByAppointmentId(String     userId,
                                                                           String     appointmentId)
    {
        final String        methodName = "getGovernanceOfficerByAppointmentId";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerResponse response = new GovernanceOfficerResponse();

        try
        {
            this.validateInitialization(methodName);

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(accessServiceName,
                                                                              repositoryConnector);

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
     * @param userId the name of the calling user.
     * @return list of governance officer objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerListResponse  getGovernanceOfficers(String     userId)
    {
        final String        methodName = "getGovernanceOfficers";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerListResponse response = new GovernanceOfficerListResponse();

        try
        {
            this.validateInitialization(methodName);

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(accessServiceName,
                                                                              repositoryConnector);

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
     * @param userId the name of the calling user.
     * @return list of governance officer objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerListResponse  getActiveGovernanceOfficers(String     userId)
    {
        final String        methodName = "getActiveGovernanceOfficers";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerListResponse response = new GovernanceOfficerListResponse();

        try
        {
            this.validateInitialization(methodName);

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(accessServiceName,
                                                                              repositoryConnector);

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
     * @param userId the name of the calling user.
     * @param requestBody domain of interest.
     * @return list of governance officer objects or
     * InvalidParameterException the governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerListResponse  getGovernanceOfficersByDomain(String                        userId,
                                                                        GovernanceDomainRequestBody   requestBody)
    {
        final String        methodName = "getGovernanceOfficersByDomain";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerListResponse response = new GovernanceOfficerListResponse();


        try
        {
            this.validateInitialization(methodName);

            GovernanceDomain  governanceDomain = null;

            if (requestBody != null)
            {
                governanceDomain = requestBody.getGovernanceDomain();
            }

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(accessServiceName,
                                                                              repositoryConnector);

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
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody unique identifier for the profile
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse appointGovernanceOfficer(String                  userId,
                                                 String                  governanceOfficerGUID,
                                                 AppointmentRequestBody  requestBody)
    {
        final String        methodName = "appointGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            String              profileGUID = null;
            Date                startDate   = null;

            if (requestBody != null)
            {
                profileGUID = requestBody.getGUID();
                startDate   = requestBody.getEffectiveDate();
            }

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(accessServiceName,
                                                                              repositoryConnector);

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
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody unique identifier for the profile.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid or
     * InvalidParameterException the profile is not linked to this governance officer or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse relieveGovernanceOfficer(String                  userId,
                                                 String                  governanceOfficerGUID,
                                                 AppointmentRequestBody  requestBody)
    {
        final String        methodName = "relieveGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            String              profileGUID = null;
            Date                endDate     = null;

            if (requestBody != null)
            {
                profileGUID = requestBody.getGUID();
                endDate     = requestBody.getEffectiveDate();
            }

            GovernanceOfficerHandler   handler = new GovernanceOfficerHandler(accessServiceName,
                                                                              repositoryConnector);

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


    /**
     * Validate that this access service has been initialized before attempting to process a request.
     *
     * @param methodName  name of method called.
     * @throws PropertyServerException not initialized
     */
    private void validateInitialization(String  methodName) throws PropertyServerException
    {
        if (repositoryConnector == null)
        {
            GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.SERVICE_NOT_INITIALIZED;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          errorMessage,
                                                          errorCode.getSystemAction(),
                                                          errorCode.getUserAction());
        }
    }
}
