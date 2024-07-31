/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.fvt.dataassets;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.DataAssetExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.ExternalAssetManagerClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.CorrelatedMetadataElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.DataAssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.AssetManagerProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CreateSchemasTest builds complex schema structures.
 */
public class CreateSchemasTest
{
    private final static String testCaseName       = "CreateSchemaTest";

    private final static int    maxPageSize        = 100;

    /*
     * The asset manager name is constant - the guid is created as part of the test.
     */
    private final static String assetManagerName            = "TestSchemaManager";
    private final static String assetManagerDisplayName     = "SchemaManager displayName";
    private final static String assetManagerDescription     = "SchemaManager description";
    private final static String assetManagerTypeDescription = "SchemaManager type";
    private final static String assetManagerVersion         = "SchemaManager version";

    private final static String assetName         = "TestAsset";
    private final static String assetShortName    = "Asset name";
    private final static String assetResourceName = "Asset resourceName";

    private final static String assetDescription = "Asset description";

    private final static String optionSchemaName        = "TestSchemaTypeOption";
    private final static String optionSchemaDisplayName = "TestSchemaTypeOption displayName";
    private final static String optionSchemaDescription = "TestSchemaTypeOption description";

    private final static String primitive1Name        = "TestPrimitive1";
    private final static String primitive1DisplayName = "TestPrimitive1 displayName";
    private final static String primitive1Description = "TestPrimitive1 description";
    private final static String primitive1DataType    = "int";


    private final static String primitive2Name        = "TestPrimitive2";
    private final static String primitive2DisplayName = "TestPrimitive2 displayName";
    private final static String primitive2Description = "TestPrimitive2 description";
    private final static String primitive2DataType    = "string";


    private final static String mapName        = "TestMap";
    private final static String mapDisplayName = "TestMap displayName";
    private final static String mapDescription = "TestMap description";

    private final static String primitiveFromName        = "TestPrimitiveFrom";
    private final static String primitiveFromDisplayName = "TestPrimitiveFrom displayName";
    private final static String primitiveFromDescription = "TestPrimitiveFrom description";
    private final static String primitiveFromType        = "date";

    private final static String primitiveToName        = "TestPrimitiveTo";
    private final static String primitiveToDisplayName = "TestPrimitiveTo displayName";
    private final static String primitiveToDescription = "TestPrimitiveTo description";
    private final static String primitiveToType        = "long";


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
            CreateSchemasTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        CreateSchemasTest thisTest = new CreateSchemasTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceWiki());

        DataAssetExchangeClient client = thisTest.getDataAssetExchangeClient(serverName, serverPlatformRootURL, auditLog);

        String assetManagerGUID = thisTest.getAssetManager(serverName, serverPlatformRootURL, userId, auditLog);
        String assetGUID = thisTest.getAsset(client, assetManagerGUID, userId);
        String primitive1GUID = thisTest.createPrimitive(client, assetManagerGUID, assetGUID, userId, primitive1Name, primitive1DisplayName, primitive1Description, primitive1DataType);
        String primitive2GUID = thisTest.createPrimitive(client, assetManagerGUID, assetGUID, userId, primitive2Name, primitive2DisplayName, primitive2Description, primitive2DataType);
        String primitiveFromGUID = thisTest.createPrimitive(client, assetManagerGUID, assetGUID, userId, primitiveFromName, primitiveFromDisplayName, primitiveFromDescription, primitiveFromType);
        String primitiveToGUID = thisTest.createPrimitive(client, assetManagerGUID, assetGUID, userId, primitiveToName, primitiveToDisplayName, primitiveToDescription, primitiveToType);
        String mapGUID = thisTest.createMap(client, assetManagerGUID, assetGUID, userId, primitiveFromGUID, primitiveToGUID);

        List<String> optionGUIDs = new ArrayList<>();

        optionGUIDs.add(primitive1GUID);
        optionGUIDs.add(primitive2GUID);
        optionGUIDs.add(mapGUID);

        String schemaOptionGUID = thisTest.getSchemaOption(client, assetManagerGUID, assetGUID, userId, optionGUIDs);

        String activityName = "cascadedDelete";

        System.out.println("activityName: " + activityName);
        System.out.println("assetGUID: " + assetGUID);
        System.out.println("schemaOptionGUID: " + schemaOptionGUID);
        System.out.println("mapGUID: " + mapGUID);
        System.out.println("primitive1GUID: " + primitive1GUID);
        System.out.println("primitive2GUID: " + primitive2GUID);
        System.out.println("primitiveFromGUID: " + primitiveFromGUID);
        System.out.println("primitiveToGUID: " + primitiveToGUID);

        /*
         * Check that all elements are deleted when the asset is deleted.
         */
        try
        {
            client.removeDataAsset(userId, assetManagerGUID, assetManagerName, assetGUID, null, null, false, false);

            thisTest.checkAssetGone(client, assetManagerGUID, assetGUID, activityName, userId);
            thisTest.checkSchemaOptionGone(client, assetManagerGUID, schemaOptionGUID, null, activityName, userId);
            thisTest.checkMapGone(client, assetManagerGUID, mapGUID, activityName, userId);
            thisTest.checkPrimitiveGone(client, assetManagerGUID, primitive1GUID, activityName, userId);
            thisTest.checkPrimitiveGone(client, assetManagerGUID, primitive2GUID, activityName, userId);
            thisTest.checkPrimitiveGone(client, assetManagerGUID, primitiveFromGUID, activityName, userId);
            thisTest.checkPrimitiveGone(client, assetManagerGUID, primitiveToGUID, activityName, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }


        /*
         * Recreate asset with schema
         */
        activityName= "deleteOneByOne";

        assetGUID = thisTest.getAsset(client, assetManagerGUID, userId);
        primitive1GUID = thisTest.createPrimitive(client, assetManagerGUID, assetGUID, userId, primitive1Name, primitive1DisplayName, primitive1Description, primitive1DataType);
        primitive2GUID = thisTest.createPrimitive(client, assetManagerGUID, assetGUID, userId, primitive2Name, primitive2DisplayName, primitive2Description, primitive2DataType);
        primitiveFromGUID = thisTest.createPrimitive(client, assetManagerGUID, assetGUID, userId, primitiveFromName, primitiveFromDisplayName, primitiveFromDescription, primitiveFromType);
        primitiveToGUID = thisTest.createPrimitive(client, assetManagerGUID, assetGUID, userId, primitiveToName, primitiveToDisplayName, primitiveToDescription, primitiveToType);
        mapGUID = thisTest.createMap(client, assetManagerGUID, assetGUID, userId, primitiveFromGUID, primitiveToGUID);

        optionGUIDs = new ArrayList<>();

        optionGUIDs.add(primitive1GUID);
        optionGUIDs.add(primitive2GUID);
        optionGUIDs.add(mapGUID);

        schemaOptionGUID = thisTest.getSchemaOption(client, assetManagerGUID, assetGUID, userId, optionGUIDs);

        System.out.println("activityName: " + activityName);
        System.out.println("assetGUID: " + assetGUID);
        System.out.println("schemaOptionGUID: " + schemaOptionGUID);
        System.out.println("mapGUID: " + mapGUID);
        System.out.println("primitive1GUID: " + primitive1GUID);
        System.out.println("primitive2GUID: " + primitive2GUID);
        System.out.println("primitiveFromGUID: " + primitiveFromGUID);
        System.out.println("primitiveToGUID: " + primitiveToGUID);

        /*
         * Check that elements can be deleted one by one
         */

        try
        {
            client.removeSchemaType(userId, assetManagerGUID, assetManagerName, primitive1GUID, null, null, false, false);

            activityName = "deleteOneByOne - primitive 1 gone";
            thisTest.checkPrimitiveGone(client, assetManagerGUID, primitive1GUID, activityName, userId);
            thisTest.checkPrimitiveOK(client, assetManagerGUID, primitive2GUID, activityName, userId, primitive2Name, primitive2DisplayName, primitive2Description, primitive2DataType);
            thisTest.checkPrimitiveOK(client, assetManagerGUID, primitiveFromGUID, activityName, userId, primitiveFromName, primitiveFromDisplayName, primitiveFromDescription, primitiveFromType);
            thisTest.checkPrimitiveOK(client, assetManagerGUID, primitiveToGUID, activityName, userId, primitiveToName, primitiveToDisplayName, primitiveToDescription, primitiveToType);
            thisTest.checkMapOK(client, assetManagerGUID, mapGUID, activityName, userId, primitiveFromGUID, primitiveToGUID);
            optionGUIDs = new ArrayList<>();
            optionGUIDs.add(primitive2GUID);
            optionGUIDs.add(mapGUID);
            thisTest.checkSchemaOptionOK(client, assetManagerGUID, schemaOptionGUID, activityName, userId, assetGUID, optionGUIDs);
            thisTest.checkAssetOK(client, assetManagerGUID, assetGUID, activityName, userId);

            client.removeSchemaType(userId, assetManagerGUID, assetManagerName, primitive2GUID, null, null, false, false);

            activityName = "deleteOneByOne - primitive 2 gone";
            thisTest.checkPrimitiveGone(client, assetManagerGUID, primitive2GUID, activityName, userId);
            thisTest.checkMapOK(client, assetManagerGUID, mapGUID, activityName, userId, primitiveFromGUID, primitiveToGUID);
            optionGUIDs = new ArrayList<>();
            optionGUIDs.add(mapGUID);
            thisTest.checkSchemaOptionOK(client, assetManagerGUID, schemaOptionGUID, activityName, userId, assetGUID, optionGUIDs);
            thisTest.checkAssetOK(client, assetManagerGUID, assetGUID, activityName, userId);

            client.removeSchemaType(userId, assetManagerGUID, assetManagerName, primitiveFromGUID, null, null, false, false);

            activityName = "deleteOneByOne - primitive From gone - " + primitiveFromGUID;
            thisTest.checkPrimitiveGone(client, assetManagerGUID, primitiveFromGUID, activityName, userId);
            thisTest.checkMapOK(client, assetManagerGUID, mapGUID, activityName, userId, null, primitiveToGUID);
            thisTest.checkSchemaOptionOK(client, assetManagerGUID, schemaOptionGUID, activityName, userId, assetGUID, optionGUIDs);
            thisTest.checkAssetOK(client, assetManagerGUID, assetGUID, activityName, userId);

            client.removeSchemaType(userId, assetManagerGUID, assetManagerName, primitiveToGUID, null, null, false, false);

            activityName = "deleteOneByOne - primitive To gone" + primitiveToGUID;
            thisTest.checkPrimitiveGone(client, assetManagerGUID, primitiveToGUID, activityName, userId);
            thisTest.checkMapOK(client, assetManagerGUID, mapGUID, activityName, userId, null, null);
            thisTest.checkSchemaOptionOK(client, assetManagerGUID, schemaOptionGUID, activityName, userId, assetGUID, optionGUIDs);
            thisTest.checkAssetOK(client, assetManagerGUID, assetGUID, activityName, userId);

            client.removeSchemaType(userId, assetManagerGUID, assetManagerName, mapGUID, null, null, false, false);

            activityName = "deleteOneByOne - map gone";
            thisTest.checkMapGone(client, assetManagerGUID, mapGUID, activityName, userId);
            thisTest.checkSchemaOptionOK(client, assetManagerGUID, schemaOptionGUID, activityName, userId, assetGUID, null);
            thisTest.checkAssetOK(client, assetManagerGUID, assetGUID, activityName, userId);

            client.removeSchemaType(userId, assetManagerGUID, assetManagerName, schemaOptionGUID, null, null, false, false);

            activityName = "deleteOneByOne - schema option gone";
            thisTest.checkSchemaOptionGone(client, assetManagerGUID, schemaOptionGUID, assetGUID, activityName, userId);
            thisTest.checkAssetOK(client, assetManagerGUID, assetGUID, activityName, userId);

            client.removeDataAsset(userId, assetManagerGUID, assetManagerName, assetGUID, null, null, false, false);

            activityName = "deleteOneByOne - asset gone";
            thisTest.checkAssetGone(client, assetManagerGUID, assetGUID, activityName, userId);
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
        final String activityName = "getSchemaManagerClient";

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
     * Create a schema manager entity and return its GUID.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @return unique identifier of the schema manager entity
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
     * Check the asset is gone.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetGUID unique id of the asset to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkAssetGone(DataAssetExchangeClient client,
                                String                  assetManagerGUID,
                                String                  assetGUID,
                                String                  activityName,
                                String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            DataAssetElement retrievedElement = client.getDataAssetByGUID(userId, assetManagerGUID, assetManagerName, assetGUID, null, false, false);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Asset returned from Retrieve)");
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
     * Check asset is ok.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetGUID unique id of the asset
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkAssetOK(DataAssetExchangeClient client,
                              String                  assetManagerGUID,
                              String                  assetGUID,
                              String                  activityName,
                              String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            DataAssetElement retrievedElement = client.getDataAssetByGUID(userId, assetManagerGUID, assetManagerName, assetGUID, null, false, false);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no AssetElement from Retrieve)");
            }

            System.out.println("Asset Element: " + retrievedElement);
            validateAnchorGUID(activityName, retrievedElement);

            DataAssetProperties retrievedAsset = retrievedElement.getDataAssetProperties();

            if (retrievedAsset == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no assetProperties from Retrieve)");
            }

            if (! assetName.equals(retrievedAsset.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! assetShortName.equals(retrievedAsset.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad name from Retrieve)");
            }
            if (! assetResourceName.equals(retrievedAsset.getResourceName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad resource from Retrieve)");
            }
            if (! assetDescription.equals(retrievedAsset.getResourceDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            
            List<DataAssetElement> assetList = client.getDataAssetsByName(userId, assetManagerGUID, assetManagerName, assetName, 0, maxPageSize, null, false, false);

            if (assetList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Asset for RetrieveByName)");
            }
            else if (assetList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty Asset list for RetrieveByName)");
            }
            else if (assetList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Asset list for RetrieveByName contains" + assetList.size() +
                                                         " elements)");
            }

            retrievedElement = assetList.get(0);
            retrievedAsset = retrievedElement.getDataAssetProperties();

            if (! assetName.equals(retrievedAsset.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! assetShortName.equals(retrievedAsset.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad name from RetrieveByName)");
            }
            if (! assetResourceName.equals(retrievedAsset.getResourceName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad resourceName from RetrieveByName)");
            }
            if (! assetDescription.equals(retrievedAsset.getResourceDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }
            
            assetList = client.getDataAssetsByName(userId, assetManagerGUID, assetManagerName, assetName, 1, maxPageSize, null, false, false);

            if (assetList != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Asset for RetrieveByName)");
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
     * Create an asset and return its GUID.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the schema manager
     * @param userId calling user
     * @return GUID of asset
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getAsset(DataAssetExchangeClient client,
                            String                  assetManagerGUID,
                            String                  userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getAsset";

        try
        {
            DataAssetProperties properties = new DataAssetProperties();

            properties.setQualifiedName(assetName);
            properties.setName(assetShortName);
            properties.setResourceName(assetResourceName);
            properties.setResourceDescription(assetDescription);
            
            properties.setTypeName("AvroFile");

            String assetGUID = client.createDataAsset(userId, assetManagerGUID, assetManagerName, true, null, properties);

            if (assetGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }
            else
            {
                checkAssetOK(client, assetManagerGUID, assetGUID, activityName, userId);
            }

            return assetGUID;
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
     * Check a schema option is gone.
     *
     * @param client interface to Asset Manager OMAS
     * @param schemaOptionGUID unique id of the schema to test
     * @param assetGUID unique id of the asset to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkSchemaOptionGone(DataAssetExchangeClient client,
                                       String                  assetManagerGUID,
                                       String                  schemaOptionGUID,
                                       String                  assetGUID,
                                       String                  activityName,
                                       String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaTypeElement retrievedElement = client.getSchemaTypeByGUID(userId, assetManagerGUID, assetManagerName, schemaOptionGUID, null, false, false);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(SchemaOption returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(getSchemaOptionByGUID returned");
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

        if (assetGUID != null)
        {
            try
            {
                /*
                 * Only one schema created so nothing should be tied to the asset.
                 */
                SchemaTypeElement assetSchemaType = client.getSchemaTypeForElement(userId, assetManagerGUID, assetManagerName, assetGUID,"Asset",  null, false, false);

                if (assetSchemaType != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(No SchemaOption returned for getSchemaTypeForElement)");
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
     * Check a schema choice is correctly stored.
     *
     * @param client interface to Asset Manager OMAS
     * @param schemaOptionGUID unique id of the schema
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void   checkSchemaOptionOK(DataAssetExchangeClient client,
                                       String                  assetManagerGUID,
                                       String                  schemaOptionGUID,
                                       String                  activityName,
                                       String                  userId,
                                       String                  assetGUID,
                                       List<String>            optionGUIDs) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaTypeElement    retrievedElement  = client.getSchemaTypeByGUID(userId, assetManagerGUID, assetManagerName, schemaOptionGUID, null, false, false);

            this.validateSchemaOption(activityName + "(getSchemaTypeByGUID)", retrievedElement);

            List<SchemaTypeElement> schemaOptionList = client.getSchemaTypeByName(userId, assetManagerGUID, assetManagerName, optionSchemaName, 0, maxPageSize, null, false, false);

            if (schemaOptionList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no SchemaOption for RetrieveByName)");
            }
            else if (schemaOptionList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty SchemaOption list for RetrieveByName)");
            }
            else if (schemaOptionList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(SchemaOption list for RetrieveByName contains" + schemaOptionList.size() +
                                                         " elements)");
            }

            validateSchemaOption(activityName, schemaOptionList.get(0));

            if (assetGUID != null)
            {
                retrievedElement = client.getSchemaTypeForElement(userId,
                                                                  assetManagerGUID,
                                                                  assetManagerName,
                                                                  assetGUID,
                                                                  "Asset",
                                                                  null,
                                                                  false,
                                                                  false);

                this.validateSchemaOption(activityName + "(getSchemaTypeForElement)", retrievedElement);
            }

            if (optionGUIDs != null)
            {
                List<SchemaTypeElement> optionElements = retrievedElement.getSchemaOptions();

                if (optionElements == null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(no attached SchemaOption Elements)");
                }
                else if (optionElements.isEmpty())
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty attached SchemaOption Elements)");
                }
                else if (optionElements.size() != optionGUIDs.size())
                {
                    throw new FVTUnexpectedCondition(testCaseName,
                                                     activityName + "(Attached SchemaOption Elements contains" + optionElements.size() +
                                                             " elements)");
                }

                for (SchemaTypeElement optionElement : optionElements)
                {
                    if (! optionGUIDs.contains(optionElement.getElementHeader().getGUID()))
                    {
                        throw new FVTUnexpectedCondition(testCaseName,
                                                         activityName + "(Bad SchemaOption Element: " + optionElement + " not in " + optionGUIDs + ")");
                    }
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


    private void validateSchemaOption(String            activityName,
                                      SchemaTypeElement retrievedElement) throws FVTUnexpectedCondition
    {
        if (retrievedElement == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - no SchemaOptionElement)");
        }

        System.out.println("Schema Option Element: " + retrievedElement);
        validateAnchorGUID(activityName, retrievedElement);

        SchemaTypeProperties retrievedSchema = retrievedElement.getSchemaTypeProperties();

        if (retrievedSchema == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "- no SchemaOptionProperties");
        }

        if (! optionSchemaName.equals(retrievedSchema.getQualifiedName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - Bad qualifiedName");
        }
        if (! optionSchemaDisplayName.equals(retrievedSchema.getDisplayName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - Bad displayName");
        }
        if (! optionSchemaDescription.equals(retrievedSchema.getDescription()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " - Bad description");
        }
    }


    /**
     * Create a schema type choice and return its GUID.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the schema manager
     * @param assetGUID unique id of the asset
     * @param userId calling user
     * @return GUID of schema option
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getSchemaOption(DataAssetExchangeClient client,
                                   String                  assetManagerGUID,
                                   String                  assetGUID,
                                   String                  userId,
                                   List<String>            optionGUIDs) throws FVTUnexpectedCondition
    {
        final String activityName = "getSchemaOption";

        try
        {
            SchemaTypeProperties properties = new SchemaTypeProperties();

            properties.setQualifiedName(optionSchemaName);
            properties.setDisplayName(optionSchemaDisplayName);
            properties.setDescription(optionSchemaDescription);
            properties.setTypeName("SchemaTypeChoice");

            String schemaOptionGUID = client.createAnchoredSchemaType(userId, assetManagerGUID, assetManagerName, true, assetGUID,null, false, false, properties);

            if (schemaOptionGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for schemaCreate)");
            }
            else
            {
                client.setupSchemaTypeParent(userId, assetManagerGUID, assetManagerName, true, schemaOptionGUID, assetGUID, "Asset", null, false, false, null);

                if (optionGUIDs != null)
                {
                    for (String optionGUID : optionGUIDs)
                    {
                        client.setupSchemaElementRelationship(userId, assetManagerGUID, assetManagerName, true, schemaOptionGUID, optionGUID, "SchemaTypeOption", null, false, false, null);
                    }
                }

                checkSchemaOptionOK(client, assetManagerGUID, schemaOptionGUID, activityName, userId, assetGUID, optionGUIDs);
            }

            return schemaOptionGUID;
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
     * Check a map is gone.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the schema manager
     * @param mapGUID unique id of the map to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkMapGone(DataAssetExchangeClient client,
                              String                  assetManagerGUID,
                              String                  mapGUID,
                              String                  activityName,
                              String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaTypeElement retrievedElement = client.getSchemaTypeByGUID(userId, assetManagerGUID, assetManagerName, mapGUID, null, false,false);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Map returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(getMapByGUID returned");
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
     * Check a map is stored OK.
     *
     * @param client interface to Asset Manager OMAS
     * @param mapGUID unique id of the schemaOption
     * @param activityName name of calling activity
     * @param userId calling user
     * @param mapFromGUID schema type to map from
     * @param mapToGUID schema type to map to
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkMapOK(DataAssetExchangeClient client,
                            String                  assetManagerGUID,
                            String                  mapGUID,
                            String                  activityName,
                            String                  userId,
                            String                  mapFromGUID,
                            String                  mapToGUID) throws FVTUnexpectedCondition
    {

        try
        {
            SchemaTypeElement    retrievedElement = client.getSchemaTypeByGUID(userId, assetManagerGUID, assetManagerName, mapGUID, null, false, false);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no MapElement from Retrieve)");
            }

            System.out.println("Map Element: " + retrievedElement);
            validateAnchorGUID(activityName, retrievedElement);

            SchemaTypeProperties retrievedMap  = retrievedElement.getSchemaTypeProperties();

            if (retrievedMap == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no MapProperties from Retrieve)");
            }

            if (! mapName.equals(retrievedMap.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! mapDisplayName.equals(retrievedMap.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! mapDescription.equals(retrievedMap.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }

            List<SchemaTypeElement> mapList = client.getSchemaTypeByName(userId, assetManagerGUID, assetManagerName, mapName, 0, maxPageSize, null, false, false);

            if (mapList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Map for RetrieveByName)");
            }
            else if (mapList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty Map list for RetrieveByName)");
            }
            else if (mapList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Map list for RetrieveByName contains" + mapList.size() +
                                                         " elements)");
            }

            retrievedElement = mapList.get(0);
            retrievedMap = retrievedElement.getSchemaTypeProperties();

            if (! mapName.equals(retrievedMap.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! mapDisplayName.equals(retrievedMap.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! mapDescription.equals(retrievedMap.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            if ((mapFromGUID == null) && (retrievedElement.getMapFromElement() != null))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Unexpected MapFrom from RetrieveByName) Retrieved element is: " + retrievedElement);
            }

            if ((mapFromGUID != null) && (! mapFromGUID.equals(retrievedElement.getMapFromElement().getElementHeader().getGUID())))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad MapFrom from RetrieveByName) Retrieved element is: " + retrievedElement);
            }

            if ((mapToGUID == null) && (retrievedElement.getMapToElement() != null))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Unexpected MapTo from RetrieveByName) Retrieved element is: " + retrievedElement);
            }

            if ((mapToGUID != null) && (! mapToGUID.equals(retrievedElement.getMapToElement().getElementHeader().getGUID())))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad MapTo from RetrieveByName) Retrieved element is: " + retrievedElement);
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
     * Create a map and return its GUID.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the schema manager
     * @param assetGUID unique identifier of anchor asset
     * @param userId calling user
     * @param mapFromGUID schema type to map from
     * @param mapToGUID schema type to map to
     * @return GUID of map
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createMap(DataAssetExchangeClient client,
                             String                  assetManagerGUID,
                             String                  assetGUID,
                             String                  userId,
                             String                  mapFromGUID,
                             String                  mapToGUID) throws FVTUnexpectedCondition
    {
        final String activityName = "createMap";

        try
        {
            SchemaTypeProperties properties = new SchemaTypeProperties();

            properties.setQualifiedName(mapName);
            properties.setDisplayName(mapDisplayName);
            properties.setDescription(mapDescription);
            properties.setTypeName("MapSchemaType");

            String mapGUID = client.createAnchoredSchemaType(userId, assetManagerGUID, assetManagerName, true, assetGUID, null, false, false, properties);

            if (mapGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for mapCreate)");
            }

            if (mapFromGUID != null)
            {
                client.setupSchemaElementRelationship(userId, assetManagerGUID, assetManagerName, true, mapGUID, mapFromGUID, "MapFromElementType", null, false, false, null);
            }

            if (mapToGUID != null)
            {
                client.setupSchemaElementRelationship(userId, assetManagerGUID, assetManagerName, true, mapGUID, mapToGUID, "MapToElementType", null, false, false, null);
            }

            checkMapOK(client, assetManagerGUID, mapGUID, activityName, userId, mapFromGUID, mapToGUID);

            return mapGUID;
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
     * Check a primitive is gone.
     *
     * @param client interface to Asset Manager OMAS
     * @param primitiveGUID unique id of the primitive to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkPrimitiveGone(DataAssetExchangeClient client,
                                    String                  assetManagerGUID,
                                    String                  primitiveGUID,
                                    String                  activityName,
                                    String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaTypeElement retrievedElement = client.getSchemaTypeByGUID(userId, assetManagerGUID, assetManagerName, primitiveGUID, null, false, false);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Primitive returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(getPrimitiveByGUID returned");
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
     * Check a primitive
     *
     * @param client interface to Asset Manager OMAS
     * @param primitiveGUID unique id of the primitive to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @param primitiveName qualified name of primitive
     * @param primitiveDisplayName display name of primitive
     * @param primitiveDescription description of primitive
     * @param primitiveDataType data type of primitive
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkPrimitiveOK(DataAssetExchangeClient client,
                                  String                  assetManagerGUID,
                                  String                  primitiveGUID,
                                  String                  activityName,
                                  String                  userId,
                                  String                  primitiveName,
                                  String                  primitiveDisplayName,
                                  String                  primitiveDescription,
                                  String                  primitiveDataType) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaTypeElement  retrievedElement = client.getSchemaTypeByGUID(userId, assetManagerGUID, assetManagerName, primitiveGUID, null, false, false);
            
            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " (no PrimitiveElement from Retrieve)");
            }

            System.out.println("Primitive: " + retrievedElement);
            validateAnchorGUID(activityName, retrievedElement);

            SchemaTypeProperties retrievedType  = retrievedElement.getSchemaTypeProperties();

            if (retrievedType == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " (no PrimitiveProperties from Retrieve).  Element is " + retrievedElement);
            }

            if (! primitiveName.equals(retrievedType.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " (Bad qualifiedName " + primitiveName + " from Retrieve).  Element is " + retrievedElement);
            }
            if (! primitiveDisplayName.equals(retrievedType.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " (Bad displayName from Retrieve).  Element is " + retrievedElement);
            }
            if (! primitiveDescription.equals(retrievedType.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " (Bad description from Retrieve).  Element is " + retrievedElement);
            }

            Map<String, Object> extendedProperties = retrievedType.getExtendedProperties();

            if (extendedProperties == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " (Bad extendedProperties from Retrieve).  Element is " + retrievedElement);
            }
            else if (! primitiveDataType.equals(extendedProperties.get("dataType")))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " (Bad data type from Retrieve).  Element is " + retrievedElement);
            }

            List<SchemaTypeElement> primitiveList = client.getSchemaTypeByName(userId, null, null, primitiveName, 0, maxPageSize, null, false, false);

            if (primitiveList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " (no Primitive for RetrieveByName).  Element is " + retrievedElement);
            }
            else if (primitiveList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " (Empty Primitive list for RetrieveByName).  Element is \" + retrievedElement");
            }
            else if (primitiveList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + " (Primitive list for RetrieveByName contains" + primitiveList.size() +
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
     * Create a primitive and return its GUID.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the schema manager
     * @param assetGUID unique id of the map to connect the column to
     * @param userId calling user
     * @param primitiveName qualified name of primitive
     * @param primitiveDisplayName display name of primitive
     * @param primitiveDescription description of primitive
     * @param primitiveDataType data type of primitive
     * @return GUID of schema
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createPrimitive(DataAssetExchangeClient client,
                                   String                  assetManagerGUID,
                                   String                  assetGUID,
                                   String                  userId,
                                   String                  primitiveName,
                                   String                  primitiveDisplayName,
                                   String                  primitiveDescription,
                                   String                  primitiveDataType) throws FVTUnexpectedCondition
    {
        final String activityName = "createPrimitive";

        try
        {
            SchemaTypeProperties  properties = new SchemaTypeProperties();

            properties.setQualifiedName(primitiveName);
            properties.setDisplayName(primitiveDisplayName);
            properties.setDescription(primitiveDescription);

            Map<String, Object> extendedProperties = new HashMap<>();

            extendedProperties.put("dataType", primitiveDataType);

            properties.setTypeName("PrimitiveSchemaType");
            properties.setExtendedProperties(extendedProperties);

            String primitiveGUID = client.createAnchoredSchemaType(userId, assetManagerGUID, assetManagerName, true, assetGUID, null, false, false, properties);

            if (primitiveGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for columnCreate)");
            }
            else
            {
                checkPrimitiveOK(client, assetManagerGUID, primitiveGUID, activityName, userId, primitiveName, primitiveDisplayName, primitiveDescription, primitiveDataType);
            }

            return primitiveGUID;
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
