/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCaseResult;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TestCaseReportResponse defines the response structure that includes the results from a single test case.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TestCaseReportResponse extends ConformanceServicesAPIResponse
{
    private static final long     serialVersionUID = 1L;

    private OpenMetadataTestCaseResult testCaseResult = null;

    /**
     * Default constructor
     */
    public TestCaseReportResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TestCaseReportResponse(TestCaseReportResponse template)
    {
        super(template);

        if (template != null)
        {
            testCaseResult = template.getTestCaseResult();
        }
    }


    /**
     * Return the results from running a single test case.
     *
     * @return results report
     */
    public OpenMetadataTestCaseResult getTestCaseResult()
    {
        return testCaseResult;
    }


    /**
     * Set up the results from running a single test case.
     *
     * @param testCaseResult results report
     */
    public void setTestCaseResult(OpenMetadataTestCaseResult testCaseResult)
    {
        this.testCaseResult = testCaseResult;
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
                "testCaseResult=" + testCaseResult +
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
