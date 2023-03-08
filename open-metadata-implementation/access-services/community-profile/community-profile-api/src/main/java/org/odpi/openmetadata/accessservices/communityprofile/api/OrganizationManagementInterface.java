/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.api;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.PersonRoleAppointee;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.PersonRoleElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ActorProfileProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AppointmentProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethodProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContributionRecord;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;


/**
 * OrganizationManagementInterface defines the client interface for setting up the profiles, roles and relationships for an organization.
 */
public interface OrganizationManagementInterface
{
    /**
     * Create a definition of an actor profile.  This could be for the whole organization, a team, a person or a system.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param properties          properties for a actor profile
     * @param contributionRecord optional properties for the contribution record
     *
     * @return unique identifier of actor profile
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String createActorProfile(String                 userId,
                              String                 externalSourceGUID,
                              String                 externalSourceName,
                              ActorProfileProperties properties,
                              ContributionRecord     contributionRecord) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Update the definition of an actor profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param actorProfileGUID unique identifier of actor profile
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     * @param contributionRecord additional properties for contribution record.
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateActorProfile(String                 userId,
                            String                 externalSourceGUID,
                            String                 externalSourceName,
                            String                 actorProfileGUID,
                            boolean                isMergeUpdate,
                            ActorProfileProperties properties,
                            ContributionRecord     contributionRecord) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Remove the definition of an actor profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param actorProfileGUID unique identifier of actor profile
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteActorProfile(String userId,
                            String actorProfileGUID,
                            String externalSourceGUID,
                            String externalSourceName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Add a new contact method to the profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param actorProfileGUID identifier of the profile to update.
     * @param properties properties of contact method.
     *
     * @return unique identifier (guid) for the new contact method.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addContactMethod(String                  userId,
                            String                  externalSourceGUID,
                            String                  externalSourceName,
                            String                  actorProfileGUID,
                            ContactMethodProperties properties) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;


    /**
     * Remove an obsolete contact method from the profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param contactMethodGUID unique identifier (guid) for the obsolete contact method.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteContactMethod(String            userId,
                             String            externalSourceGUID,
                             String            externalSourceName,
                             String            contactMethodGUID) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException;


    /**
     * Link two related team/organization actor profiles together as part of a hierarchy.
     * A team/organization actor profile can only have one parent but many child actor profiles.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param superTeamProfileGUID unique identifier of the parent team profile
     * @param subTeamProfileGUID unique identifier of the child team profile
     * @param delegationEscalationAuthority can workflows delegate/escalate through this link?
     * @param effectiveFrom start date for the team relationship
     * @param effectiveTo end date for the team relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void linkTeamsInHierarchy(String  userId,
                              String  externalSourceGUID,
                              String  externalSourceName,
                              String  superTeamProfileGUID,
                              String  subTeamProfileGUID,
                              boolean delegationEscalationAuthority,
                              Date    effectiveFrom,
                              Date    effectiveTo) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Remove the link between two actor profiles in the actor profile hierarchy.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param superTeamProfileGUID unique identifier of the parent actor profile
     * @param subTeamProfileGUID unique identifier of the child actor profile
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void unlinkTeamsInHierarchy(String userId,
                                String externalSourceGUID,
                                String externalSourceName,
                                String superTeamProfileGUID,
                                String subTeamProfileGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Return information about a specific actor profile.
     *
     * @param userId calling user
     * @param actorProfileGUID unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     * @throws InvalidParameterException actorProfileGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    ActorProfileElement getActorProfileByGUID(String userId,
                                              String actorProfileGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Return information about a specific actor profile.
     *
     * @param userId calling user
     * @param actorProfileUserId unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     * @throws InvalidParameterException actorProfileUserId or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    ActorProfileElement getActorProfileByUserId(String userId,
                                                String actorProfileUserId) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Return information about matching named actor profiles.
     *
     * @param userId calling user
     * @param name unique name for the actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ActorProfileElement> getActorProfilesByName(String userId,
                                                     String name,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;


    /**
     * Return information about all actor profiles.
     *
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ActorProfileElement> getActorProfiles(String userId,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Return information about the actor profiles associated with a location.
     *
     * @param userId calling user
     * @param locationGUID unique locationGUID for the actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles
     *
     * @throws InvalidParameterException locationGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ActorProfileElement> getActorProfilesByLocation(String userId,
                                                         String locationGUID,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Retrieve the list of matching profiles for the search string.
     *
     * @param userId the name of the calling user.
     * @param searchString RegEx string to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<ActorProfileElement> findActorProfiles(String userId,
                                                String searchString,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;


    /**
     * Create a definition of a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param properties properties for a person role
     *
     * @return unique identifier of person role
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String createPersonRole(String               userId,
                            String               externalSourceGUID,
                            String               externalSourceName,
                            PersonRoleProperties properties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Update the definition of a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param personRoleGUID unique identifier of person role
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updatePersonRole(String               userId,
                          String               externalSourceGUID,
                          String               externalSourceName,
                          String               personRoleGUID,
                          boolean              isMergeUpdate,
                          PersonRoleProperties properties) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Remove the definition of a person role.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param personRoleGUID unique identifier of person role
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deletePersonRole(String userId,
                          String externalSourceGUID,
                          String externalSourceName,
                          String personRoleGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Link a person role to a person profile to show that that person is performing the role.
     *
     * @param userId               calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID    unique identifier of the person profile
     * @param properties           optional properties for the appointment
     *
     * @return unique identifier of the appointment
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String linkPersonRoleToProfile(String                userId,
                                   String                externalSourceGUID,
                                   String                externalSourceName,
                                   String                personRoleGUID,
                                   String                personProfileGUID,
                                   AppointmentProperties properties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Return the list of people appointed to a particular role.
     *
     * @param userId               calling user
     * @param personRoleGUID       unique identifier of the person role
     * @param effectiveTime        time for appointments, null for full appointment history
     * @param startFrom            index of the list to start from (0 for start)
     * @param pageSize             maximum number of elements to return
     *
     * @return list of appointees
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<PersonRoleAppointee> getAppointees(String userId,
                                            String personRoleGUID,
                                            Date   effectiveTime,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Update the properties for the appointment of a person to a role.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param appointmentGUID unique identifier of the appointment relationship
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateAppointment(String                userId,
                           String                externalSourceGUID,
                           String                externalSourceName,
                           String                appointmentGUID,
                           boolean               isMergeUpdate,
                           AppointmentProperties properties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Remove the link between a person role and a person profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param appointmentGUID unique identifier of the appointment relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void unlinkPersonRoleFromProfile(String userId,
                                     String externalSourceGUID,
                                     String externalSourceName,
                                     String appointmentGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Link a team leader person role or team member person role to a team profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param teamRoleGUID unique identifier of the person role
     * @param teamProfileGUID unique identifier of the team profile
     * @param position position name of the role in the team
     * @param leadershipPosition is this a leadership position?
     *
     *
     * @throws InvalidParameterException one of the guids is null or not known; the person role is not a team member or team leader
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void linkTeamPlayer(String  userId,
                        String  externalSourceGUID,
                        String  externalSourceName,
                        String  teamRoleGUID,
                        String  teamProfileGUID,
                        String  position,
                        boolean leadershipPosition) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Remove the link between a person role and a team profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param teamRoleGUID unique identifier of the person role
     * @param teamProfileGUID unique identifier of the team profile
     * @param leadershipPosition is this a leadership position?
     *
     * @throws InvalidParameterException one of the guids is null or not known; the person role is not a team member or team leader
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void unlinkTeamPlayer(String  userId,
                          String  externalSourceGUID,
                          String  externalSourceName,
                          String  teamRoleGUID,
                          String  teamProfileGUID,
                          boolean leadershipPosition) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Return information about a specific person role.
     *
     * @param userId calling user
     * @param personRoleGUID unique identifier for the person role
     *
     * @return properties of the person role
     *
     * @throws InvalidParameterException personRoleGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    PersonRoleElement getPersonRoleByGUID(String userId,
                                          String personRoleGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Return information about a named person role.
     *
     * @param userId calling user
     * @param name unique name for the actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<PersonRoleElement> getPersonRolesByName(String userId,
                                                 String name,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Return information about the leadership person roles linked to a team.
     *
     * @param userId calling user
     * @param teamGUID unique identifier for the Team actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<PersonRoleElement> getLeadershipRolesForTeam(String userId,
                                                      String teamGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Return information about the membership person roles linked to a team.
     *
     * @param userId calling user
     * @param teamGUID unique identifier for the Team actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<PersonRoleElement> getMembershipRolesForTeam(String userId,
                                                      String teamGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Retrieve the list of matching roles for the search string.
     *
     * @param userId the name of the calling user.
     * @param searchString RegEx string to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<PersonRoleElement> findPersonRoles(String userId,
                                            String searchString,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException;
}
