/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.digitalarchitecture.client.ReferenceDataManager;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.rest.DigitalArchitectureRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * InvalidParameterTest calls each non-constructor method with a series of null or invalid parameters.
 * It ensures that InvalidParameterException is thrown.
 */
public class InvalidParameterTest
{
    private final static String testCaseName       = "InvalidParameterTest";

    private final static int    maxPageSize        = 100;


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
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceWiki());

        thisTest.testValidValuesManagerClient(serverName, serverPlatformRootURL, userId, auditLog);
    }



    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testValidValuesManagerClient(String   serverName,
                                              String   serverPlatformRootURL,
                                              String   userId,
                                              AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "testValidValuesManagerClient";

        try
        {
            DigitalArchitectureRESTClient restClient = new DigitalArchitectureRESTClient(serverName, serverPlatformRootURL);
            ReferenceDataManager          client     = new ReferenceDataManager(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);

            testCreateValidValuesSet(userId, client);
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
     * Test combinations of invalid parameters passed to createValidValueSet.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateValidValuesSet(String             userId,
                                          ReferenceDataManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateValidValuesSet";

        try
        {
            testCreateValidValuesSetNoUserId(client);
            testCreateValidValuesSetNoProperties(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createValidValueSet.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateValidValuesSetNoUserId(ReferenceDataManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateValidValuesSetNoUserId";

        try
        {
            client.createValidValueSet(null, null, null,null, null,null, null, null, null);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null properties passed to createValidValueSet.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateValidValuesSetNoProperties(ReferenceDataManager client,
                                                      String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateFileSystemNoProperties";

        try
        {
            client.createValidValueSet(userId, null, null,null, null,null, null, null, null);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }
}
