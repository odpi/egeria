/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram;

import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The GovernanceLeadershipInterface is used to manage the appointment of the governance officers.
 * There are methods for maintaining the governance officer's appointments and linking them to
 * an individual's personal profile.
 */
public interface GovernanceLeadershipInterface
{

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
    String createGovernanceOfficer(String                     userId,
                                   GovernanceDomain           governanceDomain,
                                   String                     appointmentId,
                                   String                     appointmentContext,
                                   String                     title,
                                   Map<String, String>        additionalProperties,
                                   List<ExternalReference>    externalReferences)  throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException;


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
    void   updateGovernanceOfficer(String                     userId,
                                   String                     governanceOfficerGUID,
                                   GovernanceDomain           governanceDomain,
                                   String                     appointmentId,
                                   String                     appointmentContext,
                                   String                     title,
                                   Map<String, String>        additionalProperties,
                                   List<ExternalReference>    externalReferences)  throws UnrecognizedGUIDException,
                                                                                          InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException;


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
    void   deleteGovernanceOfficer(String              userId,
                                   String              governanceOfficerGUID,
                                   String              appointmentId,
                                   GovernanceDomain    governanceDomain) throws UnrecognizedGUIDException,
                                                                                InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException;


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
    GovernanceOfficer        getGovernanceOfficerByGUID(String     userId,
                                                        String     governanceOfficerGUID) throws UnrecognizedGUIDException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException;


    /**
     * Retrieve a governance officer by unique appointment id.
     *
     * @param userId the name of the calling user.
     * @param appointmentId  the unique appointment identifier of the governance officer.
     * @return governance officer object
     * @throws InvalidParameterException the appointmentId or governance domain is either null or invalid.
     * @throws AppointmentIdNotUniqueException more than one governance officer entity was retrieved for this appointmentId
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    GovernanceOfficer        getGovernanceOfficerByAppointmentId(String     userId,
                                                                 String     appointmentId) throws InvalidParameterException,
                                                                                                  AppointmentIdNotUniqueException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException;


    /**
     * Return all of the defined governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<GovernanceOfficer>  getGovernanceOfficers(String     userId) throws PropertyServerException,
                                                                             UserNotAuthorizedException;


    /**
     * Return all of the currently appointed governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<GovernanceOfficer>  getActiveGovernanceOfficers(String     userId) throws PropertyServerException,
                                                                                   UserNotAuthorizedException;


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
    List<GovernanceOfficer>  getGovernanceOfficersByDomain(String             userId,
                                                           GovernanceDomain   governanceDomain) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException;


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
    void appointGovernanceOfficer(String  userId,
                                  String  governanceOfficerGUID,
                                  String  profileGUID,
                                  Date    startDate) throws UnrecognizedGUIDException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;


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
    void relieveGovernanceOfficer(String  userId,
                                  String  governanceOfficerGUID,
                                  String  profileGUID,
                                  Date    endDate) throws UnrecognizedGUIDException,
                                                          InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;
}
