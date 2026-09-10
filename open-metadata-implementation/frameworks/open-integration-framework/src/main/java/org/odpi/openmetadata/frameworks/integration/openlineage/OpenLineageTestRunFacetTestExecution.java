/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the outcome of a single test executed during a run.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageTestRunFacetTestExecution
{
    private String              name = null;
    private String              status = null;
    private String              severity = null;
    private String              type = null;
    private String              description = null;
    private String              expected = null;
    private String              actual = null;
    private String              content = null;
    private String              contentType = null;
    private Map<String, Object> params = null;


    /**
     * Default constructor
     */
    public OpenLineageTestRunFacetTestExecution()
    {
    }


    /**
     * Return the name identifying the test.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name identifying the test.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the outcome of the test: pass, fail or skip.
     *
     * @return string
     */
    public String getStatus()
    {
        return status;
    }


    /**
     * Set up the outcome of the test: pass, fail or skip.
     *
     * @param status string
     */
    public void setStatus(String status)
    {
        this.status = status;
    }


    /**
     * Return the configured consequence of a test failure: error or warn.
     *
     * @return string
     */
    public String getSeverity()
    {
        return severity;
    }


    /**
     * Set up the configured consequence of a test failure: error or warn.
     *
     * @param severity string
     */
    public void setSeverity(String severity)
    {
        this.severity = severity;
    }


    /**
     * Return the classification of the test, for example not_null, unique, row_count, freshness or custom_sql.
     *
     * @return string
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the classification of the test, for example not_null, unique, row_count, freshness or custom_sql.
     *
     * @param type string
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the human-readable description of what the test checks.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the human-readable description of what the test checks.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the expected value or threshold for the test, serialized as a string.
     *
     * @return string
     */
    public String getExpected()
    {
        return expected;
    }


    /**
     * Set up the expected value or threshold for the test, serialized as a string.
     *
     * @param expected string
     */
    public void setExpected(String expected)
    {
        this.expected = expected;
    }


    /**
     * Return the actual value observed during the test, serialized as a string.
     *
     * @return string
     */
    public String getActual()
    {
        return actual;
    }


    /**
     * Set up the actual value observed during the test, serialized as a string.
     *
     * @param actual string
     */
    public void setActual(String actual)
    {
        this.actual = actual;
    }


    /**
     * Return the test body, for example a SQL query or expression.
     *
     * @return string
     */
    public String getContent()
    {
        return content;
    }


    /**
     * Set up the test body, for example a SQL query or expression.
     *
     * @param content string
     */
    public void setContent(String content)
    {
        this.content = content;
    }


    /**
     * Return the format of the content field, for example text/x-sql.
     *
     * @return string
     */
    public String getContentType()
    {
        return contentType;
    }


    /**
     * Set up the format of the content field, for example text/x-sql.
     *
     * @param contentType string
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }


    /**
     * Return the arbitrary key-value pairs giving check-specific inputs.
     *
     * @return map
     */
    public Map<String, Object> getParams()
    {
        return params;
    }


    /**
     * Set up the arbitrary key-value pairs giving check-specific inputs.
     *
     * @param params map
     */
    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageTestRunFacetTestExecution{" +
                       "name='" + name + '\'' +
                       ", status='" + status + '\'' +
                       ", severity='" + severity + '\'' +
                       ", type='" + type + '\'' +
                       ", description='" + description + '\'' +
                       ", expected='" + expected + '\'' +
                       ", actual='" + actual + '\'' +
                       ", content='" + content + '\'' +
                       ", contentType='" + contentType + '\'' +
                       ", params=" + params +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        OpenLineageTestRunFacetTestExecution that = (OpenLineageTestRunFacetTestExecution) objectToCompare;
        return Objects.equals(name, that.name) &&
                       Objects.equals(status, that.status) &&
                       Objects.equals(severity, that.severity) &&
                       Objects.equals(type, that.type) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(expected, that.expected) &&
                       Objects.equals(actual, that.actual) &&
                       Objects.equals(content, that.content) &&
                       Objects.equals(contentType, that.contentType) &&
                       Objects.equals(params, that.params);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, status, severity, type, description, expected, actual, content, contentType, params);
    }
}
