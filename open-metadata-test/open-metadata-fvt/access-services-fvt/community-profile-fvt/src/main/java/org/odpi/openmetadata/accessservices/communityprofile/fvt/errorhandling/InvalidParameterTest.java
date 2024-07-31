/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.communityprofile.client.CommunityManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.OrganizationManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * InvalidParameterTest calls each non-constructor method with a series of null or invalid parameters.
 * It ensures that InvalidParameterException is thrown.
 */
public class InvalidParameterTest
{
    private final static String testCaseName       = "InvalidParameterTest";

    private final static int    maxPageSize        = 100;


    /**
     * Run all of the defined tests and capture the results.
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
            InvalidParameterTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        InvalidParameterTest thisTest = new InvalidParameterTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceWiki());

        thisTest.testOrganizationClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testCommunityClient(serverName, serverPlatformRootURL, userId, auditLog);
    }
    
    
    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCommunityClient(String   serverName,
                                     String   serverPlatformRootURL,
                                     String   userId,
                                     AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testCommunityClient";

        try
        {
            CommunityProfileRESTClient restClient = new CommunityProfileRESTClient(serverName, serverPlatformRootURL, auditLog);
            CommunityManagement        client     = new CommunityManagement(serverName, serverPlatformRootURL, restClient, maxPageSize);

            testSetUpCommunity(client, userId);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }



    /**
     * Test null userId passed to createCommunity.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSetUpCommunityNoUserId(CommunityManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpCommunityNoUserId";

        try
        {
            CommunityProperties properties = new CommunityProperties();
            properties.setQualifiedName("TestQualifiedName");
            client.createCommunity(null, null, null, properties);

            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null properties passed to createCommunity.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSetUpCommunityNoProperties(CommunityManagement client,
                                                String              userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpCommunityNoProperties";

        try
        {
            client.createCommunity(userId, null, null, null);

            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createCommunity.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSetUpCommunityNoName(CommunityManagement client,
                                          String              userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpCommunityNoUserId";

        try
        {
            client.createCommunity(userId, null, null, new CommunityProperties());

            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null qualifiedName passed to createCommunity.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSetUpCommunity(CommunityManagement client,
                                    String              userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpProfileNoName";

        try
        {
            testSetUpCommunityNoUserId(client);
            testSetUpCommunityNoProperties(client, userId);
            testSetUpCommunityNoName(client, userId);

        }
        catch (FVTUnexpectedCondition expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }

    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testOrganizationClient(String   serverName,
                                        String   serverPlatformRootURL,
                                        String   userId,
                                        AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testOrganizationClient";

        try
        {
            CommunityProfileRESTClient restClient = new CommunityProfileRESTClient(serverName, serverPlatformRootURL, auditLog);
            OrganizationManagement        client  = new OrganizationManagement(serverName, serverPlatformRootURL, restClient, maxPageSize);

            testGetMyProfile(client);
            testGetProfileByUserId(client);
            testSetUpProfile(userId, client);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test combinations of invalid parameters passed to getMyProfile.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetMyProfile(OrganizationManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetMyProfile";

        try
        {
            testGetMyProfileNoUserId(client);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to getMyProfile.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetMyProfileNoUserId(OrganizationManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetMyProfileNoUserId";

        try
        {
            client.getActorProfiles(null, 0, 0);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test combinations of invalid parameters passed to getMyKarmaPoints.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetProfileByUserId(OrganizationManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetMyKarmaPoints";

        try
        {
            testGetProfileByUserIdNoUserId(client);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to getMyKarmaPoints.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetProfileByUserIdNoUserId(OrganizationManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetProfileByUserIdNoUserId";

        try
        {
            client.getActorProfileByUserId(null, "testProfileUserId");
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test combinations of invalid parameters passed to createFileSystem.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSetUpProfile(String              userId,
                                  OrganizationManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpProfile";

        try
        {
            testSetUpProfileNoUserId(client);
            testSetUpProfileNoProperties(client, userId);
            testSetUpProfileNoName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createActorProfile.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSetUpProfileNoUserId(OrganizationManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpMyProfileNoUserId";

        try
        {
            ActorProfileProperties properties = new ActorProfileProperties();
            properties.setQualifiedName("TestQualifiedName");
            client.createActorProfile(null, null, null, properties, null);

            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null properties passed to createActorProfile.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSetUpProfileNoProperties(OrganizationManagement client,
                                              String                 userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpProfileNoProperties";

        try
        {
            client.createActorProfile(userId, null, null, null, null);

            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null qualifiedName passed to createActorProfile.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSetUpProfileNoName(OrganizationManagement client,
                                        String              userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpProfileNoName";

        try
        {
            client.createActorProfile(userId, null, null, new ActorProfileProperties(), null);

            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }
}
