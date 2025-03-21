/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.fvt.dataassets;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.DataAssetExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.ExternalAssetManagerClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.DataAssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.CorrelatedMetadataElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelationshipElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.AssetManagerProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CreateDatabaseTest calls the DataAssetClient to create a database with schemas tables and columns
 * and then retrieve the results.
 */
public class CreateDatabaseTest
{
    private final static String testCaseName       = "CreateDatabaseTest";

    private final static int    maxPageSize        = 100;

    /*
     * The asset manager name is constant - the guid is created as part of the test.
     */
    private final static String assetManagerName            = "TestDatabaseManager";
    private final static String assetManagerDisplayName     = "DatabaseManager displayName";
    private final static String assetManagerDescription     = "DatabaseManager description";
    private final static String assetManagerTypeDescription = "DatabaseManager type";
    private final static String assetManagerVersion         = "DatabaseManager version";

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
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceWiki());

        DataAssetExchangeClient client = thisTest.getDataAssetExchangeClient(serverName, serverPlatformRootURL, auditLog);

        String assetManagerGUID = thisTest.getAssetManager(serverName, serverPlatformRootURL, userId, auditLog);
        String databaseGUID = thisTest.getDatabase(client, assetManagerGUID, userId);
        String databaseSchemaGUID = thisTest.getDatabaseSchema(client, assetManagerGUID, databaseGUID, userId);
        String databaseTableGUID = thisTest.createDatabaseTable(client, assetManagerGUID, databaseSchemaGUID, userId);
        String databaseColumnGUID = thisTest.createDatabaseColumn(client, assetManagerGUID, databaseTableGUID, userId);

        String activityName = "cascadedDelete - remove Database";

        System.out.println("activityName = " + activityName);
        System.out.println("databaseGUID = " + databaseGUID);
        System.out.println("databaseSchemaGUID = " + databaseSchemaGUID);
        System.out.println("databaseTableGUID = " + databaseTableGUID);
        System.out.println("databaseColumnGUID = " + databaseColumnGUID);

        /*
         * Check that all elements are deleted when the database is deleted.
         */
        try
        {
            client.removeDataAsset(userId, assetManagerGUID, assetManagerName, databaseGUID, null, null, false, false);

            thisTest.checkDatabaseGone(client, assetManagerGUID, databaseGUID, activityName, userId);
            thisTest.checkDatabaseColumnOK(client, assetManagerGUID, databaseColumnGUID, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseTableOK(client, assetManagerGUID, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseSchemaOK(client, assetManagerGUID, databaseSchemaGUID, null, activityName, userId);

            activityName = "cascadedDelete - remove DatabaseSchema";

            client.removeDataAsset(userId, assetManagerGUID, assetManagerName, databaseSchemaGUID, null, null, false, false);

            thisTest.checkDatabaseSchemaGone(client, assetManagerGUID, databaseSchemaGUID, null, activityName, userId);
            thisTest.checkDatabaseTableGone(client, assetManagerGUID, databaseTableGUID,  activityName, userId);
            thisTest.checkDatabaseColumnGone(client, assetManagerGUID, databaseColumnGUID, null, activityName, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }


        /*
         * Recreate database
         */
        activityName= "deleteOneByOne";

        System.out.println("activityName = " + activityName);
        databaseGUID = thisTest.getDatabase(client, assetManagerGUID, userId);
        databaseSchemaGUID = thisTest.getDatabaseSchema(client, assetManagerGUID, databaseGUID, userId);
        databaseTableGUID = thisTest.createDatabaseTable(client, assetManagerGUID, databaseSchemaGUID, userId);
        databaseColumnGUID = thisTest.createDatabaseColumn(client, assetManagerGUID, databaseTableGUID, userId);

        System.out.println("databaseGUID = " + databaseGUID);
        System.out.println("databaseSchemaGUID = " + databaseSchemaGUID);
        System.out.println("databaseTableGUID = " + databaseTableGUID);
        System.out.println("databaseColumnGUID = " + databaseColumnGUID);


        /*
         * Check that elements can be deleted one by one
         */

        try
        {
            activityName = "deleteOneByOne - prevalidate";
            thisTest.checkDatabaseColumnOK(client, assetManagerGUID, databaseColumnGUID, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseTableOK(client, assetManagerGUID, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseSchemaOK(client, assetManagerGUID, databaseSchemaGUID, databaseGUID, activityName, userId);
            thisTest.checkDatabaseOK(client, assetManagerGUID, databaseGUID, activityName, userId);

            client.removeSchemaAttribute(userId, assetManagerGUID, assetManagerName, databaseColumnGUID, null, null, false, false);

            activityName = "deleteOneByOne - column gone";
            thisTest.checkDatabaseColumnGone(client, assetManagerGUID, databaseColumnGUID, databaseTableGUID, activityName, userId);

            activityName = "deleteOneByOne - column gone - check table: " + databaseTableGUID;

            thisTest.checkDatabaseTableOK(client, assetManagerGUID, databaseTableGUID, activityName, userId);

            activityName = "deleteOneByOne - column gone - check schema";

            thisTest.checkDatabaseSchemaOK(client, assetManagerGUID, databaseSchemaGUID, databaseGUID, activityName, userId);

            activityName = "deleteOneByOne - column gone - check DB";

            thisTest.checkDatabaseOK(client, assetManagerGUID, databaseGUID, activityName, userId);

            activityName = "deleteOneByOne - remove table";

            client.removeSchemaAttribute(userId, assetManagerGUID, assetManagerName, databaseTableGUID, null, null, false, false);

            activityName = "deleteOneByOne - table gone";
            thisTest.checkDatabaseTableGone(client, assetManagerGUID, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseSchemaOK(client, assetManagerGUID, databaseSchemaGUID, databaseGUID, activityName, userId);
            thisTest.checkDatabaseOK(client, assetManagerGUID, databaseGUID, activityName, userId);

            client.removeDataAsset(userId, assetManagerGUID, assetManagerName, databaseSchemaGUID, null, null, false, false);

            activityName = "deleteOneByOne - schema gone";
            thisTest.checkDatabaseSchemaGone(client, assetManagerGUID, databaseSchemaGUID, databaseGUID, activityName, userId);
            thisTest.checkDatabaseOK(client, assetManagerGUID, databaseGUID, activityName, userId);

            client.removeDataAsset(userId, assetManagerGUID, assetManagerName, databaseGUID, null, null, false, false);

            activityName = "deleteOneByOne - database gone";
            thisTest.checkDatabaseGone(client, assetManagerGUID, databaseGUID, activityName, userId);

            /*
             * Recreate database
             */
            activityName= "deleteOneByOne";

            databaseGUID = thisTest.getDatabase(client, assetManagerGUID, userId);
            databaseSchemaGUID = thisTest.getDatabaseSchema(client, assetManagerGUID, databaseGUID, userId);
            databaseTableGUID = thisTest.createDatabaseTable(client, assetManagerGUID, databaseSchemaGUID, userId);
            databaseColumnGUID = thisTest.createDatabaseColumn(client, assetManagerGUID, databaseTableGUID, userId);

            System.out.println("databaseGUID = " + databaseGUID);
            System.out.println("databaseSchemaGUID = " + databaseSchemaGUID);
            System.out.println("databaseTableGUID = " + databaseTableGUID);
            System.out.println("databaseColumnGUID = " + databaseColumnGUID);

            /*
             * Update tests
             */
            activityName = "updateNonExistentColumn";

            String                    databaseColumnTwoGUID       = "Blah Blah";
            SchemaAttributeProperties databaseColumnTwoProperties = new SchemaAttributeProperties();
            databaseColumnTwoProperties.setQualifiedName(databaseColumnTwoName);
            databaseColumnTwoProperties.setDisplayName(databaseColumnDisplayName); // Note wrong value
            databaseColumnTwoProperties.setDescription(databaseColumnTwoDescription);

            try
            {

                client.updateSchemaAttribute(userId, assetManagerGUID, assetManagerName, databaseColumnTwoGUID, null, true, databaseColumnTwoProperties, null, false ,false);
                throw new FVTUnexpectedCondition(testCaseName, activityName);
            }
            catch (InvalidParameterException expectedError)
            {
                // very good
            }

            activityName = "updateColumnWithSameProperties";

            databaseColumnTwoGUID = client.createSchemaAttribute(userId, assetManagerGUID, assetManagerName, true, databaseTableGUID, null, databaseColumnTwoProperties, null, false, false);

            SchemaAttributeElement beforeElement = client.getSchemaAttributeByGUID(userId, assetManagerGUID, assetManagerName, databaseColumnTwoGUID, null, false, false);

            client.updateSchemaAttribute(userId, assetManagerGUID, assetManagerName, databaseColumnTwoGUID, null, true, databaseColumnTwoProperties,null, false, false);

            SchemaAttributeElement afterElement = client.getSchemaAttributeByGUID(userId, assetManagerGUID, assetManagerName, databaseColumnTwoGUID, null, false, false);

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
            SchemaTypeProperties schemaType = new SchemaTypeProperties();

            Map<String,Object> extendedProperties = new HashMap<>();

            extendedProperties.put("dataType", databaseColumnTwoType);

            schemaType.setExtendedProperties(extendedProperties);
            databaseColumnTwoProperties.setSchemaType(schemaType);
            client.updateSchemaAttribute(userId, assetManagerGUID, assetManagerName, databaseColumnTwoGUID, null, true, databaseColumnTwoProperties, null, false, false);

            afterElement = client.getSchemaAttributeByGUID(userId, null, null, databaseColumnTwoGUID, null, false, false);

            /*
             * No change should occur in the version number because the entity properties are not different.
             */
            if (! beforeElement.getElementHeader().getVersions().equals(afterElement.getElementHeader().getVersions()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(version changed from " + beforeElement.getElementHeader().getVersions() + " to " + afterElement.getElementHeader().getVersions() + ")");
            }

            Object dataType = afterElement.getSchemaAttributeProperties().getSchemaType().getExtendedProperties().get("dataType");
            if (! databaseColumnTwoType.equals(dataType))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(data type should be " + databaseColumnTwoType + " rather than " + dataType + "). Returned element: " + afterElement);
            }

            /*
             * This change affects the entity
             */
            activityName = "updateColumnProperties";

            databaseColumnTwoProperties.setDisplayName(databaseColumnTwoDisplayName);

            client.updateSchemaAttribute(userId, assetManagerGUID, assetManagerName, databaseColumnTwoGUID, null, true,  databaseColumnTwoProperties, null, false, false);

            afterElement = client.getSchemaAttributeByGUID(userId, null, null, databaseColumnTwoGUID, null, false, false);

            /*
             * The change should have taken effect.
             */
            if (beforeElement.getElementHeader().getVersions().equals(afterElement.getElementHeader().getVersions()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(version did not change from " + beforeElement.getElementHeader().getVersions() + ")");
            }

            if (! databaseColumnTwoDisplayName.equals(afterElement.getSchemaAttributeProperties().getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(display name should be " + databaseColumnTwoDisplayName + " rather than " + afterElement.getSchemaAttributeProperties().getDisplayName() + ")");
            }

            /*
             * Check that all elements are deleted when the database is deleted.
             */
            activityName = "cascadedDelete";
            try
            {
                client.removeDataAsset(userId, assetManagerGUID, assetManagerName, databaseGUID, null, null, false, false);

                thisTest.checkDatabaseGone(client, assetManagerGUID, databaseGUID, activityName, userId);

                client.removeDataAsset(userId, assetManagerGUID, assetManagerName, databaseSchemaGUID, null, null, false, false);

                thisTest.checkDatabaseSchemaGone(client, assetManagerGUID, databaseSchemaGUID, null, activityName, userId);
                thisTest.checkDatabaseTableGone(client, assetManagerGUID, databaseTableGUID, activityName, userId);
                thisTest.checkDatabaseColumnGone(client, assetManagerGUID, databaseColumnGUID, null, activityName, userId);
                thisTest.checkDatabaseColumnGone(client, assetManagerGUID, databaseColumnTwoGUID, null, activityName, userId);
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
     * Create and return a client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private DataAssetExchangeClient getDataAssetExchangeClient(String   serverName,
                                                               String   serverPlatformRootURL,
                                                               AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getAssetOwnerClient";

        try
        {
            AssetManagerRESTClient restClient = new AssetManagerRESTClient(serverName, serverPlatformRootURL);

            return new DataAssetExchangeClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
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
    private String getAssetManager(String   serverName,
                                   String   serverPlatformRootURL,
                                   String   userId,
                                   AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getAssetManager";

        try
        {
            AssetManagerRESTClient     restClient = new AssetManagerRESTClient(serverName, serverPlatformRootURL);
            ExternalAssetManagerClient client     = new ExternalAssetManagerClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);

            AssetManagerProperties properties = new AssetManagerProperties();
            properties.setQualifiedName(assetManagerName);
            properties.setResourceName(assetManagerDisplayName);
            properties.setResourceDescription(assetManagerDescription);
            properties.setDeployedImplementationType(assetManagerTypeDescription);
            properties.setVersion(assetManagerVersion);

            String assetManagerGUID = client.createExternalAssetManager(userId, properties);

            if (assetManagerGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            String retrievedAssetManagerGUID = client.getExternalAssetManagerGUID(userId, assetManagerName);

            if (retrievedAssetManagerGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Retrieve)");
            }

            if (! retrievedAssetManagerGUID.equals(assetManagerGUID))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Inconsistent GUIDs)");
            }

            return assetManagerGUID;
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
     * @param client interface to Asset Manager OMAS
     * @param databaseGUID unique id of the database to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseGone(DataAssetExchangeClient client,
                                   String                  assetManagerGUID,
                                   String                  databaseGUID,
                                   String                  activityName,
                                   String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            DataAssetElement retrievedElement = client.getDataAssetByGUID(userId, assetManagerGUID, assetManagerName, databaseGUID, null, false, false);

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
     * @param client interface to Asset Manager OMAS
     * @param databaseGUID unique id of the database
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseOK(DataAssetExchangeClient client,
                                 String                  assetManagerGUID,
                                 String                  databaseGUID,
                                 String                  activityName,
                                 String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            DataAssetElement retrievedElement = client.getDataAssetByGUID(userId, assetManagerGUID, assetManagerName, databaseGUID, null, false, false);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseElement from Retrieve)");
            }

            DataAssetProperties retrievedDatabase = retrievedElement.getDataAssetProperties();

            if (retrievedDatabase == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseProperties from Retrieve)");
            }

            if (! databaseName.equals(retrievedDatabase.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! databaseDisplayName.equals(retrievedDatabase.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! databaseDescription.equals(retrievedDatabase.getDisplayDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }

            Map<String, Object> databaseExtendedProperties = retrievedDatabase.getExtendedProperties();

            if (! databaseType.equals(databaseExtendedProperties.get("deployedImplementationType")))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseType from Retrieve).  Retrieve Element: " + retrievedDatabase);
            }
            if (! databaseVersion.equals(databaseExtendedProperties.get("databaseVersion")))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseVersion from Retrieve).  Retrieve Element: " + retrievedDatabase);
            }

            List<DataAssetElement> databaseList = client.getDataAssetsByName(userId, assetManagerGUID, assetManagerName, databaseName, 0, maxPageSize, null, false, false);

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
            retrievedDatabase = retrievedElement.getDataAssetProperties();

            if (! databaseName.equals(retrievedDatabase.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! databaseDisplayName.equals(retrievedDatabase.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! databaseDescription.equals(retrievedDatabase.getDisplayDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            databaseExtendedProperties = retrievedDatabase.getExtendedProperties();

            if (! databaseType.equals(databaseExtendedProperties.get("deployedImplementationType")))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseType from RetrieveByName).  Retrieve Element: " + retrievedDatabase);
            }
            if (! databaseVersion.equals(databaseExtendedProperties.get("databaseVersion")))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseVersion from RetrieveByName).  Retrieve Element: " + retrievedDatabase);
            }

            databaseList = client.getDataAssetsByName(userId, assetManagerGUID, assetManagerName, databaseName, 1, maxPageSize, null, false, false);

            if (databaseList != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Database for RetrieveByName)");
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
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the database manager
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDatabase(DataAssetExchangeClient client,
                               String                  assetManagerGUID,
                               String                  userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabase";

        try
        {
            DataAssetProperties properties = new DataAssetProperties();

            properties.setQualifiedName(databaseName);
            properties.setDisplayName(databaseDisplayName);
            properties.setDisplayDescription(databaseDescription);

            properties.setTypeName(OpenMetadataType.DATABASE.typeName);

            Map<String, Object> extendedProperties = new HashMap<>();
            extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, databaseType);
            extendedProperties.put(OpenMetadataProperty.DATABASE_VERSION.name, databaseVersion);
            properties.setExtendedProperties(extendedProperties);

            String databaseGUID = client.createDataAsset(userId, assetManagerGUID, assetManagerName, true, null, properties);

            if (databaseGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }
            else
            {
                checkDatabaseOK(client, assetManagerGUID, databaseGUID, activityName, userId);
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
     * Check a database column is gone.
     *
     * @param client interface to Asset Manager OMAS
     * @param databaseSchemaGUID unique id of the database schema to test
     * @param databaseGUID unique id of the database to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseSchemaGone(DataAssetExchangeClient client,
                                         String                  assetManagerGUID,
                                         String                  databaseSchemaGUID,
                                         String                  databaseGUID,
                                         String                  activityName,
                                         String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            DataAssetElement retrievedElement = client.getDataAssetByGUID(userId, assetManagerGUID, assetManagerName, databaseSchemaGUID, null, false, false);

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
                List<RelationshipElement> relationshipList = client.getRelatedAssetsAtEnd1(userId, assetManagerGUID, assetManagerName, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, databaseGUID, 0, maxPageSize, null, false, false);

                if (relationshipList != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(No DatabaseSchema returned for getSchemasForDatabase)");
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
     * @param client interface to Asset Manager OMAS
     * @param databaseSchemaGUID unique id of the database schema
     * @param databaseGUID unique id of the database
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void   checkDatabaseSchemaOK(DataAssetExchangeClient client,
                                         String                  assetManagerGUID,
                                         String                  databaseSchemaGUID,
                                         String                  databaseGUID,
                                         String                  activityName,
                                         String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            DataAssetElement retrievedElement = client.getDataAssetByGUID(userId, assetManagerGUID, assetManagerName, databaseSchemaGUID, null, false,
                                                                          false);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseSchemaElement from Retrieve)");
            }

            validateAnchorGUID(activityName, retrievedElement);

            DataAssetProperties retrievedSchema = retrievedElement.getDataAssetProperties();

            if (retrievedSchema == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseSchemaProperties from Retrieve)");
            }

            if (! databaseSchemaName.equals(retrievedSchema.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! databaseSchemaDisplayName.equals(retrievedSchema.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! databaseSchemaDescription.equals(retrievedSchema.getDisplayDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }


            List<DataAssetElement> databaseSchemaList = client.getDataAssetsByName(userId, assetManagerGUID, assetManagerName, databaseSchemaName, 0,
                                                                                   maxPageSize, null, false, false);

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
            retrievedSchema = retrievedElement.getDataAssetProperties();

            if (! databaseSchemaName.equals(retrievedSchema.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! databaseSchemaDisplayName.equals(retrievedSchema.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! databaseSchemaDescription.equals(retrievedSchema.getDisplayDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            if (databaseGUID != null)
            {
                List<RelationshipElement> relationshipElements = client.getRelatedAssetsAtEnd1(userId, assetManagerGUID, assetManagerName,
                                                                                               OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, databaseGUID, 0, maxPageSize,
                                                                                               null, false, false);

                if (relationshipElements == null)
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
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the database manager
     * @param databaseGUID unique id of the database
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDatabaseSchema(DataAssetExchangeClient client,
                                     String                  assetManagerGUID,
                                     String                  databaseGUID,
                                     String                  userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabaseSchema";

        try
        {
            DataAssetProperties properties = new DataAssetProperties();

            properties.setQualifiedName(databaseSchemaName);
            properties.setDisplayName(databaseSchemaDisplayName);
            properties.setDisplayDescription(databaseSchemaDescription);
            properties.setTypeName(OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName);

            String databaseSchemaGUID = client.createDataAsset(userId, assetManagerGUID, assetManagerName, true, null, properties);

            client.setupRelatedDataAsset(userId, assetManagerGUID, assetManagerName, true, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, databaseSchemaGUID, databaseGUID, null, null, false, false);

            if (databaseSchemaGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for schemaCreate)");
            }
            else
            {
                checkDatabaseSchemaOK(client, assetManagerGUID, databaseSchemaGUID, databaseGUID, activityName, userId);
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
     * @param client interface to Asset Manager OMAS
     * @param databaseTableGUID unique id of the database table to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseTableGone(DataAssetExchangeClient client,
                                        String                  assetManagerGUID,
                                        String                  databaseTableGUID,
                                        String                  activityName,
                                        String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaAttributeElement retrievedElement = client.getSchemaAttributeByGUID(userId, assetManagerGUID, assetManagerName, databaseTableGUID, null, false,false);

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
    }


    /**
     * Check a database table is stored OK.
     *
     * @param client interface to Asset Manager OMAS
     * @param databaseTableGUID unique id of the databaseSchema
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseTableOK(DataAssetExchangeClient client,
                                      String                  assetManagerGUID,
                                      String                  databaseTableGUID,
                                      String                  activityName,
                                      String                  userId) throws FVTUnexpectedCondition
    {

        try
        {
            SchemaAttributeElement    retrievedElement = client.getSchemaAttributeByGUID(userId, assetManagerGUID, assetManagerName, databaseTableGUID, null, false, false);

            validateDatabaseTable(activityName + "(getSchemaAttributeByGUID)", retrievedElement);

            List<SchemaAttributeElement> databaseTableList = client.getSchemaAttributesByName(userId, assetManagerGUID, assetManagerName, databaseTableName, 0, maxPageSize, null, false, false);

            validateDatabaseTableList(activityName + "(getSchemaAttributesByName)", databaseTableList);
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


    private void validateDatabaseTableList(String                       activityName,
                                           List<SchemaAttributeElement> databaseTableList) throws FVTUnexpectedCondition
    {

        if (databaseTableList == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - no DatabaseTable");
        }
        else if (databaseTableList.isEmpty())
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - Empty DatabaseTable list");
        }
        else if (databaseTableList.size() != 1)
        {
            throw new FVTUnexpectedCondition(testCaseName,
                                             activityName + " - DatabaseTable list contains " + databaseTableList.size() + " elements)");
        }

        validateDatabaseTable(activityName, databaseTableList.get(0));
    }


    private void validateDatabaseTable(String                 activityName,
                                       SchemaAttributeElement retrievedElement) throws FVTUnexpectedCondition
    {
        if (retrievedElement == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - no DatabaseTableElement");
        }

        System.out.println("Database Table: " + retrievedElement);

        SchemaAttributeProperties retrievedTable  = retrievedElement.getSchemaAttributeProperties();

        if (retrievedTable == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - no DatabaseTableProperties");
        }

        if (! databaseTableName.equals(retrievedTable.getQualifiedName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - Bad qualifiedName: " + retrievedTable.getQualifiedName() + " rather than " + databaseTableName);
        }
        if (! databaseTableDisplayName.equals(retrievedTable.getDisplayName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - Bad displayName: " + retrievedTable.getDisplayName() + " rather than " + databaseTableDisplayName);
        }
        if (! databaseTableDescription.equals(retrievedTable.getDescription()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - Bad description: " + retrievedTable.getDescription() + " rather than " + databaseTableDescription);
        }

        validateAnchorGUID(activityName, retrievedElement);
    }


    /**
     * Create a database table and return its GUID.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the database manager
     * @param databaseSchemaGUID unique id of the databaseSchema
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createDatabaseTable(DataAssetExchangeClient client,
                                       String                  assetManagerGUID,
                                       String                  databaseSchemaGUID,
                                       String                  userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createDatabaseTable";

        try
        {
            SchemaTypeProperties schemaTypeProperties = new SchemaTypeProperties();
            schemaTypeProperties.setTypeName("RelationalDBSchemaType");
            schemaTypeProperties.setQualifiedName("SchemaOf:" + databaseSchemaName);

            String databaseSchemaTypeGUID = client.createAnchoredSchemaType(userId, assetManagerGUID, assetManagerName, true, databaseSchemaGUID, null,false, false, schemaTypeProperties);

            client.setupSchemaTypeParent(userId, assetManagerGUID, assetManagerName, true, databaseSchemaTypeGUID, databaseSchemaGUID, "Asset", null, false, false, null);

            SchemaAttributeProperties properties = new SchemaAttributeProperties();

            properties.setQualifiedName(databaseTableName);
            properties.setDisplayName(databaseTableDisplayName);
            properties.setDescription(databaseTableDescription);
            properties.setTypeName("RelationalTable");

            String databaseTableGUID = client.createSchemaAttribute(userId, assetManagerGUID, assetManagerName, true, databaseSchemaTypeGUID, null, properties, null, false, false);

            if (databaseTableGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for tableCreate)");
            }
            else
            {
                checkDatabaseTableOK(client, assetManagerGUID, databaseTableGUID, activityName, userId);
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
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the database manager
     * @param databaseSchemaTypeGUID unique id of the databaseSchemaType
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createDatabaseTableForSchemaType(DataAssetExchangeClient client,
                                                    String                  assetManagerGUID,
                                                    String                  databaseSchemaTypeGUID,
                                                    String                  userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createDatabaseTableForSchemaType";

        try
        {
            SchemaAttributeProperties properties = new SchemaAttributeProperties();

            properties.setQualifiedName(databaseTableName);
            properties.setDisplayName(databaseTableDisplayName);
            properties.setDescription(databaseTableDescription);
            properties.setTypeName("RelationalTable");

            String databaseTableGUID = client.createSchemaAttribute(userId, assetManagerGUID, assetManagerName, true, databaseSchemaTypeGUID, null, properties, null, false, false);

            if (databaseTableGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for tableCreate)");
            }
            else
            {
                checkDatabaseTableOK(client, assetManagerGUID, databaseTableGUID, activityName, userId);
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
     * @param client interface to Asset Manager OMAS
     * @param databaseColumnGUID unique id of the database column to test
     * @param databaseTableGUID unique id of the database table to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseColumnGone(DataAssetExchangeClient client,
                                         String                  assetManagerGUID,
                                         String                  databaseColumnGUID,
                                         String                  databaseTableGUID,
                                         String                  activityName,
                                         String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaAttributeElement retrievedElement = client.getSchemaAttributeByGUID(userId, assetManagerGUID, assetManagerName, databaseColumnGUID, null, false, false);

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
                List<SchemaAttributeElement> databaseColumnList = client.getNestedSchemaAttributes(userId, assetManagerGUID, assetManagerName, databaseTableGUID, 0, maxPageSize, null, false,false);

                if (databaseColumnList != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(DatabaseColumn returned for getColumnsForDatabaseTable)");
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
    }


    /**
     * Check that the database column is ok.
     *
     * @param client interface to Asset Manager OMAS
     * @param databaseColumnGUID unique id of the database column to test
     * @param databaseTableGUID unique id of the database table to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseColumnOK(DataAssetExchangeClient client,
                                       String                  assetManagerGUID,
                                       String                  databaseColumnGUID,
                                       String                  databaseTableGUID,
                                       String                  activityName,
                                       String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaAttributeElement  retrievedElement = client.getSchemaAttributeByGUID(userId, assetManagerGUID, assetManagerName, databaseColumnGUID, null, false, false);

            validateColumn(activityName + "(getSchemaAttributeByGUID)", retrievedElement);

            List<SchemaAttributeElement> databaseColumnList = client.getSchemaAttributesByName(userId, null, null, databaseColumnName, 0, maxPageSize, null, false, false);

            validateColumnList(activityName + "(getSchemaAttributesByName)", databaseColumnList);

            databaseColumnList = client.getNestedSchemaAttributes(userId, null, null, databaseTableGUID, 0, maxPageSize, null, false, false);

            validateColumnList(activityName + "(getColumnsForDatabaseTable)", databaseColumnList);
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


    private void validateColumnList(String                       activityName,
                                    List<SchemaAttributeElement> databaseColumnList) throws FVTUnexpectedCondition
    {
        if (databaseColumnList == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "- no DatabaseColumn List");
        }
        else if (databaseColumnList.isEmpty())
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - Empty DatabaseColumn list");
        }
        else if (databaseColumnList.size() != 1)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(DatabaseColumn list contains" + databaseColumnList.size() + " elements)");
        }

        this.validateColumn(activityName, databaseColumnList.get(0));
    }


    private void validateColumn(String                 activityName,
                                SchemaAttributeElement retrievedElement) throws FVTUnexpectedCondition
    {
        if (retrievedElement == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "- no DatabaseColumnElement");
        }

        System.out.println("Database Column: " + retrievedElement);

        SchemaAttributeProperties retrievedColumn  = retrievedElement.getSchemaAttributeProperties();

        if (retrievedColumn == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "- no DatabaseColumnProperties");
        }

        if (! databaseColumnName.equals(retrievedColumn.getQualifiedName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - Bad qualifiedName: " + retrievedColumn.getQualifiedName() + " rather than " + databaseColumnName);
        }
        if (! databaseColumnDisplayName.equals(retrievedColumn.getDisplayName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - Bad displayName: " + retrievedColumn.getDisplayName() + " rather than " + databaseColumnDisplayName);
        }
        if (! databaseColumnDescription.equals(retrievedColumn.getDescription()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - Bad description: " + retrievedColumn.getDescription() + " rather than " + databaseColumnDescription);
        }

        validateAnchorGUID(activityName, retrievedElement);
    }


    /**
     * Create a database column and return its GUID.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the database manager
     * @param databaseTableGUID unique id of the database table to connect the column to
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createDatabaseColumn(DataAssetExchangeClient client,
                                        String                  assetManagerGUID,
                                        String                  databaseTableGUID,
                                        String                  userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createDatabaseColumn";

        try
        {
            SchemaAttributeProperties  properties = new SchemaAttributeProperties();

            properties.setQualifiedName(databaseColumnName);
            properties.setDisplayName(databaseColumnDisplayName);
            properties.setDescription(databaseColumnDescription);
            properties.setTypeName("RelationalColumn");

            SchemaTypeProperties schemaTypeProperties = new SchemaTypeProperties();

            Map<String, Object> extendedProperties = new HashMap<>();

            extendedProperties.put("dataType", databaseColumnType);

            schemaTypeProperties.setTypeName("PrimitiveSchemaType");
            schemaTypeProperties.setQualifiedName("SchemaType:" + databaseColumnName);
            schemaTypeProperties.setExtendedProperties(extendedProperties);

            properties.setSchemaType(schemaTypeProperties);
            String databaseColumnGUID = client.createSchemaAttribute(userId, assetManagerGUID, assetManagerName, true, databaseTableGUID, null, properties, null, false, false);

            if (databaseColumnGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for columnCreate)");
            }
            else
            {
                checkDatabaseColumnOK(client, assetManagerGUID, databaseColumnGUID, databaseTableGUID, activityName, userId);
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


    private void validateAnchorGUID(String          activityName,
                                    CorrelatedMetadataElement metadataElement)
    {
        if (metadataElement.getElementHeader() != null)
        {
            if (metadataElement.getElementHeader().getClassifications() != null)
            {
                for (ElementClassification classification : metadataElement.getElementHeader().getClassifications())
                {
                    if ("Anchors".equals(classification.getClassificationName()))
                    {
                        System.out.println(metadataElement.getElementHeader().getType().getTypeName() + " element " + metadataElement.getElementHeader().getGUID() + " has anchor of " + classification.getClassificationProperties() + " in activity " + activityName);
                    }
                }
            }
        }
    }
}
