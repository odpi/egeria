/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.clientconstructors;

import org.odpi.openmetadata.accessservices.analyticsmodeling.client.AnalyticsModelingRestClient;
import org.odpi.openmetadata.accessservices.analyticsmodeling.client.ImportClient;
import org.odpi.openmetadata.accessservices.analyticsmodeling.client.SynchronizationClient;
import org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.common.AnalyticsModelingTestBase;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;


/**
 * ClientConstructorTest provides the methods to verify that all of the clients can be constructed with
 * or without security
 */
public class ClientConstructorTest extends AnalyticsModelingTestBase
{
    private final static String testCaseName   = "ClientConstructorTest";
    private final static String serverUserId   = "TestNPA";
    private final static String serverPassword = "TestNPAPassword";

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
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceWiki());

        thisTest.testImportClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testSynchronizationClient(serverName, serverPlatformRootURL, auditLog);
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
    private void testImportClient(String   serverName,
                                          String   serverPlatformRootURL,
                                          AuditLog auditLog) throws InvalidParameterException
    {
        new ImportClient(serverName, serverPlatformRootURL, auditLog);
        new ImportClient(serverName, serverPlatformRootURL);
        new ImportClient(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog);
        new ImportClient(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        AnalyticsModelingRestClient restClient = new AnalyticsModelingRestClient(serverName, serverPlatformRootURL);

        new ImportClient(serverName, serverPlatformRootURL, restClient, maxPageSize);
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
    private void testSynchronizationClient(String   serverName,
                                           String   serverPlatformRootURL,
                                           AuditLog auditLog) throws InvalidParameterException
    {
        new SynchronizationClient(serverName, serverPlatformRootURL, auditLog);
        new SynchronizationClient(serverName, serverPlatformRootURL);
        new SynchronizationClient(serverName, serverPlatformRootURL, serverUserId, serverPassword,  auditLog);
        new SynchronizationClient(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        AnalyticsModelingRestClient restClient = new AnalyticsModelingRestClient(serverName, serverPlatformRootURL);

        new SynchronizationClient(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }
}
