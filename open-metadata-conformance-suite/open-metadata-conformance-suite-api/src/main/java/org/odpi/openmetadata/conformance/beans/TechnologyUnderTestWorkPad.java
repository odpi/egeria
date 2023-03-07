/* SPDX-License-Identifier: Apache-2.0 */
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
     *
     * @param workbenchWorkPads result work pads for the requested workbenches
     */
    public TechnologyUnderTestWorkPad(List<OpenMetadataConformanceWorkbenchWorkPad>  workbenchWorkPads)
    {
        this.workbenchWorkPads = workbenchWorkPads;
        this.testLabResults.setTestRunDate(new Date());
    }


    /**
     * Requests the list of all profiles (names) registered across all workpads.
     *
     * @return the list of all profiles across all workpads
     */
    public List<String> getProfileNames()
    {
        List<String> profileNames = new ArrayList<>();
        if (workbenchWorkPads != null)
        {
            for (OpenMetadataConformanceWorkbenchWorkPad workbenchWorkPad : workbenchWorkPads)
            {
                if (workbenchWorkPad != null)
                {
                    profileNames.addAll(workbenchWorkPad.getProfileNames());
                }
            }
        }
        return profileNames;
    }


    /**
     * Requests the list of all test cases (IDs) registered across all workpads.
     *
     * @return the list of all test case IDs across all workpads
     */
    public List<String> getTestCaseIds()
    {
        List<String> testCaseIds = new ArrayList<>();
        if (workbenchWorkPads != null)
        {
            for (OpenMetadataConformanceWorkbenchWorkPad workbenchWorkPad : workbenchWorkPads)
            {
                if (workbenchWorkPad != null)
                {
                    testCaseIds.addAll(workbenchWorkPad.getTestCaseIds());
                }
            }
        }
        return testCaseIds;
    }


    /**
     * Requests detailed information on the execution of a specific profile.
     * Null is returned if the profile has not run.
     *
     * @param profileName of the profile for which to obtain a detailed report.
     * @return Profile Report
     * @throws InvalidParameterException the profile name is not known
     */
    public OpenMetadataConformanceProfileResults getProfileReport(String profileName) throws InvalidParameterException
    {
        final String   methodName    = "getProfileReport";
        final String   parameterName = "profileName";

        if (workbenchWorkPads != null)
        {
            for (OpenMetadataConformanceWorkbenchWorkPad workBenchWorkPad : workbenchWorkPads)
            {
                if (workBenchWorkPad != null)
                {
                    return workBenchWorkPad.getProfileResults(profileName);
                }
            }
        }

        /*
         * None of the workbenches know about this profile name.
         */
        ConformanceSuiteErrorCode errorCode    = ConformanceSuiteErrorCode.UNKNOWN_PROFILE_NAME;
        String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(profileName);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                parameterName);
    }


    /**
     * Requests detailed information on the execution of a specific test case.
     * Null is returned if the test case has not run.
     *
     * @param testCaseId of the test case for which to obtain a detailed report.
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
     * Requests (completion) status of a specific workbench.
     *
     * @param workbenchId identifier of the workbench.
     * @return OpenMetadataConformanceWorkbenchStatus or
     * @throws InvalidParameterException the  workbenchId is not known.
     */
    public OpenMetadataConformanceWorkbenchStatus getWorkbenchStatus(String   workbenchId) throws InvalidParameterException
    {
        final String   methodName    = "getWorkbenchStatus";
        final String   parameterName = "workbenchId";

        if (workbenchWorkPads != null)
        {
            for (OpenMetadataConformanceWorkbenchWorkPad workBenchWorkPad : workbenchWorkPads)
            {
                if (workBenchWorkPad != null)
                {
                    if (workBenchWorkPad.getWorkbenchId().equals(workbenchId))
                    {
                        return workBenchWorkPad.getWorkbenchStatus();
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
     * Request a full report on the conformance of the technology under test.
     *
     * @return test lab results
     */
    public OpenMetadataConformanceTestLabSummary getTestLabSummary()
    {
        List<OpenMetadataConformanceWorkbenchSummary> workbenchSummaries = new ArrayList<>();

        if (workbenchWorkPads != null)
        {
            for (OpenMetadataConformanceWorkbenchWorkPad workBenchWorkPad : workbenchWorkPads)
            {
                if (workBenchWorkPad != null)
                {
                    workbenchSummaries.add(workBenchWorkPad.getWorkbenchSummary());
                }
            }
        }

        OpenMetadataConformanceTestLabSummary summary = new OpenMetadataConformanceTestLabSummary();
        if (! workbenchSummaries.isEmpty())
        {
            summary.setTestSummariesFromWorkbenches(workbenchSummaries);
        }

        return summary;
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
