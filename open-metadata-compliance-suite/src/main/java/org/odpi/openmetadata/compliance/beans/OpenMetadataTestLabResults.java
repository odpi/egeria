/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.compliance.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTestLabResults is a bean for collating the results from running an open metadata compliance
 * test against a potential open metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTestLabResults extends OpenMetadataTestResults
{
    private Date   testRunDate   = new Date();
    private String serverName    = null;
    private String serverRootURL = null;

    private List<OpenMetadataTestWorkbenchResults> testResultsFromWorkbenches = null;


    /**
     * Default constructor
     */
    public OpenMetadataTestLabResults()
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

            for (OpenMetadataTestWorkbenchResults  testResultFromOneWorkbench : testResultsFromWorkbenches)
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

            for (OpenMetadataTestWorkbenchResults  testResultFromOneWorkbench : testResultsFromWorkbenches)
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

            for (OpenMetadataTestWorkbenchResults  testResultFromOneWorkbench : testResultsFromWorkbenches)
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

            for (OpenMetadataTestWorkbenchResults  testResultFromOneWorkbench : testResultsFromWorkbenches)
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
     * Return the name of the server.
     *
     * @return string server name
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Set up the name of the server.
     *
     * @param serverName string server name
     */
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }


    /**
     * Return the root url of the server to test.
     *
     * @return string URL
     */
    public String getServerRootURL()
    {
        return serverRootURL;
    }


    /**
     * Set up the root url of the server to test.
     *
     * @param serverRootURL string URL
     */
    public void setServerRootURL(String serverRootURL)
    {
        this.serverRootURL = serverRootURL;
    }


    /**
     * Return the list of test results from each workbench.
     *
     * @return list of workbench results
     */
    public List<OpenMetadataTestWorkbenchResults> getTestResultsFromWorkbenches()
    {
        return testResultsFromWorkbenches;
    }


    /**
     * Set up the list of test results from each workbench.
     *
     * @param testResultsFromWorkbenches list of workbench results
     */
    public void setTestResultsFromWorkbenches(List<OpenMetadataTestWorkbenchResults> testResultsFromWorkbenches)
    {
        this.testResultsFromWorkbenches = testResultsFromWorkbenches;
    }
}
