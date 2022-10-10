/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.fvt.discovery;

import org.odpi.openmetadata.accessservices.assetowner.client.AssetOwner;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
import org.odpi.openmetadata.accessservices.assetowner.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.PrimitiveSchemaTypeProperties;
import org.odpi.openmetadata.accessservices.discoveryengine.client.DiscoveryAnalysisReportClient;
import org.odpi.openmetadata.accessservices.discoveryengine.client.DiscoveryEngineClient;
import org.odpi.openmetadata.accessservices.discoveryengine.client.rest.ODFRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.HashMap;
import java.util.Map;

/**
 * CreateDiscoveryReportTest calls the DiscoveryReportClient to create a database with schemas tables and columns
 * and then retrieve the results.
 */
public class CreateDiscoveryReportTest
{
    private final static String testCaseName       = "CreateDiscoveryReportTest";

    private final static int    maxPageSize        = 100;

    /*
     * The database manager name is constant - the guid is created as part of the test.
     */
    private final static String discoveryReportName                = "TestDiscoveryReport";
    private final static String discoveryReportDisplayName         = "DiscoveryReport displayName";
    private final static String discoveryReportDescription         = "DiscoveryReport description";
    private final static String discoveryReportStepOne             = "DiscoveryReport stepOne";
    private final static String discoveryReportStepTwo             = "DiscoveryReport stepTwo";
    private final static String discoveryReportAnalysisParam1Name  = "Analysis Parameter 1";
    private final static String discoveryReportAnalysisParam1Value = "Analysis Parameter 1 Value";
    private final static String discoveryReportAnalysisParam2Name  = "Analysis Parameter 2";
    private final static String discoveryReportAnalysisParam2Value = "Analysis Parameter 2 Value";
    private final static String discoveryReportVersion             = "DiscoveryReport version";

    /*
     * The asset name is constant - the guid is created as part of the test.
     */
    private final static String assetName            = "TestAsset qualifiedName";
    private final static String assetDisplayName     = "Asset displayName";
    private final static String assetDescription     = "Asset description";
    private final static String assetAdditionalPropertyName  = "TestAsset additionalPropertyName";
    private final static String assetAdditionalPropertyValue = "TestAsset additionalPropertyValue";

    /*
     * The schemaType name is constant - the guid is created as part of the test.
     */
    private final static String schemaTypeName         = "SchemaType qualifiedNAme";
    private final static String schemaTypeDisplayName  = "SchemaType displayName";
    private final static String schemaTypeDescription  = "SchemaType description";
    private final static String schemaTypeType         = "SchemaType type";
    private final static String schemaTypeDefaultValue = "SchemaType defaultValue";


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
    private final static String databaseTableType        = "DatabaseTable type";
    private final static String databaseTableVersion     = "DatabaseTable version";


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
            CreateDiscoveryReportTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        CreateDiscoveryReportTest thisTest = new CreateDiscoveryReportTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceWiki());

        AssetOwner assetOwnerClient = thisTest.getAssetOwnerClient(serverName, serverPlatformRootURL, auditLog);
        String     assetGUID        = thisTest.getAsset(assetOwnerClient, userId);
        String     schemaTypeGUID   = thisTest.getSchemaType(assetOwnerClient, assetGUID, userId);


        DiscoveryEngineClient client = thisTest.getDiscoveryEngineClient(serverName, serverPlatformRootURL, auditLog);
        /* String discoveryReportGUID = thisTest.getDiscoveryReport(client,
                                                                 userId,
                                                                 assetGUID,
                                                                 null,
                                                                 null); */

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
        final String activityName = "getAsset";

        try
        {
            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put(assetAdditionalPropertyName, assetAdditionalPropertyValue);

            AssetProperties properties = new AssetProperties();

            properties.setTypeName("Asset");
            properties.setQualifiedName(assetName);
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


    /**
     * Create and return a discovery engine client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private DiscoveryEngineClient getDiscoveryEngineClient(String   serverName,
                                                           String   serverPlatformRootURL,
                                                           AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getDiscoveryEngineClient";

        try
        {
            ODFRESTClient restClient = new ODFRESTClient(serverName, serverPlatformRootURL);

            return new DiscoveryEngineClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a discovery report.
     *
     * @param client interface to Discovery  engine OMAS
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     * @return unique identifier for discovery report
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDiscoveryReport(DiscoveryEngineClient client,
                                      String                userId,
                                      String                assetGUID,
                                      String                governanceEngineGUID,
                                      String                governanceServiceGUID) throws FVTUnexpectedCondition
    {
        final String activityName = "getDiscoveryReport";

        Map<String, String> analysisParameters = new HashMap<>();

        analysisParameters.put(discoveryReportAnalysisParam1Name, discoveryReportAnalysisParam1Value);
        analysisParameters.put(discoveryReportAnalysisParam2Name, discoveryReportAnalysisParam2Value);

        try
        {
            DiscoveryAnalysisReportClient discoveryReport = new DiscoveryAnalysisReportClient(userId,
                                                                                              DiscoveryRequestStatus.WAITING,
                                                                                              assetGUID,
                                                                                              analysisParameters,
                                                                                              discoveryReportStepOne,
                                                                                              discoveryReportName,
                                                                                              discoveryReportDisplayName,
                                                                                              discoveryReportDescription,
                                                                                              governanceEngineGUID,
                                                                                              governanceServiceGUID,
                                                                                              client);

            DiscoveryRequestStatus discoveryRequestStatus = discoveryReport.getDiscoveryRequestStatus();

            if (discoveryRequestStatus == DiscoveryRequestStatus.WAITING)
            {
                discoveryReport.setDiscoveryRequestStatus(DiscoveryRequestStatus.ACTIVATING);

                discoveryRequestStatus = discoveryReport.getDiscoveryRequestStatus();

                if (discoveryRequestStatus != DiscoveryRequestStatus.ACTIVATING)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(status " + discoveryRequestStatus + " not ACTIVATING)");
                }
            }
            else
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(status " + discoveryRequestStatus + " not WAITING)");
            }

            if (! discoveryReportName.equals(discoveryReport.getReportQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(qualified name " + discoveryReport.getReportQualifiedName() + " not " + discoveryReportName + ")");
            }

            if (! discoveryReportDisplayName.equals(discoveryReport.getReportDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(display name " + discoveryReport.getReportDisplayName() + " not " + discoveryReportDisplayName + ")");
            }

            return discoveryReport.getDiscoveryReportGUID();
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
