/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.fvt.profiles;


import org.odpi.openmetadata.accessservices.communityprofile.client.ActorProfileManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.ActorRoleManagement;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CreateProfileTest calls the ProfileManagerClient to create a database with schemas tables and columns
 * and then retrieve the results.
 */
public class CreateProfileTest
{
    private final static String testCaseName          = "CreateProfileTest";

    private final static int    maxPageSize           = 100;

    private final static String profile1QualifiedName = "TestProfile1 qualifiedName";
    private final static String profile1KnownName     = "TestProfile1 knownName";
    private final static String profile1FullName      = "TestProfile1 fullName";
    private final static String profile1Description   = "TestProfile1 description";
    private final static String profile1JobTitle      = "TestProfile1 jobTitle";
    private final static String profile1jobRole       = "TestProfile1 jobRoleDescription";
    private final static String profile1AddProp       = "TestProfile1 additionalProperty";

    private final static String profile2QualifiedName = "TestProfile2 qualifiedName";
    private final static String profile2KnownName     = "TestProfile2 knownName";
    private final static String profile2FullName      = "TestProfile2 fullName";
    private final static String profile2Description   = "TestProfile2 description";
    private final static String profile2JobTitle      = "TestProfile2 jobTitle";
    private final static String profile2jobRole       = "TestProfile2 jobRoleDescription";
    private final static String profile2AddProp       = "TestProfile2 additionalProperty";

    private final static String team1QualifiedName    = "Team1 qualifiedName";
    private final static String team1KnownName        = "Team1 knownName";
    private final static String team1Description      = "Team1 description";
    private final static String team1TeamType         = "Team1 teamType";
    private final static String team1Identifier       = "Team1 identifier";

    private final static String team2QualifiedName    = "Team2 qualifiedName";
    private final static String team2KnownName        = "Team2 knownName";
    private final static String team2Description      = "Team2 description";
    private final static String team2TeamType         = "Team2 teamType";
    private final static String team2Identifier       = "Team2 identifier";



    /**
     * Run all the defined tests and capture the results.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @return  results of running test
     */
    public static FVTResults performFVT(String   serverName,
                                        String   serverPlatformRootURL,
                                        String   userId)
    {
        FVTResults results = new FVTResults(testCaseName);

        results.incrementNumberOfTests();
        try
        {
            CreateProfileTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
            results.incrementNumberOfSuccesses();
        }
        catch (Exception error)
        {
            results.addCapturedError(error);
        }

        return results;
    }


    /**
     * Run all the tests in this class.
     *
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLogDestination logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private static void runIt(String                 serverPlatformRootURL,
                              String                 serverName,
                              String                 userId,
                              FVTAuditLogDestination auditLogDestination) throws FVTUnexpectedCondition
    {
        CreateProfileTest thisTest = new CreateProfileTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceWiki());

        ActorProfileManagement actorProfileManagement = thisTest.getActorProfileClient(serverName, serverPlatformRootURL, auditLog);
        ActorRoleManagement actorRoleManagement = thisTest.getActorRoleClient(serverName, serverPlatformRootURL, auditLog);

        thisTest.runOrganizationTest(actorProfileManagement, actorRoleManagement, userId);

    }


    private void runOrganizationTest(ActorProfileManagement actorProfileManagement,
                                     ActorRoleManagement    actorRoleManagement,
                                     String                 userId) throws FVTUnexpectedCondition
    {
        String activityName = "create persons";

        try
        {
            ActorProfileProperties actorProfileProperties = new ActorProfileProperties();
            actorProfileProperties.setTypeName(OpenMetadataType.PERSON.typeName);
            actorProfileProperties.setQualifiedName(profile1QualifiedName);
            actorProfileProperties.setKnownName(profile1KnownName);
            actorProfileProperties.setDescription(profile1Description);

            Map<String, Object> extendedProperties = new HashMap<>();
            extendedProperties.put(OpenMetadataProperty.FULL_NAME.name, profile1FullName);
            extendedProperties.put(OpenMetadataProperty.JOB_TITLE.name, profile1JobTitle);

            actorProfileProperties.setExtendedProperties(extendedProperties);

            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put("jobRole", profile1jobRole);
            additionalProperties.put("addProp", profile1AddProp);

            actorProfileProperties.setAdditionalProperties(additionalProperties);


            String profile1GUID = actorProfileManagement.createActorProfile(userId,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            true,
                                                                            null,
                                                                            actorProfileProperties,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            true,
                                                                            false,
                                                                            false,
                                                                            new Date());

            PersonProfileProperties personProfileProperties = new PersonProfileProperties();
            personProfileProperties.setTypeName(OpenMetadataType.PERSON.typeName);
            personProfileProperties.setQualifiedName(profile2QualifiedName);
            personProfileProperties.setKnownName(profile2KnownName);
            personProfileProperties.setDescription(profile2Description);
            personProfileProperties.setFullName(profile2FullName);
            personProfileProperties.setJobTitle(profile2JobTitle);

            additionalProperties = new HashMap<>();
            additionalProperties.put("jobRole", profile2jobRole);
            additionalProperties.put("addProp", profile2AddProp);

            personProfileProperties.setAdditionalProperties(additionalProperties);

            String profile2GUID = actorProfileManagement.createActorProfile(userId,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            true,
                                                                            null,
                                                                            personProfileProperties,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            true,
                                                                            false,
                                                                            false,
                                                                            new Date());

            actorProfileProperties = new ActorProfileProperties();
            actorProfileProperties.setTypeName(OpenMetadataType.ORGANIZATION.typeName);
            actorProfileProperties.setQualifiedName(team1QualifiedName);
            actorProfileProperties.setKnownName(team1KnownName);
            actorProfileProperties.setDescription(team1Description);

            extendedProperties = new HashMap<>();
            extendedProperties.put(OpenMetadataProperty.TEAM_TYPE.name, team1TeamType);
            extendedProperties.put(OpenMetadataProperty.IDENTIFIER.name, team1Identifier);
            actorProfileProperties.setExtendedProperties(extendedProperties);

            String team1GUID = actorProfileManagement.createActorProfile(userId,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         true,
                                                                         null,
                                                                         actorProfileProperties,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         true,
                                                                         false,
                                                                         false,
                                                                         new Date());

            TeamProfileProperties teamProfileProperties = new TeamProfileProperties();
            teamProfileProperties.setTypeName("Team");
            teamProfileProperties.setQualifiedName(team2QualifiedName);
            teamProfileProperties.setKnownName(team2KnownName);
            teamProfileProperties.setDescription(team2Description);
            teamProfileProperties.setTeamType(team2TeamType);
            teamProfileProperties.setIdentifier(team2Identifier);

            String team2GUID = actorProfileManagement.createActorProfile(userId,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         true,
                                                                         null,
                                                                         teamProfileProperties,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         true,
                                                                         false,
                                                                         false,
                                                                         new Date());

            actorProfileManagement.linkTeamStructure(userId,
                                                     null,
                                                     null,
                                                     team1GUID,
                                                     team2GUID,
                                                     null,
                                                     true,
                                                     false,
                                                     new Date());

            PersonRoleProperties personRoleProperties = new PersonRoleProperties();
            personRoleProperties.setTypeName(OpenMetadataType.PERSON_ROLE.typeName);
            personRoleProperties.setQualifiedName("TeamLeader:" + team2QualifiedName);
            personRoleProperties.setIdentifier("TeamLeader:" + team2Description);
            String team2LeaderGUID = actorRoleManagement.createActorRole(userId,
                                                                         null,
                                                                         null,
                                                                         team2GUID,
                                                                         false,
                                                                         team2GUID,
                                                                         personRoleProperties,
                                                                         team2GUID,
                                                                         OpenMetadataType.TEAM_LEADERSHIP_RELATIONSHIP.typeName,
                                                                         null,
                                                                         true,
                                                                         false,
                                                                         false,
                                                                         new Date());

            ActorRoleProperties actorRoleProperties = new ActorRoleProperties();
            actorRoleProperties.setTypeName(OpenMetadataType.PERSON_ROLE.typeName);
            actorRoleProperties.setQualifiedName("TeamMember:" + team2QualifiedName);
            actorRoleProperties.setIdentifier("TeamMember:" + team2Description);

            String team2MemberGUID = actorRoleManagement.createActorRole(userId,
                                                                         null,
                                                                         null,
                                                                         team2GUID,
                                                                         false,
                                                                         team2GUID,
                                                                         actorRoleProperties,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         true,
                                                                         false,
                                                                         false,
                                                                         new Date());

            actorProfileManagement.linkTeamToMembershipRole(userId,
                                                            null,
                                                            null,
                                                            team2GUID,
                                                            team2MemberGUID,
                                                            null,
                                                            false,
                                                            false,
                                                            new Date());

            PersonRoleAppointmentProperties appointmentProperties = new PersonRoleAppointmentProperties();
            appointmentProperties.setIsPublic(true);

            actorRoleManagement.linkPersonRoleToProfile(userId,
                                                        null,
                                                        null,
                                                        team2MemberGUID,
                                                        profile1GUID,
                                                        appointmentProperties,
                                                        false,
                                                        false,
                                                        new Date());
            actorRoleManagement.linkPersonRoleToProfile(userId,
                                                        null,
                                                        null,
                                                        team2MemberGUID,
                                                        profile2GUID,
                                                        appointmentProperties,
                                                        false,
                                                        false,
                                                        new Date());

            List<ActorProfileElement> actorProfileElements = actorProfileManagement.findActorProfiles(userId,
                                                                                                      ".*",
                                                                                                      TemplateFilter.ALL,
                                                                                                      null,
                                                                                                      null,
                                                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                                                      null,
                                                                                                      0,
                                                                                                      0,
                                                                                                      false,
                                                                                                      false,
                                                                                                      new Date());


        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create and return a personal profile manager client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private ActorProfileManagement getActorProfileClient(String   serverName,
                                                         String   serverPlatformRootURL,
                                                         AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getActorProfileClient";

        try
        {
            return new ActorProfileManagement(this.getClass().getName(),
                                              serverName,
                                              serverPlatformRootURL,
                                              auditLog,
                                              maxPageSize);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }

    /**
     * Create and return a personal profile manager client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private ActorRoleManagement getActorRoleClient(String   serverName,
                                                   String   serverPlatformRootURL,
                                                   AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getActorRoleClient";

        try
        {
            return new ActorRoleManagement(this.getClass().getName(),
                                           serverName,
                                           serverPlatformRootURL,
                                           auditLog,
                                           maxPageSize);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }

}
