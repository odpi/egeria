/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTestCaseSummary provides basic properties of a test case for use in reporting.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
                @JsonSubTypes.Type(value = OpenMetadataTestCaseResult.class, name = "OpenMetadataTestCaseResult")
              })
public class OpenMetadataTestCaseSummary implements Serializable
{
    private static final long   serialVersionUID = 1L;

    private String testCaseId             = null;
    private String testCaseName           = null;
    private String testCaseDescriptionURL = null;

    /**
     * Default constructor
     */
    public OpenMetadataTestCaseSummary()
    {
    }


    /**
     * Constructor to create a test case summary from a test case.
     *
     * @param testCase test case object.
     */
    public OpenMetadataTestCaseSummary(OpenMetadataTestCase testCase)
    {
        this.testCaseId = testCase.getTestCaseId();
        this.testCaseName = testCase.getTestCaseName();
        this.testCaseDescriptionURL = testCase.getTestCaseDescriptionURL();
    }


    /**
     * Return the unique id of the test case.
     *
     * @return string id
     */
    public String getTestCaseId()
    {
        return testCaseId;
    }


    /**
     * Set up the unique id of the test case.
     *
     * @param testCaseId string id
     */
    public void setTestCaseId(String testCaseId)
    {
        this.testCaseId = testCaseId;
    }


    /**
     * Return the name of the test case.
     *
     * @return string name
     */
    public String getTestCaseName()
    {
        return testCaseName;
    }


    /**
     * Set up the name of the test case.
     *
     * @param testCaseName string name
     */
    public void setTestCaseName(String testCaseName)
    {
        this.testCaseName = testCaseName;
    }


    /**
     * Return the URL that links to the test case description.
     *
     * @return string url
     */
    public String getTestCaseDescriptionURL()
    {
        return testCaseDescriptionURL;
    }


    /**
     * Set up the URL that links to the test case description.
     *
     * @param testCaseDescriptionURL string url
     */
    public void setTestCaseDescriptionURL(String testCaseDescriptionURL)
    {
        this.testCaseDescriptionURL = testCaseDescriptionURL;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataTestCaseSummary{" +
                "testCaseId='" + testCaseId + '\'' +
                ", testCaseName='" + testCaseName + '\'' +
                ", testCaseDescriptionURL='" + testCaseDescriptionURL + '\'' +
                '}';
    }
}
