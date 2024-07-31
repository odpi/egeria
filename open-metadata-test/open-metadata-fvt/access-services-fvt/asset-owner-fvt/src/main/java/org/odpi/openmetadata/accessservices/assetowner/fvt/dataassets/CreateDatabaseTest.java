/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.fvt.dataassets;

import org.odpi.openmetadata.accessservices.assetowner.client.AssetOwner;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelationshipElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CreateDatabaseTest calls the AssetOwner client to create a database with schemas tables and columns
 * and then retrieve the results.
 */
public class CreateDatabaseTest
{
    private final static String testCaseName       = "CreateDatabaseTest";

    private final static int    maxPageSize        = 100;
    

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
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceWiki());

        AssetOwner client = thisTest.getAssetOwnerClient(serverName, serverPlatformRootURL, auditLog);

        String databaseGUID = thisTest.getDatabase(client, userId);
        String databaseSchemaGUID = thisTest.getDatabaseSchema(client, databaseGUID, userId);
        String databaseTableGUID = thisTest.createDatabaseTable(client, databaseSchemaGUID, userId);
        String databaseColumnGUID = thisTest.createDatabaseColumn(client, databaseSchemaGUID, databaseTableGUID, userId);

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
            client.deleteAsset(userId, databaseGUID);

            thisTest.checkDatabaseGone(client, databaseGUID, activityName, userId);
            thisTest.checkDatabaseColumnOK(client, databaseColumnGUID, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseTableOK(client, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseSchemaOK(client, databaseSchemaGUID, null, activityName, userId);

            activityName = "cascadedDelete - remove DatabaseSchema";

            client.deleteAsset(userId, databaseSchemaGUID);

            thisTest.checkDatabaseSchemaGone(client, databaseSchemaGUID, null, activityName, userId);
            thisTest.checkDatabaseTableGone(client, databaseTableGUID,  activityName, userId);
            thisTest.checkDatabaseColumnGone(client, databaseColumnGUID, null, activityName, userId);
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
        databaseGUID = thisTest.getDatabase(client, userId);
        databaseSchemaGUID = thisTest.getDatabaseSchema(client, databaseGUID, userId);
        databaseTableGUID = thisTest.createDatabaseTable(client, databaseSchemaGUID, userId);
        databaseColumnGUID = thisTest.createDatabaseColumn(client, databaseSchemaGUID, databaseTableGUID, userId);

        System.out.println("databaseGUID = " + databaseGUID);
        System.out.println("databaseSchemaGUID = " + databaseSchemaGUID);
        System.out.println("databaseTableGUID = " + databaseTableGUID);
        System.out.println("databaseColumnGUID = " + databaseColumnGUID);


        /*
         * Check that elements can be deleted one by one
         */

        try
        {
            activityName = "deleteOneByOne - pre-validate";
            thisTest.checkDatabaseColumnOK(client, databaseColumnGUID, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseTableOK(client, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseSchemaOK(client, databaseSchemaGUID, databaseGUID, activityName, userId);
            thisTest.checkDatabaseOK(client, databaseGUID, activityName, userId);

            client.removeSchemaAttribute(userId, databaseColumnGUID);

            activityName = "deleteOneByOne - column gone";
            thisTest.checkDatabaseColumnGone(client, databaseColumnGUID, databaseTableGUID, activityName, userId);

            activityName = "deleteOneByOne - column gone - check table: " + databaseTableGUID;

            thisTest.checkDatabaseTableOK(client, databaseTableGUID, activityName, userId);

            activityName = "deleteOneByOne - column gone - check schema";

            thisTest.checkDatabaseSchemaOK(client, databaseSchemaGUID, databaseGUID, activityName, userId);

            activityName = "deleteOneByOne - column gone - check DB";

            thisTest.checkDatabaseOK(client, databaseGUID, activityName, userId);

            activityName = "deleteOneByOne - remove table";

            client.removeSchemaAttribute(userId, databaseTableGUID);

            activityName = "deleteOneByOne - table gone";
            thisTest.checkDatabaseTableGone(client, databaseTableGUID, activityName, userId);
            thisTest.checkDatabaseSchemaOK(client, databaseSchemaGUID, databaseGUID, activityName, userId);
            thisTest.checkDatabaseOK(client, databaseGUID, activityName, userId);

            client.deleteAsset(userId, databaseSchemaGUID);

            activityName = "deleteOneByOne - schema gone";
            thisTest.checkDatabaseSchemaGone(client, databaseSchemaGUID, databaseGUID, activityName, userId);
            thisTest.checkDatabaseOK(client, databaseGUID, activityName, userId);

            client.deleteAsset(userId, databaseGUID);

            activityName = "deleteOneByOne - database gone";
            thisTest.checkDatabaseGone(client, databaseGUID, activityName, userId);

            /*
             * Recreate database
             */
            activityName= "deleteOneByOne";

            databaseGUID = thisTest.getDatabase(client, userId);
            databaseSchemaGUID = thisTest.getDatabaseSchema(client, databaseGUID, userId);
            databaseTableGUID = thisTest.createDatabaseTable(client, databaseSchemaGUID, userId);
            databaseColumnGUID = thisTest.createDatabaseColumn(client, databaseSchemaGUID, databaseTableGUID, userId);

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

            SchemaTypeProperties attributeType = new SchemaTypeProperties();
            attributeType.setTypeName("PrimitiveSchemaType");
            databaseColumnTwoProperties.setSchemaType(attributeType);


            try
            {

                client.updateSchemaAttribute(userId,  databaseColumnTwoGUID, true, databaseColumnTwoProperties);
                throw new FVTUnexpectedCondition(testCaseName, activityName);
            }
            catch (InvalidParameterException expectedError)
            {
                // very good
            }

            activityName = "updateColumnWithSameProperties";

            databaseColumnTwoGUID = client.addSchemaAttribute(userId, databaseSchemaGUID, databaseTableGUID, databaseColumnTwoProperties);

            SchemaAttributeElement beforeElement = client.getSchemaAttributeByGUID(userId, databaseColumnTwoGUID);

            client.updateSchemaAttribute(userId, databaseColumnTwoGUID, true, databaseColumnTwoProperties);

            SchemaAttributeElement afterElement = client.getSchemaAttributeByGUID(userId, databaseColumnTwoGUID);

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
            client.updateSchemaAttribute(userId, databaseColumnTwoGUID, true, databaseColumnTwoProperties);

            afterElement = client.getSchemaAttributeByGUID(userId, databaseColumnTwoGUID);

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

            client.updateSchemaAttribute(userId, databaseColumnTwoGUID, true,  databaseColumnTwoProperties);

            afterElement = client.getSchemaAttributeByGUID(userId, databaseColumnTwoGUID);

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
                client.deleteAsset(userId, databaseGUID);

                thisTest.checkDatabaseGone(client, databaseGUID, activityName, userId);

                client.deleteAsset(userId, databaseSchemaGUID);

                thisTest.checkDatabaseSchemaGone(client, databaseSchemaGUID, null, activityName, userId);
                thisTest.checkDatabaseTableGone(client, databaseTableGUID, activityName, userId);
                thisTest.checkDatabaseColumnGone(client, databaseColumnGUID, null, activityName, userId);
                thisTest.checkDatabaseColumnGone(client, databaseColumnTwoGUID, null, activityName, userId);
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
    private AssetOwner getAssetOwnerClient(String   serverName,
                                           String   serverPlatformRootURL,
                                           AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getAssetOwnerClient";

        try
        {
            AssetOwnerRESTClient restClient = new AssetOwnerRESTClient(serverName, serverPlatformRootURL);

            return new AssetOwner(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Check a database is gone.
     *
     * @param client interface to Asset Owner OMAS
     * @param databaseGUID unique id of the database to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseGone(AssetOwner client,
                                   String     databaseGUID,
                                   String     activityName,
                                   String     userId) throws FVTUnexpectedCondition
    {
        try
        {
            AssetElement retrievedElement = client.getAssetSummary(userId, databaseGUID);

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
     * @param client interface to Asset Owner OMAS
     * @param databaseGUID unique id of the database
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseOK(AssetOwner client,
                                 String     databaseGUID,
                                 String     activityName,
                                 String     userId) throws FVTUnexpectedCondition
    {
        try
        {
            AssetElement retrievedElement = client.getAssetSummary(userId, databaseGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseElement from Retrieve)");
            }

            AssetProperties retrievedDatabase = retrievedElement.getProperties();

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
            if (! databaseDescription.equals(retrievedDatabase.getResourceDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            if (! databaseType.equals(retrievedDatabase.getDeployedImplementationType()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad deployedImplementationType from Retrieve)");
            }

            Map<String, Object> databaseExtendedProperties = retrievedDatabase.getExtendedProperties();

            if (! databaseVersion.equals(databaseExtendedProperties.get(OpenMetadataProperty.DATABASE_VERSION.name)))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseVersion from Retrieve).  Retrieve Element: " + retrievedDatabase);
            }

            List<AssetElement> databaseList = client.getAssetsByName(userId, databaseName, 0, maxPageSize);

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
            retrievedDatabase = retrievedElement.getProperties();

            if (! databaseName.equals(retrievedDatabase.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! databaseDisplayName.equals(retrievedDatabase.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! databaseDescription.equals(retrievedDatabase.getResourceDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }
            if (! databaseType.equals(retrievedDatabase.getDeployedImplementationType()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad deployedImplementationType from RetrieveByName)");
            }
            databaseExtendedProperties = retrievedDatabase.getExtendedProperties();

            if (! databaseVersion.equals(databaseExtendedProperties.get(OpenMetadataProperty.DATABASE_VERSION.name)))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad databaseVersion from RetrieveByName).  Retrieve Element: " + retrievedDatabase);
            }

            databaseList = client.getAssetsByName(userId, databaseName, 1, maxPageSize);

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
     * @param client interface to Asset Owner OMAS
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDatabase(AssetOwner client,
                               String     userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabase";

        try
        {
            AssetProperties properties = new AssetProperties();

            properties.setQualifiedName(databaseName);
            properties.setDisplayName(databaseDisplayName);
            properties.setResourceDescription(databaseDescription);

            properties.setTypeName("Database");

            Map<String, Object> extendedProperties = new HashMap<>();
            extendedProperties.put("deployedImplementationType", databaseType);
            extendedProperties.put("databaseVersion" , databaseVersion);
            properties.setExtendedProperties(extendedProperties);

            String databaseGUID = client.addAssetToCatalog(userId, properties);

            if (databaseGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }
            else
            {
                checkDatabaseOK(client, databaseGUID, activityName, userId);
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
     * @param client interface to Asset Owner OMAS
     * @param databaseSchemaGUID unique id of the database schema to test
     * @param databaseGUID unique id of the database to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseSchemaGone(AssetOwner client,
                                         String     databaseSchemaGUID,
                                         String     databaseGUID,
                                         String     activityName,
                                         String     userId) throws FVTUnexpectedCondition
    {
        try
        {
            AssetElement retrievedElement = client.getAssetSummary(userId, databaseSchemaGUID);

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
                List<RelationshipElement> relationshipList = client.getRelatedAssetsAtEnd2(userId, "DataContentForDataSet", databaseGUID, 0, maxPageSize);

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
     * @param client interface to Asset Owner OMAS
     * @param databaseSchemaGUID unique id of the database schema
     * @param databaseGUID unique id of the database
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void   checkDatabaseSchemaOK(AssetOwner client,
                                         String     databaseSchemaGUID,
                                         String     databaseGUID,
                                         String     activityName,
                                         String     userId) throws FVTUnexpectedCondition
    {
        try
        {
            AssetElement retrievedElement = client.getAssetSummary(userId, databaseSchemaGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no DatabaseSchemaElement from Retrieve)");
            }

            validateAnchorGUID(activityName, retrievedElement);

            AssetProperties retrievedSchema = retrievedElement.getProperties();

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
            if (! databaseSchemaDescription.equals(retrievedSchema.getResourceDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }


            List<AssetElement> databaseSchemaList = client.getAssetsByName(userId, databaseSchemaName, 0, maxPageSize);

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
            retrievedSchema = retrievedElement.getProperties();

            if (! databaseSchemaName.equals(retrievedSchema.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! databaseSchemaDisplayName.equals(retrievedSchema.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! databaseSchemaDescription.equals(retrievedSchema.getResourceDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            if (databaseGUID != null)
            {
                List<RelationshipElement> relationshipElements = client.getRelatedAssetsAtEnd2(userId, "DataContentForDataSet", databaseGUID, 0, maxPageSize);

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
     * @param client interface to Asset Owner OMAS
     * @param databaseGUID unique id of the database
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDatabaseSchema(AssetOwner client,
                                     String     databaseGUID,
                                     String     userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabaseSchema";

        try
        {
            AssetProperties properties = new AssetProperties();

            properties.setQualifiedName(databaseSchemaName);
            properties.setDisplayName(databaseSchemaDisplayName);
            properties.setResourceDescription(databaseSchemaDescription);
            properties.setTypeName("DeployedDatabaseSchema");

            String databaseSchemaGUID = client.addAssetToCatalog(userId, properties);

            client.setupRelatedAsset(userId, "DataContentForDataSet", databaseGUID, databaseSchemaGUID, null);

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
     * @param client interface to Asset Owner OMAS
     * @param databaseTableGUID unique id of the database table to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseTableGone(AssetOwner client,
                                        String     databaseTableGUID,
                                        String     activityName,
                                        String     userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaAttributeElement retrievedElement = client.getSchemaAttributeByGUID(userId, databaseTableGUID);

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
     * @param client interface to Asset Owner OMAS
     * @param databaseTableGUID unique id of the databaseSchema
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseTableOK(AssetOwner client,
                                      String     databaseTableGUID,
                                      String     activityName,
                                      String     userId) throws FVTUnexpectedCondition
    {

        try
        {
            SchemaAttributeElement    retrievedElement = client.getSchemaAttributeByGUID(userId, databaseTableGUID);

            validateDatabaseTable(activityName + "(getSchemaAttributeByGUID)", retrievedElement);

            List<SchemaAttributeElement> databaseTableList = client.getSchemaAttributesByName(userId, databaseTableName, 0, maxPageSize);

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
     * @param client interface to Asset Owner OMAS
     * @param databaseSchemaGUID unique id of the databaseSchema
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createDatabaseTable(AssetOwner client,
                                       String     databaseSchemaGUID,
                                       String     userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createDatabaseTable";

        try
        {
            SchemaTypeProperties schemaTypeProperties = new SchemaTypeProperties();
            schemaTypeProperties.setTypeName("RelationalDBSchemaType");
            schemaTypeProperties.setQualifiedName("SchemaOf:" + databaseSchemaName);

            String databaseSchemaTypeGUID = client.createAnchoredSchemaType(userId, databaseSchemaGUID, schemaTypeProperties);

            client.setupSchemaTypeParent(userId, databaseSchemaTypeGUID, databaseSchemaGUID, "Asset", null);

            SchemaAttributeProperties properties = new SchemaAttributeProperties();

            properties.setQualifiedName(databaseTableName);
            properties.setDisplayName(databaseTableDisplayName);
            properties.setDescription(databaseTableDescription);
            properties.setTypeName("RelationalTable");

            SchemaTypeProperties attributeType = new SchemaTypeProperties();
            attributeType.setTypeName("RelationalTableType");
            properties.setSchemaType(attributeType);

            String databaseTableGUID = client.addSchemaAttribute(userId, databaseSchemaGUID, databaseSchemaTypeGUID, properties);

            if (databaseTableGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for tableCreate)");
            }
            else
            {
                checkDatabaseTableOK(client, databaseTableGUID, activityName, userId);
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
     * @param client interface to Asset Owner OMAS
     * @param databaseSchemaGUID unique id of the databaseSchema
     * @param databaseSchemaTypeGUID unique id of the databaseSchemaType
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createDatabaseTableForSchemaType(AssetOwner client,
                                                    String     databaseSchemaGUID,
                                                    String     databaseSchemaTypeGUID,
                                                    String     userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createDatabaseTableForSchemaType";

        try
        {
            SchemaAttributeProperties properties = new SchemaAttributeProperties();

            properties.setQualifiedName(databaseTableName);
            properties.setDisplayName(databaseTableDisplayName);
            properties.setDescription(databaseTableDescription);
            properties.setTypeName("RelationalTable");

            SchemaTypeProperties attributeType = new SchemaTypeProperties();
            attributeType.setTypeName("RelationalTableType");
            properties.setSchemaType(attributeType);

            String databaseTableGUID = client.addSchemaAttribute(userId, databaseSchemaGUID, databaseSchemaTypeGUID, properties);

            if (databaseTableGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for tableCreate)");
            }
            else
            {
                checkDatabaseTableOK(client, databaseTableGUID, activityName, userId);
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
     * @param client interface to Asset Owner OMAS
     * @param databaseColumnGUID unique id of the database column to test
     * @param databaseTableGUID unique id of the database table to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseColumnGone(AssetOwner client,
                                         String     databaseColumnGUID,
                                         String     databaseTableGUID,
                                         String     activityName,
                                         String     userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaAttributeElement retrievedElement = client.getSchemaAttributeByGUID(userId, databaseColumnGUID);

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
                List<SchemaAttributeElement> databaseColumnList = client.getNestedSchemaAttributes(userId, databaseTableGUID, 0, maxPageSize);

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
     * @param client interface to Asset Owner OMAS
     * @param databaseColumnGUID unique id of the database column to test
     * @param databaseTableGUID unique id of the database table to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkDatabaseColumnOK(AssetOwner client,
                                       String     databaseColumnGUID,
                                       String     databaseTableGUID,
                                       String     activityName,
                                       String     userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaAttributeElement  retrievedElement = client.getSchemaAttributeByGUID(userId, databaseColumnGUID);

            validateColumn(activityName + "(getSchemaAttributeByGUID)", retrievedElement);

            List<SchemaAttributeElement> databaseColumnList = client.getSchemaAttributesByName(userId, databaseColumnName, 0, maxPageSize);

            validateColumnList(activityName + "(getSchemaAttributesByName)", databaseColumnList);

            databaseColumnList = client.getNestedSchemaAttributes(userId, databaseTableGUID, 0, maxPageSize);

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
     * @param client interface to Asset Owner OMAS
     * @param databaseSchemaGUID unique id of the database schema that is the anchor
     * @param databaseTableGUID unique id of the database table to connect the column to
     * @param userId calling user
     * @return GUID of database
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createDatabaseColumn(AssetOwner client,
                                        String     databaseSchemaGUID,
                                        String     databaseTableGUID,
                                        String     userId) throws FVTUnexpectedCondition
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

            String databaseColumnGUID = client.addSchemaAttribute(userId, databaseSchemaGUID, databaseTableGUID, properties);

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


    private void validateAnchorGUID(String          activityName,
                                    MetadataElement metadataElement)
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
