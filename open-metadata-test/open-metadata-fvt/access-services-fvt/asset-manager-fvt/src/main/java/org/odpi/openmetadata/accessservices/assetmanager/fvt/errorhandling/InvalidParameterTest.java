/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.assetmanager.client.AssetManagerEventClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.ExternalAssetManagerClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.GlossaryExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.AssetManagerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * InvalidParameterTest calls each non-constructor method with a series of null or invalid parameters.
 * It ensures that InvalidParameterException is thrown.
 */
public class InvalidParameterTest
{
    private final static String testCaseName       = "InvalidParameterTest";

    private final static int    maxPageSize      = 100;
    private final static String assetManagerGUID = "TestExternalSourceGUID";
    private final static String assetManagerName = "TestExternalSourceName";


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
            InvalidParameterTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        InvalidParameterTest thisTest = new InvalidParameterTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceWiki());

        thisTest.testAssetManagerClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testGlossaryExchangeClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testAssetManagerEventClient(serverName, serverPlatformRootURL, userId, auditLog);
    }



    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testAssetManagerClient(String   serverName,
                                        String   serverPlatformRootURL,
                                        String   userId,
                                        AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testAssetManagerClient";

        try
        {
            AssetManagerRESTClient     restClient = new AssetManagerRESTClient(serverName, serverPlatformRootURL);
            ExternalAssetManagerClient client     = new ExternalAssetManagerClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);

            testCreateExternalAssetManager(userId, client);
            testGetExternalAssetManagerGUID(userId, client);
            testGetAssetManagerGUID(userId, client);
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
     * Test combinations of invalid parameters passed to createExternalAssetManager.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateExternalAssetManager(String               userId,
                                                ExternalAssetManagerClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateExternalAssetManager";

        try
        {
            testCreateExternalAssetManagerNoUserId(client);
            testCreateExternalAssetManagerNoProperties(client, userId);
            testCreateExternalAssetManagerNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createExternalAssetManager.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateExternalAssetManagerNoUserId(ExternalAssetManagerClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateExternalAssetManagerNoUserId";

        AssetManagerProperties assetManagerProperties = new AssetManagerProperties();

        try
        {
            client.createExternalAssetManager(null, assetManagerProperties);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null properties passed to createExternalAssetManager.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateExternalAssetManagerNoProperties(ExternalAssetManagerClient client,
                                                            String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateExternalAssetManagerNoProperties";

        try
        {
            client.createExternalAssetManager(userId, null);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null properties passed to createExternalAssetManager.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateExternalAssetManagerNoQualifiedName(ExternalAssetManagerClient client,
                                                               String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateExternalAssetManagerNoQualifiedName";

        try
        {
            AssetManagerProperties assetManagerProperties = new AssetManagerProperties();

            client.createExternalAssetManager(userId, assetManagerProperties);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test combinations of invalid parameters passed to getExternalAssetManagerGUID.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetExternalAssetManagerGUID(String               userId,
                                       ExternalAssetManagerClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateExternalAssetManager";

        try
        {
            testGetExternalAssetManagerGUIDNoUserId(client);
            testGetExternalAssetManagerGUIDNoProperties(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to getExternalAssetManagerGUID.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetExternalAssetManagerGUIDNoUserId(ExternalAssetManagerClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetExternalAssetManagerGUIDNoUserId";

        try
        {
            client.getExternalAssetManagerGUID(null, assetManagerName);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null properties passed to getExternalAssetManagerGUID.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetExternalAssetManagerGUIDNoProperties(ExternalAssetManagerClient client,
                                                             String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetExternalAssetManagerGUIDNoProperties";

        try
        {
            client.getExternalAssetManagerGUID(userId, null);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test combinations of invalid parameters passed to createGlossaryExchange.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetAssetManagerGUID(String             userId,
                                         ExternalAssetManagerClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testAssetManager";

        try
        {
            testGetAssetManagerGUIDNoUserId(client);
            testGetAssetManagerGUIDNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }




    /**
     * Test null userId passed to getAssetManagerGUID.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetAssetManagerGUIDNoUserId(ExternalAssetManagerClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetAssetManagerGUIDNoUserId";

        try
        {
            client.getExternalAssetManagerGUID(null, "TestQualifiedName");
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null qualifiedName passed to getAssetManagerGUID.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetAssetManagerGUIDNoQualifiedName(ExternalAssetManagerClient client,
                                                        String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetAssetManagerGUIDNoQualifiedName";

        try
        {
            client.getExternalAssetManagerGUID(userId, null);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGlossaryExchangeClient(String   serverName,
                                            String   serverPlatformRootURL,
                                            String   userId,
                                            AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testGlossaryExchangeClient";

        try
        {
            AssetManagerRESTClient restClient = new AssetManagerRESTClient(serverName, serverPlatformRootURL);
            GlossaryExchangeClient client = new GlossaryExchangeClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);

            testCreateGlossary(userId, client);
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
     * Test combinations of invalid parameters passed to createGlossary.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateGlossary(String                userId,
                                    GlossaryExchangeClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGlossary";

        try
        {
            testCreateGlossaryNoUserId(client);
            testCreateGlossaryNoProperties(client, userId);
            testCreateGlossaryNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createGlossary.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateGlossaryNoUserId(GlossaryExchangeClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGlossaryNoUserId";

        GlossaryProperties properties = new GlossaryProperties();

        try
        {
            client.createGlossary(null,
                                  assetManagerGUID,
                                  assetManagerName,
                                  true,
                                  null,
                                  properties);

            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }



    /**
     * Test null properties passed to createGlossary.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateGlossaryNoProperties(GlossaryExchangeClient client,
                                                String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGlossaryNoQualifiedName";

        try
        {
            client.createGlossary(userId,
                                  assetManagerGUID,
                                  assetManagerName,
                                  true,
                                  null,
                                  null);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test incomplete properties passed to createGlossary.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateGlossaryNoQualifiedName(GlossaryExchangeClient client,
                                                   String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGlossaryNoQualifiedName";

        GlossaryProperties properties = new GlossaryProperties();

        try
        {
            client.createGlossary(userId,
                                  assetManagerGUID,
                                  assetManagerName,
                                  true,
                                  null,
                                  properties);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }



    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testAssetManagerEventClient(String   serverName,
                                            String   serverPlatformRootURL,
                                            String   userId,
                                            AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testAssetManagerEventClient";

        try
        {
            AssetManagerEventClient client     = new AssetManagerEventClient(serverName, serverPlatformRootURL, userId, null, maxPageSize, auditLog, "");

            testRegisterListener(userId, client);
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
     * Test combinations of invalid parameters passed to registerListener.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testRegisterListener(String                 userId,
                                      AssetManagerEventClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateNestedFolders";

        try
        {
            testRegisterListenerNoUserId(client);
            testRegisterListenerNoListener(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to registerListener.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testRegisterListenerNoUserId(AssetManagerEventClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "testRegisterListenerNoUserId";

        try
        {
            client.registerListener(null, null);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null listener passed to registerListener.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testRegisterListenerNoListener(AssetManagerEventClient client,
                                                String                 userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testRegisterListenerNoListener";

        try
        {
            client.registerListener(userId, null);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }
}
