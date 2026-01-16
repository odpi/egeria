/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.fvt.clientconstructors;

import org.odpi.openmetadata.accessservices.assetmanager.client.AssetManagerEventClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.ExternalAssetManagerClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.GlossaryExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.fvt.common.AssetManagerTestBase;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;


/**
 * ClientConstructorTest provides the methods to verify that all of the clients can be constructed with
 * or without security
 */
public class ClientConstructorTest extends AssetManagerTestBase
{
    private final static String testCaseName   = "ClientConstructorTest";

    private final static String callerId       = "TestCallerId";
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
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceWiki());

        thisTest.testAssetManagerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testGlossaryExchangeClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testAssetManagerEventClient(serverName, serverPlatformRootURL, auditLog);
    }



    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testAssetManagerClient(String   serverName,
                                          String   serverPlatformRootURL,
                                          AuditLog auditLog) throws InvalidParameterException
    {
        new ExternalAssetManagerClient(serverName, serverPlatformRootURL, auditLog, 100);
        new ExternalAssetManagerClient(serverName, serverPlatformRootURL, 100);
        new ExternalAssetManagerClient(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog, 100);
        new ExternalAssetManagerClient(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100);

        AssetManagerRESTClient restClient = new AssetManagerRESTClient(serverName, serverPlatformRootURL);

        new ExternalAssetManagerClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
    }


    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testGlossaryExchangeClient(String   serverName,
                                           String   serverPlatformRootURL,
                                           AuditLog auditLog) throws InvalidParameterException
    {
        new GlossaryExchangeClient(serverName, serverPlatformRootURL, auditLog, 100);
        new GlossaryExchangeClient(serverName, serverPlatformRootURL, 100);
        new GlossaryExchangeClient(serverName, serverPlatformRootURL, serverUserId, serverPassword,  auditLog, 100);
        new GlossaryExchangeClient(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100);

        AssetManagerRESTClient restClient = new AssetManagerRESTClient(serverName, serverPlatformRootURL);

        new GlossaryExchangeClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
    }


    /**
     * Create a client using its constructor.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testAssetManagerEventClient(String   serverName,
                                             String   serverPlatformRootURL,
                                             AuditLog auditLog) throws InvalidParameterException
    {
        new AssetManagerEventClient(serverName,
                                    serverPlatformRootURL,
                                    serverUserId,
                                    serverPassword,
                                    maxPageSize,
                                    auditLog,
                                    callerId);
    }
}
