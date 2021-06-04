/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceTestLabSummary;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TestLabReportResponse defines the response structure that includes the test results.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TestLabSummaryResponse extends ConformanceServicesAPIResponse
{
    private static final long     serialVersionUID = 1L;

    private OpenMetadataConformanceTestLabSummary testLabSummary = null;

    /**
     * Default constructor
     */
    public TestLabSummaryResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TestLabSummaryResponse(TestLabSummaryResponse template)
    {
        super(template);

        if (template != null)
        {
            testLabSummary = template.getTestLabSummary();
        }
    }


    /**
     * Return the summarized results from running the tests.
     *
     * @return results report
     */
    public OpenMetadataConformanceTestLabSummary getTestLabSummary()
    {
        return testLabSummary;
    }


    /**
     * Set up the results from running the tests.
     *
     * @param testLabSummary summarized results report
     */
    public void setTestLabSummary(OpenMetadataConformanceTestLabSummary testLabSummary)
    {
        this.testLabSummary = testLabSummary;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TestLabSummaryResponse{" +
                "testLabSummary=" + testLabSummary +
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
