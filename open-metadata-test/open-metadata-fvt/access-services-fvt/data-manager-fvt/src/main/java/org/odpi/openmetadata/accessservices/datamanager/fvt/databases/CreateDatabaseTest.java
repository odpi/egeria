/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.fvt.databases;

import org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.MetadataSourceClient;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.ArrayList;
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

    private final static String databaseName        = "TestDatabase";
    private final static String databaseDisplayName = "Database displayName";
    private final static String databaseDescription = "Database description";
    private final static String databaseType        = "Database type";
    private final static String databaseVersion     = "Database version";

    private final static String databaseSchemaName        = "TestDatabaseSchema";
    private final static String databaseSchemaDisplayName = "DatabaseSchema displayName";
    private final static String databaseSchemaDescription = "DatabaseSchema description";
    private final static String databaseSchemaZone        = "DatabaseSchema Zone";
    private final static String databaseSchemaOwner       = "DatabaseSchema Owner";

    private final static String databaseTableName        = "TestDatabaseTable";
    private final static String databaseTableDisplayName = "DatabaseTable displayName";
    private final static String databaseTableDescription = "DatabaseTable description";


    private final static String databaseColumnName        = "TestDatabaseColumn";
    private final static String databaseColumnDisplayName = "DatabaseColumn displayName";
    private final static String databaseColumnDescription = "DatabaseColumn description";
    private final static String databaseColumnType = "string";


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
        String databaseSchemaGUID = thisTest.getDatabaseSchema(client, databaseManagerGUID, databaseGUID, userId);
        String databaseTableGUID = thisTest.getDatabaseTable(client, databaseManagerGUID, databaseSchemaGUID, userId);
        String databaseColumnGUID = thisTest.getDatabaseColumn(client, databaseManagerGUID, databaseTableGUID, userId);
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
     * Create a database and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the database manager
     * @param userId calling user
     * @return GUID of database
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
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Database from Retrieve)");
            }

            if (! databaseName.equals(retrievedDatabase.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! databaseDisplayName.equals(retrievedDatabase.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! databaseDescription.equals(retrievedDatabase.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            if (! databaseType.equals(retrievedDatabase.getDatabaseType()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseType from Retrieve " + retrievedDatabase.getDatabaseType() + ")");
            }
            if (! databaseVersion.equals(retrievedDatabase.getDatabaseVersion()))
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

            if (! databaseName.equals(retrievedDatabase.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! databaseDisplayName.equals(retrievedDatabase.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! databaseDescription.equals(retrievedDatabase.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }
            if (! databaseType.equals(retrievedDatabase.getDatabaseType()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseType from RetrieveByName)");
            }
            if (! databaseVersion.equals(retrievedDatabase.getDatabaseVersion()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseVersion from RetrieveByName)");
            }

            databaseList = client.getDatabasesByName(userId, databaseName, 1, maxPageSize);

            if (databaseList != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Database for RetrieveByName)");
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



    /**
     * Create a database schema and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the database manager
     * @param databaseGUID unique id of the database
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDatabaseSchema(DatabaseManagerClient client,
                                     String                databaseManagerGUID,
                                     String                databaseGUID,
                                     String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabaseSchema";

        try
        {
            DatabaseSchemaProperties properties = new DatabaseSchemaProperties();

            properties.setQualifiedName(databaseSchemaName);
            properties.setDisplayName(databaseSchemaDisplayName);
            properties.setDescription(databaseSchemaDescription);

            List<String> zones = new ArrayList<>();
            zones.add(databaseSchemaZone);
            properties.setZoneMembership(zones);
            properties.setOwner(databaseSchemaOwner);
            properties.setOwnerCategory(OwnerCategory.USER_ID);

            String databaseSchemaGUID = client.createDatabaseSchema(userId, databaseManagerGUID, databaseManagerName, databaseGUID, properties);

            if (databaseSchemaGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for schemaCreate)");
            }

            DatabaseSchemaElement    retrievedElement  = client.getDatabaseSchemaByGUID(userId, databaseSchemaGUID);
            DatabaseSchemaProperties retrievedSchema = retrievedElement.getDatabaseSchemaProperties();

            if (retrievedSchema == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseSchema from Retrieve)");
            }

            if (! databaseSchemaName.equals(retrievedSchema.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! databaseSchemaDisplayName.equals(retrievedSchema.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! databaseSchemaDescription.equals(retrievedSchema.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            if (retrievedSchema.getZoneMembership() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Null database schema Zone Membership from Retrieve)");
            }
            if (retrievedSchema.getZoneMembership().size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Database schema Zone Membership size is " + retrievedSchema.getZoneMembership().size() + " from Retrieve)");
            }
            if (! databaseSchemaZone.equals(retrievedSchema.getZoneMembership().get(0)))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad database schema Zone Membership from Retrieve)");
            }
            if (! databaseSchemaOwner.equals(retrievedSchema.getOwner()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad database owner from Retrieve)");
            }
            if (OwnerCategory.USER_ID != retrievedSchema.getOwnerCategory())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad database owner type from Retrieve)");
            }

            List<DatabaseSchemaElement> databaseSchemaList = client.getDatabaseSchemasByName(userId, databaseSchemaName, 0, maxPageSize);

            if (databaseSchemaList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseSchema for RetrieveByName)");
            }
            else if (databaseSchemaList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty DatabaseSchema list for RetrieveByName)");
            }
            else if (databaseSchemaList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(DatabaseSchema list for RetrieveByName contains" + databaseSchemaList.size() +
                                                         " elements)");
            }

            retrievedElement = databaseSchemaList.get(0);
            retrievedSchema = retrievedElement.getDatabaseSchemaProperties();

            if (! databaseSchemaName.equals(retrievedSchema.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! databaseSchemaDisplayName.equals(retrievedSchema.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! databaseSchemaDescription.equals(retrievedSchema.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }
            if (! databaseSchemaZone.equals(retrievedSchema.getZoneMembership().get(0)))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad zone membership from RetrieveByName)");
            }
            if (! databaseSchemaOwner.equals(retrievedSchema.getOwner()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad database owner from RetrieveByName)");
            }

            return databaseSchemaGUID;
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
     * Create a database table and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the database manager
     * @param databaseSchemaGUID unique id of the databaseSchema
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDatabaseTable(DatabaseManagerClient client,
                                    String                databaseManagerGUID,
                                    String                databaseSchemaGUID,
                                    String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabaseTable";

        try
        {
            DatabaseTableProperties properties = new DatabaseTableProperties();

            properties.setQualifiedName(databaseTableName);
            properties.setDisplayName(databaseTableDisplayName);
            properties.setDescription(databaseTableDescription);


            String databaseTableGUID = client.createDatabaseTable(userId, databaseManagerGUID, databaseManagerName, databaseSchemaGUID, properties);

            if (databaseTableGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for tableCreate)");
            }

            DatabaseTableElement    retrievedElement = client.getDatabaseTableByGUID(userId, databaseTableGUID);
            DatabaseTableProperties retrievedTable  = retrievedElement.getDatabaseTableProperties();

            if (retrievedTable == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseTable from Retrieve)");
            }

            if (! databaseTableName.equals(retrievedTable.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! databaseTableDisplayName.equals(retrievedTable.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! databaseTableDescription.equals(retrievedTable.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }

            List<DatabaseTableElement> databaseTableList = client.getDatabaseTablesByName(userId, databaseTableName, 0, maxPageSize);

            if (databaseTableList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseTable for RetrieveByName)");
            }
            else if (databaseTableList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty DatabaseTable list for RetrieveByName)");
            }
            else if (databaseTableList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(DatabaseTable list for RetrieveByName contains" + databaseTableList.size() +
                                                         " elements)");
            }

            retrievedElement = databaseTableList.get(0);
            retrievedTable = retrievedElement.getDatabaseTableProperties();

            if (! databaseTableName.equals(retrievedTable.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! databaseTableDisplayName.equals(retrievedTable.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! databaseTableDescription.equals(retrievedTable.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            List<DatabaseColumnElement> databaseColumnList = client.getColumnsForDatabaseTable(userId, databaseTableGUID, 0, maxPageSize);

            if (databaseColumnList != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseColumn for getColumnsForDatabaseTable)");
            }

            return databaseTableGUID;
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
     * Create a database column and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the database manager
     * @param databaseTableGUID unique id of the database table to connect the column to
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDatabaseColumn(DatabaseManagerClient client,
                                     String                databaseManagerGUID,
                                     String                databaseTableGUID,
                                     String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabaseColumn";

        try
        {
            DatabaseColumnProperties  properties = new DatabaseColumnProperties();

            properties.setQualifiedName(databaseColumnName);
            properties.setDisplayName(databaseColumnDisplayName);
            properties.setDescription(databaseColumnDescription);
            properties.setDataType(databaseColumnType);

            String databaseColumnGUID = client.createDatabaseColumn(userId, databaseManagerGUID, databaseManagerName, databaseTableGUID, properties);

            if (databaseColumnGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for columnCreate)");
            }

            DatabaseColumnElement    retrievedElement = client.getDatabaseColumnByGUID(userId, databaseColumnGUID);
            DatabaseColumnProperties retrievedColumn  = retrievedElement.getDatabaseColumnProperties();

            if (retrievedColumn == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseColumn from Retrieve)");
            }

            if (! databaseColumnName.equals(retrievedColumn.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! databaseColumnDisplayName.equals(retrievedColumn.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! databaseColumnDescription.equals(retrievedColumn.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }

            List<DatabaseColumnElement> databaseColumnList = client.getDatabaseColumnsByName(userId, databaseColumnName, 0, maxPageSize);

            if (databaseColumnList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseColumn for RetrieveByName)");
            }
            else if (databaseColumnList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty DatabaseColumn list for RetrieveByName)");
            }
            else if (databaseColumnList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(DatabaseColumn list for RetrieveByName contains" + databaseColumnList.size() +
                                                         " elements)");
            }

            retrievedElement = databaseColumnList.get(0);
            retrievedColumn = retrievedElement.getDatabaseColumnProperties();

            if (! databaseColumnName.equals(retrievedColumn.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! databaseColumnDisplayName.equals(retrievedColumn.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! databaseColumnDescription.equals(retrievedColumn.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            databaseColumnList = client.getColumnsForDatabaseTable(userId, databaseTableGUID, 0, maxPageSize);

            if (databaseColumnList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseColumn for getColumnsForDatabaseTable)");
            }
            else if (databaseColumnList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty DatabaseColumn list for getColumnsForDatabaseTable)");
            }
            else if (databaseColumnList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(DatabaseColumn list for getColumnsForDatabaseTable contains" + databaseColumnList.size() +
                                                         " elements)");
            }

            retrievedElement = databaseColumnList.get(0);
            retrievedColumn = retrievedElement.getDatabaseColumnProperties();

            if (! databaseColumnName.equals(retrievedColumn.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from getColumnsForDatabaseTable)");
            }
            if (! databaseColumnDisplayName.equals(retrievedColumn.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from getColumnsForDatabaseTable)");
            }
            if (! databaseColumnDescription.equals(retrievedColumn.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from getColumnsForDatabaseTable)");
            }

            return databaseColumnGUID;
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
