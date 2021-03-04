/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataConformanceTestLabSummary is a bean for collating the summarized results from a specific test lab.
 * A test lab contains multiple workbenches.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataConformanceTestLabSummary implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private Date   testRunDate   = new Date();

    private List<OpenMetadataConformanceWorkbenchSummary> testSummariesFromWorkbenches = null;

    /**
     * Default constructor
     */
    public OpenMetadataConformanceTestLabSummary()
    {
        super();
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
     * Return the list of test summaries from each workbench.
     *
     * @return list of workbench summaries
     */
    public List<OpenMetadataConformanceWorkbenchSummary> getTestSummariesFromWorkbenches()
    {
        return testSummariesFromWorkbenches;
    }


    /**
     * Set up the list of test summaries from each workbench.
     *
     * @param testSummariesFromWorkbenches list of workbench summaries
     */
    public void setTestSummariesFromWorkbenches(List<OpenMetadataConformanceWorkbenchSummary> testSummariesFromWorkbenches)
    {
        this.testSummariesFromWorkbenches = testSummariesFromWorkbenches;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataConformanceTestLabSummary{" +
                "testRunDate=" + testRunDate +
                ", testSummariesFromWorkbenches=" + testSummariesFromWorkbenches +
                '}';
    }
}
