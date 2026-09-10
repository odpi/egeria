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
 * This class represents a single data quality assertion evaluated against an input dataset.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataQualityAssertionsInputDataSetFacetAssertions
{
    private String              assertion = null;
    private boolean             success = false;
    private String              column = null;
    private String              severity = null;
    private String              name = null;
    private String              description = null;
    private String              expected = null;
    private String              actual = null;
    private String              content = null;
    private String              contentType = null;
    private Map<String, Object> params = null;


    /**
     * Default constructor
     */
    public OpenLineageDataQualityAssertionsInputDataSetFacetAssertions()
    {
    }


    /**
     * Return the type of expectation test that the column is subjected to, for example not_null or unique.
     *
     * @return string
     */
    public String getAssertion()
    {
        return assertion;
    }


    /**
     * Set up the type of expectation test that the column is subjected to, for example not_null or unique.
     *
     * @param assertion string
     */
    public void setAssertion(String assertion)
    {
        this.assertion = assertion;
    }


    /**
     * Return the whether the assertion succeeded.
     *
     * @return boolean
     */
    public boolean isSuccess()
    {
        return success;
    }


    /**
     * Set up the whether the assertion succeeded.
     *
     * @param success boolean
     */
    public void setSuccess(boolean success)
    {
        this.success = success;
    }


    /**
     * Return the column that the assertion applies to, if it is column-level rather than dataset-level.
     *
     * @return string
     */
    public String getColumn()
    {
        return column;
    }


    /**
     * Set up the column that the assertion applies to, if it is column-level rather than dataset-level.
     *
     * @param column string
     */
    public void setColumn(String column)
    {
        this.column = column;
    }


    /**
     * Return the configured consequence of the assertion failing, for example error or warn.
     *
     * @return string
     */
    public String getSeverity()
    {
        return severity;
    }


    /**
     * Set up the configured consequence of the assertion failing, for example error or warn.
     *
     * @param severity string
     */
    public void setSeverity(String severity)
    {
        this.severity = severity;
    }


    /**
     * Return the name identifying the assertion.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name identifying the assertion.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the human-readable description of what the assertion checks.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the human-readable description of what the assertion checks.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the expected value or threshold, serialized as a string.
     *
     * @return string
     */
    public String getExpected()
    {
        return expected;
    }


    /**
     * Set up the expected value or threshold, serialized as a string.
     *
     * @param expected string
     */
    public void setExpected(String expected)
    {
        this.expected = expected;
    }


    /**
     * Return the actual value observed, serialized as a string.
     *
     * @return string
     */
    public String getActual()
    {
        return actual;
    }


    /**
     * Set up the actual value observed, serialized as a string.
     *
     * @param actual string
     */
    public void setActual(String actual)
    {
        this.actual = actual;
    }


    /**
     * Return the assertion body, for example a SQL query or expression.
     *
     * @return string
     */
    public String getContent()
    {
        return content;
    }


    /**
     * Set up the assertion body, for example a SQL query or expression.
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
        return "OpenLineageDataQualityAssertionsInputDataSetFacetAssertions{" +
                       "assertion='" + assertion + '\'' +
                       ", success=" + success +
                       ", column='" + column + '\'' +
                       ", severity='" + severity + '\'' +
                       ", name='" + name + '\'' +
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
        OpenLineageDataQualityAssertionsInputDataSetFacetAssertions that = (OpenLineageDataQualityAssertionsInputDataSetFacetAssertions) objectToCompare;
        return Objects.equals(assertion, that.assertion) &&
                       success == that.success &&
                       Objects.equals(column, that.column) &&
                       Objects.equals(severity, that.severity) &&
                       Objects.equals(name, that.name) &&
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
        return Objects.hash(assertion, success, column, severity, name, description, expected, actual, content, contentType, params);
    }
}
