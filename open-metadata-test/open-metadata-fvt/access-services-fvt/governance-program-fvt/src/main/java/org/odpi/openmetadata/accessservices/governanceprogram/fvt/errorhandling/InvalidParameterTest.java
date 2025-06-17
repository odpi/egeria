/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.governanceprogram.client.*;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * InvalidParameterTest calls each non-constructor method with a series of null or invalid parameters.
 * It ensures that InvalidParameterException is thrown.
 */
public class InvalidParameterTest
{
    private final static String testCaseName = "InvalidParameterTest";

    private final static int    maxPageSize  = 100;


    /**
     * Run all of the defined tests and capture the results.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @return  results of running test
     */
    public static FVTResults performFVT(String serverName,
                                        String serverPlatformRootURL,
                                        String userId)
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
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceWiki());

        thisTest.testCertificationClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testExternalReferenceClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testGovernanceMetricsClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testGovernanceProgramReviewClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testGovernanceRoleClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testGovernanceZoneClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testLicenseClient(serverName, serverPlatformRootURL, userId, auditLog);
        thisTest.testSubjectAreaClient(serverName, serverPlatformRootURL, userId, auditLog);
    }



    /**
     * Create a client to test the invalid parameters.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGovernanceRoleClient(String   serverName,
                                          String   serverPlatformRootURL,
                                          String   userId,
                                          AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testGovernanceRoleClient";

        try
        {
            GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL, auditLog);
            GovernanceRoleManager       client     = new GovernanceRoleManager(serverName, serverPlatformRootURL, restClient, maxPageSize);

            new CreateRoleInvalidParameterTest(userId, client);
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
     * Create a client to test the invalid parameters.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGovernanceMetricsClient(String   serverName,
                                             String   serverPlatformRootURL,
                                             String   userId,
                                             AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testGovernanceMetricsClient";

        try
        {
            GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL, auditLog);
            GovernanceMetricsManager    client     = new GovernanceMetricsManager(serverName, serverPlatformRootURL, restClient, maxPageSize);

            new CreateGovernanceMetricInvalidParameterTest(userId, client);
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
     * Create a client to test the invalid parameters.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGovernanceProgramReviewClient(String   serverName,
                                                   String   serverPlatformRootURL,
                                                   String   userId,
                                                   AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testGovernanceProgramReviewClient";

        try
        {
            GovernanceProgramRESTClient    restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL, auditLog);
            GovernanceProgramReviewManager client     = new GovernanceProgramReviewManager(serverName, serverPlatformRootURL, restClient, maxPageSize);

            new GetGovernanceDefinitionsForDomainInvalidParameterTest(userId, client);
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
     * Create a client to test the invalid parameters.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGovernanceZoneClient(String   serverName,
                                          String   serverPlatformRootURL,
                                          String   userId,
                                          AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testGovernanceZoneClient";

        try
        {
            GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL, auditLog);
            GovernanceZoneManager       client     = new GovernanceZoneManager(serverName, serverPlatformRootURL, restClient, maxPageSize);

            new CreateZoneInvalidParameterTest(userId, client);
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
     * Create a client to test the invalid parameters.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testSubjectAreaClient(String   serverName,
                                       String   serverPlatformRootURL,
                                       String   userId,
                                       AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testSubjectAreaClient";

        try
        {
            GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL, auditLog);
            SubjectAreaManager          client     = new SubjectAreaManager(serverName, serverPlatformRootURL, restClient, maxPageSize);

            new CreateSubjectAreaInvalidParameterTest(userId, client);
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
     * Create a client to test the invalid parameters.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCertificationClient(String   serverName,
                                         String   serverPlatformRootURL,
                                         String   userId,
                                         AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testCertificationClient";

        try
        {
            GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL, auditLog);
            CertificationManager        client     = new CertificationManager(serverName, serverPlatformRootURL, restClient, maxPageSize);

            new CreateCertificationTypeInvalidParameterTest(userId, client);
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
     * Create a client to test the invalid parameters.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testLicenseClient(String   serverName,
                                   String   serverPlatformRootURL,
                                   String   userId,
                                   AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testLicenseClient";

        try
        {
            GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL, auditLog);
            RightsManager               client     = new RightsManager(serverName, serverPlatformRootURL, restClient, maxPageSize);

            new CreateLicenseTypeInvalidParameterTest(userId, client);
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
     * Create a client to test the invalid parameters.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testExternalReferenceClient(String   serverName,
                                             String   serverPlatformRootURL,
                                             String   userId,
                                             AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testExternalReferenceClient";

        try
        {
            GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL, auditLog);
            ExternalReferenceManager    client     = new ExternalReferenceManager(serverName, serverPlatformRootURL, restClient, maxPageSize);

            new CreateExternalReferenceInvalidParameterTest(userId, client);
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
}
