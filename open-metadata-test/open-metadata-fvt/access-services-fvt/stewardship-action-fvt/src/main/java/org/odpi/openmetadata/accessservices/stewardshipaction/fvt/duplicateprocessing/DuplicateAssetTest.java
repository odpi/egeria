/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.stewardshipaction.fvt.duplicateprocessing;

import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;
import org.odpi.openmetadata.accessservices.assetconsumer.client.rest.AssetConsumerRESTClient;
import org.odpi.openmetadata.accessservices.assetowner.client.AssetOwner;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
import org.odpi.openmetadata.accessservices.assetowner.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.MeaningProperties;
import org.odpi.openmetadata.accessservices.stewardshipaction.client.StewardshipAction;
import org.odpi.openmetadata.accessservices.stewardshipaction.client.rest.StewardshipActionRESTClient;
import org.odpi.openmetadata.accessservices.stewardshipaction.metadataelements.AssetElement;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;


/**
 * CreateAssetTest calls the StewardshipActionClient to create an asset with with attachments
 * and then retrieve the results.
 */
public class DuplicateAssetTest
{
    private final static String testCaseName       = "CreateAssetTest";

    private final static int    maxPageSize        = 100;

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
            DuplicateAssetTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        DuplicateAssetTest thisTest = new DuplicateAssetTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceWiki());

        StewardshipAction client = thisTest.getStewardshipActionClient(serverName, serverPlatformRootURL, auditLog);
        String assetGUID = thisTest.getAsset(client, userId);
    }


    /**
     * Create and return a StewardshipAction client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private StewardshipAction getStewardshipActionClient(String   serverName,
                                                         String   serverPlatformRootURL,
                                                         AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getStewardshipActionClient";

        try
        {
            StewardshipActionRESTClient restClient = new StewardshipActionRESTClient(serverName, serverPlatformRootURL);

            return new StewardshipAction(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create and return an AssetConsumer client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private AssetConsumer getAssetConsumerClient(String   serverName,
                                                 String   serverPlatformRootURL,
                                                 AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getAssetConsumerClient";

        try
        {
            AssetConsumerRESTClient restClient = new AssetConsumerRESTClient(serverName, serverPlatformRootURL);

            MeaningProperties likeProperties = null;

            return new AssetConsumer(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
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

            AssetProperties assetProperties = null;

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
     * @param client interface to Stewardship Action OMAS
     * @param userId calling user
     * @return GUID of asset
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getAsset(StewardshipAction client,
                            String     userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getAsset";
        AssetElement assetElement = null;

        return null;
    }



}
