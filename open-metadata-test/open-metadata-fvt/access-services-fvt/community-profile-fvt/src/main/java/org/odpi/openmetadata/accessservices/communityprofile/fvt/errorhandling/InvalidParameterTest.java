/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.communityprofile.client.MyProfileManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.HashMap;

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
     * Run all of the tests in this class.
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
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceWiki());

        thisTest.testMyProfileClient(serverName, serverPlatformRootURL, userId, auditLog);
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
    private void testMyProfileClient(String   serverName,
                                     String   serverPlatformRootURL,
                                     String   userId,
                                     AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testMyProfileClient";

        try
        {
            CommunityProfileRESTClient restClient = new CommunityProfileRESTClient(serverName, serverPlatformRootURL, auditLog);
            MyProfileManagement        client     = new MyProfileManagement(serverName, serverPlatformRootURL, restClient, maxPageSize);

            testGetMyProfile(client);
            testGetMyKarmaPoints(client);
            testSetUpMyProfile(userId, client);
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
    private void testGetMyProfile(MyProfileManagement client) throws FVTUnexpectedCondition
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
    private void testGetMyProfileNoUserId(MyProfileManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetMyProfileNoUserId";

        try
        {
            client.getMyProfile(null);
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
    private void testGetMyKarmaPoints(MyProfileManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetMyKarmaPoints";

        try
        {
            testGetMyKarmaPointsNoUserId(client);
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
    private void testGetMyKarmaPointsNoUserId(MyProfileManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetMyKarmaPointsNoUserId";

        try
        {
            client.getMyKarmaPoints(null);
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
    private void testSetUpMyProfile(String              userId,
                                    MyProfileManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpMyProfileNoUserId";

        try
        {
            testSetUpMyProfileNoUserId(client);
            testSetUpMyProfileNoQualifiedName(client, userId);
            testSetUpMyProfileNoKnownName(client, userId);
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
    private void testSetUpMyProfileNoUserId(MyProfileManagement client) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpMyProfileNoUserId";

        try
        {
            client.setUpMyProfile(null, "qualifiedName", "fullName", "knownName", "jobTitle", "jobRoleDescription", new HashMap<>());
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
     * Test null qualifiedName passed to createFileSystem.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSetUpMyProfileNoQualifiedName(MyProfileManagement client,
                                                   String              userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpMyProfileNoQualifiedName";

        try
        {
            client.setUpMyProfile(userId, null, "fullName", "knownName", "jobTitle", "jobRoleDescription", new HashMap<>());

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
     * Test null knownName passed to createFileSystem.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSetUpMyProfileNoKnownName(MyProfileManagement client,
                                                   String              userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testSetUpMyProfileNoKnownName";

        try
        {
            client.setUpMyProfile(userId, "qualifiedName", "fullName", null, "jobTitle", "jobRoleDescription", new HashMap<>());

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
