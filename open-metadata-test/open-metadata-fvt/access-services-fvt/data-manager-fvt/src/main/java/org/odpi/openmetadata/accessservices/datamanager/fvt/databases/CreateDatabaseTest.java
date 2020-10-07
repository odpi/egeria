/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.fvt.databases;

import org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.MetadataSourceClient;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseManagerProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.List;

/**
 * CreateDatabaseTest calls the DatabaseManagerClient to create a database with schemas tables and columns
 * and then retrieve the results.
 */
public class CreateDatabaseTest
{
    private final static String testCaseName       = "CreateDatabaseTest";

    private final static int    maxPageSize        = 100;

    /*
     * The database manager name is constant - the guid is created as part of the test.
     */
    private final static String databaseManagerName            = "TestDatabaseManager";
    private final static String databaseManagerDisplayName     = "DatabaseManager displayName";
    private final static String databaseManagerDescription     = "DatabaseManager description";
    private final static String databaseManagerTypeDescription = "DatabaseManager type";
    private final static String databaseManagerVersion         = "DatabaseManager version";

    /*
     * The database name is constant - the guid is created as part of the test.
     */
    private final static String databaseName        = "TestDatabase";
    private final static String databaseDisplayName = "Database displayName";
    private final static String databaseDescription = "Database description";
    private final static String databaseType        = "Database type";
    private final static String databaseVersion     = "Database version";


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
            CreateDatabaseTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        CreateDatabaseTest thisTest = new CreateDatabaseTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceWiki());

        DatabaseManagerClient client = thisTest.getDatabaseManagerClient(serverName, serverPlatformRootURL, auditLog);
        String databaseManagerGUID = thisTest.getDatabaseManager(serverName, serverPlatformRootURL, userId, auditLog);
        String databaseGUID = thisTest.getDatabase(client, databaseManagerGUID, userId);
    }



    /**
     * Create a database manager entity and return its GUID.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @return unique identifier of the database manager entity
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDatabaseManager(String   serverName,
                                      String   serverPlatformRootURL,
                                      String   userId,
                                      AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabaseManager";

        try
        {
            DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL);
            MetadataSourceClient  client     = new MetadataSourceClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);

            DatabaseManagerProperties properties = new DatabaseManagerProperties();
            properties.setQualifiedName(databaseManagerName);
            properties.setDisplayName(databaseManagerDisplayName);
            properties.setDescription(databaseManagerDescription);
            properties.setTypeDescription(databaseManagerTypeDescription);
            properties.setVersion(databaseManagerVersion);

            String databaseManagerGUID = client.createDatabaseManager(userId, null, null, properties);

            if (databaseManagerGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            String retrievedDatabaseManagerGUID = client.getMetadataSourceGUID(userId, databaseManagerName);

            if (retrievedDatabaseManagerGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Retrieve)");
            }

            if (! retrievedDatabaseManagerGUID.equals(databaseManagerGUID))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Inconsistent GUIDs)");
            }

            return databaseManagerGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Throwable unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create and return a database manager client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private DatabaseManagerClient getDatabaseManagerClient(String   serverName,
                                                           String   serverPlatformRootURL,
                                                           AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabaseManagerClient";

        try
        {
            DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL);

            return new DatabaseManagerClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Throwable unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a database and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the database manager
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDatabase(DatabaseManagerClient client,
                               String                databaseManagerGUID,
                               String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabase";

        try
        {
            DatabaseProperties properties = new DatabaseProperties();

            properties.setQualifiedName(databaseName);
            properties.setDisplayName(databaseDisplayName);
            properties.setDescription(databaseDescription);
            properties.setDatabaseType(databaseType);
            properties.setDatabaseVersion(databaseVersion);

            String databaseGUID = client.createDatabase(userId, databaseManagerGUID, databaseManagerName, properties);

            if (databaseGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            DatabaseElement retrievedElement = client.getDatabaseByGUID(userId, databaseGUID);
            DatabaseProperties retrievedDatabase = retrievedElement.getDatabaseProperties();

            if (retrievedDatabase == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Database for Retrieve)");
            }

            if (! retrievedDatabase.getQualifiedName().equals(databaseName))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! retrievedDatabase.getDisplayName().equals(databaseDisplayName))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! retrievedDatabase.getDescription().equals(databaseDescription))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            if (! retrievedDatabase.getDatabaseType().equals(databaseType))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseType from Retrieve)");
            }
            if (! retrievedDatabase.getDatabaseVersion().equals(databaseVersion))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseVersion from Retrieve)");
            }

            List<DatabaseElement> databaseList = client.getDatabasesByName(userId, databaseName, 0, maxPageSize);

            if (databaseList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Database for RetrieveByName)");
            }
            else if (databaseList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty Database list for RetrieveByName)");
            }
            else if (databaseList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Database list for RetrieveByName contains" + databaseList.size() +
                                                 " elements)");
            }

            retrievedElement = databaseList.get(0);
            retrievedDatabase = retrievedElement.getDatabaseProperties();

            if (! retrievedDatabase.getQualifiedName().equals(databaseName))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! retrievedDatabase.getDisplayName().equals(databaseDisplayName))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! retrievedDatabase.getDescription().equals(databaseDescription))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }
            if (! retrievedDatabase.getDatabaseType().equals(databaseType))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseType from RetrieveByName)");
            }
            if (! retrievedDatabase.getDatabaseVersion().equals(databaseVersion))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseVersion from RetrieveByName)");
            }

            return databaseGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Throwable unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }
}
