/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.common;

import org.odpi.openmetadata.accessservices.analyticsmodeling.client.AnalyticsModelingRestClient;
import org.odpi.openmetadata.accessservices.analyticsmodeling.client.ImportClient;
import org.odpi.openmetadata.accessservices.analyticsmodeling.client.SynchronizationClient;
import org.odpi.openmetadata.accessservices.analyticsmodeling.fvt.RepositoryService;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * AnalyticsModelingTestBase provides common functions for test cases.
 */
public class AnalyticsModelingTestBase
{
    protected final static int    maxPageSize        = 100;
    
    /**
     * Create and return a asset manager client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @param testCaseName name of calling test case
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    protected ImportClient getImportClient(String   serverName,
                                                       String   serverPlatformRootURL,
                                                       AuditLog auditLog,
                                                       String   testCaseName) throws FVTUnexpectedCondition
    {
        final String activityName = "getImportClient";

        try
        {
        	AnalyticsModelingRestClient restClient = new AnalyticsModelingRestClient(serverName, serverPlatformRootURL);

            return new ImportClient(serverName, serverPlatformRootURL, restClient, maxPageSize);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create and return a glossary exchange client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @param testCaseName name of calling test case
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    protected SynchronizationClient getSynchronizationClient(String   serverName,
                                                               String   serverPlatformRootURL,
                                                               AuditLog auditLog,
                                                               String   testCaseName) throws FVTUnexpectedCondition
    {
        final String activityName = "getSynchronizationClient";

        try
        {
        	AnalyticsModelingRestClient restClient = new AnalyticsModelingRestClient(serverName, serverPlatformRootURL);

            return new SynchronizationClient(serverName, serverPlatformRootURL, restClient, maxPageSize);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }
    
    /**
     * Create repository client.
     * @param serverName name of the server to connect to
     * @param userId to run queries.
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param testCaseName name of calling test case
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    protected RepositoryService getRepositoryServiceClient(String serverName, String userId, String serverPlatformRootURL, String testCaseName)
            		throws FVTUnexpectedCondition
    {
    	
        final String activityName = "getRepositoryServiceClient";

        try
        {
        	return new RepositoryService(serverName, userId, serverPlatformRootURL, activityName);

        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }

}
