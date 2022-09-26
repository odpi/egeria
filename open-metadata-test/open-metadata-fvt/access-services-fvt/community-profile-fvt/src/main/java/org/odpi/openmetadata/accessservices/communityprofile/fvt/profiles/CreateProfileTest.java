/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.fvt.profiles;


import org.odpi.openmetadata.accessservices.communityprofile.client.OrganizationManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ActorProfileProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AppointmentProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonRoleProperties;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

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

        OrganizationManagement organizationManagement = thisTest.getOrganizationClient(serverName, serverPlatformRootURL, auditLog);

        thisTest.runOrganizationTest(organizationManagement, userId);

    }


    private void runOrganizationTest(OrganizationManagement client,
                                     String                 userId) throws FVTUnexpectedCondition
    {
        String activityName = "create persons";

        try
        {
            ActorProfileProperties profileProperties = new ActorProfileProperties();
            profileProperties.setTypeName("Person");
            profileProperties.setQualifiedName(profile1QualifiedName);
            profileProperties.setKnownName(profile1KnownName);
            profileProperties.setDescription(profile1Description);

            Map<String, Object> extendedProperties = new HashMap<>();
            extendedProperties.put("fullName", profile1FullName);
            extendedProperties.put("jobTitle", profile1JobTitle);

            profileProperties.setExtendedProperties(extendedProperties);

            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put("jobRole", profile1jobRole);
            additionalProperties.put("addProp", profile1AddProp);

            profileProperties.setAdditionalProperties(additionalProperties);


            String profile1GUID = client.createActorProfile(userId, null, null, profileProperties, null);

            profileProperties = new ActorProfileProperties();
            profileProperties.setTypeName("Person");
            profileProperties.setQualifiedName(profile2QualifiedName);
            profileProperties.setKnownName(profile2KnownName);
            profileProperties.setDescription(profile2Description);

            extendedProperties = new HashMap<>();
            extendedProperties.put("fullName", profile2FullName);
            extendedProperties.put("jobTitle", profile2JobTitle);

            profileProperties.setExtendedProperties(extendedProperties);

            additionalProperties = new HashMap<>();
            additionalProperties.put("jobRole", profile2jobRole);
            additionalProperties.put("addProp", profile2AddProp);

            profileProperties.setAdditionalProperties(additionalProperties);

            String profile2GUID = client.createActorProfile(userId, null, null, profileProperties, null);

            profileProperties = new ActorProfileProperties();
            profileProperties.setTypeName("Organization");
            profileProperties.setQualifiedName(team1QualifiedName);
            profileProperties.setKnownName(team1KnownName);
            profileProperties.setDescription(team1Description);

            extendedProperties = new HashMap<>();
            extendedProperties.put("teamType", team1TeamType);
            extendedProperties.put("identifier", team1Identifier);
            profileProperties.setExtendedProperties(extendedProperties);

            String team1GUID = client.createActorProfile(userId, null, null, profileProperties, null);

            profileProperties = new ActorProfileProperties();
            profileProperties.setTypeName("Team");
            profileProperties.setQualifiedName(team2QualifiedName);
            profileProperties.setKnownName(team2KnownName);
            profileProperties.setDescription(team2Description);

            extendedProperties = new HashMap<>();
            extendedProperties.put("teamType", team2TeamType);
            extendedProperties.put("identifier", team2Identifier);
            profileProperties.setExtendedProperties(extendedProperties);

            String team2GUID = client.createActorProfile(userId, null, null, profileProperties, null);

            client.linkTeamsInHierarchy(userId, null,null, team1GUID, team2GUID, true, null, null);

            PersonRoleProperties personRoleProperties = new PersonRoleProperties();
            personRoleProperties.setQualifiedName("TeamLeader:" + team2QualifiedName);
            personRoleProperties.setRoleId("TeamLeader:" + team2Description);
            String team2LeaderGUID = client.createPersonRole(userId, null, null, personRoleProperties);
            client.linkTeamPlayer(userId, null, null, team2LeaderGUID, team2GUID, null,true);

            personRoleProperties = new PersonRoleProperties();
            personRoleProperties.setQualifiedName("TeamMember:" + team2QualifiedName);
            personRoleProperties.setRoleId("TeamMember:" + team2Description);

            String team2MemberGUID = client.createPersonRole(userId, null, null, personRoleProperties);
            client.linkTeamPlayer(userId, null, null, team2MemberGUID, team2GUID, null,false);

            AppointmentProperties appointmentProperties = new AppointmentProperties();
            appointmentProperties.setIsPublic(true);

            client.linkPersonRoleToProfile(userId, null, null, team2MemberGUID, profile1GUID, appointmentProperties);
            client.linkPersonRoleToProfile(userId, null, null, team2MemberGUID, profile2GUID, appointmentProperties);

            List<ActorProfileElement> actorProfileElements = client.getActorProfiles(userId, 0 ,0);


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
    private OrganizationManagement getOrganizationClient(String   serverName,
                                                         String   serverPlatformRootURL,
                                                         AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getOrganizationClient";

        try
        {
            CommunityProfileRESTClient restClient = new CommunityProfileRESTClient(serverName, serverPlatformRootURL, auditLog);

            return new OrganizationManagement(serverName, serverPlatformRootURL, restClient, maxPageSize);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }

}
