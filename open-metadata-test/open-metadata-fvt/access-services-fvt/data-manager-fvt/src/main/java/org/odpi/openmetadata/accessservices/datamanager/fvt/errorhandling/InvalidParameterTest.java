/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.FilesAndFoldersClient;
import org.odpi.openmetadata.accessservices.datamanager.client.MetadataSourceClient;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseManagerProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.FileManagerProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.FileSystemProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
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
    private final static String externalSourceGUID = "TestExternalSourceGUID";
    private final static String externalSourceName = "TestExternalSourceName";


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
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceWiki());

        thisTest.testMetadataSourceClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testDatabaseManagerClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testFilesAndFoldersClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testDataManagerEventClient(serverName, serverPlatformRootURL, userId, auditLog);
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
    private void testMetadataSourceClient(String   serverName,
                                          String   serverPlatformRootURL,
                                          String   userId,
                                          AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testMetadataSourceClient";

        try
        {
            DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL, auditLog);
            MetadataSourceClient  client     = new MetadataSourceClient(serverName, serverPlatformRootURL, restClient, maxPageSize);

            testCreateFileSystem(userId, client);
            testCreateFileManager(userId, client);
            testCreateDatabaseManager(userId, client);
            testGetMetadataSourceGUID(userId, client);
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
     * Test combinations of invalid parameters passed to createFileSystem.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateFileSystem(String               userId,
                                      MetadataSourceClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateFileSystem";

        try
        {
            testCreateFileSystemNoUserId(client);
            testCreateFileSystemNoProperties(client, userId);
            testCreateFileSystemNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createFileSystem.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateFileSystemNoUserId(MetadataSourceClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateFileSystemNoUserId";

        FileSystemProperties fileSystemProperties = new FileSystemProperties();

        try
        {
            client.createFileSystem(null, externalSourceGUID, externalSourceName, fileSystemProperties);
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
     * Test null properties passed to createFileSystem.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateFileSystemNoProperties(MetadataSourceClient client,
                                                  String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateFileSystemNoProperties";

        try
        {
            client.createFileSystem(userId, externalSourceGUID, externalSourceName, null);
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
     * Test null properties passed to createFileSystem.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateFileSystemNoQualifiedName(MetadataSourceClient client,
                                                     String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateFileSystemNoQualifiedName";

        try
        {
            FileSystemProperties fileSystemProperties = new FileSystemProperties();

            client.createFileSystem(userId, externalSourceGUID, externalSourceName, fileSystemProperties);
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
     * Test combinations of invalid parameters passed to createFileManager.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateFileManager(String               userId,
                                       MetadataSourceClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateFileSystem";

        try
        {
            testCreateFileManagerNoUserId(client);
            testCreateFileManagerNoProperties(client, userId);
            testCreateFileManagerNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createFileManager.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateFileManagerNoUserId(MetadataSourceClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateFileManagerNoUserId";

        FileManagerProperties fileManagerProperties = new FileManagerProperties();

        try
        {
            client.createFileManager(null, externalSourceGUID, externalSourceName, fileManagerProperties);
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
     * Test null properties passed to createFileManager.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateFileManagerNoProperties(MetadataSourceClient client,
                                                   String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateFileManagerNoProperties";

        try
        {
            client.createFileManager(userId, externalSourceGUID, externalSourceName, null);
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
     * Test null qualifiedName passed to createFileManager.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateFileManagerNoQualifiedName(MetadataSourceClient client,
                                                      String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateFileManagerNoQualifiedName";

        try
        {
            FileManagerProperties fileManagerProperties = new FileManagerProperties();

            client.createFileManager(userId, externalSourceGUID, externalSourceName, fileManagerProperties);
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
     * Test combinations of invalid parameters passed to createDatabaseManager.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateDatabaseManager(String               userId,
                                           MetadataSourceClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateDatabaseManager";

        try
        {
            testCreateDatabaseManagerNoUserId(client);
            testCreateDatabaseManagerNoProperties(client, userId);
            testCreateDatabaseManagerNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createDatabaseManager.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateDatabaseManagerNoUserId(MetadataSourceClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateDatabaseManagerNoUserId";

        DatabaseManagerProperties databaseManagerProperties = new DatabaseManagerProperties();

        try
        {
            client.createDatabaseManager(null, externalSourceGUID, externalSourceName, databaseManagerProperties);
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
     * Test null properties passed to createDatabaseManager.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateDatabaseManagerNoProperties(MetadataSourceClient client,
                                                       String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateDatabaseManagerNoProperties";

        try
        {
            client.createDatabaseManager(userId, externalSourceGUID, externalSourceName, null);
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
     * Test null qualifiedName passed to createDatabaseManager.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateDatabaseManagerNoQualifiedName(MetadataSourceClient client,
                                                          String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateDatabaseManagerNoQualifiedName";

        try
        {
            DatabaseManagerProperties databaseManagerProperties = new DatabaseManagerProperties();

            client.createDatabaseManager(userId, externalSourceGUID, externalSourceName, databaseManagerProperties);
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
     * Test combinations of invalid parameters passed to getMetadataSourceGUID.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetMetadataSourceGUID(String               userId,
                                           MetadataSourceClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateDatabaseManager";

        try
        {
            testGetMetadataSourceGUIDNoUserId(client);
            testGetMetadataSourceGUIDNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to getMetadataSourceGUID.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetMetadataSourceGUIDNoUserId(MetadataSourceClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetMetadataSourceGUIDNoUserId";

        try
        {
            client.getMetadataSourceGUID(null, "TestQualifiedName");
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
     * Test null qualifiedName passed to getMetadataSourceGUID.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetMetadataSourceGUIDNoQualifiedName(MetadataSourceClient client,
                                                          String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetMetadataSourceGUIDNoQualifiedName";

        try
        {
            client.getMetadataSourceGUID(userId, null);
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
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testDatabaseManagerClient(String   serverName,
                                           String   serverPlatformRootURL,
                                           String   userId,
                                           AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testDatabaseManagerClient";

        try
        {
            DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL, auditLog);
            DatabaseManagerClient client = new DatabaseManagerClient(serverName, serverPlatformRootURL, restClient, maxPageSize);

            testCreateDatabase(userId, client);
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
     * Test combinations of invalid parameters passed to createDatabase.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateDatabase(String                userId,
                                    DatabaseManagerClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateDatabase";

        try
        {
            testCreateDatabaseNoUserId(client);
            testCreateDatabaseNoProperties(client, userId);
            testCreateDatabaseNoQualifiedName(client, userId);
            testCreateDatabaseNoExternalSourceGUID(client, userId);
            testCreateDatabaseNoExternalSourceName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createDatabase.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateDatabaseNoUserId(DatabaseManagerClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateDatabaseNoUserId";

        DatabaseProperties properties = new DatabaseProperties();

        try
        {
            client.createDatabase(null, externalSourceGUID, externalSourceName, properties);
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
     * Test null properties passed to createDatabase.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateDatabaseNoProperties(DatabaseManagerClient client,
                                                String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateDatabaseNoQualifiedName";

        try
        {
            client.createDatabase(userId, externalSourceGUID, externalSourceName, null);
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
     * Test null properties passed to createDatabase.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateDatabaseNoQualifiedName(DatabaseManagerClient client,
                                                   String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateDatabaseNoQualifiedName";

        DatabaseProperties properties = new DatabaseProperties();

        try
        {
            client.createDatabase(userId, externalSourceGUID, externalSourceName, properties);
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
     * Test null fileServerCapabilityGUID passed to createDatabase.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateDatabaseNoExternalSourceGUID(DatabaseManagerClient client,
                                                        String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateDatabaseNoExternalSourceGUID";

        DatabaseProperties properties = new DatabaseProperties();

        try
        {
            client.createDatabase(userId, null, externalSourceName, properties);
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
     * Test null fileServerCapabilityGUID passed to createDatabase.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateDatabaseNoExternalSourceName(DatabaseManagerClient client,
                                                        String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateDatabaseNoExternalSourceName";

        DatabaseProperties properties = new DatabaseProperties();

        try
        {
            client.createDatabase(userId, externalSourceGUID, null, properties);
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
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testFilesAndFoldersClient(String   serverName,
                                           String   serverPlatformRootURL,
                                           String   userId,
                                           AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testFilesAndFoldersClient";

        try
        {
            DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL, auditLog);
            FilesAndFoldersClient client     = new FilesAndFoldersClient(serverName, serverPlatformRootURL, restClient, maxPageSize);

            testCreateNestedFolders(userId, client);
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
     * Test combinations of invalid parameters passed to createNestedFolders.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateNestedFolders(String                userId,
                                         FilesAndFoldersClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateNestedFolders";

        try
        {
            testCreateNestedFoldersNoUserId(client);
            testCreateNestedFoldersNoParentGUID(client, userId);
            testCreateNestedFoldersNoPathName(client, userId);
            testCreateNestedFoldersNoExternalSourceGUID(client, userId);
            testCreateNestedFoldersNoExternalSourceName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createNestedFolders.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateNestedFoldersNoUserId(FilesAndFoldersClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateNestedFoldersNoUserId";

        String parentGUID = "xxxx";
        String pathName = "top-folder/folder2";

        try
        {
            client.createNestedFolders(null, externalSourceGUID, externalSourceName, parentGUID, pathName);
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
     * Test null properties passed to createNestedFolders.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateNestedFoldersNoParentGUID(FilesAndFoldersClient client,
                                                     String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateNestedFoldersNoPathName";
        String pathName = "top-folder/folder2";

        try
        {
            client.createNestedFolders(userId, externalSourceGUID, externalSourceName, null, pathName);
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
     * Test null properties passed to createNestedFolders.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateNestedFoldersNoPathName(FilesAndFoldersClient client,
                                                   String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateNestedFoldersNoPathName";
        String parentGUID = "xxxx";

        try
        {
            client.createNestedFolders(userId, externalSourceGUID, externalSourceName, parentGUID, null);
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
     * Test null fileServerCapabilityGUID passed to createNestedFolders.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateNestedFoldersNoExternalSourceGUID(FilesAndFoldersClient client,
                                                             String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateNestedFoldersNoExternalSourceGUID";

        String pathName = "top-folder/folder2";
        String parentGUID = "xxxx";

        try
        {
            client.createNestedFolders(userId, null, externalSourceName, parentGUID, pathName);
        }
        catch (InvalidParameterException expectedError)
        {
            // all ok
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null fileServerCapabilityGUID passed to createNestedFolders.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateNestedFoldersNoExternalSourceName(FilesAndFoldersClient client,
                                                             String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateNestedFoldersNoExternalSourceGUID";

        String pathName = "top-folder/folder2";
        String parentGUID = "xxxx";

        try
        {
            client.createNestedFolders(userId, externalSourceGUID, null, parentGUID, pathName);
        }
        catch (InvalidParameterException expectedError)
        {
            // all ok
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
    private void testDataManagerEventClient(String   serverName,
                                            String   serverPlatformRootURL,
                                            String   userId,
                                            AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testDataManagerEventClient";

        try
        {
            DataManagerRESTClient  restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL, auditLog);
            DataManagerEventClient client     = new DataManagerEventClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog, "");

            testRegisterListener(userId, client);
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
     * Test combinations of invalid parameters passed to registerListener.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testRegisterListener(String                 userId,
                                      DataManagerEventClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateNestedFolders";

        try
        {
            testRegisterListenerNoUserId(client);
            testRegisterListenerNoListener(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to registerListener.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testRegisterListenerNoUserId(DataManagerEventClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testRegisterListenerNoUserId";

        try
        {
            client.registerListener(null, null);
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
     * Test null listener passed to registerListener.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testRegisterListenerNoListener(DataManagerEventClient client,
                                                String                 userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testRegisterListenerNoListener";

        try
        {
            client.registerListener(userId, null);
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
