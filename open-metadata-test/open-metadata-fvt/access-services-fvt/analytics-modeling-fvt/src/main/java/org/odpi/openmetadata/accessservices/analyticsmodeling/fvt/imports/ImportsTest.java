/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.imports;

import java.util.Arrays;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.client.ImportClient;
import org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.RepositoryService;
import org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.common.AnalyticsModelingTestBase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ModuleTableFilter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

/**
 * ImportsTest calls the AnalyticsModelingClient to ensure it is possible to 
 * get information about relational database schema.
 */
public class ImportsTest extends AnalyticsModelingTestBase
{
	private static final String TBL_DATE = "Date";
	private static final String TBL_LOCATION = "Location";
    private static final String TBL_PRODUCT = "Product";
	private static final String COL_DATE = "Date";
	private static final String COL_DESTINATION = "Destination";
	private static final String COL_PRODUCT_NAME = "ProductName";
	private static final String DATETIME = "DATETIME";
	private static final String TIMESTAMP = "TIMESTAMP";
	private static final String TESTCASENAME = "ImportsTest";
	private static final String DATABASE_GOSALES = "GOSALES";
	private static final String DATA_TYPE_VARCHAR = "VARCHAR";
	private static final String SCHEMA_DBO = "dbo";
	private static final String SERVER_TYPE_MS_SQL = "MS SQL";
	private static final String VENDOR_TYPE_STRING = "STRING";
	
	static String guidDB;


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
            ImportsTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        ImportsTest thisTest = new ImportsTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceWiki());

        // prepare repository
        RepositoryService repositoryClient = thisTest.getRepositoryServiceClient(serverName, userId, serverPlatformRootURL, TESTCASENAME);
        
		try {
			repositoryClient.cleanRepository();
        	repositoryClient.createDatabaseEntity("FirstDB", "DB2", "11.3");	// to test two DBs in the response
        	EntityDetail entityDB = repositoryClient.createDatabaseEntity(DATABASE_GOSALES, SERVER_TYPE_MS_SQL, "1.0");
        	guidDB = entityDB.getGUID();
	    	EntityDetail entitySchema = repositoryClient.createDatabaseSchemaEntity(guidDB, SCHEMA_DBO);
        	repositoryClient.createDatabaseSchemaEntity(entityDB.getGUID(), "system");	// to test two schemas in the response

			EntityDetail entityTableDate = repositoryClient.createSchemaTable(entitySchema, TBL_DATE);
			repositoryClient.addColumn(entityTableDate, COL_DATE, TIMESTAMP, DATETIME, 1);
			EntityDetail entityTableLocation = repositoryClient.createSchemaTable(entitySchema, TBL_LOCATION);
			repositoryClient.addColumn(entityTableLocation, COL_DESTINATION, DATA_TYPE_VARCHAR, VENDOR_TYPE_STRING, 1);
			EntityDetail entityTableA = repositoryClient.createSchemaTable(entitySchema, TBL_PRODUCT);
			repositoryClient.addColumn(entityTableA, COL_PRODUCT_NAME, DATA_TYPE_VARCHAR, VENDOR_TYPE_STRING, 1);
		} catch (InvalidParameterException | RepositoryErrorException | TypeErrorException | PropertyErrorException
				| ClassificationErrorException | StatusNotSupportedException | FunctionNotSupportedException
				| UserNotAuthorizedException | EntityNotKnownException | EntityProxyOnlyException | PagingErrorException
				| EntityNotDeletedException e) {
            throw new FVTUnexpectedCondition(TESTCASENAME, "Test Setup failed", e);

		}

        thisTest.testGetDatabases(repositoryClient, serverPlatformRootURL, serverName, userId, auditLog);
        thisTest.testGetSchemas(repositoryClient, serverPlatformRootURL, serverName, userId, auditLog);
        thisTest.testGetTables(repositoryClient, serverPlatformRootURL, serverName, userId, auditLog);
        thisTest.testGetModule(repositoryClient, serverPlatformRootURL, serverName, userId, auditLog);
        
    }


    /**
     * Get list of databases.
     *
     * @param repositoryClient to setup repository for the test
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetDatabases(RepositoryService repositoryClient, String serverPlatformRootURL,
                              String   serverName,  String   userId, AuditLog auditLog) throws FVTUnexpectedCondition
    {
        String activityName = "testGetDatabases";

        ImportClient client = this.getImportClient(serverName, serverPlatformRootURL, auditLog, TESTCASENAME);

        try
        {
            List<ResponseContainerDatabase> retrievedElement = client.getDatabases(userId, 0, 0);
            
            if (retrievedElement == null) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName + "(no database found)");
            }
            if (retrievedElement.size() != 2) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName + "(two databases expected)");
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        }
    }

    /**
     * Get list of database schemas.
     *
     * @param repositoryClient to setup repository for the test
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetSchemas(RepositoryService repositoryClient, String serverPlatformRootURL,
                              String   serverName,  String   userId, AuditLog auditLog) throws FVTUnexpectedCondition
    {
        String activityName = "testGetSchemas";

        ImportClient client = this.getImportClient(serverName, serverPlatformRootURL, auditLog, TESTCASENAME);

        try
        {
            List<ResponseContainerDatabaseSchema> retrievedElement = client.getSchemas(userId, guidDB, 0, 0);
            
            if (retrievedElement == null) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName + "(no schemas for database found)");
            }
            if (retrievedElement.size() != 2) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName + "(two schemas for databases expected)");
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        }
    }
    
    /**
     * Get list of tables for database schemas.
     *
     * @param repositoryClient to setup repository for the test
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetTables(RepositoryService repositoryClient, String serverPlatformRootURL,
                              String serverName, String userId, AuditLog auditLog) throws FVTUnexpectedCondition
    {
        String activityName = "testGetTables";

        ImportClient client = this.getImportClient(serverName, serverPlatformRootURL, auditLog, TESTCASENAME);

        try
        {
           
            ResponseContainerSchemaTables retrievedElement = client.getTables(userId, guidDB, "GOSALES", "dbo");
            
            if (retrievedElement == null) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName + "(no tables for database schema found)");
            }
            if (retrievedElement.getTablesList().size() != 3) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName + "(three tables for database schema expected)");
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        }
    }
    
    /**
     * Get module for database schemas.
     *
     * @param repositoryClient to setup repository for the test
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetModule(RepositoryService repositoryClient, String serverPlatformRootURL,
                              String serverName, String userId, AuditLog auditLog) throws FVTUnexpectedCondition
    {
        String activityName = "testGetModule";

        ImportClient client = this.getImportClient(serverName, serverPlatformRootURL, auditLog, TESTCASENAME);

        try
        {
    		// test table filter include
    		ModuleTableFilter filter = new ModuleTableFilter();
    		filter.getMeta().setIncludedTables(Arrays.asList(TBL_DATE, TBL_LOCATION));
            ResponseContainerModule retrievedElement = client.getModule(userId, guidDB, DATABASE_GOSALES, SCHEMA_DBO, filter);
            
            if (retrievedElement == null) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName + "(failed to generate module)");
            }
            if (retrievedElement.getPhysicalModule().getDataSource().get(0).getTable().size() != 2) {
                throw new FVTUnexpectedCondition(TESTCASENAME, activityName + "(two tables should pass the filter.)");
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(TESTCASENAME, activityName, unexpectedError);
        }
    }
}
