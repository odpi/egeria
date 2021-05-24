/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.fvt.discovery;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateDatabaseTest calls the DatabaseManagerClient to create a database with schemas tables and columns
 * and then retrieve the results.
 */
public class CreateDiscoveryReportTest
{
    private final static String testCaseName       = "CreateDatabaseTest";

    private final static int    maxPageSize        = 100;

    /*
     * The database manager name is constant - the guid is created as part of the test.
     */
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
        CreateDiscoveryReportTest thisTest = new CreateDiscoveryReportTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceWiki());


    }


}
