/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.fvt.engines;


import org.odpi.openmetadata.accessservices.governanceengine.client.rest.GovernanceEngineRESTClient;

import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceEngineElement;
import org.odpi.openmetadata.accessservices.governanceengine.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateEngineTest calls the EngineManagerClient to create a database with schemas tables and columns
 * and then retrieve the results.
 */
public class CreateEngineTest
{
    private final static String testCaseName       = "CreateEngineTest";

    private final static int    maxPageSize        = 100;

    /*
     * The database manager name is constant - the guid is created as part of the test.
     */
    private final static String databaseManagerName            = "TestEngineManager";
    private final static String databaseManagerDisplayName     = "EngineManager displayName";
    private final static String databaseManagerDescription     = "EngineManager description";
    private final static String databaseManagerTypeDescription = "EngineManager type";
    private final static String databaseManagerVersion         = "EngineManager version";

    private final static String databaseName        = "TestEngine";
    private final static String databaseDisplayName = "Engine displayName";
    private final static String databaseDescription = "Engine description";
    private final static String databaseType        = "Engine type";
    private final static String databaseVersion     = "Engine version";

    private final static String databaseSchemaName        = "TestEngineSchema";
    private final static String databaseSchemaDisplayName = "EngineSchema displayName";
    private final static String databaseSchemaDescription = "EngineSchema description";
    private final static String databaseSchemaZone        = "EngineSchema Zone";
    private final static String databaseSchemaOwner       = "EngineSchema Owner";

    private final static String databaseTableName        = "TestEngineTable";
    private final static String databaseTableDisplayName = "EngineTable displayName";
    private final static String databaseTableDescription = "EngineTable description";
    private final static String databaseTableType        = "EngineTable type";
    private final static String databaseTableVersion     = "EngineTable version";


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
            CreateEngineTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        CreateEngineTest thisTest = new CreateEngineTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceWiki());

        GovernanceEngineElement engineElement = null;

    }


}
