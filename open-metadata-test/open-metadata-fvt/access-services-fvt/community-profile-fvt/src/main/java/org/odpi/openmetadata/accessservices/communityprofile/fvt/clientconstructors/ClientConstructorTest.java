/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.fvt.clientconstructors;

import org.odpi.openmetadata.accessservices.communityprofile.client.CommunityManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.MetadataSourceClient;
import org.odpi.openmetadata.accessservices.communityprofile.client.OrganizationManagement;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;


/**
 * ClientConstructorTest provides the methods to verify that all of the clients can be constructed with
 * or without security
 */
public class ClientConstructorTest
{
    private final static String testCaseName   = "ClientConstructorTest";

    private final static String serverUserId   = "TestNPA";
    private final static String serverPassword = "TestNPAPassword";
    private final static int    maxPageSize    = 100;

    /**
     * Run all of the defined tests and capture the results.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @return  results of running test
     */
    public static FVTResults performFVT(String   serverName,
                                        String   serverPlatformRootURL)
    {
        FVTResults results = new FVTResults(testCaseName);

        results.incrementNumberOfTests();
        try
        {
            ClientConstructorTest.runIt(serverPlatformRootURL, serverName, results.getAuditLogDestination());
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
     * @param auditLogDestination logging destination
     * @throws InvalidParameterException one of the tests failed
     */
    private static void runIt(String                 serverPlatformRootURL,
                              String                 serverName,
                              FVTAuditLogDestination auditLogDestination) throws InvalidParameterException
    {
        ClientConstructorTest thisTest = new ClientConstructorTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceWiki());

        thisTest.testMetadataSourceClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testOrganizationManagement(serverName, serverPlatformRootURL, auditLog);
        thisTest.testCommunityManagement(serverName, serverPlatformRootURL, auditLog);
    }



    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testMetadataSourceClient(String   serverName,
                                          String   serverPlatformRootURL,
                                          AuditLog auditLog) throws InvalidParameterException
    {
        new MetadataSourceClient(serverName, serverPlatformRootURL, auditLog, 100);
        new MetadataSourceClient(serverName, serverPlatformRootURL, 100);
        new MetadataSourceClient(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog, 100);
        new MetadataSourceClient(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100);

        CommunityProfileRESTClient restClient = new CommunityProfileRESTClient(serverName, serverPlatformRootURL);

        new MetadataSourceClient(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }




    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testOrganizationManagement(String   serverName,
                                            String   serverPlatformRootURL,
                                            AuditLog auditLog) throws InvalidParameterException
    {
        new OrganizationManagement(serverName, serverPlatformRootURL, auditLog, 200);
        new OrganizationManagement(serverName, serverPlatformRootURL, 100);
        new OrganizationManagement(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog, 100);
        new OrganizationManagement(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100);

        CommunityProfileRESTClient restClient = new CommunityProfileRESTClient(serverName, serverPlatformRootURL);

        new OrganizationManagement(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }


    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testCommunityManagement(String   serverName,
                                         String   serverPlatformRootURL,
                                         AuditLog auditLog) throws InvalidParameterException
    {
        new CommunityManagement(serverName, serverPlatformRootURL, auditLog, 100);
        new CommunityManagement(serverName, serverPlatformRootURL, 100);
        new CommunityManagement(serverName, serverPlatformRootURL, serverUserId, serverPassword,  auditLog, 100);
        new CommunityManagement(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100);

        CommunityProfileRESTClient restClient = new CommunityProfileRESTClient(serverName, serverPlatformRootURL);

        new CommunityManagement(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }
}
