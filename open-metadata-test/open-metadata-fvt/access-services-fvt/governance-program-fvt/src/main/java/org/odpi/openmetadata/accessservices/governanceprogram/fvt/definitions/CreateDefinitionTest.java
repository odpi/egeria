/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.fvt.definitions;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;



/**
 * CreateDefinitionTest manages governance definitions.
 */
public class CreateDefinitionTest
{
    private final static String testCaseName       = "CreateDefinitionTest";

    private final static int    maxPageSize        = 100;

    /*
     * The database manager name is constant - the guid is created as part of the test.
     */
    private final static String databaseManagerName            = "TestDefinitionManager";
    private final static String databaseManagerDisplayName     = "DefinitionManager displayName";
    private final static String databaseManagerDescription     = "DefinitionManager description";
    private final static String databaseManagerTypeDescription = "DefinitionManager type";
    private final static String databaseManagerVersion         = "DefinitionManager version";

    private final static String databaseName        = "TestDefinition";
    private final static String databaseDisplayName = "Definition displayName";
    private final static String databaseDescription = "Definition description";
    private final static String databaseType        = "Definition type";
    private final static String databaseVersion     = "Definition version";

    private final static String databaseSchemaName        = "TestDefinitionSchema";
    private final static String databaseSchemaDisplayName = "DefinitionSchema displayName";
    private final static String databaseSchemaDescription = "DefinitionSchema description";
    private final static String databaseSchemaZone        = "DefinitionSchema Zone";
    private final static String databaseSchemaOwner       = "DefinitionSchema Owner";

    private final static String databaseTableName        = "TestDefinitionTable";
    private final static String databaseTableDisplayName = "DefinitionTable displayName";
    private final static String databaseTableDescription = "DefinitionTable description";
    private final static String databaseTableType        = "DefinitionTable type";
    private final static String databaseTableVersion     = "DefinitionTable version";


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
            CreateDefinitionTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        CreateDefinitionTest thisTest = new CreateDefinitionTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceWiki());

    }


}
