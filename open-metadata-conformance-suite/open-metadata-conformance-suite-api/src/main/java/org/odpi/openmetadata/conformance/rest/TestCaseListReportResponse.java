/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCaseResult;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TestCaseListReportResponse defines the response structure that includes a list of test case results.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TestCaseListReportResponse extends ConformanceServicesAPIResponse
{
    private static final long     serialVersionUID = 1L;

    private List<OpenMetadataTestCaseResult> testCaseResults = null;

    /**
     * Default constructor
     */
    public TestCaseListReportResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TestCaseListReportResponse(TestCaseListReportResponse template)
    {
        super(template);

        if (template != null)
        {
            testCaseResults = template.getTestCaseResults();
        }
    }


    /**
     * Return the results from running a single test case.
     *
     * @return results report
     */
    public List<OpenMetadataTestCaseResult> getTestCaseResults()
    {
        return testCaseResults;
    }


    /**
     * Set up the results from running a single test case.
     *
     * @param testCaseResults results report
     */
    public void setTestCaseResults(List<OpenMetadataTestCaseResult> testCaseResults)
    {
        this.testCaseResults = testCaseResults;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TestLabReportResponse{" +
                "testCaseResults=" + testCaseResults +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", successMessage='" + getSuccessMessage() + '\'' +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }
}
