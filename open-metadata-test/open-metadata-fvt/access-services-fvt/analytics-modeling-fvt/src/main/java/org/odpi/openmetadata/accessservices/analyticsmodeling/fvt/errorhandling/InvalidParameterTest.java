/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.errorhandling;


import org.odpi.openmetadata.accessservices.analyticsmodeling.client.ImportClient;
import org.odpi.openmetadata.accessservices.analyticsmodeling.client.SynchronizationClient;
import org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.common.AnalyticsModelingTestBase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
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
public class InvalidParameterTest extends AnalyticsModelingTestBase
{
    private static final AnalyticsAsset ARTIFACT = new AnalyticsAsset();
	private static final int INVALID_PAGE_SIZE = -5;
	private static final int INVALID_START_FROM = -4;
	private static final String EXCEPTION_EXPECTED = "(exception expected)";
	private static final String GUID_DB = "12345";
	private static final String TESTCASENAME = "InvalidParameterTest";

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
        FVTResults results = new FVTResults(TESTCASENAME);

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
        InvalidParameterTest thisTest = new InvalidParameterTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceWiki());

        thisTest.testImportClient_getDatabase(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testImportClient_getSchema(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testImportClient_getTables(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testImportClient_getModule(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testSynchronizationClient_CreateArtifact(serverName, serverPlatformRootURL, userId, auditLog);
    }



    /**
     * Validate all parameters of getDatabase REST function.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testImportClient_getDatabase(String   serverName,
                                        String   serverPlatformRootURL,
                                        String   userId,
                                        AuditLog auditLog) throws FVTUnexpectedCondition
    {
        ImportClient client = getImportClient(serverName, serverPlatformRootURL, auditLog, TESTCASENAME);

        testInvalidUser_getDatabase(client);
        testStartFrom_getDatabase(userId, client);
        testPageSize_getDatabase(userId, client);
    }

	private void testPageSize_getDatabase(String userId, ImportClient client)
			throws FVTUnexpectedCondition 
	{
		final String activityName = "testPageSize_getDatabase";
		try
        {
            client.getDatabases(userId, 0, INVALID_PAGE_SIZE);
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}

	private void testStartFrom_getDatabase(String userId, ImportClient client)
			throws FVTUnexpectedCondition 
	{
		final String activityName = "testStartFrom_getDatabase";
		try
        {
            client.getDatabases(userId, INVALID_START_FROM, 0);
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}


	private void testInvalidUser_getDatabase(ImportClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testInvalidUser_getDatabase";
		try
        {
            client.getDatabases(null, 0, 0);
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}

    /**
     * Validate all parameters of getSchema REST function.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testImportClient_getSchema(String   serverName,
                                        String   serverPlatformRootURL,
                                        String   userId,
                                        AuditLog auditLog) throws FVTUnexpectedCondition
    {
        ImportClient client = getImportClient(serverName, serverPlatformRootURL, auditLog, TESTCASENAME);

        testInvalidUser_getSchemas(client);
        testInvalidGuid_getSchemas(userId, client);
        testStartFrom_getSchemas(userId, client);
        testPageSize_getSchemas(userId, client);
    }

	private void testPageSize_getSchemas(String userId, ImportClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testPageSize_getSchemas";
		try
        {
            client.getSchemas(userId, GUID_DB, 0, INVALID_PAGE_SIZE);
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}
	private void testStartFrom_getSchemas(String userId, ImportClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testStartFrom_getSchemas";
		try
        {
            client.getSchemas(userId, GUID_DB, INVALID_START_FROM, 0);
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}
	private void testInvalidGuid_getSchemas(String userId, ImportClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testInvalidGuid_getSchemas";
		try
        {
            client.getSchemas(userId, null, 0, 0);
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}


	private void testInvalidUser_getSchemas(ImportClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testInvalidUser_getSchemas";
		try
        {
            client.getSchemas(null, GUID_DB, 0, 0);
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}
    /**
     * Validate all parameters of getTables REST function.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testImportClient_getTables(String   serverName,
                                        String   serverPlatformRootURL,
                                        String   userId,
                                        AuditLog auditLog) throws FVTUnexpectedCondition
    {
        ImportClient client = getImportClient(serverName, serverPlatformRootURL, auditLog, TESTCASENAME);

        testInvalidUser_getTables(client);
        testInvalidGuid_getTables(userId, client);
    }


	private void testInvalidGuid_getTables(String userId, ImportClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testInvalidGuid_getTables";
		try
        {
            client.getTables(userId, null, "catalog", "schema");
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}


	private void testInvalidUser_getTables(ImportClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testInvalidUser_getTables";
		try
        {
            client.getTables(null, GUID_DB, "catalog", "schema");
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}
    /**
     * Validate all parameters of getModule REST function.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testImportClient_getModule(String   serverName,
                                        String   serverPlatformRootURL,
                                        String   userId,
                                        AuditLog auditLog) throws FVTUnexpectedCondition
    {
        ImportClient client = getImportClient(serverName, serverPlatformRootURL, auditLog, TESTCASENAME);

        testInvalidUser_getModule(client);
        testInvalidGuid_getModule(userId, client);
    }
    
    private void testInvalidGuid_getModule(String userId, ImportClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testInvalidGuid_getModule";
		try
        {
            client.getModule(userId, null, "catalog", "schema", null);
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}


	private void testInvalidUser_getModule(ImportClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testInvalidUser_getModule";
		try
        {
            client.getModule(null, GUID_DB, "catalog", "schema", null);
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}
    
    /**
     * Validate all parameters of synchronization REST functions.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSynchronizationClient_CreateArtifact(String   serverName,
                                            String   serverPlatformRootURL,
                                            String   userId,
                                            AuditLog auditLog) throws FVTUnexpectedCondition
    {
        
        SynchronizationClient client = getSynchronizationClient(serverName, serverPlatformRootURL, auditLog, TESTCASENAME);

        testInvalidUser_CreateArtifact(client);
        testInvalidUser_UpdateArtifact(client);
        testInvalidUser_DeleteArtifact(client);
    }


	private void testInvalidUser_CreateArtifact(SynchronizationClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testInvalidUser_CreateModule";
		try
        {
            client.createArtifact(null, "serverCapability", null, ARTIFACT);
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}
	private void testInvalidUser_UpdateArtifact(SynchronizationClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testInvalidUser_UpdateArtifact";
		try
        {
            client.updateArtifact(null, "serverCapability", null, ARTIFACT);
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}
	private void testInvalidUser_DeleteArtifact(SynchronizationClient client)
			throws FVTUnexpectedCondition
	{
		final String activityName = "testInvalidUser_DeleteArtifact";
		try
        {
            client.deleteArtifact(null, "serverCapability", null, "identifier");
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName + EXCEPTION_EXPECTED);
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
        	if (!(unexpectedError instanceof InvalidParameterException)) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        	}
        }
	}
}
