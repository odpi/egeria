/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.fvt;

import org.odpi.openmetadata.accessservices.assetowner.client.AssetOwner;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
import org.odpi.openmetadata.accessservices.assetowner.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.PrimitiveSchemaTypeProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.HashMap;
import java.util.Map;

/**
 * AssetOwnerFactory uses the Asset Owner OMAS methods to create assets and schema elements that can be used by Asset Consumer OMAS.
 */
public class AssetOwnerFactory
{
    /*
     * The asset name is constant - the guid is created as part of the test.
     */
    private final static String assetName            = "TestAssetAnchor";
    private final static String assetDisplayName     = "Asset displayName";
    private final static String assetDescription     = "Asset description";
    private final static String assetAdditionalPropertyName  = "TestAsset additionalPropertyName";
    private final static String assetAdditionalPropertyValue = "TestAsset additionalPropertyValue";

    /*
     * The schemaType name is constant - the guid is created as part of the test.
     */
    private final static String schemaTypeName         = "TestSchemaType";
    private final static String schemaTypeDisplayName  = "SchemaType displayName";
    private final static String schemaTypeDescription  = "SchemaType description";
    private final static String schemaTypeType         = "SchemaType type";
    private final static String schemaTypeDefaultValue = "SchemaType defaultValue";

    private final String testCaseName;
    private final AssetOwner client;

    /**
     * Simple constructor
     *
     * @param testCaseName calling test case
     * @param serverName server name
     * @param serverPlatformRootURL server location
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition something went wrong
     */
    public AssetOwnerFactory(String   testCaseName,
                             String   serverName,
                             String   serverPlatformRootURL,
                             AuditLog auditLog) throws FVTUnexpectedCondition
    {
        this.testCaseName = testCaseName;
        this.client = getAssetOwnerClient(serverName, serverPlatformRootURL, auditLog);
    }


    /**
     * Create and return an AssetOwner client.
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

            final int maxPageSize = 100;
            return new AssetOwner(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }



    /**
     * Create an asset and return its GUID.
     *
     * @param userId calling user
     * @return GUID of asset
     * @throws FVTUnexpectedCondition the test case failed
     */
    public String getAsset(String userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getAsset";

        try
        {
            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put(assetAdditionalPropertyName, assetAdditionalPropertyValue);

            AssetProperties properties = new AssetProperties();

            properties.setTypeName("Asset");
            properties.setQualifiedName(testCaseName + ":" + assetName);
            properties.setDisplayName(assetDisplayName);
            properties.setDescription(assetDescription);
            properties.setAdditionalProperties(additionalProperties);

            String assetGUID = client.addAssetToCatalog(userId, properties);

            if (assetGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
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
     * Create an asset and return its GUID.
     *
     * @param userId calling user
     * @param templateGUID unique identifier of template to use
     * @return GUID of asset
     * @throws FVTUnexpectedCondition the test case failed
     */
    public String getAssetFromTemplate(String userId,
                                       String templateGUID) throws FVTUnexpectedCondition
    {
        final String activityName = "getAsset";

        try
        {
            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put(assetAdditionalPropertyName, assetAdditionalPropertyValue);

            TemplateProperties properties = new TemplateProperties();

            properties.setQualifiedName(testCaseName + ":" + assetName + "-2");
            properties.setDisplayName(assetDisplayName + "-2");
            properties.setDescription(assetDescription + "-2");

            String assetGUID = client.addAssetToCatalogUsingTemplate(userId, templateGUID, properties);

            if (assetGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
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
     * Create a schemaType and return its GUID.
     *
     * @param userId calling user
     * @param assetGUID unique id of the schemaType manager
     * @return GUID of schemaType
     * @throws FVTUnexpectedCondition the test case failed
     */
    public String getSchemaType(String     userId,
                                String     assetGUID) throws FVTUnexpectedCondition
    {
        final String activityName = "getSchemaType";

        try
        {
            PrimitiveSchemaTypeProperties properties = new PrimitiveSchemaTypeProperties();

            properties.setQualifiedName(testCaseName + ":" + schemaTypeName);
            properties.setDisplayName(schemaTypeDisplayName);
            properties.setDescription(schemaTypeDescription);
            properties.setDataType(schemaTypeType);
            properties.setDefaultValue(schemaTypeDefaultValue);
            properties.setTypeName("PrimitiveSchemaType");

            String schemaTypeGUID = client.addSchemaTypeToAsset(userId, assetGUID, properties);

            if (schemaTypeGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            return schemaTypeGUID;
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
