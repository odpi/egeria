/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.organization.connector;

import org.odpi.openmetadata.accessservices.communityprofile.api.CommunityProfileEventListener;
import org.odpi.openmetadata.accessservices.communityprofile.client.CommunityProfileEventClient;
import org.odpi.openmetadata.accessservices.communityprofile.client.OrganizationManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.SecurityGroupManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.UserIdentityManagement;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;

import java.util.Date;
import java.util.List;

/**
 * OrganizationIntegratorContext provides a wrapper around the Community Profile OMAS client.
 * It provides the simplified interface to open metadata needed by the OrganizationIntegratorConnector.
 */
public class OrganizationIntegratorContext extends IntegrationContext
{
    private final OrganizationManagement      organizationClient;
    private final SecurityGroupManagement     securityGroupClient;
    private final UserIdentityManagement      userIdentityClient;
    private final CommunityProfileEventClient eventClient;


    private final AuditLog auditLog;


    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param governanceConfiguration client for managing catalog targets
     * @param openMetadataStoreClient client for calling the metadata server
     * @param actionControlInterface client for initiating governance actions
     * @param organizationManagement client for exchange requests
     * @param securityGroupManagement client for exchange requests
     * @param userIdentityManagement client for exchange requests
     * @param eventClient client for registered listeners
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param externalSourceGUID unique identifier of the software server capability for the asset manager
     * @param externalSourceName unique name of the software server capability for the asset manager
     * @param maxPageSize max number of elements that can be returned on a query
     * @param auditLog logging destination
     */
    public OrganizationIntegratorContext(String                       connectorId,
                                         String                       connectorName,
                                         String                       connectorUserId,
                                         String                       serverName,
                                         OpenIntegrationClient        openIntegrationClient,
                                         GovernanceConfiguration      governanceConfiguration,
                                         OpenMetadataClient           openMetadataStoreClient,
                                         ActionControlInterface       actionControlInterface,
                                         OrganizationManagement       organizationManagement,
                                         SecurityGroupManagement      securityGroupManagement,
                                         UserIdentityManagement       userIdentityManagement,
                                         CommunityProfileEventClient  eventClient,
                                         boolean                      generateIntegrationReport,
                                         PermittedSynchronization     permittedSynchronization,
                                         String                       integrationConnectorGUID,
                                         String                       externalSourceGUID,
                                         String                       externalSourceName,
                                         int                          maxPageSize,
                                         AuditLog                     auditLog)
    {
        super(connectorId,
              connectorName,
              connectorUserId,
              serverName,
              openIntegrationClient,
              governanceConfiguration,
              openMetadataStoreClient,
              actionControlInterface,
              generateIntegrationReport,
              permittedSynchronization,
              externalSourceGUID,
              externalSourceName,
              integrationConnectorGUID,
              auditLog,
              maxPageSize);

        this.organizationClient  = organizationManagement;
        this.securityGroupClient = securityGroupManagement;
        this.userIdentityClient  = userIdentityManagement;
        this.eventClient         = eventClient;
        this.auditLog            = auditLog;
    }



    /* ========================================================
     * Returning the external source name from the configuration
     */


    /**
     * Return the qualified name of the external source that is supplied in the configuration
     * document.
     *
     * @return string name
     */
    public String getExternalSourceName()
    {
        return externalSourceName;
    }


    /* ========================================================
     * Register for inbound events from the Community Profile OMAS OutTopic
     */

    /**
     * Register a listener object that will be passed each of the events published by the Community Profile OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(CommunityProfileEventListener listener) throws InvalidParameterException,
                                                                                ConnectionCheckedException,
                                                                                ConnectorCheckedException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /* ========================================================
     * Methods to update open metadata
     */

    /**
     * Create a definition of a actor profile.  This could be for the whole organization, a team, a person or a system.
     *
     * @param properties properties for a actor profile
     * @param contributionRecord optional properties for the contribution record
     *
     * @return unique identifier of actor profile
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createActorProfile(ActorProfileProperties properties,
                                     ContributionRecord     contributionRecord) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return organizationClient.createActorProfile(userId, externalSourceGUID, externalSourceName, properties, contributionRecord);
    }


    /**
     * Update the definition of an actor profile.
     *
     * @param actorProfileGUID unique identifier of actor profile
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     * @param contributionRecord optional properties for the contribution record
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateActorProfile(String                 actorProfileGUID,
                                   boolean                isMergeUpdate,
                                   ActorProfileProperties properties,
                                   ContributionRecord     contributionRecord) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        organizationClient.updateActorProfile(userId, externalSourceGUID, externalSourceName, actorProfileGUID, isMergeUpdate, properties, contributionRecord);
    }


    /**
     * Remove the definition of an actor profile.
     *
     * @param actorProfileGUID unique identifier of actor profile
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void deleteActorProfile(String actorProfileGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        organizationClient.deleteActorProfile(userId, externalSourceGUID, externalSourceName, actorProfileGUID);
    }


    /**
     * Add a new contact method to the profile.
     *
     * @param actorProfileGUID identifier of the profile to update.
     * @param properties properties of contact method.
     *
     * @return unique identifier (guid) for the new contact method.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addContactMethod(String                  actorProfileGUID,
                                   ContactMethodProperties properties) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        return organizationClient.addContactMethod(userId, externalSourceGUID, externalSourceName, actorProfileGUID, properties);
    }


    /**
     * Remove an obsolete contact method from the profile.
     *
     * @param contactMethodGUID unique identifier (guid) for the obsolete contact method.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteContactMethod(String contactMethodGUID) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        organizationClient.deleteContactMethod(userId, externalSourceGUID, externalSourceName, contactMethodGUID);
    }


    /**
     * Link two related team/organization actor profiles together as part of a hierarchy.
     * A team/organization actor profile can only have one parent but many child actor profiles.
     *
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
    public void linkTeamsInHierarchy(String  superTeamProfileGUID,
                                     String  subTeamProfileGUID,
                                     boolean delegationEscalationAuthority,
                                     Date    effectiveFrom,
                                     Date    effectiveTo) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        organizationClient.linkTeamsInHierarchy(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                superTeamProfileGUID,
                                                subTeamProfileGUID,
                                                delegationEscalationAuthority,
                                                effectiveFrom,
                                                effectiveTo);
    }


    /**
     * Remove the link between two actor profiles in the actor profile hierarchy.
     *
     * @param superTeamProfileGUID unique identifier of the parent actor profile
     * @param subTeamProfileGUID unique identifier of the child actor profile
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void unlinkTeamsInHierarchy(String superTeamProfileGUID,
                                       String subTeamProfileGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        organizationClient.unlinkTeamsInHierarchy(userId, externalSourceGUID, externalSourceName, superTeamProfileGUID, subTeamProfileGUID);
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param actorProfileGUID unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     * @throws InvalidParameterException actorProfileGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public ActorProfileElement getActorProfileByGUID(String actorProfileGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return organizationClient.getActorProfileByGUID(userId, actorProfileGUID);
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param actorProfileUserId unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     * @throws InvalidParameterException actorProfileUserId or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public ActorProfileElement getActorProfileByUserId(String actorProfileUserId) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return organizationClient.getActorProfileByUserId(userId, actorProfileUserId);
    }


    /**
     * Return information about matching named actor profiles.
     *
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
    public List<ActorProfileElement> getActorProfilesByName(String name,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return organizationClient.getActorProfilesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Return information about actor profiles associated with a location.
     *
     * @param locationGUID unique identifier or the location
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles
     *
     * @throws InvalidParameterException locationGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<ActorProfileElement> getActorProfilesByLocation(String locationGUID,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return organizationClient.getActorProfilesByLocation(userId, locationGUID, startFrom, pageSize);
    }


    /**
     * Return information about all actor profiles.
     *
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<ActorProfileElement> getActorProfiles(int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return organizationClient.getActorProfiles(userId, startFrom, pageSize);
    }


    /**
     * Retrieve the list of matching profiles for the search string.
     *
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
    public List<ActorProfileElement> findActorProfiles(String searchString,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        return organizationClient.findActorProfiles(userId, searchString, startFrom, pageSize);
    }


    /**
     * Create a definition of a person role.
     *
     * @param properties properties for a person role
     *
     * @return unique identifier of person role
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createPersonRole(PersonRoleProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return organizationClient.createPersonRole(userId, externalSourceGUID, externalSourceName, properties);
    }


    /**
     * Update the definition of a person role.
     *
     * @param personRoleGUID unique identifier of person role
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updatePersonRole(String               personRoleGUID,
                                 boolean              isMergeUpdate,
                                 PersonRoleProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        organizationClient.updatePersonRole(userId, externalSourceGUID, externalSourceName, personRoleGUID, isMergeUpdate, properties);
    }


    /**
     * Remove the definition of a person role.
     *
     * @param personRoleGUID unique identifier of person role
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void deletePersonRole(String personRoleGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        organizationClient.deletePersonRole(userId, externalSourceGUID, externalSourceName, personRoleGUID);
    }


    /**
     * Link a person role to a person profile to show that that person is performing the role.
     *
     * @param personRoleGUID unique identifier of the person role
     * @param personProfileGUID unique identifier of the person profile
     * @param properties           optional properties for the appointment
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void linkPersonRoleToProfile(String                personRoleGUID,
                                        String                personProfileGUID,
                                        AppointmentProperties properties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        organizationClient.linkPersonRoleToProfile(userId, externalSourceGUID, externalSourceName, personRoleGUID, personProfileGUID, properties);
    }


    /**
     * Return the list of people appointed to a particular role.
     *
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
    public List<Appointee> getAppointees(String personRoleGUID,
                                         Date   effectiveTime,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return organizationClient.getAppointees(userId, personRoleGUID, effectiveTime, startFrom, pageSize);
    }


    /**
     * Update the properties for the appointment of a person to a role.
     *
     * @param appointmentGUID unique identifier of the appointment relationship
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateAppointment(String                appointmentGUID,
                                  boolean               isMergeUpdate,
                                  AppointmentProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        organizationClient.updateAppointment(userId, externalSourceGUID, externalSourceName, appointmentGUID, isMergeUpdate, properties);
    }


    /**
     * Remove the link between a person role and a person profile.
     *
     * @param appointmentGUID unique identifier of the appointment relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void unlinkPersonRoleFromProfile(String appointmentGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        organizationClient.unlinkPersonRoleFromProfile(userId, externalSourceGUID, externalSourceName, appointmentGUID);
    }


    /**
     * Link a team leader person role or team member person role to a team profile.
     *
     * @param teamRoleGUID unique identifier of the person role
     * @param teamProfileGUID unique identifier of the team profile
     * @param position position name of the role in the team
     * @param leadershipPosition is this a leadership position
     *
     * @throws InvalidParameterException one of the guids is null or not known; the person role is not a team member or team leader
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void linkTeamPlayer(String  teamRoleGUID,
                               String  teamProfileGUID,
                               String  position,
                               boolean leadershipPosition) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        organizationClient.linkTeamPlayer(userId, externalSourceGUID, externalSourceName, teamRoleGUID, teamProfileGUID, position, leadershipPosition);
    }


    /**
     * Remove the link between a person role and a team profile.
     *
     * @param teamRoleGUID unique identifier of the person role
     * @param teamProfileGUID unique identifier of the team profile
     * @param leadershipPosition is this a leadership position
     *
     * @throws InvalidParameterException one of the guids is null or not known; the person role is not a team member or team leader
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void unlinkTeamPlayer(String  teamRoleGUID,
                                 String  teamProfileGUID,
                                 boolean leadershipPosition) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        organizationClient.unlinkTeamPlayer(userId, externalSourceGUID, externalSourceName, teamRoleGUID, teamProfileGUID, leadershipPosition);
    }


    /**
     * Return information about a specific person role.
     *
     * @param personRoleGUID unique identifier for the person role
     *
     * @return properties of the person role
     *
     * @throws InvalidParameterException personRoleGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public PersonRoleElement getPersonRoleByGUID(String personRoleGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return organizationClient.getPersonRoleByGUID(userId, personRoleGUID);
    }


    /**
     * Return information about a named person role.
     *
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
    public List<PersonRoleElement> getPersonRolesByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return organizationClient.getPersonRolesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Return information about the leadership person roles linked to a team.
     *
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
    public List<PersonRoleElement> getLeadershipRolesForTeam(String teamGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return organizationClient.getLeadershipRolesForTeam(userId, teamGUID, startFrom, pageSize);
    }


    /**
     * Return information about the membership person roles linked to a team.
     *
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
    public List<PersonRoleElement> getMembershipRolesForTeam(String teamGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return organizationClient.getMembershipRolesForTeam(userId, teamGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of matching roles for the search string.
     *
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
    public List<PersonRoleElement> findPersonRoles(String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        return organizationClient.findPersonRoles(userId, searchString, startFrom, pageSize);
    }


    /* ========================================
     * Security Groups
     */

    /**
     * Create a new security group.  The type of the definition is located in the properties.
     *
     * @param properties properties of the definition
     *
     * @return unique identifier of the definition
     *
     * @throws InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     * @throws PropertyServerException problem accessing the metadata service
     * @throws UserNotAuthorizedException security access problem
     */
    public String createSecurityGroup(SecurityGroupProperties properties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return securityGroupClient.createSecurityGroup(userId, properties);
    }


    /**
     * Update an existing security group.
     *
     * @param securityGroupGUID unique identifier of the definition to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties properties to update
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  updateSecurityGroup(String                  securityGroupGUID,
                                     boolean                 isMergeUpdate,
                                     SecurityGroupProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        securityGroupClient.updateSecurityGroup(userId, securityGroupGUID, isMergeUpdate, properties);
    }


    /**
     * Delete a specific security group.
     *
     * @param securityGroupGUID unique identifier of the definition to remove
     *
     * @throws InvalidParameterException guid is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  deleteSecurityGroup(String securityGroupGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        securityGroupClient.deleteSecurityGroup(userId, securityGroupGUID);
    }


    /**
     * Return the list of security groups associated with a unique distinguishedName.  In an ideal world, the should be only one.
     *
     * @param distinguishedName unique name of the security group
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of security groups
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    public List<SecurityGroupElement> getSecurityGroupsForDistinguishedName(String distinguishedName,
                                                                            int    startFrom,
                                                                            int    pageSize) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return securityGroupClient.getSecurityGroupsForDistinguishedName(userId, distinguishedName, startFrom, pageSize);
    }


    /**
     * Return the elements that are governed by the supplied security group.
     *
     * @param securityGroupGUID unique name of the security group
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of headers for the associated elements
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    public List<ElementStub> getElementsGovernedBySecurityGroup(String securityGroupGUID,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return securityGroupClient.getElementsGovernedBySecurityGroup(userId, securityGroupGUID, startFrom, pageSize);
    }


    /**
     * Return the list of security groups that match the search string - this can be a regular expression.
     *
     * @param searchString value to search for
     * @param startFrom where to start from in the list of definition results
     * @param pageSize max number of results to return in one call
     *
     * @return list of security groups
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    public List<SecurityGroupElement> findSecurityGroups(String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return securityGroupClient.findSecurityGroups(userId, searchString, startFrom, pageSize);
    }



    /* ========================================================
     * Manage user identities
     */

    /**
     * Create a UserIdentity.  This is not connected to a profile.
     *
      * @param newIdentity properties for the new userIdentity.
     *
     * @return unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createUserIdentity(UserIdentityProperties newIdentity) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return userIdentityClient.createUserIdentity(userId, externalSourceGUID, externalSourceName, newIdentity);
    }


    /**
     * Create a UserIdentity that is for the sole use of a specific actor profile.
     *
     * @param profileGUID unique identifier of the profile
     * @param newIdentity properties for the new userIdentity
     *
     * @return unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createUserIdentityForProfile(String                 profileGUID,
                                               UserIdentityProperties newIdentity) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        return userIdentityClient.createUserIdentityForProfile(userId, externalSourceGUID, externalSourceName, profileGUID, newIdentity);
    }


    /**
     * Update a UserIdentity.
     *
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param properties updated properties for the new userIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateUserIdentity(String                 userIdentityGUID,
                                   boolean                isMergeUpdate,
                                   UserIdentityProperties properties) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        userIdentityClient.updateUserIdentity(userId, externalSourceGUID, externalSourceName, userIdentityGUID, isMergeUpdate, properties);
    }


    /**
     * Remove a user identity object.  This will fail if the profile would be left without an
     * associated user identity.
     *
     * @param userIdentityGUID unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteUserIdentity(String userIdentityGUID) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        userIdentityClient.deleteUserIdentity(userId, externalSourceGUID, externalSourceName, userIdentityGUID);
    }


    /**
     * Link a user identity to a profile.  This will fail if the user identity is already connected to
     * a profile.
     *
     * @param profileGUID the profile to add the identity to.
     * @param userIdentityGUID additional userId for the profile.
     * @param properties the properties that describe how the owner of the profile uses the user identity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addIdentityToProfile(String                    userIdentityGUID,
                                     String                    profileGUID,
                                     ProfileIdentityProperties properties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        userIdentityClient.addIdentityToProfile(userId, externalSourceGUID, externalSourceName, userIdentityGUID, profileGUID, properties);
    }


    /**
     * Update the properties of the relationship between a user identity and profile.
     *
     * @param userIdentityGUID additional userId for the profile
     * @param profileGUID the profile to add the identity to
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param properties the properties that describe how the owner of the profile uses the user identity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateProfileIdentity(String                    userIdentityGUID,
                                      String                    profileGUID,
                                      boolean                   isMergeUpdate,
                                      ProfileIdentityProperties properties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        userIdentityClient.updateProfileIdentity(userId, externalSourceGUID, externalSourceName, userIdentityGUID, profileGUID, isMergeUpdate, properties);
    }


    /**
     * Unlink a user identity from a profile.
     *
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID profile to remove it from.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeIdentityFromProfile(String userIdentityGUID,
                                          String profileGUID) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        userIdentityClient.removeIdentityFromProfile(userId, externalSourceGUID, externalSourceName, userIdentityGUID, profileGUID);
    }


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<UserIdentityElement> findUserIdentities(String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return userIdentityClient.findUserIdentities(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of user identity metadata elements with a matching qualified name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<UserIdentityElement>  getUserIdentitiesByName(String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return userIdentityClient.getUserIdentitiesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the userIdentity metadata element with the supplied unique identifier.
     *
     * @param userIdentityGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public UserIdentityElement getUserIdentityByGUID(String userIdentityGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return userIdentityClient.getUserIdentityByGUID(userId, userIdentityGUID);
    }
}
