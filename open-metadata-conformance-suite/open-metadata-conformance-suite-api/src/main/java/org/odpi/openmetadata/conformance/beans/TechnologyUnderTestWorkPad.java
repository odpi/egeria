/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;

import org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteErrorCode;
import org.odpi.openmetadata.conformance.ffdc.exception.InvalidParameterException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TechnologyUnderTestWorkPad is the class used to aggregate information about a technology (typically a metadata
 * repository) that is being tested by the ODPi Egeria Conformance Suite.
 *
 * It is responsible for accumulating the results from the different test workbenches as they probe the REST API
 * and validate the events being published by the technology under test (TUT).
 */
public class TechnologyUnderTestWorkPad
{
    private OpenMetadataConformanceTestLabResults         testLabResults = new OpenMetadataConformanceTestLabResults();
    private List<OpenMetadataConformanceWorkbenchWorkPad> workbenchWorkPads;


    /**
     * Constructor
     */
    public TechnologyUnderTestWorkPad(List<OpenMetadataConformanceWorkbenchWorkPad>  workbenchWorkPads)
    {
        this.workbenchWorkPads = workbenchWorkPads;
        this.testLabResults.setTestRunDate(new Date());
    }


    /**
     * Requests detailed information on the execution of a specific test case.
     * Null is returned if the test case has not run.
     *
     * @param testCaseId technology under test server name.
     * @return Test Case Report
     * @throws InvalidParameterException the test case id is not known
     */
    public OpenMetadataTestCaseResult getTestCaseReport(String   testCaseId) throws InvalidParameterException
    {
        final String   methodName    = "getTestCaseReport";
        final String   parameterName = "testCaseId";

        if (workbenchWorkPads != null)
        {
            for (OpenMetadataConformanceWorkbenchWorkPad workBenchWorkPad : workbenchWorkPads)
            {
                if (workBenchWorkPad != null)
                {
                    try
                    {
                        return workBenchWorkPad.getTestCaseResult(testCaseId);
                    }
                    catch (InvalidParameterException notKnownException)
                    {
                        /*
                         * This workbench does not know about this test case.
                         */
                    }
                }
            }
        }

        /*
         * None of the workbenches know about this test case.
         */
        ConformanceSuiteErrorCode errorCode    = ConformanceSuiteErrorCode.UNKNOWN_TEST_CASE_ID;
        String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(testCaseId);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            parameterName);
    }


    /**
     * Requests detailed information on the execution of all of the failed test cases.
     *
     * @return list of failed test case results.

     */
    public List<OpenMetadataTestCaseResult> getFailedTestCaseReport()
    {
        List<OpenMetadataTestCaseResult>  failedTestCases = new ArrayList<>();

        if (workbenchWorkPads != null)
        {
            for (OpenMetadataConformanceWorkbenchWorkPad workBenchWorkPad : workbenchWorkPads)
            {
                if (workBenchWorkPad != null)
                {
                    List<OpenMetadataTestCaseResult>  failedTestCasesFromWorkbench = workBenchWorkPad.getFailedTestCases();

                    if (failedTestCasesFromWorkbench != null)
                    {
                        failedTestCases.addAll(failedTestCasesFromWorkbench);
                    }
                }
            }
        }

        if (failedTestCases.isEmpty())
        {
            return null;
        }
        else
        {
            return failedTestCases;
        }
    }


    /**
     * Requests information on the level of conformance discovered by a specific workbench.
     *
     * @param workbenchId identifier of the workbench.
     * @return WorkbenchReportResponse or
     * @throws InvalidParameterException the  workbenchId is not known.
     */
    public OpenMetadataConformanceWorkbenchResults getWorkbenchReport(String   workbenchId) throws InvalidParameterException
    {
        final String   methodName    = "getWorkbenchReport";
        final String   parameterName = "workbenchId";

        if (workbenchWorkPads != null)
        {
            for (OpenMetadataConformanceWorkbenchWorkPad workBenchWorkPad : workbenchWorkPads)
            {
                if (workBenchWorkPad != null)
                {
                    if (workBenchWorkPad.getWorkbenchId().equals(workbenchId))
                    {
                        return workBenchWorkPad.getWorkbenchResults();
                    }
                }
            }
        }

        ConformanceSuiteErrorCode errorCode    = ConformanceSuiteErrorCode.UNKNOWN_WORKBENCH_ID;
        String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(workbenchId);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            parameterName);
    }


    /**
     * Request a full report on the conformance of the technology under test.
     *
     * @return test lab results
     */
    public OpenMetadataConformanceTestLabResults getTestLabResults()
    {
        List<OpenMetadataConformanceWorkbenchResults> workbenchResults = new ArrayList<>();

        if (workbenchWorkPads != null)
        {
            for (OpenMetadataConformanceWorkbenchWorkPad workBenchWorkPad : workbenchWorkPads)
            {
                if (workBenchWorkPad != null)
                {
                    workbenchResults.add(workBenchWorkPad.getWorkbenchResults());
                }
            }
        }

        if (! workbenchResults.isEmpty())
        {
            testLabResults.setTestResultsFromWorkbenches(workbenchResults);
        }

        return testLabResults;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "TechnologyUnderTestWorkPad{" +
                "testLabResults=" + testLabResults +
                ", workbenchWorkPads=" + workbenchWorkPads +
                ", failedTestCaseReport=" + getFailedTestCaseReport() +
                '}';
    }
}
