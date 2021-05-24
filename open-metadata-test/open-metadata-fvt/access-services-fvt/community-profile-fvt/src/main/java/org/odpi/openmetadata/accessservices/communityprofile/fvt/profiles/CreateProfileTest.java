/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.fvt.profiles;


import org.odpi.openmetadata.accessservices.communityprofile.client.MyProfileManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.PersonalProfileManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.ArrayList;
import java.util.List;

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
    private final static String profile2DisplayName   = "TestProfile2 displayName";
    private final static String profile2Description   = "TestProfile2 description";
    private final static String profile2JobTitle      = "TestProfile2 jobTitle";
    private final static String profile2jobRole       = "TestProfile2 jobRoleDescription";
    private final static String profile2AddProp       = "TestProfile2 additionalProperty";


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
        CreateProfileTest thisTest = new CreateProfileTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceWiki());


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
    private PersonalProfileManagement getProfileManagerClient(String   serverName,
                                                              String   serverPlatformRootURL,
                                                              AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getProfileManagerClient";

        try
        {
            CommunityProfileRESTClient restClient = new CommunityProfileRESTClient(serverName, serverPlatformRootURL, auditLog);

            return new PersonalProfileManagement(serverName, serverPlatformRootURL, restClient, maxPageSize);
        }
        catch (Throwable unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create and return a my profile manager client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private MyProfileManagement getMyProfileManagerClient(String   serverName,
                                                          String   serverPlatformRootURL,
                                                          AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getMyProfileManagerClient";

        try
        {
            CommunityProfileRESTClient restClient = new CommunityProfileRESTClient(serverName, serverPlatformRootURL, auditLog);

            return new MyProfileManagement(serverName, serverPlatformRootURL, restClient, maxPageSize);
        }
        catch (Throwable unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }



}
