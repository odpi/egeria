/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.fvt.clientconstructors;

import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.FilesAndFoldersClient;
import org.odpi.openmetadata.accessservices.datamanager.client.MetadataSourceClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
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
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceWiki());

        thisTest.testMetadataSourceClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testDatabaseManagerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testFilesAndFoldersClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testDataManagerEventClient(serverName, serverPlatformRootURL, auditLog);
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
    private void testMetadataSourceClient(String   serverName,
                                          String   serverPlatformRootURL,
                                          AuditLog auditLog) throws InvalidParameterException
    {
        new MetadataSourceClient(serverName, serverPlatformRootURL, auditLog);
        new MetadataSourceClient(serverName, serverPlatformRootURL);
        new MetadataSourceClient(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog);
        new MetadataSourceClient(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL);

        new MetadataSourceClient(serverName, serverPlatformRootURL, restClient, maxPageSize);
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
    private void testDatabaseManagerClient(String   serverName,
                                           String   serverPlatformRootURL,
                                           AuditLog auditLog) throws InvalidParameterException
    {
        new DatabaseManagerClient(serverName, serverPlatformRootURL, auditLog);
        new DatabaseManagerClient(serverName, serverPlatformRootURL);
        new DatabaseManagerClient(serverName, serverPlatformRootURL, serverUserId, serverPassword,  auditLog);
        new DatabaseManagerClient(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL);

        new DatabaseManagerClient(serverName, serverPlatformRootURL, restClient, maxPageSize);
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
    private void testFilesAndFoldersClient(String   serverName,
                                           String   serverPlatformRootURL,
                                           AuditLog auditLog) throws InvalidParameterException
    {
        new FilesAndFoldersClient(serverName, serverPlatformRootURL, auditLog);
        new FilesAndFoldersClient(serverName, serverPlatformRootURL);
        new FilesAndFoldersClient(serverName, serverPlatformRootURL, serverUserId, serverPassword, auditLog);
        new FilesAndFoldersClient(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL);

        new FilesAndFoldersClient(serverName, serverPlatformRootURL, restClient, maxPageSize);
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
    private void testDataManagerEventClient(String   serverName,
                                            String   serverPlatformRootURL,
                                            AuditLog auditLog) throws InvalidParameterException
    {
        new DataManagerEventClient(serverName, serverPlatformRootURL, callerId);
        new DataManagerEventClient(serverName, serverPlatformRootURL, serverUserId, serverPassword, callerId);

        DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL);

        new DataManagerEventClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog, callerId);
    }
}
