/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTestWorkbenchResults provides a bean for storing the results of an
 * open metadata compliance test workbench.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTestWorkbenchResults extends OpenMetadataTestResults
{
    protected List<OpenMetadataTestCaseResult>  passedTestCases  = null;
    protected List<OpenMetadataTestCaseResult>  failedTestCases  = null;
    protected List<OpenMetadataTestCaseSummary> skippedTestCases = null;


    /**
     * Count the test case results in the list - dealing with nulls and empty lists.
     *
     * @param testCaseResults - list of results.
     * @return count
     */
    private int countTestCaseSummaries(List<OpenMetadataTestCaseSummary>  testCaseResults)
    {
        if (testCaseResults == null)
        {
            return 0;
        }
        else
        {
            return testCaseResults.size();
        }
    }


    /**
     * Count the test case results in the list - dealing with nulls and empty lists.
     *
     * @param testCaseResults - list of results.
     * @return count
     */
    private int countTestCaseResults(List<OpenMetadataTestCaseResult>  testCaseResults)
    {
        if (testCaseResults == null)
        {
            return 0;
        }
        else
        {
            return testCaseResults.size();
        }
    }


    /**
     * Default constructor
     */
    public OpenMetadataTestWorkbenchResults()
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
        return (this.countTestCaseResults(passedTestCases) +
                this.countTestCaseResults(failedTestCases) +
                this.countTestCaseSummaries(skippedTestCases));
    }


    /**
     * Return the number of test cases passed.
     *
     * @return int count
     */
    public int getTestPassCount()
    {
        return this.countTestCaseResults(passedTestCases);
    }


    /**
     * Return the number of test cases failed.
     *
     * @return int count
     */
    public int getTestFailedCount()
    {
        return this.countTestCaseResults(failedTestCases);
    }


    /**
     * Return the number of test cases skipped.
     *
     * @return int count
     */
    public int getTestSkippedCount()
    {
        return this.countTestCaseSummaries(skippedTestCases);
    }

    public List<OpenMetadataTestCaseResult> getPassedTestCases()
    {
        return passedTestCases;
    }

    public void setPassedTestCases(List<OpenMetadataTestCaseResult> passedTestCases)
    {
        this.passedTestCases = passedTestCases;
    }

    public List<OpenMetadataTestCaseResult> getFailedTestCases()
    {
        return failedTestCases;
    }

    public void setFailedTestCases(List<OpenMetadataTestCaseResult> failedTestCases)
    {
        this.failedTestCases = failedTestCases;
    }

    public List<OpenMetadataTestCaseSummary> getSkippedTestCases()
    {
        return skippedTestCases;
    }

    public void setSkippedTestCases(List<OpenMetadataTestCaseSummary> skippedTestCases)
    {
        this.skippedTestCases = skippedTestCases;
    }
}
