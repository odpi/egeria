/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.fvt.clientconstructors;

import org.odpi.openmetadata.accessservices.assetowner.client.*;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
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
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceWiki());

        thisTest.testAssetOwnerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testAvroFileAssetOwnerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testCSVFileAssetOwnerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testFileSystemAssetOwnerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testValidValuesAssetOwnerClient(serverName, serverPlatformRootURL, auditLog);
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
    private void testAssetOwnerClient(String   serverName,
                                      String   serverPlatformRootURL,
                                      AuditLog auditLog) throws InvalidParameterException
    {
        new AssetOwner(serverName, serverPlatformRootURL, auditLog);
        new AssetOwner(serverName, serverPlatformRootURL);
        new AssetOwner(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog);
        new AssetOwner(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        AssetOwnerRESTClient restClient = new AssetOwnerRESTClient(serverName, serverPlatformRootURL);

        new AssetOwner(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
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
    private void testAvroFileAssetOwnerClient(String   serverName,
                                              String   serverPlatformRootURL,
                                              AuditLog auditLog) throws InvalidParameterException
    {
        new AvroFileAssetOwner(serverName, serverPlatformRootURL, auditLog);
        new AvroFileAssetOwner(serverName, serverPlatformRootURL);
        new AvroFileAssetOwner(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog);
        new AvroFileAssetOwner(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        AssetOwnerRESTClient restClient = new AssetOwnerRESTClient(serverName, serverPlatformRootURL);

        new AvroFileAssetOwner(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
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
    private void testCSVFileAssetOwnerClient(String   serverName,
                                             String   serverPlatformRootURL,
                                             AuditLog auditLog) throws InvalidParameterException
    {
        new CSVFileAssetOwner(serverName, serverPlatformRootURL, auditLog);
        new CSVFileAssetOwner(serverName, serverPlatformRootURL);
        new CSVFileAssetOwner(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog);
        new CSVFileAssetOwner(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        AssetOwnerRESTClient restClient = new AssetOwnerRESTClient(serverName, serverPlatformRootURL);

        new CSVFileAssetOwner(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
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
    private void testFileSystemAssetOwnerClient(String   serverName,
                                                String   serverPlatformRootURL,
                                                AuditLog auditLog) throws InvalidParameterException
    {
        new FileSystemAssetOwner(serverName, serverPlatformRootURL, auditLog);
        new FileSystemAssetOwner(serverName, serverPlatformRootURL);
        new FileSystemAssetOwner(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog);
        new FileSystemAssetOwner(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        AssetOwnerRESTClient restClient = new AssetOwnerRESTClient(serverName, serverPlatformRootURL);

        new FileSystemAssetOwner(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
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
    private void testValidValuesAssetOwnerClient(String   serverName,
                                                 String   serverPlatformRootURL,
                                                 AuditLog auditLog) throws InvalidParameterException
    {
        new ValidValuesAssetOwner(serverName, serverPlatformRootURL, auditLog);
        new ValidValuesAssetOwner(serverName, serverPlatformRootURL);
        new ValidValuesAssetOwner(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog);
        new ValidValuesAssetOwner(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        AssetOwnerRESTClient restClient = new AssetOwnerRESTClient(serverName, serverPlatformRootURL);

        new ValidValuesAssetOwner(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
    }
}
