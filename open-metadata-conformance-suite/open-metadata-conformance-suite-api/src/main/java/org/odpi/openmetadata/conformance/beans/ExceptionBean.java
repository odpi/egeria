/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ExceptionBean is used to capture an exception in JSON
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExceptionBean implements Serializable
{
    private static final long   serialVersionUID = 1L;

    private String              exceptionClassName     = null;
    private String              errorMessage           = null;
    private String              testCaseId             = null;
    private String              testCaseName           = null;
    private String              testCaseDescriptionURL = null;
    private Map<String, String> properties             = null;

    /**
     * Default constructor
     */
    public ExceptionBean()
    {
    }


    /**
     * Return the name of the class name for a caught exception.
     *
     * @return class name
     */
    public String getExceptionClassName()
    {
        return exceptionClassName;
    }


    /**
     * Set up the class name for the exception.
     *
     * @param exceptionClassName string name
     */
    public void setExceptionClassName(String exceptionClassName)
    {
        this.exceptionClassName = exceptionClassName;
    }


    /**
     * Return the error message associated with the exception.
     *
     * @return string message
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }


    /**
     * Set up the error message associated with the exception.
     *
     * @param errorMessage string message
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    /**
     * Return the unique identifier of the test case.
     *
     * @return string id
     */
    public String getTestCaseId()
    {
        return testCaseId;
    }


    /**
     * Set up the unique identifier of the test case.
     *
     * @param testCaseId string id
     */
    public void setTestCaseId(String testCaseId)
    {
        this.testCaseId = testCaseId;
    }


    /**
     * Return the display name of the test case.
     *
     * @return string name
     */
    public String getTestCaseName()
    {
        return testCaseName;
    }


    /**
     * Set up the display name of the test case.
     *
     * @param testCaseName string name
     */
    public void setTestCaseName(String testCaseName)
    {
        this.testCaseName = testCaseName;
    }


    /**
     * Return the URL of the description of the test case.
     *
     * @return string url
     */
    public String getTestCaseDescriptionURL()
    {
        return testCaseDescriptionURL;
    }


    /**
     * Set up the URL of the description of the test case.
     *
     * @param testCaseDescriptionURL string url
     */
    public void setTestCaseDescriptionURL(String testCaseDescriptionURL)
    {
        this.testCaseDescriptionURL = testCaseDescriptionURL;
    }


    /**
     * Return the properties providing more details about the exception.
     *
     * @return property map
     */
    public Map<String, String> getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties providing more details about the exception.
     *
     * @param properties property map
     */
    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }

    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "ExceptionBean{" +
                "exceptionClassName='" + exceptionClassName + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", testCaseId='" + testCaseId + '\'' +
                ", testCaseName='" + testCaseName + '\'' +
                ", testCaseDescriptionURL='" + testCaseDescriptionURL + '\'' +
                ", properties=" + properties +
                '}';
    }
}
