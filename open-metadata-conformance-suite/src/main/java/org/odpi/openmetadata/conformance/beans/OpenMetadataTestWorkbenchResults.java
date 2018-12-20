/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.conformance.OpenMetadataTestWorkbench;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTestWorkbenchResults provides a bean for storing the results of an
 * open metadata conformance test workbench.  This includes the accumulated
 * results from each of the tests it runs.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTestWorkbenchResults extends OpenMetadataTestResults
{
    private String workbenchName             = null;
    private String versionNumber             = null;
    private String workbenchDocumentationURL = null;

    private List<OpenMetadataTestCaseResult>  passedTestCases  = null;
    private List<OpenMetadataTestCaseResult>  failedTestCases  = null;
    private List<OpenMetadataTestCaseSummary> skippedTestCases = null;


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
     * Default constructor - used when constructing from JSON.
     */
    public OpenMetadataTestWorkbenchResults()
    {
        super();
    }


    /**
     * Constructor used during a test run.
     *
     * @param workbench the workbench running the tests
     */
    public OpenMetadataTestWorkbenchResults(OpenMetadataTestWorkbench  workbench)
    {
        super();

        this.workbenchName = workbench.getWorkbenchName();
        this.versionNumber = workbench.getVersionNumber();
        this.workbenchDocumentationURL = workbench.getWorkbenchDocumentationURL();
    }


    /**
     * Return the name of the workbench
     *
     * @return string name
     */
    public String getWorkbenchName()
    {
        return workbenchName;
    }


    /**
     * Set up the name of the workbench
     *
     * @param workbenchName name
     */
    public void setWorkbenchName(String workbenchName)
    {
        this.workbenchName = workbenchName;
    }


    /**
     * Return the version number of the workbench.
     *
     * @return string version
     */
    public String getVersionNumber()
    {
        return versionNumber;
    }


    /**
     * Set up the version number of the workbench running the tests.
     *
     * @param versionNumber string version number
     */
    public void setVersionNumber(String versionNumber)
    {
        this.versionNumber = versionNumber;
    }


    /**
     * Return the url to the workbench documentation.
     *
     * @return string url
     */
    public String getWorkbenchDocumentationURL()
    {
        return workbenchDocumentationURL;
    }


    /**
     * Set up the url to the workbench documentation.
     *
     * @param workbenchDocumentationURL url
     */
    public void setWorkbenchDocumentationURL(String workbenchDocumentationURL)
    {
        this.workbenchDocumentationURL = workbenchDocumentationURL;
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


    /**
     * Return the list of test cases that passed.
     *
     * @return list of test cases
     */
    public List<OpenMetadataTestCaseResult> getPassedTestCases()
    {
        return passedTestCases;
    }


    /**
     * Set up the list of test cases that passed.
     *
     * @param passedTestCases list of test cases
     */
    public void setPassedTestCases(List<OpenMetadataTestCaseResult> passedTestCases)
    {
        this.passedTestCases = passedTestCases;
    }


    /**
     * Return the list of test cases that failed.
     *
     * @return list of test cases
     */
    public List<OpenMetadataTestCaseResult> getFailedTestCases()
    {
        return failedTestCases;
    }


    /**
     * Set up the list of test cases that failed.
     *
     * @param failedTestCases list of test cases
     */
    public void setFailedTestCases(List<OpenMetadataTestCaseResult> failedTestCases)
    {
        this.failedTestCases = failedTestCases;
    }


    /**
     * Return the list of test cases that were not run due to an earlier failure.
     *
     * @return list of test cases
     */
    public List<OpenMetadataTestCaseSummary> getSkippedTestCases()
    {
        return skippedTestCases;
    }


    /**
     * Set up the list of test cases that were not run sue to an earlier failure.
     *
     * @param skippedTestCases list of test cases
     */
    public void setSkippedTestCases(List<OpenMetadataTestCaseSummary> skippedTestCases)
    {
        this.skippedTestCases = skippedTestCases;
    }
}
