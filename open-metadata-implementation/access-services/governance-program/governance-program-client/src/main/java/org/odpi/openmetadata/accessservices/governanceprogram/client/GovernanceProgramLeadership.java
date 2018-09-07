/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.client;


import org.odpi.openmetadata.accessservices.governanceprogram.GovernanceLeadershipInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReference;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceOfficer;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.PersonalProfile;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceProgramLeadership provides the client-side interface for the Governance Program Open Metadata Access Service (OMAS).
 * This client, manages all of the interaction with an open metadata repository.  It is initialized with the URL
 * of the server that is running the Open Metadata Access Services.  This server is responsible for locating and
 * managing the governance program definitions exchanged with this client.
 */
public class GovernanceProgramLeadership implements GovernanceLeadershipInterface
{
    private static final String  personalProfileTypeName = "Person";
    private static final String  governanceOfficerTypeName = "GovernanceOfficer";


    private String                            omasServerURL;  /* Initialized in constructor */
    private GovernanceProgramExceptionHandler exceptionHandler; /* Initialized in constructor */

    /**
     * Create a new GovernanceProgramLeadership client.
     *
     * @param newServerURL - the network address of the server running the OMAS REST services
     */
    public GovernanceProgramLeadership(String     newServerURL)
    {
        omasServerURL = newServerURL;
        exceptionHandler = new GovernanceProgramExceptionHandler(newServerURL);
    }


    /**
     * Create a personal profile for an individual who is to be appointed to a governance role but does not
     * have a profile in open metadata.
     *
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     * @return Unique identifier for the personal profile.
     * @throws InvalidParameterException the employee number or known name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public String createPersonalProfile(String              userId,
                                        String              employeeNumber,
                                        String              fullName,
                                        String              knownName,
                                        String              jobTitle,
                                        String              jobRoleDescription,
                                        Map<String, Object> additionalProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String   methodName = "createPersonalProfile";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/personal-profiles";

        final String   employeeNumberParameterName = "employeeNumber";
        final String   knownNameParameterName = "knownName";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);
        exceptionHandler.validateName(knownName, knownNameParameterName, methodName);

        PersonalDetailsRequestBody  requestBody = new PersonalDetailsRequestBody();
        requestBody.setEmployeeNumber(employeeNumber);
        requestBody.setFullName(fullName);
        requestBody.setKnownName(knownName);
        requestBody.setJobTitle(jobTitle);
        requestBody.setJobRoleDescription(jobRoleDescription);
        requestBody.setAdditionalProperties(additionalProperties);


        GUIDResponse restResult = callGUIDPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Update properties for the personal properties.  Null values result in empty fields in the profile.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param employeeNumber personnel/serial/unique employee number of the individual. Used to verify the profileGUID.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     * @throws UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid.
     * @throws InvalidParameterException the known name is null or the employeeNumber does not match the profileGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   updatePersonalProfile(String              userId,
                                        String              profileGUID,
                                        String              employeeNumber,
                                        String              fullName,
                                        String              knownName,
                                        String              jobTitle,
                                        String              jobRoleDescription,
                                        Map<String, Object> additionalProperties) throws UnrecognizedGUIDException,
                                                                                         InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String   methodName = "updatePersonalProfile";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/personal-profiles/{1}";

        final String   guidParameterName = "profileGUID";
        final String   employeeNumberParameterName = "employeeNumber";
        final String   knownNameParameterName = "knownName";


        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateGUID(profileGUID, guidParameterName, personalProfileTypeName, methodName);
        exceptionHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);
        exceptionHandler.validateName(knownName, knownNameParameterName, methodName);

        PersonalDetailsRequestBody  requestBody = new PersonalDetailsRequestBody();
        requestBody.setEmployeeNumber(employeeNumber);
        requestBody.setFullName(fullName);
        requestBody.setKnownName(knownName);
        requestBody.setJobTitle(jobTitle);
        requestBody.setJobRoleDescription(jobRoleDescription);
        requestBody.setAdditionalProperties(additionalProperties);


        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       userId,
                                                       profileGUID);

        exceptionHandler.detectAndThrowUnrecognizedGUIDException(methodName, restResult);
        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Delete the personal profile.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @throws UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid.
     * @throws InvalidParameterException the employee number or full name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   deletePersonalProfile(String              userId,
                                        String              profileGUID,
                                        String              employeeNumber) throws UnrecognizedGUIDException,
                                                                                   InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String   methodName = "deletePersonalProfile";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/personal-profiles/{1}/delete";

        final String   guidParameterName = "profileGUID";
        final String   employeeNumberParameterName = "employeeNumber";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateGUID(profileGUID, guidParameterName, personalProfileTypeName, methodName);
        exceptionHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);

        PersonalProfileValidatorRequestBody  requestBody = new PersonalProfileValidatorRequestBody();
        requestBody.setEmployeeNumber(employeeNumber);

        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       userId,
                                                       profileGUID);

        exceptionHandler.detectAndThrowUnrecognizedGUIDException(methodName, restResult);
        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Retrieve a personal profile by guid.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @return personal profile object.
     * @throws UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfile getPersonalProfileByGUID(String        userId,
                                                    String        profileGUID) throws UnrecognizedGUIDException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileByGUID";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/personal-profiles/{1}";

        final String   guidParameterName = "profileGUID";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateGUID(profileGUID, guidParameterName, personalProfileTypeName, methodName);

        PersonalProfileResponse restResult = callPersonalProfileGetRESTCall(methodName,
                                                                            omasServerURL + urlTemplate,
                                                                            userId,
                                                                            profileGUID);

        exceptionHandler.detectAndThrowUnrecognizedGUIDException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getPersonalProfile();
    }


    /**
     * Retrieve a personal profile by personnel/serial/unique employee number of the individual.
     *
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @return personal profile object.
     * @throws InvalidParameterException the employee number is null.
     * @throws EmployeeNumberNotUniqueException more than one personal profile was found.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfile getPersonalProfileByEmployeeNumber(String         userId,
                                                              String         employeeNumber) throws InvalidParameterException,
                                                                                                    EmployeeNumberNotUniqueException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileByEmployeeNumber";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/personal-profiles/by-employee-number/{1}";

        final String   employeeNumberParameterName = "employeeNumber";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);

        PersonalProfileResponse restResult = callPersonalProfileGetRESTCall(methodName,
                                                                            omasServerURL + urlTemplate,
                                                                            userId,
                                                                            employeeNumber);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowEmployeeNumberNotUniqueException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getPersonalProfile();
    }


    /**
     * Return a list of candidate personal profiles for an individual.  It matches on full name and known name.
     * The name may include wild card parameters.
     *
     * @param userId the name of the calling user.
     * @param name name of individual.
     * @return list of personal profile objects.
     * @throws InvalidParameterException the name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<PersonalProfile> getPersonalProfilesByName(String        userId,
                                                           String        name) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfilesByName";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/personal-profiles/by-name/{1}";

        final String   nameParameterName = "name";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateName(name, nameParameterName, methodName);

        PersonalProfileListResponse restResult = callPersonalProfileListGetRESTCall(methodName,
                                                                                    omasServerURL + urlTemplate,
                                                                                    userId,
                                                                                    name);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getPersonalProfiles();
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
    public String createGovernanceOfficer(String                     userId,
                                          GovernanceDomain           governanceDomain,
                                          String                     appointmentId,
                                          String                     appointmentContext,
                                          String                     title,
                                          Map<String, Object>        additionalProperties,
                                          List<ExternalReference>    externalReferences) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String   methodName = "createGovernanceOfficer";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/governance-officers";

        final String   appointmentIdParameterName = "appointmentId";
        final String   titleParameterName = "title";
        final String   governanceDomainParameterName = "governanceDomain";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        exceptionHandler.validateName(title, titleParameterName, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceOfficerDetailsRequestBody  requestBody = new GovernanceOfficerDetailsRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);
        requestBody.setAppointmentId(appointmentId);
        requestBody.setAppointmentContext(appointmentContext);
        requestBody.setTitle(title);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExternalReferences(externalReferences);

        GUIDResponse restResult = callGUIDPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

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
     * @throws UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid.
     * @throws InvalidParameterException the title is null or the governanceDomain/appointmentId does not match the
     *                                   the existing values associated with the governanceOfficerGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   updateGovernanceOfficer(String                     userId,
                                          String                     governanceOfficerGUID,
                                          GovernanceDomain           governanceDomain,
                                          String                     appointmentId,
                                          String                     appointmentContext,
                                          String                     title,
                                          Map<String, Object>        additionalProperties,
                                          List<ExternalReference>    externalReferences)  throws UnrecognizedGUIDException,
                                                                                                 InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String   methodName = "updateGovernanceOfficer";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/governance-officers/{1}";

        final String   guidParameterName = "governanceOfficerGUID";
        final String   appointmentIdParameterName = "appointmentId";
        final String   titleParameterName = "title";
        final String   governanceDomainParameterName = "governanceDomain";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateGUID(governanceOfficerGUID, guidParameterName, governanceOfficerTypeName, methodName);
        exceptionHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        exceptionHandler.validateName(title, titleParameterName, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceOfficerDetailsRequestBody  requestBody = new GovernanceOfficerDetailsRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);
        requestBody.setAppointmentId(appointmentId);
        requestBody.setAppointmentContext(appointmentContext);
        requestBody.setTitle(title);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExternalReferences(externalReferences);

        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       userId,
                                                       governanceOfficerGUID);

        exceptionHandler.detectAndThrowUnrecognizedGUIDException(methodName, restResult);
        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Remove the requested governance officer.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param governanceDomain  the governance domain for the governance officer.
     * @throws UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid.
     * @throws InvalidParameterException the appointmentId or governance domain is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   deleteGovernanceOfficer(String              userId,
                                          String              governanceOfficerGUID,
                                          String              appointmentId,
                                          GovernanceDomain    governanceDomain) throws UnrecognizedGUIDException,
                                                                                       InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String   methodName = "deleteGovernanceOfficer";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/governance-officers/{1}/delete";

        final String   guidParameterName = "governanceOfficerGUID";
        final String   appointmentIdParameterName = "appointmentId";
        final String   governanceDomainParameterName = "governanceDomain";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateGUID(governanceOfficerGUID, guidParameterName, governanceOfficerTypeName, methodName);
        exceptionHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceOfficerValidatorRequestBody  requestBody = new GovernanceOfficerValidatorRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);
        requestBody.setAppointmentId(appointmentId);

        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       userId,
                                                       governanceOfficerGUID);

        exceptionHandler.detectAndThrowUnrecognizedGUIDException(methodName, restResult);
        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Retrieve a governance officer description by unique guid.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @return governance officer object
     * @throws UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficer getGovernanceOfficerByGUID(String     userId,
                                                        String     governanceOfficerGUID) throws UnrecognizedGUIDException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficerByGUID";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/governance-officers/{1}";

        final String   guidParameterName = "governanceOfficerGUID";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateGUID(governanceOfficerGUID, guidParameterName, governanceOfficerTypeName, methodName);

        GovernanceOfficerResponse restResult = callGovernanceOfficerGetRESTCall(methodName,
                                                                                omasServerURL + urlTemplate,
                                                                                userId,
                                                                                governanceOfficerGUID);

        exceptionHandler.detectAndThrowUnrecognizedGUIDException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

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
    public GovernanceOfficer        getGovernanceOfficerByAppointmentId(String     userId,
                                                                        String     appointmentId) throws InvalidParameterException,
                                                                                                         AppointmentIdNotUniqueException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficerByAppointmentId";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/governance-officers/by-appointment-id/{1}";

        final String   appointmentIdParameterName = "appointmentId";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateName(appointmentId, appointmentIdParameterName, methodName);

        GovernanceOfficerResponse restResult = callGovernanceOfficerGetRESTCall(methodName,
                                                                                omasServerURL + urlTemplate,
                                                                                userId,
                                                                                appointmentId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowAppointmentIdNotUniqueException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGovernanceOfficer();
    }


    /**
     * Return all of the defined governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<GovernanceOfficer>  getGovernanceOfficers(String     userId) throws PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficers";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/governance-officers";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);

        GovernanceOfficerListResponse restResult = callGovernanceOfficerListGetRESTCall(methodName,
                                                                                        omasServerURL + urlTemplate,
                                                                                        userId);

        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGovernanceOfficers();
    }


    /**
     * Return all of the currently appointed governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<GovernanceOfficer>  getActiveGovernanceOfficers(String     userId) throws PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficers";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/governance-officers/active";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);

        GovernanceOfficerListResponse restResult = callGovernanceOfficerListGetRESTCall(methodName,
                                                                                        omasServerURL + urlTemplate,
                                                                                        userId);

        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

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
    public List<GovernanceOfficer>  getGovernanceOfficersByDomain(String             userId,
                                                                  GovernanceDomain   governanceDomain) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficersByDomain";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/governance-officers/by-domain";

        final String   governanceDomainParameterName = "governanceDomain";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceDomainRequestBody  requestBody = new GovernanceDomainRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);

        GovernanceOfficerListResponse restResult = callGovernanceOfficerListPostRESTCall(methodName,
                                                                                        omasServerURL + urlTemplate,
                                                                                         requestBody,
                                                                                         userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGovernanceOfficers();
    }


    /**
     * Link a person to a governance officer.  Only one person may be appointed at any one time.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param profileGUID unique identifier for the profile.
     * @param startDate the official start date of the appointment - null means effective immediately.
     * @throws UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void appointGovernanceOfficer(String  userId,
                                         String  governanceOfficerGUID,
                                         String  profileGUID,
                                         Date    startDate) throws UnrecognizedGUIDException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String   methodName = "appointGovernanceOfficer";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/governance-officers/{1}/appoint";

        final String   governanceOfficerGUIDParameterName = "governanceOfficerGUID";
        final String   profileGUIDParameterName = "profileGUID";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, governanceOfficerTypeName, methodName);
        exceptionHandler.validateGUID(profileGUID, profileGUIDParameterName, personalProfileTypeName, methodName);

        AppointmentRequestBody  requestBody = new AppointmentRequestBody();
        requestBody.setGUID(profileGUID);
        requestBody.setEffectiveDate(startDate);

        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       userId,
                                                       governanceOfficerGUID);

        exceptionHandler.detectAndThrowUnrecognizedGUIDException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Unlink a person from a governance officer appointment.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param profileGUID unique identifier for the profile.
     * @param endDate the official end of the appointment - null means effective immediately.
     * @throws UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid.
     * @throws InvalidParameterException the profile is not linked to this governance officer.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void relieveGovernanceOfficer(String  userId,
                                         String  governanceOfficerGUID,
                                         String  profileGUID,
                                         Date    endDate) throws UnrecognizedGUIDException,
                                                                 InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName = "relieveGovernanceOfficer";
        final String   urlTemplate = "/open-metadata/access-services/governance-program/users/{0}/leadership/governance-officers/{1}/relieve";

        final String   governanceOfficerGUIDParameterName = "governanceOfficerGUID";
        final String   profileGUIDParameterName = "profileGUID";

        exceptionHandler.validateOMASServerURL(methodName);
        exceptionHandler.validateUserId(userId, methodName);
        exceptionHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, governanceOfficerTypeName, methodName);
        exceptionHandler.validateGUID(profileGUID, profileGUIDParameterName, personalProfileTypeName, methodName);

        AppointmentRequestBody  requestBody = new AppointmentRequestBody();
        requestBody.setGUID(profileGUID);
        requestBody.setEffectiveDate(endDate);

        VoidResponse restResult = callVoidPostRESTCall(methodName,
                                                       omasServerURL + urlTemplate,
                                                       requestBody,
                                                       userId,
                                                       governanceOfficerGUID);

        exceptionHandler.detectAndThrowUnrecognizedGUIDException(methodName, restResult);
        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /*
     * ===================================
     * REST methods
     * ===================================
     */


    /**
     * Issue a POST REST call that returns a VoidResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body contains the rest of the parameters packaged as a single object
     * @param params  a list of parameters that are slotted into the url template
     * @return VoidResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private VoidResponse callVoidPostRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object    requestBody,
                                              Object... params) throws PropertyServerException
    {
        VoidResponse restResult = new VoidResponse();

        try
        {
            RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.postForObject(urlTemplate, requestBody, restResult.getClass(), params);
        }
        catch (Throwable error)
        {
            exceptionHandler.handleRESTRequestFailure(methodName, error);
        }

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a guid object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body contains the rest of the parameters packaged as a single object
     * @param params  a list of parameters that are slotted into the url template
     * @return GUIDResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private GUIDResponse callGUIDPostRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object    requestBody,
                                              Object... params) throws PropertyServerException
    {
        GUIDResponse restResult = new GUIDResponse();

        try
        {
            RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.postForObject(urlTemplate, requestBody, restResult.getClass(), params);
        }
        catch (Throwable error)
        {
            exceptionHandler.handleRESTRequestFailure(methodName, error);
        }

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of Governance Officer objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body contains the rest of the parameters packaged as a single object
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceOfficerListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private GovernanceOfficerListResponse callGovernanceOfficerListPostRESTCall(String    methodName,
                                                                                String    urlTemplate,
                                                                                Object    requestBody,
                                                                                Object... params) throws PropertyServerException
    {
        GovernanceOfficerListResponse restResult = new GovernanceOfficerListResponse();

        try
        {
            RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.postForObject(urlTemplate, requestBody, restResult.getClass(), params);
        }
        catch (Throwable error)
        {
            exceptionHandler.handleRESTRequestFailure(methodName, error);
        }

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a PersonalProfile object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return PersonalProfileResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private PersonalProfileResponse callPersonalProfileGetRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object... params) throws PropertyServerException
    {
        PersonalProfileResponse restResult = new PersonalProfileResponse();

        /*
         * Issue the request
         */
        try
        {
            RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.getForObject(urlTemplate, restResult.getClass(), params);
        }
        catch (Throwable error)
        {
            exceptionHandler.handleRESTRequestFailure(methodName, error);
        }

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of PersonalProfile objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return PersonalProfileListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private PersonalProfileListResponse callPersonalProfileListGetRESTCall(String    methodName,
                                                                           String    urlTemplate,
                                                                           Object... params) throws PropertyServerException
    {
        PersonalProfileListResponse restResult = new PersonalProfileListResponse();

        /*
         * Issue the request
         */
        try
        {
            RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.getForObject(urlTemplate, restResult.getClass(), params);
        }
        catch (Throwable error)
        {
            exceptionHandler.handleRESTRequestFailure(methodName, error);
        }

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceOfficer object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceOfficerResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private GovernanceOfficerResponse callGovernanceOfficerGetRESTCall(String    methodName,
                                                                       String    urlTemplate,
                                                                       Object... params) throws PropertyServerException
    {
        GovernanceOfficerResponse restResult = new GovernanceOfficerResponse();

        /*
         * Issue the request
         */
        try
        {
            RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.getForObject(urlTemplate, restResult.getClass(), params);
        }
        catch (Throwable error)
        {
            exceptionHandler.handleRESTRequestFailure(methodName, error);
        }

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list GovernanceOfficer objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceOfficerListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private GovernanceOfficerListResponse callGovernanceOfficerListGetRESTCall(String    methodName,
                                                                               String    urlTemplate,
                                                                               Object... params) throws PropertyServerException
    {
        GovernanceOfficerListResponse restResult = new GovernanceOfficerListResponse();

        try
        {
            RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.getForObject(urlTemplate, restResult.getClass(), params);
        }
        catch (Throwable error)
        {
            exceptionHandler.handleRESTRequestFailure(methodName, error);
        }

        return restResult;
    }
}
