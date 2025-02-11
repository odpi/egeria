/* SPDX-License-Identifier: Apache-2.0 */
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
        CreateDefinitionTest thisTest = new CreateDefinitionTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceWiki());


    }

}
