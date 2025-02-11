/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.fvt.clientconstructors;


import org.odpi.openmetadata.accessservices.governanceprogram.client.*;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;


/**
 * ClientConstructorTest provides the methods to verify that all of the clients can be constructed with
 * or without security
 */
public class ClientConstructorTest
{
    private final static String testCaseName   = "ClientConstructorTest";

    private final static String serverUserId   = "TestNPA";
    private final static String serverPassword = "TestNPAPassword";
    private final static int    maxPageSize    = 100;

    /**
     * Run all of the defined tests and capture the results.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @return  results of running test
     */
    public static FVTResults performFVT(String   serverName,
                                        String   serverPlatformRootURL)
    {
        FVTResults results = new FVTResults(testCaseName);

        results.incrementNumberOfTests();
        try
        {
            ClientConstructorTest.runIt(serverPlatformRootURL, serverName, results.getAuditLogDestination());
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
     * @param auditLogDestination logging destination
     * @throws InvalidParameterException one of the tests failed
     */
    private static void runIt(String                 serverPlatformRootURL,
                              String                 serverName,
                              FVTAuditLogDestination auditLogDestination) throws InvalidParameterException
    {
        ClientConstructorTest thisTest = new ClientConstructorTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceWiki());

        thisTest.testCertificationManagerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testExternalReferenceManagerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testClassificationLevelClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testGovernanceDefinitionManagerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testGovernanceMetricsManagerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testGovernanceProgramReviewManagerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testGovernanceRoleManagerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testGovernanceZoneManagerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testRightsManagerClient(serverName, serverPlatformRootURL, auditLog);
        thisTest.testSubjectAreaManagerClient(serverName, serverPlatformRootURL, auditLog);
    }


    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testGovernanceRoleManagerClient(String   serverName,
                                                 String   serverPlatformRootURL,
                                                 AuditLog auditLog) throws InvalidParameterException
    {
        new GovernanceRoleManager(serverName, serverPlatformRootURL, 100, auditLog);
        new GovernanceRoleManager(serverName, serverPlatformRootURL);
        new GovernanceRoleManager(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100, auditLog);
        new GovernanceRoleManager(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL);

        new GovernanceRoleManager(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }


    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testGovernanceDefinitionManagerClient(String   serverName,
                                                       String   serverPlatformRootURL,
                                                       AuditLog auditLog) throws InvalidParameterException
    {
        new GovernanceDefinitionManager(serverName, serverPlatformRootURL, 100, auditLog);
        new GovernanceDefinitionManager(serverName, serverPlatformRootURL);
        new GovernanceDefinitionManager(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100, auditLog);
        new GovernanceDefinitionManager(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL);

        new GovernanceDefinitionManager(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }


    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testGovernanceMetricsManagerClient(String   serverName,
                                                    String   serverPlatformRootURL,
                                                    AuditLog auditLog) throws InvalidParameterException
    {
        new GovernanceMetricsManager(serverName, serverPlatformRootURL, 100, auditLog);
        new GovernanceMetricsManager(serverName, serverPlatformRootURL);
        new GovernanceMetricsManager(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100, auditLog);
        new GovernanceMetricsManager(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL);

        new GovernanceMetricsManager(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }



    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testGovernanceProgramReviewManagerClient(String   serverName,
                                                          String   serverPlatformRootURL,
                                                          AuditLog auditLog) throws InvalidParameterException
    {
        new GovernanceProgramReviewManager(serverName, serverPlatformRootURL, 100, auditLog);
        new GovernanceProgramReviewManager(serverName, serverPlatformRootURL);
        new GovernanceProgramReviewManager(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100, auditLog);
        new GovernanceProgramReviewManager(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL);

        new GovernanceProgramReviewManager(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }



    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testGovernanceZoneManagerClient(String   serverName,
                                                 String   serverPlatformRootURL,
                                                 AuditLog auditLog) throws InvalidParameterException
    {
        new GovernanceZoneManager(serverName, serverPlatformRootURL, 100, auditLog);
        new GovernanceZoneManager(serverName, serverPlatformRootURL);
        new GovernanceZoneManager(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100, auditLog);
        new GovernanceZoneManager(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL);

        new GovernanceZoneManager(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }


    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testSubjectAreaManagerClient(String   serverName,
                                              String   serverPlatformRootURL,
                                              AuditLog auditLog) throws InvalidParameterException
    {
        new SubjectAreaManager(serverName, serverPlatformRootURL, 100, auditLog);
        new SubjectAreaManager(serverName, serverPlatformRootURL);
        new SubjectAreaManager(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100, auditLog);
        new SubjectAreaManager(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL);

        new SubjectAreaManager(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }


    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testCertificationManagerClient(String   serverName,
                                                String   serverPlatformRootURL,
                                                AuditLog auditLog) throws InvalidParameterException
    {
        new CertificationManager(serverName, serverPlatformRootURL, 100, auditLog);
        new CertificationManager(serverName, serverPlatformRootURL);
        new CertificationManager(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100, auditLog);
        new CertificationManager(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL);

        new CertificationManager(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }


    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testRightsManagerClient(String   serverName,
                                         String   serverPlatformRootURL,
                                         AuditLog auditLog) throws InvalidParameterException
    {
        new RightsManager(serverName, serverPlatformRootURL, 100, auditLog);
        new RightsManager(serverName, serverPlatformRootURL);
        new RightsManager(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100, auditLog);
        new RightsManager(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL);

        new RightsManager(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }


    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testExternalReferenceManagerClient(String   serverName,
                                                    String   serverPlatformRootURL,
                                                    AuditLog auditLog) throws InvalidParameterException
    {
        new ExternalReferenceManager(serverName, serverPlatformRootURL, 100, auditLog);
        new ExternalReferenceManager(serverName, serverPlatformRootURL);
        new ExternalReferenceManager(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100, auditLog);
        new ExternalReferenceManager(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL);

        new ExternalReferenceManager(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }


    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    private void testClassificationLevelClient(String   serverName,
                                               String   serverPlatformRootURL,
                                               AuditLog auditLog) throws InvalidParameterException
    {
        new GovernanceClassificationLevelManager(serverName, serverPlatformRootURL, 100, auditLog);
        new GovernanceClassificationLevelManager(serverName, serverPlatformRootURL);
        new GovernanceClassificationLevelManager(serverName, serverPlatformRootURL, serverUserId, serverPassword, 100, auditLog);
        new GovernanceClassificationLevelManager(serverName, serverPlatformRootURL, serverUserId, serverPassword);

        GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL);

        new GovernanceClassificationLevelManager(serverName, serverPlatformRootURL, restClient, maxPageSize);
    }
}
