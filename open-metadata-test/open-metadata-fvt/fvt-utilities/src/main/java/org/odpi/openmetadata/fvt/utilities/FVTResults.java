/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.fvt.utilities;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecord;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;

import java.util.ArrayList;
import java.util.List;

/**
 * FVTResults holds the results from running a number of FVT tests.
 */
public class FVTResults
{
    private String                 testCaseName        = null;
    private int                    numberOfTests       = 0;
    private int                    numberOfSuccesses   = 0;
    private List<Exception>        capturedErrors      = new ArrayList<>();
    private FVTAuditLogDestination auditLogDestination = new FVTAuditLogDestination();


    /**
     * Set up the results for a specific test case
     *
     * @param testCaseName unique name for test
     */
    public FVTResults(String testCaseName)
    {
        this.testCaseName = testCaseName;
    }


    /**
     * Increment the test count.
     */
    synchronized public void incrementNumberOfTests()
    {
        numberOfTests ++;
    }


    /**
     * Increment the number of successful tests.
     */
    synchronized public void incrementNumberOfSuccesses()
    {
        numberOfSuccesses ++;
    }


    /**
     * Save an unexpected exception that occurred during the tests.
     *
     * @param exception unexpected exception
     */
    synchronized public void addCapturedError(Exception exception)
    {
        capturedErrors.add(exception);
    }


    /**
     * Retrieve the number of test run.
     *
     * @return int
     */
    synchronized public int getNumberOfTests()
    {
        return numberOfTests;
    }


    /**
     * Retrieve the number of successful tests (should match the number of tests).
     *
     * @return int
     */
    synchronized public int getNumberOfSuccesses()
    {
        return numberOfSuccesses;
    }


    /**
     * Return the list of captured exceptions (should be empty).
     *
     * @return list of exceptions
     */
    synchronized public List<Exception> getCapturedErrors()
    {
        return capturedErrors;
    }


    /**
     * Return the audit log for the client.
     *
     * @return destination containing all of the log records.
     */
    synchronized public FVTAuditLogDestination getAuditLogDestination()
    {
        return auditLogDestination;
    }


    /**
     * Return whether the results are successful or not
     *
     * @return boolean flag
     */
    synchronized public boolean isSuccessful()
    {
        return (numberOfTests == numberOfSuccesses);
    }

    /**
     * Print out results.
     *
     * @param serverName calling server
     */
    synchronized public void printResults(String serverName)
    {
        System.out.println("==========================================");

        if ((this.isSuccessful()) && (capturedErrors.isEmpty()))
        {
            System.out.println(testCaseName + " FVT (" + serverName + ") ran successfully");
        }
        else
        {
            int numberOfFailedTests = numberOfTests - numberOfSuccesses;

            System.out.println(testCaseName + " FVT (" + serverName + ") failed");

            System.out.println("Number of Failed tests: " + numberOfFailedTests);
            System.out.println("Captured Exceptions: " + capturedErrors.size());

            for (Exception exception : capturedErrors)
            {
                System.out.println(" ==> " + exception.getClass().getName() + " with message " + exception.getMessage());
                System.out.println(exception.toString());
                exception.printStackTrace();
            }
        }

        System.out.println("Captured AuditLog");
        int recordId = 0;
        for (AuditLogRecord logRecord : auditLogDestination.getAuditLogRecords())
        {
            System.out.println("_______________________________________________________________");
            System.out.println("[" + recordId + "] " + logRecord.toString());
            System.out.println("_______________________________________________________________");
        }
    }
}
