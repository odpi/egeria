/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceTestLabResults;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TestLabReportResponse defines the response structure that includes the test results.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TestLabReportResponse extends ConformanceServicesAPIResponse
{
    private static final long     serialVersionUID = 1L;

    private OpenMetadataConformanceTestLabResults testLabResults = null;

    /**
     * Default constructor
     */
    public TestLabReportResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TestLabReportResponse(TestLabReportResponse template)
    {
        super(template);

        if (template != null)
        {
            testLabResults = template.getTestLabResults();
        }
    }


    /**
     * Return the results from running the tests.
     *
     * @return results report
     */
    public OpenMetadataConformanceTestLabResults getTestLabResults()
    {
        return testLabResults;
    }


    /**
     * Set up the results from running the tests.
     *
     * @param testLabResults results report
     */
    public void setTestLabResults(OpenMetadataConformanceTestLabResults testLabResults)
    {
        this.testLabResults = testLabResults;
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
                "testLabResults=" + testLabResults +
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
