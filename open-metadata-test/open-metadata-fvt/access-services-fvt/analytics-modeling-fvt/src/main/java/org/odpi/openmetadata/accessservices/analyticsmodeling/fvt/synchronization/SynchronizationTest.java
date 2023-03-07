/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.synchronization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.commons.io.FilenameUtils;
import org.odpi.openmetadata.accessservices.analyticsmodeling.client.SynchronizationClient;
import org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.RepositoryService;
import org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.common.AnalyticsModelingTestBase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerAssets;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * SynchronizationTest calls the SynchronizationClient to create, update, delete analytic artifacts.
 */
public class SynchronizationTest extends AnalyticsModelingTestBase
{
    private static final String RESPONSE_DOES_NOT_CONTAIN_GUID_OF_THE_AFFECTED_ARTIFACT = 
    		": (response does not contain GUID of the affected artifact)";

	private static final String TESTCASENAME = "SynchronizationTest";

    /*
     * The server software capability source used for artifact operations.
     */
    private static final String SERVERCAPABILITY = "ServerCapabilityAnalytiscModelingFVT_26";

	private static final String INPUT_FOLDER = "/src/test/resources/synchronization/input/";

    private static final ObjectReader OBJECT_READER = new ObjectMapper().reader();

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
            SynchronizationTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        SynchronizationTest thisTest = new SynchronizationTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceWiki());

        SynchronizationClient client = thisTest.getSynchronizationClient(serverName, serverPlatformRootURL, auditLog, TESTCASENAME);

        RepositoryService repositoryClient = thisTest.getRepositoryServiceClient(serverName, userId, serverPlatformRootURL, TESTCASENAME);
        
        testCreateArtifact(repositoryClient, client, userId);
        testUpdateArtifact(repositoryClient, client, userId);
        testDeleteArtifact(client, userId);
        
    }
    
    /**
     * Method to test create artifact operation.
     * @param repositoryClient 
     * @param client to use for test.
     * @param userId for tests.
     * @throws FVTUnexpectedCondition if the test case failed.
     */
    private static void testCreateArtifact(RepositoryService repositoryClient, SynchronizationClient client, String userId)
    		throws FVTUnexpectedCondition
    {
    	String activityName = "testCreateArtifact";
        try {
        	AnalyticsAsset artifact = readObjectJson(INPUT_FOLDER, "create", AnalyticsAsset.class);
           	repositoryClient.createRequiredEntities(artifact);
        	ResponseContainerAssets assets = client.createArtifact(userId, SERVERCAPABILITY, null, artifact);
            
        	if (assets.getAssetsList().size() != 1) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName + RESPONSE_DOES_NOT_CONTAIN_GUID_OF_THE_AFFECTED_ARTIFACT);
            }

        }
        catch (Exception unexpectedError) {
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        }
    }

    /**
     * Method to test update artifact operation.
     * @param client to use for test.
     * @param userId for tests.
     * @throws FVTUnexpectedCondition if the test case failed.
     */
    private static void testUpdateArtifact(RepositoryService repositoryClient, SynchronizationClient client, String userId)
    		throws FVTUnexpectedCondition
    {
    	String activityName = "testUpdateArtifact";
        try {
        	AnalyticsAsset artifact = readObjectJson(INPUT_FOLDER, "update", AnalyticsAsset.class);
        	repositoryClient.createRequiredEntities(artifact);
        	ResponseContainerAssets assets = client.updateArtifact(userId, SERVERCAPABILITY, null, artifact);
        	
        	if (assets.getAssetsList().size() != 1) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName + RESPONSE_DOES_NOT_CONTAIN_GUID_OF_THE_AFFECTED_ARTIFACT);
            }
        }
        catch (Exception unexpectedError) {
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        }
    }


    /**
     * Method to test delete artifact operation.
     * @param client to use for test.
     * @param userId for tests.
     * @throws FVTUnexpectedCondition if the test case failed.
     */
    private static void testDeleteArtifact(SynchronizationClient client, String userId)
    		throws FVTUnexpectedCondition
    {
    	String activityName = "testDeleteArtifact";
        try {
        	ResponseContainerAssets assets = client.deleteArtifact(userId, SERVERCAPABILITY, null, "iBASEMODULE");	// id from create operation
        	
        	if (assets.getAssetsList().size() != 1) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName + RESPONSE_DOES_NOT_CONTAIN_GUID_OF_THE_AFFECTED_ARTIFACT);
            }
        } 
        catch (Exception unexpectedError) {
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        }
    }
    
	/**
	 * Build object from JSON content provided in file.
	 * @param <T> class of the object to build.
	 * @param folder location of the file.
	 * @param fileName without extension, JSON extension is appended.
	 * @param cls class to build.
	 * @return object created from json file.
	 * @throws IOException if input file cannot be read.
	 */
	public static <T extends Object> T readObjectJson(String folder, String fileName, Class<T> cls)
			throws IOException
	{
		String root = Paths.get(".").toAbsolutePath().normalize().toString();
		String input  =  root + folder + FilenameUtils.getName(fileName) + ".json";
        return OBJECT_READER.readValue(Files.readString(Paths.get(input).normalize(), Charset.defaultCharset()), cls);
	}
}
