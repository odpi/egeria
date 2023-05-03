/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.fvt.assets;

import org.odpi.openmetadata.accessservices.assetowner.client.AssetOwner;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.PrimitiveSchemaTypeProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.TemplateProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PrimitiveSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CreateAssetTest calls the AssetOwnerClient to create an asset with attachments
 * and then retrieve the results.
 */
public class CreateAssetTest
{
    private final static String testCaseName       = "CreateAssetTest";

    private final static int    maxPageSize        = 100;

    /*
     * The asset name is constant - the guid is created as part of the test.
     */
    private final static String assetQualifiedName = "TestAsset qualifiedName";
    private final static String assetFullQualifiedName = "TestAsset qualifiedName-full";
    private final static String assetResourceName   = "Asset resourceName";
    private final static String assetVersionIdentifier   = "Asset versionIdentifier";
    private final static String assetResourceDescription = "Asset resourceDescription";
    private final static String assetDisplayName            = "Asset displayName";
    private final static String assetDisplayDescription     = "Asset displayDescription";
    private final static String assetDisplaySummary     = "Asset displaySummary";
    private final static String assetAbbreviation     = "Asset abbreviation";
    private final static String assetUsage     = "Asset usage";
    private final static String assetOriginPropertyName = "Asset origin";
    private final static String assetOriginPropertyValue = "Asset origin value";
    private final static String assetAdditionalPropertyName = "TestAsset additionalPropertyName";
    private final static String assetAdditionalPropertyValue = "TestAsset additionalPropertyValue";

    private final static String assetCopyResourceName   = "Asset resourceName Copy";


    /*
     * The schemaType name is constant - the guid is created as part of the test.
     */
    private final static String schemaTypeName         = "SchemaType qualifiedNAme";
    private final static String schemaTypeDisplayName  = "SchemaType displayName";
    private final static String schemaTypeDescription  = "SchemaType description";
    private final static String schemaTypeType         = "SchemaType type";
    private final static String schemaTypeDefaultValue = "SchemaType defaultValue";


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
            CreateAssetTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        CreateAssetTest thisTest = new CreateAssetTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceWiki());

        AssetOwner client = thisTest.getAssetOwnerClient(serverName, serverPlatformRootURL, auditLog);
        String assetGUID = thisTest.getAsset(client, userId);
        String fullAssetGUID = thisTest.getFullAsset(client, userId);
        String schemaTypeGUID = thisTest.getSchemaType(client, assetGUID, userId);
        String copyAssetGUID = thisTest.getCopyAsset(client, fullAssetGUID, userId);
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
     * @param client interface to Asset Owner OMAS
     * @param userId calling user
     * @return GUID of asset
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getAsset(AssetOwner client,
                            String     userId) throws FVTUnexpectedCondition
    {
        String activityName = "getAsset - create";

        try
        {
            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put(assetAdditionalPropertyName, assetAdditionalPropertyValue);

            AssetProperties properties = new AssetProperties();

            properties.setTypeName("Asset");
            properties.setQualifiedName(assetQualifiedName);
            properties.setName(assetResourceName);
            properties.setDescription(assetResourceDescription);
            properties.setAdditionalProperties(additionalProperties);

            String assetGUID = client.addAssetToCatalog(userId, properties);

            Map<String, String> otherOriginValues = new HashMap<>();
            otherOriginValues.put(assetOriginPropertyName, assetOriginPropertyValue);

            client.addAssetOrigin(userId, assetGUID, null, null, otherOriginValues);

            this.validateAsset(client,
                               userId,
                               assetGUID,
                               assetQualifiedName,
                               assetResourceName,
                               null,
                               assetResourceDescription,
                               additionalProperties,
                               activityName);

            activityName = "getAsset - update";

            properties = new AssetProperties();

            properties.setVersionIdentifier(assetVersionIdentifier);
            properties.setDisplayName(assetDisplayName);
            properties.setDisplaySummary(assetDisplaySummary);
            properties.setDisplayDescription(assetDisplayDescription);
            properties.setAbbreviation(assetAbbreviation);
            properties.setUsage(assetUsage);

            client.updateAsset(userId, assetGUID, true, properties);

            this.validateAsset(client,
                               userId,
                               assetGUID,
                               assetQualifiedName,
                               assetResourceName,
                               assetVersionIdentifier,
                               assetResourceDescription,
                               assetDisplayName,
                               assetDisplaySummary,
                               assetDisplayDescription,
                               assetAbbreviation,
                               assetUsage,
                               additionalProperties,
                               activityName);

            activityName = "getAsset - update 2";

            properties = new AssetProperties();

            properties.setTypeName("Asset");
            properties.setQualifiedName(assetQualifiedName);
            properties.setName(assetResourceName + " - 2");
            properties.setDescription(assetResourceDescription + " - 2");
            properties.setDisplayName(assetDisplayName + " - 2");
            properties.setDisplaySummary(assetDisplaySummary + " - 2");
            properties.setDisplayDescription(assetDisplayDescription + " - 2");
            properties.setAbbreviation(assetAbbreviation + " - 2");
            properties.setUsage(assetUsage + " - 2");
            client.updateAsset(userId, assetGUID, false, properties);

            this.validateAsset(client,
                               userId,
                               assetGUID,
                               assetQualifiedName,
                               assetResourceName + " - 2",
                               null,
                               assetResourceDescription + " - 2",
                               assetDisplayName + " - 2",
                               assetDisplaySummary + " - 2",
                               assetDisplayDescription + " - 2",
                               assetAbbreviation + " - 2",
                               assetUsage + " - 2",
                               null,
                               activityName);

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
     * Create an asset with supplementary properties and return its GUID.
     *
     * @param client interface to Asset Owner OMAS
     * @param userId calling user
     * @return GUID of asset
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getFullAsset(AssetOwner client,
                                String     userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getFullAsset";

        try
        {
            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put(assetAdditionalPropertyName, assetAdditionalPropertyValue);

            AssetProperties properties = new AssetProperties();

            properties.setTypeName("Asset");
            properties.setQualifiedName(assetFullQualifiedName);
            properties.setName(assetResourceName);
            properties.setVersionIdentifier(assetVersionIdentifier);
            properties.setDescription(assetResourceDescription);
            properties.setDisplayName(assetDisplayName);
            properties.setDisplaySummary(assetDisplaySummary);
            properties.setDisplayDescription(assetDisplayDescription);
            properties.setAbbreviation(assetAbbreviation);
            properties.setUsage(assetUsage);
            properties.setAdditionalProperties(additionalProperties);

            String assetGUID = client.addAssetToCatalog(userId, properties);

            Map<String, String> otherOriginValues = new HashMap<>();
            otherOriginValues.put(assetOriginPropertyName, assetOriginPropertyValue);

            client.addAssetOrigin(userId, assetGUID, null, null, otherOriginValues);

            this.validateAsset(client,
                               userId,
                               assetGUID,
                               assetFullQualifiedName,
                               assetResourceName,
                               assetVersionIdentifier,
                               assetResourceDescription,
                               assetDisplayName,
                               assetDisplaySummary,
                               assetDisplayDescription,
                               assetAbbreviation,
                               assetUsage,
                               additionalProperties,
                               activityName);

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

    private void validateAsset(AssetOwner          client,
                               String              userId,
                               String              assetGUID,
                               String              qualifiedName,
                               String              name,
                               String              versionIdentifier,
                               String              description,
                               Map<String, String> additionalProperties,
                               String              activityName) throws FVTUnexpectedCondition
    {
        try
        {
            if (assetGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            AssetElement    retrievedElement    = client.getAssetSummary(userId, assetGUID);
            AssetProperties retrievedAsset = retrievedElement.getAssetProperties();

            if (retrievedAsset == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no asset from Retrieve)");
            }

            if (! qualifiedName.equals(retrievedAsset.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! name.equals(retrievedAsset.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad name from Retrieve)");
            }
            if (versionIdentifier == null)
            {
                if (retrievedAsset.getVersionIdentifier() != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(Non-null versionIdentifier from Retrieve)");
                }
            }
            else if (! versionIdentifier.equals(retrievedAsset.getVersionIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad versionIdentifier from Retrieve)");
            }
            if (! description.equals(retrievedAsset.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            if (additionalProperties == null)
            {
                if (retrievedAsset.getAdditionalProperties() != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(non-null additionalProperties from Retrieve)");
                }
            }
            else
            {
                if (retrievedAsset.getAdditionalProperties() == null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(null additionalProperties from Retrieve)");
                }
                else if (! assetAdditionalPropertyValue.equals(retrievedAsset.getAdditionalProperties().get(assetAdditionalPropertyName)))
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad additionalProperties from Retrieve)");
                }
            }

            List<AssetElement> assetList = client.getAssetsByName(userId, qualifiedName, 0, maxPageSize);

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
            retrievedAsset = retrievedElement.getAssetProperties();

            if (! qualifiedName.equals(retrievedAsset.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! name.equals(retrievedAsset.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad name from RetrieveByName)");
            }
            if (versionIdentifier == null)
            {
                if (retrievedAsset.getVersionIdentifier() != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(Non-null versionIdentifier from RetrieveByName)");
                }
            }
            else if (! versionIdentifier.equals(retrievedAsset.getVersionIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad versionIdentifier from RetrieveByName)");
            }
            if (! description.equals(retrievedAsset.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }
            if (additionalProperties == null)
            {
                if (retrievedAsset.getAdditionalProperties() != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(non-null additionalProperties from RetrieveByName)");
                }
            }
            else
            {
                if (retrievedAsset.getAdditionalProperties() == null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(null additionalProperties from RetrieveByName)");
                }
                else if (! assetAdditionalPropertyValue.equals(retrievedAsset.getAdditionalProperties().get(assetAdditionalPropertyName)))
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad additionalProperties from RetrieveByName)");
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


    private void validateAsset(AssetOwner          client,
                               String              userId,
                               String              assetGUID,
                               String              qualifiedName,
                               String              name,
                               String              versionIdentifier,
                               String              description,
                               String              displayName,
                               String              displaySummary,
                               String              displayDescription,
                               String              abbreviation,
                               String              usage,
                               Map<String, String> additionalProperties,
                               String              activityName) throws FVTUnexpectedCondition
    {
        try
        {
            if (assetGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            AssetElement    retrievedElement    = client.getAssetSummary(userId, assetGUID);
            AssetProperties retrievedAsset = retrievedElement.getAssetProperties();

            if (retrievedAsset == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no asset from Retrieve)");
            }

            if (! qualifiedName.equals(retrievedAsset.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! name.equals(retrievedAsset.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad name from Retrieve)");
            }
            if (versionIdentifier == null)
            {
                if (retrievedAsset.getVersionIdentifier() != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(Non-null versionIdentifier from Retrieve)");
                }
            }
            else if (! versionIdentifier.equals(retrievedAsset.getVersionIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad versionIdentifier from Retrieve)");
            }
            if (! description.equals(retrievedAsset.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            if (! displayName.equals(retrievedAsset.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName <" + retrievedAsset.getDisplayName() + "> from Retrieve)");
            }
            if (! displaySummary.equals(retrievedAsset.getDisplaySummary()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displaySummary from Retrieve)");
            }
            if (! displayDescription.equals(retrievedAsset.getDisplayDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayDescription from Retrieve)");
            }
            if (! abbreviation.equals(retrievedAsset.getAbbreviation()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad abbreviation from Retrieve)");
            }
            if (! usage.equals(retrievedAsset.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from Retrieve)");
            }
            if (additionalProperties == null)
            {
                if (retrievedAsset.getAdditionalProperties() != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(non-null additionalProperties from Retrieve)");
                }
            }
            else
            {
                if (retrievedAsset.getAdditionalProperties() == null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(null additionalProperties from Retrieve)");
                }
                else if (! assetAdditionalPropertyValue.equals(retrievedAsset.getAdditionalProperties().get(assetAdditionalPropertyName)))
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad additionalProperties from Retrieve)");
                }
            }

            if (retrievedAsset.getOtherOriginValues() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(null origin values from Retrieve)");
            }

            List<AssetElement> assetList = client.getAssetsByName(userId, qualifiedName, 0, maxPageSize);

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
            retrievedAsset = retrievedElement.getAssetProperties();

            if (! qualifiedName.equals(retrievedAsset.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! name.equals(retrievedAsset.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad name from RetrieveByName)");
            }
            if (versionIdentifier == null)
            {
                if (retrievedAsset.getVersionIdentifier() != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(Non-null versionIdentifier from RetrieveByName)");
                }
            }
            else if (! versionIdentifier.equals(retrievedAsset.getVersionIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad versionIdentifier from RetrieveByName)");
            }
            if (! description.equals(retrievedAsset.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }
            if (! displayName.equals(retrievedAsset.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! displaySummary.equals(retrievedAsset.getDisplaySummary()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displaySummary from RetrieveByName)");
            }
            if (! displayDescription.equals(retrievedAsset.getDisplayDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayDescription from RetrieveByName)");
            }
            if (! abbreviation.equals(retrievedAsset.getAbbreviation()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad abbreviation from RetrieveByName)");
            }
            if (! usage.equals(retrievedAsset.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from RetrieveByName)");
            }
            if (additionalProperties == null)
            {
                if (retrievedAsset.getAdditionalProperties() != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(non-null additionalProperties from RetrieveByName)");
                }
            }
            else
            {
                if (retrievedAsset.getAdditionalProperties() == null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(null additionalProperties from RetrieveByName)");
                }
                else if (! assetAdditionalPropertyValue.equals(retrievedAsset.getAdditionalProperties().get(assetAdditionalPropertyName)))
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad additionalProperties from RetrieveByName)");
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
     * Create a schemaType and return its GUID.
     *
     * @param client interface to Asset Owner OMAS
     * @param assetGUID unique id of the schemaType manager
     * @param userId calling user
     * @return GUID of schemaType
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getSchemaType(AssetOwner client,
                                 String     assetGUID,
                                 String     userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getSchemaType";

        try
        {
            PrimitiveSchemaTypeProperties properties = new PrimitiveSchemaTypeProperties();

            properties.setQualifiedName(schemaTypeName);
            properties.setDisplayName(schemaTypeDisplayName);
            properties.setDescription(schemaTypeDescription);
            properties.setDataType(schemaTypeType);
            properties.setDefaultValue(schemaTypeDefaultValue);
            properties.setTypeName("PrimitiveSchemaType");

            String schemaTypeGUID = client.addSchemaTypeToAsset(userId, assetGUID, properties);

            if (schemaTypeGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for CreateSchemaType)");
            }

            AssetUniverse     assetUniverse = client.getAssetProperties(userId, assetGUID);

            if (assetUniverse == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no asset universe for CreateSchemaType)");
            }

            SchemaType schemaType = assetUniverse.getSchema();

            if (schemaType == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no schema type for CreateSchemaType)");
            }

            if (! schemaTypeGUID.equals(schemaType.getGUID()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(wrong schema type GUID for CreateSchemaType - " + schemaType.getGUID() + "rather than" + schemaTypeGUID + ")");
            }

            if (! schemaTypeName.equals(schemaType.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(wrong qualifiedName for CreateSchemaType - " + schemaType.getQualifiedName() + "rather than" + schemaTypeName + ")");
            }

            if (! schemaTypeDisplayName.equals(schemaType.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(wrong displayName for CreateSchemaType - " + schemaType.getDisplayName() + "rather than" + schemaTypeDisplayName + ")");
            }

            if (! schemaTypeDescription.equals(schemaType.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(wrong description for CreateSchemaType - " + schemaType.getDescription() + "rather than" + schemaTypeDescription + ")");
            }

            if (schemaType instanceof PrimitiveSchemaType primitiveSchemaType)
            {
                if (! schemaTypeType.equals(primitiveSchemaType.getDataType()))
                {
                    throw new FVTUnexpectedCondition(testCaseName,
                                                     activityName + "(wrong data type for CreateSchemaType - " + primitiveSchemaType.getDataType() + "rather than" + schemaTypeType + ")");
                }
            }
            else
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(wrong type for CreateSchemaType - " + schemaType.getClass().getName() + "rather than PrimitiveSchemaType)");
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

    /**
     * Create a copy of the full asset and return its GUID.
     *
     * @param client interface to Asset Owner OMAS
     * @param assetGUID unique id of the schemaType manager
     * @param userId calling user
     * @return GUID of schemaType
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getCopyAsset(AssetOwner client,
                                String     assetGUID,
                                String     userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getCopyAsset";

        try
        {
            TemplateProperties properties = new TemplateProperties();

            properties.setQualifiedName(assetCopyResourceName);

            String assetCopyGUID = client.addAssetToCatalogUsingTemplate(userId, assetGUID, properties);

            if (assetCopyGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for CreateByTemplate)");
            }



            return assetCopyGUID;
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
