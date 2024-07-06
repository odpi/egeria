/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.fvt.databases;

import org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.MetadataSourceClient;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
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

    private final static String databaseName        = "TestDatabase";
    private final static String databaseDisplayName = "Database displayName";
    private final static String databaseDescription = "Database description";
    private final static String databaseType        = "Database type";
    private final static String databaseVersion     = "Database version";

    private final static String databaseSchemaName        = "TestDatabaseSchema";
    private final static String databaseSchemaDisplayName = "DatabaseSchema displayName";
    private final static String databaseSchemaDescription = "DatabaseSchema description";

    private final static String databaseTableName        = "TestDatabaseTable";
    private final static String databaseTableDisplayName = "DatabaseTable displayName";
    private final static String databaseTableDescription = "DatabaseTable description";


    private final static String databaseColumnName        = "TestDatabaseColumn";
    private final static String databaseColumnDisplayName = "DatabaseColumn displayName";
    private final static String databaseColumnDescription = "DatabaseColumn description";
    private final static String databaseColumnType = "string";

    private final static String databaseColumnTwoName        = "TestDatabaseColumn2";
    private final static String databaseColumnTwoDisplayName = "DatabaseColumn2 displayName";
    private final static String databaseColumnTwoDescription = "DatabaseColumn2 description";
    private final static String databaseColumnTwoType = "date";

    private final static String copyDatabaseName        = "TestDatabaseByCopy";
    private final static String copyDatabaseDisplayName = "Database displayName for copy";
    private final static String copyDatabaseDescription = "Database description for copy";


    /**
     * Run all the defined tests and capture the results.
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
     * Run all the tests in this class.
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
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceWiki());

        DatabaseManagerClient client = thisTest.getDatabaseManagerClient(serverName, serverPlatformRootURL, auditLog);

        String databaseManagerGUID = thisTest.getDatabaseManager(serverName, serverPlatformRootURL, userId, auditLog);
        String databaseGUID = thisTest.getDatabase(client, databaseManagerGUID, userId);
        String databaseSchemaGUID = thisTest.getDatabaseSchema(client, databaseManagerGUID, databaseGUID, userId);
        String databaseTableGUID = thisTest.createDatabaseTable(client, databaseManagerGUID, databaseSchemaGUID, userId);
        String databaseColumnGUID = thisTest.createDatabaseColumn(client, databaseManagerGUID, databaseTableGUID, userId);
        String copyDatabaseGUID = thisTest.getDatabaseFromTemplate(client, databaseManagerGUID, databaseGUID, userId);

        /*
         * Check that all elements are deleted when the database is deleted.
         */
        String activityName = "cascadedDelete";
        try
        {
            client.removeDatabase(userId, databaseManagerGUID, databaseManagerName, databaseGUID);

            thisTest.checkDatabaseGone(client, databaseGUID, activityName, userId);
            thisTest.checkDatabaseSchemaGone(client, databaseSchemaGUID, null, activityName, userId);
            thisTest.checkDatabaseTableGone(client, databaseTableGUID, null, activityName, userId);
            thisTest.checkDatabaseColumnGone(client, databaseColumnGUID, null, activityName, userId);

            activityName = "cascadedDelete - check template ok";
            thisTest.checkDatabaseOK(client, copyDatabaseGUID, copyDatabaseName, copyDatabaseDisplayName, copyDatabaseDescription, activityName, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }


        /*
         * Recreate database
         */
        activityName= "deleteOneByOne";

        databaseGUID = thisTest.getDatabase(client, databaseManagerGUID, userId);
        databaseSchemaGUID = thisTest.getDatabaseSchema(client, databaseManagerGUID, databaseGUID, userId);
        databaseTableGUID = thisTest.createDatabaseTable(client, databaseManagerGUID, databaseSchemaGUID, userId);
        databaseColumnGUID = thisTest.createDatabaseColumn(client, databaseManagerGUID, databaseTableGUID, userId);

        /*
         * Check that elements can be deleted one by one
         */
        try
        {
            activityName = "deleteOneByOne - pre-validate";
            thisTest.checkDatabaseColumnOK(client, databaseColumnGUID, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseTableOK(client, databaseTableGUID, databaseSchemaGUID, activityName, userId);
            thisTest.checkDatabaseSchemaOK(client, databaseSchemaGUID, databaseGUID, activityName, userId);
            thisTest.checkDatabaseOK(client, databaseGUID, databaseName, databaseDisplayName, databaseDescription, activityName, userId);

            client.removeDatabaseColumn(userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID);

            activityName = "deleteOneByOne - column gone";
            thisTest.checkDatabaseColumnGone(client, databaseColumnGUID, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseTableOK(client, databaseTableGUID, databaseSchemaGUID, activityName, userId);
            thisTest.checkDatabaseSchemaOK(client, databaseSchemaGUID, databaseGUID, activityName, userId);
            thisTest.checkDatabaseOK(client, databaseGUID, databaseName, databaseDisplayName, databaseDescription, activityName, userId);

            client.removeDatabaseTable(userId, databaseManagerGUID, databaseManagerName, databaseTableGUID);

            activityName = "deleteOneByOne - table gone";
            thisTest.checkDatabaseColumnGone(client, databaseColumnGUID, null, activityName, userId);
            thisTest.checkDatabaseTableGone(client, databaseTableGUID, databaseSchemaGUID, activityName, userId);
            thisTest.checkDatabaseSchemaOK(client, databaseSchemaGUID, databaseGUID, activityName, userId);
            thisTest.checkDatabaseOK(client, databaseGUID, databaseName, databaseDisplayName, databaseDescription, activityName, userId);

            client.removeDatabaseSchema(userId, databaseManagerGUID, databaseManagerName, databaseSchemaGUID);

            activityName = "deleteOneByOne - schema gone";
            thisTest.checkDatabaseColumnGone(client, databaseColumnGUID, null, activityName, userId);
            thisTest.checkDatabaseTableGone(client, databaseTableGUID, null, activityName, userId);
            thisTest.checkDatabaseSchemaGone(client, databaseSchemaGUID, databaseGUID, activityName, userId);
            thisTest.checkDatabaseOK(client, databaseGUID, databaseName, databaseDisplayName, databaseDescription, activityName, userId);

            client.removeDatabase(userId, databaseManagerGUID, databaseManagerName, databaseGUID);

            activityName = "deleteOneByOne - database gone";
            thisTest.checkDatabaseColumnGone(client, databaseColumnGUID, null, activityName, userId);
            thisTest.checkDatabaseTableGone(client, databaseTableGUID, null, activityName, userId);
            thisTest.checkDatabaseSchemaGone(client, databaseSchemaGUID, null, activityName, userId);
            thisTest.checkDatabaseGone(client, databaseGUID, activityName, userId);


            /*
             * Recreate database
             */
            databaseGUID = thisTest.getDatabase(client, databaseManagerGUID, userId);
            databaseSchemaGUID = thisTest.getDatabaseSchema(client, databaseManagerGUID, databaseGUID, userId);
            databaseTableGUID = thisTest.createDatabaseTable(client, databaseManagerGUID, databaseSchemaGUID, userId);
            databaseColumnGUID = thisTest.createDatabaseColumn(client, databaseManagerGUID, databaseTableGUID, userId);

            /*
             * Update tests
             */
            activityName = "updateNonExistentColumn";

            String databaseColumnTwoGUID = "Blah Blah";
            DatabaseColumnProperties databaseColumnTwoProperties = new DatabaseColumnProperties();
            databaseColumnTwoProperties.setQualifiedName(databaseColumnTwoName);
            databaseColumnTwoProperties.setDisplayName(databaseColumnDisplayName); // Note wrong value
            databaseColumnTwoProperties.setDescription(databaseColumnTwoDescription);
            databaseColumnTwoProperties.setDataType(databaseColumnType); // Note wrong value

            try
            {

                client.updateDatabaseColumn(userId, databaseManagerGUID, databaseManagerName, databaseColumnTwoGUID, true, databaseColumnTwoProperties);
                throw new FVTUnexpectedCondition(testCaseName, activityName);
            }
            catch (InvalidParameterException expectedError)
            {
                // very good
            }

            activityName = "updateColumnWithSameProperties";

            databaseColumnTwoGUID = client.createDatabaseColumn(userId, databaseManagerGUID, databaseManagerName, databaseTableGUID, databaseColumnTwoProperties);

            DatabaseColumnElement beforeElement = client.getDatabaseColumnByGUID(userId, databaseColumnTwoGUID);

            client.updateDatabaseColumn(userId, databaseManagerGUID, databaseManagerName, databaseColumnTwoGUID, false, databaseColumnTwoProperties);

            DatabaseColumnElement afterElement = client.getDatabaseColumnByGUID(userId, databaseColumnTwoGUID);

            /*
             * No change should occur in the version number because the properties are not different.
             */
            if (! beforeElement.getElementHeader().getVersions().equals(afterElement.getElementHeader().getVersions()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(version changed from " + beforeElement.getElementHeader().getVersions() + " to " + afterElement.getElementHeader().getVersions() + ")");
            }

            activityName = "updateColumnClassificationProperties";

            /*
             * This change effects the classification of the column
             */
            databaseColumnTwoProperties.setDataType(databaseColumnTwoType);

            client.updateDatabaseColumn(userId, databaseManagerGUID, databaseManagerName, databaseColumnTwoGUID, false, databaseColumnTwoProperties);

            afterElement = client.getDatabaseColumnByGUID(userId, databaseColumnTwoGUID);

            /*
             * No change should occur in the version number because the entity properties are not different.
             */
            if (! beforeElement.getElementHeader().getVersions().equals(afterElement.getElementHeader().getVersions()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(version changed from " + beforeElement.getElementHeader().getVersions() + " to " + afterElement.getElementHeader().getVersions() + ")");
            }

            if (! databaseColumnTwoType.equals(afterElement.getDatabaseColumnProperties().getDataType()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(data type should be " + databaseColumnTwoType + " rather than " + afterElement.getDatabaseColumnProperties().getDataType() + ")");
            }

            /*
             * This change affects the entity
             */
            activityName = "updateColumnProperties";

            databaseColumnTwoProperties.setDisplayName(databaseColumnTwoDisplayName);

            client.updateDatabaseColumn(userId, databaseManagerGUID, databaseManagerName, databaseColumnTwoGUID, false, databaseColumnTwoProperties);

            afterElement = client.getDatabaseColumnByGUID(userId, databaseColumnTwoGUID);

            /*
             * The change should have taken effect.
             */
            if (beforeElement.getElementHeader().getVersions().equals(afterElement.getElementHeader().getVersions()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(version did not change from " + beforeElement.getElementHeader().getVersions() + ")");
            }

            if (! databaseColumnTwoDisplayName.equals(afterElement.getDatabaseColumnProperties().getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(display name should be " + databaseColumnTwoDisplayName + " rather than " + afterElement.getDatabaseColumnProperties().getDisplayName() + ")");
            }

            /*
             * Check that all elements are deleted when the database is deleted.
             */
            activityName = "cascadedDelete";
            try
            {
                client.removeDatabase(userId, databaseManagerGUID, databaseManagerName, databaseGUID);

                thisTest.checkDatabaseGone(client, databaseGUID, activityName, userId);
                thisTest.checkDatabaseSchemaGone(client, databaseSchemaGUID, null, activityName, userId);
                thisTest.checkDatabaseTableGone(client, databaseTableGUID, null, activityName, userId);
                thisTest.checkDatabaseColumnGone(client, databaseColumnGUID, null, activityName, userId);
                thisTest.checkDatabaseColumnGone(client, databaseColumnTwoGUID, null, activityName, userId);
            }
            catch (Exception unexpectedError)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
            }


            /*
             * Working with database schema types

             * Recreate database
             */
            activityName= "schemaType test";

            databaseGUID = thisTest.getDatabase(client, databaseManagerGUID, userId);
            databaseSchemaGUID = thisTest.getDatabaseSchema(client, databaseManagerGUID, databaseGUID, userId);

            String databaseSchemaTypeGUID = client.createDatabaseSchemaType(userId, databaseManagerGUID, databaseManagerName, "SchemaOf:" + databaseSchemaName);

            databaseTableGUID = thisTest.createDatabaseTableForSchemaType(client, databaseManagerGUID, databaseSchemaTypeGUID, userId);
            databaseColumnGUID = thisTest.createDatabaseColumn(client, databaseManagerGUID, databaseTableGUID, userId);

            client.attachSchemaTypeToDatabaseAsset(userId, databaseManagerGUID, databaseManagerName, databaseSchemaGUID, databaseSchemaTypeGUID);

            /*
             * Check that all elements are deleted when the database is deleted.
             */
            activityName = "cascadedDelete for SchemaType";
            try
            {
                client.removeDatabase(userId, databaseManagerGUID, databaseManagerName, databaseGUID);

                thisTest.checkDatabaseGone(client, databaseGUID, activityName, userId);
                thisTest.checkDatabaseSchemaGone(client, databaseSchemaGUID, null, activityName, userId);
                thisTest.checkDatabaseTableGone(client, databaseTableGUID, null, activityName, userId);
                thisTest.checkDatabaseColumnGone(client, databaseColumnGUID, null, activityName, userId);
            }
            catch (Exception unexpectedError)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
            }
        }
        catch (Exception unexpectedError)
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
            DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL, auditLog);

            return new DatabaseManagerClient(serverName, serverPlatformRootURL, restClient, maxPageSize);
        }
        catch (Exception unexpectedError)
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
            MetadataSourceClient  client     = new MetadataSourceClient(serverName, serverPlatformRootURL, restClient, maxPageSize);

            DatabaseManagerProperties properties = new DatabaseManagerProperties();
            properties.setQualifiedName(databaseManagerName);
            properties.setDisplayName(databaseManagerDisplayName);
            properties.setDescription(databaseManagerDescription);
            properties.setDeployedImplementationType(databaseManagerTypeDescription);
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
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Check a database is gone.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseGUID unique id of the database to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseGone(DatabaseManagerClient client,
                                   String                databaseGUID,
                                   String                activityName,
                                   String                userId) throws FVTUnexpectedCondition
    {
        try
        {
            DatabaseElement retrievedElement = client.getDatabaseByGUID(userId, databaseGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Database returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Retrieve returned)");
        }
        catch (InvalidParameterException expectedException)
        {
            // all ok
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
     * Check database is ok.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseGUID unique id of the database
     * @param qualifiedName expected qualified name
     * @param name expected name
     * @param description expected description
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseOK(DatabaseManagerClient client,
                                 String                databaseGUID,
                                 String                qualifiedName,
                                 String                name,
                                 String                description,
                                 String                activityName,
                                 String                userId) throws FVTUnexpectedCondition
    {
        try
        {
            DatabaseElement retrievedElement = client.getDatabaseByGUID(userId, databaseGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseElement from Retrieve)");
            }

            DatabaseProperties retrievedDatabase = retrievedElement.getDatabaseProperties();

            if (retrievedDatabase == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseProperties from Retrieve)");
            }

            if (! qualifiedName.equals(retrievedDatabase.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! name.equals(retrievedDatabase.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! description.equals(retrievedDatabase.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            if (! databaseType.equals(retrievedDatabase.getDeployedImplementationType()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseType from Retrieve " + retrievedDatabase.getDeployedImplementationType() + ")");
            }
            if (! databaseVersion.equals(retrievedDatabase.getDatabaseVersion()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseVersion from Retrieve)");
            }

            List<DatabaseElement> databaseList = client.getDatabasesByName(userId, qualifiedName, 0, maxPageSize);

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

            if (! qualifiedName.equals(retrievedDatabase.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! name.equals(retrievedDatabase.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! description.equals(retrievedDatabase.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }
            if (! databaseType.equals(retrievedDatabase.getDeployedImplementationType()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseType from RetrieveByName)");
            }
            if (! databaseVersion.equals(retrievedDatabase.getDatabaseVersion()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseVersion from RetrieveByName)");
            }

            databaseList = client.getDatabasesByName(userId, qualifiedName, 1, maxPageSize);

            if (databaseList != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(too many Databases for RetrieveByName(1))");
            }
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
     * Create a database and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the database manager
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    @SuppressWarnings(value = "deprecation")
    private String getDatabase(DatabaseManagerClient client,
                               String                databaseManagerGUID,
                               String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabase";

        try
        {
            DatabaseProperties properties = new DatabaseProperties();

            properties.setQualifiedName(databaseName);
            properties.setName(databaseDisplayName); // check deprecated method still works
            properties.setDescription(databaseDescription);
            properties.setDeployedImplementationType(databaseType);
            properties.setDatabaseVersion(databaseVersion);

            String databaseGUID = client.createDatabase(userId, databaseManagerGUID, databaseManagerName, properties);

            if (databaseGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }
            else
            {
                checkDatabaseOK(client, databaseGUID, databaseName, databaseDisplayName, databaseDescription, activityName, userId);
            }

            return databaseGUID;
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
     * Create a database and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the database manager
     * @param databaseGUID unique id of the database to copy
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDatabaseFromTemplate(DatabaseManagerClient client,
                                           String                databaseManagerGUID,
                                           String                databaseGUID,
                                           String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabaseFromTemplate";

        try
        {
            TemplateProperties properties = new TemplateProperties();

            properties.setQualifiedName(copyDatabaseName);
            properties.setDisplayName(copyDatabaseDisplayName);
            properties.setDescription(copyDatabaseDescription);

            String copyDatabaseGUID = client.createDatabaseFromTemplate(userId, databaseManagerGUID, databaseManagerName, databaseGUID, properties);

            if (copyDatabaseGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for CreateByTemplate)");
            }
            else
            {
                checkDatabaseOK(client, copyDatabaseGUID, copyDatabaseName, copyDatabaseDisplayName, copyDatabaseDescription, activityName, userId);

                List<DatabaseSchemaElement> databaseSchemaList = client.getSchemasForDatabase(userId, databaseGUID, 0, 0);

                if (databaseSchemaList == null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseSchema for CreateByTemplate)");
                }
                else if (databaseSchemaList.isEmpty())
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty DatabaseSchema list for CreateByTemplate)");
                }
                else if (databaseSchemaList.size() != 1)
                {
                    throw new FVTUnexpectedCondition(testCaseName,
                                                     activityName + "(DatabaseSchema list for CreateByTemplate contains" + databaseSchemaList.size() +
                                                             " elements)");
                }
            }

            return copyDatabaseGUID;
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
     * Check a database column is gone.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseSchemaGUID unique id of the database schema to test
     * @param databaseGUID unique id of the database to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseSchemaGone(DatabaseManagerClient client,
                                         String                databaseSchemaGUID,
                                         String                databaseGUID,
                                         String                activityName,
                                         String                userId) throws FVTUnexpectedCondition
    {
        try
        {
            DatabaseSchemaElement retrievedElement = client.getDatabaseSchemaByGUID(userId, databaseSchemaGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(DatabaseSchema returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(getDatabaseSchemaByGUID returned");
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (InvalidParameterException expectedError)
        {
            // all ok
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }

        if (databaseGUID != null)
        {
            try
            {
                /*
                 * Only one schema created so nothing should be tied to the database.
                 */
                List<DatabaseSchemaElement> databaseSchemaList = client.getSchemasForDatabase(userId, databaseGUID, 0, maxPageSize);

                if (databaseSchemaList != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(DatabaseSchema returned for getSchemasForDatabase)");
                }
            }
            catch(FVTUnexpectedCondition testCaseError)
            {
                throw testCaseError;
            }
            catch(Exception unexpectedError)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
            }
        }
    }


    /**
     * Check a database schema is correctly stored.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseSchemaGUID unique id of the database schema
     * @param databaseGUID unique id of the database
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void   checkDatabaseSchemaOK(DatabaseManagerClient client,
                                         String                databaseSchemaGUID,
                                         String                databaseGUID,
                                         String                activityName,
                                         String                userId) throws FVTUnexpectedCondition
    {
        try
        {
            DatabaseSchemaElement    retrievedElement  = client.getDatabaseSchemaByGUID(userId, databaseSchemaGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseSchemaElement from Retrieve)");
            }

            DatabaseSchemaProperties retrievedSchema = retrievedElement.getDatabaseSchemaProperties();

            if (retrievedSchema == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseSchemaProperties from Retrieve)");
            }

            if (! databaseSchemaName.equals(retrievedSchema.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! databaseSchemaDisplayName.equals(retrievedSchema.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! databaseSchemaDescription.equals(retrievedSchema.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
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
            if (! databaseSchemaDisplayName.equals(retrievedSchema.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! databaseSchemaDescription.equals(retrievedSchema.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            databaseSchemaList = client.getSchemasForDatabase(userId, databaseGUID, 0, maxPageSize);

            if (databaseSchemaList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseSchema for getSchemasForDatabase)");
            }
            else if (databaseSchemaList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty DatabaseSchema list for getSchemasForDatabase)");
            }
            else if (databaseSchemaList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(DatabaseSchema list for getSchemasForDatabase contains" + databaseSchemaList.size() +
                                                         " elements)");
            }
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
            properties.setName(databaseSchemaDisplayName);
            properties.setDescription(databaseSchemaDescription);

            String databaseSchemaGUID = client.createDatabaseSchema(userId, databaseManagerGUID, databaseManagerName, databaseGUID, properties);

            if (databaseSchemaGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for schemaCreate)");
            }
            else
            {
                checkDatabaseSchemaOK(client, databaseSchemaGUID, databaseGUID, activityName, userId);
            }

            return databaseSchemaGUID;
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
     * Check a database table is gone.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseTableGUID unique id of the database table to test
     * @param databaseSchemaGUID unique id of the database schema to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseTableGone(DatabaseManagerClient client,
                                        String                databaseTableGUID,
                                        String                databaseSchemaGUID,
                                        String                activityName,
                                        String                userId) throws FVTUnexpectedCondition
    {
        try
        {
            DatabaseTableElement retrievedElement = client.getDatabaseTableByGUID(userId, databaseTableGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(DatabaseTable returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(getDatabaseTableByGUID returned");
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (InvalidParameterException expectedError)
        {
            // all ok
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }

        if (databaseSchemaGUID != null)
        {
            try
            {
                /*
                 * Only one table created so nothing should be tied to the schema.
                 */
                List<DatabaseTableElement> databaseTableList = client.getTablesForDatabaseAsset(userId, databaseSchemaGUID, 0, maxPageSize);

                if (databaseTableList != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(DatabaseTable returned for getTablesForDatabaseAsset)");
                }
            }
            catch(FVTUnexpectedCondition testCaseError)
            {
                throw testCaseError;
            }
            catch(Exception unexpectedError)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
            }
        }
    }




    /**
     * Check a database table is stored OK.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseTableGUID unique id of the databaseSchema
     * @param databaseSchemaGUID unique id of the databaseSchema
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseTableOK(DatabaseManagerClient client,
                                      String                databaseTableGUID,
                                      String                databaseSchemaGUID,
                                      String                activityName,
                                      String                userId) throws FVTUnexpectedCondition
    {

        try
        {
            DatabaseTableElement    retrievedElement = client.getDatabaseTableByGUID(userId, databaseTableGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseTableElement from Retrieve)");
            }

            DatabaseTableProperties retrievedTable  = retrievedElement.getDatabaseTableProperties();

            if (retrievedTable == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseTableProperties from Retrieve)");
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

            if (databaseSchemaGUID != null)
            {
                databaseTableList = client.getTablesForDatabaseAsset(userId, databaseSchemaGUID, 0, maxPageSize);

                if (databaseTableList == null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseTable for getTablesForDatabaseAsset)");
                }
                else if (databaseTableList.isEmpty())
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty DatabaseTable list for getTablesForDatabaseAsset)");
                }
                else if (databaseTableList.size() != 1)
                {
                    throw new FVTUnexpectedCondition(testCaseName,
                                                     activityName + "(DatabaseColumn list for getTablesForDatabaseAsset contains" + databaseTableList.size() +
                                                             " elements)");
                }
            }
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
     * Create a database table and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the database manager
     * @param databaseSchemaGUID unique id of the databaseSchema
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createDatabaseTable(DatabaseManagerClient client,
                                       String                databaseManagerGUID,
                                       String                databaseSchemaGUID,
                                       String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createDatabaseTable";

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
            else
            {
                checkDatabaseTableOK(client, databaseTableGUID, databaseSchemaGUID, activityName, userId);
            }

            return databaseTableGUID;
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
     * Create a database table and attach it to the supplied schema type and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the database manager
     * @param databaseSchemaTypeGUID unique id of the databaseSchemaType
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createDatabaseTableForSchemaType(DatabaseManagerClient client,
                                                    String                databaseManagerGUID,
                                                    String                databaseSchemaTypeGUID,
                                                    String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createDatabaseTableForSchemaType";

        try
        {
            DatabaseTableProperties properties = new DatabaseTableProperties();

            properties.setQualifiedName(databaseTableName);
            properties.setDisplayName(databaseTableDisplayName);
            properties.setDescription(databaseTableDescription);


            String databaseTableGUID = client.createDatabaseTableForSchemaType(userId, databaseManagerGUID, databaseManagerName, databaseSchemaTypeGUID, properties);

            if (databaseTableGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for tableCreate)");
            }
            else
            {
                checkDatabaseTableOK(client, databaseTableGUID, null, activityName, userId);
            }

            return databaseTableGUID;
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
     * Check a database column is gone.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseColumnGUID unique id of the database column to test
     * @param databaseTableGUID unique id of the database table to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseColumnGone(DatabaseManagerClient client,
                                         String                databaseColumnGUID,
                                         String                databaseTableGUID,
                                         String                activityName,
                                         String                userId) throws FVTUnexpectedCondition
    {
        try
        {
            DatabaseColumnElement retrievedElement = client.getDatabaseColumnByGUID(userId, databaseColumnGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(DatabaseColumn returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(getDatabaseColumnByGUID returned");
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (InvalidParameterException expectedError)
        {
            // all ok
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }

        if (databaseTableGUID != null)
        {
            try
            {
                /*
                 * Only one column created so nothing should be tied to the table.
                 */
                List<DatabaseColumnElement> databaseColumnList = client.getColumnsForDatabaseTable(userId, databaseTableGUID, 0, maxPageSize);

                if (databaseColumnList != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(DatabaseColumn returned for getColumnsForDatabaseTable)");
                }
            }
            catch(FVTUnexpectedCondition testCaseError)
            {
                throw testCaseError;
            }
            catch(Exception unexpectedError)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
            }
        }
    }


    /**
     * Create a database column and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseColumnGUID unique id of the database column to test
     * @param databaseTableGUID unique id of the database table to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseColumnOK(DatabaseManagerClient client,
                                       String                databaseColumnGUID,
                                       String                databaseTableGUID,
                                       String                activityName,
                                       String                userId) throws FVTUnexpectedCondition
    {
        try
        {
            DatabaseColumnElement  retrievedElement = client.getDatabaseColumnByGUID(userId, databaseColumnGUID);
            
            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseColumnElement from Retrieve)");
            }
            
            DatabaseColumnProperties retrievedColumn  = retrievedElement.getDatabaseColumnProperties();

            if (retrievedColumn == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseColumnProperties from Retrieve)");
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
     * Create a database column and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the database manager
     * @param databaseTableGUID unique id of the database table to connect the column to
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createDatabaseColumn(DatabaseManagerClient client,
                                        String                databaseManagerGUID,
                                        String                databaseTableGUID,
                                        String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createDatabaseColumn";

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
            else
            {
                checkDatabaseColumnOK(client, databaseColumnGUID, databaseTableGUID, activityName, userId);
            }

            return databaseColumnGUID;
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
}
