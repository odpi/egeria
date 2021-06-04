/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTestCaseResults is a bean for storing the result of a single test.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTestCaseResult extends OpenMetadataTestCaseSummary
{
    private static final long     serialVersionUID = 1L;

    private String              successMessage       = null;

    private Long                elapsedTime = null;

    private List<String>        successfulAssertions   = new ArrayList<>();
    private List<String>        unsuccessfulAssertions = new ArrayList<>();
    private List<String>        notSupportedAssertions = new ArrayList<>();

    private Map<String, Object> discoveredProperties   = null;

    private ExceptionBean       conformanceException  = null;

    /**
     * Default Constructor used when converting from JSON
     */
    public OpenMetadataTestCaseResult()
    {
        super();
    }


    /**
     * Constructor used when test cases are running since the superclass's properties can be
     * extracted from the test case.
     *
     * @param testCase running test
     */
    public OpenMetadataTestCaseResult(OpenMetadataTestCase   testCase)
    {
        super(testCase);
    }


    /**
     * Return the message to confirm the successful run of the test.  This property is null if the test failed.
     *
     * @return string message
     */
    public String getSuccessMessage()
    {
        return successMessage;
    }


    /**
     * Set up the message to confirm the successful run of the test.
     *
     * @param successMessage string message
     */
    public void setSuccessMessage(String successMessage)
    {
        this.successMessage = successMessage;
    }


    /**
     * Return the elapsed time that this test case executed.
     *
     * @return time in milliseconds
     */
    public Long getElapsedTime()
    {
        return elapsedTime;
    }


    /**
     * Set the elapsed time that this test case executed.
     *
     * @param elapsedTime time in milliseconds
     */
    public void setElapsedTime(Long elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }


    /**
     * Return the list of assertions that were true when the test ran.
     *
     * @return list of assertion messages
     */
    public List<String> getSuccessfulAssertions()
    {
        return successfulAssertions;
    }


    /**
     * Set up the list of assertions that were true when the test ran.
     *
     * @param successfulAssertions list of assertion messages
     */
    public void setSuccessfulAssertions(List<String> successfulAssertions)
    {
        this.successfulAssertions = successfulAssertions;
    }


    /**
     * Return the list of assertions that were false when the test ran.
     *
     * @return list of assertion messages
     */
    public List<String> getUnsuccessfulAssertions()
    {
        return unsuccessfulAssertions;
    }


    /**
     * Set up the list of assertions that were false when the test ran.
     *
     * @param unsuccessfulAssertions list of assertion messages
     */
    public void setUnsuccessfulAssertions(List<String> unsuccessfulAssertions)
    {
        this.unsuccessfulAssertions = unsuccessfulAssertions;
    }


    /**
     * Return the list of assertions for functions that correctly reported not supported.
     *
     * @return list of assertion messages
     */
    public List<String> getNotSupportAssertions()
    {
        return notSupportedAssertions;
    }


    /**
     * Set up the list of assertions for functions that correctly reported not supported.
     *
     * @param notSupportedAssertions list of assertion messages
     */
    public void setNotSupportedAssertions(List<String> notSupportedAssertions)
    {
        this.notSupportedAssertions = notSupportedAssertions;
    }


    /**
     * Return details of an unexpected exception that interrupted the test.
     *
     * @return exception bean
     */
    public ExceptionBean getConformanceException()
    {
        return conformanceException;
    }


    /**
     * Set up details of an unexpected exception that interrupted the test.
     *
     * @param conformanceException bean with exception properties
     */
    public void setConformanceException(ExceptionBean conformanceException)
    {
        this.conformanceException = conformanceException;
    }


    /**
     * Return the properties about the repository that were discovered during the test.
     *
     * @return property map
     */
    public Map<String, Object> getDiscoveredProperties()
    {
        return discoveredProperties;
    }


    /**
     * Set up the properties about the repository that were discovered during the test.
     *
     * @param discoveredProperties property map
     */
    public void setDiscoveredProperties(Map<String, Object> discoveredProperties)
    {
        this.discoveredProperties = discoveredProperties;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataTestCaseResult{" +
                "successMessage='" + successMessage + '\'' +
                ", elapsedTime='" + elapsedTime + '\'' +
                ", successfulAssertions=" + successfulAssertions +
                ", unsuccessfulAssertions=" + unsuccessfulAssertions +
                ", notSupportedAssertions=" + notSupportedAssertions +
                ", discoveredProperties=" + discoveredProperties +
                ", conformanceException=" + conformanceException +
                ", notSupportAssertions=" + getNotSupportAssertions() +
                ", testCaseId='" + getTestCaseId() + '\'' +
                ", testCaseName='" + getTestCaseName() + '\'' +
                ", testCaseDescriptionURL='" + getTestCaseDescriptionURL() + '\'' +
                '}';
    }
}
