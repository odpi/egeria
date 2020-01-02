/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataConformanceTestLabResults is a bean for collating the results from a specific test lab.
 * A test lab contains multiple workbenches.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataConformanceTestLabResults extends OpenMetadataConformanceTestResults
{
    private static final long     serialVersionUID = 1L;

    private Date   testRunDate   = new Date();

    private List<OpenMetadataConformanceWorkbenchResults> testResultsFromWorkbenches = null;

    /**
     * Default constructor
     */
    public OpenMetadataConformanceTestLabResults()
    {
        super();
    }


    /**
     * Return the number of test cases run.
     *
     * @return int count
     */
    public int getTestCaseCount()
    {
        if (testResultsFromWorkbenches == null)
        {
            return 0;
        }
        else if (testResultsFromWorkbenches.isEmpty())
        {
            return 0;
        }
        else
        {
            int  testResultCount = 0;

            for (OpenMetadataConformanceWorkbenchResults  testResultFromOneWorkbench : testResultsFromWorkbenches)
            {
                testResultCount = testResultCount + testResultFromOneWorkbench.getTestCaseCount();
            }

            return testResultCount;
        }
    }


    /**
     * Return the number of test cases passed.
     *
     * @return int count
     */
    public int getTestPassCount()
    {
        if (testResultsFromWorkbenches == null)
        {
            return 0;
        }
        else if (testResultsFromWorkbenches.isEmpty())
        {
            return 0;
        }
        else
        {
            int  testResultCount = 0;

            for (OpenMetadataConformanceWorkbenchResults  testResultFromOneWorkbench : testResultsFromWorkbenches)
            {
                testResultCount = testResultCount + testResultFromOneWorkbench.getTestPassCount();
            }

            return testResultCount;
        }
    }


    /**
     * Return the number of test cases failed.
     *
     * @return int count
     */
    public int getTestFailedCount()
    {
        if (testResultsFromWorkbenches == null)
        {
            return 0;
        }
        else if (testResultsFromWorkbenches.isEmpty())
        {
            return 0;
        }
        else
        {
            int  testResultCount = 0;

            for (OpenMetadataConformanceWorkbenchResults  testResultFromOneWorkbench : testResultsFromWorkbenches)
            {
                testResultCount = testResultCount + testResultFromOneWorkbench.getTestFailedCount();
            }

            return testResultCount;
        }
    }


    /**
     * Return the number of test cases skipped.
     *
     * @return int count
     */
    public int getTestSkippedCount()
    {
        if (testResultsFromWorkbenches == null)
        {
            return 0;
        }
        else if (testResultsFromWorkbenches.isEmpty())
        {
            return 0;
        }
        else
        {
            int  testResultCount = 0;

            for (OpenMetadataConformanceWorkbenchResults  testResultFromOneWorkbench : testResultsFromWorkbenches)
            {
                testResultCount = testResultCount + testResultFromOneWorkbench.getTestSkippedCount();
            }

            return testResultCount;
        }
    }


    /**
     * Return date that the test was run.
     *
     * @return date object
     */
    public Date getTestRunDate()
    {
        return testRunDate;
    }


    /**
     * Set up the date of the test.
     *
     * @param testRunDate date object
     */
    public void setTestRunDate(Date testRunDate)
    {
        this.testRunDate = testRunDate;
    }


    /**
     * Return the list of test results from each workbench.
     *
     * @return list of workbench results
     */
    public List<OpenMetadataConformanceWorkbenchResults> getTestResultsFromWorkbenches()
    {
        return testResultsFromWorkbenches;
    }


    /**
     * Set up the list of test results from each workbench.
     *
     * @param testResultsFromWorkbenches list of workbench results
     */
    public void setTestResultsFromWorkbenches(List<OpenMetadataConformanceWorkbenchResults> testResultsFromWorkbenches)
    {
        this.testResultsFromWorkbenches = testResultsFromWorkbenches;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataConformanceTestLabResults{" +
                "testRunDate=" + testRunDate +
                ", testResultsFromWorkbenches=" + testResultsFromWorkbenches +
                ", testCaseCount=" + getTestCaseCount() +
                ", testPassCount=" + getTestPassCount() +
                ", testFailedCount=" + getTestFailedCount() +
                ", testSkippedCount=" + getTestSkippedCount() +
                '}';
    }
}
