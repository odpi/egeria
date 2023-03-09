/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.fvt.clientconstructors;


import org.odpi.openmetadata.accessservices.discoveryengine.client.DiscoveryEngineClient;
import org.odpi.openmetadata.accessservices.discoveryengine.client.rest.ODFRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;


/**
 * ClientConstructorTest provides the methods to verify that all of the clients can be constructed with
 * or without security
 */
public class ClientConstructorTest
{
    private final static String testCaseName   = "ClientConstructorTest";

    private final static String callerId       = "TestCallerId";
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
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceWiki());

        thisTest.testDiscoveryEngineClient(serverName, serverPlatformRootURL, auditLog);
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
    private void testDiscoveryEngineClient(String   serverName,
                                           String   serverPlatformRootURL,
                                           AuditLog auditLog) throws InvalidParameterException
    {
        new DiscoveryEngineClient(serverName, serverPlatformRootURL, auditLog);
        new DiscoveryEngineClient(serverName, serverPlatformRootURL);
        new DiscoveryEngineClient(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog);
        new DiscoveryEngineClient(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        ODFRESTClient restClient = new ODFRESTClient(serverName, serverPlatformRootURL);

        new DiscoveryEngineClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
    }

}
